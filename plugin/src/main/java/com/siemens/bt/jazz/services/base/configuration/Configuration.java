package com.siemens.bt.jazz.services.base.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Configuration {
  // make sure every configurator is added exactly once
  protected Collection<ServiceConfigurator> configurations = new HashSet<>();

  public Configuration() {}

  public Configuration(ServiceConfigurator... configurators) {
    configurations.addAll(Arrays.asList(configurators));
  }

  public Configuration(Configuration... configurations) {
    for (Configuration c : configurations) {
      this.configurations.addAll(c.get());
    }
  }

  public Collection<ServiceConfigurator> get() {
    return configurations;
  }

  public void add(ServiceConfigurator configurator) {
    configurations.add(configurator);
  }

  public void add(Collection<ServiceConfigurator> configurators) {
    configurations.addAll(configurators);
  }

  public void merge(Configuration other) {
    this.configurations.addAll(other.get());
  }
}
