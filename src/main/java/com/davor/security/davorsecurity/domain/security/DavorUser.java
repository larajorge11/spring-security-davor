package com.davor.security.davorsecurity.domain.security;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "DavorUser")
@Table(name = "user")
public class DavorUser {

    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    @Column(name = "user_id", updatable = false)
    private UUID userId;

    private String username;
    private String password;

    @Singular
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "role_id")})
    private Set<DavorRole> roles;

    @Transient
    private Set<DavorAuthority> authorities;

    public Set<DavorAuthority> getAuthorities() {
        return this.roles.stream()
                .map(DavorRole::getAuthorities)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Builder.Default
    private boolean enabled = true;
}
