/*
 *
 *  * Copyright - TrianaCloud
 *  * Copyright (C) 2012. Kieran Evans. All Rights Reserved.
 *  *
 *  * This program is free software; you can redistribute it and/or
 *  * modify it under the terms of the GNU General Public License
 *  * as published by the Free Software Foundation; either version 2
 *  * of the License, or (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program; if not, write to the Free Software
 *  * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package org.trianacode.TrianaCloud.Worker;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.File;
import java.net.URL;

/*
 * The worker looks for tasks, and executes them.
 */

public class Worker {
    private static final String RPC_QUEUE_NAME = "task_dispatch";

    public static void main(String[] argv) {
        Connection connection = null;
        Channel channel;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.1.201");
            factory.setUsername("guest");
            factory.setPassword("guest");

            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

            channel.basicQos(1);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
            
            System.out.println(" [x] Loading Plugins");

            ClassLoader classLoader = Worker.class.getClassLoader();
            URL[] urls = new URL[1];
            String workingDir = System.getProperty("user.dir");
            File f = new File(workingDir);
            urls[0] = f.toURI().toURL();
            TaskExecutorLoader tel = new TaskExecutorLoader(urls,classLoader);
            
            System.out.println(" [x] Awaiting RPC requests");
            
            while (true) {
                String response = "";

                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties
                        .Builder()
                        .correlationId(props.getCorrelationId())
                        .build();

                try {
                    ///TODO: Load all TaskExecutors, use metadata to figure out which to use.
                    ///TODO: Create a serializable Task class. Metadata + Byte array. TaskExecutor can do what it wants with the byte array.

                    String message = new String(delivery.getBody());
                    TaskExecutor ex = tel.getExecutor("org.trianacode.TrianaCloud.TaskExecutionExample.TaskExecutionExample");
                    ex.setData(message.getBytes());

                    response = new String(ex.executeTask());
                } catch (Exception e) {
                    System.out.println(" [.] " + e.toString());
                    response = "";
                } finally {
                    channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}