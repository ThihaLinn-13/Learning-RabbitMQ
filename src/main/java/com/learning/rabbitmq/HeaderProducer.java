package com.learning.rabbitmq;

import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HeaderProducer {

    private static final String EXCHANGE_NAME = "insurance.headers";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            Map<String, Object> headers = new HashMap<>();

            headers.put("type", "policy");
            headers.put("priority", "low");
            headers.put("region", "yangon");

            AMQP.BasicProperties properties =
                new AMQP.BasicProperties.Builder()
                    .headers(headers)
                    .build();

            String message = "New insurance policy created";

            channel.basicPublish(
                EXCHANGE_NAME,
                "",
                properties,
                message.getBytes(StandardCharsets.UTF_8)
            );

            System.out.println("[x] Sent: " + message);
        }
    }
}