package grails.feature.toggle

import grails.plugin.feature.toggle.annotations.Feature

@Feature(name='sample')
class SampleController {

	@Feature(name='featuredSample')
	def index() {
    log.debug("Is Feature Enabled? ${featureEnabled('sample')}")
    withFeature('sample') { ->
      log.debug('the feature must be enabled')
    }
    withoutFeature('sample') { ->
      log.debug('the feature must not be enabled')
    }
	}
}