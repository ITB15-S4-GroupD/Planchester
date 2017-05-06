package Application;

import Domain.PermissionRestrain;
import Domain.PermissionObject;
import Persistence.AccountRDBMapper;
import Persistence.Entities.AccountEntity;
import Utils.Enum.AccountRole;

/**
 * Created by timorzipa on 04/05/2017.
 */
public class AccountAdministrationManager {

    private static AccountRole userRole = null;
    private static AccountEntity userAccount = null;
    private static PermissionRestrain userRestrain = null;

    public static AccountEntity getAccount(String username, String password) {
        return AccountRDBMapper.getAccount(username, password);
    }

    public static void setLoggedInUser(AccountEntity account){

        if(userAccount == null){
            userAccount = account;
            userRole = AccountRole.valueOf(userAccount.getAccountRole());
            userRestrain = new PermissionObject(userRole);
        }
    }

    public static AccountEntity getLoggedInAccount(){
        return userAccount;
    }

    public static String getLoggedInName(){
        return userAccount.getUsername() + " as " + userAccount.getAccountRole();
    }

    public static PermissionRestrain getUserRestrain(){
        return userRestrain;
    }

}
