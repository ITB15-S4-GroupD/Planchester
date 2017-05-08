package Presentation.EventSchedule;

import Application.AccountAdministrationManager;
import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Domain.Models.Permission;
import Utils.DateHelper;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.MessageHelper;
import Utils.PlanchesterConstants;
import Utils.PlanchesterMessages;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import jfxtras.scene.control.agenda.Agenda;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Christina on 29.04.2017.
 */
public class EditRehearsalController extends EditController {

    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXTimePicker startTime;
    @FXML private JFXTimePicker endTime;
    @FXML private JFXDatePicker date;
    @FXML private TextField eventLocation;
    @FXML private TextField conductor;
    @FXML private TextField points;

    private EventDutyDTO initEventDutyDTO; // remember init data to compare
    private Agenda.Appointment initAppointment; // remember init data to compare

    @FXML private Button btnSaveEvent;
    @FXML private Button btnCancelEvent;
    @FXML private Button btnEditEvent;
    @FXML private Text txtTitle;

    @Override
    @FXML
    protected void initialize() {
        //TODO GET LIST OF REHEARSALS : Christina
        super.checkMandatoryFields();

        initAppointment = EventScheduleController.getSelectedAppointment();
        initEventDutyDTO = EventScheduleController.getEventForAppointment(initAppointment);

        name.setText(initEventDutyDTO.getName());
        description.setText(initEventDutyDTO.getDescription());
        date.setValue(initEventDutyDTO.getStartTime().toLocalDateTime().toLocalDate());
        startTime.setValue(initEventDutyDTO.getStartTime().toLocalDateTime().toLocalTime());
        endTime.setValue(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalTime());
        eventLocation.setText(initEventDutyDTO.getLocation());
        conductor.setText(initEventDutyDTO.getConductor());
        points.setText(initEventDutyDTO.getPoints() != null ? String.valueOf(initEventDutyDTO.getPoints()) : null);

        initNotEditableFields();

        points.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*[\\,.]?\\d*?")) {
                points.setText(newValue.replaceAll("[^\\d*[\\,.]?\\d*?]", ""));
            }
        });
    }

    protected void initNotEditableFields() {
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
        eventLocation.setEditable(false);
        conductor.setEditable(false);
        points.setEditable(false);
        startTime.setDisable(true);
        endTime.setDisable(true);
        date.setDisable(true);

        name.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        description.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        date.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        startTime.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        endTime.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        eventLocation.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        points.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        conductor.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
    }

    @Override
    @FXML
    protected void save() throws ValidationException {
        if(validate()) {
            Agenda.Appointment selectedAppointment = EventScheduleController.getSelectedAppointment();
            EventDutyDTO oldEventDutyDTO = EventScheduleController.getEventForAppointment(selectedAppointment);
            EventScheduleController.removeSelectedAppointmentFromCalendar(selectedAppointment);

            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setEventDutyId(oldEventDutyDTO.getEventDutyId());
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue()));
            eventDutyDTO.setEndTime(endTime.getValue() == null ? DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue().plusHours(2)) : DateHelper.mergeDateAndTime(date.getValue(), endTime.getValue()));
            eventDutyDTO.setEventType(EventType.Rehearsal);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(null);
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setInstrumentation(null);
            eventDutyDTO.setRehearsalFor(initEventDutyDTO.getRehearsalFor());

            EventScheduleManager.updateEventDuty(eventDutyDTO, initEventDutyDTO);

            EventScheduleController.addEventDutyToGUI(eventDutyDTO);
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
        }
    }

    @Override
    @FXML
    protected boolean cancel() {
        if(points.getText() == null) {
            points.setText("0.0");
        }

        String pointRef = (initEventDutyDTO.getPoints() != null)? String.valueOf(initEventDutyDTO.getPoints()) : "0.0";
        if(!name.getText().equals(initEventDutyDTO.getName())
                || !description.getText().equals(initEventDutyDTO.getDescription())
                || !date.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalDate())
                || !startTime.getValue().equals(initEventDutyDTO.getStartTime().toLocalDateTime().toLocalTime())
                || !endTime.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalTime())
                || !conductor.getText().equals(initEventDutyDTO.getConductor())
                || !eventLocation.getText().equals(initEventDutyDTO.getLocation())
                || !eventLocation.getText().equals(initEventDutyDTO.getLocation())
                || !points.getText().equals(pointRef)
                || (musicalWorks == null && initEventDutyDTO.getMusicalWorks() != null) // musical work removed
                || (musicalWorks != null && initEventDutyDTO.getMusicalWorks() == null) // musical work added
                || (musicalWorks != null && initEventDutyDTO.getMusicalWorks() != null && !musicalWorks.equals(initEventDutyDTO.getMusicalWorks()))){// points changed {

            ButtonType answer = MessageHelper.showConfirmationMessage(PlanchesterMessages.DISCARD_CHANGES);
            if(ButtonType.NO.equals(answer)) {
                return false;
            }
        }
        // remove content of sidebar
        EventScheduleController.resetSideContent();
        EventScheduleController.removeSelection(initAppointment);
        return true;
    }

    @Override
    @FXML
    protected void editEvent () {
        btnCancelEvent.setVisible(true);
        btnSaveEvent.setVisible(true);
        btnEditEvent.setVisible(false);

        name.setEditable(true);
        description.setEditable(true);
        eventLocation.setEditable(true);
        points.setEditable(true);
        conductor.setEditable(true);
        startTime.setDisable(false);
        endTime.setDisable(false);
        date.setDisable(false);

        name.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        description.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        date.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        startTime.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        endTime.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        eventLocation.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        points.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        conductor.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
    }

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
            MessageHelper.showErrorAlertMessage("The endtime is not after the starttime. ");
            return false;
        } else if(date.getValue().equals(today) && start.isBefore(LocalTime.now())){
            MessageHelper.showErrorAlertMessage("The starttime must be in future.");
            date.requestFocus();
            return false;
        }
        return true;
    }
}