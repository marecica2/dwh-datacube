package org.bmsource.dwh.multitenancy.users;

import org.bmsource.dwh.multitenancy.AbstractRepositoryTest;
import org.bmsource.dwh.multitenancy.groups.Group;
import org.bmsource.dwh.multitenancy.groups.GroupRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void testInsert() {
        User user1 = new User("John", "Doe");
        user1 = userRepository.save(user1);

        Optional<User> user2 = userRepository.findById(user1.getId());
        Assert.assertEquals("John", user2.get().getFirstName());
    }

    @Test
    public void testAddToGroup() {
        Group group = new Group("admin");
        group = groupRepository.save(group);

        User user1 = new User("John", "Doe");
        user1 = userRepository.save(user1);

        Assert.assertEquals("John", user1.getFirstName());
        Assert.assertTrue(user1.getGroups().size() == 0);

        user1.getGroups().add(group);
        User user2 = userRepository.save(user1);
        Assert.assertEquals("admin", user2.getGroups().iterator().next().getName());
    }

}
