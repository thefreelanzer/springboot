package com.example.dto;

import com.example.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;

    private String orderTrackingNumber;

    private int totalQuantity;

    private BigDecimal totalPrice;

    private String status;

    private LocalDateTime dateCreated;

    private LocalDateTime lastUpdated;

    private List<OrderItemDto> orderItem;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemDto {
        private Long id;

        private String imageUrl;

        private BigDecimal price;

        private int quantity;

        private Order order;
    }
}
