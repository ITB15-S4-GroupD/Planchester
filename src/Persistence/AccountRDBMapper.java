package Persistence;

import Persistence.Entities.AccountEntity;
import org.hibernate.Session;
import javax.persistence.NoResultException;

/**
 * Created by timor on 04.05.2017.
 */

public class AccountRDBMapper extends Mapper<AccountEntity> {
    @Override
    Class<AccountEntity> getEntityClass() {
        return AccountEntity.class;
    }

    public static AccountEntity getAccount(String username, String password) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        AccountEntity accountEntity;
        try {
            accountEntity = (AccountEntity) session.createQuery("FROM AccountEntity WHERE username = '" + username + "' AND password = '" + password + "'").getSingleResult();
        } catch (NoResultException exception) {
            accountEntity = null;
        }
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return accountEntity;
    }
}