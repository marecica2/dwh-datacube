package org.bmsource.dwh.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RestConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setRepositoryDetectionStrategy(RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED);
    }

//    @Bean
//    public HateoasPageableHandlerMethodArgumentResolver customResolver(
//            HateoasPageableHandlerMethodArgumentResolver pageableResolver) {
//        pageableResolver.setOneIndexedParameters(true);
//        pageableResolver.setFallbackPageable(PageRequest.of(0, Integer.MAX_VALUE));
//        pageableResolver.setMaxPageSize(Integer.MAX_VALUE);
//        return pageableResolver;
//    }
}
