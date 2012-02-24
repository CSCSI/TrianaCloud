package org.trianacode.TrianaCloud.Ventilator;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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

    public Ventilator() throws Exception {
        taskMap = new HashMap<String, Task>();

        {   //RabbitMQ init
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
        }

        //Spin up the receive thread.
        receiver = new Receiver(taskMap);
        replyQueueName = receiver.init();
        recThread = new Thread(receiver);
        recThread.start();
    }

    public void call(String message) throws Exception {
        Task t = new Task("call", message);
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        taskMap.put(corrId, t);
        channel.basicPublish("", requestQueueName, props, message.getBytes());
        System.out.println("Sent job " + corrId + " with payload " + message);
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
                v.call(String.valueOf(i));
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