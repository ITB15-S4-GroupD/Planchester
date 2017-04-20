package Presentation.EventSchedule;

import Domain.Models.EventDutyModel;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jfxtras.scene.control.agenda.Agenda;

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
}


