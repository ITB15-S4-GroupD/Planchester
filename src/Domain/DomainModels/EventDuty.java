package Domain.DomainModels;

import Domain.IEventDutyHandler;
import Domain.Entities.EventDutyEntity;

/**
 * Created by julia on 13.04.2017.
 */
public class EventDuty implements IEventDutyHandler {
    private EventDutyEntity eventDuty;

    @Override
    public EventDutyEntity getEventDuty() {
        return eventDuty;
    }

    @Override
    public void setEventDuty(EventDutyEntity eventDuty) {
        this.eventDuty = eventDuty;
    }

    //TODO julia: validate();
}