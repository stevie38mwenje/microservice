package com.example.customer.kafka;

import com.example.customer.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
@Slf4j
public class QueueImpl {
    private KafkaTemplate<String, String> kafkaTemplate;

    public QueueImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(String payload, String topic){
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(topic, String.valueOf(payload));

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + payload +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                        + payload + "] due to : " + ex.getMessage());
            }
        });

    }

}
