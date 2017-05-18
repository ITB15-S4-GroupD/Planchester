package TeamF.Domain.interfaces;

import javafx.util.Pair;
import java.util.List;

public interface EntityLogic<T extends DomainEntity, E extends DomainEntityProperty> {
    /**
     * validates the attributes on the given object
     * @param object    the object which should be checked
     * @return          a list of key-value pairs:
     *                  key: specifies the property
     *                  value: a detailed error message for the user
     *                  is never null
     */
    public List<Pair<String, String>> validate(T object, E... properties);

    /**
     * validates all attributes on the given object
     * @param object    the object which should be checked
     * @return          a list of key-value pairs:
     *                  key: specifies the property
     *                  value: a detailed error message for the user
     *                  is never null
     */
    public List<Pair<String, String>> validate(T object);
}
