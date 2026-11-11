
package com.learning.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Producer {

    private static final String QUEUE_NAME = "helloWorld";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()
        ) {

            channel.queueDeclare(
                QUEUE_NAME,
                true,
                false,
                true,
                null
            );

            String message = "Hello RabbitMQ!";

            channel.basicPublish(
                "",
                QUEUE_NAME,
                null,
                message.getBytes()
            );

            System.out.println(" [x] Sent: " + message);
        }
    }
}
