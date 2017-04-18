package Domain.Models;

import Domain.Entities.EventDutyEntity;

import java.awt.*;

/**
 * Created by julia on 13.04.2017.
 */
public class EventDutyModel {
    private EventDutyEntity eventDuty;

    public EventDutyModel(EventDutyEntity eventDutyEntity) {
        //todo julia: change!
        eventDuty = eventDutyEntity;
    }

    public EventDutyEntity getEventDuty() {
        return eventDuty;
    }

    public void setEventDuty(EventDutyEntity eventDuty) {
        this.eventDuty = eventDuty;
    }

    //TODO julia: validate();
}