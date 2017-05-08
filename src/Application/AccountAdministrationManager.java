package Application;

import Domain.Permission;
import Persistence.AccountRDBMapper;
import Persistence.Entities.AccountEntity;
import Utils.Enum.AccountRole;

/**
 * Created by timorzipa on 04/05/2017.
 */
public class AccountAdministrationManager {

    private static AccountAdministrationManager instance = null;

    private AccountRole accountRole = null;
    private AccountEntity userAccount = null;
    private Permission permission = null;

    private AccountAdministrationManager() {}

    public static AccountAdministrationManager getInstance() {
        if(instance == null) {
            instance = new AccountAdministrationManager();
        }
        return instance;
    }

    public void setAccount(String username, String password) {
        AccountEntity accountEntity = AccountRDBMapper.getAccount(username, password);
        if(accountEntity != null) {
            setLoggedInUser(accountEntity);
        }
    }

    private void setLoggedInUser(AccountEntity account) {
        userAccount = account;
        accountRole = AccountRole.valueOf(userAccount.getAccountRole());

        // set permissions
        permission = new Permission(accountRole);
    }

    public void resetUser() {
        accountRole = null;
        userAccount = null;
        permission = null;
    }

    public AccountEntity getLoggedInAccount(){
        return userAccount;
    }

    public Permission getUserPermissions(){
        return permission;
    }

    public AccountRole getAccountRole() {
        return accountRole;
    }
}
