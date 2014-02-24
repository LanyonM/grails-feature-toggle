
package grails.plugin.feature.toggle

class Feature {
  String key
  String description
  boolean enabled
  public Feature(Map.Entry<String, ConfigObject> config) {
    key = config.getKey()
    description = config.getValue().description
    enabled = config.getValue().enabled
  }
}