package Presentation.EventSchedule;

import Application.MusicalWorkAdministratonManager;
import Domain.InstrumentationModel;
import Utils.PlanchesterConstants;
import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
            getAllMusicalWorks();
        }

        private void setBackgroundStandardInstrumentation() {
                standardFirstViolin.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardSecondViolin.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardViola.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardVioloncello.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardDoublebass.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardHorn.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardTrumpet.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardTrombone.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardTuba.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardFlute.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardOboe.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardClarinet.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardBasson.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardKettledrum.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardPercussion.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardHarp.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
                standardDescription.setStyle(PlanchesterConstants.INPUTFIELD_NOTEDITABLE);
        }

        public void getAllMusicalWorks() {


                tableAvailable.getColumns().get(0).setCellValueFactory(cellData -> cellData.getValue());

                List<String> musicalWorks = MusicalWorkAdministratonManager.getAllMusicalWorks();
                tableAvailable.getItems().addAll(musicalWorks);
        }

        private class CellData {
                private String name;

                private String getName() {
                        return name;
                }

                private void setName(String s) {
                        name = s;
                }
        }
}