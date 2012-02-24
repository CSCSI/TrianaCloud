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

    public String init() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
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

                System.out.println("Job: " + t.call + " took " + t.totalTime().getTime());

                ///TODO: make Task runnable. We can then stick results into the task, and let it do the approp thing.

                taskMap.remove(corrid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
