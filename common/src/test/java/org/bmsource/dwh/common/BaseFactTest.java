package org.bmsource.dwh.common;

import org.junit.jupiter.api.Test;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseFactTest {

    @Test
    public void getFieldsSQL() throws Exception {
        TestFact f = new TestFact();
        assertThat(f.insertSQL()).isEqualTo("INSERT INTO test_fact (transaction_id, supplier_name, business_unit, " +
            "origin_city, origin_state, origin_country, origin_zip, destination_city, destination_state, " +
            "destination_country, destination_zip, zone, shipment_date, delivery_date, service_type, " +
            "standard_service_type, service_type_group, billable_weight, actual_weight, length, width, height, cost, " +
            "accessorial_service1, accessorial_service2, accessorial_service3, accessorial_charge1, " +
            "accessorial_charge2, accessorial_charge3, discount, distance) VALUES (:transactionId, :supplierName, " +
            ":businessUnit, :originCity, :originState, :originCountry, :originZip, :destinationCity, " +
            ":destinationState, :destinationCountry, :destinationZip, :zone, :shipmentDate, :deliveryDate, " +
            ":serviceType, :standardServiceType, :serviceTypeGroup, :billableWeight, :actualWeight, :length, :width, " +
            ":height, :cost, :accessorialService1, :accessorialService2, :accessorialService3, :accessorialCharge1, " +
            ":accessorialCharge2, :accessorialCharge3, :discount, :distance)");
        assertThat(f.selectSQL()).isEqualTo("SELECT * FROM test_fact ORDER BY id ASC");
    }

    @Table(name = "test_fact")
    public static class TestFact extends BaseFact {


        public static final String TABLE_NAME = "fact";

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private BigDecimal id;

        String transactionId;

        String supplierName;

        String businessUnit;

        String originCity;

        String originState;

        String originCountry;

        String originZip;

        String destinationCity;

        String destinationState;

        String destinationCountry;

        String destinationZip;

        String zone;

        Date shipmentDate;

        Date deliveryDate;

        @NotNull
        String serviceType;

        String standardServiceType;

        String serviceTypeGroup;

        @NotNull
        Double billableWeight;

        Double actualWeight;

        Double length;

        Double width;

        Double height;

        @NotNull
        BigDecimal cost;

        String accessorialService1;

        String accessorialService2;

        String accessorialService3;

        BigDecimal accessorialCharge1;

        BigDecimal accessorialCharge2;

        BigDecimal accessorialCharge3;

        BigDecimal discount;

        Double distance;
    }
}
