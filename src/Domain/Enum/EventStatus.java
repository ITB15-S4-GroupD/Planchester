package Domain.Enum;

/**
 * Created by timorzipa on 11/04/2017.
 */
public enum EventStatus {
    Unpublished, Published, Cancelled;

    public static boolean contains(String s) {
        for (EventStatus enumValue : values()) {
            if (enumValue.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
