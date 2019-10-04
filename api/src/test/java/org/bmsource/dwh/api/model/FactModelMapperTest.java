package org.bmsource.dwh.api.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.*;

public class FactModelMapperTest {

    Map<String, String> columnMapping = Stream.of(new String[][] {
        { "S. No", "transactionId" },
        { "Supplier name", "supplierName" },
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));


    List<Object> dataRow = Arrays.asList("12345", "UPS");

    @Test
    public void modelMapperTest() {
        FactModelMapper mapper = new FactModelMapper(columnMapping);
        Fact fact = mapper.mapRow(dataRow);
        assertThat(fact.getSupplierName()).isEqualTo("UPS");
        assertThat(fact.getTransactionId()).isEqualTo("12345");
    }

}
