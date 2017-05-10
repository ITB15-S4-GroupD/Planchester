package Presentation;

import Application.DTO.EventDutyDTO;
import Application.EventScheduleManager;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import jfxtras.scene.control.agenda.Agenda;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by TheeLenchen on 08.05.2017.
 */
public abstract class CalenderController {
    @FXML protected Agenda agenda;
    @FXML protected ScrollPane scrollPane;

    @FXML protected MenuButton addNewEvent;
    @FXML protected Label calenderWeekLabel;

    @FXML private JFXTextField colorKeyConcert;
    @FXML protected JFXTextField colorKeyOpera;
    @FXML protected JFXTextField colorKeyTour;
    @FXML protected JFXTextField colorKeyHofkapelle;
    @FXML protected JFXTextField colorKeyRehearsal;
    @FXML protected JFXTextField colorKeyNonMusical;

    private String colorOpera;
    private String colorConcert;
    private String colorTour;
    private String colorRehearsal;
    private String colorNonMusical;
    private String colorHofkapelle;

    protected static Agenda.AppointmentGroup opera;
    protected static Agenda.AppointmentGroup concert;
    protected static Agenda.AppointmentGroup hofkapelle;
    protected static Agenda.AppointmentGroup tour;
    protected static Agenda.AppointmentGroup rehearsal;
    protected static Agenda.AppointmentGroup nonMusicalEvent;

    protected void getGroupColorsFromCSS() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Presentation/CSS/agenda.css"), "UTF-8"));
            String line = br.readLine();

            while (line != null) {
                String setControlInnerBackground = "-fx-control-inner-background: ";
                if(line.contains("group1")) {
                    colorOpera = setControlInnerBackground + getColor(line) + ";";
                } else if(line.contains("group2")) {
                    colorConcert = setControlInnerBackground + getColor(line) + ";";
                } else if(line.contains("group3")) {
                    colorHofkapelle = setControlInnerBackground + getColor(line) + ";";
                } else if(line.contains("group4")) {
                    colorTour = setControlInnerBackground + getColor(line) + ";";
                } else if(line.contains("group5")) {
                    colorRehearsal = setControlInnerBackground + getColor(line) + ";";
                } else if(line.contains("group6")) {
                    colorNonMusical = setControlInnerBackground + getColor(line) + ";";
                }
                line = br.readLine();
            }
            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getColor(String s) {
        try {
            return s.substring(s.indexOf("#"),s.indexOf("#")+7);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    protected void setColorKeyMap() {
        colorKeyOpera.setStyle(colorOpera);
        colorKeyConcert.setStyle(colorConcert);
        colorKeyTour.setStyle(colorTour);
        colorKeyHofkapelle.setStyle(colorHofkapelle);
        colorKeyRehearsal.setStyle(colorRehearsal);
        colorKeyNonMusical.setStyle(colorNonMusical);
    }

    protected void setCalenderWeekLabel() {
        Calendar cal = agenda.getDisplayedCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        calenderWeekLabel.setText("Calender Week " + String.valueOf(week));
    }

    @FXML
    protected void showActualWeekClicked() {
        agenda.setDisplayedLocalDateTime(LocalDateTime.now());
        setCalenderWeekLabel();
    }

    protected void initializeAppointmentGroupsForEventtypes() {
        opera = new Agenda.AppointmentGroupImpl();
        opera.setStyleClass("group1");
        concert = new Agenda.AppointmentGroupImpl();
        concert.setStyleClass("group2");
        hofkapelle = new Agenda.AppointmentGroupImpl();
        hofkapelle.setStyleClass("group3");
        tour = new Agenda.AppointmentGroupImpl();
        tour.setStyleClass("group4");
        rehearsal = new Agenda.AppointmentGroupImpl();
        rehearsal.setStyleClass("group5");
        nonMusicalEvent = new Agenda.AppointmentGroupImpl();
        nonMusicalEvent.setStyleClass("group6");
    }

    protected void initialzeCalendarSettings() {
        // agenda settings
        agenda.setAllowDragging(false); //drag and drop the event
        agenda.setAllowResize(false);
        agenda.localeProperty().set(Locale.UK);
        agenda.setDisplayedLocalDateTime(LocalDateTime.now()); //show current week in event scheduler
    }
}
