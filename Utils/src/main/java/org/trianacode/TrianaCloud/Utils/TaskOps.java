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
        if (b == null) {
            logger.debug("Encoded Task is null");
            throw new IOException("Encoded Task is null!");
        }
        return b;
    }
}
