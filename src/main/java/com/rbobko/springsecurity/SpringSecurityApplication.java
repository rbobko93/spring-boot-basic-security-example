package com.rbobko.springsecurity;

import com.rbobko.springsecurity.domain.User;
import com.rbobko.springsecurity.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
public class SpringSecurityApplication {

    private final UserRepository userRepository;

    public SpringSecurityApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

    @PostConstruct
    private void init() {
        User admin = new User("admin", "admin");
        User user = new User("user", "user");

        userRepository.saveAll(Arrays.asList(admin, user));
    }

}
