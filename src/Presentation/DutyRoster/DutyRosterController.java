package Presentation.DutyRoster;

import Application.*;
import Application.DTO.EventDutyDTO;
import Application.DTO.MusicalWorkDTO;
import Application.DutyRosterManager;
import Domain.Interfaces.IInstrumentation;
import Domain.Models.Permission;
import Presentation.CalenderController;
import Presentation.PlanchesterGUI;
import Utils.DateHelper;
import Utils.Enum.*;
import Utils.MessageHelper;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import jfxtras.scene.control.agenda.Agenda;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
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
    @FXML private Label dutyRosterLabel;

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
            showDutyDetailView();
        });


        Permission permission = AccountAdministrationManager.getInstance().getUserPermissions();
        if(AccountAdministrationManager.getInstance().getSectionType() != null) {
            dutyRosterLabel.setText("Duty Roster: " + AccountAdministrationManager.getInstance().getSectionType());
        }
        publishDutyRoster.setVisible(permission.isPublishDutyRoster());
    }

    private void showDutyDetailView() {
        try {
            // fill content into side panel
            scrollPane.setContent(FXMLLoader.load(getClass().getResource("ShowDuty.fxml")));
            EventDutyDTO eventDutyDTO = getEventForAppointment(selectedAppointment);
            SectionType sectionType = AccountAdministrationManager.getInstance().getSectionType();

            // if tour, load other fxml into tabpane
            if(eventDutyDTO.getEventType().equals(EventType.Tour)) {
                TabPane tabPane = (TabPane)PlanchesterGUI.scene.lookup("#tabPane");
                tabPane.getTabs().get(0).setContent(FXMLLoader.load(getClass().getResource("DutyDetailTour.fxml")));
            }

            // fill in event details
            Label eventName = (Label)PlanchesterGUI.scene.lookup("#dutyDetailEventName");
            eventName.setText(eventDutyDTO.getName());
            Label eventDescription = (Label)PlanchesterGUI.scene.lookup("#dutyDetailEventDescription");
            eventDescription.setText(eventDutyDTO.getDescription());

            if(!eventDutyDTO.getEventType().equals(EventType.Tour)) {
                Label eventDate = (Label)PlanchesterGUI.scene.lookup("#dutyDetailEventDate");
                eventDate.setText(eventDutyDTO.getStartTime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                Label eventStartTime = (Label)PlanchesterGUI.scene.lookup("#dutyDetailEventStartTime");
                eventStartTime.setText(eventDutyDTO.getStartTime().toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm")));
                Label eventEndTime = (Label)PlanchesterGUI.scene.lookup("#dutyDetailEventEndTime");
                eventEndTime.setText(eventDutyDTO.getEndTime().toLocalDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm")));
            } else {
                Label eventStartDate = (Label)PlanchesterGUI.scene.lookup("#dutyDetailEventStartDate");
                eventStartDate.setText(eventDutyDTO.getStartTime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                Label eventEndDate = (Label)PlanchesterGUI.scene.lookup("#dutyDetailEventEndDate");
                eventEndDate.setText(eventDutyDTO.getEndTime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            Label eventLocation = (Label) PlanchesterGUI.scene.lookup("#dutyDetailEventLocation");
            eventLocation.setText(eventDutyDTO.getLocation());
            Label eventConductor = (Label) PlanchesterGUI.scene.lookup("#dutyDetailEventConductor");
            eventConductor.setText(eventDutyDTO.getConductor());
            Label eventPoints = (Label) PlanchesterGUI.scene.lookup("#dutyDetailEventPoints");
            eventPoints.setText(eventDutyDTO.getPoints().toString());

            // fill in musical works
            if(eventDutyDTO.getMusicalWorks() != null) {
                TableView eventMusicalWorkTable = (TableView) PlanchesterGUI.scene.lookup("#dutyDetailEventMusicalWorkTable");
                for (MusicalWorkDTO musicalWorkDTO : eventDutyDTO.getMusicalWorks()) {
                    eventMusicalWorkTable.getItems().add(musicalWorkDTO.getName());
                }
            }

            // fill in disposition details
            IInstrumentation instrumentation = eventDutyDTO.getInstrumentation();
            int amountOfTables = 0;

            String label1 = null;
            String label2 = null;
            String label3 = null;
            String label4 = null;

            int required1 = 0;
            int required2 = 0;
            int required3 = 0;
            int required4 = 0;

            List<String> entries1 = new ArrayList<>();
            List<String> entries2 = new ArrayList<>();
            List<String> entries3 = new ArrayList<>();
            List<String> entries4 = new ArrayList<>();

            // get data from section type
            switch (sectionType) {
                case Violin1:
                    amountOfTables = 1;
                    required1 = instrumentation.getFirstViolin();
                    label1 = "First Violin";
                    entries1 = getAdressed(eventDutyDTO, label1);
                    break;
                case Violin2:
                    amountOfTables = 1;
                    required1 = instrumentation.getSecondViolin();
                    label1 ="Second Violin";
                    entries1 = getAdressed(eventDutyDTO, label1);
                    break;
                case Viola:
                    amountOfTables = 1;
                    required1 = instrumentation.getViola();
                    label1 ="Viola";
                    entries1 = getAdressed(eventDutyDTO, label1);
                    break;
                case Violoncello:
                    amountOfTables = 1;
                    required1 = instrumentation.getVioloncello();
                    label1 ="Violoncello";
                    entries1 = getAdressed(eventDutyDTO, label1);
                    break;
                case Doublebass:
                    amountOfTables = 1;
                    required1 = instrumentation.getDoublebass();
                    label1 ="Double Bass";
                    entries1 = getAdressed(eventDutyDTO, label1);
                    break;

                case Woodwind:
                    amountOfTables = 4;

                    label1 = "Bassoon";
                    required1 = instrumentation.getBasson();
                    entries1 = getAdressed(eventDutyDTO, label1);

                    label2 = "Clarinet";
                    required2 = instrumentation.getClarinet();
                    entries2 = getAdressed(eventDutyDTO, label2);

                    label3 = "Flute";
                    required3 = instrumentation.getFlute();
                    entries3 = getAdressed(eventDutyDTO, label3);

                    label4 = "Oboe";
                    required4 = instrumentation.getOboe();
                    entries4 = getAdressed(eventDutyDTO, label4);
                    break;

                case Brass:
                    amountOfTables = 4;

                    label1 = "French Horn";
                    required1 = instrumentation.getHorn();
                    entries1 = getAdressed(eventDutyDTO, label1);

                    label2 = "Trombone";
                    required2 = instrumentation.getTrombone();
                    entries2 = getAdressed(eventDutyDTO, label2);

                    label3 = "Trumpet";
                    required3 = instrumentation.getTrumpet();
                    entries3 = getAdressed(eventDutyDTO, label3);

                    label4 = "Tuba";
                    required4 = instrumentation.getTube();
                    entries4 = getAdressed(eventDutyDTO, label4);
                    break;

                case Percussion:
                    amountOfTables = 3;

                    label1 = "Percussion";
                    required1 = instrumentation.getPercussion();
                    entries1 = getAdressed(eventDutyDTO, label1);

                    label2 = "Harp";
                    required2 = instrumentation.getHarp();
                    entries2 = getAdressed(eventDutyDTO, label2);

                    label3 = "Kettledrum";
                    required3 = instrumentation.getKettledrum();
                    entries3 = getAdressed(eventDutyDTO, label3);
                    break;
            }

            // get elements from scene
            GridPane dispositionGridPane = (GridPane)PlanchesterGUI.scene.lookup("#dispositionGridPane");

            TableView table1 = (TableView)PlanchesterGUI.scene.lookup("#table1");
            TableView table2 = (TableView)PlanchesterGUI.scene.lookup("#table2");
            TableView table3 = (TableView)PlanchesterGUI.scene.lookup("#table3");
            TableView table4 = (TableView)PlanchesterGUI.scene.lookup("#table4");

            Label table1Label = (Label)PlanchesterGUI.scene.lookup("#table1Label");
            Label table2Label = (Label)PlanchesterGUI.scene.lookup("#table2Label");
            Label table3Label = (Label)PlanchesterGUI.scene.lookup("#table3Label");
            Label table4Label = (Label)PlanchesterGUI.scene.lookup("#table4Label");

            // remove unneeded tables
            if(amountOfTables == 1) {
                dispositionGridPane = removeRowFromGridPane(2, dispositionGridPane);
                dispositionGridPane = removeRowFromGridPane(3, dispositionGridPane);
                dispositionGridPane = removeRowFromGridPane(4, dispositionGridPane);
            } else if(amountOfTables == 3) {
                dispositionGridPane = removeRowFromGridPane(4, dispositionGridPane);
            }

            // add data into tables
            if(amountOfTables > 0) {
                insertDataIntoTable(table1, label1, table1Label, entries1, required1);
            }
            if(amountOfTables > 2) {
                insertDataIntoTable(table2, label2, table2Label, entries2, required2);
                insertDataIntoTable(table3, label3, table3Label, entries3, required3);
            }
            if(amountOfTables > 3) {
                insertDataIntoTable(table4, label4, table4Label, entries4, required4);
            }

            // spare musician
            Label labelSpareMusician = (Label)PlanchesterGUI.scene.lookup("#labelSpareMusician");
            String spare = getSpare(eventDutyDTO, sectionType);

            if(spare != null) {
                labelSpareMusician.setText(spare);
            } else {
                labelSpareMusician.setText("Missing");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertDataIntoTable(TableView table, String labelText, Label label, List<String> entries, int required) {
        label.setText(labelText);
        for(String s : entries) {
            table.getItems().add(s);
        }
        for(int i = required - entries.size(); i > 0; i--) {
            table.getItems().add("Missing");
        }
    }

    private GridPane removeRowFromGridPane(int row, GridPane gridPane ) {
        Set<Node> deleteNodes = new HashSet<>();
        for (Node child : gridPane.getChildren()) {
            // get index from child
            Integer rowIndex = GridPane.getRowIndex(child);

            // handle null values for index=0
            int r = rowIndex == null ? 0 : rowIndex;

            if (r == row) {
                // collect matching rows for deletion
                deleteNodes.add(child);
            }
        }
        gridPane.getChildren().removeAll(deleteNodes);
        return gridPane;
    }

    private List<String> getAdressed(EventDutyDTO eventDutyDTO, String partType) {
        return DutyRoster.getAdressedMusicicansForEventAndPart(eventDutyDTO, partType, DutyDispositionStatus.Normal);
    }

    private String getSpare(EventDutyDTO eventDutyDTO, SectionType sectionType) {
        return DutyRoster.getSpareMusicicansForEventAndSectionType(eventDutyDTO, sectionType);
    }

    public static EventDutyDTO getEventForAppointment(Agenda.Appointment appointment) {
        return staticLoadedEventsMap.get(appointment);
    }

    private void addMonthsEntriesToMenuButton() {
        if(AccountAdministrationManager.getInstance().getUserPermissions().isPublishDutyRoster()) {
            publishDutyRoster.getItems().clear();
            List<EventDutyDTO> unpublishedEvents = DutyRosterManager.getAllUnpublishedMonths();
            if(unpublishedEvents == null) {
                return;
            }
            List<String> months = new ArrayList<>();
            EventHandler<ActionEvent> action = chooseMonthToPublish();
            Calendar cal = Calendar.getInstance();
            for (EventDutyDTO unpublishedEvent : unpublishedEvents) {
                cal.setTimeInMillis(unpublishedEvent.getStartTime().getTime());
                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);
                String monthYear;
                if(month < 10) {
                    monthYear = String.valueOf("0" + month + " | " + year);
                } else {
                    monthYear = String.valueOf(month + " | " + year);
                }
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
            Collections.sort(months);
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
        EventDutyDTO eventDutyDTO = dutyRoster.validateMonth(Year.of(year), Month.of(month));

        if (eventDutyDTO == null) {
            PublishDutyRoster.publish(Year.of(year), Month.of(month));
            publishDutyRoster.getItems().remove(item);
            refresh();
        } else {
            agenda.setDisplayedLocalDateTime(eventDutyDTO.getStartTime().toLocalDateTime());
            refresh();
            setSelectedAppointment(eventDutyDTO);
        }
    }

    public static void setSelectedAppointment(EventDutyDTO eventDutyDTO) {
        for (Map.Entry<Agenda.Appointment, EventDutyDTO> entry : staticLoadedEventsMap.entrySet()) {
            if (eventDutyDTO.getEventDutyId().equals(entry.getValue().getEventDutyId())) {
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
        if(events == null || events.isEmpty()) {
            return;
        }
        for(EventDutyDTO event : events) {
            addDutyRosterToGUI(event);
        }
    }
}
