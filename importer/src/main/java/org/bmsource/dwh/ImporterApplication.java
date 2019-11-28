package org.bmsource.dwh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
public class ImporterApplication {
  public static void main(String[] args) {
    SpringApplication.run(ImporterApplication.class, args);
  }
}
