package org.bmsource.dwh.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = {"org.bmsource.dwh"})
@EnableJpaRepositories
@EnableTransactionManagement
public class ApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }
}
