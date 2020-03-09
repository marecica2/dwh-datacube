package org.bmsource.dwh.olap;

import org.bmsource.dwh.common.courier.CourierConfiguration;
import org.bmsource.dwh.common.courier.Fact;
import org.bmsource.dwh.common.courier.RawFact;
import org.bmsource.dwh.common.security.client.ClientSecurityConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
@Import({
    CourierConfiguration.class,
    ClientSecurityConfig.class
})
@EntityScan
public class OlapConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(RawFact.class);
        config.exposeIdsFor(Fact.class);
        config.setBasePath("");
    }
}
