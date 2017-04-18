package Presentation.EventSchedule;

import Domain.Entities.EventDutyEntity;
import Domain.Models.EventDutyModel;
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

    private EventDutyModel initEventDutyModel; // remember init data

    @FXML
    public void initialize() {
        Agenda.Appointment appointment = EventScheduleController.getSelectedAppointment();
        name.setText(appointment.getSummary());
        description.setText(appointment.getDescription());
        date.setValue(appointment.getStartLocalDateTime().toLocalDate());
        startTime.setValue(appointment.getStartLocalDateTime().toLocalTime());
        endTime.setValue(appointment.getEndLocalDateTime().toLocalTime());
        eventLocation.setText(appointment.getLocation());

        EventDutyModel eventDutyModel = EventScheduleController.getEventForAppointment(appointment);
        conductor.setText(eventDutyModel.getEventDuty().getConductor());
        points.setText((String.valueOf(eventDutyModel.getEventDuty().getDefaultPoints())));

        initEventDutyModel = eventDutyModel;
    }

    @FXML
    private void save() {
        // TODO: save data
    }

    @FXML
    public boolean discard() {
        // TODO: check with init data for changes
        if(!name.getText().equals(initEventDutyModel.getEventDuty().getName())
                || !description.getText().equals(initEventDutyModel.getEventDuty().getDescription())
                || !date.getValue().equals(initEventDutyModel.getEventDuty().getEndtime().toLocalDateTime().toLocalDate())
                || !startTime.getValue().equals(initEventDutyModel.getEventDuty().getStarttime().toLocalDateTime().toLocalTime())
                || !endTime.getValue().equals(initEventDutyModel.getEventDuty().getEndtime().toLocalDateTime().toLocalTime())
                || !conductor.getText().equals(initEventDutyModel.getEventDuty().getConductor())
                || !eventLocation.getText().equals(initEventDutyModel.getEventDuty().getLocation())
                || Double.parseDouble(points.getText()) != initEventDutyModel.getEventDuty().getDefaultPoints()){
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
