package Presentation.EventSchedule;

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
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jfxtras.scene.control.agenda.Agenda;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timorzipa on 02/05/2017.
 */
public class EditController {

    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXTimePicker startTime;
    @FXML private JFXTimePicker endTime;
    @FXML private JFXDatePicker date;
    @FXML private TextField eventLocation;
    @FXML private TextField conductor;
    @FXML private TextField points;

    public static List<EventDutyDTO> actualRehearsalList;
    public static List<EventDutyDTO> newRehearsalList;
    @FXML private TableView<String> rehearsalTableView;
    @FXML private TableColumn<String, String> rehearsalTableColumn;

    @FXML private TableView<String> musicalWorkTable;
    @FXML private TableColumn<String, String> selectedMusicalWorks;

    private List<MusicalWorkDTO> musicalWorks;
    private InstrumentationDTO instrumentation; // TODO timebox2

    private Agenda.Appointment initAppointment; // remember init data to compare
    private EventDutyDTO initEventDutyDTO; // remember init data to compare

    @FXML private Button btnCancelEvent;
    @FXML private Button btnSaveEvent;
    @FXML private Button btnEditEvent;
    @FXML private Button btnEditDetails;
    @FXML private Button btnAddRehearsal;
    @FXML private Button btnRemoveRehearsal;

    @FXML
    public void initialize() {
        checkMandatoryFields();

        selectedMusicalWorks.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        Agenda.Appointment appointment = EventScheduleController.getSelectedAppointment();
        EventDutyDTO eventDutyDTO = EventScheduleController.getEventForAppointment(appointment);

        actualRehearsalList = EventScheduleManager.getAllRehearsalsOfEventDuty(eventDutyDTO);
        newRehearsalList = actualRehearsalList;
        rehearsalTableView.getItems().clear();
        rehearsalTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        for(EventDutyDTO e : newRehearsalList) {
            String rehearsalToAdd = e.getName();
            rehearsalTableView.getItems().add(rehearsalToAdd);
        }

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
        initNotEditableFields();
        initAppointment = appointment;
        initEventDutyDTO = eventDutyDTO;

        points.textProperty().addListener((observable, oldValue, newValue) -> {
            //^\d*\.\d{2}$
            //"^\\d*[\\.,]?\\d{1,2}?$"

            if (!newValue.matches("\\d*[\\,.]?\\d*?")) {
                points.setText(newValue.replaceAll("[^\\d*[\\,.]?\\d*?]", " "));
            }
        });
    }

    private void initNotEditableFields() {
        btnEditEvent.setVisible(true);
        btnCancelEvent.setVisible(false);
        btnSaveEvent.setVisible(false);
        btnEditDetails.setVisible(false);
        btnAddRehearsal.setVisible(false);
        btnRemoveRehearsal.setVisible(false);

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
        conductor.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        points.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
    }

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
            eventDutyDTO.setEventType(initEventDutyDTO.getEventType());
            eventDutyDTO.setEventStatus(initEventDutyDTO.getEventStatus());
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setEventLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(musicalWorks);
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setInstrumentation(null); //TODO timebox 2
            eventDutyDTO.setRehearsalFor(null);

            EventScheduleManager.updateEventDuty(eventDutyDTO, initEventDutyDTO);

            //Add added Rehearsal to EventDuty
            updateRehearsal(eventDutyDTO);

            EventScheduleController.addEventDutyToGUI(eventDutyDTO);
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
        }
    }

    private void updateRehearsal(EventDutyDTO eventDutyDTO) throws ValidationException {
        for(EventDutyDTO rehearsalFromNew : newRehearsalList) {
            if(rehearsalFromNew.getEventDutyID() == null) {
                rehearsalFromNew.setRehearsalFor(eventDutyDTO.getEventDutyID());
                EventScheduleManager.createEventDuty(rehearsalFromNew);
                EventScheduleController.addEventDutyToGUI(rehearsalFromNew);
            }
        }
        actualRehearsalList.clear();
        newRehearsalList.clear();
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

        stage.setOnCloseRequest(we -> {
            if(CreateRehearsalController.apply) {
                newRehearsalList.add(CreateRehearsalController.eventDutyDTO);
                rehearsalTableView.getItems().clear();
                rehearsalTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
                for(EventDutyDTO e : newRehearsalList) {
                    String rehearsalToAdd = e.getName();
                    rehearsalTableView.getItems().add(rehearsalToAdd);
                }
            }
        });
        CreateRehearsalController.stage = stage;
    }

    @FXML
    public boolean cancel() {
        if(points.getText()==null) {
            points.setText("0.0");
        }

        String pointRef = (initEventDutyDTO.getPoints() != null)? String.valueOf(initEventDutyDTO.getPoints()) : "0.0";
        if(!name.getText().equals(initEventDutyDTO.getName())
                || !description.getText().equals(initEventDutyDTO.getDescription())
                || !date.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalDate())
                || !startTime.getValue().equals(initEventDutyDTO.getStartTime().toLocalDateTime().toLocalTime())
                || !endTime.getValue().equals(initEventDutyDTO.getEndTime().toLocalDateTime().toLocalTime())
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

    @FXML
    public void editEvent () {
        btnCancelEvent.setVisible(true);
        btnSaveEvent.setVisible(true);
        btnEditEvent.setVisible(false);
        btnEditDetails.setVisible(true);
        btnAddRehearsal.setVisible(true);
        btnRemoveRehearsal.setVisible(true);

        name.setEditable(true);
        description.setEditable(true);
        date.setEditable(true);
        startTime.setEditable(true);
        endTime.setEditable(true);
        eventLocation.setEditable(true);
        conductor.setEditable(true);
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


    @FXML
    public void editInstrumentation() {
        InstrumentationController.selectMultipleMusicalWorks = true;
        if(date.getValue() != null) {
            InstrumentationController.newHeading = name.getText() + " | " + date.getValue().toString();
        } else {
            InstrumentationController.newHeading = name.getText();
        }

        InstrumentationController.selectedMusicalWorks = new ArrayList<>();
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
        stage.setOnCloseRequest(we -> {
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

        });
        InstrumentationController.stage = stage;

        stage.showAndWait();
    }

    protected void checkMandatoryFields() {
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
        } else if(musicalWorks == null || musicalWorks.isEmpty()){
            MessageHelper.showErrorAlertMessage("A musical work has to be selected.");
            return false;
        }
        return true;
    }
}