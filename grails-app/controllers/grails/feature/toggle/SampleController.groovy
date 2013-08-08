package grails.feature.toggle

import grails.plugin.feature.toggle.annotations.Feature

@Feature(name='sample')
class SampleController {

	@Feature(name='featuredSample')
	def index() {
		withFeature("sample") { ->
			log.debug("the feature must be enabled")
		}
	}
}