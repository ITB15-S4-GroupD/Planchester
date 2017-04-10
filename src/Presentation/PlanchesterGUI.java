package Presentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

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

        Tab musicalWorks = new Tab();
        musicalWorks.setText("Musical Works");

        Tab instruments = new Tab();
        instruments.setText("Instruments");

        Tab user = new Tab();
        user.setText("User Administration");

        // add Tabs
        tabPane.getTabs().add(dutyRoster);
        tabPane.getTabs().add(eventSchedule);
        tabPane.getTabs().add(musicalWorks);
        tabPane.getTabs().add(instruments);
        tabPane.getTabs().add(user);

        //Tabs not closeable
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // set and show scene
        Scene scene = new Scene(tabPane, 1200, 900, Color.WHITE);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}