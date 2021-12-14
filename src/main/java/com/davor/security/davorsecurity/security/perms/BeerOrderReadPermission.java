package com.davor.security.davorsecurity.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('order.read') OR " + "hasAuthority('customer.order.read') " +
        "AND @beerOrderAuthManager.customerIdMatches(authentication, #customerId)")
public @interface BeerOrderReadPermission {
}