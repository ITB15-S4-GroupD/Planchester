package Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by julia on 16.04.2017.
 */
public class DatabaseConnectionHandler {
    private final String HIBERNATE_CONFIGURARION = "hibernate.cfg.xml";

    private static DatabaseConnectionHandler instance = null;
    private Session session;

    private DatabaseConnectionHandler() {
        // Exists only to defeat instantiation.
    }

    public static DatabaseConnectionHandler getInstance() {
        if(instance == null) {
            instance = new DatabaseConnectionHandler();
        }
        return instance;
    }

    public void beginSession() {
        if(session == null) {
            Configuration cfg = new Configuration();
            cfg.configure(HIBERNATE_CONFIGURARION);
            SessionFactory factory = cfg.buildSessionFactory();
            session = factory.openSession();
        }
    }

    public void closeSession() {
        if(session != null) {
            session.close();
        }
    }

    public Session beginTransaction() {
        session.beginTransaction();
        return session;
    }

    public void commitTransaction() {
        session.getTransaction().commit();
    }
}