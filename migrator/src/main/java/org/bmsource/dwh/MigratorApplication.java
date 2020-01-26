package org.bmsource.dwh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MigratorApplication implements CommandLineRunner {

    @Autowired
    Migrator migrator;

    private static Logger logger = LoggerFactory
        .getLogger(MigratorApplication.class);

    public static void main(String[] args) {
        logger.info("Hello");
        logger.info("Migrating schemas");
        SpringApplication.run(MigratorApplication.class, args);
        logger.info("Migration completed");
    }

    @Override
    public void run(String... args) {
        migrator.migrate();
        System.exit(0);
    }

}