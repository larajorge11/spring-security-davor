package com.davor.security.davorsecurity.repositories.security;

import com.davor.security.davorsecurity.domain.security.DavorAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<DavorAuthority, UUID> {
}
