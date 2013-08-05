/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *        or more contributor license agreements.  See the NOTICE file
 *        distributed with this work for additional information
 *        regarding copyright ownership.  The ASF licenses this file
 *        to you under the Apache License, Version 2.0 (the
 *        "License"); you may not use this file except in compliance
 *        with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing,
 *        software distributed under the License is distributed on an
 *        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *        KIND, either express or implied.  See the License for the
 *        specific language governing permissions and limitations
 *        under the License.
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