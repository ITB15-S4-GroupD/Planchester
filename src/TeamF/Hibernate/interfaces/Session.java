package TeamF.Hibernate.interfaces;

import javax.persistence.EntityManager;

public interface Session {
    public void closeSession();
    public EntityManager getCurrentSession();
}