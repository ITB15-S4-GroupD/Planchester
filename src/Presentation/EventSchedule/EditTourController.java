package Presentation.EventSchedule;

import Application.AccountAdministrationManager;
import Application.DTO.EventDutyDTO;
import Application.DTO.InstrumentationDTO;
import Application.DTO.MusicalWorkDTO;
import Application.EventScheduleManager;
import Utils.DateHelper;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.MessageHelper;
import Utils.PlanchesterConstants;
import Utils.PlanchesterMessages;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxtras.scene.control.agenda.Agenda;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christina on 20.04.2017.
 */
public class EditTourController extends EditController {

    @FXML private JFXDatePicker endDate;
  
    @Override
    @FXML
    protected void insertEventDuty() throws ValidationException {
        if(validate()) {
            Agenda.Appointment selectedAppointment = EventScheduleController.getSelectedAppointment();
            EventDutyDTO oldEventDutyDTO = EventScheduleController.getEventForAppointment(selectedAppointment);
            EventScheduleController.removeSelectedAppointmentFromCalendar(selectedAppointment);

            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setEventDutyID(oldEventDutyDTO.getEventDutyID());
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(name.getText());
            eventDutyDTO.setStartTime(DateHelper.mergeDateAndTime(date.getValue(), LocalTime.MIDNIGHT));
            eventDutyDTO.setEndTime(DateHelper.mergeDateAndTime(endDate.getValue(), LocalTime.MAX));
            eventDutyDTO.setEventType(EventType.Tour);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setEventLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(musicalWorks);
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setInstrumentation(null); //TODO timebox 2
            eventDutyDTO.setRehearsalFor(null);
          
            EventScheduleManager.updateEventDuty(eventDutyDTO, initEventDutyDTO);

            EventScheduleController.addEventDutyToGUI(eventDutyDTO);
            //Add added Rehearsal to EventDuty
            updateRehearsal(eventDutyDTO);

            EventScheduleController.addEventDutyToGUI(eventDutyDTO);
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
        }
    }

    @Override
    @FXML
    protected boolean cancel() {
        String pointRef = (initEventDutyDTO.getPoints() != null)? String.valueOf(initEventDutyDTO.getPoints()) : "0.0";
        if(!name.getText().equals(initEventDutyDTO.getName())
                || !description.getText().equals(initEventDutyDTO.getDescription())
                || !date.getValue().equals(initEventDutyDTO.getStartTime().toLocalDateTime().toLocalDate())
                || !endDate.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalDate())
                || !conductor.getText().equals(initEventDutyDTO.getConductor())
                || !eventLocation.getText().equals(initEventDutyDTO.getEventLocation())
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
    protected void initializeMandatoryFields() {
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
}