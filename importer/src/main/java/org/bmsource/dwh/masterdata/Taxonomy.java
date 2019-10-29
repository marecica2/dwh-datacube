package org.bmsource.dwh.masterdata;

import org.bmsource.dwh.common.BaseFact;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@Table(name = "service_type_taxonomy")
public class Taxonomy extends BaseFact {

    @Id
    private String id;

    private Boolean expedite;

    private String name;

    private String serviceLevelDownType1;

    private String serviceLevelDownType2;

    private String discountGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getExpedite() {
        return expedite;
    }

    public void setExpedite(Boolean expedite) {
        this.expedite = expedite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceLevelDownType1() {
        return serviceLevelDownType1;
    }

    public void setServiceLevelDownType1(String serviceLevelDownType1) {
        this.serviceLevelDownType1 = serviceLevelDownType1;
    }

    public String getServiceLevelDownType2() {
        return serviceLevelDownType2;
    }

    public void setServiceLevelDownType2(String serviceLevelDownType2) {
        this.serviceLevelDownType2 = serviceLevelDownType2;
    }

    public String getDiscountGroup() {
        return discountGroup;
    }

    public void setDiscountGroup(String discountGroup) {
        this.discountGroup = discountGroup;
    }
}
