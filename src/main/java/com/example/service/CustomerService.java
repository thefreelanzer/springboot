package com.example.service;

import com.example.dto.CustomerDto;
import com.example.entity.Address;
import com.example.entity.Customer;
import com.example.exception.CustomerException;
import com.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * method for creating customer
     *
     * @param customerDto customerDto
     * @return customerDto
     */
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setAge(customerDto.getAge());
        customer.setDesignation(customerDto.getDesignation());

        if (customerDto.getAddress() != null) {
            Address address = new Address();
            address.setAddressline1(customerDto.getAddress().getAddressline1());
            address.setAddressline2(customerDto.getAddress().getAddressline2());
            address.setCity(customerDto.getAddress().getCity());
            address.setState(customerDto.getAddress().getState());
            address.setCountry(customerDto.getAddress().getCountry());
            customer.setAddress(address);
        }

        customer = customerRepository.save(customer);
        customerDto.setId(customer.getId());
        return customerDto;
    }

    /**
     * method for updating customer
     *
     * @param id          id
     * @param customerDto customerDto
     * @return customerDto or Exception if customer not exist
     */
    public CustomerDto updateCustomer(int id, CustomerDto customerDto) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setName(customerDto.getName());
            customer.setEmail(customerDto.getEmail());
            customer.setAge(customerDto.getAge());
            customer = customerRepository.save(customer);
            customerDto.setId(customer.getId());
            return customerDto;
        } else {
            throw new CustomerException("Customer with ID " + id + " not found.");
        }
    }

    /**
     * method for deleting customers
     *
     * @param id id
     */
    public void deleteCustomer(int id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        } else {
            throw new CustomerException("Customer with ID " + id + " not found.");
        }
    }

    /**
     * list all customers
     *
     * @return List
     */
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        return customers.stream().map(customer -> {
            CustomerDto dto = new CustomerDto();
            dto.setId(customer.getId());
            dto.setName(customer.getName());
            dto.setAge(customer.getAge());
            dto.setAge(customer.getAge());
            return dto;
        }).collect(Collectors.toList());
    }
}
