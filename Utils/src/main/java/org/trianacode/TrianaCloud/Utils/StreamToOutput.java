package org.trianacode.TrianaCloud.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ian Harvey
 * @version 1.0.0 Apr 18, 2012
 */
public class StreamToOutput implements Runnable {

    private InputStream inputStream;
    private String description;
    private Thread thread;
    private BufferedReader inreader;

    public StreamToOutput(InputStream inputStream, String description) {
        this.inputStream = inputStream;
        this.description = description;
        inreader = new BufferedReader(new InputStreamReader(this.inputStream));

    }

    public void start() {
        thread = new Thread(this);
        thread.run();
    }
    
    private String getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd_HH-mm-ss-SS_z");
        return dateFormat.format(new Date());
    }

    @Override
    public void run() {
//        System.out.println("Reading stream : " + description);
        try {
            String str;
            while ((str = inreader.readLine()) != null) {
                System.out.println("[" + getTimeStamp() + " >> " + description + " : " + str);
            }
        } catch (IOException e) {
            System.out.println("Error with stream " + description + " closing");
        } finally {
            try {
                inreader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Closed streamReader " + description);
        }
    }
}


