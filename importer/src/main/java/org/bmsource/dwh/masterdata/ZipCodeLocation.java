package org.bmsource.dwh.masterdata;

import org.bmsource.dwh.common.BaseFact;

public class ZipCodeLocation extends BaseFact {
    private String zipCode;

    private double lattitude;

    private double longitude;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
