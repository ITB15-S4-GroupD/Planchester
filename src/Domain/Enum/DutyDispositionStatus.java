package Domain.Enum;

/**
 * Created by julia on 13.04.2017.
 */
public enum DutyDispositionStatus {
    Spare, Illness, Normal;

    public static boolean contains(String s) {
        for (DutyDispositionStatus enumValue : values()) {
            if (enumValue.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}