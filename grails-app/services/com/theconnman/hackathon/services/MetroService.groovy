package com.theconnman.hackathon.services

import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.transaction.Transactional

@Transactional
class MetroService {

	def grailsApplication
	static final WMAT_API_ROOT = 'https://api.wmata.com/Rail.svc/json/'
	static final REAL_TIME_API_ROOT = 'https://excellathon.herokuapp.com/wmata/StationPrediction.svc/json/GetPrediction/'
	static final MAPS_API_ROOT = 'http://maps.googleapis.com/maps/api/geocode/json?address='

	Map constructResponse(double lat, double lng, double radius, String destination) {
		sleep 400
		Collection<Map> stations = getNearbyStation(lat, lng, radius)
		Collection nextTrains = getNextTrains(stations*.Code)
		Map resp = [
			station: stations.size() != 0 ? stations.first().Name : '',
			stationLat: stations.size() != 0 ? stations.first().Lat : '',
			stationLong: stations.size() != 0 ? stations.first().Lon : '',
			departures: nextTrains
		]
		if (destination) {
			Map location = getLatLng(destination)
			Collection<Map> destinationStations = getNearbyStation(location.lat, location.lng, 600)
			Collection commonLines = getCommonLines(stations, destinationStations)
			Collection endsOnCommonLines = nextTrains.grep {
				it.line in commonLines
			}
			Collection startStationsWithLine = stations.grep { hasLines(it, commonLines) }
			if (startStationsWithLine.size() == 1) {
				resp.departures = endsOnCommonLines.grep { Map train ->
					Collection remainingStops = getRemainingStops(startStationsWithLine.first().Code, train.destinationCode)
					return (destinationStations*.Code ?: []).intersect(remainingStops*.StationCode ?: [])
				}
			}
		}
		resp.departures = resp.departures.collect {
			it.remove('destinationCode')
			return it
		}
		return resp
	}

	Collection<Map> getNearbyStation(double lat, double lng, double radius) {
		RestResponse resp = new RestBuilder().get(WMAT_API_ROOT + 'jStationEntrances?Lat=' + lat + '&Lon=' + lng + '&Radius=' + radius) {
			header 'api_key', grailsApplication.config.wmat.api.key
		}
		Map json = JSON.parse(resp.text)
		if (json.Entrances && json.Entrances.size() == 0) {
			return []
		}
		String stationCode = json.Entrances ? json.Entrances.first().StationCode1 : []
		Collection stations = getStations()
		Collection currentStations = stations.grep { Map station ->
			station.Code == stationCode || station.StationTogether1 == stationCode || station.StationTogether2 == stationCode
		}
		if (currentStations.size() == 0) {
			return []
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
				minutes: train.Min,
				destinationCode: train.DestinationCode
			]
		}
	}

	Map getLatLng(String address) {
		RestResponse resp = new RestBuilder().get(MAPS_API_ROOT + address)
		Map json = JSON.parse(resp.text)
		Collection results = json.results
		if (results.size() == 0) {
			return [:]
		}
		return results.first().geometry.location
	}

	Collection getRemainingStops(String startCode, String endCode) {
		RestResponse resp = new RestBuilder().get(WMAT_API_ROOT + 'jPath?FromStationCode=' + startCode + '&ToStationCode=' + endCode) {
			header 'api_key', grailsApplication.config.wmat.api.key
		}
		Map json = JSON.parse(resp.text)
		return json.Path
	}

	Collection getCommonLines(Collection startStations, Collection endStations) {
		return startStations.collectMany {
			return getStationLineCodes(it)
		}.intersect(endStations.collectMany {
			return getStationLineCodes(it)
		}).grep { it }
	}

	boolean hasLines(Map station, Collection lines) {
		Collection stationLines = getStationLineCodes(station)
		return lines.intersect(stationLines).size() != 0
	}

	Collection getStationLineCodes(Map station) {
		return [station.LineCode1, station.LineCode2, station.LineCode3, station.LineCode4].grep { it }
	}

	Collection getStations() {
		return JSON.parse(grailsApplication.mainContext.getResource('data/wmat.json').file.text).Stations
	}
}
