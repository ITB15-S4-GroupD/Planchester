package TeamF.Domain.helper;

import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import java.util.LinkedList;
import java.util.List;

public class EnumHelper {
    // save the values
    private static EventType[] _eventTypeList;

    public static EventStatus[] getEventStatusList() {
        return EventStatus.values();
    }

    public static EventType[] getEventTypeList() {
        return EventType.values();
    }

    public static EventType[] getBasicEventTypeList() {
        if (_eventTypeList == null) {
            List<EventType> eventTypes = new LinkedList();

            for (EventType item : EventType.values()) {
                // skip unnecessary event types
                if (item == EventType.Rehearsal) {
                    continue;
                }

                eventTypes.add(item);
            }

            EventType[] list = new EventType[eventTypes.size()];
            _eventTypeList = eventTypes.toArray(list);
        }

        return _eventTypeList;
    }
}
