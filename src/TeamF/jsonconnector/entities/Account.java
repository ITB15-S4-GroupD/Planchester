package TeamF.jsonconnector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import TeamF.jsonconnector.enums.AccountRole;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements JSONObjectEntity {
    private Integer _accountID;
    private String _username;
    private String _password;
    private AccountRole _accountRole;

    @JsonGetter("id")
    public Integer getAccountID() {
        return _accountID;
    }

    @JsonGetter("username")
    public String getUsername() {
        return _username;
    }

    @JsonGetter("password")
    public String getPassword() {
        return _password;
    }

    @JsonGetter("role")
    public AccountRole getRole() {
        return _accountRole;
    }

    @JsonSetter("id")
    public void setAccountID(Integer accountID) {
        _accountID = accountID;
    }

    @JsonSetter("username")
    public void setUsername(String username) {
        _username = username;
    }

    @JsonSetter("password")
    public void setPassword(String password) {
        _password = password;
    }

    @JsonSetter("role")
    public void setRole(AccountRole accountRole) {
        _accountRole = accountRole;
    }

    @Override
    public String getEntityName() {
        return "Account";
    }

    @Override
    public String getDisplayName() {
        return getUsername();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
