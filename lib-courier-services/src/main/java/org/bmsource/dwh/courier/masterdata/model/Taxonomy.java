package org.bmsource.dwh.courier.masterdata.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "service_type_taxonomy")
public class Taxonomy implements Persistable<String> {

    @Id
    private String id;

    private Boolean expedite;

    private String name;

    @Column(name = "level_down_letter_key")
    private String serviceLevelDownType1;

    @Column(name = "level_down_parcel_key")
    private String serviceLevelDownType2;

    private String standardServiceTypeGroup;

    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
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

    public String getStandardServiceTypeGroup() {
        return standardServiceTypeGroup;
    }

    public void setStandardServiceTypeGroup(String standardServiceTypeGroup) {
        this.standardServiceTypeGroup = standardServiceTypeGroup;
    }
}
