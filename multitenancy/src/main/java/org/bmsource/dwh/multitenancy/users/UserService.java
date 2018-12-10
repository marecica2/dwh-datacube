package org.bmsource.dwh.multitenancy.users;

import org.bmsource.dwh.multitenancy.groups.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
@Transactional
public class UserService {

    @Autowired
    UserRepository repository;

    public User getUser(long id) {
        Optional<User> user = repository.findById(id);
        return user.isPresent() ? user.get() : null;
    }

    public User createUser(User user) {
        return repository.save(user);
    }

    public User addUserToGroup(User user, Group group) {
        user.getGroups().add(group);
        return repository.save(user);
    }

    public User removeUserFromGroup(User user, Group group) {
        user.getGroups().remove(group);
        return repository.save(user);
    }
}
