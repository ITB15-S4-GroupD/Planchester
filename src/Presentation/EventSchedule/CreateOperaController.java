package Presentation.EventSchedule;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jfxtras.scene.control.LocalTimePicker;

import java.time.LocalDate;
import java.time.LocalTime;
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
        System.out.println("ICH BIN DA");

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
        String warning=" ";

        boolean validate = true;

         if(txtName==null){
             validate=false;
             warning = warning+"Name missing\n";
         }
         if(txtDescription==null){
             validate=false;
             warning = warning+"Description missing\n";
         }

         LocalDate date = pckDate.getValue();
         if(pckDate==null||date.isAfter(LocalDate.now())){
             validate=false;
             warning=warning+"Date wrong\n";
         }

        LocalTime start = pckStartTime.getLocalTime();

         if(txtLocation==null){
             validate=false;
             warning=warning+"Location missing\n";
         }

         if(txtConductor==null){
             validate=false;
             warning=warning+"Conductor missing\n";
         }
         if(txtPoints==null){
             validate=false;
             warning=warning+"Points missing\n";
         }

        if(validate=true) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setHeaderText("Daten werden gespeichert: ");
            dialog.setContentText(collectedData);
            dialog.setResizable(true);
            dialog.getDialogPane().setPrefSize(350, 500);
            dialog.showAndWait();
            final Optional<ButtonType> result = dialog.showAndWait();

        }else{
            Alert problem = new Alert(Alert.AlertType.ERROR);
            problem.setHeaderText("Fehler ");
            problem.setContentText(warning);
            problem.setResizable(true);
            problem.getDialogPane().setPrefSize(350, 500);
            problem.showAndWait();
            final Optional<ButtonType> result = problem.showAndWait();
        }
    }

}
