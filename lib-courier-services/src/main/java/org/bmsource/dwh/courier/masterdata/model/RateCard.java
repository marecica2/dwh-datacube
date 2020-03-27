package org.bmsource.dwh.courier.masterdata.model;

import org.bmsource.dwh.common.BaseFact;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "standard_rate_card")
public class RateCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigDecimal id;

    private String supplierName;
    private String supplierServiceType;
    private String supplierZone;
    private Double weight;
    private String weightType;
    private Double weightFrom;
    private Double weightTo;
    private BigDecimal price;
    private BigDecimal pricePerUnit;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierServiceType() {
        return supplierServiceType;
    }

    public void setSupplierServiceType(String supplierServiceType) {
        this.supplierServiceType = supplierServiceType;
    }

    public String getSupplierZone() {
        return supplierZone;
    }

    public void setSupplierZone(String supplierZone) {
        this.supplierZone = supplierZone;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public Double getWeightFrom() {
        return weightFrom;
    }

    public void setWeightFrom(Double weightFrom) {
        this.weightFrom = weightFrom;
    }

    public Double getWeightTo() {
        return weightTo;
    }

    public void setWeightTo(Double weightTo) {
        this.weightTo = weightTo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
