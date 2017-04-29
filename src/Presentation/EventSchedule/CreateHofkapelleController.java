package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Application.DTO.InstrumentationDTO;
import Application.DTO.MusicalWorkDTO;
import Application.EventScheduleManager;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Domain.EventDutyModel;
import Utils.DateHelper;
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

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ina on 09.04.2017.
 */
public class CreateHofkapelleController {

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

    private MusicalWorkDTO musicalWork;
    private List<MusicalWorkDTO> musicalWorks;
    private InstrumentationDTO instrumentation; // TODO timebox2

    @FXML
    public void initialize() {
        initializeMandatoryFields();

        selectedMusicalWorks.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

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
    private void insertNewHofkapellePerformance() throws ValidationException {
        if(validate()) {
            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue()));
            eventDutyDTO.setEndTime(endTime.getValue() == null ? DateHelper.mergeDateAndTime(date.getValue(), startTime.getValue().plusHours(2)) : DateHelper.mergeDateAndTime(date.getValue(), endTime.getValue()));
            eventDutyDTO.setEventType(EventType.Hofkapelle);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setEventLocation(eventLocation.getText());

            if(musicalWork != null) {
                List<MusicalWorkDTO> selectedMusicalWorks = new ArrayList<MusicalWorkDTO>();
                selectedMusicalWorks.add(musicalWork);
                eventDutyDTO.setMusicalWorks(selectedMusicalWorks);
            } else {
                eventDutyDTO.setMusicalWorks(null);
            }

            eventDutyDTO.setPoints((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText()));
            eventDutyDTO.setInstrumentation(null); //TODO timebox 2
            eventDutyDTO.setRehearsalFor(null); //TODO christina

            EventScheduleManager.createEventDuty(eventDutyDTO);

            EventScheduleController.addEventDutyToGUI(eventDutyDTO); // add event to agenda
            EventScheduleController.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime()); // set agenda view to week of created event
            EventScheduleController.resetSideContent(); // remove content of sidebar
            EventScheduleController.setSelectedAppointment(eventDutyDTO); // select created appointment
        }
    }

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

    private void initializeMandatoryFields() {
        name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        date.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        startTime.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);

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
}