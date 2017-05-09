package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Application.DTO.InstrumentationDTO;
import Application.DTO.MusicalWorkDTO;
import Application.EventScheduleManager;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.MessageHelper;
import Utils.PlanchesterConstants;
import Utils.PlanchesterMessages;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christina on 20.04.2017.
 */
public class CreateTourController extends CreateController {
    @FXML private JFXDatePicker endDate;

    @FXML
    @Override
    protected void insertEventDuty() throws ValidationException {
        if (validate()) {
            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(Timestamp.valueOf(date.getValue().atStartOfDay()));
            eventDutyDTO.setEndTime(Timestamp.valueOf(endDate.getValue().atTime(23, 59, 59)));
            eventDutyDTO.setEventType(EventType.Tour);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(musicalWorks);
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setInstrumentation(null); //TODO timebox 2
            eventDutyDTO.setRehearsalFor(null);

            eventDutyDTO = EventScheduleManager.createEventDuty(eventDutyDTO);
            EventScheduleController.addEventDutyToGUI(eventDutyDTO); // add event to agenda

            for(EventDutyDTO eventD : rehearsalList){
                eventD.setRehearsalFor(eventDutyDTO.getEventDutyId());
                EventScheduleManager.createEventDuty(eventD);
                EventScheduleController.addEventDutyToGUI(eventD);
            }

            rehearsalList.clear();
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
            EventScheduleController.setSelectedAppointment(eventDutyDTO); // select created appointment
        }
    }

    @FXML
    @Override
    public boolean cancel() {
        if (!name.getText().isEmpty() || !description.getText().isEmpty() || date.getValue() != null
                || endDate.getValue() != null || !eventLocation.getText().isEmpty() || !conductor.getText().isEmpty()
                || !points.getText().isEmpty() || (musicalWorks != null && !musicalWorks.isEmpty())) {

            ButtonType answer = MessageHelper.showConfirmationMessage(PlanchesterMessages.DISCARD_CHANGES);
            if (ButtonType.NO.equals(answer)) {
                return false;
            }
        }
        EventScheduleController.resetSideContent();
        return true;
    }

    @Override
    protected void initializeMandatoryFields() {
        name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        date.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        endDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);

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

        endDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(endDate.getValue() == null) {
                endDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
            } else {
                endDate.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
            }
        });
    }

    @Override
    protected boolean validate() {
        LocalDate start = date.getValue();
        LocalDate end = endDate.getValue();

        if (name.getText().isEmpty()) {
            MessageHelper.showErrorAlertMessage("The Name is missing.");
            name.requestFocus();
            return false;
        } else if (start == null) {
            MessageHelper.showErrorAlertMessage("Startdate has to be set.");
            date.requestFocus();
            return false;
        } else if (end == null) {
            MessageHelper.showErrorAlertMessage("Enddate has to be set.");
            endDate.requestFocus();
            return false;
        } else if (start.isAfter(end) || start.equals(end)) {
            MessageHelper.showErrorAlertMessage("Enddate has to be after the startdate.");
            return false;
        } else if (musicalWorks == null || musicalWorks.isEmpty()) {
            MessageHelper.showErrorAlertMessage("A musical work has to be selected.");
            return false;
        }
        return true;
    }
}