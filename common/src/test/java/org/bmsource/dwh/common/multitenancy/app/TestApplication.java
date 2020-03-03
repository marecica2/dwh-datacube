package org.bmsource.dwh.common.multitenancy.app;

import org.bmsource.dwh.common.multitenancy.EnableMultitenancy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableMultitenancy(TestApplication.class)
@EnableAspectJAutoProxy
@EnableJpaRepositories(
//    entityManagerFactoryRef = "multitenantEntityManagerFactory"
)
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
