package org.bmsource.dwh;

import org.bmsource.dwh.common.multitenancy.EnableMultitenancy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableMultitenancy(ImporterApplication.class)
@EnableAspectJAutoProxy
@SpringBootApplication(exclude = {
    RedisRepositoriesAutoConfiguration.class
})
@EnableAsync
@EnableTransactionManagement
@EnableJpaRepositories
public class ImporterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImporterApplication.class, args);
    }
}
