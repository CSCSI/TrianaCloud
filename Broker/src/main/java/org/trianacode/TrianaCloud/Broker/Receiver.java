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

package org.trianacode.TrianaCloud.Broker;

import com.rabbitmq.client.*;
import org.apache.log4j.Logger;
import org.trianacode.TrianaCloud.Utils.MD5;
import org.trianacode.TrianaCloud.Utils.Task;
import org.trianacode.TrianaCloud.Utils.TaskDAO;
import org.trianacode.TrianaCloud.Utils.TaskOps;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kieran David Evans
 * @version 1.0.0 Feb 26, 2012
 *          The Receiver is a background thread responsible for connecting results back to the task.
 */
@SuppressWarnings("FieldCanBeLocal")
public class Receiver implements Runnable {
    private Logger logger = Logger.getLogger(this.getClass().toString());

    ///TODO: is taskmap necessary?
    private String receiveQueueName;
    private ConcurrentHashMap<String, Task> taskMap;
    private ConcurrentHashMap<String, Task> resultsMap;
    private Connection connection;
    private Channel channel;
    private QueueingConsumer consumer;
    private boolean keepRunning = true;

    private String host;
    private int port;
    private String user;
    private String password;
    private String vHost;

    private TaskDAO td;

    public Receiver(String host, int port, String user, String pass, String vHost) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = pass;
        this.vHost = vHost;

        td = new TaskDAO();
    }

    /*
     * Initialise RabbitMQ, set up an excluseive queue, and pass the queuename back so it can be passed on down to the Worker.
     */
    public String init() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(user);
        factory.setPassword(password);
        factory.setVirtualHost(vHost);
        factory.setConnectionTimeout(60);

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            consumer = new QueueingConsumer(channel);
            receiveQueueName = channel.queueDeclare("", false, false, true, null).getQueue();
            channel.basicConsume(receiveQueueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }


        return receiveQueueName;
    }

    public void stopRunning() {
        keepRunning = false;
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void run() {
        while (keepRunning) {
            try {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                String corrid = delivery.getProperties().getCorrelationId();

                //Task t = taskMap.get(corrid);

                Task t = td.getByUUID(corrid);

                Task r = TaskOps.decodeTask(delivery.getBody());
                if (t == null) {
                    continue;
                }

                t.setReturnData(r.getReturnData());
                t.setReturnDataType(r.getReturnDataType());
                t.setReturnCode(r.getReturnCode());
                t.setReturnDataMD5(r.getReturnDataMD5());

                t.setState(Task.COMPLETE);

                td.save(t);

                System.out.println("Job: " + t.getName() + " took " + t.getTotalTime().getTime());
                if (r.getReturnDataType().equalsIgnoreCase("string")) {
                    System.out.println("Return Data MD5");
                    System.out.println("Expected: " + r.getReturnDataMD5());
                    System.out.println("Got     : " + MD5.getMD5Hash(r.getReturnData()));
                    System.out.println("Return Data:");
                    String d = new String(r.getReturnData(), "UTF-8");
                    System.out.println(d);
                } else {
                    System.out.println("Return Data MD5");
                    System.out.println("Expected: " + r.getReturnDataMD5());
                    System.out.println("Got     : " + MD5.getMD5Hash(r.getReturnData()));
                }

            } catch (ShutdownSignalException e) {
                System.out.println("Shutting down receiver");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
