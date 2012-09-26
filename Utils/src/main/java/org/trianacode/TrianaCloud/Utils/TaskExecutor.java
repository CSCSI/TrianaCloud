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

import org.apache.log4j.Logger;

/**
 * @author Kieran David Evans
 * @version 1.0.0 Feb 26, 2012
 */

public abstract class TaskExecutor {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    private Logger getLogger() {
        return logger;
    }

    abstract public byte[] executeTask() throws TaskExecutionException;

    abstract public void setTask(Task task);

    abstract public String getRoutingKey();
}
