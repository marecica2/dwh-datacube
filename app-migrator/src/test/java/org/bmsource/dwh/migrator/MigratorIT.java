package org.bmsource.dwh.migrator;

import org.bmsource.dwh.common.portal.Tenant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("integration-test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { MigratorApplication.class })
public class MigratorIT {
    private Tenant tenant1 = new Tenant("tenant1", "tenant1");
    private Tenant tenant2 = new Tenant("tenant2", "tenant2");
    private Tenant tenant3 = new Tenant("tenant3", "tenant3");

    @Autowired
    Migrator migrator;

    @MockBean
    TenantRepository repository;

    @Autowired
    JdbcTemplate template;

    @AfterEach
    public void cleanup() {
        template.execute(String.format("DROP SCHEMA IF EXISTS %s CASCADE", tenant1.getId()));
        template.execute(String.format("DROP SCHEMA IF EXISTS %s CASCADE", tenant2.getId()));
        template.execute(String.format("DROP SCHEMA IF EXISTS %s CASCADE", tenant3.getId()));
    }

    @Test
    public void migrateTenants() {
        when(repository.findAll()).thenReturn(Arrays.asList(tenant1, tenant2));

        migrator.migrate();
        List<Map<String, Object>> schemas = querySchemas();
        Optional<Map<String, Object>> schema1 = findSchema(schemas, tenant1);
        Optional<Map<String, Object>> schema2 = findSchema(schemas, tenant2);
        Assertions.assertTrue(schema1.isPresent());
        Assertions.assertTrue(schema2.isPresent());
        Assertions.assertTrue(countTablesInSchema(tenant1) > 0);
    }

    @Test
    public void addTenant() {
        migrator.createNewTenant(tenant3);
        List<Map<String, Object>> schemas = querySchemas();
        Optional<Map<String, Object>> schema3 = findSchema(schemas, tenant3);
        Assertions.assertTrue(schema3.isPresent());
        Assertions.assertTrue(countTablesInSchema(tenant3) > 0);
    }

    private List<Map<String, Object>> querySchemas() {
        String sql = "SELECT schema_name FROM information_schema.schemata;";
        return template.queryForList(sql);
    }

    private Integer countTablesInSchema(Tenant tenant) {
        String sql = "select count(*) " +
            "from information_schema.tables " +
            "where table_schema = '" + tenant.getId() + "';";
        return template.queryForObject(sql, Integer.class);
    }

    private Optional<Map<String, Object>> findSchema(List<Map<String, Object>> schemas, Tenant tenant) {
        return schemas
            .stream()
            .filter(schema -> tenant.getId().equals(schema.get("schema_name")))
            .findFirst();
    }
}
