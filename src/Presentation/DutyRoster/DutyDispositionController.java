package Presentation.DutyRoster;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by Christina on 11.05.2017.
 */
public class DutyDispositionController {

    @FXML private Label table1Label;
    @FXML private Label label2Table;
    @FXML private Label label3Table;
    @FXML private Label lable4Table;
    @FXML private TableView<String> table1;
    @FXML private TableColumn<String, String> tableColumn1;
    @FXML private TableView<String> table2;
    @FXML private TableColumn<String, String> tableColumn2;
    @FXML private TableView<String> table3;
    @FXML private TableColumn<String, String> tableColumn3;
    @FXML private TableView<String> table4;
    @FXML private TableColumn<String, String> tableColumn4;

    @FXML public void initialize() {
        tableColumn1.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        tableColumn2.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        tableColumn3.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        tableColumn4.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
    }
}