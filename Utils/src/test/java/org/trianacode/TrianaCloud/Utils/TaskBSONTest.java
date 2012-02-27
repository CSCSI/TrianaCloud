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

package org.trianacode.TrianaCloud.Utils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;

public class TaskBSONTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TaskBSONTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TaskBSONTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        Task t = new Task();
        t.setData("abc123abc123".getBytes());
        t.setDispatchTime(System.currentTimeMillis());
        t.setName("testtask");
        t.setOrigin("TaskBSONTest");
        t.setRoutingKey("test.test");

        try {
            byte[] b = TaskOps.encodeTask(t);
            System.out.println(b.toString());

            Task nt = TaskOps.decodeTask(b);
            System.out.println(nt.getName());
            System.out.println(nt.getOrigin());
            System.out.println(nt.getRoutingKey());
            System.out.println(nt.getData().toString());
            System.out.println(nt.getTotalTime().getTime());

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            fail();
        }
        assertTrue(true);
    }
}
