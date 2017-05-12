package Presentation.DutyRoster;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

import java.io.IOException;


/**
 * Created by Christina on 11.05.2017.
 */
public class ShowDutyController {

    @FXML private Label sectionLabel;
    @FXML private Tab detailsTab;
    @FXML private Tab dispositionTab;

    @FXML
    public void initialize() throws IOException {
        detailsTab.setContent(FXMLLoader.load(getClass().getResource("DutyDetail.fxml")));
        dispositionTab.setContent(FXMLLoader.load(getClass().getResource("DutyDisposition.fxml")));
    }


}
