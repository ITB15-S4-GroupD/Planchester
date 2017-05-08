package Persistence;

import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by julia on 26.04.2017.
 */
public class PersistanceFacade<T> {

    private final Class<T> persistanceClass;

    public PersistanceFacade() {
        persistanceClass = (Class<T>)
                ((ParameterizedType)getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[0];
    }

    public T get(int oid) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        T object = session.get(persistanceClass,oid);
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return object;
    }

    public T put(T obj) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        session.saveOrUpdate(obj);
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return obj;
    }

    public void remove(int oid) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        Object object = (T)session.get(persistanceClass, oid);
        session.delete(object);
        DatabaseConnectionHandler.getInstance().commitTransaction();
    }

    public List<T> list(Predicate<T> predicate) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(persistanceClass);
        Root<T> root = query.from(persistanceClass);
        query.select(root);

        List<T> list;
        if (predicate == null) {
            list = session.createQuery(query).list();
        } else {
            list = session.createQuery(query).stream().filter(predicate).collect(Collectors.toList());
        }

        DatabaseConnectionHandler.getInstance().commitTransaction();
        return list;
    }
}