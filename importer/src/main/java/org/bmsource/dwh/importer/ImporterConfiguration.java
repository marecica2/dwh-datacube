package org.bmsource.dwh.importer;

import org.bmsource.dwh.common.appstate.EnableAppStateManagement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableAppStateManagement
public class ImporterConfiguration {

    public enum StateType {
        IMPORT_STATUS_STATE ("importStatus");

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
    public List<String> channels() {
        return  Stream.of(StateType.values())
            .map(StateType::getValue)
            .collect(Collectors.toList());
    }
}
