package Presentation.EventSchedule;

import Application.EventSchedule;
import Domain.Enum.EventType;
import Domain.PresentationModels.EventDutyDTO;
import Presentation.PlanchesterGUI;
import Utils.DateHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventScheduleController {
    private static Agenda staticAgenda;
    @FXML private Agenda agenda;
    private static ScrollPane staticScrollPane;
    @FXML private ScrollPane scrollPane;
    private static ComboBox staticComboNewDuty;
    @FXML private ComboBox comboNewEvent;
    @FXML private Label calenderWeekLabel;

    private static Agenda.AppointmentGroup opera;
    private static Agenda.AppointmentGroup concert;
    private static Agenda.AppointmentGroup hofkapelle;
    private static Agenda.AppointmentGroup tour;
    private static Agenda.AppointmentGroup rehearsal;
    private static Agenda.AppointmentGroup nonMusicalEvent;

    @FXML
    public void initialize() {
        staticAgenda = agenda;
        staticScrollPane = scrollPane;
        staticComboNewDuty = comboNewEvent;

        initializeAppointmentGroupsForEventtypes();
        initialzeCalendarSettings();
        initialzeCalendarView();

        //EventHandler: show clicked event details on gui
        agenda.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0){
                showEventDetailView();
            }
        });

        //EventHandler: show empty form for adding a new EventDuty
        comboNewEvent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldVal, String newVal) {
                showEmptyEventDetailView(newVal);
            }
        });
    }

    @FXML
    private void navigateOneWeekBackClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.minus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyDTO> events = EventSchedule.getEventDutyForWeek(agenda.getDisplayedCalendar());
        for(EventDutyDTO event : events) {
            addEventDutyToGUI(event);
        }
    }

    @FXML
    private void navigateOneWeekForwardClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.plus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyDTO> events = EventSchedule.getEventDutyForWeek(agenda.getDisplayedCalendar());
        for(EventDutyDTO event : events) {
            addEventDutyToGUI(event);
        }
    }

    @FXML
    private void showActualWeekClicked() {
        agenda.setDisplayedLocalDateTime(LocalDateTime.now());
        setCalenderWeekLabel();
    }

    @FXML
    private void saveEventChanges() {
        // TODO: implement into edit controllers
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


    public static void setDisplayedLocalDateTime(LocalDateTime localDateTime) {
        staticAgenda.setDisplayedLocalDateTime(localDateTime);
    }

    public static void resetSideContent() {
        staticScrollPane.setContent(null);
        staticComboNewDuty.getSelectionModel().clearSelection();
    }

    public static void addEventDutyToGUI(EventDutyDTO event) {
        Agenda.Appointment appointment = new Agenda.AppointmentImpl();
        appointment.setSummary(event.getEventDuty().getName());
        appointment.setDescription(event.getEventDuty().getDescription());
        appointment.setLocation(event.getEventDuty().getLocation());
        appointment.setStartTime(DateHelper.convertTimestampToCalendar(event.getEventDuty().getStarttime()));
        appointment.setEndTime(DateHelper.convertTimestampToCalendar(event.getEventDuty().getEndtime()));

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
        staticAgenda.appointments().add(appointment);
    }

    private static void initializeAppointmentGroupsForEventtypes() {
        opera = new Agenda.AppointmentGroupImpl();
        opera.setStyleClass("group1");
        concert = new Agenda.AppointmentGroupImpl();
        concert.setStyleClass("group2");
        hofkapelle = new Agenda.AppointmentGroupImpl();
        hofkapelle.setStyleClass("group3");
        tour = new Agenda.AppointmentGroupImpl();
        tour.setStyleClass("group4");
        rehearsal = new Agenda.AppointmentGroupImpl();
        rehearsal.setStyleClass("group5");
        nonMusicalEvent = new Agenda.AppointmentGroupImpl();
        nonMusicalEvent.setStyleClass("group6");
    }

    private void initialzeCalendarSettings() {
        // agenda settings
        agenda.setAllowDragging(false); //drag and drop the event
        agenda.setAllowResize(false);
        agenda.localeProperty().set(Locale.GERMAN);
        agenda.setDisplayedLocalDateTime(LocalDateTime.now()); //show current week in event scheduler

        // disable edit menu
        agenda.setEditAppointmentCallback(new Callback<Agenda.Appointment, Void>() {
            @Override
            public Void call(Agenda.Appointment param) {
                return null;
            }
        });
    }

    private void initialzeCalendarView() {
        //set CalenderWeek
        setCalenderWeekLabel();
        setAddNewEventCombobox();

        //put events to calendar
        List<EventDutyDTO> events = EventSchedule.getEventDutyForCurrentWeek();
        for(EventDutyDTO event : events) {
            addEventDutyToGUI(event);
        }
    }

    private void setAddNewEventCombobox() {
        ObservableList<String> eventTypes = FXCollections.observableArrayList(EventType.Concert.toString(), EventType.Opera.toString(), EventType.Tour.toString(), EventType.Hofkapelle.toString(), EventType.Rehearsal.toString(), EventType.NonMusicalEvent.toString() );
        comboNewEvent.setItems(eventTypes);
        comboNewEvent.setPromptText("Add new Event");
    }

    private void showEventDetailView() {
        // TODO all: check what type of event was selected an load correct fxml file
        try {
            ObservableList<Agenda.Appointment> appointments = agenda.selectedAppointments();
            if(!appointments.isEmpty()) {
                Agenda.Appointment appointment = appointments.get(0);
                scrollPane.setContent(FXMLLoader.load(getClass().getResource("EditOpera.fxml")));

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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Property selection changed");
    }

    private void showEmptyEventDetailView(String newVal) {
        Map<String, String> eventToSelectFromCombobox = new HashMap<>();
        eventToSelectFromCombobox.put("Opera","CreateOpera.fxml");
        eventToSelectFromCombobox.put("Concert","CreateConcert.fxml");
        eventToSelectFromCombobox.put("Tour","CreateTour.fxml");
        eventToSelectFromCombobox.put("Hofkapelle","CreateHofkapelle.fxml");
        eventToSelectFromCombobox.put("Rehearsal","CreateRehearsal.fxml");
        eventToSelectFromCombobox.put("Non-musical event","CreateNonMusical.fxml");

        String choice = newVal;
        String formToLoad = eventToSelectFromCombobox.get(choice);
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

    private void setCalenderWeekLabel() {
        Calendar cal = agenda.getDisplayedCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        calenderWeekLabel.setText("Calender Week " + String.valueOf(week));
    }
}