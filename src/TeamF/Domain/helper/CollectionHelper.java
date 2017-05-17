package TeamF.Domain.helper;

import java.util.Collection;

public class CollectionHelper {
    public static boolean check(Collection collection) {
        if (collection != null && collection.size() > 0 && !collection.contains(null)) {
            return true;
        }

        return false;
    }

    public static boolean containsDuplicates(Collection collection) {
        int count;

        if (collection != null) {
            for (Object item : collection) {
                count = 0;

                for (Object entry : collection) {
                    if (item != null) {
                        if (item.equals(entry)) {
                            count++;

                            if (count > 1) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}
