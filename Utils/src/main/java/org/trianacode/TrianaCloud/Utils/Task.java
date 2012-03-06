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
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    ///TODO: Task metadata class.
    ///TODO: add an Options propery. Can be used for anything, e.g. command line args, env properties whatever.
    private String _origin;
    private String _name;
    private byte[] _data;
    private long _dispatchTime;
    private String _routingKey;
    private String _dataType;
    private String _fileName;
    private byte[] _returnData;
    private String _returnDataType;
    private String _dataMD5;
    private String _returnDataMD5;

    public String getDataMD5() {
        return _dataMD5;
    }

    public String getReturnDataMD5() {
        return _returnDataMD5;
    }

    public String getDataType() {
        return _dataType;
    }

    public byte[] getReturnData() {
        return _returnData;
    }

    public String getReturnDataType() {
        return _returnDataType;
    }

    public String getOrigin() {
        return _origin;
    }

    public String getName() {
        return _name;
    }

    public long getDispatchTime() {
        return _dispatchTime;
    }

    public String getRoutingKey() {
        return _routingKey;
    }

    public String getFileName() {
        return _fileName;
    }

    public void setFileName(String f) {
        this._fileName = f;
    }

    public void setDataMD5(String md5) {
        this._dataMD5 = md5;
    }

    public void setReturnDataMD5(String md5) {
        this._returnDataMD5 = md5;
    }

    public void setDataType(String d) {
        this._dataType = d;
    }

    public void setReturnData(byte[] b) {
        this._returnData = b;
    }

    public void setReturnDataType(String d) {
        this._returnDataType = d;
    }

    public void setOrigin(String origin) {
        this._origin = origin;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setDispatchTime(long dispatchTime) {
        this._dispatchTime = dispatchTime;
    }

    public void setRoutingKey(String routingKey) {
        this._routingKey = routingKey;
    }

    public Date getTotalTime() {
        return new Date(System.currentTimeMillis() - _dispatchTime);
    }

    public byte[] getData() {
        return _data;
    }

    public void setData(byte[] data) {
        this._data = data;
    }
}