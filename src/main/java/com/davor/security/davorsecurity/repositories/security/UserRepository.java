package com.davor.security.davorsecurity.repositories.security;

import com.davor.security.davorsecurity.domain.security.DavorUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<DavorUser, UUID> {
    Optional<DavorUser> findByUsername(String username);
}
