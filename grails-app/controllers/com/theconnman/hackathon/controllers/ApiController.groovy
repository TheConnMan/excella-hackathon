package com.theconnman.hackathon.controllers

import grails.converters.JSON

class ApiController {

	def helloWorld() {
		Collection collectionSizeArray = JSON.parse(request.reader.text)
		Collection iterationArray = (1..collectionSizeArray.first().toInteger())
		render([data: iterationArray.collect {
			return 'Hello World'
		}] as JSON)
	}
}
