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

package org.trianacode.TrianaCloud.TaskExecutionExample;

import org.trianacode.TrianaCloud.Utils.Task;
import org.trianacode.TrianaCloud.Utils.TaskExecutionException;
import org.trianacode.TrianaCloud.Utils.TaskExecutor;

import org.apache.log4j.Logger;


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
}