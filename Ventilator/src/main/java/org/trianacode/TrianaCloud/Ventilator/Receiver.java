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

package org.trianacode.TrianaCloud.Ventilator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.Map;

/*
 * The Receiver is a background thread responsible for connecting results back to the task.
 */
public class Receiver implements Runnable {
    private String receiveQueueName;
    private Map<String, Task> taskMap;
    private Connection connection;
    private Channel channel;
    private QueueingConsumer consumer;
    private boolean keepRunning = true;

    public Receiver(Map t) {
        taskMap = t;
    }

    /*
     * Initialise RabbitMQ, set up an excluseive queue, and pass the queuename back so it can be passed on down to the Worker.
     */
    public String init() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.201");
        factory.setUsername("guest");
        factory.setPassword("guest");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            consumer = new QueueingConsumer(channel);
            receiveQueueName = channel.queueDeclare().getQueue();
            channel.basicConsume(receiveQueueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
        return receiveQueueName;
    }

    public void stopRunning() {
        keepRunning = false;
    }


    public void run() {
        while (keepRunning) {
            try {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                String corrid = delivery.getProperties().getCorrelationId();

                Task t = taskMap.get(corrid);

                if (t == null) {
                    continue;
                }

                String d = new String(t.getData());
                
                System.out.println("Job: " + d + " took " + t.totalTime().getTime());

                ///TODO: make Task runnable. We can then stick results into the task, and let it do the approp thing.

                taskMap.remove(corrid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
