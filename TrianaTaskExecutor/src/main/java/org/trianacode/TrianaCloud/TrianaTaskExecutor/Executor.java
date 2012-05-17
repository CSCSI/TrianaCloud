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

package org.trianacode.TrianaCloud.TrianaTaskExecutor;

import org.apache.log4j.Logger;
import org.trianacode.TrianaCloud.Utils.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hello world!
 */
public class Executor extends TaskExecutor {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    Task task;
    File temp;
    private byte[] out = new byte[0];

    @Override
    public byte[] executeTask() {
        ///TODO: pass temp to triana via shiwa bundles.
        ///TODO: get the returned file, read it into task.setReturnData(), setReturnType to binary
        ///TODO return the task as below

        System.out.println("Runtime folder : " + System.getProperty("user.dir"));

        try {
            File tempReturnFile = File.createTempFile("outputBundle", ".zip");

            String executableString = "./triana.sh -n -p unbundle "
                    + temp.getAbsolutePath() + " "
                    + tempReturnFile.getAbsolutePath();

            System.out.println("Will run : " + executableString);

//            executableString = "ls";

            List<String> options = new ArrayList<String>();
            String[] optionsStrings = executableString.split(" ");
            Collections.addAll(options, optionsStrings);

            ProcessBuilder processBuilder = new ProcessBuilder(optionsStrings);
//            processBuilder.directory(new File("triana"));
            Process p = processBuilder.start();

            new StreamToOutput(p.getInputStream(), "std.out").start();
            new StreamToOutput(p.getErrorStream(), "std.err").start();

            p.waitFor();

            if (tempReturnFile.exists()) {
                System.out.println("Triana produced bundle at : "
                        + tempReturnFile.getAbsolutePath());
                out = getBytesFromFile(tempReturnFile);
            }

            task.setReturnDataType("binary");
            task.setReturnData(out);
            task.setReturnDataMD5(MD5.getMD5Hash(task.getReturnData()));
            task.setReturnCode("0");


            ///HACK: Hack to remove results folder
            removeResults();
            return TaskOps.encodeTask(task);  //To change body of implemented methods use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return new byte[0];
        }
    }

    public void removeResults() {
        File res = new File("/home/ubuntu/results");
        if (!res.exists())
            return;
        if (!res.isDirectory())
            res.delete();

        String[] list = res.list();


        for (String file: list){
            File f = new File(res,file);
            f.delete();
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
        return "*.triana";
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();


        if (length > Integer.MAX_VALUE) {
            return new byte[0];
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

}
