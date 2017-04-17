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
    }

    @FXML
    private void save() {
        String warning = "";
        boolean validate = true;

        if(name.getText().isEmpty()) {
            validate = false;
            warning = warning + "Name missing\n";
        }

        /*
        if(description.getText().isEmpty()) {
            validate = false;
            warning = warning + "Description missing\n";
        }
        */

        LocalDate date = this.date.getValue();
        if(date == null || !date.isAfter(LocalDate.now())) {
            validate = false;
            warning = warning + "Date wrong\n";
        }

        LocalTime start = startTime.getValue();
        LocalTime end = endTime.getValue();
        if(start.isAfter(end)) {
            validate = false;
            warning = warning + "Endtime ist not after starttime\n";
        }

        if(eventLocation.getText().isEmpty()) {
            validate = false;
            warning = warning + "Location missing\n";
        }

        if(conductor.getText().isEmpty()) {
            validate = false;
            warning = warning + "Conductor missing\n";
        }

        if(points.getText().isEmpty()) {
            validate = false;
            warning = warning + "Points missing\n";
        }

        if(validate) {
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
            Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(date, startTime.getValue()));
            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(date, endTime.getValue()));
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

            Alert information = new Alert(Alert.AlertType.INFORMATION);
            information.setHeaderText("Event saved");
            information.setContentText("Event saved");
            information.setResizable(false);
            information.getDialogPane().setPrefSize(350, 500);
            information.showAndWait();

        }else {
            Alert problem = new Alert(Alert.AlertType.ERROR);
            problem.setHeaderText("Error");
            problem.setContentText(warning);
            problem.setResizable(false);
            problem.getDialogPane().setPrefSize(350, 500);
            problem.showAndWait();
        }
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