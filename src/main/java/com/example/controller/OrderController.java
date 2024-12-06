package com.example.controller;

import com.example.dto.CustomerDto;
import com.example.dto.OrderDto;
import com.example.service.CustomerService;
import com.example.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // List all orders
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/add")
    public ResponseEntity<OrderDto> addOrder(@Valid @RequestBody OrderDto orderDto) {
        orderDto = orderService.createOrder(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        OrderDto updatedOrder = orderService.updateOrder(id, orderDto);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order with ID " + id + " has been deleted.");
    }
}
