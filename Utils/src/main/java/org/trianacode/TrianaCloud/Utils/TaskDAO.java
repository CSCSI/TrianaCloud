package org.trianacode.TrianaCloud.Utils;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: keyz
 * Date: 09/05/12
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
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
