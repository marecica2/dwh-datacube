package org.bmsource.dwh;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class Migrator {

    @Autowired
    DataSource datasource;

    public Flyway tenantFlyway() {
        Flyway flyway = Flyway.configure()
            .dataSource(datasource)
            .locations("migration/tenant")
            .load();
        return flyway;
    }

    public void migrate() {
        tenantFlyway().migrate();
    }
}
