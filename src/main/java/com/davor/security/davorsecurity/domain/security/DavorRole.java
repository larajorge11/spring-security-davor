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
@Entity(name = "DavorRole")
@Table(name = "role")
public class DavorRole {
    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Column(name = "role_id", updatable = false)
    private UUID roleId;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<DavorUser> users;

    @Singular
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "role_authority", joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "role_id")},
    inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "authority_id")})
    private Set<DavorAuthority> authorities;
}
