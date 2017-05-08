package Presentation.EventSchedule;

import Application.AccountAdministrationManager;
import Application.DTO.EventDutyDTO;
import Domain.Permission;
import Utils.Enum.EventStatus;
import Utils.PlanchesterConstants;
import Utils.PlanchesterMessages;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import jfxtras.scene.control.agenda.Agenda;

/**
 * Created by Christina on 27.04.2017.
 */
public class EditNonMusicalEventController {

    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXDatePicker date;
    @FXML private JFXTimePicker startTime;
    @FXML private JFXTimePicker endTime;
    @FXML private TextField eventLocation;
    @FXML private TextField points;

    @FXML private Button btnCancelEvent;
    @FXML private Button btnSaveEvent;
    @FXML private Button btnEditEvent;
    @FXML private Text txtTitle;

    private EventDutyDTO initEventDutyDTO; // remember init data to compare
    private Agenda.Appointment initAppointment; // remember init data to compare


    @FXML
    public void initialize() {
        checkMandatoryFields();

        Agenda.Appointment appointment = EventScheduleController.getSelectedAppointment();
        EventDutyDTO eventDutyDTO = EventScheduleController.getEventForAppointment(appointment);

        name.setText(appointment.getSummary());
        description.setText(appointment.getDescription());
        date.setValue(appointment.getStartLocalDateTime().toLocalDate());
        startTime.setValue(appointment.getStartLocalDateTime().toLocalTime());
        endTime.setValue(appointment.getEndLocalDateTime().toLocalTime());
        eventLocation.setText(appointment.getLocation());
        points.setText(eventDutyDTO.getPoints() != null ? String.valueOf(eventDutyDTO.getPoints()) : "0.0");

        initNotEditableFields();
        initAppointment = appointment;
        initEventDutyDTO = eventDutyDTO;

        points.textProperty().addListener((observable, oldValue, newValue) -> {
            //^\d*\.\d{2}$
            //"^\\d*[\\.,]?\\d{1,2}?$"

            if (!newValue.matches("\\d*[\\,.]?\\d*?")) {
                points.setText(newValue.replaceAll("[^\\d*[\\,.]?\\d*?]", ""));
            }
        });
    }

    private void initNotEditableFields() {
        Permission permission = AccountAdministrationManager.getInstance().getUserPermissions();
        if(permission.isEditEventSchedule() && EventStatus.Unpublished.equals(initEventDutyDTO.getEventStatus())) {
            btnEditEvent.setVisible(true);
        } else {
            btnEditEvent.setVisible(false);
        }

        btnCancelEvent.setVisible(false);
        btnSaveEvent.setVisible(false);

        name.setEditable(false);
        description.setEditable(false);
        date.setEditable(false);
        startTime.setEditable(false);
        endTime.setEditable(false);
        eventLocation.setEditable(false);
        points.setEditable(false);

        name.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        description.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        date.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        startTime.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        endTime.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        eventLocation.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        points.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
    }

    @FXML
    public void save() {
        //TODO NEXT TIMEBOX
    }

    @FXML
    public boolean cancel() {
        String pointRef = (initEventDutyDTO.getPoints() != null)? String.valueOf(initEventDutyDTO.getPoints()) : "0.0";
        if(!name.getText().equals(initEventDutyDTO.getName())
                || !description.getText().equals(initEventDutyDTO.getDescription())
                || !date.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalDate())
                || !startTime.getValue().equals(initEventDutyDTO.getStartTime().toLocalDateTime().toLocalTime())
                || !points.getText().equals(pointRef)
                || !eventLocation.getText().equals(initEventDutyDTO.getEventLocation())
                || !points.getText().equals(pointRef)) {

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

    public void editEvent () {
        btnCancelEvent.setVisible(true);
        btnSaveEvent.setVisible(true);
        btnEditEvent.setVisible(false);

        name.setEditable(true);
        description.setEditable(true);
        date.setEditable(true);
        startTime.setEditable(true);
        endTime.setEditable(true);
        eventLocation.setEditable(true);
        points.setEditable(true);

        name.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        description.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        date.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        startTime.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        endTime.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        eventLocation.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        points.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
    }

    private void checkMandatoryFields() {
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if(name.getText() == null || name.getText().isEmpty()) {
                name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
            } else {
                name.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
            }

        });
        date.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(date.getValue() == null) {
                date.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
            } else {
                date.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
            }
        });

        startTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(startTime.getValue() == null) {
                startTime.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
            } else {
                startTime.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
            }
        });
    }
}