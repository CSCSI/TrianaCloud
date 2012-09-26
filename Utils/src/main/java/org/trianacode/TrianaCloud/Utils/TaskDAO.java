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

import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.List;

/**
 * @author Kieran David Evans
 */
public class TaskDAO extends DAO {

    /*
     * Inserts a record containing the task.
     *
     * @param t The {@link Task} to insert
     * @return The {@link Task} inserted
     */
    public Task create(Task t) {
        try {
            begin();
            getSession().save(t);
            commit();
            return t;
        } catch (HibernateException e) {
            rollback();
            throw e;
        }
    }

    /*
    * Deletes the record containing the specified UUID
    *
    * @param t The UUID of the {@link Task} to delete
    */

    public void delete(String uuid) {
        try {
            Task task;
            begin();
            Query q = getSession().createQuery(
                    "from Task t where t.UUID = :uuid"
            ).setString("uuid", uuid);
            task = (Task) q.uniqueResult();
            commit();
            begin();
            getSession().delete(q.uniqueResult());
            commit();
        } catch (HibernateException e) {
            rollback();
            throw e;
        }
    }

    /*
     * Get a Task by ID
     *
     * @param id The ID to search by
     * @return The Task found
     */
    public Task get(Integer id) {
        try {
            begin();
            Query q = getSession().createQuery(
                    "from Task t where t.id = :id");
            q.setString("id", id.toString());
            Task t = (Task) q.uniqueResult();
            commit();
            return t;
        } catch (HibernateException e) {
            throw e;
        }
    }

    /*
     * Update the record corresponding to the {@link Task}
     *
     * @param t The {@link Task} to save
     */
    public void save(Task t) {
        try {
            begin();
            getSession().update(t);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw e;
        }
    }

    /*
     * The next Task in the DB marked as Task.PENDING
     *
     * @return  The next pending {@link Task}
     */
    public Task getNextPending() {
        try {
            begin();
            Query q = getSession().createQuery(
                    "from Task t where t.State = :state");
            q.setString("state", "" + Task.PENDING);
            q.setMaxResults(1);
            Task t = (Task) q.uniqueResult();
            t.setState(Task.SENT);
            getSession().update(t);
            commit();
            return t;
        } catch (HibernateException e) {
            throw e;
        }
    }

    /*
     * Gets a Task from the DB by UUID
     *
     * @param uuid The UUID of the {@link Task} to retreive
     *
     * @return The Task
     */
    public Task getByUUID(String uuid) {
        try {
            begin();
            Query q = getSession().createQuery(
                    "from Task t where t.UUID = :uuid");
            q.setString("uuid", uuid);
            q.setMaxResults(1);
            Task t = (Task) q.uniqueResult();
            commit();
            return t;
        } catch (HibernateException e) {
            throw e;
        }
    }


    /*
     * Gets all Tasks from the DB
     *
     * @return The Tasks
     */
    @SuppressWarnings("unchecked")
    public List<Task> list() {
        try {
            begin();
            Query q = getSession().createQuery("from Task");
            List<Task> t = q.list();
            commit();
            return t;
        } catch (HibernateException e) {
            throw e;
        }
    }


    /*
     * Get a subset of the Tasks from the DB
     *
     * @return The Tasks
     */
    public List<Task> list(int firstResult, int maxResults) {
        try {
            begin();
            Query q = getSession().createQuery("from Task");
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResults);
            List<Task> t = q.list();

            System.out.println(t.size());
            commit();
            return t;
        } catch (HibernateException e) {
            throw e;
        }
    }
}
