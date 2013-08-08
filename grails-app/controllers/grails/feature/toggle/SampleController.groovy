package grails.feature.toggle

import grails.plugin.feature.toggle.annotations.Feature

@Feature(name='controller')
class SampleController {

	@Feature(name='action')
	def index() {
    log.debug("Is Feature Enabled? ${featureEnabled('sample')}")
    model = [result: 'didn\'t run']
    withFeature('code') { ->
      log.debug('the feature must be enabled')
      model.result = 'ran with feature'
    }
    withoutFeature('code') { ->
      log.debug('the feature must not be enabled')
      model.result = 'ran without feature'
    }
    return result
	}
}