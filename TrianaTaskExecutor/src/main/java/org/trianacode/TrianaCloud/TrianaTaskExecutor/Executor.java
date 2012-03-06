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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Hello world!
 */
public class Executor extends TaskExecutor {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    Task task;
    File temp;

    @Override
    public byte[] executeTask() throws TaskExecutionException {
        ///TODO: pass temp to triana via shiwa bundles.
        ///TODO: get the returned file, read it into task.setReturnData(), setReturnType to binary
        ///TODO return the task as below
        /*
        try {
            SHIWABundle b = new SHIWABundle(temp);
            Tria    naBundle tb = new TrianaBundle();
            SHIWABundle out = tb.executeBundle(b, null);
            File outfile = new File(out.getAggregatedResource().getFilename());
            FileInputStream fis = new FileInputStream(outfile);
            long length = outfile.length();

            if (length > Integer.MAX_VALUE) {
                // File is too large
                throw new Exception("File too large");
            }

            // Create the byte array to hold the data
            byte[] bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead;
            while (offset < bytes.length
                    && (numRead = fis.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + outfile.getName());
            }

            // Close the input stream and return bytes
            fis.close();
            //return bytes;
            System.out.println(outfile.getPath());
            return outfile.getPath().getBytes();
        } catch (SHIWADesktopIOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
           */
        try {
            task.setReturnDataType("binary");
            task.setReturnData(task.getData());
            task.setReturnDataMD5(MD5.getMD5Hash(task.getReturnData()));
            return TaskOps.encodeTask(task);  //To change body of implemented methods use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
