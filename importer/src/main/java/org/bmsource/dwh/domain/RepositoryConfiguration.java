package org.bmsource.dwh.domain;

import org.bmsource.dwh.domain.model.Fact;
import org.bmsource.dwh.domain.model.RawFact;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@EnableJpaRepositories
@EntityScan
@Configuration
public class RepositoryConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(RawFact.class);
        config.exposeIdsFor(Fact.class);
        config.setBasePath("/");
    }
}
