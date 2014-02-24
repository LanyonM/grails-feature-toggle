package grails.plugin.featuretoggle

//import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class ToggleTagLib {
	def featureToggleService
  static returnObjectForTags = ['hasFeature']

  /**
   * Only displays content if a feature is enabled, as configured in Config.groovy
   * under swapfish.features[:].
   * 
   * Clone of 'toggle'.  Please use this tag instead of 'toggle'.
   *
   * @attr feature REQUIRED the name of the feature
   */
  def withFeature = { attrs, body ->

    def feature = attrs.feature

    if(feature) {
      pageScope.lastFeatureToggle = feature
    } else {
      feature = pageScope.lastFeatureToggle
    }

    if(featureToggleService.isFeatureEnabled(feature)) {
      out << body()
    }
  }

  /**
   * Only displays content if a feature is NOT enabled, as configured in Config.groovy
   * under swapfish.features[:].
   *
   * @attr feature REQUIRED the name of the feature
   */
  def withoutFeature = { attrs, body ->

    def feature = attrs.feature

    if(feature) {
      pageScope.lastFeatureToggle = feature
    } else {
      feature = pageScope.lastFeatureToggle
    }


    if(!featureToggleService.isFeatureEnabled(feature)) {
      out << body()
    }
  }
  
  /**
   * Only displays content if a feature is enabled, as configured in Config.groovy
   * under swapfish.features[:].
   * 
   * This method is confusingly named, since it does not 'toggle' anything and in fact does not change the program state in any way.  
   * It is also the same as the injected groovy method 'withFeature'.  Therefore, please use the tag 'withFeature' instead.  'withFeature' is a clone of this tag.
   *
   * @attr feature REQUIRED the name of the feature
   */
  @Deprecated
  def toggle = withFeature

  /**
   * Evaluates to true when a feature is enabled, false otherwise.
   *
   * @attr feature REQUIRED the name of the feature
   */
  def hasFeature = { attrs ->
    return featureToggleService.isFeatureEnabled(attrs.feature)
  }
  /**
   * Evaluates to 'true' (a String, not a boolean) when a feature is enabled, 'false' (again, a String, not a boolean) otherwise.
   *
   * Note: this tag returns a String, not a boolean.
   *
   * @attr feature REQUIRED the name of the feature
   */
  @Deprecated
  def featureEnabled = { attrs ->
    out << featureToggleService.isFeatureEnabled(attrs.feature) ? 'true' : 'false'
  }
}
