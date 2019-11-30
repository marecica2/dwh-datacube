package org.bmsource.dwh.masterdata;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@EntityScan
@EnableAutoConfiguration
@ComponentScan
public class MasterDataConfiguration {

    @Bean
    MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}
