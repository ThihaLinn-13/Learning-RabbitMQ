
package com.learning.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Consumer {

    private static final String QUEUE_NAME = "helloWorld";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(
            QUEUE_NAME,
            true,
            false,
            true,
            null
        );

        System.out.println(" [*] Waiting for messages. Press CTRL+C to exit.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());

            System.out.println(" [x] Received: " + message);

            long deliveryTag = delivery.getEnvelope().getDeliveryTag();

            channel.basicNack(deliveryTag, false, true);

            System.out.println(" [x] Message NACK: " + deliveryTag);

        };

        channel.basicConsume(
            QUEUE_NAME,
            false,
            deliverCallback,
            consumerTag -> {}
        );
    }
}
