package Presentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PlanchesterGUI {

    public static Scene scene;

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Planchester");

        TabPane tabPane = new TabPane();
        //Create Tabs
        Tab dutyRoster = new Tab();
        dutyRoster.setText("Duty Roster");

        Tab eventSchedule = new Tab();
        eventSchedule.setText("Event Schedule");
        eventSchedule.setContent(FXMLLoader.load(getClass().getResource("EventSchedule/EventSchedule.fxml")));

        // add Tabs
        tabPane.getTabs().add(dutyRoster);
        tabPane.getTabs().add(eventSchedule);

        // set and show scene
        Scene scene = new Scene(tabPane, 1200, 900, Color.WHITE);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}