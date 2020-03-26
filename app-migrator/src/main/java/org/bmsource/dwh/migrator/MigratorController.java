package org.bmsource.dwh.migrator;

import org.bmsource.dwh.common.portal.Tenant;
import org.bmsource.dwh.common.portal.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MigratorController {

    Migrator migrator;

    TenantRepository repository;

    @Autowired
    public void setMigrator(Migrator migrator) {
        this.migrator = migrator;
    }

    @Autowired
    public void setRepository(TenantRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/migrate")
    public ResponseEntity<HttpStatus> migrate() {
        migrator.migrate();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/migrate/tenant/{id}")
    public ResponseEntity<HttpStatus> migrateTenant(@PathVariable("id") String tenantId) {
        Optional<Tenant> tenant = repository.findById(tenantId);
        if (tenant.isPresent()) {
            migrator.createNewTenant(tenant.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
