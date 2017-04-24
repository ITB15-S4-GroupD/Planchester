package Presentation.EventSchedule;

import Domain.Models.EventDutyModel;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jfxtras.scene.control.agenda.Agenda;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Christina on 20.04.2017.
 */
public class EditHofkapelleController {

        @FXML private TextField name;
        @FXML private TextArea description;
        @FXML private JFXTimePicker startTime;
        @FXML private JFXTimePicker endTime;
        @FXML private JFXDatePicker date;
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
                                if(name.getText().equals("") || name.getText() == null) {
                                        name.setStyle("-fx-control-inner-background: #ffdec9");
                                } else {
                                        name.setStyle("-fx-control-inner-background: #ffffff");
                                }

                        }
                });
                date.valueProperty().addListener(new ChangeListener<LocalDate>() {
                        @Override
                        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                                if(date.getValue() == null) {
                                        date.setStyle("-fx-control-inner-background: #ffdec9");
                                } else {
                                        date.setStyle("-fx-control-inner-background: #ffffff");
                                }
                        }
                });

                startTime.valueProperty().addListener(new ChangeListener<LocalTime>() {
                        @Override
                        public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                                if(startTime.getValue() == null) {
                                        startTime.setStyle("-fx-control-inner-background: #ffdec9");
                                } else {
                                        startTime.setStyle("-fx-control-inner-background: #ffffff");
                                }
                        }
                });
        }
}


