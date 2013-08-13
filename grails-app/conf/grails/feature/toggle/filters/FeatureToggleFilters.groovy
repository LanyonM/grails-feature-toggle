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

				if (annotation != null && !featureToggleService.isFeatureEnabled(annotation.name())) {
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
						if (methodAnnotation != null && !featureToggleService.isFeatureEnabled(methodAnnotation.name())) {
							if (methodAnnotation.responseRedirect().size() > 0) {
								redirect(uri: methodAnnotation.responseRedirect())
								return false
							} else {
								render(status: methodAnnotation.responseStatus())
								return false
							}
						}
					}
				}
			}
		}
	}
}
