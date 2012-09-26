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

package org.trianacode.TrianaCloud.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    @Override
    public void run() {
//        System.out.println("Reading stream : " + description);
        try {
            String str;
            while ((str = inreader.readLine()) != null) {
                System.out.println(" >> " + description + " : " + str);
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


