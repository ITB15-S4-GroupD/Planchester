package TeamF.Hibernate.facade;

import org.hibernate.HibernateException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SessionFactory {
    private static EntityManagerFactory _entityManagerFactory;

    private SessionFactory() {
    }

    public static EntityManager getSession() throws HibernateException {
        if (_entityManagerFactory == null) {
            try {
                _entityManagerFactory = Persistence.createEntityManagerFactory("sem4_team2");
            } catch (Throwable ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }

        return _entityManagerFactory.createEntityManager();
    }

    public static void close() {
        if (_entityManagerFactory != null) {
            _entityManagerFactory.close();
        }
    }
}
