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

package org.trianacode.TrianaCloud.Worker;

import org.apache.log4j.Logger;
import org.trianacode.TrianaCloud.Utils.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Kieran David Evans
 * @version 1.0.0 Feb 26, 2012
 *          The worker looks for tasks, and executes them. Easy.
 */

public class Worker extends RPCClient {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    private boolean continueLoop = true;
    private TaskExecutorLoader tel;

    private String executor = "org.trianacode.TrianaCloud.TrianaTaskExecutor.Executor";

    public Worker(String[] argv) {
        if (argv.length > 1){
            this.executor = argv[1];
        }
        try {
            loadPlugins(argv);

            /*
            "dart.triana" is the queue name. If the task is covered by multiple topic bindings, it's duplicated. If
            there are multiple queues for a topic binding, it's duplicated. This could be useful for debugging, logging
            and auditing. This could also be used to implement validation, which inherently ensures that the two
            (or more) duplicate tasks to compare and validate aren't executed on the same machine. But for a standard
            single worker, single task config, follow the rule of thumb.

            Rule of thumb: if the topic binding is the same, the queue name should be the same.
            If you're using wildcards in a topic name, MAKE SURE other topics/queues don't collide.

            e.g.:
                BAD:
                    dart.triana on worker1
                    #.triana on worker2

                GOOD:
                    dart.eddie.triana   (this will grab those with this exact topic)
                    *.triana            (* substitutes exactly one word. dart.eddie.triana ISN'T caught)
            */

            //String routingKey = "*.kieran";
//            String routingKey = "*.triana";

            for (String routingKey : tel.routingKeys) {
                logger.info(" [x] Routing Key: " + routingKey);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        logger.info(" [x] Sending RPC requests.");

        while (continueLoop) {
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                logger.error(e);
            }

            byte[] response;
            Task t = getTask(tel.routingKeys);

            try {
                if (t.getNOTASK()) {
                    int wait = t.getTimeToWait();
                    logger.info(" [.] Waiting for " + wait + " Seconds");
                    sendCompleteTask(TaskOps.encodeTask(t));
                    Thread.sleep(wait * 1000);
                    logger.info(" [x] Resuming.");
                    continue;
                }

                TaskExecutor ex;
                ///TODO: Use metadata to figure out which Executor to use.
                ///TODO: Figure out what wire-protocol to use. Something simple like ASN.1? Or a subset of it?

                ex = tel.getExecutor(this.executor);
                //TaskExecutor ex = tel.getExecutor("org.trianacode.TrianaCloud.CommandLineExecutor.Executor");
                ex.setTask(t);

                response = ex.executeTask();

                sendCompleteTask(response);
            } catch (Exception e) {
                ///TODO: filter the errors. Worker errors should skip the Ack, and allow the task to be redone.
                ///TODO: Two new exeptions for the task executor, one to indicate that the execution failed due to
                ///     the data, one to indicate some other error. The former would be ack'ed and sent back, as
                ///     it's a user error (i.e. the data is bad). The latter would indicate any other errors (bad
                ///     config, random error, missile strike).
                logger.error(" [.] " + e.toString());
                logger.error(e);
                response = new byte[0];
            }
        }
    }

    public static void main(String[] argv) {
        Worker w = new Worker(argv);
        w.run();
    }

    private void loadPlugins(String[] argv) throws MalformedURLException {
        logger.info(" [x] Loading Plugins");

        ClassLoader classLoader = Worker.class.getClassLoader();

        ///TODO:Make sure there's not a better way to do this
        URL[] urls = new URL[1];
        ///TODO:Grab a plugin dir from the config file
        String workingDir;
        File f;
        if (argv.length > 0) {
            workingDir = argv[0];
            f = new File(workingDir);
        } else {
            workingDir = System.getProperty("user.dir");
            f = new File(workingDir + File.separator + "depsdir");
        }
        logger.info("Addon path : " + f.getAbsolutePath());
        urls[0] = f.toURI().toURL();
        //Load plugins using the fancy-pants loader hacked together using the code from iharvey and the intarwebs
        tel = new TaskExecutorLoader(urls, classLoader);
    }
}
