package org.bmsource.dwh.common.courier;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CourierConfiguration.class)
@EntityScan(basePackageClasses = CourierConfiguration.class)
public class CourierConfiguration {
}
