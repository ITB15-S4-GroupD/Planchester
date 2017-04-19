package Presentation.EventSchedule;

import Application.EventSchedule;
import Domain.Enum.EventType;
import Domain.Models.EventDutyModel;
import Presentation.PlanchesterGUI;
import Utils.DateHelper;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventScheduleController {
    private String colorOpera;
    private String colorConcert;
    private String colorTour;
    private String colorRehearsal;
    private String colorNonMusical;
    private String colorHofkapelle;

    private static Agenda staticAgenda;
    private static ScrollPane staticScrollPane;

    @FXML private Agenda agenda;
    @FXML private ScrollPane scrollPane;
    @FXML private MenuButton addNewEvent;
    @FXML private Label calenderWeekLabel;

    @FXML private MenuItem addNewConcert;
    @FXML private MenuItem addNewOpera;
    @FXML private MenuItem addNewTour;
    @FXML private MenuItem addNewHofkapelle;
    @FXML private MenuItem addNewRehearsal;
    @FXML private MenuItem addNewNonMusicalEvent;

    @FXML private JFXTextField colorKeyConcert;
    @FXML private JFXTextField colorKeyOpera;
    @FXML private JFXTextField colorKeyTour;
    @FXML private JFXTextField colorKeyHofkapelle;
    @FXML private JFXTextField colorKeyRehearsal;
    @FXML private JFXTextField colorKeyNonMusical;

    private static Agenda.AppointmentGroup opera;
    private static Agenda.AppointmentGroup concert;
    private static Agenda.AppointmentGroup hofkapelle;
    private static Agenda.AppointmentGroup tour;
    private static Agenda.AppointmentGroup rehearsal;
    private static Agenda.AppointmentGroup nonMusicalEvent;

    private static Agenda.Appointment selectedAppointment;
    public static Map<Agenda.Appointment, EventDutyModel> staticLoadedEventsMap = new HashMap<>();
    private static Map<String, String> eventWhichWasSelectedToCreate = new HashMap<>();

    static {
        eventWhichWasSelectedToCreate.put(EventType.Opera.toString(),"CreateOpera.fxml");
        eventWhichWasSelectedToCreate.put(EventType.Concert.toString(),"CreateConcert.fxml");
        eventWhichWasSelectedToCreate.put(EventType.Tour.toString(),"CreateTour.fxml");
        eventWhichWasSelectedToCreate.put(EventType.Hofkapelle.toString(),"CreateHofkapelle.fxml");
        eventWhichWasSelectedToCreate.put(EventType.Rehearsal.toString(),"CreateRehearsal.fxml");
        eventWhichWasSelectedToCreate.put(EventType.NonMusicalEvent.toString(),"CreateNonMusical.fxml");
    }

    @FXML
    public void initialize() {
        staticAgenda = agenda;
        staticScrollPane = scrollPane;

        getGroupColorsFromCSS();
        initializeAppointmentGroupsForEventtypes(); //must be the first call
        initialzeCalendarSettings();
        initialzeCalendarView();
        setEventToMenuItems();

        agenda.selectedAppointments().addListener(new ListChangeListener<Agenda.Appointment>() {
            @Override
            public void onChanged(Change<? extends Agenda.Appointment> c) {
                if(agenda.selectedAppointments().isEmpty()) {
                    return;
                }

                if(selectedAppointment != null && (agenda.selectedAppointments().get(0) != selectedAppointment)) {
                    if(tryResetSideContent() == null) {
                        showEventDetailView();
                        selectedAppointment = agenda.selectedAppointments().get(0);
                    } else {
                        agenda.selectedAppointments().clear();
                        agenda.selectedAppointments().add(selectedAppointment);
                    }
                } else if(selectedAppointment == null && tryResetSideContent() == null) {
                    showEventDetailView();
                    selectedAppointment = agenda.selectedAppointments().get(0);
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

    public static void setDisplayedLocalDateTime(LocalDateTime localDateTime) {
        staticAgenda.setDisplayedLocalDateTime(localDateTime);
    }

    public static Node tryResetSideContent() {
        if(getSideContent() == null) {
            return null;
        } else {
            Button discard = (Button) PlanchesterGUI.scene.lookup("#discard");
            discard.fire();
            return getSideContent();
        }
    }

    public static Node getSideContent() {
        return staticScrollPane.getContent();
    }

    public static void resetSideContent() {
        staticScrollPane.setContent(null);
    }

    public static void removeSelection(Agenda.Appointment appointment) {
        if(!staticAgenda.selectedAppointments().isEmpty() && staticAgenda.selectedAppointments().get(0) == appointment)
        {
            staticAgenda.selectedAppointments().clear();
            selectedAppointment = null;
        }
    }

    public static void addEventDutyToGUI(EventDutyModel event) {
        Agenda.Appointment appointment = new Agenda.AppointmentImpl();
        appointment.setSummary(event.getName());
        appointment.setDescription(event.getDescription());
        appointment.setLocation(event.getLocation());
        appointment.setStartTime(DateHelper.convertTimestampToCalendar(event.getStarttime()));
        appointment.setEndTime(DateHelper.convertTimestampToCalendar(event.getEndtime()));

        if(EventType.Opera.toString().equals(event.getEventType())) {
            appointment.setAppointmentGroup(opera);
        } else if(EventType.Concert.toString().equals(event.getEventType())) {
            appointment.setAppointmentGroup(concert);
        } else if(EventType.Tour.toString().equals(event.getEventType())) {
            appointment.setAppointmentGroup(tour);
        } else if(EventType.Rehearsal.toString().equals(event.getEventType())) {
            appointment.setAppointmentGroup(rehearsal);
        } else if(EventType.Hofkapelle.toString().equals(event.getEventType())) {
            appointment.setAppointmentGroup(hofkapelle);
        } else if(EventType.NonMusicalEvent.toString().equals(event.getEventType())) {
            appointment.setAppointmentGroup(nonMusicalEvent);
        }
        staticLoadedEventsMap.put(appointment, event);
        staticAgenda.appointments().add(appointment);
    }

    public static EventDutyModel getEventForAppointment(Agenda.Appointment appointment) {
        return staticLoadedEventsMap.get(appointment);
    }

    public static Agenda.Appointment getSelectedAppointment() {
        if(!staticAgenda.selectedAppointments().isEmpty()) {
            return staticAgenda.selectedAppointments().get(0);
        }
        return null;
    }

    public static void setSelectedAppointment(EventDutyModel eventDutyModel) {
        for (Map.Entry<Agenda.Appointment, EventDutyModel> entry : staticLoadedEventsMap.entrySet()) {
            if (Objects.equals(eventDutyModel, entry.getValue())) {
                staticAgenda.selectedAppointments().clear();
                staticAgenda.selectedAppointments().add(entry.getKey());
            }
        }
    }

    private void getGroupColorsFromCSS() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Presentation/CSS/agenda.css"), "UTF-8"));
            String line = br.readLine();

            while (line != null) {
                if(line.contains("group1")) {
                    colorOpera = "-fx-control-inner-background: " + getColor(line) + ";";
                } else if(line.contains("group2")) {
                    colorConcert = "-fx-control-inner-background: " + getColor(line) + ";";
                } else if(line.contains("group3")) {
                    colorHofkapelle = "-fx-control-inner-background: " + getColor(line) + ";";
                } else if(line.contains("group4")) {
                    colorTour = "-fx-control-inner-background: " + getColor(line) + ";";
                } else if(line.contains("group5")) {
                    colorRehearsal = "-fx-control-inner-background: " + getColor(line) + ";";
                } else if(line.contains("group6")) {
                    colorNonMusical = "-fx-control-inner-background: " + getColor(line) + ";";
                }
                line = br.readLine();
            }
            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getColor(String s) {
        try {
            return s.substring(s.indexOf("#"),s.indexOf("#")+7);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void setCalenderWeekLabel() {
        Calendar cal = agenda.getDisplayedCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        calenderWeekLabel.setText("Calender Week " + String.valueOf(week));
    }

    private void setEventToMenuItems() {
        addNewOpera.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(tryResetSideContent() == null) {
                        agenda.selectedAppointments().clear();
                        selectedAppointment = null;
                        scrollPane.setContent(FXMLLoader.load(getClass().getResource("CreateOpera.fxml")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
        agenda.localeProperty().set(Locale.UK);
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
        addEventTypeEntriesToMenuButton();
        setColorKeyMap();

        //put events to calendar
        List<EventDutyModel> events = EventSchedule.getEventDutyForCurrentWeek();
        for(EventDutyModel event : events) {
            addEventDutyToGUI(event);
        }
    }

    private void setColorKeyMap() {
        colorKeyOpera.setStyle(colorOpera);
        colorKeyConcert.setStyle(colorConcert);
        colorKeyTour.setStyle(colorTour);
        colorKeyHofkapelle.setStyle(colorHofkapelle);
        colorKeyRehearsal.setStyle(colorRehearsal);
        colorKeyNonMusical.setStyle(colorNonMusical);
    }

    private void addEventTypeEntriesToMenuButton() {
        addNewConcert = new MenuItem(EventType.Concert.toString());
        addNewOpera = new MenuItem(EventType.Opera.toString());
        addNewTour = new  MenuItem(EventType.Tour.toString());
        addNewHofkapelle = new MenuItem(EventType.Hofkapelle.toString());
        addNewRehearsal = new MenuItem(EventType.Rehearsal.toString());
        addNewNonMusicalEvent = new MenuItem(EventType.NonMusicalEvent.toString());

        addNewEvent.getItems().add(addNewConcert);
        addNewEvent.getItems().add(addNewOpera);
        addNewEvent.getItems().add(addNewTour);
        addNewEvent.getItems().add(addNewHofkapelle);
        addNewEvent.getItems().add(addNewRehearsal);
        addNewEvent.getItems().add(addNewNonMusicalEvent);
    }

    private void showEventDetailView() {
        try {
            ObservableList<Agenda.Appointment> appointments = agenda.selectedAppointments();
            if(!appointments.isEmpty()) {
                Agenda.Appointment appointment = appointments.get(0);
                EventDutyModel eventDutyModel = getEventForAppointment(appointment);

                if(EventType.Opera.toString().equals(eventDutyModel.getEventType())) {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource("EditOpera.fxml")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}