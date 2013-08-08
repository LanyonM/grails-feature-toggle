package grails.feature.toggle

import grails.feature.toggle.filters.FeatureToggleFilters
import grails.plugin.featuretoggle.FeatureToggleService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

import org.junit.Test
import org.junit.Before

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
class SampleControllerTests {

	def featureService
  FiltersUnitTestMixin f = new FiltersUnitTestMixin()

  @Before
  void setup() {
    f.grailsApplication = grailsApplication
    f.applicationContext = grailsApplication.mainContext
    f.mockFilters(FeatureToggleFilters)
  }

  @Test
	void testFeatureIsDisabled() {
		def featureService = mockFor(FeatureToggleService)
		featureService.demand.isFeatureEnabled {String feature -> return false}

		f.withFilters(action: 'index') {
			controller.index()
		}

		assert response.status == 404
	}
}
