package com.domingo.inventoryservice.services;

import com.domingo.inventoryservice.model.dtos.BaseResponse;
import com.domingo.inventoryservice.model.dtos.InventoryRequest;
import com.domingo.inventoryservice.model.dtos.InventoryResponse;
import com.domingo.inventoryservice.model.dtos.OrderItemRequest;
import com.domingo.inventoryservice.model.entities.Inventory;
import com.domingo.inventoryservice.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryResponse addInventory(InventoryRequest inventoryRequest){
        Inventory inventory = Inventory.builder()
                .sku(inventoryRequest.getSku())
                .build();
        inventoryRepository.save(inventory);
        log.info("inventory added: {}", inventory);
        return inventoryToInventoryResponse(inventory);
    }

    public List<InventoryResponse> getAllInventory(){
        return inventoryRepository.findAll().stream().map(this::inventoryToInventoryResponse).toList();
    }

    private InventoryResponse inventoryToInventoryResponse(Inventory inventory){
        return InventoryResponse.builder()
                .id(inventory.getId())
                .sku(inventory.getSku())
                .quantity(inventory.getQuantity())
                .build();
    }

    public boolean isInStock(String sku) {
        var inventory = inventoryRepository.findBySku(sku);
        return inventory.filter(value->value.getQuantity()>0).isPresent();
    }

    public BaseResponse areInStock(List<OrderItemRequest> orderItems) {
        var errorList = new ArrayList<String>();
        List<String> skus = orderItems.stream().map(OrderItemRequest::getSku).toList();
        List<Inventory> inventories = inventoryRepository.findBySkuIn(skus);

        orderItems.forEach(orderItem->{
            var inventory = inventories.stream().filter(value->value.getSku().equals(orderItem.getSku())).findFirst();
            if(inventory.isEmpty()){
                errorList.add("Product with sku: "+orderItem.getSku()+" does not exist");
            }else if (inventory.get().getQuantity() < orderItem.getQuantity()){
                errorList.add("Product with sku: "+orderItem.getSku()+" has insufficient quantity");
            }
        });

        return  new BaseResponse(errorList.toArray(new String[0]));
    }
}
