package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.DateHelper;
import Utils.PlanchesterConstants;
import Utils.PlanchesterMessages;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Christina on 27.04.2017.
 */
public class CreateNonMusicalEventController {

    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXDatePicker date;
    @FXML private JFXTimePicker startTime;
    @FXML private JFXTimePicker endTime;
    @FXML private TextField eventLocation;
    @FXML private TextField points;
    @FXML private Button btnCancel;
    @FXML private Button btnSaveNewConcert;



    @FXML public void initialize() {
        initializeMandatoryFields();

        points.textProperty().addListener((observable, oldValue, newValue) -> {
            //^\d*\.\d{2}$
            //"^\\d*[\\.,]?\\d{1,2}?$"

            if (!newValue.matches("\\d*[\\,.]?\\d*?")) {
                points.setText(newValue.replaceAll("[^\\d*[\\,.]?\\d*?]", ""));
            }
        });
    }

    @FXML public boolean cancel() {
        if(!name.getText().isEmpty() || !description.getText().isEmpty() || date.getValue() != null
                || !eventLocation.getText().isEmpty() || !points.getText().isEmpty()) {
            Alert confirmationAlterMessage = new Alert(Alert.AlertType.CONFIRMATION, PlanchesterMessages.DISCARD_CHANGES, ButtonType.YES, ButtonType.NO);
            confirmationAlterMessage.showAndWait();

            if (confirmationAlterMessage.getResult() == ButtonType.NO) {
                return false;
            }
        }
        // remove content of sidebar
        EventScheduleController.resetSideContent();
        return true;

    }

    @FXML
    public void insertNewConcertPerformance() {
        if(validate()) {
            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue()));
            eventDutyDTO.setEndTime(endTime.getValue() == null ? DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue().plusHours(2)) : DateHelper.mergeDateAndTime(date.getValue(), endTime.getValue()));
            eventDutyDTO.setEventType(EventType.NonMusicalEvent);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setEventLocation(eventLocation.getText());
            eventDutyDTO.setPoints((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText()));

            eventDutyDTO = EventScheduleManager.createNonMusicalPerformance(eventDutyDTO);
            EventScheduleController.addEventDutyToGUI(eventDutyDTO); // add event to agenda
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
            EventScheduleController.setSelectedAppointment(eventDutyDTO); // select created appointment
        }
    }

    private boolean validate() {
        LocalDate today = LocalDate.now();
        LocalTime start = startTime.getValue();
        LocalTime end = endTime.getValue();

        if(name.getText().isEmpty()){
            throwErrorAlertMessage("The Name is missing.");
            name.requestFocus();
            return false;
        } else if(date.getValue() == null || date.getValue().isBefore(today) ){
            throwErrorAlertMessage("The date is not valid.");
            date.requestFocus();
            return false;
        } else if(start == null) {
            throwErrorAlertMessage("The starttime is missing.");
            return false;
        } else if(end != null && (start.isAfter(end) || start.equals(end))) {
            throwErrorAlertMessage("The endtime is not after the starttime. ");
            return false;
        } else if(date.getValue().equals(today) && start.isBefore(LocalTime.now())){
            throwErrorAlertMessage("The starttime must be in future. \n");
            return false;
        }

        return true;
    }

    private void throwErrorAlertMessage(String errormessage){
        Alert alert = new Alert(Alert.AlertType.ERROR, errormessage, ButtonType.OK);
        alert.showAndWait();
    }

    private void initializeMandatoryFields() {
        name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        date.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        startTime.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);

        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if(name.getText() == null || name.getText().isEmpty()) {
                name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
            } else {
                name.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
            }
        });
        date.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(date.getValue() == null) {
                date.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
            } else {
                date.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
            }
        });

        startTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(startTime.getValue() == null) {
                startTime.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
            } else {
                startTime.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
            }
        });
    }
}