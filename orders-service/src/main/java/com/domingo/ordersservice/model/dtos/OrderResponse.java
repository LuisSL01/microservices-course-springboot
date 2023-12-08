package com.domingo.ordersservice.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public record OrderResponse(Long id,String orderNumber, List<OrderItemResponse> orderItems) {

}
