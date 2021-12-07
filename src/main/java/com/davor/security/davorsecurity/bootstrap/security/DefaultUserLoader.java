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

        // Beer Orders
        DavorAuthority createOrder = DavorAuthority.builder().permission("order.create").build();
        DavorAuthority readOrder = DavorAuthority.builder().permission("order.read").build();
        DavorAuthority updateOrder = DavorAuthority.builder().permission("order.update").build();
        DavorAuthority deleteOrder = DavorAuthority.builder().permission("order.delete").build();

        DavorAuthority createCustomerOrder = DavorAuthority.builder().permission("customer.order.create").build();
        DavorAuthority readCustomerOrder = DavorAuthority.builder().permission("customer.order.read").build();
        DavorAuthority updateCustomerOrder = DavorAuthority.builder().permission("customer.order.update").build();
        DavorAuthority deleteCustomerOrder = DavorAuthority.builder().permission("customer.order.delete").build();

        authorityRepository.saveAll(List.of(createOrder, readOrder, updateOrder, deleteOrder,
                createCustomerOrder, readCustomerOrder, updateCustomerOrder, deleteCustomerOrder));

        // Saving Roles
        DavorRole adminRole = DavorRole.builder().name("ADMIN").build();
        DavorRole customerRole = DavorRole.builder().name("CUSTOMER").build();
        DavorRole userRole = DavorRole.builder().name("USER").build();

        // Admin Authorities
        Set<DavorAuthority> beerAdminAuthorities = Set.of(createBeer, readBeer, updateBeer, deleteBeer);
        Set<DavorAuthority> breweryAdminAuthorities = Set.of(createBrewery, readBrewery, updateBrewery, deleteBrewery);
        Set<DavorAuthority> customerAdminAuthorities = Set.of(createCustomer, readCustomer, updateCustomer, deleteCustomer);
        Set<DavorAuthority> orderAdminAuthorities = Set.of(createOrder, readOrder, updateOrder, deleteOrder);

        // Customer Authorities
        Set<DavorAuthority> beerCustomerAuthorities = Set.of(readBeer);
        Set<DavorAuthority> breweryCustomerAuthorities = Set.of(readBrewery);
        Set<DavorAuthority> customerCustomerAuthorities = Set.of(readCustomer);
        Set<DavorAuthority> orderCustomerAuthorities = Set.of(createCustomerOrder, readCustomerOrder, updateCustomerOrder, deleteCustomerOrder);

        // User Authorities
        Set<DavorAuthority> beerUserAuthorities = Set.of(readBeer);

       var setAdminAuthorities = new HashSet<Set<DavorAuthority>>();
        setAdminAuthorities.addAll(Set.of(
                beerAdminAuthorities,
                breweryAdminAuthorities,
                customerAdminAuthorities,
                orderAdminAuthorities));
        adminRole.setAuthorities(getDavorAuthorities(setAdminAuthorities));

        var setCustomerAuthorities = new HashSet<Set<DavorAuthority>>();
        setCustomerAuthorities.addAll(Set.of(
                beerCustomerAuthorities,
                breweryCustomerAuthorities,
                customerCustomerAuthorities,
                orderCustomerAuthorities));
        customerRole.setAuthorities(getDavorAuthorities(setCustomerAuthorities));

        var setUserAuthorities = new HashSet<Set<DavorAuthority>>();
        setUserAuthorities.addAll(Set.of(
                beerUserAuthorities));
        userRole.setAuthorities(getDavorAuthorities(setUserAuthorities));

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

    private Set<DavorAuthority> getDavorAuthorities(Set<Set<DavorAuthority>> authorities) {
        return authorities.stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
