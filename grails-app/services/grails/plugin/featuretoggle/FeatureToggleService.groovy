package grails.plugin.featuretoggle

import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.plugin.feature.toggle.Feature

class FeatureToggleService {

	GrailsApplication grailsApplication

	static boolean transactional = false
  Map<String, Feature> features
  boolean disableAll

  void loadFeatures() {
    features = new HashMap<String, Feature>();
    grailsApplication.config.features.each { Map.Entry<String, ConfigObject> feature ->
      features.put(feature.getKey(), new Feature(feature));
    }
  }

	def allFeatures() {
    if(!features) {
      loadFeatures()
    }
    return features
	}

	boolean featuresDisabled() {
    if(!features) {
      loadFeatures()
    }
		return disableAll
	}

  void disableDefaultOverride() {
    disableAll = false
  }

  boolean isFeatureEnabled(String feature) {
    if(!features) {
      loadFeatures()
    }
    //log.debug("checking to see if feature '${feature}' is enabled...")
    if(disableAll) {
      //log.debug("All features are toggled off; returing false.")
      return false
    }

    boolean isFeatureEnabled = true

    def featureConfig = features[feature]

    if(featureConfig) {

      isFeatureEnabled = featureConfig.enabled

      //log.debug("Feature is ${isFeatureEnabled ? 'enabled' : 'disabled'}; returing ${isFeatureEnabled}.")
    } else {
      //log.debug("Feature not found; returing true by default.")
    }

    return isFeatureEnabled
  }
  void setFeatureEnabled(String feature, boolean enabled) {
    features[feature].enabled = enabled
  }

  public def withFeature = { String featureName, Closure... closure ->
    if(closure.length < 1 || closure.length > 2) {
      throw new IllegalArgumentException("withFeature takes one or two closures as an argument.  You gave ${closure.length}")
    }
    if(isFeatureEnabled(featureName)) {
      closure[0].call()
    } else if (closure.length > 1) {
      closure[1].call()
    }
  }
  public def withoutFeature = { String featureName, Closure... closure ->
    if(closure.length < 1 || closure.length > 2) {
      throw new IllegalArgumentException("withoutFeature takes one or two closures as an argument.  You gave ${closure.length}")
    }
    if(!isFeatureEnabled(featureName)) {
      closure[0].call()
    } else if(closure.length > 1) {
      closure[1].call()
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
  public void enhanceMock(mock) {
    int max = 2147483646
    mock.demand.withFeature(0..max, withFeature)
    mock.demand.withoutFeature(0..max, withoutFeature)
    mock.demand.featureEnabled(0..max, featureEnabled)
  }
}
