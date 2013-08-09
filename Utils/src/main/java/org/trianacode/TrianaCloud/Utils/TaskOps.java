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

import de.undercouch.bson4jackson.BsonFactory;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Kieran David Evans
 * @version 1.0.0 Feb 26, 2012
 */

public class TaskOps {

    private static Logger logger = Logger.getLogger("org.trianacode.TrianaCloud.Utils.TaskOps");

    public static Task decodeTask(byte[] data) throws IOException {
        logger.trace("Decoding tast");
        ObjectMapper mapper = new ObjectMapper(new BsonFactory());
        Task t = mapper.readValue(data, 0, data.length, Task.class);
        if (t == null) {
            logger.debug("Decoded Task is null");
            throw new IOException("Decoded Task is null!");
        }
        return t;
    }

    public static byte[] encodeTask(Task t) throws IOException {
        logger.trace("Encoding tast");
        ObjectMapper mapper = new ObjectMapper(new BsonFactory());
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        mapper.writeValue(o, t);
        byte[] b = o.toByteArray();
        if (b == null || b.length == 0) {
            logger.debug("Encoded Task is null or zero length");
            throw new IOException("Encoded Task is null or zero length!");
        }
        return b;
    }
}
