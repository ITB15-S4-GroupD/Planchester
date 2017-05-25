package Presentation.EventSchedule;

import Application.AccountAdministrationManager;
import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Utils.Enum.EventType;
import Utils.Enum.RequestType;
import Utils.Enum.RequestTypeGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class EditRequestsController {
    public static Stage stage;
    public static int month;
    public static int year;

    @FXML private TableView<RequestEntry> table;
    List<RequestEntry> initWishes;

    @FXML
    public void initialize() {
        table.setEditable(true);
        createColumns();
        loadEventsForMonth();
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
            EventScheduleManager.updateWish(requestEntry.eventDutyDTO, requestType, AccountAdministrationManager.getInstance().getLoggedInAccount(), requestEntry.getRequestDescription().getText());
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

        List<EventDutyDTO> eventDutyDTOList = EventScheduleManager.getAvailableEventsForWishInMonth(Month.of(month), Year.of(year));
        ObservableList<RequestEntry> wishEntries = FXCollections.observableArrayList();

        for(EventDutyDTO eventDutyDTO : eventDutyDTOList){
            String dateTime = null;
            if(eventDutyDTO.getEventType().equals(EventType.Tour)) {
                dateTime = eventDutyDTO.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                + " - " +
                                eventDutyDTO.getEndTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                ;
            } else {
                dateTime = eventDutyDTO.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                + ": " +
                                eventDutyDTO.getStartTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("hh:mm"))
                                + " - " +
                                eventDutyDTO.getEndTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("hh:mm"))
                ;
            }

            RequestType requestType = EventScheduleManager.getWishForEventAndLoggedInUser(eventDutyDTO);
            RequestTypeGUI requestTypeGUI = null;
            if(requestType == null){
                requestTypeGUI = RequestTypeGUI.None;
            } else if(requestType.equals(RequestType.Leave_of_absence)){
                requestTypeGUI = RequestTypeGUI.Absence;
            } else if(requestType.equals(RequestType.Playrequest)){
                requestTypeGUI = RequestTypeGUI.Playrequest;
            }

            RequestEntry wishEntry = new RequestEntry(eventDutyDTO.getEventType().toString(),
                    eventDutyDTO.getName(),
                    dateTime,
                    eventDutyDTO.getLocation(),
                    eventDutyDTO.getConductor(),
                    requestTypeGUI,
                    EventScheduleManager.getWishDescriptionForEventAndLoggedInUser(eventDutyDTO),
                    eventDutyDTO

            );
            wishEntries.add(wishEntry);
        }
        initWishes = wishEntries;
        table.setItems(wishEntries);
    }

    private void createColumns() {
        TableColumn<RequestEntry, String> eventTypeCol = new TableColumn("Type");
        eventTypeCol.setMinWidth(100);
        eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("eventType"));

        TableColumn<RequestEntry, String> eventNameCol = new TableColumn("Name");
        eventNameCol.setMinWidth(100);
        eventNameCol.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        TableColumn<RequestEntry, String> eventDateTimeCol = new TableColumn("Time");
        eventDateTimeCol.setMinWidth(200);
        eventDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("eventDateTime"));

        TableColumn<RequestEntry, String> eventLocationCol = new TableColumn("Location");
        eventLocationCol.setMinWidth(100);
        eventLocationCol.setCellValueFactory(new PropertyValueFactory<>("eventLocation"));

        TableColumn<RequestEntry, String> eventConductorCol = new TableColumn("Conductor");
        eventConductorCol.setMinWidth(100);
        eventConductorCol.setCellValueFactory(new PropertyValueFactory<>("eventConductor"));

        TableColumn<RequestEntry, RequestTypeGUI> requestTypeCol = new TableColumn("Request");
        requestTypeCol.setCellFactory((param) -> new RequestRadioButtonCell<>(EnumSet.allOf(RequestTypeGUI.class)));
        requestTypeCol.setCellValueFactory(new PropertyValueFactory<>("requestType"));

        TableColumn<RequestEntry, TextField> eventDescriptionCol = new TableColumn("Description");
        eventDescriptionCol.setMinWidth(300);
        eventDescriptionCol.setCellValueFactory(new PropertyValueFactory<RequestEntry, TextField>("requestDescription"));

        table.getColumns().addAll(eventTypeCol, eventNameCol, eventDateTimeCol, eventLocationCol, eventConductorCol, requestTypeCol, eventDescriptionCol);
    }
}