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

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


/*
 * The Ventilator receives tasks, and dispatches them to the queue.
 * Tasks are kept track of in taskMap, so that when the Receiver thread
 * gets results, it knows what to do with them.
 */
public class Ventilator {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "task_dispatch";
    private String replyQueueName;
    private HashMap<String, Task> taskMap;
    private Receiver receiver;
    private Thread recThread;

    /*
     * Initialises RabbitMQ and fires up the receiver thread.
     */
    public Ventilator() throws Exception {
        taskMap = new HashMap<String, Task>();

        {   //RabbitMQ init
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.1.201");
            factory.setUsername("guest");
            factory.setPassword("guest");

            connection = factory.newConnection();
            channel = connection.createChannel();
        }

        //Spin up the receive thread.
        receiver = new Receiver(taskMap);
        replyQueueName = receiver.init();
        recThread = new Thread(receiver);
        recThread.start();
    }

    /*
     * Dispatches the task to the Task Queue
     */
    public void dispatchTask(Task t) throws IOException{
        ///TODO: Read task metadata to determine which queue to send to (e.g. #.triana)
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        taskMap.put(corrId, t);
        channel.basicPublish("", requestQueueName, props, t.getData());
        System.out.println("Sent job " + corrId + " with payload " + t.getData().toString());
    }

    ///TODO: This should be a webservice, multipart post, metadata+task http://www.discursive.com/books/cjcook/reference/http-webdav-sect-upload-multipart-post
    public void makeTask(String message) throws Exception {
        Task t = new Task("call", message.getBytes());
        dispatchTask(t);
    }

    public void close() throws Exception {
        connection.close();
        recThread.join();
    }

    public static void main(String[] argv) {
        Ventilator v = null;

        try {
            v = new Ventilator();
            for (int i = 0; i < 250; i++) {
                v.makeTask(String.valueOf(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (v != null) {
                try {
                    v.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}