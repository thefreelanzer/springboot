package com.example.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@NoArgsConstructor
@Data
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generating ID
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private int age;
}
