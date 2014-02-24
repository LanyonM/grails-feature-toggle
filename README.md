# Grails Feature Toggles Plugin [![Build Status](https://travis-ci.org/LanyonM/grails-feature-toggle.png?branch=master)](https://travis-ci.org/LanyonM/grails-feature-toggle)
This is a fork of the recognized [Feature Toggles](http://grails.org/plugin/feature-toggle) plugin.

# Installation

    grails install-plugin feature-toggle

Dependency info: ``compile ":feature-toggle:0.22"``.

# Description
The feature toggles plugin provides Tag Libraries and dynamic methods to implement configurable features within the Grails application. [Feature Toggles](http://martinfowler.com/bliki/FeatureToggle.html) are a pattern proposed by Martin Fowler as an alternative to feature branching.

## Usage

### In GSPs
The plugin provides multiple facilities to encapsulate functionality in the application that can toggle whether or not specific functionality is available. Within GSPs, the mechanism is the ``<g:withFeature />`` tag.

    <g:withFeature feature="myNiftyFeature">
        <h1>Hello World</h1>
    </g:withFeature>
    
 If you want to make functionality specifically unavailable, you can use the ``<g:withoutFeature />`` tag.

    <g:withoutFeature feature="myNiftyFeature">
        <h1>Let's just stay in bed today...</h1>
    </g:withoutFeature>

And you can use both together to have separate-but-similar functionality, like an if/else statement.  The ``feature="..."`` attribute is optional on the second tag.

    <g:withFeature feature="myNiftyFeature">
        <h1>Hello World</h1>
    </g:withFeature>
    <g:withoutFeature>
        <h1>Let's just stay in bed today...</h1>
    </g:withoutFeature>

If you want to have a boolean test for the feature, use ``featureEnabled()``.

	<div class="${g.featureEnabled(feature: 'myNiftyFeature') ? 'feature-on' : 'feature-off'}"></div>
	<input type="hidden" name="feature-enabled-input" value="${g.featureEnabled(feature: 'myNiftyFeature')}"></input>


### In Controllers and Services

You can use the injected ``withFeature`` method in controllers and services to limit the execution of code.

    withFeature('myNiftyFeature') {
        log.debug('Hello World')
    }

If you would like to use this as an if/else statement, you can give a second closure.

    withFeature('myNiftyFeature', {
        log.debug('Hello World')
    }, { //else
        log.debug('Let's just stay in bed today...')    
    })
    
If closures are causing you issues, or just get need to grab the boolean value, you can use ``featureEnabled()``.  Unlike the tag with the same name, this method actually returns a boolean.

	if(featureEnabled('myNiftyFeature')) {
        log.debug('Hello World')
	} else {
        log.debug('Let's just stay in bed today...')
	}
	boolean featureEnabledBoolean = featureEnabled('myNiftyFeature')

It is bad style to mix-and-match if(featureEnabled())/else and withFeature(3), so try to avoid that. Decide with your development team if you want prefer one mechanism over another.  This is a good thing to mention in a style guide.

There is also ``withoutFeature``.

	withoutFeature('myNiftyFeature', {
        log.debug('Let's just stay in bed today...')    
    })

``withoutFeature`` will take a second closure, like ``withFeature``, but I would recommend that you avoid using that for readability reasons.

### In Other Groovy Classes

``withFeature``, ``withoutFeature`` and ``featureEnabled`` are not injected in classes other than controllers and services.  You can use the featureToggleService to do the same thing in this context.

	def featureToggleService
	
	…
	
    featureToggleService.withFeature('myNiftyFeature') {
        log.debug('Hello World')
    }
 
If you can't or don't want to use Spring bean injection in a class, then you can get the service from the context.

	import grails.util.Holders
	
	…
	
	def featureToggleService = Holders.grailsApplication.mainContext.getBean('featureToggleService')
    featureToggleService.withFeature('myNiftyFeature') {
        log.debug('Hello World')
    }

### Toggling Controllers and Actions

As of Version 0.2, We now support the ability to toggle controller class and actions through the grails.feature.toggle.annotations.Feature annotation.  (This part can be a bit messy, so make sure to test thoroughly.)  When the feature is not enabled, the action will send an error code (404 by default).  You can also optionally set a responseStatus (again, 404 by default) and/or a responseRedirect.  

    import grails.plugin.feature.toggle.annotations.Feature
    
    …
    
    @Feature(name="myNiftyFeature", responseStatus=404, responseRedirect="/foo/bar")
    class MyController {
        @Feature(name="myNiftyFeature2", responseStatus=404, responseRedirect="/foo/bar")
        def myAction() {
          
        }
    }


## Configuration
Features are configured in the Config.groovy file in the ``features`` block.

    features {
        myNiftyFeature {
            enabled = false
            description = "my feature is pretty cool"
        }
    }

You can place this block inside of the per environment configuration (particularly useful!) and also in an overriding block.

You can also disable ALL features that are tagged for toggle by explicitly stating so in the config.

    features {
        disableAll = true
    }

## Runtime Changes
Once you've deployed your application, you may want to change the enablement or disablement of different features on the fly. You can use the controller at ``/features`` in your deployed application to see a list of all configured feature toggles and links to enable and disable each feature indiviudally.

In the case that ``disableAll`` is set to ``true``, all features will appear disabled. If you enable any feature when ``disableAll`` is ``true``, then you will override ``disableAll`` and give yourself the control to disable and enable individual features.

You can also download the current config data so you can take the current runtime configuration and install it for additional deployments.

# Publishing
If you would like to publish to an external repo, you can specify ``mavenInfo.groovy`` in the config file as specified [here](http://blog.jeffbeck.info/?p=144).

# Plugin Contributions
Please use this plugin!  If you encounter issues, please open an issue in GitHub.  Pull Requests are encouraged, although we may take some time to respond.