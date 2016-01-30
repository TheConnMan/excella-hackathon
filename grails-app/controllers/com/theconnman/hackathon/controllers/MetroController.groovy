package com.theconnman.hackathon.controllers

import grails.converters.JSON

class MetroController {

	def metroService

	def index() {
		Map input = JSON.parse(request.reader.text)
		render(metroService.constructResponse(input.latitude.toDouble(), input.longitude.toDouble(), input.radius.toDouble()) as JSON)
	}
}
