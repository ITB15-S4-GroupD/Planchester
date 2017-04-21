package Presentation.EventSchedule;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

/**
 * Created by Christina on 20.04.2017.
 */
public class CreateTourController {
    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXDatePicker startDate;
    @FXML private JFXDatePicker endDate;
    @FXML private TextField eventLocation;
    @FXML private ChoiceBox<String> musicalWork;
    @FXML private TextField conductor;
    @FXML private TextField points;

    @FXML
    public void initialize() {
        name.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
        startDate.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
        endDate.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
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
        startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(startDate.getValue() == null) {
                    startDate.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
                } else {
                    startDate.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_FILLED);
                }
            }
        });

        endDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(endDate.getValue() == null) {
                    endDate.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_REQUIRED);
                } else {
                    endDate.setStyle(PlanchesterConstants.BACKGROUNDSTYLE_FILLED);
                }
            }
        });
    }
}
