package com.example.customer.controller;


import com.example.customer.dto.CustomerRequest;
import com.example.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("api/v1/customer")
public record CustomerController(CustomerService customerService) {
    @PostMapping
    public void addCustomer(@RequestBody CustomerRequest customerRequest) throws IOException, InterruptedException {
        log.info("CREATED CUSTOMER: {}", customerRequest);
        customerService.register(customerRequest);
    }
}
