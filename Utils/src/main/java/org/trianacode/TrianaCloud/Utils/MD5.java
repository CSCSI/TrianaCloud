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