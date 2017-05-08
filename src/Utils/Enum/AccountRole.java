package Utils.Enum;

/**
 * Created by julia on 13.04.2017.
 */
public enum AccountRole {
    Musician, Administrator, Manager, Substitute, Section_representative, Orchestral_facility_manager, Music_librarian;

    public static boolean contains(String s) {
        for (AccountRole et : values()) {
            if (et.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}