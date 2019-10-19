package org.bmsource.dwh.common.importer.batch;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@StepScope
@Component
public class ImportPartitioner implements Partitioner {

    private static final String CONFIG_KEY = "config";

    private static final String PARTITION_KEY = "partition";

    private String keyName = CONFIG_KEY;

    @Value("#{jobParameters['files']}")
    private String files;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        if(this.files != null) {
            String[] files = this.files.split(",");
            Map<String, ExecutionContext> map = new HashMap<>(files.length);
            int i = 0, k = 1;
            for (String file : files) {
                ExecutionContext context = new ExecutionContext();
                context.putString("fileName", file);
                map.put(PARTITION_KEY + i, context);
                i++;
            }
            return map;
        }
        return new HashMap<>();
    }
};
