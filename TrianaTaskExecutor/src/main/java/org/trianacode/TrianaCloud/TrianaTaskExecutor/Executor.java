package org.trianacode.TrianaCloud.TrianaTaskExecutor;

import org.shiwa.desktop.data.description.SHIWABundle;
import org.shiwa.desktop.data.util.exception.SHIWADesktopIOException;
import org.trianacode.TrianaCloud.Worker.TaskExecutionException;
import org.trianacode.TrianaCloud.Worker.TaskExecutor;
import org.trianacode.shiwa.bundle.TrianaBundle;

import java.io.*;

/**
 * Hello world!
 *
 */
public class Executor extends TaskExecutor
{
    File temp;
    @Override
    public byte[] executeTask() throws TaskExecutionException {
        try {
            SHIWABundle b = new SHIWABundle(temp);
            TrianaBundle tb = new TrianaBundle();
            SHIWABundle out = tb.executeBundle(b,null);
            File outfile = new File(out.getAggregatedResource().getFilename());
            FileInputStream fis = new FileInputStream(outfile);
            long length = outfile.length();

            if (length > Integer.MAX_VALUE) {
                // File is too large
                throw new Exception("File too large");
            }

            // Create the byte array to hold the data
            byte[] bytes = new byte[(int)length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead=fis.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "+outfile.getName());
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

        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setData(byte[] data) {
        temp = null;
        try {
            temp = File.createTempFile("data", ".bundle");
            //temp.deleteOnExit();

            System.out.println(temp.getPath());

            FileOutputStream o = new FileOutputStream(temp);
            o.write(data);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        //To change body of implemented methods use File | Settings | File Templates.
    }
}
