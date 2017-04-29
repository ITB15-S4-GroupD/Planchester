package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Utils.DateHelper;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Domain.EventDutyModel;
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

    public static List<EventDutyDTO> rehearsalList;
    @FXML private TableView<String> rehearsalTableView;
    @FXML private TableColumn<String, String> rehearsalTableColumn;

    @FXML
    public void initialize() {
        rehearsalList = new LinkedList<>();
        initializeMandatoryFields();
    }

    @FXML
    private void insertNewTourPerformance() throws ValidationException {
        if(validate()) {

            EventDutyDTO eventDutyDTO = new EventDutyDTO();
            eventDutyDTO.setName(name.getText());
            eventDutyDTO.setDescription(description.getText());
            eventDutyDTO.setStartTime(Timestamp.valueOf(startDate.getValue().atStartOfDay()));
            eventDutyDTO.setEndTime(Timestamp.valueOf(endDate.getValue().atTime(23,59,59)));
            eventDutyDTO.setEventType(EventType.Tour);
            eventDutyDTO.setEventStatus(EventStatus.Unpublished);
            eventDutyDTO.setConductor(conductor.getText());
            eventDutyDTO.setEventLocation(eventLocation.getText());
            eventDutyDTO.setMusicalWorks(null); //TODO TIMO
            eventDutyDTO.setPoints(((points.getText() == null || points.getText().isEmpty()) ? null : Double.valueOf(points.getText())));
            eventDutyDTO.setInstrumentation(null); //TODO TIMO
            eventDutyDTO.setRehearsalFor(null); //TODO TIMO

            EventScheduleManager.createTourPerformance(eventDutyDTO);
            EventScheduleController.addEventDutyToGUI(eventDutyDTO); // add event to agenda

            for(EventDutyDTO eventD : rehearsalList){
                eventD.setRehearsalFor(eventDutyDTO.getEventDutyID());
                EventScheduleManager.createRehearsalPerformance(eventD);
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
        if(!name.getText().isEmpty() || !description.getText().isEmpty() || startDate.getValue() != null
                || endDate.getValue() != null || !eventLocation.getText().isEmpty() || !conductor.getText().isEmpty() || !points.getText().isEmpty()) {

            ButtonType answer = MessageHelper.showConfirmationMessage(PlanchesterMessages.DISCARD_CHANGES);
            if(ButtonType.NO.equals(answer)) {
                return false;
            }
        }
        EventScheduleController.resetSideContent();
        return true;
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


        if(name.getText().isEmpty()){
            MessageHelper.showErrorAlertMessage("The Name is missing.");
            name.requestFocus();
            return false;
        } else if(start == null){
            MessageHelper.showErrorAlertMessage("Startdate has to be set.");
            startDate.requestFocus();
            return false;
        } else if(end == null ){
            MessageHelper.showErrorAlertMessage("Enddate has to be set.");
            endDate.requestFocus();
            return false;
        }  else if(start.isAfter(end) || start.equals(end)) {
            MessageHelper.showErrorAlertMessage("Enddate has to be after the startdate.");
            return false;
        }
        //TODO TIMO: validate musiclaWork: is mandatory!
        return true;
    }

    private void initializeMandatoryFields() {
        name.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        startDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
        endDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);

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
        startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(startDate.getValue() == null) {
                    startDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                } else {
                    startDate.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                }
            }
        });

        endDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(endDate.getValue() == null) {
                    endDate.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY);
                } else {
                    endDate.setStyle(PlanchesterConstants.INPUTFIELD_VALID);
                }
            }
        });
    }
}
