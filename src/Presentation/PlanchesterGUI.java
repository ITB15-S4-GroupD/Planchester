package Presentation;

import Application.DatabaseSessionManager;
import Utils.Enum.AccountRole;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.net.URL;
import java.rmi.UnexpectedException;

public class PlanchesterGUI {
    public static Scene scene;

    public void start(Stage primaryStage) throws Exception {
        DatabaseSessionManager.readConfiguration();

        primaryStage.setTitle("Planchester Login");
        scene = new Scene(FXMLLoader.load(getClass().getResource("Login.fxml")));
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:src/Presentation/Images/logoplanchester.png"));
        primaryStage.show();

        primaryStage.setOnCloseRequest(t -> {
            if(LoginController.loggedInUser != null) {
                try {
                    showPlanchesterGUI(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Platform.exit();
                System.exit(0);
            }
        });

        LoginController.stage = primaryStage;
    }

    private void showPlanchesterGUI(Stage primaryStage) throws Exception {
        primaryStage = new Stage();
        TabPane tabPane = createTabs();
        primaryStage.setTitle("Planchester");
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
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
        String accountRole = LoginController.loggedInUser.getAccountRole();

        TabPane tabPane = new TabPane();

        Tab dutyRoster = new Tab();
        dutyRoster.setText("Duty Roster");
        tabPane.getTabs().add(dutyRoster);

        Tab eventSchedule = new Tab();
        eventSchedule.setText("Event Schedule");
        eventSchedule.setContent(FXMLLoader.load(getClass().getResource("EventSchedule/EventSchedule.fxml")));
        tabPane.getTabs().add(eventSchedule);

        if(accountRole == AccountRole.Manager.toString() || accountRole == AccountRole.Administrator.toString()) {
            Tab musicalWorks = new Tab();
            musicalWorks.setText("Musical Works");
            tabPane.getTabs().add(musicalWorks);

            Tab instruments = new Tab();
            instruments.setText("Instruments");
            tabPane.getTabs().add(instruments);

            Tab userAdministration = new Tab();
            userAdministration.setText("User Administration");
            tabPane.getTabs().add(userAdministration);
        }

        Tab support = new Tab();
        support.setText("Support");
        tabPane.getTabs().add(support);

        //Tabs not closeable
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabPane;
    }
}