package Presentation.EventSchedule;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import jfxtras.scene.control.LocalTimePicker;
import java.util.Optional;

/**
 * Created by Ina on 08.04.2017.
 */
public class CreateOperaController {

    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;
    @FXML private DatePicker pckDate;
    @FXML private LocalTimePicker pckStartTime;
    @FXML private LocalTimePicker pckEndTime;
    @FXML private TextField txtLocation;
    @FXML private ChoiceBox<String> choiceWork;
    @FXML private TextField txtConductor;
    @FXML private TextField txtPoints;


    @FXML
    public void initialize() {

        //IODO fill Works from DB into choiceWork

    }

    @FXML
    private void saveNewOperaDuty(){

    //i: Test

        String collectedData = "Event Name: " + txtName.getText() + "\n" +
                "Description: " + txtDescription.getText() + "\n" +
                "Date: " + pckDate.getValue() + "\n" +
                "Start Time: " + pckStartTime.getLocalTime().toString() + "\n" +
                "End Time: " + pckEndTime.getLocalTime().toString() + "\n" +
                "Location: " + txtLocation.getText() + "\n" +
                "Description: " + txtDescription.getText() + "\n" +
                "Work: " + choiceWork.getValue() + "\n" +
                "Conductor: " + txtConductor.getText() + "\n" +
                "Points: " + txtPoints.getText() + "\n";


        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setHeaderText( "Daten werden gespeichert: " );
        dialog.setContentText(collectedData);
        dialog.setResizable(true);
        dialog.getDialogPane().setPrefSize(350, 500);
        dialog.showAndWait();
        final Optional<ButtonType> result = dialog.showAndWait();

    }

}
