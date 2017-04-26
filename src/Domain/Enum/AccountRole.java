package Domain.Enum;

/**
 * Created by julia on 13.04.2017.
 */
public enum AccountRole {
    Musician, Administrator, Manager, Substitute, Section_representative;

    public static boolean contains(String s) {
        for (AccountRole et : values()) {
            if (et.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
