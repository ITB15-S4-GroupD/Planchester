package Domain.Enum;

/**
 * Created by julia on 13.04.2017.
 */
public enum PersonRole {
    Musician, Substitute, External_musician, Orchestral_facility_manager, Music_librarian, Manager;

    public static boolean contains(String s) {
        for (PersonRole enumValue : values()) {
            if (enumValue.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}