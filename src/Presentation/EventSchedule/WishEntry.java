package Presentation.EventSchedule;

import Utils.Enum.RequestType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by timorzipa on 25/05/2017.
 */
public class WishEntry {
    private final SimpleStringProperty eventType;
    private final SimpleStringProperty eventName;
    private final SimpleStringProperty eventDateTime;
    private final SimpleStringProperty eventLocation;
    private final SimpleStringProperty eventConductor;
    private final SimpleObjectProperty<RequestType> requestType =  new SimpleObjectProperty<RequestType>();

    public WishEntry(String eventType, String eventName, String eventDateTime, String eventLocation, String eventConductor, RequestType r) {
        this.eventType = new SimpleStringProperty(eventType);
        this.eventName = new SimpleStringProperty(eventName);
        this.eventDateTime = new SimpleStringProperty(eventDateTime);
        this.eventLocation = new SimpleStringProperty(eventLocation);
        this.eventConductor = new SimpleStringProperty(eventConductor);
        this.requestType.setValue(r);
    }

    public String getEventType() {
        return eventType.get();
    }

    public SimpleStringProperty eventTypeProperty() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType.set(eventType);
    }

    public String getEventName() {
        return eventName.get();
    }

    public SimpleStringProperty eventNameProperty() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName.set(eventName);
    }

    public String getEventDateTime() {
        return eventDateTime.get();
    }

    public SimpleStringProperty eventDateTimeProperty() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime.set(eventDateTime);
    }

    public String getEventLocation() {
        return eventLocation.get();
    }

    public SimpleStringProperty eventLocationProperty() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation.set(eventLocation);
    }

    public String getEventConductor() {
        return eventConductor.get();
    }

    public SimpleStringProperty eventConductorProperty() {
        return eventConductor;
    }

    public void setEventConductor(String eventConductor) {
        this.eventConductor.set(eventConductor);
    }

    public RequestType getRequestType() {
        return requestType.get();
    }

    public SimpleObjectProperty<RequestType> requestTypeProperty() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType.set(requestType);
    }
}
