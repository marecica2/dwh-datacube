package org.bmsource.dwh.olap;

import org.bmsource.dwh.common.security.client.ClientSecurityConfig;
import org.bmsource.dwh.courier.Fact;
import org.bmsource.dwh.courier.RawFact;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
@Import({
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
