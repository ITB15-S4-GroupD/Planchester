package Persistence;

import org.hibernate.Session;

/**
 * Created by julia on 26.04.2017.
 */
public abstract class Mapper<T> {

    abstract Class<T> getEntityClass();

    public T get(Integer objectID) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        T object = session.get(getEntityClass(), objectID);
        session.evict(object);
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return object;
    }

    public T put(T object) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        session.saveOrUpdate(object);
        session.evict(object);
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return object;
    }

    public void remove(Integer objectID) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        Object object = (T)session.get(getEntityClass(), objectID);
        session.delete(object);
        DatabaseConnectionHandler.getInstance().commitTransaction();
    }
}
