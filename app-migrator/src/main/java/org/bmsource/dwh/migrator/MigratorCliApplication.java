package org.bmsource.dwh.migrator;

import org.bmsource.dwh.common.portal.PortalConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(
    scanBasePackageClasses = {
        MigratorCliApplication.class,
    },
    exclude = {
        RedisAutoConfiguration.class,
        RepositoryRestMvcAutoConfiguration.class
    }
)
@EnableJpaRepositories(
    basePackageClasses = {
        MigratorCliApplication.class,
        PortalConfiguration.class,
    }
)
@EntityScan(basePackageClasses = {
    MigratorCliApplication.class,
    PortalConfiguration.class,
})
@EnableTransactionManagement
@Profile("cli")
public class MigratorCliApplication implements CommandLineRunner {

    @Autowired
    Migrator migrator;

    private static Logger logger = LoggerFactory
        .getLogger(MigratorCliApplication.class);

    public static void main(String[] args) {
        logger.info("CLI Migrating schemas");
        SpringApplication app = new SpringApplication(MigratorCliApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
        logger.info("CLI Migration completed");
    }

    @Override
    public void run(String... args) {
        migrator.migrate();
        System.exit(0);
    }
}
