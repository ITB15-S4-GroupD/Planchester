package Presentation.EventSchedule;

import Application.AccountAdministrationManager;
import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Utils.Enum.EventType;
import Utils.Enum.RequestType;
import Utils.Enum.RequestTypeGUI;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.misc.Request;

import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class EditWishesController {
    public static Stage stage;
    public static int month;
    public static int year;

    @FXML private TableView<WishEntry> table;
    List<WishEntry> initWishes;

    @FXML
    public void initialize() {
        table.setEditable(true);
        createColumns();
        loadEventsForMonth();
    }

    @FXML
    public void save() {
        List<WishEntry> editedEntries = table.getItems().stream().filter(p -> p.edited == true).collect(Collectors.toList());

        for(WishEntry wishEntry : editedEntries) {

            RequestType requestType = null;
            if(wishEntry.getRequestType().equals(RequestTypeGUI.Absence)) {
                requestType = RequestType.Leave_of_absence;
            } else if(wishEntry.getRequestType().equals(RequestTypeGUI.Playrequest)) {
                requestType = RequestType.Playrequest;
            }
            EventScheduleManager.updateWish(wishEntry.eventDutyDTO, requestType, AccountAdministrationManager.getInstance().getLoggedInAccount(), wishEntry.getRequestDescription().getText());
        }

        // TODO save entered data, show error message if someshing is missing or fails,
        // TODO show message if wishes have been successfully saved
        // TODO implement saving data in eventschedulemanager
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
        ObservableList<WishEntry> wishEntries = FXCollections.observableArrayList();

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

            WishEntry wishEntry = new WishEntry(eventDutyDTO.getEventType().toString(),
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
        TableColumn<WishEntry, String> eventTypeCol = new TableColumn("Type");
        eventTypeCol.setMinWidth(100);
        eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("eventType"));

        TableColumn<WishEntry, String> eventNameCol = new TableColumn("Name");
        eventNameCol.setMinWidth(100);
        eventNameCol.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        TableColumn<WishEntry, String> eventDateTimeCol = new TableColumn("Time");
        eventDateTimeCol.setMinWidth(200);
        eventDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("eventDateTime"));

        TableColumn<WishEntry, String> eventLocationCol = new TableColumn("Location");
        eventLocationCol.setMinWidth(100);
        eventLocationCol.setCellValueFactory(new PropertyValueFactory<>("eventLocation"));

        TableColumn<WishEntry, String> eventConductorCol = new TableColumn("Conductor");
        eventConductorCol.setMinWidth(100);
        eventConductorCol.setCellValueFactory(new PropertyValueFactory<>("eventConductor"));

        TableColumn<WishEntry, RequestTypeGUI> requestTypeCol = new TableColumn("Request");
        requestTypeCol.setCellFactory((param) -> new WishRadioButtonCell<>(EnumSet.allOf(RequestTypeGUI.class)));
        requestTypeCol.setCellValueFactory(new PropertyValueFactory<>("requestType"));

        TableColumn<WishEntry, TextField> eventDescriptionCol = new TableColumn("Description");
        eventDescriptionCol.setMinWidth(300);
        eventDescriptionCol.setCellValueFactory(new PropertyValueFactory<WishEntry, TextField>("requestDescription"));

        table.getColumns().addAll(eventTypeCol, eventNameCol, eventDateTimeCol, eventLocationCol, eventConductorCol, requestTypeCol, eventDescriptionCol);
    }
}