package Domain.Interfaces;

import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import java.sql.Timestamp;

/**
 * Created by timorzipa on 08/05/2017.
 */
public interface IEventDuty {
    Integer getEventDutyId();
    void setEventDutyId(Integer eventDutyId);
    String getName();
    void setName(String name);
    String getDescription();
    void setDescription(String description);
    Timestamp getStartTime();
    void setStartTime(Timestamp startTime);
    Timestamp getEndTime();
    void setEndTime(Timestamp endTime);
    EventType getEventType();
    void setEventType(EventType eventType);
    EventStatus getEventStatus();
    void setEventStatus(EventStatus eventStatus);
    String getConductor();
    void setConductor(String conductor);
    String getLocation();
    void setLocation(String location);
    Double getPoints();
    void setPoints(Double points);
    Integer getRehearsalFor();
    void setRehearsalFor(Integer rehearsalFor);
    Integer getInstrumentation();
    void setInstrumentation(Integer instrumentation);
}