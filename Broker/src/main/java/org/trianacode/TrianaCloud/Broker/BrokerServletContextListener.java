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

package org.trianacode.TrianaCloud.Broker;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.log4j.Logger;
import org.trianacode.TrianaCloud.Utils.Task;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kieran David Evans
 * @version 1.0.0 Feb 26, 2012
 */
public class BrokerServletContextListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    private static Receiver receiver;
    private static RPCServer rpcServer;
    private static Thread receiverThread;
    private static Thread rpcServerThread;

    public void contextInitialized(ServletContextEvent event) {
        ServletContext sc = event.getServletContext();

        String host = sc.getInitParameter("rabbitmq.host");
        int port = Integer.parseInt(sc.getInitParameter("rabbitmq.port"));
        String user = sc.getInitParameter("rabbitmq.user");
        String pass = sc.getInitParameter("rabbitmq.pass");
        String rpc_queue_name = sc.getInitParameter("rabbitmq.rpc_queue_name");
        String vHost = sc.getInitParameter("rabbitmq.vhost");

        ConcurrentHashMap<String, Task> taskMap = new ConcurrentHashMap<String, Task>();
        ConcurrentHashMap<String, Task> resultMap = new ConcurrentHashMap<String, Task>();

        receiver = new Receiver(host, port, user, pass, vHost);
        rpcServer = new RPCServer(host, port, user, pass, vHost, rpc_queue_name);

        String replyQueue = receiver.init();
        try {
            rpcServer.init();
        } catch (ServletException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        receiverThread = new Thread(receiver);
        receiverThread.start();

        rpcServerThread = new Thread(rpcServer);
        rpcServerThread.start();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(user);
        factory.setPassword(pass);

        sc.setAttribute("RabbitMQConnectionFactory", factory);
        sc.setAttribute("taskmap", taskMap);
        sc.setAttribute("replyQueue", replyQueue);
        sc.setAttribute("resultMap", resultMap);
        sc.setAttribute("rpc_queue_name", rpc_queue_name);
    }

    public void contextDestroyed(ServletContextEvent event) {
        receiver.stopRunning();
        rpcServer.stopRunning();
        try {
            receiverThread.join();
            rpcServerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
