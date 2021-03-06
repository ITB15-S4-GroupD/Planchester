package Presentation.EventSchedule;

import Application.AccountAdministrationManager;
import Application.DTO.EventDutyDTO;
import Application.DTO.MusicalWorkDTO;
import Application.EventScheduleManager;
import Domain.Models.Permission;
import Utils.DateHelper;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.MessageHelper;
import Utils.PlanchesterConstants;
import Utils.PlanchesterMessages;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jfxtras.scene.control.agenda.Agenda;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by Christina on 20.04.2017.
 */
public class EditTourController extends EditController {

    @FXML private JFXDatePicker endDate;

    @FXML
    @Override
    public void initialize() {

        checkMandatoryFields();

        selectedMusicalWorks.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        initAppointment = EventScheduleController.getSelectedAppointment();
        initEventDutyDTO = EventScheduleController.getEventForAppointment(initAppointment);

        actualRehearsalList = EventScheduleManager.getAllRehearsalsOfEventDuty(initEventDutyDTO);
        newRehearsalList = actualRehearsalList;
        rehearsalTableView.getItems().clear();
        rehearsalTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        for(EventDutyDTO e : newRehearsalList) {
            String rehearsalToAdd = e.getName();
            rehearsalTableView.getItems().add(rehearsalToAdd);
        }

        name.setText(initEventDutyDTO.getName());
        description.setText(initEventDutyDTO.getDescription());
        date.setValue(initEventDutyDTO.getStartTime().toLocalDateTime().toLocalDate());
        endDate.setValue(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalDate());
        eventLocation.setText(initEventDutyDTO.getLocation());
        conductor.setText(initEventDutyDTO.getConductor());
        points.setText(initEventDutyDTO.getPoints() != null ? String.valueOf(initEventDutyDTO.getPoints()) : "0.0");
        if(initEventDutyDTO.getMusicalWorks() != null && !initEventDutyDTO.getMusicalWorks().isEmpty()) {
            musicalWorks = new ArrayList<>();
            for(MusicalWorkDTO musicalWorkDTO : initEventDutyDTO.getMusicalWorks()) {
                musicalWorks.add(musicalWorkDTO);
                musicalWorkTable.getItems().add(musicalWorkDTO.getName());
            }
        }

        initNotEditableFields();

        points.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*[\\,.]?\\d*?")) {
                points.setText(newValue.replaceAll("[^\\d*[\\,.]?\\d*?]", ""));
            }
        });
    }

    @Override
    protected void initNotEditableFields() {
        Permission permission = AccountAdministrationManager.getInstance().getUserPermissions();
        if(permission.isEditEventSchedule() && EventStatus.Unpublished.equals(initEventDutyDTO.getEventStatus())) {
            btnEditEvent.setVisible(true);
        } else {
            btnEditEvent.setVisible(false);
        }

        btnCancelEvent.setVisible(false);
        btnSaveEvent.setVisible(false);
        btnEditDetails.setVisible(false);
        btnAddRehearsal.setVisible(false);
        btnRemoveRehearsal.setVisible(false);

        name.setEditable(false);
        description.setEditable(false);
        date.setDisable(true);
        endDate.setDisable(true);
        eventLocation.setEditable(false);
        conductor.setEditable(false);
        points.setEditable(false);

        name.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        description.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        date.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        endDate.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        eventLocation.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        points.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        conductor.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
    }

    @FXML
    @Override
    protected void save() throws ValidationException {
        if(validate()) {
            Agenda.Appointment selectedAppointment = EventScheduleController.getSelectedAppointment();
            EventDutyDTO oldEventDutyDTO = EventScheduleController.getEventForAppointment(selectedAppointment);
            EventScheduleController.removeSelectedAppointmentFromCalendar(selectedAppointment);

            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setEventDutyId(oldEventDutyDTO.getEventDutyId());
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(DateHelper.mergeDateAndTime(date.getValue(), LocalTime.MIDNIGHT));
            eventDutyDTO.setEndTime(DateHelper.mergeDateAndTime(endDate.getValue(), LocalTime.MAX));
            eventDutyDTO.setEventType(EventType.Tour);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(musicalWorks);
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setInstrumentation(null);
            eventDutyDTO.setRehearsalFor(null);
          
            EventScheduleManager.updateEventDuty(eventDutyDTO, initEventDutyDTO);
            EventScheduleController.addEventDutyToGUI(eventDutyDTO);
            updateRehearsal(eventDutyDTO);
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
        }
    }

    @FXML
    @Override
    protected boolean cancel() {
        String pointRef = (initEventDutyDTO.getPoints() != null)? String.valueOf(initEventDutyDTO.getPoints()) : "0.0";
        if(!name.getText().equals(initEventDutyDTO.getName())
                || !description.getText().equals(initEventDutyDTO.getDescription())
                || !date.getValue().equals(initEventDutyDTO.getStartTime().toLocalDateTime().toLocalDate())
                || !endDate.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalDate())
                || !conductor.getText().equals(initEventDutyDTO.getConductor())
                || !eventLocation.getText().equals(initEventDutyDTO.getLocation())
                || !points.getText().equals(pointRef)
                || (musicalWorks == null && initEventDutyDTO.getMusicalWorks() != null) // musical work removed
                || (musicalWorks != null && initEventDutyDTO.getMusicalWorks() == null) // musical work added
                || (musicalWorks != null && initEventDutyDTO.getMusicalWorks() != null && !musicalWorks.equals(initEventDutyDTO.getMusicalWorks()))) { // musical work changed

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
    protected void checkMandatoryFields() {
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            if (name.getText() == null || name.getText().isEmpty()) {
                name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
            } else {
                name.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
            }

        });

        date.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (date.getValue() == null) {
                date.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
            } else {
                date.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
            }
        });

        endDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (endDate.getValue() == null) {
                endDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
            } else {
                endDate.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
            }
        });
    }

    @Override
    protected boolean validate() {
        LocalDate today = LocalDate.now();

        if(name.getText().isEmpty()){
            MessageHelper.showErrorAlertMessage("The Name is missing.");
            name.requestFocus();
            return false;
        } else if(date.getValue() == null || date.getValue().isBefore(today) ){
            MessageHelper.showErrorAlertMessage("The startdate is not valid.");
            date.requestFocus();
            return false;
        } else if(endDate.getValue().isBefore(date.getValue())){
            MessageHelper.showErrorAlertMessage("The startdate must be in befor the enddate. \n");
            return false;
        } else if(musicalWorks == null || musicalWorks.isEmpty()){
            MessageHelper.showErrorAlertMessage("A musical work has to be selected.");
            return false;
        }
        return true;
    }

    @FXML
    @Override
    protected void editEvent() {
        btnCancelEvent.setVisible(true);
        btnSaveEvent.setVisible(true);
        btnEditEvent.setVisible(false);
        btnEditDetails.setVisible(true);
        btnAddRehearsal.setVisible(true);
        btnRemoveRehearsal.setVisible(true);

        name.setEditable(true);
        description.setEditable(true);
        date.setDisable(false);
        endDate.setDisable(false);
        eventLocation.setEditable(true);
        conductor.setEditable(true);
        points.setEditable(true);
        conductor.setEditable(true);

        name.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        description.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        date.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        endDate.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        eventLocation.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        points.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
        conductor.setStyle(PlanchesterConstants.INPUTFIELD_VALID);;
    }
}