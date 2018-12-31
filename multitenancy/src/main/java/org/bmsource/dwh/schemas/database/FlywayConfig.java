package org.bmsource.dwh.schemas.database;

import org.bmsource.dwh.schemas.database.repositories.TenantRepository;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile({"!unit-test"})
public class FlywayConfig {

    public static final String DEFAULT_SCHEMA = "master";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public Flyway flyway(DataSource dataSource) {
        logger.info("Migrating default schema ");
        Flyway flyway = new Flyway();
        flyway.setLocations("db/migration/default");
        flyway.setDataSource(dataSource);
        flyway.setSchemas(DEFAULT_SCHEMA);
        flyway.migrate();
        return flyway;
    }

    @Bean
    public Boolean tenantsFlyway(TenantRepository repository, DataSource dataSource){
        repository.findAll().forEach(tenant -> {
            String schema = tenant.getSchemaName();
            Flyway flyway = new Flyway();
            flyway.setLocations("db/migration/tenants");
            flyway.setDataSource(dataSource);
            flyway.setSchemas(schema);
            flyway.migrate();
        });
        return true;
    }

}
