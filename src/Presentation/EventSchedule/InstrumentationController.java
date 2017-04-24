package Presentation.EventSchedule;

import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;


/**
 * Created by Christina on 21.04.2017.
 */
public class InstrumentationController {

        @FXML private ScrollPane scrollPane;

        @FXML private TextField standardFirstViolin;
        @FXML private TextField standardSecondViolin;
        @FXML private TextField standardViola;
        @FXML private TextField standardVioloncello;
        @FXML private TextField standardDoublebass;
        @FXML private TextField standardHorn;
        @FXML private TextField standardTrumpet;
        @FXML private TextField standardTrombone;
        @FXML private TextField standardTuba;
        @FXML private TextField standardFlute;
        @FXML private TextField standardOboe;
        @FXML private TextField standardClarinet;
        @FXML private TextField standardBasson;
        @FXML private TextField standardKettledrum;
        @FXML private TextField standardPercussion;
        @FXML private TextField standardHarp;
        @FXML private TextArea standardDescription;


        @FXML private TextField alternativeFirstViolin;
        @FXML private TextField alternativeSecondViolin;
        @FXML private TextField alternativeViola;
        @FXML private TextField alternativeVioloncello;
        @FXML private TextField alternativeDoublebass;
        @FXML private TextField alternativeHorn;
        @FXML private TextField alternativeTrumpet;
        @FXML private TextField alternativeTrombone;
        @FXML private TextField alternativeTuba;
        @FXML private TextField alternativeFlute;
        @FXML private TextField alternativeOboe;
        @FXML private TextField alternativeClarinet;
        @FXML private TextField alternativeBasson;
        @FXML private TextField alternativeKettledrum;
        @FXML private TextField alternativePercussion;
        @FXML private TextField alternativeHarp;
        @FXML private TextArea alternativeDescription;

        @FXML private TextField eventFirstViolin;
        @FXML private TextField eventSecondViolin;
        @FXML private TextField eventViola;
        @FXML private TextField eventVioloncello;
        @FXML private TextField eventDoublebass;
        @FXML private TextField eventHorn;
        @FXML private TextField eventTrumpet;
        @FXML private TextField eventTrombone;
        @FXML private TextField eventTuba;
        @FXML private TextField eventFlute;
        @FXML private TextField eventOboe;
        @FXML private TextField eventClarinet;
        @FXML private TextField eventBassoon;
        @FXML private TextField eventKettledrum;
        @FXML private TextField eventHarp;
        @FXML private TextField eventPercussion;
        @FXML private TextArea eventDescription;

        @FXML private TableColumn<?, ?> tableAvailableMusicalWorks;
        @FXML private TableColumn<?, ?> tableSelectedMusicalWorks;

        @FXML private TableView<String> tableAvailable;
        @FXML private TableView<?> tableSelected;

        @FXML private Button addToSelected;
        @FXML private Button removeFromSelected;

        @FXML private JFXRadioButton radioButtonStandard;
        @FXML private JFXRadioButton radioButtonAlternative;

        @FXML
        public void initialize() {
            setBackgroundStandardInstrumentation();
        }

        private void setBackgroundStandardInstrumentation() {
                standardFirstViolin.setStyle("-fx-control-inner-background: #eeeeee");
                standardSecondViolin.setStyle("-fx-control-inner-background: #eeeeee");
                standardViola.setStyle("-fx-control-inner-background: #eeeeee");
                standardVioloncello.setStyle("-fx-control-inner-background: #eeeeee");
                standardDoublebass.setStyle("-fx-control-inner-background: #eeeeee");
                standardHorn.setStyle("-fx-control-inner-background: #eeeeee");
                standardTrumpet.setStyle("-fx-control-inner-background: #eeeeee");
                standardTrombone.setStyle("-fx-control-inner-background: #eeeeee");
                standardTuba.setStyle("-fx-control-inner-background: #eeeeee");
                standardFlute.setStyle("-fx-control-inner-background: #eeeeee");
                standardOboe.setStyle("-fx-control-inner-background: #eeeeee");
                standardClarinet.setStyle("-fx-control-inner-background: #eeeeee");
                standardBasson.setStyle("-fx-control-inner-background: #eeeeee");
                standardKettledrum.setStyle("-fx-control-inner-background: #eeeeee");
                standardPercussion.setStyle("-fx-control-inner-background: #eeeeee");
                standardHarp.setStyle("-fx-control-inner-background: #eeeeee");
                standardDescription.setStyle("-fx-control-inner-background: #eeeeee");

        }
}
