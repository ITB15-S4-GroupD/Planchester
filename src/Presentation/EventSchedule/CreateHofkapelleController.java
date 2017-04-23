package Presentation.EventSchedule;

import Utils.PlanchesterConstants;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jfxtras.scene.control.LocalTimePicker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Created by Ina on 09.04.2017.
 */
public class CreateHofkapelleController {

    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXTimePicker startTime;
    @FXML private JFXTimePicker endTime;
    @FXML private JFXDatePicker date;
    @FXML private TextField eventLocation;
    @FXML private ChoiceBox<String> musicalWork;
    @FXML private TextField conductor;
    @FXML private TextField points;

    @FXML
    public void initialize() {
        name.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
        date.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
        startTime.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
        checkRequiredFields();
    }

    @FXML
    private void save() {

    }

    @FXML
    public boolean discard() {
        //TODO check for Changes
        EventScheduleController.resetSideContent();
        return true;
    }
    private void checkRequiredFields() {
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(name.getText().equals("") || name.getText() == null) {
                    name.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
                } else {
                    name.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_FILLED);
                }

            }
        });
        date.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(date.getValue() == null) {
                    date.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
                } else {
                    date.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_FILLED);
                }
            }
        });

        startTime.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                if(startTime.getValue() == null) {
                    startTime.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
                } else {
                    startTime.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_FILLED);
                }
            }
        });
    }
}