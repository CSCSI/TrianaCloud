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

package org.trianacode.TrianaCloud.TaskExecutionExample;

import org.apache.log4j.Logger;
import org.trianacode.TrianaCloud.Utils.Task;
import org.trianacode.TrianaCloud.Utils.TaskExecutionException;
import org.trianacode.TrianaCloud.Utils.TaskExecutor;


public class TaskExecutionExample extends TaskExecutor {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    private Task task;
    private String data;

    @Override
    public byte[] executeTask() throws TaskExecutionException {
        String response;
        System.out.println(" [.] data(" + data + ")");
        response = task.getName().toUpperCase();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            TaskExecutionException ex = new TaskExecutionException();
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
        return response.getBytes();
    }

    @Override
    public void setTask(Task t) {
        this.task = t;
        this.data = t.getName();
    }

    @Override
    public String getRoutingKey() {
        return "*.test";
    }
}