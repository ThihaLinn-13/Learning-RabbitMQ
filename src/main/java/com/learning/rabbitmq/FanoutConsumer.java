
package com.learning.rabbitmq;

import com.rabbitmq.client.*;

public class FanoutConsumer {

    private static final String EXCHANGE_NAME = "insurance.fanout";

    private static final String QUEUE_1 = "email.fanout.queue";
    private static final String QUEUE_2 = "sms.fanout.queue";
    private static final String QUEUE_3 = "audit.fanout.queue";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Declare Fanout Exchange
        channel.exchangeDeclare(
            EXCHANGE_NAME,
            BuiltinExchangeType.FANOUT
        );

        // Declare Queues
        channel.queueDeclare(QUEUE_1, true, false, false, null);
        channel.queueDeclare(QUEUE_2, true, false, false, null);
        channel.queueDeclare(QUEUE_3, true, false, false, null);

        // Bind Queues to Fanout Exchange
        channel.queueBind(QUEUE_1, EXCHANGE_NAME, "");
        channel.queueBind(QUEUE_2, EXCHANGE_NAME, "");
        channel.queueBind(QUEUE_3, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages...");

        DeliverCallback deliverCallbackForQueOne = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody());

            System.out.println(" [x] Received: " + message);
            System.out.println(" [x] Queue: " + QUEUE_1);
        };

            DeliverCallback deliverCallbackForQueTwo = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody());

            System.out.println(" [x] Received: " + message);
            System.out.println(" [x] Queue: " + QUEUE_2);
        };

            DeliverCallback deliverCallbackForThree = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody());

            System.out.println(" [x] Received: " + message);
            System.out.println(" [x] Queue: " + QUEUE_3);
        };

        channel.basicConsume(QUEUE_1, true, deliverCallbackForQueOne, consumerTag -> {});
        channel.basicConsume(QUEUE_2, true, deliverCallbackForQueTwo, consumerTag -> {});
        channel.basicConsume(QUEUE_3, true, deliverCallbackForThree, consumerTag -> {});
    }
}
