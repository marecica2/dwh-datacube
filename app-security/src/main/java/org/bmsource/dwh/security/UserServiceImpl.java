package org.bmsource.dwh.security;

import org.bmsource.dwh.security.model.Role;
import org.bmsource.dwh.security.model.User;
import org.bmsource.dwh.security.model.UserDto;
import org.bmsource.dwh.security.repository.RoleRepository;
import org.bmsource.dwh.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println(userId);
        User user = userRepository.findByUsername(userId);
        if (user == null) {
            log.error("Invalid username or password.");
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Set<SimpleGrantedAuthority> grantedAuthorities = getAuthorities(user);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
            grantedAuthorities);
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<Role> roleByUserId = user.getRoles();
        return roleByUserId
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString().toUpperCase()))
            .collect(Collectors.toSet());
    }

    @Override
    public UserDto save(UserDto userDto) {
        userDto.setRoles(Collections.singletonList("USER"));
        User userWithDuplicateUsername = userRepository.findByUsername(userDto.getUsername());
        if (userWithDuplicateUsername != null && userDto.getId() != userWithDuplicateUsername.getId()) {
            log.error(String.format("Duplicate username %s", userDto.getUsername()));
            throw new SecurityException("Duplicate username.");
        }
        User userWithDuplicateEmail = userRepository.findByEmail(userDto.getEmail());
        if (userWithDuplicateEmail != null && userDto.getId() != userWithDuplicateEmail.getId()) {
            log.error(String.format("Duplicate email %s", userDto.getEmail()));
            throw new SecurityException("Duplicate email.");
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRoles(roleRepository.find( Collections.singletonList("USER")));
        userRepository.save(user);
        return userDto;
    }
}
