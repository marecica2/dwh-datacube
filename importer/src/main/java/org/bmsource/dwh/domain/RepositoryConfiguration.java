package org.bmsource.dwh.domain;

import org.bmsource.dwh.domain.model.Fact;
import org.bmsource.dwh.domain.model.RawFact;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class RepositoryConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(RawFact.class);
        config.exposeIdsFor(Fact.class);
        config.setBasePath("/");
    }
}
