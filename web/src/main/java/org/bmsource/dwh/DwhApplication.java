package org.bmsource.dwh;

import org.bmsource.dwh.users.DebugUserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SecurityConfig.class, DebugUserConfig.class})
public class DwhApplication {
    public static void main(String[] args) {
        SpringApplication.run(DwhApplication.class, args);
    }
}
