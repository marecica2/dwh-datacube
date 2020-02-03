package org.bmsource.dwh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableTransactionManagement
public class ImporterApplication {
  public static void main(String[] args) {
    SpringApplication.run(ImporterApplication.class, args);
  }
}
