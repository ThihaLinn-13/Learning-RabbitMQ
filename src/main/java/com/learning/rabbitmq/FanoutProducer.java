package com.learning.rabbitmq;

import com.rabbitmq.client.*;

public class FanoutProducer {

    private static final String EXCHANGE_NAME = "insurance.fanout";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String message = "Your insurance policy notification!";

        channel.basicPublish(
            EXCHANGE_NAME,
            "email",
            null,
            message.getBytes()
        );

        System.out.println(" [x] Sent: " + message);

        channel.close();
        connection.close();
    }
}