package org.bmsource.dwh.users;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("it")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIT {

    @Autowired
    private UserRepository repository;

    @Test
    public void testStoreUser() {
        User user = new User("Marek", "Balla");
        repository.save(user);
        Assert.assertThat(user.getId(), is(1L));
    }
}