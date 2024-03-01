package com.choresync.assignment;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AssignmentServiceConfig {

  @Bean
  public ServiceInstanceListSupplier supplier() {
    return new TestServiceInstanceListSupplier();
  }
}
