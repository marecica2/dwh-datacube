package org.bmsource.dwh.multitenancy.group;

import org.bmsource.dwh.multitenancy.users.User;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@Transactional
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToMany(mappedBy = "groups")
    private Set<User> users = new HashSet<>();

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
