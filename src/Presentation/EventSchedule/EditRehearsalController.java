package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
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

    @Override
    @FXML
    public void initialize() {
        //TODO GET LIST OF REHEARSALS : Christina
        super.checkMandatoryFields();

        Agenda.Appointment appointment = EventScheduleController.getSelectedAppointment();
        EventDutyDTO eventDutyDTO = EventScheduleController.getEventForAppointment(appointment);

        name.setText(appointment.getSummary());
        description.setText(appointment.getDescription());
        date.setValue(appointment.getStartLocalDateTime().toLocalDate());
        startTime.setValue(appointment.getStartLocalDateTime().toLocalTime());
        endTime.setValue(appointment.getEndLocalDateTime().toLocalTime());
        eventLocation.setText(appointment.getLocation());
        conductor.setText(eventDutyDTO.getConductor());
        points.setText(eventDutyDTO.getPoints() != null ? String.valueOf(eventDutyDTO.getPoints()) : null);

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
        btnEditEvent.setVisible(true);
        btnCancelEvent.setVisible(false);
        btnSaveEvent.setVisible(false);

        name.setEditable(false);
        description.setEditable(false);
        date.setEditable(false);
        startTime.setEditable(false);
        endTime.setEditable(false);
        eventLocation.setEditable(false);
        conductor.setEditable(false);
        points.setEditable(false);


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
    public void save() throws ValidationException {
        if(validate()) {
            Agenda.Appointment selectedAppointment = EventScheduleController.getSelectedAppointment();
            EventDutyDTO oldEventDutyDTO = EventScheduleController.getEventForAppointment(selectedAppointment);
            EventScheduleController.removeSelectedAppointmentFromCalendar(selectedAppointment);

            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setEventDutyID(oldEventDutyDTO.getEventDutyID());
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue()));
            eventDutyDTO.setEndTime(endTime.getValue() == null ? DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue().plusHours(2)) : DateHelper.mergeDateAndTime(date.getValue(), endTime.getValue()));
            eventDutyDTO.setEventType(EventType.Rehearsal);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setEventLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(null);
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setInstrumentation(null);
            eventDutyDTO.setRehearsalFor(null);

            EventScheduleManager.updateEventDuty(eventDutyDTO, initEventDutyDTO);

            EventScheduleController.addEventDutyToGUI(eventDutyDTO);
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
        }
    }

    @Override
    @FXML
    public boolean cancel() {
        if(!name.getText().equals(initEventDutyDTO.getName())
                || !description.getText().equals(initEventDutyDTO.getDescription())
                || !date.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalDate())
                || !startTime.getValue().equals(initEventDutyDTO.getStartTime().toLocalDateTime().toLocalTime())
                || !endTime.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalTime())
                || !conductor.getText().equals(initEventDutyDTO.getConductor())
                || !eventLocation.getText().equals(initEventDutyDTO.getEventLocation())
                || (initEventDutyDTO.getPoints() == null && !points.getText().isEmpty()) // points added
                || (initEventDutyDTO.getPoints() != null && points.getText().isEmpty()) // points removed
                || (initEventDutyDTO.getPoints() != null && !Double.valueOf(points.getText()).equals(initEventDutyDTO.getPoints()))){// points changed {

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
        conductor.setEditable(true);

        name.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        description.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        date.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        startTime.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        endTime.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        eventLocation.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        points.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        conductor.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
    }

    private boolean validate() {
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