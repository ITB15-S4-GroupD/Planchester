package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Domain.EventDutyModel;
import Utils.DateHelper;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.MessageHelper;
import Utils.PlanchesterConstants;
import com.jfoenix.controls.JFXDatePicker;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by Christina on 20.04.2017.
 */
public class EditTourController {

    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXDatePicker startDate;
    @FXML private JFXDatePicker endDate;
    @FXML private TextField eventLocation;
    @FXML private TextField conductor;
    @FXML private TextField points;

    public static List<EventDutyDTO> rehearsalList;
    @FXML private TableView<String> rehearsalTableView;
    @FXML private TableColumn<String, String> rehearsalTableColumn;

    private EventDutyDTO initEventDutyDTO; // remember init data to compare
    private Agenda.Appointment initAppointment; // remember init data to compare

    @FXML
    public void initialize() {
        //TODO GET LIST OF REHEARSALS : Christina
        checkMandatoryFields();

        Agenda.Appointment appointment = EventScheduleController.getSelectedAppointment();
        EventDutyDTO eventDutyDTO = EventScheduleController.getEventForAppointment(appointment);

        name.setText(appointment.getSummary());
        description.setText(appointment.getDescription());
        startDate.setValue(appointment.getStartLocalDateTime().toLocalDate());
        endDate.setValue(appointment.getEndLocalDateTime().toLocalDate());
        eventLocation.setText(appointment.getLocation());
        conductor.setText(eventDutyDTO.getConductor());
        points.setText(eventDutyDTO.getPoints() != null ? String.valueOf(eventDutyDTO.getPoints()) : null);

        initAppointment = appointment;
        initEventDutyDTO = eventDutyDTO;
    }

    @FXML
    public void addNewRehearsal() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CreateRehearsal.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Add new Rehearsal");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if(CreateRehearsalController.apply) {
                    rehearsalList.add(CreateRehearsalController.eventDutyDTO);
                    rehearsalTableView.getItems().clear();
                    rehearsalTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
                    for(EventDutyDTO e : rehearsalList) {
                        String rehearsalToAdd = e.getName();
                        rehearsalTableView.getItems().add(rehearsalToAdd);
                    }
                }
            }
        });
        CreateRehearsalController.stage = stage;
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
            eventDutyDTO.setStartTime(DateHelper.mergeDateAndTime(startDate.getValue(), LocalTime.MIDNIGHT));
            eventDutyDTO.setEndTime(DateHelper.mergeDateAndTime(endDate.getValue(), LocalTime.MAX));
            eventDutyDTO.setEventType(EventType.Tour);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setEventLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(null); //TODO TIMO
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setInstrumentation(null); //TODO TIMO
            eventDutyDTO.setRehearsalFor(null); //TODO TIMO

            EventScheduleController.addEventDutyToGUI(eventDutyDTO);
            //TODO UPDATE REHEARSALS : Christina
            EventScheduleController.addEventDutyToGUI(eventDutyDTO);
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
        }
    }

    @FXML
    public boolean discard() {
        // remove content of sidebar
        EventScheduleController.resetSideContent();
        EventScheduleController.removeSelection(initAppointment);
        return true;
    }

    private void checkMandatoryFields() {
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (name.getText() == null || name.getText().isEmpty()) {
                    name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                } else {
                    name.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                }

            }
        });

        startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (startDate.getValue() == null) {
                    startDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                } else {
                    startDate.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                }
            }
        });

        endDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (endDate.getValue() == null) {
                    endDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                } else {
                    endDate.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                }
            }
        });
    }

    private boolean validate() {
        LocalDate today = LocalDate.now();

        if(name.getText().isEmpty()){
            MessageHelper.showErrorAlertMessage("The Name is missing.");
            name.requestFocus();
            return false;
        } else if(startDate.getValue() == null || startDate.getValue().isBefore(today) ){
            MessageHelper.showErrorAlertMessage("The startdate is not valid.");
            startDate.requestFocus();
            return false;
        } else if(endDate.getValue().isBefore(startDate.getValue())){
            MessageHelper.showErrorAlertMessage("The startdate must be in befor the enddate. \n");
            return false;
        }
        //TODO TIMO: validate musiclaWork: is mandatory!
        return true;
    }
}
