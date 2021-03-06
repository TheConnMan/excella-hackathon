def loc = ['../UserConfig.groovy', '/usr/share/tomcat8/webapps/ROOT/Jenkins.groovy'].grep { new File(it).exists() }.first();
def localConfig = new ConfigSlurper(grailsSettings.grailsEnv).parse(new File(loc).toURI().toURL())

grails.project.groupId = appName
grails.mime.file.extensions = true
grails.mime.use.accept.header = false
grails.mime.types = [
	all:			'*/*',
	atom:			'application/atom+xml',
	css:			'text/css',
	csv:			'text/csv',
	form:			'application/x-www-form-urlencoded',
	html:			['text/html','application/xhtml+xml'],
	js:				'text/javascript',
	json:			['application/json', 'text/json'],
	multipartForm:	'multipart/form-data',
	rss:			'application/rss+xml',
	text:			'text/plain',
	csv: 			'text/csv',
	pdf: 			'application/pdf',
	rtf: 			'application/rtf',
	excel: 			'application/vnd.ms-excel',
	ods: 			'application/vnd.oasis.opendocument.spreadsheet',
	hal:			['application/hal+json','application/hal+xml'],
	xml:			['text/xml', 'application/xml']
]

grails.views.default.codec = "none"
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.views.gsp.sitemesh.preprocess = true
grails.scaffolding.templates.domainSuffix = 'Instance'

grails.json.legacy.builder = false
grails.enable.native2ascii = true
grails.spring.bean.packages = []
grails.web.disable.multipart=false

grails.exceptionresolver.params.exclude = ['password']

grails.hibernate.cache.queries = false

wmat.api.key = localConfig.wmat.api.key

environments {
	development {
		grails.logging.jul.usebridge = true
	}
	devdeploy {
		grails.logging.jul.usebridge = false
	}
	production {
		grails.logging.jul.usebridge = false
		grails.assets.minifyCss = false
		grails.assets.minifyJs = false
	}
}

log4j = {
	appenders {
		console name: 'stdout', threshold: org.apache.log4j.Level.ERROR
		rollingFile name: 'info', file: 'logs/info.log', layout: pattern(conversionPattern: '[%p] %d{yyyy-MM-dd HH:mm:ss} %c{2} - %m%n'), threshold: org.apache.log4j.Level.INFO
		rollingFile name: 'warn', file: 'logs/warn.log', layout: pattern(conversionPattern: '[%p] %d{yyyy-MM-dd HH:mm:ss} %c{2} - %m%n'), threshold: org.apache.log4j.Level.WARN
	}

	warn 'warn': [
		'grails.app.controllers.com.theconnman.hackathon',
		'grails.app.services.com.theconnman.hackathon',
		'grails.app.conf.com.theconnman.hackathon',
		'grails.app.domain.com.theconnman.hackathon'
	]

	info 'info': [
		'grails.app.controllers.com.theconnman.hackathon',
		'grails.app.services.com.theconnman.hackathon',
		'grails.app.conf.com.theconnman.hackathon',
		'grails.app.domain.com.theconnman.hackathon'
	]

	off 'org.grails.plugin.resource.ResourceMeta'
}

grails.resources.resourceLocatorEnabled = true

grails.app.context="/"

grails.plugin.springsecurity.providerNames = [
	'daoAuthenticationProvider',
	'anonymousAuthenticationProvider',
	'rememberMeAuthenticationProvider'
]

grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.theconnman.hackathon.domains.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.theconnman.hackathon.domains.UserRole'
grails.plugin.springsecurity.authority.className = 'com.theconnman.hackathon.domains.Role'

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/**':								['permitAll']
]
