package com.davor.security.davorsecurity.web.controllers.api;

import com.davor.security.davorsecurity.security.perms.BeerOrderReadPermission;
import com.davor.security.davorsecurity.services.BeerOrderService;
import com.davor.security.davorsecurity.web.model.BeerOrderDto;
import com.davor.security.davorsecurity.web.model.BeerOrderPagedList;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/customers/{customerId}/")
@RestController
public class BeerOrderController {
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerOrderService beerOrderService;

    public BeerOrderController(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    @BeerOrderReadPermission
    @GetMapping("orders")
    public BeerOrderPagedList listOrders(@PathVariable("customerId") UUID customerId,
                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return beerOrderService.listOrders(customerId, PageRequest.of(pageNumber, pageSize));
    }

    @PreAuthorize("hasAuthority('order.create') OR " + "hasAuthority('customer.order.create') " +
            "AND @beerOrderAuthManager.customerIdMatches(authentication, #customerId)")
    @PostMapping("orders")
    @ResponseStatus(HttpStatus.CREATED)
    public BeerOrderDto placeOrder(@PathVariable("customerId") UUID customerId, @RequestBody BeerOrderDto beerOrderDto){
        return beerOrderService.placeOrder(customerId, beerOrderDto);
    }

    @BeerOrderReadPermission
    @GetMapping("orders/{orderId}")
    public BeerOrderDto getOrder(@PathVariable("customerId") UUID customerId, @PathVariable("orderId") UUID orderId){
        return beerOrderService.getOrderById(customerId, orderId);
    }

    @PreAuthorize("hasAuthority('order.pickup') OR " + "hasAuthority('customer.order.pickup') " +
            "AND @beerOrderAuthManager.customerIdMatches(authentication, #customerId)")
    @PutMapping("/orders/{orderId}/pickup")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void pickupOrder(@PathVariable("customerId") UUID customerId, @PathVariable("orderId") UUID orderId){
        beerOrderService.pickupOrder(customerId, orderId);
    }
}
