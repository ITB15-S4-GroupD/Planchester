package Domain.Enum;

/**
 * Created by julia on 13.04.2017.
 */
public enum RequestType {
    Leave_of_absence, Playrequest;

    public static boolean contains(String s) {
        for (RequestType enumValue : values()) {
            if (enumValue.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
