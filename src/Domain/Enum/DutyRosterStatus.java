package Domain.Enum;

/**
 * Created by julia on 13.04.2017.
 */
public enum DutyRosterStatus {
    Unpublished, Published;

    public static boolean contains(String s) {
        for (DutyRosterStatus enumValue : values()) {
            if (enumValue.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
