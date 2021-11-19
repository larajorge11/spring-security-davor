package com.davor.security.davorsecurity.bootstrap.security;

import com.davor.security.davorsecurity.domain.security.DavorAuthority;
import com.davor.security.davorsecurity.domain.security.DavorUser;
import com.davor.security.davorsecurity.repositories.security.AuthorityRepository;
import com.davor.security.davorsecurity.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultUserLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            loadUserData();
        }
    }

    private void loadUserData() {
        DavorAuthority authorityAdmin = DavorAuthority.builder()
                .role("ADMIN")
                .build();

        DavorAuthority authorityUser = DavorAuthority.builder()
                .role("USER")
                .build();

        DavorAuthority authorityCustomer = DavorAuthority.builder()
                .role("CUSTOMER")
                .build();

        authorityRepository.saveAll(List.of(authorityAdmin, authorityUser, authorityCustomer));

        DavorUser user1 = DavorUser.builder()
                .username("spring")
                .password(encoder.encode("davor1989"))
                .authorities(new HashSet<>(Set.of(authorityAdmin)))
                .build();

        DavorUser user2 = DavorUser.builder()
                .username("user")
                .password(encoder.encode("davor1989"))
                .authorities(new HashSet<>(Set.of(authorityUser)))
                .build();

        DavorUser user3 = DavorUser.builder()
                .username("spring")
                .password(encoder.encode("davor1989"))
                .authorities(new HashSet<>(Set.of(authorityCustomer)))
                .build();

        userRepository.saveAll(List.of(user1, user2, user3));
    }
}
