package Presentation;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Locale;

public class PlanchesterGUI extends Application {

    public static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // start Presentation
        Parent root = FXMLLoader.load(getClass().getResource("Planchester.fxml"));
        //scene = new Scene(root, 800, 600);
        scene = new Scene(root);
        Agenda agenda = (Agenda) scene.lookup("#agenda");

        // Test Events
        Agenda.Appointment appointment = new Agenda.AppointmentImpl();
        LocalDateTime start = LocalDateTime.of(2017,Month.APRIL,4,13,00);
        LocalDateTime end = LocalDateTime.of(2017,Month.APRIL,4,16,00);
        appointment.setStartLocalDateTime(start);
        appointment.setEndLocalDateTime(end);
        appointment.setDescription("Test 1");
        appointment.setLocation("Raum 5");
        appointment.setSummary("Title 1");

        Agenda.Appointment appointment2 = new Agenda.AppointmentImpl();
        LocalDateTime start2 = LocalDateTime.of(2017,Month.APRIL,3,12,00);
        LocalDateTime end2 = LocalDateTime.of(2017,Month.APRIL,3,14,00);
        appointment2.setStartLocalDateTime(start2);
        appointment2.setEndLocalDateTime(end2);
        appointment2.setDescription("Test 2");
        appointment2.setLocation("Room 3");
        appointment2.setSummary("Title 2");

        agenda.appointments().add(appointment);
        agenda.appointments().add(appointment2);

        agenda.setAllowDragging(false);
        agenda.setAllowResize(false);

        agenda.localeProperty().set(Locale.GERMAN);
        agenda.setDisplayedLocalDateTime(LocalDateTime.now());

        // event when event selected
        agenda.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0){
                System.out.println("Property selection changed");
                ObservableList<Agenda.Appointment> appointments = agenda.selectedAppointments();
                Agenda.Appointment appointment = appointments.get(0);

                TextField textField = (TextField) scene.lookup("#name");
                textField.setText(appointment.getSummary());

                TextField textField2 = (TextField) scene.lookup("#description");
                textField2.setText(appointment.getDescription());

                DatePicker datePicker = (DatePicker) scene.lookup("#date");
                datePicker.setValue(appointment.getStartLocalDateTime().toLocalDate());

                LocalTimePicker startTime = (LocalTimePicker) scene.lookup("#start");
                startTime.setLocalTime(appointment.getStartLocalDateTime().toLocalTime());

                LocalTimePicker endTime = (LocalTimePicker) scene.lookup("#end");
                endTime.setLocalTime(appointment.getEndLocalDateTime().toLocalTime());

                /*
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Property Clicked");
                alert.setHeaderText(appointment.getDescription());
                alert.setContentText(appointment.getDescription());

                alert.showAndWait();
                */
            }
        });

        // show Presentation
        primaryStage.setTitle("Planchester");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}