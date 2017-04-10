package Presentation.EventSchedule;

import Presentation.PlanchesterGUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventScheduleController {

    //i:List for ChoiceBox to choice new duty
    ObservableList<String> dutyTypes = FXCollections.observableArrayList("Concert performance",
            "Opera performance","Tour duty","Hofkapelle","Rehearsal","Non-musical duty");

    //i:Mapping choiced duty for loading form/file
    Map<String, String> dutyToForm = new HashMap<>();

    @FXML private Agenda agenda;
    @FXML private ScrollPane scrollPane;
    //i:declare the choice box
    @FXML private ComboBox comboNewDuty;
    @FXML private Label calenderWeekLabel;

    @FXML
    public void initialize() {

        // Test Events
        Agenda.Appointment appointment = new Agenda.AppointmentImpl();
        LocalDateTime start = LocalDateTime.of(2017, Month.APRIL,4,13,00);
        LocalDateTime end = LocalDateTime.of(2017, Month.APRIL,4,16,00);
        appointment.setStartLocalDateTime(start);
        appointment.setEndLocalDateTime(end);
        appointment.setDescription("Test 1");
        appointment.setLocation("Raum 5");
        appointment.setSummary("Title 1");

        Agenda.Appointment appointment2 = new Agenda.AppointmentImpl();
        LocalDateTime start2 = LocalDateTime.of(2017, Month.APRIL,3,12,00);
        LocalDateTime end2 = LocalDateTime.of(2017, Month.APRIL,3,14,00);
        appointment2.setStartLocalDateTime(start2);
        appointment2.setEndLocalDateTime(end2);
        appointment2.setDescription("Test 2");
        appointment2.setLocation("Room 3");
        appointment2.setSummary("Title 2");

        agenda.appointments().add(appointment);
        agenda.appointments().add(appointment2);

        // agenda settings
        agenda.setAllowDragging(false);
        agenda.setAllowResize(false);
        agenda.localeProperty().set(Locale.GERMAN);
        agenda.setDisplayedLocalDateTime(LocalDateTime.now());

        //set CalenderWeek
        setCalenderWeekLabel();

        agenda.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0){
                // TODO: check what type of event was selected an load correct fxml file
                try {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource("EditOpera.fxml")));

                    ObservableList<Agenda.Appointment> appointments = agenda.selectedAppointments();
                    Agenda.Appointment appointment = appointments.get(0);

                    TextField textField = (TextField) scrollPane.lookup("#name");
                    textField.setText(appointment.getSummary());

                    TextField textField2 = (TextField) scrollPane.lookup("#description");
                    textField2.setText(appointment.getDescription());

                    DatePicker datePicker = (DatePicker) scrollPane.lookup("#date");
                    datePicker.setValue(appointment.getStartLocalDateTime().toLocalDate());

                    LocalTimePicker startTime = (LocalTimePicker) scrollPane.lookup("#start");
                    startTime.setLocalTime(appointment.getStartLocalDateTime().toLocalTime());

                    LocalTimePicker endTime = (LocalTimePicker) scrollPane.lookup("#end");
                    endTime.setLocalTime(appointment.getEndLocalDateTime().toLocalTime());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Property selection changed");
            }
        });

        //i:ChoiceBox choiceNewEventDuty initialisieren
        comboNewDuty.setItems(dutyTypes);
        comboNewDuty.setPromptText("Choose Eventtype");

        //i:Map initialisieren
        dutyToForm.put("Opera performance","CreateOpera.fxml");
        dutyToForm.put("Concert performance","CreateConcert.fxml");
        dutyToForm.put("Tour duty","CreateTour.fxml");
        dutyToForm.put("Hofkapelle","CreateHofkapelle.fxml");
        dutyToForm.put("Rehearsal","CreateRehearsal.fxml");
        dutyToForm.put("Non-musical duty","CreateNonMusical.fxml");


        comboNewDuty.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldVal, String newVal) {

                String choice = newVal;
                String formToLoad = dutyToForm.get( choice );


                if( choice.equals("choose duty")) {
                    return;
                }

                try {
                    scrollPane.setContent(FXMLLoader.load(getClass().getResource(formToLoad)));

                } catch (Exception e) {
                    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                    dialog.setHeaderText( "Your choice: " + choice );
                    dialog.setContentText("Trying to open file " + formToLoad + ":\n" + "Form unsupported yet");
                    dialog.setResizable(true);
                    dialog.getDialogPane().setPrefSize(350, 200);
                    dialog.showAndWait();
                    final Optional<ButtonType> result = dialog.showAndWait();

                    e.printStackTrace();
                }
            }
        });
    }

    private void setCalenderWeekLabel() {
        Calendar cal = agenda.getDisplayedCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        calenderWeekLabel.setText("Calender Week " + String.valueOf(week));
    }

    @FXML
    private void navigateOneWeekBackClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.minus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();
    }

    @FXML
    private void navigateOneWeekForwardClicked() {
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.plus(7, ChronoUnit.DAYS));
        setCalenderWeekLabel();
    }

    @FXML
    private void showActualWeekClicked() {
        agenda.setDisplayedLocalDateTime(LocalDateTime.now());
        setCalenderWeekLabel();
    }

    @FXML
    private void saveEventChanges(){
        // TODO: implement for every kind of event
        TextField name = (TextField) PlanchesterGUI.scene.lookup("#name");
        TextField description = (TextField) PlanchesterGUI.scene.lookup("#description");
        DatePicker date = (DatePicker) PlanchesterGUI.scene.lookup("#date");
        LocalTimePicker startTime = (LocalTimePicker) PlanchesterGUI.scene.lookup("#start");
        LocalTimePicker endTime = (LocalTimePicker) PlanchesterGUI.scene.lookup("#end");

        LocalDateTime start = LocalDateTime.of(date.getValue(), startTime.getLocalTime());
        LocalDateTime end = LocalDateTime.of(date.getValue(), endTime.getLocalTime());

        ObservableList<Agenda.Appointment> appointments = agenda.selectedAppointments();
        Agenda.Appointment appointment = appointments.get(0);

        appointment.setSummary(name.getText());
        appointment.setDescription(description.getText());
        appointment.setStartLocalDateTime(start);
        appointment.setEndLocalDateTime(end);

        agenda.refresh();
    }




}
