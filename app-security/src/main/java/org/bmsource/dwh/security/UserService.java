package org.bmsource.dwh.security;

import org.bmsource.dwh.security.model.User;
import org.bmsource.dwh.security.model.UserDto;

import java.util.List;

public interface UserService {

    UserDto save(UserDto user);
//    List<UserDto> findAll();
//    User findOne(long id);
//    void delete(long id);
}
