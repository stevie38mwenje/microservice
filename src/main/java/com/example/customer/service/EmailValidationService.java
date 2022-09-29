package com.example.customer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Slf4j
@Service
public class EmailValidationService {
    public boolean validateEmail(String emailAddress){
        log.info("Validating email: {}", emailAddress);
        var EMAIL_REGEX_PATTERN = "^(.+)@(\\S+)$";
        return Pattern.compile(EMAIL_REGEX_PATTERN)
                .matcher(emailAddress)
                .matches();
    }
}
