package com.learning.rabbitmq;

import com.rabbitmq.client.*;
import java.util.HashMap;
import java.util.Map;

public class HeaderStepup {

    private static final String EXCHANGE_NAME = "insurance.headers";
    private static final String POLICY_QUEUE = "policy.queue";
    private static final String AUDIT_QUEUE = "audit.queue";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // 1. Declare Headers Exchange
            channel.exchangeDeclare(
                EXCHANGE_NAME,
                BuiltinExchangeType.HEADERS,
                true
            );

            // 2. Declare Queues
            channel.queueDeclare(POLICY_QUEUE, true, false, false, null);
            channel.queueDeclare(AUDIT_QUEUE, true, false, false, null);

            // 3. Policy Queue Binding
            Map<String, Object> policyHeaders = new HashMap<>();
            policyHeaders.put("x-match", "all");
            policyHeaders.put("type", "policy");
            policyHeaders.put("priority", "high");

            channel.queueBind(
                POLICY_QUEUE,
                EXCHANGE_NAME,
                "",
                policyHeaders
            );

            // 4. Audit Queue Binding
            Map<String, Object> auditHeaders = new HashMap<>();
            auditHeaders.put("x-match", "any");
            auditHeaders.put("region", "yangon");
            auditHeaders.put("priority", "high");

            channel.queueBind(
                AUDIT_QUEUE,
                EXCHANGE_NAME,
                "",
                auditHeaders
            );

            System.out.println("[*] Headers Exchange setup completed!");
        }
    }
}