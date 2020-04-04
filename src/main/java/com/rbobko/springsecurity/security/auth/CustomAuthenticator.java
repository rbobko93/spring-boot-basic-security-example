package com.rbobko.springsecurity.security.auth;

import com.rbobko.springsecurity.domain.User;
import com.rbobko.springsecurity.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CustomAuthenticator implements AuthenticationProvider {

    private final UserRepository userRepository;

    public CustomAuthenticator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userRepository.findByLogin(login).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Bad credentials");
        }

        return new UsernamePasswordAuthenticationToken(login, password, grantAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> grantAuthorities(User currentUser) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (currentUser.getLogin().equals("admin")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
