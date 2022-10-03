package com.example.customer.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService{
    @Value("https://partners-dev.equitygroupholdings.com/v1/email/notification/v2")
    private String emailServerUrl;

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("b20bffb4ddcb46d393232d9aa72258d8")
    private String apimSubscriptionKey;

    private final HttpClient httpClient;

    @Autowired
    public EmailServiceImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    @Override
    public HttpResponse<String> sendEmail(String to) throws IOException, InterruptedException {
        ObjectNode emailBodyNode = OBJECT_MAPPER.createObjectNode();
        ObjectNode toNode = OBJECT_MAPPER.createObjectNode();
        ArrayNode toArrayNode = OBJECT_MAPPER.valueToTree(to);
        toNode.set("TO", toArrayNode);

        emailBodyNode.put("from_address", "customercare@equitybank.co.ke");
        emailBodyNode.put("subject", "Customer creation");
        emailBodyNode.put("message", "Body");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(emailServerUrl))
                .timeout(Duration.ofSeconds(5))
                .header("Ocp-Apim-Subscription-Key",apimSubscriptionKey)
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(emailBodyNode.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2){
            log.info("Failed sending an email notification.");
        } else {
            log.info("Completed sending notification by email");
        }

        return response;
    }
}
