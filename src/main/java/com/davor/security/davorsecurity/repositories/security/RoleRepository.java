package com.davor.security.davorsecurity.repositories.security;

import com.davor.security.davorsecurity.domain.security.DavorRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<DavorRole, UUID> {

    Optional<DavorRole> findByName(String customer);
}
