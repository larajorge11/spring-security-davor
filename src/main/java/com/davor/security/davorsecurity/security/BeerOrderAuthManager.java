package com.davor.security.davorsecurity.security;

import com.davor.security.davorsecurity.domain.security.DavorUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class BeerOrderAuthManager {

    public boolean customerIdMatches(Authentication authentication, UUID customerId) {
        DavorUser authenticationUser = (DavorUser) authentication.getPrincipal();
        log.debug("Auth user customer id: " + authenticationUser.getCustomer().getId() + "Customer Id: " + customerId);

        return authenticationUser.getCustomer().getId().equals(customerId);
    }
}
