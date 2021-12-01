package com.davor.security.davorsecurity.bootstrap.security;

import com.davor.security.davorsecurity.domain.security.DavorAuthority;
import com.davor.security.davorsecurity.domain.security.DavorRole;
import com.davor.security.davorsecurity.domain.security.DavorUser;
import com.davor.security.davorsecurity.repositories.security.AuthorityRepository;
import com.davor.security.davorsecurity.repositories.security.RoleRepository;
import com.davor.security.davorsecurity.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultUserLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            loadUserData();
        }
    }

    private void loadUserData() {
        // Beer
        DavorAuthority createBeer = DavorAuthority.builder().permission("beer.create").build();
        DavorAuthority readBeer = DavorAuthority.builder().permission("beer.read").build();
        DavorAuthority updateBeer = DavorAuthority.builder().permission("beer.update").build();
        DavorAuthority deleteBeer = DavorAuthority.builder().permission("beer.delete").build();

        authorityRepository.saveAll(List.of(createBeer, readBeer, updateBeer, deleteBeer));

        // Customer
        DavorAuthority createCustomer = DavorAuthority.builder().permission("customer.create").build();
        DavorAuthority readCustomer = DavorAuthority.builder().permission("customer.read").build();
        DavorAuthority updateCustomer = DavorAuthority.builder().permission("customer.update").build();
        DavorAuthority deleteCustomer = DavorAuthority.builder().permission("customer.delete").build();

        authorityRepository.saveAll(List.of(createCustomer, readCustomer, updateCustomer, deleteCustomer));

        // Brewery
        DavorAuthority createBrewery = DavorAuthority.builder().permission("brewery.create").build();
        DavorAuthority readBrewery = DavorAuthority.builder().permission("brewery.read").build();
        DavorAuthority updateBrewery = DavorAuthority.builder().permission("brewery.update").build();
        DavorAuthority deleteBrewery = DavorAuthority.builder().permission("brewery.delete").build();

        authorityRepository.saveAll(List.of(createBrewery, readBrewery, updateBrewery, deleteBrewery));

        // Saving Roles
        DavorRole adminRole = DavorRole.builder().name("ADMIN").build();
        DavorRole customerRole = DavorRole.builder().name("CUSTOMER").build();
        DavorRole userRole = DavorRole.builder().name("USER").build();

        Set<DavorAuthority> beerAdminAuthorities = Set.of(createBeer, readBeer, updateBeer, deleteBeer);
        Set<DavorAuthority> breweryAdminAuthorities = Set.of(createBeer, readBeer, updateBeer, deleteBeer);
        Set<DavorAuthority> customerAdminAuthorities = Set.of(createBeer, readBeer, updateBeer, deleteBeer);

        Set<DavorAuthority> beerCustomerAuthorities = Set.of(readBeer);
        Set<DavorAuthority> breweryCustomerAuthorities = Set.of(readBeer);
        Set<DavorAuthority> customerCustomerAuthorities = Set.of(readBeer);

        Set<DavorAuthority> beerUserAuthorities = Set.of(readBeer);
        Set<DavorAuthority> breweryUserAuthorities = new HashSet<>();
        Set<DavorAuthority> customerUserAuthorities = new HashSet<>();

        adminRole.setAuthorities(new HashSet<>(getDavorAuthorities(
                beerAdminAuthorities,
                breweryAdminAuthorities,
                customerAdminAuthorities)));

        customerRole.setAuthorities(new HashSet<>(getDavorAuthorities(
                beerCustomerAuthorities,
                breweryCustomerAuthorities,
                customerCustomerAuthorities)));

        userRole.setAuthorities(new HashSet<>(getDavorAuthorities(
                beerUserAuthorities,
                breweryUserAuthorities,
                customerUserAuthorities)));

        roleRepository.saveAll(List.of(adminRole, customerRole, userRole));

        DavorUser user1 = DavorUser.builder()
                .username("jorge")
                .password(encoder.encode("davor1989"))
                .role(adminRole)
                .build();

        DavorUser user2 = DavorUser.builder()
                .username("jacobo")
                .password(encoder.encode("davor1989"))
                .role(userRole)
                .build();

        DavorUser user3 = DavorUser.builder()
                .username("davor")
                .password(encoder.encode("davor1989"))
                .role(customerRole)
                .build();

        userRepository.saveAll(List.of(user1, user2, user3));
    }

    private Set<DavorAuthority> getDavorAuthorities(Set<DavorAuthority> beerAuthorities,
                                                    Set<DavorAuthority> breweryAuthorities,
                                                    Set<DavorAuthority> customerAuthorities) {
        return Stream.of(
                beerAuthorities,
                breweryAuthorities,
                customerAuthorities)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
