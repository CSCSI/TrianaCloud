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

package org.trianacode.TrianaCloud.PegasusExecutor;

import org.apache.log4j.Logger;
import org.shiwa.desktop.data.description.SHIWABundle;
import org.trianacode.TrianaCloud.Utils.MD5;
import org.trianacode.TrianaCloud.Utils.Task;
import org.trianacode.TrianaCloud.Utils.TaskExecutor;
import org.trianacode.TrianaCloud.Utils.TaskOps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Run Pegasus-plan with values found in a SHIWA Bundle
 */
public class PegasusExecutor extends TaskExecutor implements Runnable {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    Task task;
    File temp;
    private byte[] out = new byte[0];

    @Override
    public byte[] executeTask() {

        try {
            Thread thread = new Thread(this);
            thread.run();
            thread.join();

        } catch (Exception e) {
            System.out.println("Error in executor");
            e.printStackTrace();
        }

        try {
            task.setReturnDataType("binary");
            task.setReturnData(out);
            task.setReturnDataMD5(MD5.getMD5Hash(task.getReturnData()));
            return TaskOps.encodeTask(task);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Override
    public void setTask(Task t) {
        this.task = t;
        temp = null;
        try {
            temp = File.createTempFile("data", ".bundle");
            //temp.deleteOnExit();

            System.out.println(temp.getPath());

            FileOutputStream o = new FileOutputStream(temp);
            o.write(task.getData());

            System.out.println(task.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRoutingKey() {
        return "*.pegasus";
    }

    @Override
    public void run() {
        SHIWABundle b = null;
        try {
            b = new SHIWABundle(temp);
            RunPegasusPlan pegasusPlan = new RunPegasusPlan();
            out = pegasusPlan.executeBundle(b, null);
//            SHIWABundle execedBundle = testExecution(b);
//            out = TrianaBundle.getFileBytes(
//                    DataUtils.bundle(File.createTempFile("execed", ".tmp"),
//                            execedBundle.getAggregatedResource()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
