package org.bmsource.dwh.migrator;

import org.bmsource.dwh.common.portal.Tenant;
import org.bmsource.dwh.common.portal.TenantRepository;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class Migrator {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String DEFAULT_SCHEMA = "master";

    @Autowired
    private TenantRepository repository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate template;

    public void migrate() {
        migrateSchema("master");
        migrateTenants();
    }

    public void createNewTenant(Tenant tenant) {
        createTenantSchema(tenant);
        migrateSchema(tenant.getSchemaName());
    }

    private void migrateTenants() {
        repository.findAll().forEach(tenant -> {
            migrateSchema(tenant.getSchemaName());
        });
    }

    private void migrateSchema(String schema) {
        try {
            Flyway flyway = Flyway
                .configure()
                .locations("migration/" + schema)
                .dataSource(dataSource)
                .schemas(schema).load();
            flyway.migrate();
            logger.info("Migration for {} successful", schema);
        } catch (Exception e) {
            logger.error("Migration for {} failed", schema);
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private void createTenantSchema(Tenant tenant) {
        template.execute("CREATE SCHEMA IF NOT EXISTS \"" + tenant.getSchemaName() + "\"");
    }
}
