package org.bmsource.dwh.multitenancy.users;

import org.bmsource.dwh.multitenancy.test.utils.AbstractRepositoryTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Ignore
public class UserRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void testInsert() {

        User user1 = new User("John", "Doe");
        repository.save(user1);

        Optional<User> user2 = repository.findById(1L);
        Assert.assertEquals("John", user2.get().getFirstName());
    }

}
