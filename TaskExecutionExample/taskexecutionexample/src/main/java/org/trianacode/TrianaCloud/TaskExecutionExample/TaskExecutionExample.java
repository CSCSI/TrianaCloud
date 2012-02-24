package org.trianacode.TrianaCloud.TaskExecutionExample;

import org.trianacode.TrianaCloud.Worker.TaskExecutionException;
import org.trianacode.TrianaCloud.Worker.TaskExecutor;

public class TaskExecutionExample extends TaskExecutor {
    private String data;

    @Override
    public byte[] executeTask() throws TaskExecutionException {
        String response;
        int n = Integer.parseInt(data);

        System.out.println(" [.] fib(" + data + ")");
        response = "" + n * 2;
        try {
            Thread.sleep(n * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            TaskExecutionException ex = new TaskExecutionException();
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
        return response.getBytes();
    }

    @Override
    public void setData(byte[] data) {
        this.data = new String(data);
    }
}