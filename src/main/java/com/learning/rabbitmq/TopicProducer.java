package com.learning.rabbitmq;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TopicProducer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        System.out.println(" [*] Connected to RabbitMQ");

        String routingKey = "insurance.payment.success";
        String message = "Insurance payment successful";
        
        channel.basicPublish(
                "insurance.topic",
                routingKey,
                null,
                message.getBytes(StandardCharsets.UTF_8));

        System.out.println(" [x] Sent: " + routingKey + " -> " + message);
    }
}