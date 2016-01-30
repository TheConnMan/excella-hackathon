package com.theconnman.hackathon.services

import java.util.Map;

import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.transaction.Transactional

@Transactional
class MetroService {

	def grailsApplication
	static final WMAT_API_ROOT = 'https://api.wmata.com/Rail.svc/json/'
	static final REAL_TIME_API_ROOT = 'https://excellathon.herokuapp.com/wmata/StationPrediction.svc/json/GetPrediction/'

	Map constructResponse(double lat, double lng, double radius) {
		Collection<Map> stations = getNearbyStation(lat, lng, radius)
		Collection nextTrains = getNextTrains(stations*.Code)
		return [
			station: stations?.first().Name,
			stationLat: stations?.first().Lat,
			stationLong: stations?.first().Lon,
			departures: nextTrains
		]
	}

	Collection<Map> getNearbyStation(double lat, double lng, double radius) {
		RestResponse resp = new RestBuilder().get(WMAT_API_ROOT + 'jStationEntrances?Lat=' + lat + '&Lon=' + lng + '&Radius=' + radius) {
			header 'api_key', grailsApplication.config.wmat.api.key
		}
		Map json = JSON.parse(resp.text)
		if (json.Entrances.size() == 0) {
			return [:]
		}
		String stationCode = json.Entrances.first().StationCode1
		Collection stations = getStations()
		Collection currentStations = stations.grep { Map station ->
			station.Code == stationCode || station.StationTogether1 == stationCode || station.StationTogether2 == stationCode
		}
		if (currentStations.size() == 0) {
			return [:]
		}
		return currentStations
	}

	Collection getNextTrains(Collection<String> stationCodes) {
		RestResponse resp = new RestBuilder().get(REAL_TIME_API_ROOT + stationCodes.join(','))
		Collection allTrains = JSON.parse(resp.text).Trains.grep { Map train ->
			return train.DestinationCode != null
		}
		return allTrains.collect { Map train ->
			return [
				destination: train.DestinationName,
				line: train.Line,
				minutes: train.Min
			]
		}
	}

	Collection getStations() {
		return JSON.parse(grailsApplication.mainContext.getResource('data/wmat.json').file.text).Stations
	}
}
