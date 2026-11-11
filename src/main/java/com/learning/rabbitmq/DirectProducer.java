
package com.learning.rabbitmq;

import com.rabbitmq.client.*;

public class DirectProducer {

    private static final String EXCHANGE_NAME = "insurance.direct";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(
                    EXCHANGE_NAME,
                    BuiltinExchangeType.DIRECT);

            String routingKey = "sms";
            String message = "Your insurance policy SMS notification!";

            channel.basicPublish(
                    EXCHANGE_NAME,
                    routingKey,
                    null,
                    message.getBytes());

            System.out.println(" [x] Sent: " + message);
            System.out.println(" [x] Routing Key: " + routingKey);
        }
    }
}
