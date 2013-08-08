package grails.plugin.featuretoggle

import org.codehaus.groovy.grails.commons.GrailsApplication

class FeatureToggleService {

	GrailsApplication grailsApplication

	static boolean transactional = false

	def allFeatures() {
		grailsApplication.config.features.findAll { it.key != "disableAll" }
	}

	boolean featuresDisabled() {
		return grailsApplication.config.features?.disableAll == true && grailsApplication.config.features?.disableAll != false
	}

	void disableDefaultOverride() {
		grailsApplication.config.features?.disableAll = false
	}

  boolean isFeatureEnabled(String feature) {
    log.debug("checking to see if feature '${feature}' is enabled...")
    if(featuresDisabled()) {
      log.debug("All features are toggled off; returing false.")
      return false
    }

    boolean isFeatureEnabled = true

    def featureConfig = allFeatures()?."${feature}"

    if(featureConfig) {

      isFeatureEnabled = (featureConfig.enabled.getClass() == groovy.util.ConfigObject) ? true : featureConfig.enabled

      log.debug("Feature is ${isFeatureEnabled ? 'enabled' : 'disabled'}; returing ${isFeatureEnabled}.")
    } else {
      log.debug("Feature not found; returing true by default.")
    }

    return isFeatureEnabled
  }
  void setFeatureEnabled(String feature, boolean enabled) {
    allFeatures()?."${feature}" = enabled
  }

  public def withFeature = { String featureName, Closure closure, Closure otherwise ->
    if(isFeatureEnabled(featureName)) {
      closure.call()
    } else if(otherwise != null) {
      otherwise.call()
    }
  }
  public def withoutFeature = { String featureName, Closure closure, Closure otherwise ->
    if(!isFeatureEnabled(featureName)) {
      closure.call()
    } else if(otherwise != null) {
      otherwise.call()
    }
  }
  public def featureEnabled = { String featureName ->
    return isFeatureEnabled(featureName)
  }
  public void enhance(theClass) {
    theClass.metaClass.withFeature = withFeature
    theClass.metaClass.withoutFeature = withoutFeature
    theClass.metaClass.featureEnabled = featureEnabled
  }
}
