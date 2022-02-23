package com.example.customer.service;

import com.example.customer.FraudCheckResponse;
import com.example.customer.configs.CustomerConfig;
import com.example.customer.dto.CustomerRequest;
import com.example.customer.entity.Customer;
import com.example.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class CustomerService {
    private final RestTemplate restTemplate;

    private final CustomerRepository customerRepository;
    public void register(CustomerRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        customerRepository.saveAndFlush(customer);
        //TODO: CHECK IF EMAIL IS VALID
        //TODO: CHECK IF EMAIL IS NOT TAKEN

        //TODO: CHECK IF FRAUDSTER
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://localhost:8081/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );
        if(fraudCheckResponse.isFraudulentCustomer()){
            throw new IllegalStateException("fraudster");
        }
        //TODO: SEND NOTIFICATION


    }
}
