package Presentation.DutyRoster;

import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Presentation.CalenderController;

import Utils.DateHelper;
import Utils.Enum.EventType;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jfxtras.scene.control.agenda.Agenda;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by TheeLenchen on 08.05.2017.
 */
public class DutyRosterController extends CalenderController{

        @FXML
        private Agenda agenda;
        @FXML private ScrollPane scrollPane;

        private static Agenda staticAgenda;
        private static Agenda.AppointmentGroup opera;
        private static Agenda.AppointmentGroup concert;
        private static Agenda.AppointmentGroup hofkapelle;
        private static Agenda.AppointmentGroup tour;
        private static Agenda.AppointmentGroup rehearsal;
        private static Agenda.AppointmentGroup nonMusicalEvent;
        private static Agenda.Appointment selectedAppointment;

        private static Map<Agenda.Appointment, EventDutyDTO> staticLoadedEventsMap = new HashMap<>();
        private static ScrollPane staticScrollPane;
        private static boolean editOpen = false;

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

    protected void getGroupColorsFromCSS() {
            super.getGroupColorsFromCSS();
    }

    protected void setColorKeyMap() {
            super.setColorKeyMap();
    }

    protected void setCalenderWeekLabel() {
            super.setCalenderWeekLabel();
    }

    protected void showActualWeekClicked() {
            super.showActualWeekClicked();
    }

    @FXML
    protected void navigateOneWeekBackClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.minus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyDTO> events = EventScheduleManager.getEventDutyListForWeek(agenda.getDisplayedCalendar());
        for(EventDutyDTO event : events) {
            addDutyRosterToGUI(event);
        }
    }

    @FXML
    protected void navigateOneWeekForwardClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.plus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyDTO> events = EventScheduleManager.getEventDutyListForWeek(agenda.getDisplayedCalendar());
        for (EventDutyDTO event : events) {
            addDutyRosterToGUI(event);

        }
    }

    public static void removeSelection() {
            staticAgenda.selectedAppointments().clear();
            selectedAppointment = null;
        }

    protected void initialzeCalendarSettings() {
        super.initialzeCalendarSettings();
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
        staticLoadedEventsMap.put(appointment, event);
        staticAgenda.appointments().add(appointment);
    }



    protected void initializeAppointmentGroupsForEventtypes() {
          super.initializeAppointmentGroupsForEventtypes();
       }


    private void initialzeCalendarView() {
        //set CalenderWeek
        setCalenderWeekLabel();
        setColorKeyMap();

        //put events to calendar
        List<EventDutyDTO> events = EventScheduleManager.getEventDutyListForCurrentWeek();
        for(EventDutyDTO event : events) {
            addDutyRosterToGUI(event);
        }
    }
    }
