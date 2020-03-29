package org.bmsource.dwh.security.web;

import org.bmsource.dwh.security.UserServiceImpl;
import org.bmsource.dwh.security.model.User;
import org.bmsource.dwh.security.model.UserDto;
import org.bmsource.dwh.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SecurityController {

    UserServiceImpl userService;

    UserRepository userRepository;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    public UserDto currentLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).toUserDto();
    }

    @PostMapping(value = "/register")
    public HttpEntity<HttpStatus> register(@RequestBody UserDto userDto) {
        userService.save(userDto);
        return new HttpEntity<>(HttpStatus.CREATED);
    }
}
