/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.davor.security.davorsecurity.bootstrap;

import com.davor.security.davorsecurity.domain.*;
import com.davor.security.davorsecurity.domain.security.DavorAuthority;
import com.davor.security.davorsecurity.domain.security.DavorRole;
import com.davor.security.davorsecurity.domain.security.DavorUser;
import com.davor.security.davorsecurity.repositories.*;
import com.davor.security.davorsecurity.repositories.security.AuthorityRepository;
import com.davor.security.davorsecurity.repositories.security.RoleRepository;
import com.davor.security.davorsecurity.repositories.security.UserRepository;
import com.davor.security.davorsecurity.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    public static final String TECNI_RODAMIENTOS = "Tecni-Rodamientos LTDA";
    public static final String PETS_GROOMING = "Pets Grooming";
    public static final String BRANDIO = "Brand io";

    public static final String USER_TECNI = "tecni";
    public static final String USER_PETS = "pets";
    public static final String USER_BRANDIO = "io";

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        loadUserData();
        loadBreweryData();
        loadTastingRoomData();
        loadCustomerData();
    }

    private void loadCustomerData() {
        DavorRole customerRole = roleRepository.findByName("CUSTOMER").orElseThrow();

        // Create customers
        Customer tecnirodamientosCustomer = Customer.builder()
                .customerName(TECNI_RODAMIENTOS)
                .apiKey(UUID.randomUUID())
                .build();

        Customer petsgroomingCustomer = Customer.builder()
                .customerName(PETS_GROOMING)
                .apiKey(UUID.randomUUID())
                .build();

        Customer brandioCustomer = Customer.builder()
                .customerName(BRANDIO)
                .apiKey(UUID.randomUUID())
                .build();

        customerRepository.saveAll(List.of(tecnirodamientosCustomer, petsgroomingCustomer, brandioCustomer));

        // Create Users
        DavorUser tecnirodamientosUser = DavorUser.builder()
                .username(USER_TECNI)
                .password(encoder.encode("davor1989"))
                .customer(tecnirodamientosCustomer)
                .role(customerRole)
                .build();

        DavorUser petsgroomingUser = DavorUser.builder()
                .username(USER_PETS)
                .password(encoder.encode("davor1989"))
                .customer(petsgroomingCustomer)
                .role(customerRole)
                .build();

        DavorUser brandioUser = DavorUser.builder()
                .username(USER_BRANDIO)
                .password(encoder.encode("davor1989"))
                .customer(brandioCustomer)
                .role(customerRole)
                .build();

        userRepository.saveAll(List.of(tecnirodamientosUser, petsgroomingUser, brandioUser));

        // Create Orders
        createOrder(tecnirodamientosCustomer, 2);
        createOrder(petsgroomingCustomer, 5);
        createOrder(brandioCustomer, 3);
    }

    private void createOrder(Customer customer, Integer numberOrder) {
        beerOrderRepository.save(BeerOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatusEnum.NEW)
                .beerOrderLines(Set.of(BeerOrderLine.builder()
                        .beer(beerRepository.findByUpc(BEER_1_UPC))
                        .orderQuantity(numberOrder)
                        .build()))
                .build());
    }

    private void loadTastingRoomData() {
        Customer tastingRoom = Customer.builder()
                .customerName(TASTING_ROOM)
                .apiKey(UUID.randomUUID())
                .build();

        customerRepository.save(tastingRoom);

        beerRepository.findAll().forEach(beer -> {
            beerOrderRepository.save(BeerOrder.builder()
                    .customer(tastingRoom)
                    .orderStatus(OrderStatusEnum.NEW)
                    .beerOrderLines(Set.of(BeerOrderLine.builder()
                            .beer(beer)
                            .orderQuantity(2)
                            .build()))
                    .build());
        });
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());

            Beer mangoBobs = Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_1_UPC)
                    .build();

            beerRepository.save(mangoBobs);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(mangoBobs)
                    .quantityOnHand(500)
                    .build());

            Beer galaxyCat = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .build();

            beerRepository.save(galaxyCat);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(galaxyCat)
                    .quantityOnHand(500)
                    .build());

            Beer pinball = Beer.builder()
                    .beerName("Pinball Porter")
                    .beerStyle(BeerStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_3_UPC)
                    .build();

            beerRepository.save(pinball);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(pinball)
                    .quantityOnHand(500)
                    .build());

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
        DavorAuthority pickupOrder = DavorAuthority.builder().permission("order.pickup").build();

        DavorAuthority createCustomerOrder = DavorAuthority.builder().permission("customer.order.create").build();
        DavorAuthority readCustomerOrder = DavorAuthority.builder().permission("customer.order.read").build();
        DavorAuthority updateCustomerOrder = DavorAuthority.builder().permission("customer.order.update").build();
        DavorAuthority deleteCustomerOrder = DavorAuthority.builder().permission("customer.order.delete").build();
        DavorAuthority pickupCustomerOrder = DavorAuthority.builder().permission("customer.order.pickup").build();

        authorityRepository.saveAll(List.of(createOrder, readOrder, updateOrder, deleteOrder, pickupOrder,
                createCustomerOrder, readCustomerOrder, updateCustomerOrder, deleteCustomerOrder, pickupCustomerOrder));

        // Saving Roles
        DavorRole adminRole = DavorRole.builder().name("ADMIN").build();
        DavorRole customerRole = DavorRole.builder().name("CUSTOMER").build();
        DavorRole userRole = DavorRole.builder().name("USER").build();

        // Admin Authorities
        Set<DavorAuthority> beerAdminAuthorities = Set.of(createBeer, readBeer, updateBeer, deleteBeer);
        Set<DavorAuthority> breweryAdminAuthorities = Set.of(createBrewery, readBrewery, updateBrewery, deleteBrewery);
        Set<DavorAuthority> customerAdminAuthorities = Set.of(createCustomer, readCustomer, updateCustomer, deleteCustomer);
        Set<DavorAuthority> orderAdminAuthorities = Set.of(createOrder, readOrder, updateOrder, deleteOrder, pickupOrder);

        // Customer Authorities
        Set<DavorAuthority> beerCustomerAuthorities = Set.of(readBeer);
        Set<DavorAuthority> breweryCustomerAuthorities = Set.of(readBrewery);
        Set<DavorAuthority> customerCustomerAuthorities = Set.of(readCustomer);
        Set<DavorAuthority> orderCustomerAuthorities = Set.of(createCustomerOrder, readCustomerOrder, updateCustomerOrder,
                deleteCustomerOrder, pickupCustomerOrder);

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
