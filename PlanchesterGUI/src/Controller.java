import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Controller {

    @FXML
    private void selectionChanged() {
        System.out.println("selection changed");
    }

    @FXML
    private void navigateOneWeekBackClicked() {
        Agenda agenda = (Agenda) Main.scene.lookup("#agenda");
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.minus(7, ChronoUnit.DAYS));
    }

    @FXML
    private void navigateOneWeekForwardClicked() {
        Agenda agenda = (Agenda) Main.scene.lookup("#agenda");
        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();
        agenda.setDisplayedLocalDateTime(displayedDate.plus(7, ChronoUnit.DAYS));
    }

    @FXML
    private void saveEventChanges(){
        Agenda agenda = (Agenda) Main.scene.lookup("#agenda");
        TextField name = (TextField) Main.scene.lookup("#name");
        TextField description = (TextField) Main.scene.lookup("#description");
        DatePicker date = (DatePicker) Main.scene.lookup("#date");
        LocalTimePicker startTime = (LocalTimePicker) Main.scene.lookup("#start");
        LocalTimePicker endTime = (LocalTimePicker) Main.scene.lookup("#end");

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