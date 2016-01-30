package com.theconnman.hackathon.services

import grails.transaction.Transactional

@Transactional
class ApiService {

	boolean isAnagram(Collection<String> stringPair) {
		return stringPair.first().split('').sort().join('') == stringPair.last().split('').sort().join('')
	}
}
