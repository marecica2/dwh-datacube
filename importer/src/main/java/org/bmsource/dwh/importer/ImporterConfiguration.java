package org.bmsource.dwh.importer;

import org.bmsource.dwh.common.job.JobService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@Import(JobService.class)
public class ImporterConfiguration {

    public enum StateType {
        IMPORT_STATUS_STATE("importStatus");

        private String value;

        StateType(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }

    @Bean
    MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean("fact")
    public Fact fact() {
        return new Fact();
    }

//    @Bean
//    public List<String> channels() {
//        return  Stream.of(StateType.values())
//            .map(StateType::getValue)
//            .collect(Collectors.toList());
//    }
}
