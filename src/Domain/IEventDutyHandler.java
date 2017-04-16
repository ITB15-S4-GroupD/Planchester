package Domain;

import Domain.Entities.EventDutyEntity;

/**
 * Created by julia on 13.04.2017.
 */
public interface IEventDutyHandler {
    public EventDutyEntity getEventDuty();
    public void setEventDuty(EventDutyEntity eventDuty);
}
