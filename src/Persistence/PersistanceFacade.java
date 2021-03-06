package Persistence;

import Persistence.Entities.EventDutyEntity;
import Utils.Enum.EventStatus;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by julia on 26.04.2017.
 */

public class PersistanceFacade<T> {

    private final Class<T> persistanceClass;

    public PersistanceFacade(Class<T> entityClass) {
        persistanceClass = entityClass;
    }

    public T get(int oid) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        T object = session.get(persistanceClass, oid);
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return object;
    }

    public T get(Predicate<T> predicate) {
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

        T object;
        if(list.isEmpty()) {
            object = null;
        } else {
            object = list.get(0);
        }

        DatabaseConnectionHandler.getInstance().commitTransaction();
        return object;
    }

    public T put(T obj) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        session.saveOrUpdate(obj);
        session.flush();
        session.refresh(obj);
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return obj;
    }

    public void remove(int oid) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        Object obj = (T)session.get(persistanceClass, oid);
        session.delete(obj);
        DatabaseConnectionHandler.getInstance().commitTransaction();
    }

    public void remove(T obj) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        session.delete(obj);
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
