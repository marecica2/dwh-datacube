package org.bmsource.dwh.multitenancy.groups;

import org.bmsource.dwh.multitenancy.AbstractRepositoryTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class GroupRepositoryTest extends AbstractRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupRepositoryTest.class);

    @Autowired
    private GroupRepository repository;

    @Test
    public void testInsert() {
        Group group = new Group("admin");
        LOGGER.info("aaa");
        repository.save(group);
        Optional<Group> savedGroup = repository.findById(1L);
        Assert.assertEquals("admin", savedGroup.get().getName());
    }

}
