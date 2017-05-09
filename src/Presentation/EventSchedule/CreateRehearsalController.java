package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.DateHelper;
import Utils.MessageHelper;
import Utils.PlanchesterConstants;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Christina on 28.04.2017.
 */
public class CreateRehearsalController extends CreateController {

    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXDatePicker date;
    @FXML private JFXTimePicker startTime;
    @FXML private JFXTimePicker endTime;
    @FXML private TextField eventLocation;
    @FXML private TextField conductor;
    @FXML private TextField points;

    public static boolean apply = false;
    public static Stage stage;
    public static EventDutyDTO eventDutyDTO;

    @Override
    @FXML public void initialize() {
        stage = new Stage();
        super.initializeMandatoryFields();
    }

    @FXML
    void applyNewRehearsal() {
        if(validate()) {
            eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue()));
            eventDutyDTO.setEndTime(endTime.getValue() == null ? DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue().plusHours(2)) : DateHelper.mergeDateAndTime(date.getValue(), endTime.getValue()));
            eventDutyDTO.setEventType(EventType.Rehearsal);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setLocation(eventLocation.getText());
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setRehearsalFor(null);
            apply = true;
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    @FXML
    @Override
    public boolean cancel() {
        stage.fireEvent( new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        return true;
    }

    @Override
    protected boolean validate() {
        LocalDate today = LocalDate.now();
        LocalTime start = startTime.getValue();
        LocalTime end = endTime.getValue();

        if(name.getText().isEmpty()){
            MessageHelper.showErrorAlertMessage("The Name is missing.");
            name.requestFocus();
            return false;
        } else if(date.getValue() == null || date.getValue().isBefore(today) ){
            MessageHelper.showErrorAlertMessage("The date is not valid.");
            date.requestFocus();
            return false;
        } else if(start == null) {
            MessageHelper.showErrorAlertMessage("The starttime is missing.");
            return false;
        } else if(end != null && (start.isAfter(end) || start.equals(end))) {
            MessageHelper.showErrorAlertMessage("The endtime is not after the starttime.");
            return false;
        } else if(date.getValue().equals(today) && start.isBefore(LocalTime.now())){
            MessageHelper.showErrorAlertMessage("The starttime must be in future.");
            return false;
        }
        return true;
    }
}