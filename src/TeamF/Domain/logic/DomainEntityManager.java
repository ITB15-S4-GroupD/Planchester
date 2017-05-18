package TeamF.Domain.logic;

import TeamF.Domain.enums.EntityType;
import TeamF.Domain.interfaces.EntityLogic;

public class DomainEntityManager {
    private static PersonLogic personLogic = new PersonLogic();
    private static AccountLogic accountLogic = new AccountLogic();

    public DomainEntityManager() {
    }

    public static EntityLogic getLogic(EntityType entity) {
        switch (entity) {
            case PERSON:
                return personLogic;
            case ACCOUNT:
                return accountLogic;
            default:
                return null;
        }
    }
}
