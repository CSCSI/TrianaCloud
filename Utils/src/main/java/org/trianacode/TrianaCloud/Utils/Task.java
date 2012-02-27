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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

/**
 * Task encapsulates a task (or job), and any relevant information (e.g. where to send results).
 * TODO: A toWire method that converts the necessary data to some wire format (e.g. ASN.1). Keep it simple.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    ///TODO: Task metadata class.
    public String origin;
    public String name;
    public byte[] data;
    public long dispatchTime;
    public String routingKey;

    public String getOrigin() {
        return origin;
    }

    public String getName() {
        return name;
    }

    public long getDispatchTime() {
        return dispatchTime;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDispatchTime(long dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public Date getTotalTime() {
        return new Date(System.currentTimeMillis() - dispatchTime);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}