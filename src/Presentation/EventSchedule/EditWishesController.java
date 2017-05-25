package Presentation.EventSchedule;

import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import Utils.Enum.RequestType;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.Month;
import java.time.Year;
import java.util.EnumSet;
import java.util.List;

public class EditWishesController {
    public static Stage stage;
    public static int month;
    public static int year;

    @FXML private TableView<String> table;
    @FXML private TableColumn<String, String> eventColumn;
    @FXML private TableColumn<EventDutyDTO, RequestType> wishColumn;

    @FXML
    public void initialize() {
        eventColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        wishColumn.setCellFactory((param) -> new WishRadioButtonCell<>(EnumSet.allOf(RequestType.class)));
        loadEventsForMonth();
    }

    public void loadEventsForMonth() {
        // TODO implement
        List<EventDutyDTO> eventDutyDTOList = EventScheduleManager.getAvailableEventsForWishInMonth(Month.of(month), Year.of(year));
        for(EventDutyDTO eventDutyDTO : eventDutyDTOList){
            table.getItems().add(eventDutyDTO.getName());
        }
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