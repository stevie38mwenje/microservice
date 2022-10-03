package com.example.customer.service;

import com.example.customer.FraudCheckResponse;
import com.example.customer.configs.CustomerConfig;
import com.example.customer.dto.CustomerRequest;
import com.example.customer.email.EmailService;
import com.example.customer.entity.Customer;
import com.example.customer.exception.InvalidCustomerDataException;
import com.example.customer.exception.InvalidEmailException;
import com.example.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.http.HttpResponse;

@AllArgsConstructor
@Slf4j
@Service
public class CustomerService {
    private final RestTemplate restTemplate;
    private final EmailValidationService emailValidationService;

    private final EmailService emailService;

    private final CustomerRepository customerRepository;
    public void register(CustomerRequest request) throws IOException, InterruptedException {
        if(Strings.isNullOrEmpty(request.email())){
            throw new InvalidCustomerDataException("Empty or null email");
        }
        if(!emailValidationService.validateEmail(request.email())){
             throw new InvalidEmailException("invalid email");
        }

        var customerObj = customerRepository.getByEmail(request.email());
        log.info("cutomer info : {}", customerObj);
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




        //return email response


        customerRepository.save(customer);


        //TODO: CHECK IF FRAUDSTER
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://localhost:8081/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );
        assert fraudCheckResponse != null;
        if(fraudCheckResponse.isFraudulentCustomer()){
            throw new IllegalStateException("fraudster");
        }

       // Send Email notification
        HttpResponse<String> emailResponse = emailService.sendEmail(request.email());
        log.info("Email response: {}", emailResponse);

    }
}
