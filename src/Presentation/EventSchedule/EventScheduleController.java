package Presentation.EventSchedule;

import Application.EventSchedule;
import Domain.PresentationModels.Enum.EventType;
import Domain.PresentationModels.EventDutyDTO;
import Presentation.PlanchesterGUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventScheduleController {
    //i:List for ChoiceBox to choice new duty
    ObservableList<String> dutyTypes = FXCollections.observableArrayList("Concert performance",
            "Opera performance","Tour duty","Hofkapelle","Rehearsal","Non-musical duty");

    //i:Mapping choiced duty for loading form/file
    Map<String, String> dutyToForm = new HashMap<>();

    @FXML private Agenda agenda;
    @FXML private ScrollPane scrollPane;
    @FXML private ComboBox comboNewDuty;
    @FXML private Label calenderWeekLabel;

    @FXML
    public void initialize() {
        Agenda.AppointmentGroup opera = new Agenda.AppointmentGroupImpl();
        opera.setStyleClass("group1");
        Agenda.AppointmentGroup concert = new Agenda.AppointmentGroupImpl();
        concert.setStyleClass("group2");
        Agenda.AppointmentGroup hofkapelle = new Agenda.AppointmentGroupImpl();
        hofkapelle.setStyleClass("group3");
        Agenda.AppointmentGroup tour = new Agenda.AppointmentGroupImpl();
        tour.setStyleClass("group4");
        Agenda.AppointmentGroup rehearsal = new Agenda.AppointmentGroupImpl();
        rehearsal.setStyleClass("group5");
        Agenda.AppointmentGroup nonMusicalEvent = new Agenda.AppointmentGroupImpl();
        nonMusicalEvent.setStyleClass("group6");

        List<EventDutyDTO> events = EventSchedule.getAllEventDuty();
        for(EventDutyDTO event : events) {
            //convert Timestamp to Calendar
            Calendar starttimeCalendar = Calendar.getInstance();
            starttimeCalendar.setTimeInMillis(event.getEventDuty().getStarttime().getTime());
            Calendar endtimeCalendar = Calendar.getInstance();
            endtimeCalendar.setTimeInMillis(event.getEventDuty().getEndtime().getTime());

            Agenda.Appointment appointment = new Agenda.AppointmentImpl();
            appointment.setSummary(event.getEventDuty().getName());
            appointment.setDescription(event.getEventDuty().getDescription());
            appointment.setLocation(event.getEventDuty().getLocation());
            appointment.setStartTime(starttimeCalendar);
            appointment.setEndTime(endtimeCalendar);

            if(EventType.Opera.toString().equals(event.getEventDuty().getEventType())) {
                appointment.setAppointmentGroup(opera);
            } else if(EventType.Concert.toString().equals(event.getEventDuty().getEventType())) {
                appointment.setAppointmentGroup(concert);
            } else if(EventType.Tour.toString().equals(event.getEventDuty().getEventType())) {
                appointment.setAppointmentGroup(tour);
            } else if(EventType.Rehearsal.toString().equals(event.getEventDuty().getEventType())) {
                appointment.setAppointmentGroup(rehearsal);
            } else if(EventType.Hofkapelle.toString().equals(event.getEventDuty().getEventType())) {
                appointment.setAppointmentGroup(hofkapelle);
            } else if(EventType.NonMusicalEvent.toString().equals(event.getEventDuty().getEventType())) {
                appointment.setAppointmentGroup(nonMusicalEvent);
            }
            agenda.appointments().add(appointment);
        }
        // agenda settings
        agenda.setAllowDragging(false); //drag and drop the event
        agenda.setAllowResize(false);
        agenda.localeProperty().set(Locale.GERMAN);
        agenda.setDisplayedLocalDateTime(LocalDateTime.now()); //show current week in event scheduler

        //set CalenderWeek
        setCalenderWeekLabel();

        agenda.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0){
                // TODO timo: check what type of event was selected an load correct fxml file
                try {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource("EditOpera.fxml")));

                    ObservableList<Agenda.Appointment> appointments = agenda.selectedAppointments();
                    Agenda.Appointment appointment = appointments.get(0);

                    TextField textField = (TextField) scrollPane.lookup("#name");
                    textField.setText(appointment.getSummary());

                    TextField textField2 = (TextField) scrollPane.lookup("#description");
                    textField2.setText(appointment.getDescription());

                    DatePicker datePicker = (DatePicker) scrollPane.lookup("#date");
                    datePicker.setValue(appointment.getStartLocalDateTime().toLocalDate());

                    LocalTimePicker startTime = (LocalTimePicker) scrollPane.lookup("#start");
                    startTime.setLocalTime(appointment.getStartLocalDateTime().toLocalTime());

                    LocalTimePicker endTime = (LocalTimePicker) scrollPane.lookup("#end");
                    endTime.setLocalTime(appointment.getEndLocalDateTime().toLocalTime());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Property selection changed");
            }
        });

        //i:ChoiceBox choiceNewEventDuty initialisieren
        comboNewDuty.setItems(dutyTypes);
        comboNewDuty.setPromptText("Choose Eventtype");

        //i:Map initialisieren
        dutyToForm.put("Opera performance","CreateOpera.fxml");
        dutyToForm.put("Concert performance","CreateConcert.fxml");
        dutyToForm.put("Tour duty","CreateTour.fxml");
        dutyToForm.put("Hofkapelle","CreateHofkapelle.fxml");
        dutyToForm.put("Rehearsal","CreateRehearsal.fxml");
        dutyToForm.put("Non-musical duty","CreateNonMusical.fxml");

        comboNewDuty.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldVal, String newVal) {

                String choice = newVal;
                String formToLoad = dutyToForm.get(choice);

                if( choice.equals("choose duty")) {
                    return;
                }

                try {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource(formToLoad)));
                } catch (Exception e) {
                    Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                    dialog.setHeaderText( "Your choice: " + choice );
                    dialog.setContentText("Trying to open file " + formToLoad + ":\n" + "Form unsupported yet");
                    dialog.setResizable(true);
                    dialog.getDialogPane().setPrefSize(350, 200);
                    dialog.showAndWait();
                    e.printStackTrace();
                }
            }
        });
    }

    private void setCalenderWeekLabel() {
        Calendar cal = agenda.getDisplayedCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        calenderWeekLabel.setText("Calender Week " + String.valueOf(week));
    }

    @FXML
    private void navigateOneWeekBackClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.minus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();
    }

    @FXML
    private void navigateOneWeekForwardClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.plus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();
    }

    @FXML
    private void showActualWeekClicked() {
        agenda.setDisplayedLocalDateTime(LocalDateTime.now());
        setCalenderWeekLabel();
    }

    @FXML
    private void saveEventChanges(){
        // TODO: implement for every kind of event
        TextField name = (TextField) PlanchesterGUI.scene.lookup("#name");
        TextField description = (TextField) PlanchesterGUI.scene.lookup("#description");
        DatePicker date = (DatePicker) PlanchesterGUI.scene.lookup("#date");
        LocalTimePicker startTime = (LocalTimePicker) PlanchesterGUI.scene.lookup("#start");
        LocalTimePicker endTime = (LocalTimePicker) PlanchesterGUI.scene.lookup("#end");

        LocalDateTime start = LocalDateTime.of(date.getValue(), startTime.getLocalTime());
        LocalDateTime end = LocalDateTime.of(date.getValue(), endTime.getLocalTime());

        ObservableList<Agenda.Appointment> appointments = agenda.selectedAppointments();
        Agenda.Appointment appointment = appointments.get(0);

        appointment.setSummary(name.getText());
        appointment.setDescription(description.getText());
        appointment.setStartLocalDateTime(start);
        appointment.setEndLocalDateTime(end);

        agenda.refresh();
    }
}