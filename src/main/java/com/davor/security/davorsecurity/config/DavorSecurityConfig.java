package com.davor.security.davorsecurity.config;

import com.davor.security.davorsecurity.security.DavorPasswordEncoderFactory;
import com.davor.security.davorsecurity.security.RestHeaderAuthenticationFilter;
import com.davor.security.davorsecurity.security.RestUrlAuthenticationFilter;
import com.davor.security.davorsecurity.services.security.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DavorSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JpaUserDetailsService userDetailsService;

    public RestHeaderAuthenticationFilter restHeaderAuthenticationFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthenticationFilter filter = new RestHeaderAuthenticationFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }


    public RestUrlAuthenticationFilter restUrlAuthenticationFilter(AuthenticationManager authenticationManager) {
        RestUrlAuthenticationFilter filter = new RestUrlAuthenticationFilter(new AntPathRequestMatcher("/api/**"));
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

        http.addFilterBefore(restUrlAuthenticationFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class).csrf().disable();

        http.authorizeRequests((requests) -> {
            requests.antMatchers("/h2/**").permitAll();
            requests.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
            requests.antMatchers("/beers/find")
                    .hasAnyRole("ADMIN", "CUSTOMER", "USER");
            requests.antMatchers(HttpMethod.GET, "/api/v1/beer/**")
                    .hasAnyRole("ADMIN", "CUSTOMER", "USER");
//            requests.mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**")
//                    .hasRole("ADMIN");
            requests.mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/{upc}")
                    .hasAnyRole("ADMIN", "CUSTOMER", "USER");
            requests.mvcMatchers( "/brewery/breweries")
                    .hasAnyRole("ADMIN", "CUSTOMER");
            requests.mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries")
                    .hasAnyRole("ADMIN", "CUSTOMER");
            //requests.mvcMatchers(HttpMethod.GET, "/api/v1/")
        })
                .authorizeRequests()
                        .anyRequest().authenticated()
                        .and()
                                .formLogin().and()
                        .httpBasic()
                                .and().csrf().disable();

        http.headers().frameOptions().sameOrigin();
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


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password("{sha256}8c105208827eaf331c69dd001f06cbb0586f657c499d1372c7c06ebebbb81271e462d643a87e86bb") // SHA516
//                .roles("ADMIN")
//                .and()
//                .withUser("jacobo")
//                .password("{bcrypt}$2a$10$XUyzkH.ARZI8Mf7gKpd5Vu66sCO9TSAF8wWpCRRnDYhEG9eDJepIS") // BCRYPT
//                .roles("USER");
//
//        auth.inMemoryAuthentication()
//                .withUser("jorge")
//                .password("{ldap}{SSHA}DdeLH07lXO3z99c83Dqp0oZVxS1qgS7Ej5AXZw==") // LDAP
//                .roles("READER");
//
//        // Exercise bcrypt encoder using a strength of 15
//        auth.inMemoryAuthentication()
//                .withUser("scott")
//                .password("{bcrypt15}$2a$15$ClFfAkuYtTQqiuzwSxN8tOQQZMLzrEfJ2GIgwwSWbIqJtppeGQ2tK") // bcrypt15
//                .roles("READER");
//
//
//    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }
}
