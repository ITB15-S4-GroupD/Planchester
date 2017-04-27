package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Utils.DateHelper;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Domain.EventDutyModel;
import Utils.PlanchesterConstants;
import Utils.PlanchesterMessages;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Christina on 20.04.2017.
 */
public class CreateTourController {
    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXDatePicker startDate;
    @FXML private JFXDatePicker endDate;
    @FXML private TextField eventLocation;
    @FXML private ChoiceBox<String> musicalWork;
    @FXML private TextField conductor;
    @FXML private TextField points;

    @FXML
    public void initialize() {
        initializeMandatoryFields();
    }

    @FXML
    private void insertNewTourPerformance() {
        if(validate()) {

            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(Timestamp.valueOf(startDate.getValue().atStartOfDay()));
            eventDutyDTO.setEndTime(Timestamp.valueOf(endDate.getValue().atTime(23,59,59)));
            eventDutyDTO.setEventType(EventType.Tour);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setEventLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(null); //TODO TIMO
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setInstrumentation(null); //TODO TIMO
            eventDutyDTO.setRehearsalFor(null); //TODO TIMO

            EventScheduleManager.createTourPerformance(eventDutyDTO);
            EventScheduleController.addEventDutyToGUI(eventDutyDTO); // add event to agenda
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
            EventScheduleController.setSelectedAppointment(eventDutyDTO); // select created appointment
        }
    }

    @FXML
    public boolean cancel() {
        if(!name.getText().isEmpty() || !description.getText().isEmpty() || startDate.getValue() != null
                || endDate.getValue() != null || !eventLocation.getText().isEmpty() || !conductor.getText().isEmpty() || !points.getText().isEmpty()) {
            Alert confirmationAlterMessage = new Alert(Alert.AlertType.CONFIRMATION, PlanchesterMessages.DISCARD_CHANGES, ButtonType.YES, ButtonType.NO);
            confirmationAlterMessage.showAndWait();

            if (confirmationAlterMessage.getResult() == ButtonType.NO) {
                return false;
            }
        }
        EventScheduleController.resetSideContent();
        return true;
    }

    private boolean validate() {
        LocalDate today = LocalDate.now();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if(name.getText().isEmpty()){
            throwErrorAlertMessage("The Name is missing.");
            name.requestFocus();
            return false;
        } else if(start == null){
            throwErrorAlertMessage("Startdate has to be set.");
            startDate.requestFocus();
            return false;
        } else if(end == null ){
            throwErrorAlertMessage("Enddate has to be set.");
            endDate.requestFocus();
            return false;
        }  else if(start.isAfter(end) || start.equals(end)) {
            throwErrorAlertMessage("Enddate has to be after the startdate.");
            return false;
        }
        //TODO TIMO: validate musiclaWork: is mandatory!
        return true;
    }

    private void throwErrorAlertMessage(String errormessage){
        Alert alert = new Alert(Alert.AlertType.ERROR, errormessage, ButtonType.OK);
        alert.showAndWait();
    }


    private void initializeMandatoryFields() {
        name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        startDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        endDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);

        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(name.getText() == null || name.getText().isEmpty()) {
                    name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                } else {
                    name.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                }
            }
        });
        startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(startDate.getValue() == null) {
                    startDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                } else {
                    startDate.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                }
            }
        });

        endDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(endDate.getValue() == null) {
                    endDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                } else {
                    endDate.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                }
            }
        });
    }
}
