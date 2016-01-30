grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6

grails.project.dependency.resolver = "maven"

grails.project.dependency.resolution = {
	inherits("global") {
	}
	log "error"
	checksums true
	legacyResolve false

	repositories {
		inherits true

		grailsPlugins()
		grailsHome()
		grailsCentral()

		mavenLocal()
		mavenCentral()

		mavenRepo "http://repo.spring.io/milestone/"
		mavenRepo "http://download.java.net/maven/2/"
		mavenRepo "http://repo.grails.org/grails/core"
	}

	dependencies { }

	plugins {
		runtime ":hibernate4:4.3.8.1"
		compile ":asset-pipeline:2.3.2"
		compile ":scaffolding:2.1.2"

		build ":tomcat:7.0.55.2"
		compile ":spring-security-ui:1.0-RC2"
		compile ":spring-security-core:2.0-RC5"

		test ":codenarc:0.22"
		test ":code-coverage:2.0.3-3"
	}
}
codenarc.reports = {
	MyXmlReport('xml') {
		outputFile = 'target/CodeNarcReport.xml'
		title = 'XML Report'
	}


	MyHtmlReport('html') {
		outputFile = 'target/CodeNarcReport.html'
		title = 'HTML Report'
	}
}
codenarc.ruleSetFiles="file:test/CodeNarcRules.groovy"

codenarc.properties = {
	GrailsPublicControllerMethod.enabled = false
	EmptyIfStatement.priority = 1
}

codenarc.maxPriority1Violations = 50
codenarc.maxPriority2Violations = 100
codenarc.maxPriority3Violations = 100

codenarc.systemExitOnBuildException = false