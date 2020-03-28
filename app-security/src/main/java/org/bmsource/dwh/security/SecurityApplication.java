package org.bmsource.dwh.security;

import org.bmsource.dwh.common.portal.PortalConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@EntityScan(basePackageClasses = {
    SecurityConfiguration.class, PortalConfiguration.class
})
@EnableJpaRepositories(basePackageClasses = {
    SecurityConfiguration.class, PortalConfiguration.class
})
public class SecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
}
