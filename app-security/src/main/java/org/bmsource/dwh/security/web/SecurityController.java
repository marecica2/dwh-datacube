package org.bmsource.dwh.security.web;

import org.bmsource.dwh.security.UserServiceImpl;
import org.bmsource.dwh.security.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SecurityController {

    UserServiceImpl userService;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    public UserDetails currentUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = userService.loadUserByUsername(auth.getName());
        return user;
    }

    @PostMapping(value = "/register")
    public HttpEntity<HttpStatus> register(@RequestBody UserDto userDto) {
        userService.save(userDto);
        return new HttpEntity<>(HttpStatus.CREATED);
    }
}
