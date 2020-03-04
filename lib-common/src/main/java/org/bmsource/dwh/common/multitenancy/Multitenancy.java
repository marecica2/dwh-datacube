package org.bmsource.dwh.common.multitenancy;

import org.bmsource.dwh.common.multitenancy.impl.MultitenancyConfiguration;
import org.bmsource.dwh.common.portal.PortalConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * No-op multi-tenancy configuration entry point
 */
@Configuration
@ComponentScan(basePackageClasses = {PortalConfig.class, MultitenancyConfiguration.class})
public class Multitenancy {
}
