package Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Created by julia on 16.04.2017.
 */
public class DatabaseHelper {
    public static Session beginSession() {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();
        return session;
    }

    public static void closeSession(Session session) {
        session.getTransaction().commit();
        session.close();
    }
}