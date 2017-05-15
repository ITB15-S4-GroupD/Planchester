package Presentation.DutyRoster;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by Christina on 11.05.2017.
 */
public class DutyDetailController {

    @FXML private TableView<String> musicalWorkTable;
    @FXML private TableColumn<String, String> selectedMusicalWorks;

    @FXML private Label eventNameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label dateLabel;
    @FXML private Label endTimeLabel;
    @FXML private Label locationLabel;
    @FXML private Label conductorLabel;
    @FXML private Label pointsLabel;
    @FXML private Label startTimeLabel;

    @FXML public void initialize() {
        selectedMusicalWorks.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
    }
}