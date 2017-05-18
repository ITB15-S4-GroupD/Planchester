package Presentation.EventSchedule;

import Application.DTO.InstrumentationDTO;
import Application.DTO.MusicalWorkDTO;
import Application.MusicalWorkAdministratonManager;
import Domain.Interfaces.IInstrumentation;
import Utils.PlanchesterConstants;
import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christina on 21.04.2017.
 */
public class InstrumentationController {
    public static boolean selectMultipleMusicalWorks = true;
    public static String newHeading;


    public static boolean apply = false;
    public static List<MusicalWorkDTO> selectedMusicalWorks;
    public static InstrumentationDTO instrumentation;
    public static Stage stage;

    @FXML private Label heading;

    @FXML private ScrollPane scrollPane;

    @FXML private TextField filterTextField;

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

    @FXML private TableColumn<String, String> tableAvailableMusicalWorks;
    @FXML private TableColumn<String, String> tableSelectedMusicalWorks;

    @FXML private TableView<String> tableAvailable;
    @FXML private TableView<String> tableSelected;

    @FXML private Button addToSelected;
    @FXML private Button removeFromSelected;

    final ToggleGroup toggleGroup = new ToggleGroup();
    @FXML private JFXRadioButton radioButtonStandard;
    @FXML private JFXRadioButton radioButtonAlternative;

    List<MusicalWorkDTO> musicalWorks;

    @FXML
    public void initialize() {
        setBackgroundStandardInstrumentation();
        getAllMusicalWorks();

        radioButtonStandard.setToggleGroup(toggleGroup);
        radioButtonAlternative.setToggleGroup(toggleGroup);

        heading.setText(newHeading);

        if(selectedMusicalWorks != null && !selectedMusicalWorks.isEmpty()) {
            for(MusicalWorkDTO musicalWorkDTO : selectedMusicalWorks) {
                tableAvailable.getItems().remove(tableAvailable.getItems().stream().filter(o -> o.equals(musicalWorkDTO.getName())).findFirst().get());
                tableSelected.getItems().add(musicalWorkDTO.getName());
            }
        }

        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<MusicalWorkDTO> filteredMusicalWorks = new ArrayList<MusicalWorkDTO>();

            for(MusicalWorkDTO mwDTO : musicalWorks) {
                if(mwDTO.getName().toLowerCase().contains(newValue.toLowerCase()) || mwDTO.getName().toLowerCase().equals(newValue.toLowerCase())) {
                    filteredMusicalWorks.add(mwDTO);
                }
            }

            tableAvailable.getItems().clear();
            for(MusicalWorkDTO mwDTO : filteredMusicalWorks) {
                tableAvailable.getItems().add(mwDTO.getName());
            }
        });

        tableSelected.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSelectedMusicalWork(tableSelected.getSelectionModel().getSelectedItem()));

        tableAvailable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSelectedMusicalWork(tableAvailable.getSelectionModel().getSelectedItem()));

    }

    @FXML
    private void apply() {
        apply = true;
        selectedMusicalWorks = new ArrayList<>();

        for(String s : tableSelected.getItems()) {
            MusicalWorkDTO musicalWorkDTO = musicalWorks.stream().filter(o -> o.getName().equals(s)).findFirst().get();
            selectedMusicalWorks.add(musicalWorkDTO);
        }

        stage.fireEvent(
            new WindowEvent(
                stage,
                WindowEvent.WINDOW_CLOSE_REQUEST
            )
        );
    }

    @FXML
    private void cancel() {
        stage.fireEvent(
            new WindowEvent(
                stage,
                WindowEvent.WINDOW_CLOSE_REQUEST
            )
        );
    }

    @FXML
    private void addMusicalWorkToSelected() {
        if(selectMultipleMusicalWorks == false) {
            if(tableSelected.getItems().size() > 0) {
                return;
            }
        }
        String selecetdItem = tableAvailable.getSelectionModel().getSelectedItem();
        if(selecetdItem != null && selecetdItem != "" ) {
            tableAvailable.getItems().remove(tableAvailable.getSelectionModel().getFocusedIndex());
            tableSelected.getItems().add(selecetdItem);
        }
    }

    @FXML
    private void removeMusicalWorkFromSelected() {
        String selecetdItem = tableSelected.getSelectionModel().getSelectedItem();
        if(selecetdItem != null && selecetdItem != "" ) {
            tableSelected.getItems().remove(tableSelected.getSelectionModel().getFocusedIndex());
            tableAvailable.getItems().add(selecetdItem);
        }
    }

    private void setSelectedMusicalWork(String selecetdItem) {
        if(selecetdItem != null) {
            MusicalWorkDTO musicalWorkDTO = musicalWorks.stream().filter(o -> o.getName().equals(selecetdItem)).findFirst().get();
            clearInstrumentation();
            setStandardInstrumentation(musicalWorkDTO.getInstrumentation());
        }
    }

    private void clearInstrumentation() {
        standardBasson.setText(null);
        standardClarinet.setText(null);
        standardDescription.setText(null);
        standardDoublebass.setText(null);
        standardFirstViolin.setText(null);
        standardFlute.setText(null);
        standardHarp.setText(null);
        standardHorn.setText(null);
        standardKettledrum.setText(null);
        standardOboe.setText(null);
        standardPercussion.setText(null);
        standardSecondViolin.setText(null);
        standardTrombone.setText(null);
        standardTrumpet.setText(null);
        standardTuba.setText(null);
        standardViola.setText(null);
        standardVioloncello.setText(null);
    }

    private void setStandardInstrumentation(IInstrumentation instrumentation) {
        standardBasson.setText(String.valueOf(instrumentation.getBasson()));
        standardClarinet.setText(String.valueOf(instrumentation.getClarinet()));
        if(instrumentation.getDescription() != null) {
            standardDescription.setText(instrumentation.getDescription());
        }
        standardDoublebass.setText(String.valueOf(instrumentation.getDoublebass()));
        standardFirstViolin.setText(String.valueOf(instrumentation.getFirstViolin()));
        standardFlute.setText(String.valueOf(instrumentation.getFlute()));
        standardHarp.setText(String.valueOf(instrumentation.getHarp()));
        standardHorn.setText(String.valueOf(instrumentation.getHorn()));
        standardKettledrum.setText(String.valueOf(instrumentation.getKettledrum()));
        standardOboe.setText(String.valueOf(instrumentation.getOboe()));
        standardPercussion.setText(String.valueOf(instrumentation.getPercussion()));
        standardSecondViolin.setText(String.valueOf(instrumentation.getSecondViolin()));
        standardTrombone.setText(String.valueOf(instrumentation.getTrombone()));
        standardTrumpet.setText(String.valueOf(instrumentation.getTrumpet()));
        standardTuba.setText(String.valueOf(instrumentation.getTube()));
        standardViola.setText(String.valueOf(instrumentation.getViola()));
        standardVioloncello.setText(String.valueOf(instrumentation.getVioloncello()));
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
        tableAvailableMusicalWorks.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        tableSelectedMusicalWorks.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        List<MusicalWorkDTO> musicalWorks = MusicalWorkAdministratonManager.getAllMusicalWorks();

        for(MusicalWorkDTO mwDTO : musicalWorks) {
            tableAvailable.getItems().add(mwDTO.getName());
        }

        this.musicalWorks = musicalWorks;
    }
}