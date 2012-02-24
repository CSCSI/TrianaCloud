package org.trianacode.TrianaCloud.Ventilator;

import java.io.Serializable;
import java.util.Date;

/**
 * Task encapsulates a task (or job), and any relevant information (e.g. where to send results).
 */
public class Task implements Serializable {
    public String origin;
    public String call;
    public long dispatchTime;

    public Task(String o, String c) {
        origin = o;
        call = c;
        dispatchTime = System.currentTimeMillis();
    }

    public Date totalTime() {
        return new Date(System.currentTimeMillis() - dispatchTime);
    }
}
