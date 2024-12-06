package com.example.service;

import com.example.dto.OrderDto;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderTrackingNumber(orderDto.getOrderTrackingNumber());
        order.setTotalQuantity(orderDto.getTotalQuantity());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setStatus(orderDto.getStatus());
        LocalDateTime now = LocalDateTime.now();
        Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        order.setDateCreated(currentDate);
        order.setLastUpdated(currentDate);

        orderDto.setDateCreated(currentDate);
        orderDto.setLastUpdated(currentDate);

        Set<OrderItem> orderItems = new HashSet<>();
        if (orderDto.getOrderItem() != null) {
            for (OrderDto.OrderItemDto itemDto : orderDto.getOrderItem()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setImageUrl(itemDto.getImageUrl());
                orderItem.setPrice(itemDto.getPrice());
                orderItem.setQuantity(itemDto.getQuantity());
                orderItems.add(orderItem);
                itemDto.setId(orderItem.getId());
            }
        }
        order.setOrderItem(orderItems);

        order = orderRepository.save(order);
        orderDto.setId(order.getId());
        return orderDto;
    }
}
