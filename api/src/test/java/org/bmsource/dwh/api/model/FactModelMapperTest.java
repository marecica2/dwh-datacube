package org.bmsource.dwh.api.model;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.*;

public class FactModelMapperTest {

    static Map<String, String> columnMapping = new HashMap<>();
    static {
        columnMapping.put("Service Type", "serviceType");
        columnMapping.put("Business Unit", "businessUnit");
        columnMapping.put("Supplier name", "supplierName");
        columnMapping.put("S. No", "transactionId");
        columnMapping.put("Origin-City", "originCity");
        columnMapping.put("Zone", "zone");
    }

    List<Object> columns = Arrays.asList("S. No", "Supplier name", "Service Type", "Business Unit", "Zone", "Origin-City");

    List<Object> dataRow = Arrays.asList("12345", "UPS", "Air mail", "BU1", "202", "Atlanta");

    @Test
    public void modelMapperTest() {
        FactModelMapper mapper = new FactModelMapper(columns, columnMapping);
        Fact fact = mapper.mapRow(dataRow);
        assertThat(fact.getSupplierName()).isEqualTo("UPS");
        assertThat(fact.getTransactionId()).isEqualTo("12345");
        assertThat(fact.getZone()).isEqualTo("202");
        assertThat(fact.getServiceType()).isEqualTo("Air mail");
        assertThat(fact.getOriginCity()).isEqualTo("Atlanta");
        assertThat(fact.getBusinessUnit()).isEqualTo("BU1");
    }

}
