package org.bmsource.dwh.security;

import org.bmsource.dwh.security.model.User;
import org.bmsource.dwh.security.model.UserDto;
import org.bmsource.dwh.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SecurityController {

    @Autowired
    UserServiceImpl userService;

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
