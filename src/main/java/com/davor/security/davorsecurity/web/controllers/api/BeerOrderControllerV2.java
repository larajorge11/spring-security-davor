package com.davor.security.davorsecurity.web.controllers.api;

import com.davor.security.davorsecurity.domain.security.DavorUser;
import com.davor.security.davorsecurity.security.perms.BeerOrderReadPermission;
import com.davor.security.davorsecurity.security.perms.BeerOrderReadPermissionV2;
import com.davor.security.davorsecurity.services.BeerOrderService;
import com.davor.security.davorsecurity.web.model.BeerOrderDto;
import com.davor.security.davorsecurity.web.model.BeerOrderPagedList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@RequestMapping("/api/v2/orders/")
@RestController
public class BeerOrderControllerV2 {
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerOrderService beerOrderService;

    public BeerOrderControllerV2(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    @BeerOrderReadPermissionV2
    @GetMapping
    public BeerOrderPagedList listOrders(@AuthenticationPrincipal DavorUser user,
                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (user.getCustomer() != null) {
            return beerOrderService.listOrders(user.getCustomer().getId(), PageRequest.of(pageNumber, pageSize));
        } else {
            // Is Admin User
            return beerOrderService.listOrders(PageRequest.of(pageNumber, pageSize));
        }

    }

    @BeerOrderReadPermissionV2
    @GetMapping("{orderId}")
    public BeerOrderDto getOrder(@PathVariable("orderId") UUID orderId){
        BeerOrderDto beerOrderDto =  beerOrderService.getOrderById(orderId);

        if (beerOrderDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden not found!");
        }

        log.debug("Order found: " + beerOrderDto);
        return beerOrderDto;
    }
}
