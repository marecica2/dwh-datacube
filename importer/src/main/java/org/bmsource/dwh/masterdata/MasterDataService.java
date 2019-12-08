package org.bmsource.dwh.masterdata;

public interface MasterDataService {

    String getStandardServiceType(String supplierServiceType);

    String getServiceGroup(String supplierServiceType);
}
