package com.example.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_tracking_number")
    private String orderTrackingNumber;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    // one to many unidirectional mapping
    // default fetch type for OneToMany: LAZY
    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@JoinColumn(name = "order_id", referencedColumnName = "id")
    //private Set<OrderItem> orderItem = new HashSet<>();

    // OneToMany with mappedBy to establish bidirectional relationship
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItemSet = new HashSet<>();

    // Helper method to add an item to the order
    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);  // Set the Order reference in the OrderItem
        this.orderItemSet.add(orderItem);  // Add to the collection
    }

    public void add(OrderItem orderItem) {
        orderItemSet.add(orderItem);
        orderItem.setOrder(this); // Set the order on the orderItem
    }
}
