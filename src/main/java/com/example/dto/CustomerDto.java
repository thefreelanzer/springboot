package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private int id;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotEmpty(message = "The email is required.")
    @Email(message = "The email is not a valid email.")
    private String email;

    private int age;

    private String designation;

    private AddressDto address;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddressDto {
        @NotBlank(message = "Address line 1 is required.")
        private String addressline1;

        private String addressline2;

        @NotBlank(message = "City is required.")
        private String city;

        @NotBlank(message = "State is required.")
        private String state;

        @NotBlank(message = "Country is required.")
        private String country;
    }
}
