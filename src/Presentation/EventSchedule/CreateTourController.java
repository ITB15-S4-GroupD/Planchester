package Presentation.EventSchedule;

import Application.EventSchedule;
import Domain.Enum.EventStatus;
import Domain.Enum.EventType;
import Domain.Models.EventDutyModel;
import Utils.PlanchesterMessages;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Timestamp;
import java.time.LocalDate;

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
        name.setStyle("-fx-control-inner-background: #ffdec9");
        startDate.setStyle("-fx-control-inner-background: #ffdec9");
        endDate.setStyle("-fx-control-inner-background: #ffdec9");
        checkRequiredFields();
    }

    @FXML
    private void insertNewTourPerformance() {
        if(!prevalidateGUI())return;

        // create object
        EventDutyModel eventDutyModel = new EventDutyModel();
        eventDutyModel.setName(name.getText());
        eventDutyModel.setDescription(description.getText());
        eventDutyModel.setStarttime(Timestamp.valueOf(startDate.getValue().atStartOfDay()));
        eventDutyModel.setEndtime( Timestamp.valueOf(endDate.getValue().atStartOfDay()));
        eventDutyModel.setEventType(EventType.Tour.toString());
        eventDutyModel.setEventStatus(EventStatus.Unpublished.toString());
        eventDutyModel.setConductor(conductor.getText());
        eventDutyModel.setLocation(eventLocation.getText());
        if( !points.getText().isEmpty() ) eventDutyModel.setDefaultPoints(Double.parseDouble(points.getText()));
        //TODO eventDutyByRehearsalFor and instrumentation

        EventSchedule.insertTourEventDuty(eventDutyModel);
    }

    @FXML
    public boolean discard() {
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

    private boolean prevalidateGUI() {
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
        } else if(start.isAfter(end) || start.equals(end)) {
            throwErrorAlertMessage("Endtime has to be after the starttime. ");
            return false;
        }
        return true;
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
        startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(startDate.getValue() == null) {
                    startDate.setStyle("-fx-control-inner-background: #ffdec9");
                } else {
                    startDate.setStyle("-fx-control-inner-background: #ffffff");
                }
            }
        });

        endDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(endDate.getValue() == null) {
                    endDate.setStyle("-fx-control-inner-background: #ffdec9");
                } else {
                    endDate.setStyle("-fx-control-inner-background: #ffffff");
                }
            }
        });
    }
}
