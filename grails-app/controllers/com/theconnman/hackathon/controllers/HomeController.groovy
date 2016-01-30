package com.theconnman.hackathon.controllers

class HomeController {

	@SuppressWarnings("EmptyMethod")
	def index() { }

	def health() {
		render(status: 200)
	}
}
