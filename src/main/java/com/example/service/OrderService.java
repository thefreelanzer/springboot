package com.example.service;

import com.example.dto.OrderDto;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders == null || orders.isEmpty()) {
            System.out.println("No orders found!");
            return Collections.emptyList();
        }
        System.out.println("Orders retrieved: " + orders.size());

        return orders.stream().map(order -> {
            System.out.println("Mapping Order ID: " + order.getId());
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setOrderTrackingNumber(order.getOrderTrackingNumber());
            orderDto.setTotalQuantity(order.getTotalQuantity());
            orderDto.setTotalPrice(order.getTotalPrice());
            orderDto.setStatus(order.getStatus());
            orderDto.setDateCreated(order.getDateCreated());
            orderDto.setLastUpdated(order.getLastUpdated());

            if (order.getOrderItemSet() == null || order.getOrderItemSet().isEmpty()) {
                System.out.println("No order items found for Order ID: " + order.getId());
                orderDto.setOrderItem(Collections.emptyList());
            } else {
                System.out.println("Order Items count for Order ID: " + order.getId() + " is " + order.getOrderItemSet().size());
                List<OrderDto.OrderItemDto> itemDtos = order.getOrderItemSet().stream().map(orderItem -> {
                    OrderDto.OrderItemDto itemDto = new OrderDto.OrderItemDto();
                    itemDto.setId(orderItem.getId());
                    itemDto.setImageUrl(orderItem.getImageUrl());
                    itemDto.setPrice(orderItem.getPrice());
                    itemDto.setQuantity(orderItem.getQuantity());
                    return itemDto;
                }).toList();
                orderDto.setOrderItem(itemDtos);
            }
            return orderDto;
        }).toList();
    }

    public OrderDto createOrder(OrderDto orderDto) {
        // Create a new Order entity
        Order order = new Order();
        LocalDateTime now = LocalDateTime.now();

        // Reflect created and updated times in DTO
        orderDto.setDateCreated(now);
        orderDto.setLastUpdated(now);

        // Initialize the orderItems set
        Set<OrderItem> orderItems = new HashSet<>();

        System.out.println("OrderDTO Items Size: " + (orderDto.getOrderItem() != null ? orderDto.getOrderItem().size() : "null"));
        if (orderDto.getOrderItem() != null) {
            for (OrderDto.OrderItemDto itemDto : orderDto.getOrderItem()) {
                System.out.println("Processing OrderItemDto: " + itemDto);
                OrderItem orderItem = new OrderItem();
                orderItem.setImageUrl(itemDto.getImageUrl());
                orderItem.setPrice(itemDto.getPrice());
                orderItem.setQuantity(itemDto.getQuantity());

                order.addOrderItem(orderItem);
            }
        }

        order.setOrderTrackingNumber(orderDto.getOrderTrackingNumber());
        order.setTotalQuantity(orderDto.getTotalQuantity());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setStatus(orderDto.getStatus());
        order.setDateCreated(now);
        order.setLastUpdated(now);

        System.out.println("Order Items Setting!!!!" + order);

        for (OrderItem item : orderItems) {
            System.out.println("OrderItem Details: " + item);
        }

        // Save the order
        System.out.println("Saving Order: " + order);
        order = orderRepository.save(order);
        System.out.println("Order Saved: " + order);

        // Update the DTO with the saved order ID
        orderDto.setId(order.getId());

        // Map back the saved OrderItem IDs to DTO
        if (orderDto.getOrderItem() != null) {
            int index = 0;
            for (OrderItem savedOrderItem : order.getOrderItemSet()) {
                OrderDto.OrderItemDto itemDto = orderDto.getOrderItem().get(index);
                itemDto.setId(savedOrderItem.getId());
                System.out.println("Set ID for OrderItemDto: " + savedOrderItem.getId());
                index++;
            }
        }

        return orderDto;
    }

    /*public OrderDto updateOrder(Long orderId, OrderDto orderDto) {
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
                    OrderItem existingItem = existingOrder.getOrderItems().stream()
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
        existingOrder.setOrderItems(updatedOrderItems);

        // Save updated order
        existingOrder = orderRepository.save(existingOrder);

        // Update DTO with persisted values
        orderDto.setId(existingOrder.getId());
        orderDto.setDateCreated(existingOrder.getDateCreated());
        orderDto.setLastUpdated(existingOrder.getLastUpdated());

        if (orderDto.getOrderItem() != null) {
            int index = 0;
            for (OrderItem orderItem : existingOrder.getOrderItems()) {
                OrderDto.OrderItemDto itemDto = orderDto.getOrderItem().get(index);
                itemDto.setId(orderItem.getId());
                index++;
            }
        }

        return orderDto;
    }*/

    @Transactional
    public OrderDto updateOrder(Long orderId, OrderDto orderDto) {
        // Fetch the existing order from the database
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));

        // Update basic fields of the order
        order.setOrderTrackingNumber(orderDto.getOrderTrackingNumber());
        order.setTotalQuantity(orderDto.getTotalQuantity());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setStatus(orderDto.getStatus());
        LocalDateTime now = LocalDateTime.now();
        order.setLastUpdated(now);

        // Reflect the updated time in the DTO
        orderDto.setLastUpdated(now);

        // Update the order items (if necessary)
        if (orderDto.getOrderItem() != null) {
            // Remove the existing items if necessary (e.g., clear them or update as needed)
            order.getOrderItemSet().clear();  // Optional: Remove existing items
            Set<OrderItem> orderItems = new HashSet<>();

            for (OrderDto.OrderItemDto itemDto : orderDto.getOrderItem()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setImageUrl(itemDto.getImageUrl());
                orderItem.setPrice(itemDto.getPrice());
                orderItem.setQuantity(itemDto.getQuantity());

                // Add to the set
                order.add(orderItem);
            }

        }

        // Save the updated order
        order = orderRepository.save(order);

        // Update the DTO with the saved order ID
        orderDto.setId(order.getId());

        // Map back the saved OrderItem IDs to DTO
        if (orderDto.getOrderItem() != null) {
            int index = 0;
            for (OrderItem savedOrderItem : order.getOrderItemSet()) {
                OrderDto.OrderItemDto itemDto = orderDto.getOrderItem().get(index);
                itemDto.setId(savedOrderItem.getId());
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
