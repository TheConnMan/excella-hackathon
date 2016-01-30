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

	Map constructResponse(double lat, double lng, double radius) {
		Map station = getNearbyStation(lat, lng, radius)
		return [
			station: station?.Name,
			stationLat: station?.Lat,
			stationLong: station?.Lon
		]
	}

	Map getNearbyStation(double lat, double lng, double radius) {
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
			station.Code == stationCode
		}
		if (currentStations.size() == 0) {
			return [:]
		}
		return currentStations.first()
	}

	Collection getStations() {
		return JSON.parse(grailsApplication.mainContext.getResource('data/wmat.json').file.text).Stations
	}
}
