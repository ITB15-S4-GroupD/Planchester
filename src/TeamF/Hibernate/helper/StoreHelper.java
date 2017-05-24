package TeamF.Hibernate.helper;

import javax.persistence.EntityManager;

public class StoreHelper {
    public static boolean storeEntities(EntityManager session) {
        try {
            session.flush();
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }

        return false;
    }
}
