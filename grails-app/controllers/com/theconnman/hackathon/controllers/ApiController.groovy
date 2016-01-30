package com.theconnman.hackathon.controllers

import grails.converters.JSON

class ApiController {

	def apiService

	def helloWorld() {
		Collection collectionSizeArray = JSON.parse(request.reader.text)
		Collection iterationArray = (1..collectionSizeArray.first().toInteger())
		render([data: iterationArray.collect {
			return 'Hello World'
		}] as JSON)
	}

	def anagram() {
		Collection stringPairs = JSON.parse(request.reader.text)
		render([data: stringPairs.collect {
			return apiService.isAnagram(it)
		}] as JSON)
	}
}
