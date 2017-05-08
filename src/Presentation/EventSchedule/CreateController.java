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

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timorzipa on 02/05/2017.
 */
public abstract class CreateController {

    @FXML protected TextField name;
    @FXML protected TextArea description;
    @FXML protected JFXTimePicker startTime;
    @FXML protected JFXTimePicker endTime;
    @FXML protected JFXDatePicker date;
    @FXML protected TextField eventLocation;
    @FXML protected TextField conductor;
    @FXML protected TextField points;

    protected static List<EventDutyDTO> rehearsalList = new LinkedList<>();
    @FXML protected TableView<String> rehearsalTableView;
    @FXML protected TableColumn<String, String> rehearsalTableColumn;

    @FXML protected TableView<String> musicalWorkTable;
    @FXML protected TableColumn<String, String> selectedMusicalWorks;

    protected List<MusicalWorkDTO> musicalWorks;
    protected InstrumentationDTO instrumentation; // TODO timebox2

    protected boolean allowMultipleMusicalWorkSelection = true;
    protected EventType eventType;

    @FXML
    public boolean cancel() {
        if(!name.getText().isEmpty() || !description.getText().isEmpty() || date.getValue() != null
                || !eventLocation.getText().isEmpty() || !conductor.getText().isEmpty() || !points.getText().isEmpty() || (musicalWorks != null && !musicalWorks.isEmpty())) {
            ButtonType answer = MessageHelper.showConfirmationMessage(PlanchesterMessages.DISCARD_CHANGES);
            if(ButtonType.NO.equals(answer)) {
                return false;
            }
        }
        // remove content of sidebar
        EventScheduleController.resetSideContent();
        return true;
    }

    @FXML
    public void initialize() {
        initializeMandatoryFields();

        selectedMusicalWorks.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        points.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*[\\,.]?\\d*?")) {
                points.setText(newValue.replaceAll("[^\\d*[\\,.]?\\d*?]", ""));
            }
        });
    }

    protected void setMusicalWorkMulitpleSelection(boolean value) {
        allowMultipleMusicalWorkSelection = value;
    }

    protected void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @FXML
    public void editInstrumentation() {
        InstrumentationController.selectMultipleMusicalWorks = allowMultipleMusicalWorkSelection;
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
                rehearsalList.add(CreateRehearsalController.eventDutyDTO);
                rehearsalTableView.getItems().clear();
                rehearsalTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
                for(EventDutyDTO e : rehearsalList) {
                    String rehearsalToAdd = e.getName();
                    rehearsalTableView.getItems().add(rehearsalToAdd);
                }
            }
        });
        CreateRehearsalController.stage = stage;
    }

    @FXML
    public void removeRehearsal() {
        String rehearsalToRemove = rehearsalTableView.getSelectionModel().getSelectedItem();
        for(EventDutyDTO eventDutyDTO : rehearsalList) {
            if(eventDutyDTO.getName().equals(rehearsalToRemove)) {
                rehearsalList.remove(eventDutyDTO);
                break;
            }
        }
        rehearsalTableView.getItems().remove(rehearsalTableView.getSelectionModel().getFocusedIndex());
        rehearsalTableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        for(EventDutyDTO e : rehearsalList) {
            String rehearsalToAdd = e.getName();
            rehearsalTableView.getItems().add(rehearsalToAdd);
        }
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
        } else if(musicalWorks == null || musicalWorks.isEmpty()){
            MessageHelper.showErrorAlertMessage("A musical work has to be selected.");
            return false;
        }
        return true;
    }

    protected void initializeMandatoryFields() {
        name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        date.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        startTime.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);

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

    @FXML
    protected void insertEventDuty() throws ValidationException {
        if(validate()) {

            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue()));
            eventDutyDTO.setEndTime(endTime.getValue() == null ? DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue().plusHours(2)) : DateHelper.mergeDateAndTime(date.getValue(), endTime.getValue()));
            eventDutyDTO.setEventType(eventType);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(musicalWorks);
            eventDutyDTO.setPoints((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText()));
            eventDutyDTO.setInstrumentation(null); //TODO timebox 2
            eventDutyDTO.setRehearsalFor(null);

            eventDutyDTO = EventScheduleManager.createEventDuty(eventDutyDTO);
            EventScheduleController.addEventDutyToGUI(eventDutyDTO); // add event to agenda

            for(EventDutyDTO eventD : rehearsalList){
                eventD.setRehearsalFor(eventDutyDTO.getEventDutyId());
                eventD = EventScheduleManager.createEventDuty(eventD);
                EventScheduleController.addEventDutyToGUI(eventD);
            }

            rehearsalList.clear();

            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
            EventScheduleController.setSelectedAppointment(eventDutyDTO); // select created appointment
        }
    }
}