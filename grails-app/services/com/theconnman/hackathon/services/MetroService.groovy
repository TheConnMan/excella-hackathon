package com.theconnman.hackathon.services

import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class MetroService {

	def grailsApplication

	Map getJson() {
		return JSON.parse(grailsApplication.mainContext.getResource('data/wmat.json').file.text)
	}
}
