package com.example.mrizudev.democracyinfo.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/logging")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/homepage.html",true);
//                .failureUrl("/login.html?error=true")
//                .and()
//                .logout()
//                .logoutSuccessUrl("/index.html");
    }
}
