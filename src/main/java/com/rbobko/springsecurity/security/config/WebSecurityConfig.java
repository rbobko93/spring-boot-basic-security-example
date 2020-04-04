package com.rbobko.springsecurity.security.config;

import com.rbobko.springsecurity.security.auth.CustomAuthenticator;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticator customAuthenticator;

    public WebSecurityConfig(CustomAuthenticator customAuthenticator) {
        this.customAuthenticator = customAuthenticator;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticator);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic(); // Logout doesn't work with HttpBasic. That's how it was designed.

        // Disabling this stuff to allow anonymous access to H2 console
        http
                .csrf().disable()
                .headers().frameOptions().disable();
    }
}
