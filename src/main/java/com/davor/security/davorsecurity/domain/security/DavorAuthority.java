package com.davor.security.davorsecurity.domain.security;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "DavorAuthority")
@Table(name = "authority")
public class DavorAuthority {

    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Column(name = "authority_id", updatable = false)
    private UUID authorityId;

    @ManyToMany(mappedBy = "authorities")
    private String role;
}
