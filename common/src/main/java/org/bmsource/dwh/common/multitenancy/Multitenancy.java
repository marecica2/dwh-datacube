package org.bmsource.dwh.common.multitenancy;

import org.bmsource.dwh.common.portal.PortalConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {PortalConfig.class, WebMvcConfig.class})
public class Multitenancy {
}
