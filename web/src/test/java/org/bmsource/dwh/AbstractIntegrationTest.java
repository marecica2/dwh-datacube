package org.bmsource.dwh;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.sql.Connection;
import java.sql.SQLException;

@ActiveProfiles("it")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DwhApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    @ClassRule
    public static GenericContainer postgres = new GenericContainer("postgres:10.3");

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.datasource.platform=postgresql",
                    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
                    "spring.datasource.url=jdbc:tc:postgresql:10.3://localhost/test?TC_INITFUNCTION=org.bmsource.dwh.AbstractIntegrationTest::initDatabase",
                    "spring.datasource.user=postgres",
                    "spring.datasource.password=postgres"
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    public static void initDatabase(Connection connection) throws SQLException {
        System.out.println("FLYWAAAAY");
    }

}



