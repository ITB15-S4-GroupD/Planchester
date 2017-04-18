package Presentation.EventSchedule;

import Application.EventSchedule;
import Domain.Entities.EventDutyEntity;
import Domain.Enum.EventStatus;
import Domain.Enum.EventType;
import Domain.Models.EventDutyModel;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jfxtras.scene.control.LocalTimePicker;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private void save() {
        if(name.getText().isEmpty()){
            doAlert("Name missing\n");
            name.requestFocus();
            return;
        }

        if(description.getText().isEmpty()){
            doAlert("Description missing\n");
            description.requestFocus();
            return;
        }


        LocalDate today = LocalDate.now();
        if(date.getValue() == null || date.getValue().isBefore(today) ){
            doAlert("Date wrong\n");
            this.date.requestFocus();
            return;
        }

        LocalTime start = startTime.getValue();
        LocalTime end = endTime.getValue();

        if(start == null || end == null) {
            doAlert("Start or Endtime missing\n");
            return;
        }

        if(start.isAfter(end) || start.equals(end)) {
            doAlert("Endtime is not after starttime\n");
            return;
        }

        if(date.getValue().equals(today) && start.isBefore(LocalTime.now())){
            doAlert("Starttime lies in the past\n");
            return;
        }

        if(eventLocation.getText().isEmpty()){
            doAlert("Location missing\n");
            eventLocation.requestFocus();
            return;
        }

        if(conductor.getText().isEmpty()){
            doAlert("Conductor missing\n");
            conductor.requestFocus();
            return;
        }

        if(points.getText().isEmpty()){
            doAlert("Points missing\n");
            points.requestFocus();
            return;
        }



        // create object
        EventDutyEntity eventDutyEntity = new EventDutyEntity();

        // save text data
        eventDutyEntity.setName(name.getText());
        eventDutyEntity.setDescription(description.getText());
        eventDutyEntity.setLocation(eventLocation.getText());
        eventDutyEntity.setEventType(EventType.Opera.toString());
        eventDutyEntity.setEventStatus(EventStatus.Unpublished.toString());
        eventDutyEntity.setConductor(conductor.getText());

        // save points
        eventDutyEntity.setDefaultPoints(Double.parseDouble(points.getText()));

        // save time
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(date.getValue(), startTime.getValue()));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(date.getValue(), endTime.getValue()));
        eventDutyEntity.setStarttime(startTimestamp);
        eventDutyEntity.setEndtime(endTimestamp);

        // save object
        EventDutyModel eventDuty = new EventDutyModel(eventDutyEntity);
        EventSchedule.createOpera(eventDuty);

        // add event to agenda
        EventScheduleController.addEventDutyToGUI(eventDuty);

        // set agenda view to week of created event
        EventScheduleController.setDisplayedLocalDateTime(eventDuty.getEventDuty().getStarttime().toLocalDateTime());

        // remove content of sidebar
        EventScheduleController.resetSideContent();

        /*
        Alert information = new Alert(Alert.AlertType.INFORMATION);
        information.setHeaderText("Event saved");
        information.setContentText("Event saved");
        information.setResizable(false);
        information.getDialogPane().setPrefSize(350, 500);
        information.showAndWait();
        */
    }

    private void doAlert(String warning){
        Alert problem = new Alert(Alert.AlertType.ERROR);
        problem.setHeaderText("Error");
        problem.setContentText(warning);
        problem.setResizable(true);
        problem.getDialogPane().setPrefSize(350, 200);
        problem.showAndWait();
    }

    @FXML
    public boolean discard() {
        if(!name.getText().isEmpty() || !description.getText().isEmpty() || date.getValue() != null
                || !eventLocation.getText().isEmpty() || !conductor.getText().isEmpty() || !points.getText().isEmpty()) {
            Alert confirmationAlterMessage = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to discard your changes?", ButtonType.YES, ButtonType.NO);
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