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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christina on 20.04.2017.
 */
public class CreateTourController {
    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXDatePicker startDate;
    @FXML private JFXDatePicker endDate;
    @FXML private TextField eventLocation;
    @FXML private TextField conductor;
    @FXML private TextField points;

    @FXML private TableView<String> musicalWorkTable;
    @FXML private TableColumn<String, String> selectedMusicalWorks;

    private List<MusicalWorkDTO> musicalWorks;
    private InstrumentationDTO instrumentation; // TODO timebox2

    public static List<EventDutyDTO> rehearsalList;
    @FXML private TableView<String> rehearsalTableView;
    @FXML private TableColumn<String, String> rehearsalTableColumn;

    @FXML
    public void initialize() {
        rehearsalList = new LinkedList<>();
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
    private void insertNewTourPerformance() throws ValidationException {
        if (validate()) {

            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(Timestamp.valueOf(startDate.getValue().atStartOfDay()));
            eventDutyDTO.setEndTime(Timestamp.valueOf(endDate.getValue().atTime(23, 59, 59)));
            eventDutyDTO.setEventType(EventType.Tour);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setEventLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(musicalWorks);
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setInstrumentation(null); //TODO timebox 2
            eventDutyDTO.setRehearsalFor(null); //TODO christina

            EventScheduleManager.createEventDuty(eventDutyDTO);
            EventScheduleController.addEventDutyToGUI(eventDutyDTO); // add event to agenda

            for(EventDutyDTO eventD : rehearsalList){
                eventD.setRehearsalFor(eventDutyDTO.getEventDutyID());
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
    public boolean cancel() {
        if (!name.getText().isEmpty() || !description.getText().isEmpty() || startDate.getValue() != null
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

    @FXML
    public void editTourInstrumentation() {
        InstrumentationController.selectMultipleMusicalWorks = true;
        if (startDate.getValue() != null && endDate.getValue() != null) {
            InstrumentationController.newHeading = name.getText() + " | " + startDate.getValue().toString() + "-" + endDate.getValue().toString();
        } else {
            InstrumentationController.newHeading = name.getText();
        }

        InstrumentationController.selectedMusicalWorks = new ArrayList<MusicalWorkDTO>();
        if (musicalWorks != null && !musicalWorks.isEmpty()) {
            for (MusicalWorkDTO musicalWorkDTO : musicalWorks) {
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
                if (InstrumentationController.apply) {
                    if (!InstrumentationController.selectedMusicalWorks.isEmpty()) {
                        musicalWorks = InstrumentationController.selectedMusicalWorks;
                        musicalWorkTable.getItems().clear();
                        for (MusicalWorkDTO musicalWorkDTO : musicalWorks) {
                            musicalWorkTable.getItems().add(musicalWorkDTO.getName());
                        }
                    } else {
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

    private boolean validate() {
        LocalDate today = LocalDate.now();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if (name.getText().isEmpty()) {
            MessageHelper.showErrorAlertMessage("The Name is missing.");
            name.requestFocus();
            return false;
        } else if (start == null) {
            MessageHelper.showErrorAlertMessage("Startdate has to be set.");
            startDate.requestFocus();
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

    private void initializeMandatoryFields() {
        name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        startDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        endDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);

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
}