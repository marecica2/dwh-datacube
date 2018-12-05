package org.bmsource.dwh.multitenancy;

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
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

@ActiveProfiles("it")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DwhApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    private static int PG_PORT = 5432;

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("db_1", PG_PORT);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.datasource.platform=postgresql",
                    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
                    "spring.datasource.url=jdbc:tc:postgresql:10.3://" + environment.getServiceHost("db_1", PG_PORT) + "/tenant1?TC_INITFUNCTION=org.bmsource.dwh.multitenancy.AbstractIntegrationTest::initDatabase",
                    "spring.datasource.user=tenant1",
                    "spring.datasource.password=password1"
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    public static void initDatabase(Connection connection) throws SQLException {
        System.out.println("Some initialization here");
    }
}



