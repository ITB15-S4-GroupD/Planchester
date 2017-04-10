package Domain.PresentationModels;

import Domain.Model.EventDutyEntity;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventDutyDTO {

    private EventDutyEntity eventDuty;

    public EventDutyDTO(EventDutyEntity eventDuty) {
        this.eventDuty = eventDuty;
    }

    public EventDutyEntity getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(EventDutyEntity eventDuty) {
        this.eventDuty = eventDuty;
    }
}
