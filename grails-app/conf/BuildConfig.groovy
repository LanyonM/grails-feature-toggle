grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
	// inherit Grails' default dependencies
	inherits("global") {
		// uncomment to disable ehcache
		// excludes 'ehcache'
	}
	log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	repositories {
    grailsPlugins()
    grailsHome()
    grailsCentral()
    mavenLocal()
    mavenCentral()

		// uncomment the below to enable remote dependency resolution
		// from public Maven repositories
		//mavenLocal()
		//mavenCentral()
		//mavenRepo "http://snapshots.repository.codehaus.org"
		//mavenRepo "http://repository.codehaus.org"
		//mavenRepo "http://download.java.net/maven/2/"
		//mavenRepo "http://repository.jboss.com/maven2/"
	}
	dependencies {
    runtime 'org.eclipse.jdt.core.compiler:ecj:3.7.2'
		// specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

		// runtime 'mysql:mysql-connector-java:5.1.13'
	}
	plugins {
		build(":release:2.2.1") {
			export = false
		}
	}
}

def mavenConfigFile = new File("${basedir}/grails-app/conf/mavenInfo.groovy")
if (mavenConfigFile.exists()) {
  def slurpedMavenInfo = new ConfigSlurper().parse(mavenConfigFile.toURI().toURL())
  slurpedMavenInfo.grails.project.repos.each { k, v ->
    println "Adding maven info for repo $k"
    grails.project.repos."$k" = v
  }
} else {
  println "No mavenInfo file found."
}
