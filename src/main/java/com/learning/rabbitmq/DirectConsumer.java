
package com.learning.rabbitmq;

import com.rabbitmq.client.*;

public class DirectConsumer {

    private static final String EXCHANGE_NAME = "insurance.direct";
    private static final String QUEUE_NAME = "email.queue";
    private static final String ROUTING_KEY = "email";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(
                EXCHANGE_NAME,
                BuiltinExchangeType.DIRECT);

        channel.queueDeclare(
                QUEUE_NAME,
                true,
                false,
                false,
                null);

        channel.queueDeclare(
                "sms.queue",
                true,
                false,
                false,
                null);

        channel.queueBind(
                QUEUE_NAME,
                EXCHANGE_NAME,
                ROUTING_KEY);

        channel.queueBind(
                "sms.queue",
                EXCHANGE_NAME,
                "sms");

        System.out.println(" [*] Waiting for messages...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody());

            System.out.println(" [x] Received: " + message);
            System.out.println(" [x] Routing Key: "
                    + delivery.getEnvelope().getRoutingKey());
        };

        channel.basicConsume(
                QUEUE_NAME,
                true,
                deliverCallback,
                consumerTag -> {
                });
                
        channel.basicConsume(
                "sms.queue",
                true,
                deliverCallback,
                consumerTag -> {
                });
    }
}
