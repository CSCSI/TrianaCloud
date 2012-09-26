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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

/**
 * @author Kieran David Evans
 * @version 1.0.0 Feb 26, 2012
 *          <p/>
 *          Task encapsulates a task (or job), and any relevant information (e.g. where to send results).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

    public static final int PENDING = 0;
    public static final int SENT = 1;
    public static final int COMPLETE = 2;

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
    private String _returnCode;
    private String _UUID;
    private int _ID;
    private int _State;
    private int _timeToWait;
    private boolean _NOTASK;
    private String _commandArguments;

    public void setCommandArguments(String args) {
        this._commandArguments = args;
    }

    public String getCommandArguments() {
        return this._commandArguments;
    }

    public void setTimeToWait(int i) {
        this._timeToWait = i;
    }

    public int getTimeToWait() {
        return this._timeToWait;
    }

    public void setNOTASK(boolean i) {
        this._NOTASK = i;
    }

    public boolean getNOTASK() {
        return this._NOTASK;
    }


    public void setState(int s) {
        this._State = s;
    }

    public int getState() {
        return this._State;
    }

    public int getID() {
        return _ID;
    }

    public void setID(int id) {
        _ID = id;
    }

    public String getUUID() {
        return _UUID;
    }

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

    public void setUUID(String uuid) {
        this._UUID = uuid;
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

    public String getReturnCode() {
        return _returnCode;
    }

    public void setReturnCode(String returnCode) {
        this._returnCode = returnCode;
    }
}
