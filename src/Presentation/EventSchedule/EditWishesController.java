package Presentation.EventSchedule;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EditWishesController {

    public static Stage stage;
    public static int month;
    public static int year;

    @FXML
    public void initialize() {
        loadEventsForMonth();
    }

    public void loadEventsForMonth() {
        // TODO implement
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