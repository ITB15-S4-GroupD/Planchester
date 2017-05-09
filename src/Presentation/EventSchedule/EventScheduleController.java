package Presentation.EventSchedule;

import Application.AccountAdministrationManager;
import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Application.PublishEventSchedule;
import Presentation.CalenderController;
import Domain.Models.Permission;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Presentation.PlanchesterGUI;
import Utils.DateHelper;
import Utils.PlanchesterConstants;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import jfxtras.scene.control.agenda.Agenda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventScheduleController extends CalenderController {

    @FXML private Agenda agenda;
    @FXML private ScrollPane scrollPane;
    @FXML private MenuButton addNewEvent;
    @FXML private Button btnPublishEventSchedule;
    @FXML private Label calenderWeekLabel;
    @FXML private Label unpublishedLabel;

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
    @FXML private JFXTextField mandatoryField;

    private String colorOpera;
    private String colorConcert;
    private String colorTour;
    private String colorRehearsal;
    private String colorNonMusical;
    private String colorHofkapelle;

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
        setEventToMenuItems();

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
                    showEventDetailView();
                } else {
                    agenda.selectedAppointments().clear();
                    agenda.selectedAppointments().add(selectedAppointment);
                }
            } else if(selectedAppointment == null && tryResetSideContent() == null) {
                selectedAppointment = agenda.selectedAppointments().get(0);
                showEventDetailView();
            }
        });

        Permission permission = AccountAdministrationManager.getInstance().getUserPermissions();
        btnPublishEventSchedule.setVisible(permission.isPublishEventSchedule());
        addNewEvent.setVisible(permission.isEditEventSchedule());
        unpublishedLabel.setVisible(permission.isEditEventSchedule());

    }

    @FXML
    public void navigateOneWeekBackClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.minus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyDTO> events = EventScheduleManager.getEventDutyListForWeek(agenda.getDisplayedCalendar());
        for(EventDutyDTO event : events) {
            addEventDutyToGUI(event);
        }
    }

    @FXML
    public void navigateOneWeekForwardClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.plus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();

        List<EventDutyDTO> events = EventScheduleManager.getEventDutyListForWeek(agenda.getDisplayedCalendar());
        for(EventDutyDTO event : events) {
            addEventDutyToGUI(event);
        }
    }

    @FXML
    public void showActualWeekClicked() {
        super.showActualWeekClicked();
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

    public static void resetSideContent() {
        staticScrollPane.setContent(null);
        editOpen = false;
    }

    public static Node getSideContent() {
        return staticScrollPane.getContent();
    }

    public static void removeSelection(Agenda.Appointment appointment) {
        if(!staticAgenda.selectedAppointments().isEmpty() && staticAgenda.selectedAppointments().get(0) == appointment) {
            staticAgenda.selectedAppointments().clear();
            selectedAppointment = null;
        }
    }

    public static void removeSelection() {
        staticAgenda.selectedAppointments().clear();
        selectedAppointment = null;
    }


    public static void removeSelectedAppointmentFromCalendar(Agenda.Appointment appointment) {
        staticLoadedEventsMap.remove(selectedAppointment);
        staticAgenda.appointments().remove(selectedAppointment);
    }

    public static void addEventDutyToGUI(EventDutyDTO event) {
        Agenda.Appointment appointment = new Agenda.AppointmentImpl();
        appointment.setDescription(event.getName());
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
            appointment.setSummary(event.getName());
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

        if(event.getEventStatus().equals(EventStatus.Published)) {
            appointment.setSummary(appointment.getSummary() + " (P)");
        } else if(event.getEventStatus().equals(EventStatus.Cancelled)) {
            appointment.setSummary(appointment.getSummary() + " (C)");
        } else if(event.getEventStatus().equals(EventStatus.Unpublished)) {
            appointment.setSummary(appointment.getSummary() + " (UP)");
        }

        staticLoadedEventsMap.put(appointment, event);
        staticAgenda.appointments().add(appointment);
    }

    public static EventDutyDTO getEventForAppointment(Agenda.Appointment appointment) {
        return staticLoadedEventsMap.get(appointment);
    }

    public static Agenda.Appointment getSelectedAppointment() {
        if(!staticAgenda.selectedAppointments().isEmpty()) {
            return staticAgenda.selectedAppointments().get(0);
        }
        return null;
    }

    public static void setSelectedAppointment(EventDutyDTO eventDutyDTO) {
        for (Map.Entry<Agenda.Appointment, EventDutyDTO> entry : staticLoadedEventsMap.entrySet()) {
            if (eventDutyDTO.getEventDutyId() == entry.getValue().getEventDutyId()) {
                staticAgenda.selectedAppointments().clear();
                staticAgenda.selectedAppointments().add(entry.getKey());
            }
        }
    }

    protected void getGroupColorsFromCSS() {
       super.getGroupColorsFromCSS();
    }

    protected void initializeAppointmentGroupsForEventtypes() {
       super.initializeAppointmentGroupsForEventtypes();
    }

    protected void initialzeCalendarSettings() {
        super.initialzeCalendarSettings();
    }

    private void initialzeCalendarView() {

        //set CalenderWeek
        setCalenderWeekLabel();
        addEventTypeEntriesToMenuButton();
        setColorKeyMap();

        //put events to calendar
        List<EventDutyDTO> events = EventScheduleManager.getEventDutyListForCurrentWeek();
        for(EventDutyDTO event : events) {
            addEventDutyToGUI(event);
        }
    }

    protected void setColorKeyMap() {
        super.setColorKeyMap();
        
        
        /* old from master
         colorKeyOpera.setStyle(colorOpera);
        colorKeyConcert.setStyle(colorConcert);
        colorKeyTour.setStyle(colorTour);
        colorKeyHofkapelle.setStyle(colorHofkapelle);
        colorKeyRehearsal.setStyle(colorRehearsal);
        colorKeyNonMusical.setStyle(colorNonMusical);
        mandatoryField.setStyle(PlanchesterConstants.INPUTFIELD_MANDATORY); */
    }

    private void addEventTypeEntriesToMenuButton() {
        addNewConcert = new MenuItem(EventType.Concert.toString());
        addNewOpera = new MenuItem(EventType.Opera.toString());
        addNewTour = new  MenuItem(EventType.Tour.toString());
        addNewHofkapelle = new MenuItem(EventType.Hofkapelle.toString());
        //addNewRehearsal = new MenuItem(EventType.Rehearsal.toString());
        addNewNonMusicalEvent = new MenuItem(EventType.NonMusicalEvent.toString());

        addNewEvent.getItems().add(addNewConcert);
        addNewEvent.getItems().add(addNewOpera);
        addNewEvent.getItems().add(addNewTour);
        addNewEvent.getItems().add(addNewHofkapelle);
        //addNewEvent.getItems().add(addNewRehearsal);
        addNewEvent.getItems().add(addNewNonMusicalEvent);
    }

    private void showEventDetailView() {
        try {
            ObservableList<Agenda.Appointment> appointments = agenda.selectedAppointments();
            if(!appointments.isEmpty()) {
                Agenda.Appointment appointment = appointments.get(0);
                EventDutyDTO eventDutyDTO = getEventForAppointment(appointment);

                if(EventType.Opera.equals(eventDutyDTO.getEventType())) {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource("EditOpera.fxml")));
                    editOpen = true;
                } else if(EventType.Concert.equals(eventDutyDTO.getEventType())) {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource("EditConcert.fxml")));
                    editOpen = true;
                } else if(EventType.Tour.equals(eventDutyDTO.getEventType())) {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource("EditTour.fxml")));
                    editOpen = true;
                } else if(EventType.Hofkapelle.equals(eventDutyDTO.getEventType())) {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource("EditHofkapelle.fxml")));
                    editOpen = true;
                } else if(EventType.NonMusicalEvent.equals(eventDutyDTO.getEventType())) {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource("EditNonMusicalEvent.fxml")));
                    editOpen = true;
                } else if(EventType.Rehearsal.equals(eventDutyDTO.getEventType())) {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource("EditRehearsal.fxml")));
                    editOpen = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setCalenderWeekLabel() {
       super.setCalenderWeekLabel();
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

        addNewConcert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(tryResetSideContent() == null) {
                        agenda.selectedAppointments().clear();
                        selectedAppointment = null;
                        scrollPane.setContent(FXMLLoader.load(getClass().getResource("CreateConcert.fxml")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addNewHofkapelle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(tryResetSideContent() == null) {
                        agenda.selectedAppointments().clear();
                        selectedAppointment = null;
                        scrollPane.setContent(FXMLLoader.load(getClass().getResource("CreateHofkapelle.fxml")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addNewTour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(tryResetSideContent() == null) {
                        agenda.selectedAppointments().clear();
                        selectedAppointment = null;
                        scrollPane.setContent(FXMLLoader.load(getClass().getResource("CreateTour.fxml")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addNewNonMusicalEvent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(tryResetSideContent() == null) {
                        agenda.selectedAppointments().clear();
                        selectedAppointment = null;
                        scrollPane.setContent(FXMLLoader.load(getClass().getResource("CreateNonMusicalEvent.fxml")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML public void publishEventSchedule() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        EventDutyDTO eventDutyDTO = PublishEventSchedule.publish(Year.of(displayedDate.getYear()),displayedDate.getMonth());
        if(eventDutyDTO != null) {
            setSelectedAppointment(eventDutyDTO);
        }
    }

    @FXML public void refresh() {
        removeAllData();

        List<EventDutyDTO> events = EventScheduleManager.getEventDutyListForWeek(staticAgenda.getDisplayedCalendar());
        for(EventDutyDTO event : events) {
            addEventDutyToGUI(event);
        }
    }

    public static void reload() {
        removeAllData();

        List<EventDutyDTO> events = EventScheduleManager.getEventDutyListForWeek(staticAgenda.getDisplayedCalendar());
        for(EventDutyDTO event : events) {
            addEventDutyToGUI(event);
        }
    }

    public static void removeAllData() {
        staticLoadedEventsMap.clear();
        staticAgenda.appointments().clear();
        EventScheduleManager.loadedEventsEnddate = null;
        EventScheduleManager.loadedEventsStartdate = null;
    }
}
