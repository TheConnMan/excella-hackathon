package com.theconnman.hackathon.services

import grails.transaction.Transactional

@Transactional
class ApiService {

	Collection fibonacciCache = [0, 1]
	Map fromNumeralMap = [
		'I': [
			value: 1,
			subtractFrom: ['V', 'X']
		],
		'V': [
			value: 5,
			subtractFrom: []
		],
		'X': [
			value: 10,
			subtractFrom: ['L', 'C']
		],
		'L': [
			value: 50,
			subtractFrom: []
		],
		'C': [
			value: 100,
			subtractFrom: ['D', 'M']
		],
		'D': [
			value: 500,
			subtractFrom: []
		],
		'M': [
			value: 1000,
			subtractFrom: []
		]
	]
	Collection toNumeralKey = [
		['I', 'V'],
		['X', 'L'],
		['C', 'D'],
		['M']
	]

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

	def fizzBuzz(int number) {
		if (number % 6 == 0) {
			return 'FizzBuzz'
		}
		if (number % 2 == 0) {
			return 'Fizz'
		}
		if (number % 3 == 0) {
			return 'Buzz'
		}
		return number
	}

	boolean isPrime(int number) {
		if (number < 4) {
			return true
		}
		int squareRoot = Math.floor(Math.sqrt(number))
		for (int i = 2; i <= squareRoot; i++) {
			if (number % i == 0) {
				return false
			}
		}
		return true
	}

	Collection sexyPrimes(int number) {
		Collection primes = getPrimes(number)
		Collection sexyPrimes = []
		primes.each {
			if ((it + 6) in primes) {
				sexyPrimes.push([it, it + 6])
			}
		}
		return sexyPrimes
	}

	Collection getPrimes(int number) {
		return (2..number).grep {
			return isPrime(it)
		}
	}

	Collection primeFactors(double number) {
		if (number == 0 || number > 1000000000) {
			return []
		}
		Collection factors = []
		while (number != 1) {
			int nextFactor = getNextFactor(number)
			factors.push(nextFactor)
			number = number / nextFactor
		}
		return factors
	}

	int getNextFactor(double number) {
		if (number < 4) {
			return number
		}
		for (int i = 2; i <= number; i++) {
			if (number % i == 0) {
				return i
			}
		}
	}

	int fromRoman(String numeral) {
		int index = 0
		int sum = 0
		while (index < numeral.size()) {
			Map result = parseNumeral(numeral[index], numeral.size() > index + 1 ? numeral[index + 1] : null)
			sum += result.num
			index += result.increment
		}
		return sum
	}

	Map parseNumeral(String currentNum, String nextNum) {
		Map currentMap = fromNumeralMap[currentNum]
		if (!nextNum || !(nextNum in currentMap.subtractFrom)) {
			return [num: currentMap.value, increment: 1]
		}
		Map nextMap = fromNumeralMap[nextNum]
		return [num: nextMap.value - currentMap.value, increment: 2]
	}

	String toRoman(int number) {
		String numeral = ''
		if (number >= 1000) {
			numeral += (1..(number / 1000)).collect { 'M' }.join('')
		}
		String string = (number % 1000).toString()
		for (int index = 0; index < string.size(); index++) {
			int power = string.size() - index - 1
			int num = string[index].toInteger()
			if (num != 0) {
				if (num < 4) {
					numeral += (1..num).collect { toNumeralKey[power][0] }.join('')
				} else if (num == 4) {
					numeral += toNumeralKey[power][0] + toNumeralKey[power][1]
				} else if (num == 5) {
					numeral += toNumeralKey[power][1]
				} else if (num < 9) {
					numeral += toNumeralKey[power][1] + (6..num).collect { toNumeralKey[power][0] }.join('')
				} else {
					numeral += toNumeralKey[power][0] + toNumeralKey[power + 1][0]
				}
			}
		}
		return numeral
	}

	int bowling(Collection bowls) {
		int score = 0
		boolean firstThrow = true
		String last = ''
		String lastLast = ''
		int frame = 1
		for (int index = 0; index < bowls.size(); index++) {
			if ((last == 'strike' || last == 'spare') && frame < 12) {
				score += bowls[index]
			}
			if (lastLast == 'strike') {
				score += bowls[index]
			}
			if (frame < 11) {
				score += bowls[index]
			}
			lastLast = last
			if (firstThrow && bowls[index] == 10) { // Strike
				last = 'strike'
				frame++
			} else if (!firstThrow && bowls[index - 1] + bowls[index] == 10) { // Spare
				last = 'spare'
				firstThrow = true
				frame ++
			} else { // Other
				last = ''
				firstThrow = !firstThrow
				frame += !firstThrow ? 1 : 0
			}
		}
		return score
	}
}
