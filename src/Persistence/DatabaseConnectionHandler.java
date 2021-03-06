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
    private Configuration cfg;
    private Session session;
    private SessionFactory factory;

    private DatabaseConnectionHandler() {
        // Exists only to defeat instantiation.
    }

    public static DatabaseConnectionHandler getInstance() {
        if(instance == null) {
            instance = new DatabaseConnectionHandler();
        }
        return instance;
    }

    public void readConfiguration() {
        if(cfg == null) {
            cfg = new Configuration();
            cfg.configure(HIBERNATE_CONFIGURARION);
            openSession();
        }
    }

    private void openSession() {
        factory = cfg.buildSessionFactory();
        session = factory.openSession();
    }

    public void closeSession() {
        session.close();
    }

    public Session beginTransaction() {
        session.beginTransaction();
        return session;
    }

    public void commitTransaction() {
        session.getTransaction().commit();
    }
}