package org.bmsource.dwh.users;

import org.bmsource.dwh.DwhApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@ContextConfiguration(classes = {DwhApplication.class})
@TestPropertySource(locations = "classpath:/application.yml")
public class UserRepositoryTest {

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
