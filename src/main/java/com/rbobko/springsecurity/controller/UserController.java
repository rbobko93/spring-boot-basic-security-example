package com.rbobko.springsecurity.controller;

import com.rbobko.springsecurity.domain.User;
import com.rbobko.springsecurity.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getLoggedUser(Principal principal) {
        String login = principal.getName();
        return userRepository.findByLogin(login).orElseThrow(() -> new EntityNotFoundException("User not found."));
    }
}
