/*
 * Copyright (c) 2012, SHIWA
 *
 *     This file is part of TrianaCloud.
 *
 *     TrianaCloud is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     TrianaCloud is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with TrianaCloud.  If not, see <http://www.gnu.org/licenses/>.
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

    public RPCClient() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("zotac.toaster.dbyz.co.uk");
            factory.setVirtualHost("trianacloud");
            factory.setUsername("trianacloud");
            factory.setPassword("trianacloud");
            factory.setRequestedHeartbeat(60);
            connection = factory.newConnection();
            channel = connection.createChannel();

            replyQueueName = channel.queueDeclare().getQueue();
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(replyQueueName, true, consumer);
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

    public int sendCompleteTask(byte[] message) {
        try {
            byte[] method = RETURN_TASK.getBytes();

            byte[] ret = call(ArrayUtils.addAll(method, message));

            return new Integer(ret[0]);
        } catch (Exception e) {
            logger.error("Error sending complete task");
            return -1;
        }
    }


    public void close() throws Exception {
        connection.close();
    }
}
