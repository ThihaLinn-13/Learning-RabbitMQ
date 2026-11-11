package com.learning.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class TopicConsumer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Declare Exchange
        channel.exchangeDeclare(
                "insurance.topic",
                BuiltinExchangeType.TOPIC);

        // Declare Queues
        channel.queueDeclare("policy.queue", true, false, false, null);
        channel.queueDeclare("audit.queue", true, false, false, null);
        channel.queueDeclare("payment.queue", true, false, false, null);

        // Bind Queues
        channel.queueBind("policy.queue", "insurance.topic", "insurance.policy.*");
        channel.queueBind("audit.queue", "insurance.topic", "insurance.#");
        channel.queueBind("payment.queue", "insurance.topic", "*.payment.*");

        // Policy Callback
        DeliverCallback policyCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [Policy] Received: " + message);
        };

        // Audit Callback
        DeliverCallback auditCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [Audit] Received: " + message);
        };

        // Payment Callback
        DeliverCallback paymentCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [Payment] Received: " + message);
        };

        // Register Consumers
        channel.basicConsume("policy.queue", true, policyCallback, consumerTag -> {
        });
        channel.basicConsume("policy.queue", true, policyCallback, consumerTag -> {
        });
        channel.basicConsume("audit.queue", true, auditCallback, consumerTag -> {
        });
        channel.basicConsume("payment.queue", true, paymentCallback, consumerTag -> {
        });

        System.out.println(" [*] Waiting for messages...");
    }
}