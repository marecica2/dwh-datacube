package org.bmsource.dwh.security.model;

import javax.persistence.*;

@Entity
@Table(name = "roles", schema = "master")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private RoleType name;
    private String description;
    private Long createdOn;
    private Long modifiedOn;

    public RoleType getName() {
        return name;
    }
}
