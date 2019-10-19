package org.bmsource.dwh.common.importer;

import org.bmsource.dwh.common.BaseFact;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ImportService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImportServiceIT {
    private String tenant = "000000-00000-00001";
    private String project = "1";
    private String transaction = "123456789";
    private List<String> files = new ArrayList<String>() {{
        add("/spends.xlsx");
        add("/spends2.xlsx");
        add("/spends3.xlsx");
    }};
    private Map<String, String> mapping = new LinkedHashMap<String, String>() {{
        put("Service Type", "serviceType");
        put("Business Unit", "businessUnit");
        put("Supplier Name", "supplierName");
        put("S. No.", "transactionId");
        put("Origin-City", "originCity");
        put("Zone", "zone");
    }};

    @Autowired
    JdbcTemplate template;

    @Bean("fact")
    public Fact fact() {
        return new Fact();
    }

    @Autowired
    ImportService importService;

    @BeforeAll
    public void createTable() {
        template.execute("DROP TABLE fact IF EXISTS;" +
            "CREATE TABLE fact" +
            "(" +
            "    id BIGINT IDENTITY NOT NULL PRIMARY KEY," +
            "    transaction_id VARCHAR(255)," +
            "    business_unit  VARCHAR(255)" +
            ");");
    }

    @Test
    public void testRunImport() {
        importService.runImport(tenant, project, transaction, files, mapping);
    }

    @Table(name = "fact")
    public static class Fact extends BaseFact {

        private String businessUnit;

        private String transactionId;

        public String getBusinessUnit() {
            return businessUnit;
        }

        public void setBusinessUnit(String businessUnit) {
            this.businessUnit = businessUnit;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        @Override
        public String toString() {
            return "Fact{" +
                ", transactionId='" + transactionId + '\'' +
                '}';
        }
    }
}
