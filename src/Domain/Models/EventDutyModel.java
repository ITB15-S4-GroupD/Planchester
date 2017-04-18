package Domain.Models;

import Domain.Entities.EventDutyEntity;
import Domain.Enum.EventType;

import javax.persistence.*;
import java.awt.*;
import java.sql.Timestamp;

/**
 * Created by julia on 13.04.2017.
 */
public class EventDutyModel {
    private int eventDutyId;
    private String name;
    private String description;
    private Timestamp starttime;
    private Timestamp endtime;
    private String eventType;
    private String eventStatus;
    private String conductor;
    private String location;
    private double defaultPoints;

    //TODO julia
//    private int rehearsalFor;
//    private EventDutyEntity eventDutyByRehearsalFor;

    public EventDutyModel() {
    }

    public EventDutyModel(EventDutyEntity eventDutyEntity) {
        eventDutyId = eventDutyEntity.getEventDutyId();
        name = eventDutyEntity.getName();
        description = eventDutyEntity.getDescription();
        starttime = eventDutyEntity.getStarttime();
        endtime = eventDutyEntity.getEndtime();
        eventType = eventDutyEntity.getEventType();
        eventStatus = eventDutyEntity.getEventStatus();
        conductor = eventDutyEntity.getConductor();
        location = eventDutyEntity.getLocation();
        defaultPoints = eventDutyEntity.getDefaultPoints();
    }

    public EventDutyEntity getEventDutyEntity() {
        EventDutyEntity eventDutyEntity = new EventDutyEntity();
        eventDutyEntity.setEventDutyId(eventDutyId);
        eventDutyEntity.setName(name);
        eventDutyEntity.setDescription(description);
        eventDutyEntity.setStarttime(starttime);
        eventDutyEntity.setEndtime(endtime);
        eventDutyEntity.setEventType(eventType);
        eventDutyEntity.setEventStatus(eventStatus);
        eventDutyEntity.setConductor(conductor);
        eventDutyEntity.setLocation(location);
        eventDutyEntity.setDefaultPoints(defaultPoints);
        //TODO: setRehearsal...
        //TODO: setInstrumentation
        return eventDutyEntity;
    }

    public boolean validate() {
        //TODO julia
        return false;
    }

    public int getEventDutyId() {
        return eventDutyId;
    }

    public void setEventDutyId(int eventDutyId) {
        this.eventDutyId = eventDutyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStarttime() {
        return starttime;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    public Timestamp getEndtime() {
        return endtime;
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getDefaultPoints() {
        return defaultPoints;
    }

    public void setDefaultPoints(double defaultPoints) {
        this.defaultPoints = defaultPoints;
    }
}