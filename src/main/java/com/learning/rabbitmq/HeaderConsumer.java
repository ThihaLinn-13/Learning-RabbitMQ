package com.learning.rabbitmq;

import com.rabbitmq.client.*;

public class HeaderConsumer {

    private static final String EXCHANGE_NAME = "insurance.headers";
    private static final String POLICY_QUEUE = "policy.queue";
    private static final String AUDIT_QUEUE = "audit.queue";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(POLICY_QUEUE, true, false, false, null);
        channel.queueDeclare(AUDIT_QUEUE, true, false, false, null);

        DeliverCallback callback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());

            System.out.println(
                "[x] Received from " + delivery.getEnvelope().getRoutingKey()
                + ": " + message
            );

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(POLICY_QUEUE, false, callback, consumerTag -> {});
        channel.basicConsume(AUDIT_QUEUE, false, callback, consumerTag -> {});

        System.out.println("[*] Waiting for messages. Press CTRL+C to exit.");
    }
}