package org.trianacode.TrianaCloud.CommandLineExecutor;

import org.trianacode.TrianaCloud.Utils.Task;
import org.trianacode.TrianaCloud.Utils.TaskExecutionException;
import org.trianacode.TrianaCloud.Utils.TaskExecutor;

import java.io.*;

/**
 * Hello world!
 *
 */
public class Executor extends TaskExecutor 
{
    private Task task;
    File temp;
    @Override
    public byte[] executeTask() throws TaskExecutionException {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("cmd /c dir");
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
            StringBuilder b = new StringBuilder();
            while ((line = bre.readLine()) != null) {
                b.append(line);
            }
            bre.close();
            p.waitFor();
            System.out.println(b.toString()+"Done.");
            task.setName(b.toString());
        }
        catch (Exception err) {
            err.printStackTrace();
        }

        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
