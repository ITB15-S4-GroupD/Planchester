package TeamF.Domain.entities;

import TeamF.Domain.enums.AccountRole;
import TeamF.Domain.interfaces.DomainEntity;

public class Account implements DomainEntity {
    private Integer _accountID;
    private String _username;
    private String _password;
    private AccountRole _accountRole;

    public Integer getAccountID() {
        return _accountID;
    }

    public String getUsername() {
        return _username;
    }

    public String getPassword() {
        return _password;
    }

    public AccountRole getRole() {
        return _accountRole;
    }

    public void setAccountID(Integer accountID) {
        _accountID = accountID;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public void setRole(AccountRole accountRole) {
        _accountRole = accountRole;
    }

    @Override
    public int getID() {
        return getAccountID();
    }
}
