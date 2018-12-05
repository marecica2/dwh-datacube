package org.bmsource.dwh.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "dwh")
public class DwhConfig {

    Logger logger = LoggerFactory.getLogger(DwhConfig.class);

    private String property;

    @PostConstruct
    public void init() {
        logger.info(this.property);
        logger.trace(this.property);
        logger.debug(this.property);
        logger.error(this.property);
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
