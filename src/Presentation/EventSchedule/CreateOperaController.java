package Presentation.EventSchedule;

import Application.EventSchedule;
import Domain.Enum.EventStatus;
import Domain.Enum.EventType;
import Domain.Models.EventDutyModel;
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
 * Created by Ina on 08.04.2017.
 */
public class CreateOperaController {
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
        //TODO all: fill Works from DB into musicalWork

        points.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    points.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    private void insertNewOperaPerformance() {
        prevalidateGUI();

        // create object
        EventDutyModel eventDutyModel = new EventDutyModel();
        eventDutyModel.setName(name.getText());
        eventDutyModel.setDescription(description.getText());
        eventDutyModel.setLocation(eventLocation.getText());
        eventDutyModel.setEventType(EventType.Opera.toString());
        eventDutyModel.setEventStatus(EventStatus.Unpublished.toString());
        eventDutyModel.setConductor(conductor.getText());
        eventDutyModel.setDefaultPoints(Double.parseDouble(points.getText()));
        eventDutyModel.setStarttime(DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue()));
        eventDutyModel.setEndtime(DateHelper.mergeDateAndTime(date.getValue(), endTime.getValue()));

        EventSchedule.insertNewOperaPerformance(eventDutyModel);

        EventScheduleController.addEventDutyToGUI(eventDutyModel); // add event to agenda
        EventScheduleController.setDisplayedLocalDateTime(eventDutyModel.getStarttime().toLocalDateTime()); // set agenda view to week of created event
        EventScheduleController.resetSideContent(); // remove content of sidebar
        EventScheduleController.setSelectedAppointment(eventDutyModel); // select created appointment
    }

    private void prevalidateGUI() {
        LocalDate today = LocalDate.now();
        LocalTime start = startTime.getValue();
        LocalTime end = endTime.getValue();

        if(name.getText().isEmpty()){
            throwErrorAlertMessage("The Name is missing.");
            name.requestFocus();
        } else if(date.getValue() == null || date.getValue().isBefore(today) ){
            throwErrorAlertMessage("The date is not valid.");
            date.requestFocus();
        } else if(start == null) {
            throwErrorAlertMessage("The starttime is missing.");
        } else if(end != null && (start.isAfter(end) || start.equals(end))) {
            throwErrorAlertMessage("The endtime is not after the starttime. ");
        } else if(date.getValue().equals(today) && start.isBefore(LocalTime.now())){
            throwErrorAlertMessage("The starttime must be in future. \n");
        }
    }

    private void throwErrorAlertMessage(String errormessage){
        Alert alert = new Alert(Alert.AlertType.ERROR, errormessage, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    public boolean discard() {
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
}