package Presentation;

import Application.AccountAdministrationManager;
import Application.DatabaseSessionManager;
import Application.UserAdministrationManager;
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
import java.util.ArrayList;
import java.util.List;

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
            if(AccountAdministrationManager.getLoggedInAccount() != null) {
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
        tabPane.setId("MainTabPane");
        primaryStage.setTitle("Planchester - Logged in: " + AccountAdministrationManager.getLoggedInName());
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

        TabPane tabPane = new TabPane();
        List<Tab> tabList = new ArrayList<>();

        Tab dutyRoster = new Tab();
        dutyRoster.setId("TbDutyRoster");
        dutyRoster.setText("Duty Roster");
        tabList.add(dutyRoster);

        Tab eventSchedule = new Tab();
        eventSchedule.setId("TbEventSchedule");
        eventSchedule.setText("Event Schedule");
        eventSchedule.setContent(FXMLLoader.load(getClass().getResource("EventSchedule/EventSchedule.fxml")));
        tabList.add(eventSchedule);

            Tab musicalWorks = new Tab();
            musicalWorks.setId("TbMusicalWorks");
            musicalWorks.setText("Musical Works");
            tabList.add(musicalWorks);

            Tab instruments = new Tab();
            instruments.setId("TbInstruments");
            instruments.setText("Instruments");
            tabList.add(instruments);

            Tab userAdministration = new Tab();
            userAdministration.setId("TbUserAdministration");
            userAdministration.setText("User Administration");
            tabList.add(userAdministration);

        Tab support = new Tab();
        support.setId("TbSupport");
        support.setText("Support");
        tabList.add(support);

        tabPane.getTabs().addAll(AccountAdministrationManager.getUserRestrain().constrainMainTabs(tabList));

        //Tabs not closeable
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabPane;
    }
}