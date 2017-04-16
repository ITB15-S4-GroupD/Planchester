package Domain.PresentationModels;

import Domain.IEventDutyHandler;
import Domain.Entities.EventDutyEntity;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventDutyDTO implements IEventDutyHandler {
    private EventDutyEntity eventDuty;

    public EventDutyDTO(EventDutyEntity eventDuty) {
        this.eventDuty = eventDuty;
    }

    @Override
    public EventDutyEntity getEventDuty() {
        return eventDuty;
    }

    @Override
    public void setEventDuty(EventDutyEntity eventDuty) {
        this.eventDuty = eventDuty;
    }
}
