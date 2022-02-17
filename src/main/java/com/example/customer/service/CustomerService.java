package com.example.customer.service;

import com.example.customer.dto.CustomerRequest;
import com.example.customer.entity.Customer;
import com.example.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public record CustomerService(CustomerRepository customerRepository) {
    public void register(CustomerRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        customerRepository.save(customer);
        //TODO: CHECK IF EMAIL IS VALID
        //TODO: CHECK IF EMAIL IS NOT TAKEN
        //TODO: STORE IN DB
    }
}
