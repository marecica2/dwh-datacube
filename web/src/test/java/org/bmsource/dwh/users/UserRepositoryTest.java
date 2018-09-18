package org.bmsource.dwh.users;

import org.bmsource.dwh.AbstractRepositoryTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

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
