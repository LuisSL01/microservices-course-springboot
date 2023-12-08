package com.domingo.ordersservice.services;

import com.domingo.ordersservice.model.dtos.*;
import com.domingo.ordersservice.model.entities.Order;
import com.domingo.ordersservice.model.entities.OrderItem;
import com.domingo.ordersservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    public void addOrder(OrderRequest orderRequest){
        //check inventory
        BaseResponse result =  this.webClientBuilder.build()
                .post()
                .uri("http://localhost:8083/api/inventory/in-stock")
                .bodyValue(orderRequest.getOrderItemsRequests())
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .block();

        if(result != null && !result.hasErrors()){
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setOrderItems(orderRequest.getOrderItemsRequests().stream()
                    .map(orderItemRequest -> mapOrderItemsRequestToOrderItems(orderItemRequest, order))
                    .toList());
            orderRepository.save(order);
            log.info("order added, id: {}, orderNumber: {}", order.getId(), order.getOrderNumber());
        }else{
            throw new IllegalArgumentException("Some of the products are not in stock");
        }


    }
    public List<OrderResponse> getAllOrders(){
        return orderRepository.findAll().stream().map(this::orderToOrderResponse).toList();
    }

    private OrderItem mapOrderItemsRequestToOrderItems(OrderItemRequest orderItemRequest, Order order){
        return OrderItem.builder()
                .sku(orderItemRequest.getSku())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .order(order)
                .build();
    }

    private OrderResponse orderToOrderResponse(Order order){
        return new OrderResponse(order.getId(), order.getOrderNumber(),
                order.getOrderItems().stream().map(this::orderItemToOrderItemResponse).toList());
    }

    private OrderItemResponse orderItemToOrderItemResponse(OrderItem orderItem){
        return new OrderItemResponse(orderItem.getId(), orderItem.getSku(), orderItem.getPrice(), orderItem.getQuantity());
    }

}

