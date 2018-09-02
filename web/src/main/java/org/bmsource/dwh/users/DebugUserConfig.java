package org.bmsource.dwh.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile("development")
@Configuration
public class DebugUserConfig {

    Logger logger = LoggerFactory.getLogger(DebugUserConfig.class);

    @PostConstruct
    public void init() {
        logger.info("DebugUserConfig INFO");
        logger.trace("DebugUserConfig TRACE");
        logger.debug("DebugUserConfig DEBUG");
        logger.warn("DebugUserConfig WARN");
    }
}
