package Presentation;

import Application.DatabaseSessionManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.rmi.UnexpectedException;

public class PlanchesterGUI {
    public static Scene scene;

    public void start(Stage primaryStage) throws Exception {
        DatabaseSessionManager.readConfiguration();

        TabPane tabPane = createTabs();

        primaryStage.setTitle("Planchester");
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        // set and show scene
        scene = new Scene(tabPane, 1200, 900, Color.WHITE);
        URL url = this.getClass().getResource("CSS/stylesheet.css");
        if (url == null) {
            throw new UnexpectedException("CSS Resource not found. Aborting.");
        }
        String css = url.toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:src/Presentation/Images/logoplanchester.png"));
        primaryStage.show();
    }

    private TabPane createTabs() throws java.io.IOException {
        TabPane tabPane = new TabPane();
        Tab dutyRoster = new Tab();
        dutyRoster.setText("Duty Roster");

        Tab eventSchedule = new Tab();
        eventSchedule.setText("Event Schedule");
        eventSchedule.setContent(FXMLLoader.load(getClass().getResource("EventSchedule/EventSchedule.fxml")));

        Tab musicalWorks = new Tab();
        musicalWorks.setText("Musical Works");

        Tab instruments = new Tab();
        instruments.setText("Instruments");

        Tab userAdministration = new Tab();
        userAdministration.setText("User Administration");

        Tab support = new Tab();
        support.setText("Support");

        // add Tabs
        tabPane.getTabs().add(dutyRoster);
        tabPane.getTabs().add(eventSchedule);
        tabPane.getTabs().add(musicalWorks);
        tabPane.getTabs().add(instruments);
        tabPane.getTabs().add(userAdministration);
        tabPane.getTabs().add(support);

        //Tabs not closeable
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabPane;
    }
}