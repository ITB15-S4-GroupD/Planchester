package Presentation.DutyRoster;

import Application.*;
import Application.DTO.EventDutyDTO;
import Application.DutyRosterManager;
import Domain.Models.Permission;
import Presentation.CalenderController;

import Presentation.PlanchesterGUI;
import Utils.DateHelper;
import Utils.Enum.DutyRosterStatus;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.MessageHelper;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import jfxtras.scene.control.agenda.Agenda;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
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
    protected static Agenda.Appointment selectedAppointment;
    @FXML private ScrollPane scrollPane;
    @FXML protected MenuButton publishDutyRoster;

    @FXML
    public void initialize() {
        try {
            scrollPane.setContent(FXMLLoader.load(getClass().getResource("ShowDuty.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        staticAgenda = agenda;
        staticScrollPane = scrollPane;

        getGroupColorsFromCSS();
        initializeAppointmentGroupsForEventtypes(); //must be the first initialize-call
        initialzeCalendarSettings();
        initialzeCalendarView();

        agenda.setOnMouseClicked(event -> {
            if(event.getTarget().toString().contains("DayBodyPane")) {
                resetSideContent();
                removeSelection();
            }
        });

        agenda.selectedAppointments().addListener((ListChangeListener<Agenda.Appointment>) c -> {
            if(agenda.selectedAppointments().isEmpty()) {
                resetSideContent();
                return;
            }

            resetSideContent();
            selectedAppointment = agenda.selectedAppointments().get(0);
            //showDutyDetailView();
        });

        Permission permission = AccountAdministrationManager.getInstance().getUserPermissions();
        publishDutyRoster.setVisible(permission.isPublishDutyRoster());
    }

    private void addMonthsEntriesToMenuButton() {
        if(AccountAdministrationManager.getInstance().getUserPermissions().isPublishDutyRoster()) {
            publishDutyRoster.getItems().clear();
            List<EventDutyDTO> unpublishedEvents = DutyRosterManager.getAllUnpublishedMonths();
            List<String> months = new ArrayList<>();
            EventHandler<ActionEvent> action = chooseMonthToPublish();
            Calendar cal = Calendar.getInstance();
            for (EventDutyDTO unpublishedEvent : unpublishedEvents) {
                cal.setTimeInMillis(unpublishedEvent.getStartTime().getTime());
                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);
                String monthYear = String.valueOf(month + " | " + year);
                boolean isInList = false;
                for (String monYear : months) {
                    if (monYear.equals(monthYear)) {
                        isInList = true;
                    }
                }
                if (!isInList) {
                    months.add(monthYear);
                }
            }
            for (String monthYear : months) {
                MenuItem month = new MenuItem(monthYear);
                month.setOnAction(action);
                publishDutyRoster.getItems().add(month);
            }
        }
    }

    private EventHandler<ActionEvent> chooseMonthToPublish() {
        return event -> {
            MenuItem mItem = (MenuItem) event.getSource();
            ButtonType buttonType = MessageHelper.showConfirmationMessage("Are you sure to publish " + mItem.getText());
            if(buttonType.equals(ButtonType.YES)) {
                publishDutyRoster(mItem);
            }
        };
    }

    private void publishDutyRoster(MenuItem item) {
        String monthOfYear = item.getText();
        String[] parts = monthOfYear.split(" | ");
        int month = Integer.valueOf(parts[0]);
        int year = Integer.valueOf(parts[2]);

        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        DutyRoster dutyRoster = new DutyRoster();
        EventDutyDTO eventDutyDTO = dutyRoster.validateMonth(Year.of(displayedDate.getYear()), displayedDate.getMonth());

        if (eventDutyDTO != null) {
            PublishDutyRoster.publish(Year.of(year), Month.of(month));
            agenda.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime());
            refresh();
            setSelectedAppointment(eventDutyDTO);
        } else {
            publishDutyRoster.getItems().remove(item);
        }

    }

    public static void setSelectedAppointment(EventDutyDTO eventDutyDTO) {
        for (Map.Entry<Agenda.Appointment, EventDutyDTO> entry : staticLoadedEventsMap.entrySet()) {
            if (eventDutyDTO.getEventDutyId() == entry.getValue().getEventDutyId()) {
                staticAgenda.selectedAppointments().clear();
                staticAgenda.selectedAppointments().add(entry.getKey());
            }
        }
    }

    @FXML
    protected void navigateOneWeekBackClicked() {
        removeAllData();
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
        removeAllData();
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.plus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyDTO> events = DutyRosterManager.getDutyRosterListForWeek(agenda.getDisplayedCalendar());
        for (EventDutyDTO event : events) {
            addDutyRosterToGUI(event);
        }
    }

    @FXML
    protected void showActualWeekClicked() {
        super.showActualWeekClicked();
        refresh();
    }

    @FXML public void refresh() {
        removeAllData();

        List<EventDutyDTO> events = DutyRosterManager.getDutyRosterListForWeek(staticAgenda.getDisplayedCalendar());
        for(EventDutyDTO event : events) {
            addDutyRosterToGUI(event);
        }
        addMonthsEntriesToMenuButton();
    }

    public static void reload() {
        removeAllData();

        List<EventDutyDTO> events = DutyRosterManager.getDutyRosterListForWeek(staticAgenda.getDisplayedCalendar());
        for(EventDutyDTO event : events) {
            addDutyRosterToGUI(event);
        }
    }

    public static void removeAllData() {
        staticLoadedEventsMap.clear();
        staticAgenda.appointments().clear();
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

    public static void resetSideContent() {
        staticScrollPane.setContent(null);
    }

    private void initialzeCalendarView() {
        //set CalenderWeek
        setCalenderWeekLabel();
        setColorKeyMap();
        addMonthsEntriesToMenuButton();

        //put events to calendar
        List<EventDutyDTO> events = DutyRosterManager.getDutyRosterListForCurrentWeek();
        for(EventDutyDTO event : events) {
            addDutyRosterToGUI(event);
        }
    }
}
