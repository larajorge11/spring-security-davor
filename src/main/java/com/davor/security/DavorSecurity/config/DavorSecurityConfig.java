package com.davor.security.DavorSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class DavorSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests((requests) -> {
            requests.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
            requests.antMatchers("/beers/find").permitAll();
            requests.antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll();
            requests.mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/{upc}").permitAll();
            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)requests.anyRequest()).authenticated();
        });
        http.formLogin();
        http.httpBasic();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        final UserDetails user1 = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("ADMIN")
                .build();

        final UserDetails user2 = User.withDefaultPasswordEncoder()
                .username("jacobo")
                .password("20210712")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
}
