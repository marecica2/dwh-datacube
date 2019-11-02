package org.bmsource.dwh.masterdata;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;

@Component
public class MasterDataRepositoryConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(ZipCodeLocation.class);
        config.exposeIdsFor(Taxonomy.class);
        config.exposeIdsFor(ServiceTypeMapping.class);
        config.exposeIdsFor(RateCard.class);
        config.setBasePath("/");
    }
}
