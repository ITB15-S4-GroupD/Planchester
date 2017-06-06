package Presentation.EventSchedule;

import Application.AccountAdministrationManager;
import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Persistence.Entities.AccountEntity;
import Utils.Enum.EventType;
import Utils.Enum.RequestType;
import Utils.Enum.RequestTypeGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class EditRequestsController {
    public static Stage stage;
    public static int month;
    public static int year;

    @FXML private TableView<RequestEntry> table;
    static TableView<RequestEntry> staticTable;

    @FXML
    public void initialize() {
        staticTable = table;
        table.setEditable(true);
        createColumns();
        loadEventsForMonth();
    }

    public static boolean isEditable(Integer id){
        List<RequestEntry> requestEntries =  staticTable.getItems().stream().filter(p -> p.getEventDutyDTO().getEventDutyId().equals(id)).collect(Collectors.toList());
        if(!requestEntries.isEmpty() && requestEntries.size() > 0) {
            for(RequestEntry reqEntry : requestEntries) {
                if(reqEntry.getRequestType().equals(RequestTypeGUI.Absence)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void setReahearsalsAbsence(Integer id) {
        List<RequestEntry> requestEntries =  staticTable.getItems().stream().filter(p -> p.getEventDutyDTO().getRehearsalFor() != null && p.getEventDutyDTO().getRehearsalFor().equals(id)).collect(Collectors.toList());
        if(!requestEntries.isEmpty()) {
            for(RequestEntry reqEntry : requestEntries) {
                reqEntry.setRequestType(RequestTypeGUI.Absence);
                reqEntry.requestTypeProperty().set(RequestTypeGUI.Absence);
                reqEntry.setEdited(true);
            }
        }
    }

    @FXML
    public void save() {
        List<RequestEntry> editedEntries = table.getItems().stream().filter(p -> p.edited == true).collect(Collectors.toList());

        for(RequestEntry requestEntry : editedEntries) {

            RequestType requestType = null;
            if(requestEntry.getRequestType().equals(RequestTypeGUI.Absence)) {
                requestType = RequestType.Leave_of_absence;
            } else if(requestEntry.getRequestType().equals(RequestTypeGUI.Playrequest)) {
                requestType = RequestType.Playrequest;
            }
            AccountEntity accountEntity = AccountAdministrationManager.getInstance().getLoggedInAccount();
            EventScheduleManager.updateRequest(requestEntry.eventDutyDTO, requestType, AccountAdministrationManager.getInstance().getLoggedInAccount(), requestEntry.getRequestDescription().getText());
        }
        stage.fireEvent(
            new WindowEvent(
                stage,
                WindowEvent.WINDOW_CLOSE_REQUEST
            )
        );
    }

    @FXML
    public void cancel() {
        stage.fireEvent(
                new WindowEvent(
                        stage,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
    }

    private void loadEventsForMonth() {
        table.getItems().clear();

        List<EventDutyDTO> eventDutyDTOList = EventScheduleManager.getAvailableEventsForRequestInMonth(Month.of(month), Year.of(year));

        List<EventDutyDTO> eventList = new ArrayList<>();
        List<EventDutyDTO> rehearsalList = new ArrayList<>();

        for(EventDutyDTO eventDutyDTO : eventDutyDTOList) {
            if(eventDutyDTO.getEventType().equals(EventType.Rehearsal)) {
                rehearsalList.add(eventDutyDTO);
            } else {
                eventList.add(eventDutyDTO);
            }
        }

        eventList.sort((o1, o2) -> o1.getStartTime().after(o2.getStartTime()) == true ? 1 : -1);

        ObservableList<RequestEntry> requestEntries = FXCollections.observableArrayList();

        boolean gray = true;

        for(EventDutyDTO eventDutyDTO : eventList){
            String dateTime = null;
            if(eventDutyDTO.getEventType().equals(EventType.Tour)) {
                dateTime = eventDutyDTO.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                + " - " +
                                eventDutyDTO.getEndTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                ;
            } else {
                dateTime = eventDutyDTO.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                + ": " +
                                eventDutyDTO.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                                + " - " +
                                eventDutyDTO.getEndTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                ;
            }

            RequestType requestType = EventScheduleManager.getRequestForEventAndLoggedInUser(eventDutyDTO);
            RequestTypeGUI requestTypeGUI = null;
            if(requestType == null){
                requestTypeGUI = RequestTypeGUI.None;
            } else if(requestType.equals(RequestType.Leave_of_absence)){
                requestTypeGUI = RequestTypeGUI.Absence;
            } else if(requestType.equals(RequestType.Playrequest)){
                requestTypeGUI = RequestTypeGUI.Playrequest;
            }

            RequestEntry requestEntry = new RequestEntry(eventDutyDTO.getEventType().toString(),
                    eventDutyDTO.getName(),
                    dateTime,
                    eventDutyDTO.getLocation(),
                    eventDutyDTO.getConductor(),
                    requestTypeGUI,
                    EventScheduleManager.getRequestDescriptionForEventAndLoggedInUser(eventDutyDTO),
                    eventDutyDTO

            );

            requestEntry.setGray(gray);
            requestEntries.add(requestEntry);

            // insert all rehearsals
            List<EventDutyDTO> rehearsals = rehearsalList.stream().filter(p ->
                    p.getRehearsalFor() != null && p.getRehearsalFor().equals(eventDutyDTO.getEventDutyId())).collect(Collectors.toList());


            for(EventDutyDTO rehearsal : rehearsals) {
                dateTime = rehearsal.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        + ": " +
                        rehearsal.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        + " - " +
                        rehearsal.getEndTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                ;

                requestType = EventScheduleManager.getRequestForEventAndLoggedInUser(rehearsal);
                requestTypeGUI = null;
                if(requestType == null){
                    requestTypeGUI = RequestTypeGUI.None;
                } else if(requestType.equals(RequestType.Leave_of_absence)){
                    requestTypeGUI = RequestTypeGUI.Absence;
                } else if(requestType.equals(RequestType.Playrequest)){
                    requestTypeGUI = RequestTypeGUI.Playrequest;
                }

                requestEntry = new RequestEntry(rehearsal.getEventType().toString(),
                        rehearsal.getName(),
                        dateTime,
                        rehearsal.getLocation(),
                        rehearsal.getConductor(),
                        requestTypeGUI,
                        EventScheduleManager.getRequestDescriptionForEventAndLoggedInUser(rehearsal),
                        rehearsal

                );

                requestEntry.setGray(gray);
                requestEntries.add(requestEntry);
            }

            if(gray == true) {
                gray = false;
            } else {
                gray = true;
            }
        }
        table.setItems(requestEntries);
    }

    private void createColumns() {
        TableColumn<RequestEntry, String> eventTypeCol = new TableColumn("Type");
        eventTypeCol.setMinWidth(100);
        eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("eventType"));
        eventTypeCol.setSortable(false);

        TableColumn<RequestEntry, String> eventNameCol = new TableColumn("Name");
        eventNameCol.setMinWidth(100);
        eventNameCol.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventNameCol.setSortable(false);

        TableColumn<RequestEntry, String> eventDateTimeCol = new TableColumn("Time");
        eventDateTimeCol.setMinWidth(200);
        eventDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("eventDateTime"));
        eventDateTimeCol.setSortable(false);

        TableColumn<RequestEntry, String> eventLocationCol = new TableColumn("Location");
        eventLocationCol.setMinWidth(100);
        eventLocationCol.setCellValueFactory(new PropertyValueFactory<>("eventLocation"));
        eventLocationCol.setSortable(false);

        TableColumn<RequestEntry, String> eventConductorCol = new TableColumn("Conductor");
        eventConductorCol.setMinWidth(100);
        eventConductorCol.setCellValueFactory(new PropertyValueFactory<>("eventConductor"));
        eventConductorCol.setSortable(false);

        TableColumn<RequestEntry, RequestTypeGUI> requestTypeCol = new TableColumn("Request");
        requestTypeCol.setCellFactory((param) -> new RequestRadioButtonCell<>(EnumSet.allOf(RequestTypeGUI.class)));
        requestTypeCol.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        requestTypeCol.setSortable(false);

        TableColumn<RequestEntry, TextField> eventDescriptionCol = new TableColumn("Description");
        eventDescriptionCol.setMinWidth(300);
        eventDescriptionCol.setCellValueFactory(new PropertyValueFactory<RequestEntry, TextField>("requestDescription"));
        eventDescriptionCol.setResizable(false);
        eventDescriptionCol.setSortable(false);

        table.getColumns().addAll(eventTypeCol, eventNameCol, eventDateTimeCol, eventLocationCol, eventConductorCol, requestTypeCol, eventDescriptionCol);
    }
}