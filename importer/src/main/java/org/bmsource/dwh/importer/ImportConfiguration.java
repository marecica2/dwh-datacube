package org.bmsource.dwh.importer;

import org.bmsource.dwh.common.appstate.AppStateAwareConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ImportConfiguration implements AppStateAwareConfiguration {

    public static String IMPORT_STATUS_STATE = "importStatus";

    @Bean
    @Override
    public List<String> applicationStateTopics() {
        return Arrays.asList(IMPORT_STATUS_STATE);
    }
}
