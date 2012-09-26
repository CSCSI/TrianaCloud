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

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;
import org.apache.log4j.Logger;
import org.trianacode.TrianaCloud.Utils.RPCClient;
import org.trianacode.TrianaCloud.Utils.Task;
import org.trianacode.TrianaCloud.Utils.TaskDAO;
import org.trianacode.TrianaCloud.Utils.TaskOps;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;

public class RPCServer implements Runnable {
    private Logger logger = Logger.getLogger(this.getClass().toString());

    private String rpc_queue_name;
    private String host;
    private int port;
    private String user;
    private String password;
    private String vHost;

    private Connection connection;
    private Channel channel;
    private ConnectionFactory factory;
    private QueueingConsumer consumer;

    private boolean keepRunning;

    private TaskDAO td;

    /*
     * Sets the RabbitMQ connection details
     *
     * @param host The hostname of the RabbitMQ Server
     * @param port The port to connect to the RabbitMQ Server
     * @param user The user to connect to RabbitMQ with
     * @param pass The password to use to connect to RabbitMQ with
     * @param vHost The virtual host on RabbitMQ to connect to
     * @param rpc_queue_name The name of the RPC Queue
     */
    public RPCServer(String host, int port, String user, String pass, String vHost, String rpc_queue_name) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = pass;
        this.vHost = vHost;
        this.rpc_queue_name = rpc_queue_name;

    }

    public void init() throws ServletException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(user);
        factory.setPassword(password);
        factory.setVirtualHost(vHost);
        factory.setConnectionTimeout(60);
        try {
            keepRunning = true;
            td = new TaskDAO();

            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(rpc_queue_name, false, false, false, null);

            channel.basicQos(1);

            consumer = new QueueingConsumer(channel);
            channel.basicConsume(rpc_queue_name, false, consumer);
            logger.info("[x] Awaiting RPC requests");
        } catch (Exception e) {
            ServletException se = new ServletException(e);
            logger.fatal("Something Happened!", se);
            throw se;
        }
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

                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties
                        .Builder()
                        .correlationId(props.getCorrelationId())
                        .build();

                byte[] del = delivery.getBody();


                byte[] method = Arrays.copyOfRange(del, 0, 7);
                byte[] message = Arrays.copyOfRange(del, 7, del.length);

                if (Arrays.equals(method, RPCClient.GET_TASK.getBytes())) {
                    String[] plugins = (new String(message)).split("\n");

                    ///TODO: grab tasks by plugin

                    logger.info(" [.] Task Requested");
                    Task t = td.getNextPending();
                    if (t == null) {
                        t = new Task();
                        t.setNOTASK(true);
                        t.setTimeToWait(10);
                    }
                    channel.basicPublish("", props.getReplyTo(), replyProps, TaskOps.encodeTask(t));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } else if (Arrays.equals(method, RPCClient.RETURN_TASK.getBytes())) {
                    Task rettask = TaskOps.decodeTask(message);

                    if (rettask.getNOTASK()) {
                        byte[] ret = new byte[0];
                        channel.basicPublish("", props.getReplyTo(), replyProps, ret);
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    } else {
                        Task task = td.getByUUID(rettask.getUUID());

                        task.setReturnCode(rettask.getReturnCode());
                        task.setReturnData(rettask.getReturnData());
                        task.setReturnDataMD5(rettask.getReturnDataMD5());
                        task.setReturnDataType(rettask.getReturnDataType());
                        task.setState(Task.COMPLETE);

                        td.save(task);

                        logger.info(" [x] Task " + task.getUUID() + " Complete");

                        byte[] ret = new byte[1];
                        ret[0] = 0;
                        channel.basicPublish("", props.getReplyTo(), replyProps, ret);
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }

                }//else if some other rpc call/method/thing
            } catch (ShutdownSignalException e) {
                System.out.println("Shutting down the RPC Server");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error", e);
            }
        }

    }
}
