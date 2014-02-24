import grails.plugin.featuretoggle.FeatureToggleService

class FeatureToggleGrailsPlugin {
	// the plugin version
	def version = "0.20"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "1.3.7 > *"
	// the other plugins this plugin depends on
	def dependsOn = [:]
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
			"grails-app/views/error.gsp"
	]

	// TODO Fill in these fields
	def author = "Olivia Witt"
	def authorEmail = "oliviaw@criticalmass.com"
	def title = "Enable and disable controllers, actions, and blocks of code, using toggles which can be flipped at runtime."
	def description = '''\\

'''

	// URL to the plugin's documentation
	def documentation = "http://grails.org/plugin/grails-feature-toggle"

	def doWithWebDescriptor = { xml ->
		// TODO Implement additions to web.xml (optional), this event occurs before
	}

	def doWithSpring = {
		// TODO Implement runtime spring config (optional)
	}

	def doWithDynamicMethods = { ctx ->
		FeatureToggleService service = ctx.getBean('featureToggleService')
		for(controllerClass in application.controllerClasses) {
			service.enhance(controllerClass)
		}
		for(serviceClass in application.serviceClasses) {
			service.enhance(serviceClass)
		}
	}

	def doWithApplicationContext = { applicationContext ->
		// TODO Implement post initialization spring config (optional)
	}

	def onChange = { event ->
		doWithDynamicMethods event.ctx
	}

	def onConfigChange = { event ->
		// TODO Implement code that is executed when the project configuration changes.
		// The event is the same as for 'onChange'.
	}
}
