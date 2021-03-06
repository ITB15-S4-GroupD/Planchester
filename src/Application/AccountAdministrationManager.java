package Application;

import Domain.Models.Permission;
import Persistence.Entities.AccountEntity;
import Persistence.Entities.MusicianPartEntity;
import Persistence.PersistanceFacade;
import Utils.Enum.AccountRole;
import Utils.Enum.SectionType;

import java.util.Collection;

/**
 * Created by timorzipa on 04/05/2017.
 */
public class AccountAdministrationManager {

    private static AccountAdministrationManager instance = null;
    private static PersistanceFacade<AccountEntity> persistanceFacade = new PersistanceFacade<>(AccountEntity.class);

    private AccountRole accountRole = null;
    private AccountEntity userAccount = null;
    private Permission permission = null;
    private SectionType sectionType;

    private AccountAdministrationManager() {}

    public static AccountAdministrationManager getInstance() {
        if(instance == null) {
            instance = new AccountAdministrationManager();
        }
        return instance;
    }

    public void setAccount(String username, String password) {
        AccountEntity accountEntity = persistanceFacade.get(p -> p.getUsername().equals(username) && p.getPassword().equals(password));
        if(accountEntity != null) {
            setLoggedInUser(accountEntity);
        }
    }

    private void setLoggedInUser(AccountEntity account) {
        resetUser();

        userAccount = account;
        accountRole = AccountRole.valueOf(userAccount.getAccountRole());

        // set permissions
        permission = new Permission(accountRole);

        // get section type
        if(account.getPersonAccountId() == null) {
            sectionType = null;
            return;
        }

        if(account.getPersonAccountId().getMusicianPartsByPersonId() != null) {
            Collection<MusicianPartEntity> parts = account.getPersonAccountId().getMusicianPartsByPersonId();
            for(MusicianPartEntity musicianPartEntity : parts) {
                sectionType = SectionType.valueOf(musicianPartEntity.getPartByPart().getSectionType());
                return;
            }
        }
    }

    public void resetUser() {
        accountRole = null;
        userAccount = null;
        permission = null;
        sectionType = null;
    }

    public SectionType getSectionType() { return sectionType; }

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
