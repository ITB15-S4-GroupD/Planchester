package Presentation.EventSchedule;

import Domain.Models.EventDutyModel;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jfxtras.scene.control.agenda.Agenda;

import java.time.LocalDate;

/**
 * Created by Christina on 20.04.2017.
 */
public class EditTourController {

    @FXML private TextField name;
    @FXML private TextArea description;
    @FXML private JFXDatePicker startDate;
    @FXML private JFXDatePicker endDate;
    @FXML private TextField eventLocation;
    @FXML private ChoiceBox<String> musicalWork;
    @FXML private TextField conductor;
    @FXML private TextField points;

    private EventDutyModel initEventDutyModel; // remember init data to compare
    private Agenda.Appointment initAppointment; // remember init data to compare

    @FXML
    public void initialize() {
        checkRequiredFields();
    }

    @FXML
    private void save() {
        // TODO: save data
    }

    @FXML
    public boolean discard() {
        // TODO: check with init data for changes

        // remove content of sidebar
        EventScheduleController.resetSideContent();
        EventScheduleController.removeSelection(initAppointment);
        return true;
    }

    private void checkRequiredFields() {
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (name.getText().equals("") || name.getText() == null) {
                    name.setStyle("-fx-control-inner-background: #ffdec9");
                } else {
                    name.setStyle("-fx-control-inner-background: #ffffff");
                }

            }
        });
        startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (startDate.getValue() == null) {
                    startDate.setStyle("-fx-control-inner-background: #ffdec9");
                } else {
                    startDate.setStyle("-fx-control-inner-background: #ffffff");
                }
            }
        });

        endDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (endDate.getValue() == null) {
                    endDate.setStyle("-fx-control-inner-background: #ffdec9");
                } else {
                    endDate.setStyle("-fx-control-inner-background: #ffffff");
                }
            }
        });
    }
}
