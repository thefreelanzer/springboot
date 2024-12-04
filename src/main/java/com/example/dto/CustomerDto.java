package com.example.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private int id;
    private String name;
    private String email;
    private int age;

    public CustomerDto(int id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
