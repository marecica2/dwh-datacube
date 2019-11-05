package org.bmsource.dwh;

import org.bmsource.dwh.importer.MappingPreset;
import org.bmsource.dwh.masterdata.RateCard;
import org.bmsource.dwh.masterdata.ServiceTypeMapping;
import org.bmsource.dwh.masterdata.Taxonomy;
import org.bmsource.dwh.masterdata.ZipCodeLocation;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;

@Component
public class RepositoryConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(ZipCodeLocation.class);
        config.exposeIdsFor(Taxonomy.class);
        config.exposeIdsFor(ServiceTypeMapping.class);
        config.exposeIdsFor(RateCard.class);
        config.exposeIdsFor(MappingPreset.class);
        config.setBasePath("/");
    }
}
