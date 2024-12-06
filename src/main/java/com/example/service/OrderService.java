package com.example.service;

import com.example.dto.OrderDto;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setOrderTrackingNumber(order.getOrderTrackingNumber());
            orderDto.setTotalQuantity(order.getTotalQuantity());
            orderDto.setTotalPrice(order.getTotalPrice());
            orderDto.setStatus(order.getStatus());
            orderDto.setDateCreated(order.getDateCreated());
            orderDto.setLastUpdated(order.getLastUpdated());

            List<OrderDto.OrderItemDto> itemDtos = order.getOrderItem().stream().map(orderItem -> {
                OrderDto.OrderItemDto itemDto = new OrderDto.OrderItemDto();
                itemDto.setId(orderItem.getId());
                itemDto.setImageUrl(orderItem.getImageUrl());
                itemDto.setPrice(orderItem.getPrice());
                itemDto.setQuantity(orderItem.getQuantity());
                return itemDto;
            }).toList();

            orderDto.setOrderItem(itemDtos);
            return orderDto;
        }).toList();
    }

    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderTrackingNumber(orderDto.getOrderTrackingNumber());
        order.setTotalQuantity(orderDto.getTotalQuantity());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setStatus(orderDto.getStatus());
        LocalDateTime now = LocalDateTime.now();
        order.setDateCreated(now);
        order.setLastUpdated(now);

        orderDto.setDateCreated(now);
        orderDto.setLastUpdated(now);

        Set<OrderItem> orderItems = new HashSet<>();
        if (orderDto.getOrderItem() != null) {
            for (OrderDto.OrderItemDto itemDto : orderDto.getOrderItem()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setImageUrl(itemDto.getImageUrl());
                orderItem.setPrice(itemDto.getPrice());
                orderItem.setQuantity(itemDto.getQuantity());
                orderItems.add(orderItem);
            }
        }
        order.setOrderItem(orderItems);
        order = orderRepository.save(order);
        orderDto.setId(order.getId());

        if (orderDto.getOrderItem() != null) {
            int index = 0;
            for (OrderItem orderItem : order.getOrderItem()) {
                OrderDto.OrderItemDto itemDto = orderDto.getOrderItem().get(index);
                itemDto.setId(orderItem.getId());
                index++;
            }
        }

        return orderDto;
    }

    public OrderDto updateOrder(Long orderId, OrderDto orderDto) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        Order existingOrder = optionalOrder.get();

        // Update basic fields
        existingOrder.setOrderTrackingNumber(orderDto.getOrderTrackingNumber());
        existingOrder.setTotalQuantity(orderDto.getTotalQuantity());
        existingOrder.setTotalPrice(orderDto.getTotalPrice());
        existingOrder.setStatus(orderDto.getStatus());
        LocalDateTime now = LocalDateTime.now();
        existingOrder.setLastUpdated(now);
        orderDto.setLastUpdated(now);

        // Update order items
        Set<OrderItem> updatedOrderItems = new HashSet<>();
        if (orderDto.getOrderItem() != null) {
            for (OrderDto.OrderItemDto itemDto : orderDto.getOrderItem()) {
                OrderItem orderItem = new OrderItem();
                if (itemDto.getId() != null) {
                    // Check if the item exists in the current order
                    OrderItem existingItem = existingOrder.getOrderItem().stream()
                            .filter(i -> i.getId().equals(itemDto.getId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Order item not found with ID: " + itemDto.getId()));
                    orderItem = existingItem;
                }
                // Update or add new order item
                orderItem.setImageUrl(itemDto.getImageUrl());
                orderItem.setPrice(itemDto.getPrice());
                orderItem.setQuantity(itemDto.getQuantity());
                updatedOrderItems.add(orderItem);
            }
        }
        existingOrder.setOrderItem(updatedOrderItems);

        // Save updated order
        existingOrder = orderRepository.save(existingOrder);

        // Update DTO with persisted values
        orderDto.setId(existingOrder.getId());
        orderDto.setDateCreated(existingOrder.getDateCreated());
        orderDto.setLastUpdated(existingOrder.getLastUpdated());

        if (orderDto.getOrderItem() != null) {
            int index = 0;
            for (OrderItem orderItem : existingOrder.getOrderItem()) {
                OrderDto.OrderItemDto itemDto = orderDto.getOrderItem().get(index);
                itemDto.setId(orderItem.getId());
                index++;
            }
        }

        return orderDto;
    }

    public void deleteOrder(Long orderId) {
        // Check if the order exists
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        // Delete the order; cascading will handle associated OrderItems
        orderRepository.delete(optionalOrder.get());
    }
}
