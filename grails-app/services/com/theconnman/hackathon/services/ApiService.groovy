package com.theconnman.hackathon.services

import grails.transaction.Transactional

@Transactional
class ApiService {

	Collection fibonacciCache = [0, 1]

	boolean isAnagram(Collection<String> stringPair) {
		return stringPair.first().split('').sort().join('') == stringPair.last().split('').sort().join('')
	}

	boolean isPalindrome(String word) {
		return word == word.reverse()
	}

	int fibonacci(int index) {
		if (fibonacciCache.size() > index) {
			return fibonacciCache[index]
		}
		int value = fibonacci(index - 2) + fibonacci(index - 1)
		fibonacciCache[index] = value
		return value
	}
}
