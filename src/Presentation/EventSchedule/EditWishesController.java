package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Utils.Enum.EventType;
import Utils.Enum.RequestType;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.misc.Request;

import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;

public class EditWishesController {
    public static Stage stage;
    public static int month;
    public static int year;

    @FXML private TableView<WishEntry> table;

    @FXML
    public void initialize() {
        createColumns();
        loadEventsForMonth();
    }

    public void loadEventsForMonth() {
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


            WishEntry wishEntry = new WishEntry(eventDutyDTO.getEventType().toString(),
                    eventDutyDTO.getName(),
                    dateTime,
                    eventDutyDTO.getLocation(),
                    eventDutyDTO.getConductor(),
                    RequestType.Leave_of_absence

            );
            wishEntries.add(wishEntry);
        }

        table.setItems(wishEntries);
    }

    private void createColumns() {
        TableColumn<WishEntry, String> eventTypeCol = new TableColumn("Type");
        eventTypeCol.setMinWidth(100);
        eventTypeCol.setCellValueFactory(
                new PropertyValueFactory<>("eventType"));

        TableColumn<WishEntry, String> eventNameCol = new TableColumn("Name");
        eventNameCol.setMinWidth(100);
        eventNameCol.setCellValueFactory(
                new PropertyValueFactory<>("eventName"));

        TableColumn<WishEntry, String> eventDateTimeCol = new TableColumn("Time");
        eventDateTimeCol.setMinWidth(100);
        eventDateTimeCol.setCellValueFactory(
                new PropertyValueFactory<>("eventDateTime"));

        TableColumn<WishEntry, String> eventLocationCol = new TableColumn("Location");
        eventLocationCol.setMinWidth(100);
        eventLocationCol.setCellValueFactory(
                new PropertyValueFactory<>("eventLocation"));

        TableColumn<WishEntry, String> eventConductorCol = new TableColumn("Conductor");
        eventConductorCol.setMinWidth(100);
        eventConductorCol.setCellValueFactory(
                new PropertyValueFactory<>("eventConductor"));

        TableColumn<WishEntry, RequestType> requestTypeColumn = new TableColumn("Participation");
        requestTypeColumn.setCellFactory((param) -> new WishRadioButtonCell<>(EnumSet.allOf(RequestType.class)));
        requestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("participation"));
        requestTypeColumn.setOnEditCommit(
                t -> (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRequestType(t.getNewValue())
        );

        table.getColumns().addAll(eventTypeCol, eventNameCol, eventDateTimeCol, eventLocationCol, eventConductorCol, requestTypeColumn );
    }

    @FXML
    public void save() {
        // TODO save entered data, show error message if someshing is missing or fails
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
}