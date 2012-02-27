package grails.feature.toggle.filters
import grails.plugin.featuretoggle.FeatureToggleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

class FeatureToggleFilters implements ApplicationContextAware {

	FeatureToggleService featureToggleService
	ApplicationContext applicationContext

	def filters = {
		allControllers(controller:'*', action:'*') {
			before = {
				def controllers = request.session.servletContext['controlledActions']

				if(controllers != null) {
					def curAction = actionName

					// In the case of the action not being specified (default action) we need to look up
					// if it was configured in the controller or it is using the convention 'index'
					if(curAction == null) {
						def artefact = grailsApplication.getArtefactByLogicalPropertyName("Controller", controllerName)

						def bean = applicationContext.getBean(artefact.clazz.name)
						def metaProperty = bean.metaClass.getMetaProperty("defaultAction")

						if(metaProperty) {
							curAction = bean.defaultAction
						} else {
							curAction = 'index'
						}
					}

					def action = controllers[controllerName?.toLowerCase() + '.' + curAction?.toLowerCase()]

					if(action != null && !featureToggleService.isFeatureEnabled(action.name)) {
						if(action.resultRedirect.size() > 0) {
							redirect(uri: action.resultRedirect)
							return false;
						} else {
							render(status: action.resultStatus)
							return false;
						}
					}
				}
			}
		}
	}
}