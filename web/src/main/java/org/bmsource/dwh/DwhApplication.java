package org.bmsource.dwh;

import org.bmsource.dwh.users.UserDevConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SecurityConfig.class, UserDevConfiguration.class})
public class DwhApplication {
    public static void main(String[] args) {
        SpringApplication.run(DwhApplication.class, args);
    }
}
