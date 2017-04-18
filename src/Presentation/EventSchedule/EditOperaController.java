package Presentation.EventSchedule;

import Domain.Entities.EventDutyEntity;
import Domain.Models.EventDutyModel;
import Utils.PlanchesterMessages;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jfxtras.scene.control.agenda.Agenda;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EditOperaController {

    @FXML private TextField name;
    @FXML private TextArea description;

    @FXML private JFXTimePicker startTime;
    @FXML private JFXTimePicker endTime;
    @FXML private JFXDatePicker date;

    @FXML private TextField eventLocation;
    @FXML private ChoiceBox<String> musicalWork;
    @FXML private TextField conductor;
    @FXML private TextField points;

    private EventDutyModel initEventDutyModel; // remember init data to compare
    private Agenda.Appointment initAppointment; // remember init data to compare

    @FXML
    public void initialize() {
        Agenda.Appointment appointment = EventScheduleController.getSelectedAppointment();
        EventDutyModel eventDutyModel = EventScheduleController.getEventForAppointment(appointment);

        name.setText(appointment.getSummary());
        description.setText(appointment.getDescription());
        date.setValue(appointment.getStartLocalDateTime().toLocalDate());
        startTime.setValue(appointment.getStartLocalDateTime().toLocalTime());
        endTime.setValue(appointment.getEndLocalDateTime().toLocalTime());
        eventLocation.setText(appointment.getLocation());
        conductor.setText(eventDutyModel.getConductor());
        points.setText((String.valueOf(eventDutyModel.getDefaultPoints())));

        initAppointment = appointment;
        initEventDutyModel = eventDutyModel;
    }

    @FXML
    private void save() {
        // TODO: save data
    }

    @FXML
    public boolean discard() {
        // TODO: check with init data for changes
        if(!name.getText().equals(initEventDutyModel.getName())
                || !description.getText().equals(initEventDutyModel.getDescription())
                || !date.getValue().equals(initEventDutyModel.getEndtime().toLocalDateTime().toLocalDate())
                || !startTime.getValue().equals(initEventDutyModel.getStarttime().toLocalDateTime().toLocalTime())
                || !endTime.getValue().equals(initEventDutyModel.getEndtime().toLocalDateTime().toLocalTime())
                || !conductor.getText().equals(initEventDutyModel.getConductor())
                || !eventLocation.getText().equals(initEventDutyModel.getLocation())
                || Double.parseDouble(points.getText()) != initEventDutyModel.getDefaultPoints()) {

            Alert confirmationAlertMessage = new Alert(Alert.AlertType.CONFIRMATION, PlanchesterMessages.DISCARD_CHANGES, ButtonType.YES, ButtonType.NO);
            confirmationAlertMessage.showAndWait();

            if (confirmationAlertMessage.getResult() == ButtonType.NO) {
                return false;
            }
        }
        // remove content of sidebar
        EventScheduleController.resetSideContent();
        EventScheduleController.removeSelection(initAppointment);
        return true;
    }
}
