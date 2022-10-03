package com.example.customer.email;

import java.io.IOException;
import java.net.http.HttpResponse;

public interface EmailService {
    HttpResponse<String> sendEmail(String to) throws IOException, InterruptedException;

}
