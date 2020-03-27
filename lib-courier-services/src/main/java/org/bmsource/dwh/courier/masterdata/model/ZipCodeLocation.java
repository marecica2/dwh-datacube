package org.bmsource.dwh.courier.masterdata.model;

import org.bmsource.dwh.common.BaseFact;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@Table(name = "zip_code_location")
public class ZipCodeLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    private String zipCode;

    private double latitude;

    private double longitude;

    public BigInteger getId() {
        return id;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double lattitude) {
        this.latitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
