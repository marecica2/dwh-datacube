package org.bmsource.dwh.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;

@Configuration
public class RestConfiguration extends RepositoryRestConfigurerAdapter {

//    @Bean
//    public HateoasPageableHandlerMethodArgumentResolver customResolver(
//            HateoasPageableHandlerMethodArgumentResolver pageableResolver) {
//        pageableResolver.setOneIndexedParameters(true);
//        pageableResolver.setFallbackPageable(PageRequest.of(0, Integer.MAX_VALUE));
//        pageableResolver.setMaxPageSize(Integer.MAX_VALUE);
//        return pageableResolver;
//    }
}
