package org.bmsource.dwh.common.portal;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tenants", schema = "master")
public class Tenant {

    @Id
    @GenericGenerator(
            name = "assigned-sequence",
            strategy = "org.bmsource.dwh.common.portal.StringSequenceIdentifier",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "tenants_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "sequence_prefix", value = "tenant"),
            }
    )
    @GeneratedValue(generator = "assigned-sequence", strategy = GenerationType.SEQUENCE)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date modifiedOn;

    public Tenant() {
    }

    public Tenant(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }
}
