# Grails Feature Toggles Plugin [![Build Status](https://travis-ci.org/LanyonM/grails-feature-toggle.png?branch=master)](https://travis-ci.org/LanyonM/grails-feature-toggle)
This is a fork the the recognized [Feature Toggles](http://grails.org/plugin/feature-toggle) plugin.

# Installation

    grails install-plugin feature-toggle

Dependency info: ``compile ":feature-toggle:0.2"``.

# Description
The feature toggles plugin provides Tag Libraries and dynamic methods to implement configurable features within the Grails application. [Feature Toggles](http://martinfowler.com/bliki/FeatureToggle.html) are a pattern proposed by Martin Fowler as an alternative to feature branching.

## Usage
The plugin provides multiple facilities to encapsulate functionality in the application that can toggle whether or not specific functionality is available. Within GSPs, the mechanism is the ``<g:toggle />`` tag.

    <g:toggle feature="myNiftyFeature">
        <h1>Hello World</h1>
    </g:toggle>

You can also use the injected ``withFeature`` method in controllers and services (for now) to limit the execution of code components to the enablement of the feature.

    withFeature("myNiftyFeature") { ->
        log.debug("Hello World")
    }

As of Version 0.2, We now support the ability to toggle controller actions through the grails.feature.toggle.annotations.Feature annotation.

    class MyController {
        @Feature(name="myFeature", responseStatus=404, responseRedirect="/foo/bar")
        def myAction() {
          
        }
    }

You may provide the status returned when the feature is disabled or the URL to redirect to when the feature is disabled.

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

## Plugin Contributions
Please use this plugin!  If you encounter issues, please open an issue in GitHub.