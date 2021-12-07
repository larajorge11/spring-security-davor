package com.davor.security.davorsecurity.services.security;

import com.davor.security.davorsecurity.domain.security.DavorUser;
import com.davor.security.davorsecurity.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DavorUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not found"));
        return user;
    }
}
