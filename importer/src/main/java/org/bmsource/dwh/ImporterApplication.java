package org.bmsource.dwh;

import org.bmsource.dwh.common.multitenancy.EnableMultitenancy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    RedisRepositoriesAutoConfiguration.class,
})
@EnableJpaRepositories(
    //entityManagerFactoryRef = "multitenantEntityManagerFactory"
)
@EnableAspectJAutoProxy
@EnableAsync
@EnableTransactionManagement
@EnableMultitenancy(ImporterApplication.class)
public class ImporterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImporterApplication.class, args);
    }
}
