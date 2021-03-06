/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *        or more contributor license agreements.  See the NOTICE file
 *        distributed with this work for additional information
 *        regarding copyright ownership.  The ASF licenses this file
 *        to you under the Apache License, Version 2.0 (the
 *        "License"); you may not use this file except in compliance
 *        with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing,
 *        software distributed under the License is distributed on an
 *        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *        KIND, either express or implied.  See the License for the
 *        specific language governing permissions and limitations
 *        under the License.
 */

package org.trianacode.TrianaCloud.Utils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.List;


public class RPCClient {
    private Logger logger = Logger.getLogger(this.getClass().toString());

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "trianaloud_rpc_queue";
    private String replyQueueName;
    private QueueingConsumer consumer;

    public static final String GET_TASK = "gettask";
    public static final String RETURN_TASK = "rettask";

    public static final String NOTASK = "notask";
    public boolean didFailToConnect = true;

    public RPCClient() {
        try {
            //TODO: Config file!
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("i86.cscloud.cf.ac.uk");
            factory.setVirtualHost("trianacloud");
            factory.setUsername("trianacloud");
            factory.setPassword("trianacloud");
            //factory.setPort(7002);
            factory.setRequestedHeartbeat(60);
            connection = factory.newConnection();
            channel = connection.createChannel();

            replyQueueName = channel.queueDeclare().getQueue();
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(replyQueueName, true, consumer);

            didFailToConnect = false;
        } catch (Exception e) {
            logger.fatal("Error connecting to Rabbit while initialising RPCClient", e);
        }
    }

    private byte[] call(String message) throws Exception {
        return call(message.getBytes());
    }

    private byte[] call(byte[] message) throws Exception {
        byte[] response = null;
        String corrId = java.util.UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = delivery.getBody();
                break;
            }
        }

        return response;
    }

    public Task getTask(List<String> plugins) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(GET_TASK + "\n");

            for (String p : plugins) {
                sb.append(p + "\n");
            }

            Task t = TaskOps.decodeTask(call(sb.toString()));
            return t;
        } catch (Exception e) {
            logger.error("Error getting new task", e);
            return null;
        }
    }

    public boolean sendCompleteTask(byte[] message) {
        try {
            byte[] method = RETURN_TASK.getBytes();

            byte[] ret = call(ArrayUtils.addAll(method, message));

            return true;
        } catch (Exception e) {
            logger.error("Error sending complete task",e);
            return false;
        }
    }


    public void close() throws Exception {
        connection.close();
    }
}
