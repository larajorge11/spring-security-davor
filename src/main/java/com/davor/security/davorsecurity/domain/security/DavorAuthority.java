package com.davor.security.davorsecurity.domain.security;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "DavorAuthority")
@Table(name = "authority")
public class DavorAuthority {

    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Column(name = "authority_id", updatable = false)
    private UUID authorityId;

    private String permission;

    @ManyToMany(mappedBy = "authorities")
    private Set<DavorRole> roles;
}
