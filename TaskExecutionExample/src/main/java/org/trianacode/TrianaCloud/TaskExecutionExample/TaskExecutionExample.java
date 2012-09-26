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