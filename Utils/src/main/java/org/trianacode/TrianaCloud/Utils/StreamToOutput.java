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

package org.trianacode.TrianaCloud.Utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Ian Harvey
 * @version 1.0.0 Apr 18, 2012
 */
public class StreamToOutput implements Runnable {
    private Logger logger = Logger.getLogger(this.getClass().toString());

    private InputStream inputStream;
    private String description;
    private Thread thread;
    private BufferedReader inreader;

    public StreamToOutput(InputStream inputStream, String description) {
        this.inputStream = inputStream;
        this.description = description;
        inreader = new BufferedReader(new InputStreamReader(this.inputStream));

    }

    public void start() {
        thread = new Thread(this);
        thread.run();
    }

    @Override
    public void run() {
//        System.out.println("Reading stream : " + description);
        try {
            String str;
            while ((str = inreader.readLine()) != null) {
                logger.info(" >> " + description + " : " + str);
            }
        } catch (IOException e) {
            logger.error("Error with stream " + description + " closing");
        } finally {
            try {
                inreader.close();
            } catch (IOException e) {
                logger.error(e);
            }
            logger.info("Closed streamReader " + description);
        }
    }
}


