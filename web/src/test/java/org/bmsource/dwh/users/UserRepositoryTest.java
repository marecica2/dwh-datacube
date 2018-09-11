package org.bmsource.dwh.users;

import org.bmsource.dwh.DwhConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import org.junit.Assert;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {DwhConfiguration.class},
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class UserRepositoryTest {

    @Resource
    private UserRepository repository;

    @Test
    public void testInsert() {
        User user1 = new User("John", "Doe");
        repository.save(user1);

        Optional<User> user2 = repository.findById(1L);
        Assert.assertEquals("John", user2.get().getFirstName());
    }

}
