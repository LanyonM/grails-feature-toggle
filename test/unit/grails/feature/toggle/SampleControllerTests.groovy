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

import groovy.mock.interceptor.StubFor

/**
 * This test is actually used to test the FeatureToggleFilters implementation by
 * testing the SampleController with that filter.
 *
 * @author mlanyon, oliviaw
 */
@TestFor(SampleController)
@Mock(FeatureToggleFilters)
class SampleControllerTests {

	def controller
	def featureToggleService

	@Before
	void setup() {
		defineBeans{
			featureToggleService(FeatureToggleService)
		}
		featureToggleService = applicationContext.getBean('featureToggleService')
		featureToggleService.grailsApplication = grailsApplication
		// this is required because Grails 2.0.x doesn't inject the service into the mocked filter
		def featureToggleFilters = applicationContext.getBean('grails.feature.toggle.filters.FeatureToggleFilters')
		featureToggleFilters.featureToggleService = featureToggleService

		controller = new SampleController()
		/*controllerMock.demand.static.withFeature { String featureName, Closure closure ->
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
		}*/
    featureToggleService.enhance(controller)

		assertNotNull 'controller should not be null', controller
		assertNotNull 'featureToggleService should not be null', featureToggleService
		//assertTrue 'controller should have withFeature method', controller.class.getMethods().findAll { it.name.contains('withFeature') }.size() > 0
	}

  @Ignore("We are having difficulty testing the filter.")
	@Test
	void testActionIsEnabled() {
		withFilters(action: 'index') {
			controller.index()
		}
		assert response.status == 200
		assertEquals controller.modelAndView.model.result, "success!"
		println controller.modelAndView.model.result
	}

  @Ignore("We are having difficulty testing the filter.")
  @Test
  void testControllerIsDisabled() {
    boolean featureState = featureToggleService.isFeatureEnabled('action')
    featureToggleService.setFeatureEnabled('controller', false)
    println "action feature is ${featureToggleService.isFeatureEnabled('action')}"
    withFilters(action: 'index') {
      controller.index()
    }
    // return the value to what it used to be
    featureToggleService.setFeatureEnabled('controller', featureState)
    println "action feature is ${featureToggleService.isFeatureEnabled('action')}"
    assert response.status == 404
  }

  @Ignore("We are having difficulty testing the filter.")
  @Test
  void testActionIsDisabled() {
    boolean featureState = featureToggleService.isFeatureEnabled('action')
    featureToggleService.setFeatureEnabled('action', false)
    println "action feature is ${featureToggleService.isFeatureEnabled('action')}"
    withFilters(action: 'index') {
      controller.index()
    }
    // return the value to what it used to be
    featureToggleService.setFeatureEnabled('action', featureState)
    println "action feature is ${featureToggleService.isFeatureEnabled('action')}"
    assert response.status == 404
  }

	@Test
	void testCodeIsEnabled() {
		def model = controller.toggleInside()
		assert response.status == 200
		assertEquals model.result, "ran with feature"
	}

  @Test
  void testCodeisDisabled() {
    boolean featureState = featureToggleService.isFeatureEnabled('code')
    featureToggleService.setFeatureEnabled('code', false)
    def model = controller.toggleInside()
    // return the value to what it used to be
    featureToggleService.setFeatureEnabled('code', featureState)
    assert response.status == 200
    assertEquals model.result, "ran without feature"
  }

  @Test
  void testWithElseConstructEnabled() {
    boolean featureState = featureToggleService.isFeatureEnabled('code')
    featureToggleService.setFeatureEnabled('code', true)
    def model = controller.useWithElseConstruct()
    // return the value to what it used to be
    featureToggleService.setFeatureEnabled('code', featureState)
    assert response.status == 200
    assertEquals model.result, "ran with feature"
  }
  @Test
  void testWithElseConstructDisabled() {
    boolean featureState = featureToggleService.isFeatureEnabled('code')
    featureToggleService.setFeatureEnabled('code', false)
    def model = controller.useWithElseConstruct()
    // return the value to what it used to be
    featureToggleService.setFeatureEnabled('code', featureState)
    assert response.status == 200
    assertEquals model.result, "ran without feature"
  }
  @Test
  void testWithoutElseConstructEnabled() {
    boolean featureState = featureToggleService.isFeatureEnabled('code')
    featureToggleService.setFeatureEnabled('code', true)
    def model = controller.useWithoutElseConstruct()
    // return the value to what it used to be
    featureToggleService.setFeatureEnabled('code', featureState)
    assert response.status == 200
    assertEquals model.result, "ran with feature"
  }
  @Test
  void testWithoutElseConstructDisabled() {
    boolean featureState = featureToggleService.isFeatureEnabled('code')
    featureToggleService.setFeatureEnabled('code', false)
    def model = controller.useWithoutElseConstruct()
    // return the value to what it used to be
    featureToggleService.setFeatureEnabled('code', featureState)
    assert response.status == 200
    assertEquals model.result, "ran without feature"
  }
}
