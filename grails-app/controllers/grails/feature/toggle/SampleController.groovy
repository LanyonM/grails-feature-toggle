package grails.feature.toggle

import grails.plugin.feature.toggle.annotations.Feature

@Feature(name = 'controller')
class SampleController {

	@Feature(name = 'action')
	def index() {
		return [result: 'success!']
	}

  def toggleInside() {
    def ret = 'didn\'t run'

    withFeature('code') { ->
      log.debug('the feature must be enabled')
      ret = 'ran with feature'
    }

    withoutFeature('code') { ->
      log.debug('the feature must not be enabled')
      ret = 'ran without feature'
    }

    return [result: ret]
  }

  def useWithElseConstruct() {
    def ret = 'didn\'t run'

    withFeature('code', { ->
      log.debug('the feature must be enabled')
      ret = 'ran with feature'
    }, { ->
      log.debug('the feature must not be enabled')
      ret = 'ran without feature'
    })

    return [result: ret]
  }

  def useWithoutElseConstruct() {
    def ret = 'didn\'t run'

    withoutFeature('code', { ->
      log.debug('the feature must be enabled')
      ret = 'ran without feature'
    }, { ->
      log.debug('the feature must not be enabled')
      ret = 'ran with feature'
    })

    return [result: ret]
  }
}
