package com.example.customer.service;

import com.example.customer.FraudCheckResponse;
import com.example.customer.configs.CustomerConfig;
import com.example.customer.dto.CustomerRequest;
import com.example.customer.entity.Customer;
import com.example.customer.exception.InvalidCustomerDataException;
import com.example.customer.exception.InvalidEmailException;
import com.example.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class CustomerService {
    private final RestTemplate restTemplate;
    private final EmailValidationService emailValidationService;

    private final CustomerRepository customerRepository;
    public void register(CustomerRequest request) {
        if(Strings.isNullOrEmpty(request.email())){
            throw new InvalidCustomerDataException("Empty or null email");
        }
        if(!emailValidationService.validateEmail(request.email())){
             throw new InvalidEmailException("invalid email");
        }

        var customerObj = customerRepository.getByEmail(request.email());
        if(customerObj!=null){
            var email = customerObj.getEmail();
            if(request.email().equalsIgnoreCase(email)){
                throw new InvalidEmailException("email already existig");
            }
        }

        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        customerRepository.saveAndFlush(customer);


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
