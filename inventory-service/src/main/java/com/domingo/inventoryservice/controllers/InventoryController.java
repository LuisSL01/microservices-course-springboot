package com.domingo.inventoryservice.controllers;

import com.domingo.inventoryservice.model.dtos.BaseResponse;
import com.domingo.inventoryservice.model.dtos.InventoryRequest;
import com.domingo.inventoryservice.model.dtos.InventoryResponse;
import com.domingo.inventoryservice.model.dtos.OrderItemRequest;
import com.domingo.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku") String sku ){
        return this.inventoryService.isInStock(sku);
    }

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse areInStock(@RequestBody List<OrderItemRequest> orderItems){
        return this.inventoryService.areInStock(orderItems);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse addInventory(@RequestBody InventoryRequest inventoryRequest){
       return this.inventoryService.addInventory(inventoryRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> getAllInventory(){
        return this.inventoryService.getAllInventory();
    }
}
