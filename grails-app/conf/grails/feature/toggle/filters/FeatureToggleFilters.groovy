package grails.feature.toggle.filters
import grails.plugin.featuretoggle.FeatureToggleService
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

import grails.plugin.feature.toggle.annotations.Feature

import java.lang.reflect.Method

class FeatureToggleFilters implements ApplicationContextAware {

	FeatureToggleService featureToggleService
	ApplicationContext applicationContext

	def filters = {
		allControllers(controller:'*', action:'*') {
			before = {
				def artefact = grailsApplication.getArtefactByLogicalPropertyName('Controller', controllerName)
				def controller = applicationContext.getBean(artefact.clazz.name)
				def annotation = controller.class.getAnnotation(Feature)
				if (annotation != null && !passesTests(annotation)) {
					if (annotation.responseRedirect().size() > 0) {
						redirect(uri: annotation.responseRedirect())
						return false
					} else {
						render(status: annotation.responseStatus())
						return false
					}
				}

				Method[] methods = controller.class.getMethods().each { method ->
					if (method.name == actionName) {
						def methodAnnotation = method.getAnnotation(Feature)
						if (methodAnnotation != null && !passesTests(methodAnnotation)) {
							if (methodAnnotation.responseRedirect().size() > 0) {
								redirect(uri: methodAnnotation.responseRedirect())
								return false
							} else {
                response.sendError(404)
								return false
							}
						}
					}
				}
			}
		}
	}

  private boolean passesTests(annotation) {
    def name = annotation.name()
    if(name && name != Feature.NULL && !featureToggleService.isFeatureEnabled(annotation.name())) {
      return false
    }

    String[] with = annotation.with()
    for(def feature:with) {
      if(!featureToggleService.isFeatureEnabled(feature)) {
        return false
      }
    }

    String[] without = annotation.without()
    for(def feature:without) {
      if(featureToggleService.isFeatureEnabled(feature)) {
        return false
      }
    }

    return true
  }
}
