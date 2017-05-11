package Presentation.DutyRoster;

import Application.DTO.EventDutyDTO;
import Application.DutyRoster;
import Application.DutyRosterManager;
import Application.PublishEventSchedule;
import Application.DutyRosterManager;
import Presentation.CalenderController;

import Presentation.PlanchesterGUI;
import Utils.DateHelper;
import Utils.Enum.DutyRosterStatus;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import jfxtras.scene.control.agenda.Agenda;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by TheeLenchen on 08.05.2017.
 */
public class DutyRosterController extends CalenderController{

    protected static Agenda staticAgenda;
    protected static ScrollPane staticScrollPane;
    protected static Map<Agenda.Appointment, EventDutyDTO> staticLoadedEventsMap = new HashMap<>();
    protected static boolean editOpen = false;
    protected static Agenda.Appointment selectedAppointment;



    @FXML
    public void initialize() {
        staticAgenda = agenda;
        staticScrollPane = scrollPane;

        getGroupColorsFromCSS();
        initializeAppointmentGroupsForEventtypes(); //must be the first initialize-call
        initialzeCalendarSettings();
        initialzeCalendarView();

        agenda.setOnMouseClicked(event -> {
            if(event.getTarget().toString().contains("DayBodyPane")) {
                if(editOpen == true){
                    if(tryResetSideContent() == null) {
                        removeSelection();
                    }
                }
            }
        });

        agenda.selectedAppointments().addListener((ListChangeListener<Agenda.Appointment>) c -> {
            if(agenda.selectedAppointments().isEmpty()) {
                return;
            }

            if(selectedAppointment != null && (agenda.selectedAppointments().get(0) != selectedAppointment)) {
                if(tryResetSideContent() == null) {
                    selectedAppointment = agenda.selectedAppointments().get(0);
//                        showDutyDetailView();
                } else {
                    agenda.selectedAppointments().clear();
                    agenda.selectedAppointments().add(selectedAppointment);
                }
            } else if(selectedAppointment == null && tryResetSideContent() == null) {
                selectedAppointment = agenda.selectedAppointments().get(0);
//                    showDutyDetailView();
            }
        });
    }

    @FXML
    private void publishDutyRoster() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        DutyRoster dutyRoster = new DutyRoster();
        dutyRoster.validateMonth(Year.of(displayedDate.getYear()), displayedDate.getMonth());
    }

    @FXML
    protected void navigateOneWeekBackClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.minus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyDTO> events = DutyRosterManager.getDutyRosterListForWeek(agenda.getDisplayedCalendar());
        for(EventDutyDTO event : events) {
            addDutyRosterToGUI(event);
        }
    }

    @FXML
    protected void navigateOneWeekForwardClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.plus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyDTO> events = DutyRosterManager.getDutyRosterListForWeek(agenda.getDisplayedCalendar());
        for (EventDutyDTO event : events) {
            addDutyRosterToGUI(event);
        }
    }

    @FXML
    public void refresh() {

    }

    public static void removeSelection() {
            staticAgenda.selectedAppointments().clear();
            selectedAppointment = null;
    }

    public static void addDutyRosterToGUI(EventDutyDTO event) {
        Agenda.Appointment appointment = new Agenda.AppointmentImpl();
        appointment.setDescription(event.getDescription());
        appointment.setLocation(event.getLocation());
        appointment.setStartTime(DateHelper.convertTimestampToCalendar(event.getStartTime()));
        appointment.setEndTime(DateHelper.convertTimestampToCalendar(event.getEndTime()));

        if(EventType.Opera.equals(event.getEventType())) {
            appointment.setAppointmentGroup(opera);
            appointment.setSummary(event.getName() + "\nOpera");
        } else if(EventType.Concert.equals(event.getEventType())) {
            appointment.setAppointmentGroup(concert);
            appointment.setSummary(event.getName() + "\nConcert");
        } else if(EventType.Tour.equals(event.getEventType())) {
            appointment.setAppointmentGroup(tour);
            appointment.setWholeDay(true);
        } else if(EventType.Rehearsal.equals(event.getEventType())) {
            appointment.setAppointmentGroup(rehearsal);
            appointment.setSummary(event.getName() + "\nRehearsal");
        } else if(EventType.Hofkapelle.equals(event.getEventType())) {
            appointment.setAppointmentGroup(hofkapelle);
            appointment.setSummary(event.getName() + "\nHofkapelle");
        } else if(EventType.NonMusicalEvent.equals(event.getEventType())) {
            appointment.setAppointmentGroup(nonMusicalEvent);
            appointment.setSummary(event.getName() + "\nNonMusicalEvent");
        }

        if(EventStatus.Cancelled.equals(event.getEventStatus())) {
            appointment.setSummary(appointment.getSummary() + " (C)");
        } else if(DutyRosterStatus.Published.equals(event.getDutyRosterStatus())) {
            appointment.setSummary(appointment.getSummary() + " (P)");
        } else if(DutyRosterStatus.Unpublished.equals(event.getDutyRosterStatus())) {
            appointment.setSummary(appointment.getSummary() + " (UP)");
        }


        staticLoadedEventsMap.put(appointment, event);
        staticAgenda.appointments().add(appointment);
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

    public static void resetSideContent() {
        staticScrollPane.setContent(null);
        editOpen = false;
    }

    public static Node getSideContent() {
        return staticScrollPane.getContent();
    }

    private void initialzeCalendarView() {
        //set CalenderWeek
        setCalenderWeekLabel();
        setColorKeyMap();

        //put events to calendar
        List<EventDutyDTO> events = DutyRosterManager.getDutyRosterListForCurrentWeek();
        for(EventDutyDTO event : events) {
            addDutyRosterToGUI(event);
        }
    }
}
