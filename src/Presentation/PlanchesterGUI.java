package Presentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;

public class PlanchesterGUI {

    public static Scene scene;

    public void start(Stage primaryStage) throws Exception {

        Image image = new Image(new File("../Images/logoplanchester.png").toURI().toString());
        primaryStage.getIcons().add(image);

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

        Tab support = new Tab();
        support.setText("Support");

        // add Tabs
        tabPane.getTabs().add(dutyRoster);
        tabPane.getTabs().add(eventSchedule);
        tabPane.getTabs().add(musicalWorks);
        tabPane.getTabs().add(instruments);
        tabPane.getTabs().add(user);
        tabPane.getTabs().add(support);

        //Tabs not closeable
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // set and show scene
        Scene scene = new Scene(tabPane, 1200, 900, Color.WHITE);
        primaryStage.setMaximized(true);

        URL url = this.getClass().getResource("CSS\\stylesheet.css");
        if (url == null) {
            System.out.println("CSS Resource not found. Aborting.");
        }
        String css = url.toExternalForm();
        scene.getStylesheets().add(css);


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}