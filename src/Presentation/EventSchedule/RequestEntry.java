package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Utils.Enum.RequestType;
import Utils.Enum.RequestTypeGUI;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * Created by timorzipa on 25/05/2017.
 */
public class RequestEntry {
    private final SimpleStringProperty eventType;
    private final SimpleStringProperty eventName;
    private final SimpleStringProperty eventDateTime;
    private final SimpleStringProperty eventLocation;
    private final SimpleStringProperty eventConductor;
    private final SimpleObjectProperty<RequestTypeGUI> requestType =  new SimpleObjectProperty<>();
    private TextField requestDescription = new TextField();
    private boolean gray;
    EventDutyDTO eventDutyDTO;
    boolean edited;

    public RequestEntry(String eventType, String eventName, String eventDateTime, String eventLocation, String eventConductor, RequestTypeGUI r, String requestDescription, EventDutyDTO eventDutyDTO) {
        this.eventType = new SimpleStringProperty(eventType);
        this.eventName = new SimpleStringProperty(eventName);
        this.eventDateTime = new SimpleStringProperty(eventDateTime);
        this.eventLocation = new SimpleStringProperty(eventLocation);
        this.eventConductor = new SimpleStringProperty(eventConductor);
        this.requestDescription.setText(requestDescription);
        setRequestType(r);
        this.eventDutyDTO = eventDutyDTO;

        this.requestDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            this.setEdited(true);
        });
    }

    public boolean isGray() {
        return gray;
    }

    public void setGray(boolean gray) {
        this.gray = gray;
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

    public RequestTypeGUI getRequestType() {
        return requestType.get();
    }

    public SimpleObjectProperty<RequestTypeGUI> requestTypeProperty() {
        return requestType;
    }

    public void setRequestType(RequestTypeGUI requestType) {
        this.requestType.set(requestType);
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public EventDutyDTO getEventDutyDTO() {
        return eventDutyDTO;
    }

    public void setEventDutyDTO(EventDutyDTO eventDutyDTO) {
        this.eventDutyDTO = eventDutyDTO;
    }

    public TextField getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(TextField requestDescription) {
        this.requestDescription = requestDescription;
    }
}
