package org.bmsource.dwh.common.importer.job;

import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.common.io.DataRow;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FactItemProcessor<Fact extends BaseFact> implements ItemProcessor<DataRow<Fact>, DataRow<Fact>> {

    @Override
    public DataRow<Fact> process(DataRow<Fact> row) {
        return row;
    }

}