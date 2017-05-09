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


    @FXML
    private Agenda agenda;
    @FXML private ScrollPane scrollPane;
    @FXML private MenuButton addNewEvent;
    @FXML private Label calenderWeekLabel;

    @FXML
    private JFXTextField colorKeyConcert;
    @FXML private JFXTextField colorKeyOpera;
    @FXML private JFXTextField colorKeyTour;
    @FXML private JFXTextField colorKeyHofkapelle;
    @FXML private JFXTextField colorKeyRehearsal;
    @FXML private JFXTextField colorKeyNonMusical;


    private String colorOpera;
    private String colorConcert;
    private String colorTour;
    private String colorRehearsal;
    private String colorNonMusical;
    private String colorHofkapelle;


    private static Agenda staticAgenda;
    private static Agenda.AppointmentGroup opera;
    private static Agenda.AppointmentGroup concert;
    private static Agenda.AppointmentGroup hofkapelle;
    private static Agenda.AppointmentGroup tour;
    private static Agenda.AppointmentGroup rehearsal;
    private static Agenda.AppointmentGroup nonMusicalEvent;
    private static Agenda.Appointment selectedAppointment;

    private static Map<Agenda.Appointment, EventDutyDTO> staticLoadedEventsMap = new HashMap<>();
    private static ScrollPane staticScrollPane;
    private static boolean editOpen = false;

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

    protected static Node tryResetSideContent() {
        if(getSideContent() == null) {
            return null;
        } else {
            Button discard = (Button) PlanchesterGUI.scene.lookup("#discard");
            discard.fire();
            return getSideContent();
        }
    }

    protected static void resetSideContent() {
        staticScrollPane.setContent(null);
        editOpen = false;
    }

    protected static Node getSideContent() {
        return staticScrollPane.getContent();
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
