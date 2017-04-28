package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Application.DTO.InstrumentationDTO;
import Application.DTO.MusicalWorkDTO;
import Application.EventScheduleManager;
import Domain.EventDutyModel;
import Utils.DateHelper;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.MessageHelper;
import Utils.PlanchesterConstants;
import Utils.PlanchesterMessages;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
public class EditHofkapelleController {

        @FXML private TextField name;
        @FXML private TextArea description;
        @FXML private JFXTimePicker startTime;
        @FXML private JFXTimePicker endTime;
        @FXML private JFXDatePicker date;
        @FXML private TextField eventLocation;
        @FXML private TextField conductor;
        @FXML private TextField points;

        @FXML private TableView<String> musicalWorkTable;
        @FXML private TableColumn<String, String> selectedMusicalWorks;

        private List<MusicalWorkDTO> musicalWorks;
        private InstrumentationDTO instrumentation; // TODO timebox2


        private EventDutyDTO initEventDutyDTO; // remember init data to compare
        private Agenda.Appointment initAppointment; // remember init data to compare

        @FXML
        public void initialize() {
                checkMandatoryFields();

                selectedMusicalWorks.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

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

                if(eventDutyDTO.getMusicalWorks() != null && !eventDutyDTO.getMusicalWorks().isEmpty()) {
                        musicalWorks = new ArrayList<>();
                        for(MusicalWorkDTO musicalWorkDTO : eventDutyDTO.getMusicalWorks()) {
                                musicalWorks.add(musicalWorkDTO);
                                musicalWorkTable.getItems().add(musicalWorkDTO.getName());
                        }
                }

                initAppointment = appointment;
                initEventDutyDTO = eventDutyDTO;

                points.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                //^\d*\.\d{2}$
                                //"^\\d*[\\.,]?\\d{1,2}?$"

                                if (!newValue.matches("\\d*[\\,.]?\\d*?")) {
                                        points.setText(newValue.replaceAll("[^\\d*[\\,.]?\\d*?]", ""));
                                }
                        }
                });
        }

        @FXML
        private void save() throws ValidationException {
                if(validate()) {
                        Agenda.Appointment selectedAppointment = EventScheduleController.getSelectedAppointment();
                        EventDutyDTO oldEventDutyDTO = EventScheduleController.getEventForAppointment(selectedAppointment);
                        EventScheduleController.removeSelectedAppointmentFromCalendar(selectedAppointment);

                        EventDutyDTO eventDutyDTO = new EventDutyDTO();
                        eventDutyDTO.setEventDutyID(oldEventDutyDTO.getEventDutyID());
                        eventDutyDTO.setName(name.getText());
                        eventDutyDTO.setDescription(name.getText());
                        eventDutyDTO.setStartTime(DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue()));
                        eventDutyDTO.setEndTime(endTime.getValue() == null ? DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue().plusHours(2)) : DateHelper.mergeDateAndTime(date.getValue(), endTime.getValue()));
                        eventDutyDTO.setEventType(EventType.Hofkapelle);
                        eventDutyDTO.setEventStatus(EventStatus.Unpublished);
                        eventDutyDTO.setConductor(conductor.getText());
                        eventDutyDTO.setEventLocation(eventLocation.getText());
                        eventDutyDTO.setMusicalWorks(musicalWorks);
                        eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
                        eventDutyDTO.setInstrumentation(null); //TODO timebox 2
                        eventDutyDTO.setRehearsalFor(null); //TODO christina

                        EventScheduleManager.updateEventDuty(eventDutyDTO, initEventDutyDTO);

                        EventScheduleController.addEventDutyToGUI(eventDutyDTO);
                        EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
                        EventScheduleController.resetSideContent(); // remove content of sidebar
                }
        }

        @FXML
        public boolean discard() {
                if(!name.getText().equals(initEventDutyDTO.getName())
                        || !description.getText().equals(initEventDutyDTO.getDescription())
                        || !date.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalDate())
                        || !startTime.getValue().equals(initEventDutyDTO.getStartTime().toLocalDateTime().toLocalTime())
                        || !endTime.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalTime())
                        || !conductor.getText().equals(initEventDutyDTO.getConductor())
                        || !eventLocation.getText().equals(initEventDutyDTO.getEventLocation())
                        || !Double.valueOf(points.getText()).equals(initEventDutyDTO.getPoints())
                        || (musicalWorks == null && initEventDutyDTO.getMusicalWorks() != null) // musical work wurde entfernt
                        || (musicalWorks != null && initEventDutyDTO.getMusicalWorks() == null) // musical work wurde hinzugefügt
                        || (musicalWorks != null && initEventDutyDTO.getMusicalWorks() != null && !musicalWorks.equals(initEventDutyDTO.getMusicalWorks()))) { // musical work wurde verändert

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


        @FXML
        public void editHofkapelleInstrumentation() {
                InstrumentationController.selectMultipleMusicalWorks = true;
                if(date.getValue() != null) {
                        InstrumentationController.newHeading = name.getText() + " | " + date.getValue().toString();
                } else {
                        InstrumentationController.newHeading = name.getText();
                }

                InstrumentationController.selectedMusicalWorks = new ArrayList<MusicalWorkDTO>();
                if(musicalWorks != null && !musicalWorks.isEmpty()) {
                        for(MusicalWorkDTO musicalWorkDTO : musicalWorks) {
                                InstrumentationController.selectedMusicalWorks.add(musicalWorkDTO);
                        }
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Instrumentation.fxml"));
                Scene scene = null;
                try {
                        scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                        e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("Musical Work & Instrumentation");
                stage.setScene(scene);
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        public void handle(WindowEvent we) {
                                if(InstrumentationController.apply) {
                                        if(!InstrumentationController.selectedMusicalWorks.isEmpty()) {
                                                musicalWorks = InstrumentationController.selectedMusicalWorks;
                                                musicalWorkTable.getItems().clear();
                                                for(MusicalWorkDTO musicalWorkDTO : musicalWorks) {
                                                        musicalWorkTable.getItems().add(musicalWorkDTO.getName());
                                                }
                                        }  else {
                                                musicalWorkTable.getItems().clear();
                                                musicalWorks = null;
                                        }
                                        // TODO: timbox 2 save instrumentation
                                }

                        }
                });
                InstrumentationController.stage = stage;

                stage.showAndWait();
        }

        private void checkMandatoryFields() {
                name.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                if(name.getText() == null || name.getText().isEmpty()) {
                                        name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                                } else {
                                        name.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                                }

                        }
                });
                date.valueProperty().addListener(new ChangeListener<LocalDate>() {
                        @Override
                        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                                if(date.getValue() == null) {
                                        date.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                                } else {
                                        date.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                                }
                        }
                });

                startTime.valueProperty().addListener(new ChangeListener<LocalTime>() {
                        @Override
                        public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                                if(startTime.getValue() == null) {
                                        startTime.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                                } else {
                                        startTime.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                                }
                        }
                });
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
                        MessageHelper.showErrorAlertMessage("The starttime must be in future. \n");
                        return false;
                } else if(musicalWorks == null || musicalWorks.isEmpty()){
                        MessageHelper.showErrorAlertMessage("A musical work has to be selected.");
                        return false;
                }
                return true;}
}


