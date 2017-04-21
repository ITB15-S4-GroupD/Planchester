package Presentation.EventSchedule;

import Application.EventSchedule;
import Domain.Enum.EventStatus;
import Domain.Enum.EventType;
import Domain.Models.EventDutyModel;
import Persistence.EventDuty;
import Utils.DateHelper;
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
 * Created by Christina on 20.04.2017.
 */
public class CreateConcertController {

    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXTimePicker startTime;
    @FXML private JFXTimePicker endTime;
    @FXML private JFXDatePicker date;
    @FXML private TextField eventLocation;
    @FXML private ChoiceBox<String> musicalWork;
    @FXML private TextField conductor;
    @FXML private TextField points;

    @FXML
    public void initialize() {
        name.setStyle("-fx-control-inner-background: #ffdec9");
        date.setStyle("-fx-control-inner-background: #ffdec9");
        startTime.setStyle("-fx-control-inner-background: #ffdec9");
        checkRequiredFields();
    }

    @FXML
    private void save() {

        // create object

        EventDutyModel eventDutyModel = new EventDutyModel();

        eventDutyModel.setEventType(EventType.Concert.toString());
        eventDutyModel.setName(name.getText());
        eventDutyModel.setDescription(description.getText());
        eventDutyModel.setStarttime(DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue()));
        eventDutyModel.setEndtime(DateHelper.mergeDateAndTime(date.getValue(), endTime.getValue()));
        eventDutyModel.setLocation(eventLocation.getText());
        //TODO ina musical work
        eventDutyModel.setConductor(conductor.getText());
        eventDutyModel.setDefaultPoints(Double.parseDouble(points.getText()));
        eventDutyModel.setEventStatus(EventStatus.Unpublished.toString());

        EventSchedule.insertConcertEventDuty(eventDutyModel);

        EventScheduleController.addEventDutyToGUI(eventDutyModel); // add event to agenda
        EventScheduleController.setDisplayedLocalDateTime(eventDutyModel.getStarttime().toLocalDateTime()); // set agenda view to week of created event
        EventScheduleController.resetSideContent(); // remove content of sidebar
        EventScheduleController.setSelectedAppointment(eventDutyModel); // select created appointment

        EventDuty.insertEventDuty(eventDutyModel);
    }

    @FXML
    public boolean discard() {
        //TODO implement musical works
        if(!name.getText().isEmpty() || !description.getText().isEmpty() || date.getValue() != null
                || !eventLocation.getText().isEmpty() || !conductor.getText().isEmpty() || !points.getText().isEmpty()) {
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

    private void validateGUI() {
        //TODO implement musical work
        LocalDate today = LocalDate.now();
        LocalTime start = startTime.getValue();
        LocalTime end = endTime.getValue();

        if(name.getText().isEmpty()){
            throwErrorAlertMessage("The Name is missing.");
            name.requestFocus();
        } else if(description.getText().isEmpty()){
            throwErrorAlertMessage("The description is missing.");
            description.requestFocus();
        } else if(date.getValue() == null || date.getValue().isBefore(today) ){
            throwErrorAlertMessage("The date is not valid.");
            date.requestFocus();
        } else if(start == null) {
            throwErrorAlertMessage("The starttime is missing.");
        } else if(end == null) {
            throwErrorAlertMessage("The endtime is missing.");
        } else if(start.isAfter(end) || start.equals(end)) {
            throwErrorAlertMessage("The endtime is not after the starttime. ");
        } else if(date.getValue().equals(today) && start.isBefore(LocalTime.now())){
            throwErrorAlertMessage("The starttime must be in future. \n");
        } else if(eventLocation.getText().isEmpty()){
            throwErrorAlertMessage("The location is missing.");
            eventLocation.requestFocus();
        } else if(conductor.getText().isEmpty()){
            throwErrorAlertMessage("The conductor is missing.");
            conductor.requestFocus();
        } else if(points.getText().isEmpty()){
            throwErrorAlertMessage("The points are missing.");
            points.requestFocus();
        }
    }

    private void throwErrorAlertMessage(String errormessage){
        Alert alert = new Alert(Alert.AlertType.ERROR, errormessage, ButtonType.OK);
        alert.showAndWait();
    }


    private void checkRequiredFields() {
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(name.getText().equals("") || name.getText() == null) {
                    name.setStyle("-fx-control-inner-background: #ffdec9");
                } else {
                    name.setStyle("-fx-control-inner-background: #ffffff");
                }

            }
        });
        date.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(date.getValue() == null) {
                    date.setStyle("-fx-control-inner-background: #ffdec9");
                } else {
                    date.setStyle("-fx-control-inner-background: #ffffff");
                }
            }
        });

        startTime.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                if(startTime.getValue() == null) {
                    startTime.setStyle("-fx-control-inner-background: #ffdec9");
                } else {
                    startTime.setStyle("-fx-control-inner-background: #ffffff");
                }
            }
        });
    }
}
