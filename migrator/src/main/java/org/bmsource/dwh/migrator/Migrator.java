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
        migrateTenants();
    }

    public void createNewTenant(Tenant tenant) {
        createTenantSchema(tenant);
        migrateTenant(tenant);
    }

    private void migrateTenants() {
        repository.findAll().forEach(tenant -> {
            migrateTenant(tenant);
        });
    }

    private void migrateTenant(Tenant tenant) {
        try {
            String schema = tenant.getSchemaName();
            Flyway flyway = Flyway
                .configure()
                .locations("migration/tenant")
                .dataSource(dataSource)
                .schemas(schema).load();
            flyway.migrate();
            logger.info("Migration for tenant {} successful", tenant);
        } catch (Exception e) {
            logger.error("Migration for tenant {} failed", tenant);
            logger.error(e.getMessage(), e);
        }
    }

    private void createTenantSchema(Tenant tenant) {
        template.execute("CREATE SCHEMA IF NOT EXISTS \"" + tenant.getSchemaName() + "\"");
    }
}
