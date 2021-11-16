package com.davor.security.davorsecurity.config;

import com.davor.security.davorsecurity.security.DavorPasswordEncoderFactory;
import com.davor.security.davorsecurity.security.RestHeaderAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class DavorSecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthenticationFilter restHeaderAuthenticationFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthenticationFilter filter = new RestHeaderAuthenticationFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return DavorPasswordEncoderFactory.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(restHeaderAuthenticationFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class).csrf().disable();

        http.authorizeRequests((requests) -> {
            requests.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
            requests.antMatchers("/beers/find").permitAll();
            requests.antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll();
            requests.mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/{upc}").permitAll();
            requests.mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/{beerId}").permitAll();
            requests.antMatchers("/h2/**").permitAll();
            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)requests.anyRequest()).authenticated();
        });
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
        http.formLogin();
        http.httpBasic();
    }

//    @Bean
//    @Override
//    public UserDetailsService userDetailsServiceBean() throws Exception {
//        final UserDetails user1 = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//
//        final UserDetails user2 = User.withDefaultPasswordEncoder()
//                .username("jacobo")
//                .password("20210712")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("{sha256}8c105208827eaf331c69dd001f06cbb0586f657c499d1372c7c06ebebbb81271e462d643a87e86bb") // SHA516
                .roles("ADMIN")
                .and()
                .withUser("jacobo")
                .password("{bcrypt}$2a$10$9Nh10.qGlIl2G5LUIqKGwuzIppkPyPziP/a/g78qRFweTMNZKlad6") // BCRYPT
                .roles("USER");

        auth.inMemoryAuthentication()
                .withUser("jorge")
                .password("{ldap}{SSHA}DdeLH07lXO3z99c83Dqp0oZVxS1qgS7Ej5AXZw==") // LDAP
                .roles("READER");

        // Exercise bcrypt encoder using a strength of 15
        auth.inMemoryAuthentication()
                .withUser("scott")
                .password("{bcrypt15}$2a$15$ClFfAkuYtTQqiuzwSxN8tOQQZMLzrEfJ2GIgwwSWbIqJtppeGQ2tK") // bcrypt15
                .roles("READER");


    }
}
