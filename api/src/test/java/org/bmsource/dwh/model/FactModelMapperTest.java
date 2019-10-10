package org.bmsource.dwh.model;

import org.bmsource.dwh.common.reader.FactModelMapper;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;
import java.util.*;

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
        FactModelMapper<Fact> mapper = new FactModelMapper<>(Fact.class, columns, columnMapping);
        Fact fact = mapper.mapRow(dataRow);
        assertThat(fact.getSupplierName()).isEqualTo("UPS");
        assertThat(fact.getTransactionId()).isEqualTo("12345");
        assertThat(fact.getZone()).isEqualTo("202");
        assertThat(fact.getServiceType()).isEqualTo("Air mail");
        assertThat(fact.getOriginCity()).isEqualTo("Atlanta");
        assertThat(fact.getBusinessUnit()).isEqualTo("BU1");
    }

    public static class Fact {

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

        String shipmentDate;

        String deliveryDate;

        @NotNull
        String serviceType;

        @NotNull
        String billableWeight;

        String actualWeight;

        String length;

        String width;

        String height;

        @NotNull
        String cost;

        String accessorialService1;

        String accessorialService2;

        String accessorialService3;

        String accessorialCharge1;

        String accessorialCharge2;

        String accessorialCharge3;

        String discount;

        String distance;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public String getBusinessUnit() {
            return businessUnit;
        }

        public void setBusinessUnit(String businessUnit) {
            this.businessUnit = businessUnit;
        }

        public String getOriginCity() {
            return originCity;
        }

        public void setOriginCity(String originCity) {
            this.originCity = originCity;
        }

        public String getOriginState() {
            return originState;
        }

        public void setOriginState(String originState) {
            this.originState = originState;
        }

        public String getOriginCountry() {
            return originCountry;
        }

        public void setOriginCountry(String originCountry) {
            this.originCountry = originCountry;
        }

        public String getOriginZip() {
            return originZip;
        }

        public void setOriginZip(String originZip) {
            this.originZip = originZip;
        }

        public String getDestinationCity() {
            return destinationCity;
        }

        public void setDestinationCity(String destinationCity) {
            this.destinationCity = destinationCity;
        }

        public String getDestinationState() {
            return destinationState;
        }

        public void setDestinationState(String destinationState) {
            this.destinationState = destinationState;
        }

        public String getDestinationCountry() {
            return destinationCountry;
        }

        public void setDestinationCountry(String destinationCountry) {
            this.destinationCountry = destinationCountry;
        }

        public String getDestinationZip() {
            return destinationZip;
        }

        public void setDestinationZip(String destinationZip) {
            this.destinationZip = destinationZip;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getShipmentDate() {
            return shipmentDate;
        }

        public void setShipmentDate(String shipmentDate) {
            this.shipmentDate = shipmentDate;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public String getBillableWeight() {
            return billableWeight;
        }

        public void setBillableWeight(String billableWeight) {
            this.billableWeight = billableWeight;
        }

        public String getActualWeight() {
            return actualWeight;
        }

        public void setActualWeight(String actualWeight) {
            this.actualWeight = actualWeight;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getAccessorialService1() {
            return accessorialService1;
        }

        public void setAccessorialService1(String accessorialService1) {
            this.accessorialService1 = accessorialService1;
        }

        public String getAccessorialService2() {
            return accessorialService2;
        }

        public void setAccessorialService2(String accessorialService2) {
            this.accessorialService2 = accessorialService2;
        }

        public String getAccessorialService3() {
            return accessorialService3;
        }

        public void setAccessorialService3(String accessorialService3) {
            this.accessorialService3 = accessorialService3;
        }

        public String getAccessorialCharge1() {
            return accessorialCharge1;
        }

        public void setAccessorialCharge1(String accessorialCharge1) {
            this.accessorialCharge1 = accessorialCharge1;
        }

        public String getAccessorialCharge2() {
            return accessorialCharge2;
        }

        public void setAccessorialCharge2(String accessorialCharge2) {
            this.accessorialCharge2 = accessorialCharge2;
        }

        public String getAccessorialCharge3() {
            return accessorialCharge3;
        }

        public void setAccessorialCharge3(String accessorialCharge3) {
            this.accessorialCharge3 = accessorialCharge3;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }

}