package org.bmsource.dwh.common.job;

import org.bmsource.dwh.common.BaseFact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Table;
import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ActiveProfiles("unit-test")
@Component
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JobService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JobServiceIT {
    private String tenant = "000000-00000-00001";
    private String project = "1";
    private String transaction = "123456789";
    private List<String> files = new ArrayList<String>() {{
        add("/spends.xlsx");
        add("/spends2.xlsx");
    }};
    private Map<String, String> mapping = new LinkedHashMap<String, String>() {{
        put("Service Type", "serviceType");
        put("Business Unit", "businessUnit");
        put("Supplier Name", "supplierName");
        put("S. No.", "transactionId");
        put("Origin-City", "originCity");
        put("Zone", "zone");
        put("Cost (USD)", "cost");
        put("Billable Weight (lb)", "billableWeight");
    }};

    @Autowired
    JdbcTemplate template;

    @Bean("rawFact")
    public RawFact rawFact() {
        return new RawFact();
    }

    @Bean("fact")
    public Fact fact() {
        return new Fact();
    }

    @Autowired
    JobService jobService;

    @BeforeAll
    public void createTable() {
        template.execute("DROP TABLE raw_fact IF EXISTS;" +
            "CREATE TABLE raw_fact" +
            "(" +
            "    id BIGINT IDENTITY NOT NULL PRIMARY KEY," +
            "    transaction_id VARCHAR(255) NOT NULL," +
            "    business_unit  VARCHAR(255) NOT NULL," +
            "    cost  DECIMAL NOT NULL," +
            "    billable_weight FLOAT NOT NULL" +
            ");");

        template.execute("DROP TABLE fact IF EXISTS;" +
            "CREATE TABLE fact" +
            "(" +
            "    id BIGINT IDENTITY NOT NULL PRIMARY KEY," +
            "    transaction_id VARCHAR(255) NOT NULL," +
            "    business_unit  VARCHAR(255) NOT NULL," +
            "    cost  DECIMAL NOT NULL," +
            "    billable_weight FLOAT NOT NULL" +
            ");");
    }

    @Test
    public void testImport() {
        jobService.runImport(tenant, project, transaction, files, mapping);
        int importedRows = template.queryForObject("SELECT count(*) FROM raw_fact", Integer.class);
        Assertions.assertEquals(472, importedRows);
    }

    @Table(name = "raw_fact")
    public static class RawFact extends BaseFact {

        @NotNull
        private String businessUnit;

        @NotNull
        private String transactionId;

        @NotNull
        @Min(0)
        private BigDecimal cost;

        @NotNull
        private Integer billableWeight;

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

        public Integer getBillableWeight() {
            return billableWeight;
        }

        public void setBillableWeight(Integer billableWeight) {
            this.billableWeight = billableWeight;
        }

        public BigDecimal getCost() {
            return cost;
        }

        public void setCost(BigDecimal cost) {
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "RawFact{" +
                "businessUnit='" + businessUnit + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", billableWeight=" + billableWeight +
                ", cost=" + cost +
                '}';
        }
    }

    @Table(name = "fact")
    public static class Fact extends BaseFact {

        @NotNull
        private String businessUnit;

        @NotNull
        private String transactionId;

        @NotNull
        @Min(0)
        private BigDecimal cost;

        @NotNull
        private Integer billableWeight;

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

        public Integer getBillableWeight() {
            return billableWeight;
        }

        public void setBillableWeight(Integer billableWeight) {
            this.billableWeight = billableWeight;
        }

        public BigDecimal getCost() {
            return cost;
        }

        public void setCost(BigDecimal cost) {
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "Fact{" +
                "businessUnit='" + businessUnit + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", billableWeight=" + billableWeight +
                ", cost=" + cost +
                '}';
        }
    }
}