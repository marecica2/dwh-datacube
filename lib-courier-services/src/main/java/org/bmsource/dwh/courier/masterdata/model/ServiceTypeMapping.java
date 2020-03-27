package org.bmsource.dwh.courier.masterdata.model;

import org.bmsource.dwh.common.BaseFact;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "service_type_mapping")
public class ServiceTypeMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    private String supplierName;

    private String supplierServiceType;

    private String standardServiceTypeLetter;

    private String standardServiceTypeParcel;

    private String standardServiceTypeLetterKey;

    private String standardServiceTypeParcelKey;


    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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


    public String getStandardServiceTypeLetter() {
        return standardServiceTypeLetter;
    }

    public void setStandardServiceTypeLetter(String standardServiceTypeLetter) {
        this.standardServiceTypeLetter = standardServiceTypeLetter;
    }


    public String getStandardServiceTypeParcel() {
        return standardServiceTypeParcel;
    }

    public void setStandardServiceTypeParcel(String standardServiceTypeParcel) {
        this.standardServiceTypeParcel = standardServiceTypeParcel;
    }


    public String getStandardServiceTypeLetterKey() {
        return standardServiceTypeLetterKey;
    }

    public void setStandardServiceTypeLetterKey(String standardServiceTypeLetterKey) {
        this.standardServiceTypeLetterKey = standardServiceTypeLetterKey;
    }


    public String getStandardServiceTypeParcelKey() {
        return standardServiceTypeParcelKey;
    }

    public void setStandardServiceTypeParcelKey(String standardServiceTypeParcelKey) {
        this.standardServiceTypeParcelKey = standardServiceTypeParcelKey;
    }

}
