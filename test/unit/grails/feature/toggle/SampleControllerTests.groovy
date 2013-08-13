package grails.feature.toggle

import grails.feature.toggle.filters.FeatureToggleFilters
import grails.plugin.featuretoggle.FeatureToggleService
import grails.test.GrailsMock
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

import org.junit.Test
import org.junit.Before
import org.junit.Ignore

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.test.mixin.web.FiltersUnitTestMixin


/**
 * This test is actually used to test the FeatureToggleFilters implementation by
 * testing the SampleController with that filter.
 *
 * @author mminella
 */
@TestFor(SampleController)
@Mock(FeatureToggleFilters)
class SampleControllerTests {

	def controller
	def featureToggleService

	@Before
	void setup() {
		// def featureService = mockFor(FeatureToggleService)
		// featureService.demand.isFeatureEnabled { String feature -> return true }
		// featureToggleService = featureService.createMock()
		defineBeans{
			// featureToggleServiceControl(GrailsMock, FeatureToggleService)
			// featureToggleService(featureToggleServiceControl: 'createMock')
			featureToggleService(FeatureToggleService)
		}
		// def featureToggleServiceControl = applicationContext.getBean('featureToggleServiceControl')
		// featureToggleServiceControl.demand.isFeatureEnabled = { String feature -> return true }
		featureToggleService = applicationContext.getBean('featureToggleService')
		featureToggleService.grailsApplication = grailsApplication

		// featureToggleService.enhance(controller.class)
		def controllerMock = mockFor(SampleController)
		controllerMock.demand.static.withFeature { String featureName, Closure closure ->
			if(featureToggleService.isFeatureEnabled(featureName)) {
				closure.call()
			}
		}
		controllerMock.demand.static.withoutFeature { String featureName, Closure closure ->
			if(!featureToggleService.isFeatureEnabled(featureName)) {
				closure.call()
			}
		}
		controllerMock.demand.static.featureEnabled { String featureName ->
			return featureToggleService.isFeatureEnabled(featureName)
		}
		controller = controllerMock.createMock()

		assertNotNull 'controller should not be null', controller
		assertNotNull 'featureToggleService should not be null', featureToggleService
		// assertTrue 'controller should have withFeature method', controller.class.getMethods().findAll { it.name.contains('withFeature') }.size() > 0
	}

	@Test
	void testActionIsEnabled() {
		withFilters(action: 'index') {
			controller.index()
		}
		assert response.status == 200
		assertEquals controller.modelAndView.model.result, "success!"
		println controller.modelAndView.model.result
	}

	@Test
	void testActionIsDisabled() {
		// def featureService = mockFor(FeatureToggleService)
		// featureService.demand.isFeatureEnabled {String feature -> return false}
		boolean featureState = featureToggleService.isFeatureEnabled('action')
		// this should work, but doesn't
		featureToggleService.setFeatureEnabled('action', false)
		// so we've done this instead
		featureToggleService.grailsApplication.config.features.action.enabled = false
		println "action feature is ${featureToggleService.isFeatureEnabled('action')}"
		withFilters(action: 'index') {
			controller.index()
		}
		// return the value to what it used to be
		// this should work, but doesn't
		featureToggleService.setFeatureEnabled('action', featureState)
		// so we've done this instead
		featureToggleService.grailsApplication.config.features.action.enabled = featureState
		println "action feature is ${featureToggleService.isFeatureEnabled('action')}"
		assert response.status == 404
	}

	@Test
	void testCodeIsEnabled() {
		withFilters(action: 'toggleInside') {
			controller.toggleInside()
		}
		assert response.status == 200
		assertEquals controller.modelAndView.model.result, "ran with feature"
		println controller.modelAndView.model.result
	}

	@Test
	void testCodeisDisabled() {
		boolean featureState = featureToggleService.isFeatureEnabled('code')
		// this should work, but doesn't
		featureToggleService.setFeatureEnabled('code', false)
		// so we've done this instead
		featureToggleService.grailsApplication.config.features.code.enabled = false
		withFilters(action: 'toggleInside') {
			controller.toggleInside()
		}
		// return the value to what it used to be
		// this should work, but doesn't
		featureToggleService.setFeatureEnabled('code', featureState)
		// so we've done this instead
		featureToggleService.grailsApplication.config.features.code.enabled = featureState
		assert response.status == 200
		assertEquals controller.modelAndView.model.result, "ran without feature"
		println controller.modelAndView.model.result
	}

}
