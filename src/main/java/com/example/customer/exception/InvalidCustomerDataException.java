package com.example.customer.exception;

public class InvalidCustomerDataException extends RuntimeException{

    public InvalidCustomerDataException() {
        super();
    }
    public InvalidCustomerDataException(String message) {
        super(message);
    }
}
