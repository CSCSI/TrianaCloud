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

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Andrew Harrison
 * @version 1.0.0 Jan 15, 2011
 */

public class MD5 {


    public static String getMD5Hash(String input) {
        return getMD5Hash(input.getBytes());
    }

    public static String getMD5Hash(byte[] input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input);
            return toHex(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String getMD5Hash(InputStream in) throws IOException {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] input = new byte[4096];
            int c;
            while ((c = in.read(input)) != -1) {
                md5.update(input, 0, c);
            }
            return toHex(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


    /**
     * Return an 8 byte representation of the 32 byte MD5 digest
     *
     * @param digest the message digest
     * @return String 8 byte hexadecimal
     */
    public static String toHex(byte[] digest) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            buf.append(Integer.toHexString((int) digest[i] & 0x00FF));
        }
        return buf.toString();
    }

}