package Domain.Enum;

import java.awt.*;

/**
 * Created by timorzipa on 06/04/2017.
 */
public enum EventType {
    Opera, Concert, Tour, Hofkapelle, Rehearsal, NonMusicalEvent;

    public static boolean contains(String s) {
        for (EventType enumValue : values()) {
            if (enumValue.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
}