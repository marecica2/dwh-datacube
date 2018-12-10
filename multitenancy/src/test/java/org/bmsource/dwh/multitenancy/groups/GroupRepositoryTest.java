package org.bmsource.dwh.multitenancy.groups;

import org.bmsource.dwh.multitenancy.AbstractRepositoryTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class GroupRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private GroupRepository repository;

    @Test
    public void testInsert() {
        Group group = new Group("admin");
        repository.save(group);
        Optional<Group> savedGroup = repository.findById(1L);
        Assert.assertEquals("admin", savedGroup.get().getName());
    }

}
