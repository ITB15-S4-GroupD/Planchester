package Presentation.EventSchedule;

import Application.EventSchedule;
import Domain.Model.EventDutyEntity;
import Domain.PresentationModels.Enum.EventStatus;
import Domain.PresentationModels.Enum.EventType;
import Domain.PresentationModels.EventDutyDTO;
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
    @FXML private DatePicker date;
    @FXML private LocalTimePicker startTime;
    @FXML private LocalTimePicker endTime;
    @FXML private TextField eventLocation;
    @FXML private ChoiceBox<String> musicalWork;
    @FXML private TextField conductor;
    @FXML private TextField points;


    @FXML
    public void initialize() {

        points.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    points.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //TODO all: fill Works from DB into musicalWork
    }

    @FXML
    private void saveNewOperaDuty(){
        String warning = "";
        boolean validate = true;

        if(name.getText().isEmpty()){
            validate = false;
            warning = warning + "Name missing\n";
        }

        if(description.getText().isEmpty()){
            validate = false;
            warning = warning + "Description missing\n";
        }

        LocalDate date = this.date.getValue();
        if(date == null || !date.isAfter(LocalDate.now())){
            validate = false;
            warning = warning + "Date wrong\n";
        }

        LocalTime start = startTime.getLocalTime();
        LocalTime end = endTime.getLocalTime();
        if(start.isAfter(end))
        {
            validate = false;
            warning = warning + "Endtime ist not after starttime\n";
        }

        if(eventLocation.getText().isEmpty()){
            validate = false;
            warning = warning + "Location missing\n";
        }

        if(conductor.getText().isEmpty()){
            validate = false;
            warning = warning + "Conductor missing\n";
        }

        if(points.getText().isEmpty()){
            validate = false;
            warning = warning + "Points missing\n";
        }

        if(validate){
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
            Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(date, startTime.getLocalTime()));
            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(date, endTime.getLocalTime()));
            eventDutyEntity.setStarttime(startTimestamp);
            eventDutyEntity.setEndtime(endTimestamp);

            // save object
            EventDutyDTO eventDutyDTO = new EventDutyDTO(eventDutyEntity);
            EventSchedule.createOpera(eventDutyDTO);

            // add event to agenda
            EventScheduleController.addEventDuty(eventDutyDTO);

            // set agenda view to week of created event
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getEventDuty().getStarttime().toLocalDateTime());

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setHeaderText("Event saved");
            confirmation.setContentText("Event saved");
            confirmation.setResizable(false);
            confirmation.getDialogPane().setPrefSize(350, 500);
            confirmation.showAndWait();
        }else{
            Alert problem = new Alert(Alert.AlertType.ERROR);
            problem.setHeaderText("Error");
            problem.setContentText(warning);
            problem.setResizable(false);
            problem.getDialogPane().setPrefSize(350, 500);
            problem.showAndWait();
        }
    }
}
