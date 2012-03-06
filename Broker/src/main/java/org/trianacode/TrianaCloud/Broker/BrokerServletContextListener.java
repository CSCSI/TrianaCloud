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

package org.trianacode.TrianaCloud.Broker;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.log4j.Logger;
import org.trianacode.TrianaCloud.Utils.Task;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: keyz
 * Date: 25/02/12
 * Time: 22:55
 * To change this template use File | Settings | File Templates.
 */
public class BrokerServletContextListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    private static Receiver receiver;
    private static ConcurrentHashMap<String, Task> taskMap;
    private static ConcurrentHashMap<String, Task> resultMap;
    private static Thread receiverThread;
    private static String replyQueue;

    private String host;
    private int port;
    private String user;
    private String pass;

    private ConnectionFactory factory;

    public void contextInitialized(ServletContextEvent event) {
        ServletContext sc = event.getServletContext();

        host = sc.getInitParameter("rabbitmq.host");
        port = Integer.parseInt(sc.getInitParameter("rabbitmq.port"));
        user = sc.getInitParameter("rabbitmq.user");
        pass = sc.getInitParameter("rabbitmq.pass");

        taskMap = new ConcurrentHashMap<String, Task>();
        resultMap = new ConcurrentHashMap<String, Task>();

        receiver = new Receiver(taskMap, resultMap, host, port, user, pass);
        replyQueue = receiver.init();

        receiverThread = new Thread(receiver);
        receiverThread.start();

        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(user);
        factory.setPassword(pass);

        sc.setAttribute("RabbitMQConnectionFactory", factory);
        sc.setAttribute("taskmap", taskMap);
        sc.setAttribute("replyQueue", replyQueue);
        sc.setAttribute("resultMap", resultMap);
    }

    public void contextDestroyed(ServletContextEvent event) {
        receiver.stopRunning();
        try {
            receiverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
