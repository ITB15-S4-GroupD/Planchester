package Presentation.EventSchedule;

import Application.EventSchedule;
import Domain.Enum.EventType;
import Domain.Models.EventDutyModel;
import Presentation.PlanchesterGUI;
import Utils.DateHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.UnexpectedException;
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
    private static ComboBox staticComboNewEvent;
    @FXML private ComboBox comboNewEvent;
    @FXML private Label calenderWeekLabel;

    @FXML private MenuButton addNewEvent;
    @FXML private MenuItem addNewConcert;
    @FXML private MenuItem addNewOpera;
    @FXML private MenuItem addNewTour;
    @FXML private MenuItem addNewHofkappelle;
    @FXML private MenuItem addNewRehearsal;
    @FXML private MenuItem addNewNonMusicalEvent;

    private static Agenda.AppointmentGroup opera;
    private static Agenda.AppointmentGroup concert;
    private static Agenda.AppointmentGroup hofkapelle;
    private static Agenda.AppointmentGroup tour;
    private static Agenda.AppointmentGroup rehearsal;
    private static Agenda.AppointmentGroup nonMusicalEvent;

    private static Map<String, String> eventToSelectFromCombobox = new HashMap<>();
    static {
        eventToSelectFromCombobox.put(EventType.Opera.toString(),"CreateOpera.fxml");
        eventToSelectFromCombobox.put(EventType.Concert.toString(),"CreateConcert.fxml");
        eventToSelectFromCombobox.put(EventType.Tour.toString(),"CreateTour.fxml");
        eventToSelectFromCombobox.put(EventType.Hofkapelle.toString(),"CreateHofkapelle.fxml");
        eventToSelectFromCombobox.put(EventType.Rehearsal.toString(),"CreateRehearsal.fxml");
        eventToSelectFromCombobox.put(EventType.NonMusicalEvent.toString(),"CreateNonMusical.fxml");
    }

    @FXML
    public void initialize() {
        staticAgenda = agenda;
        staticScrollPane = scrollPane;
        staticComboNewEvent = comboNewEvent;

        initialzeCalendarSettings();
        initialzeCalendarView();

        initializeAppointmentGroupsForEventtypes();
        //EventHandler: show empty form for adding a new EventDuty
        comboNewEvent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldVal, String newVal) {
                try {
                    showEmptyEventDetailView(newVal);
                } catch (UnexpectedException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                }
    }

    private void setEventToMenuItems() {
        addNewOpera.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource("CreateOpera.fxml")));
                } catch (Exception e) {
                    System.out.println("Resource not found. Aborting.");
                }
            }
        });
    }

    @FXML
    private void navigateOneWeekBackClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.minus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyModel> events = EventSchedule.getEventDutyForWeek(agenda.getDisplayedCalendar());
        for(EventDutyModel event : events) {
            addEventDutyToGUI(event);
        }
    }

    @FXML
    private void navigateOneWeekForwardClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.plus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyModel> events = EventSchedule.getEventDutyForWeek(agenda.getDisplayedCalendar());
        for(EventDutyModel event : events) {
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
        staticComboNewEvent.getSelectionModel().clearSelection();
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
        setAddNewEventMenuButton();

        //put events to calendar
        List<EventDutyModel> events = EventSchedule.getEventDutyForCurrentWeek();
        for(EventDutyModel event : events) {
            addEventDutyToGUI(event);
        }
    }

    public static void addEventDutyToGUI(EventDutyModel event) {
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

    private void setAddNewEventMenuButton() {
        addNewConcert = new MenuItem(EventType.Concert.toString());
        addNewOpera = new MenuItem(EventType.Opera.toString());
        addNewTour = new  MenuItem(EventType.Tour.toString());
        addNewHofkappelle = new MenuItem(EventType.Hofkapelle.toString());
        addNewRehearsal = new MenuItem(EventType.Rehearsal.toString());
        addNewNonMusicalEvent = new MenuItem(EventType.NonMusicalEvent.toString());

        addNewEvent.getItems().add(addNewConcert);
        addNewEvent.getItems().add(addNewOpera);
        addNewEvent.getItems().add(addNewTour);
        addNewEvent.getItems().add(addNewHofkappelle);
        addNewEvent.getItems().add(addNewRehearsal);
        addNewEvent.getItems().add(addNewNonMusicalEvent);
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

    private void showEmptyEventDetailView(String selectedComboboxParameter) throws UnexpectedException {
        String formToLoad = eventToSelectFromCombobox.get(selectedComboboxParameter);

        try {
            scrollPane.setContent(FXMLLoader.load(getClass().getResource(formToLoad)));
        } catch (Exception e) {
            throw new UnexpectedException("No controll found for selected type.");
        }
    }

    private void setCalenderWeekLabel() {
        Calendar cal = agenda.getDisplayedCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        calenderWeekLabel.setText("Calender Week " + String.valueOf(week));
    }
}