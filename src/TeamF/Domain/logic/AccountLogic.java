package TeamF.Domain.logic;

import javafx.util.Pair;
import TeamF.Domain.entities.Account;
import TeamF.Domain.enums.AccountProperty;
import TeamF.Domain.interfaces.EntityLogic;
import java.util.LinkedList;
import java.util.List;
import static TeamF.Domain.enums.AccountProperty.*;

public class AccountLogic implements EntityLogic<Account, AccountProperty> {
    protected AccountLogic() {
    }

    @Override
    public List<Pair<String, String>> validate(Account account, AccountProperty... properties) {
        List<Pair<String, String>> resultList = new LinkedList<>();

        LOOP:
        for (AccountProperty property : properties) {

            switch (property) {
                case USERNAME:
                    if (account.getUsername() == null) {
                        resultList.add(new Pair<>(String.valueOf(USERNAME), "is empty"));
                    }

                    break;

                // @TODO: validate the cases
                // use AccountLogic for the account specific logic
            }
        }

        return resultList;
    }

    @Override
    public List<Pair<String, String>> validate(Account account) {
        return validate(account, AccountProperty.values());
    }
}
