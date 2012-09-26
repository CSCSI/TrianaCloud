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
