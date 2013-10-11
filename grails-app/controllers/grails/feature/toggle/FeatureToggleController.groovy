package grails.feature.toggle

import grails.plugin.featuretoggle.FeatureToggleService

class FeatureToggleController {

	FeatureToggleService featureToggleService

	def list = {
		def toggles = featureToggleService.allFeatures()

		withFormat {
			json {
				render(contentType: "application/json") {
					features {
						toggles.keySet().sort({ a, b -> a?.name <=> b?.name}).each { featureName ->
							feature(name: featureName, enabled: featureToggleService.isFeatureEnabled(featureName), description: features[featureName].description)
						}
					}
				}
			}

			html {
				render(view: 'index', model: [ features : toggles ])
			}
		}
	}

	def disable = {
		featureToggleService.setFeatureEnabled "${params.feature}", false
		log.debug featureToggleService.isFeatureEnabled("${params.feature}")
		redirect(action: "list")
	}

	def enable = {
   log.debug 'enabling feature'
		featureToggleService.disableDefaultOverride()
    featureToggleService.setFeatureEnabled "${params.feature}", true
    log.debug featureToggleService.isFeatureEnabled("${params.feature}")
		redirect(action: "list")
	}

	def download = {
		render(text: featureToggleService.allFeatures())
	}
}
