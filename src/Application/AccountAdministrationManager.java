package Application;

import Persistence.AccountRDBMapper;
import Persistence.Entities.AccountEntity;

/**
 * Created by timorzipa on 04/05/2017.
 */
public class AccountAdministrationManager {
    public static AccountEntity getAccount(String username, String password) {
        return AccountRDBMapper.getAccount(username, password);
    }
}
