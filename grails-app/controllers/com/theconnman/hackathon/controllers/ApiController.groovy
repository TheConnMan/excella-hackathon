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

	def palindrome() {
		Collection stringPairs = JSON.parse(request.reader.text)
		render([data: stringPairs.collect {
			return apiService.isPalindrome(it)
		}] as JSON)
	}

	def fibonacci() {
		Collection input = JSON.parse(request.reader.text)
		render([data: input.collect {
			return apiService.fibonacci(it)
		}] as JSON)
	}

	def fizzBuzz() {
		Collection input = JSON.parse(request.reader.text)
		render([data: input.collect {
			return apiService.fizzBuzz(it)
		}] as JSON)
	}

	def prime() {
		Collection input = JSON.parse(request.reader.text)
		render([data: input.collect {
			return apiService.isPrime(it)
		}] as JSON)
	}

	def primeFactors() {
		Collection input = JSON.parse(request.reader.text)
		render([data: input.collect {
			return apiService.primeFactors(it)
		}] as JSON)
	}

	def fromRoman() {
		Collection input = JSON.parse(request.reader.text)
		render([data: input.collect {
			return apiService.fromRoman(it)
		}] as JSON)
	}
}
