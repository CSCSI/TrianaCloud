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

package org.trianacode.TrianaCloud.CommandLineExecutor;

import org.apache.log4j.Logger;
import org.trianacode.TrianaCloud.Utils.*;

import java.io.*;

/**
 * Hello world!
 */
public class Executor extends TaskExecutor {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    private Task task;
    File temp;

    @Override
    public byte[] executeTask() throws TaskExecutionException {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("ls -alh");
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));

            StringBuilder b = new StringBuilder();

            while ((line = bri.readLine()) != null) {
                b.append(line);
            }
            bri.close();
            while ((line = bre.readLine()) != null) {
                b.append(line);
            }
            bre.close();
            p.waitFor();

            task.setReturnDataType("String");
            String retData = b.toString();

            task.setReturnData(retData.getBytes());
            task.setReturnDataMD5(MD5.getMD5Hash(retData.getBytes()));

            System.out.println(new String(task.getReturnData(), "UTF-8") + "Done.");
            return TaskOps.encodeTask(task);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public void setTask(Task task) {
        this.task = task;
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
        return "*.cmd";
    }
}
