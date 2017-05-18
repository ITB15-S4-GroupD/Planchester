package TeamF.Application.helper;

import Utils.Enum.EventType;
import javafx.scene.paint.Color;

public class EventDutyHelper {
    public static Color getColor(EventType eventType) {
        if (eventType != null) {
            switch (eventType) {
                case Concert:
                    return Color.SADDLEBROWN;
                case Tour:
                    return Color.DARKSALMON;
                case Opera:
                    return Color.DODGERBLUE;
                case Rehearsal:
                    return Color.DARKSLATEBLUE;
                case Hofkapelle:
                    return Color.DARKMAGENTA;
                case NonMusicalEvent:
                    return Color.DARKCYAN;
            }
        }

        return null;
    }

    public static String getEventTypeText(EventType eventType) {
        if (eventType != null) {
            switch (eventType) {
                case NonMusicalEvent:
                    return "Non Musical Event";
                case Hofkapelle:
                    return "Hofkapelle";
                case Rehearsal:
                    return "Rehearsal";
                case Opera:
                    return "Opera";
                case Tour:
                    return "Tour";
                case Concert:
                    return "Concert";
            }
        }

        return null;
    }

    public static String getEventTypeCode(EventType eventType) {
        if (eventType != null) {
            switch (eventType) {
                case NonMusicalEvent:
                    return "NM";
                case Hofkapelle:
                    return "HK";
                case Rehearsal:
                    return "RH";
                case Opera:
                    return "OP";
                case Tour:
                    return "T";
                case Concert:
                    return "CO";
            }
        }

        return null;
    }
}
