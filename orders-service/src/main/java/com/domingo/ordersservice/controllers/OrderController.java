package com.domingo.ordersservice.controllers;

import com.domingo.ordersservice.model.dtos.OrderRequest;
import com.domingo.ordersservice.model.dtos.OrderResponse;
import com.domingo.ordersservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addOrder(@RequestBody OrderRequest orderRequest){
        this.orderService.addOrder(orderRequest);
        return "Order created succesfully";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders(){
        return this.orderService.getAllOrders();
    }
}
