package org.bmsource.dwh.common.importer.batch;

import org.bmsource.dwh.common.AbstractFact;

public class Fact extends AbstractFact {

    public Fact() {
        super();
    }

    public Fact(String businessUnit, String transactionId) {
        this.businessUnit = businessUnit;
        this.transactionId = transactionId;
    }

    public String businessUnit;

    public String transactionId;

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
            "businessUnit='" + businessUnit + '\'' +
            ", transactionId='" + transactionId + '\'' +
            '}';
    }
}
