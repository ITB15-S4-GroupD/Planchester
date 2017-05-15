package Presentation;

import Application.AccountAdministrationManager;
import Application.DatabaseSessionManager;
import Presentation.EventSchedule.EventScheduleController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.rmi.UnexpectedException;

public class PlanchesterGUI {

    public static Scene scene;
    public static Stage primaryStage;

    public void start(Stage primaryStage) throws Exception {
        DatabaseSessionManager.beginSession();
        showFirstLogin(primaryStage);
    }

    public void showFirstLogin(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Planchester Login");
        scene = new Scene(FXMLLoader.load(getClass().getResource("Login/Login.fxml")));
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:src/Presentation/Images/logoplanchester.png"));
        primaryStage.show();
        checkLogin(primaryStage);
        this.primaryStage = primaryStage;
    }

    public static void showLogin() {
        try {
            // reset all loaded data
            EventScheduleController.removeAllData();

            primaryStage.close();
            primaryStage = new Stage();
            primaryStage.setTitle("Planchester Login");
            scene = new Scene(FXMLLoader.load(PlanchesterGUI.class.getResource("Login/Login.fxml")));
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image("file:src/Presentation/Images/logoplanchester.png"));
            primaryStage.show();
            checkLogin(primaryStage);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void checkLogin(Stage primaryStage) {
        primaryStage.setOnCloseRequest(t -> {
            if(AccountAdministrationManager.getInstance().getLoggedInAccount() != null) {
                try {
                    showPlanchesterGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                closePlanchester();
            }
        });
    }

    private static void showPlanchesterGUI() {
        try {
            primaryStage = new Stage();
            primaryStage.setTitle("Planchester");
            primaryStage.setMaximized(true);

            primaryStage.setOnCloseRequest(t -> {
                closePlanchester();
            });

            scene = new Scene(FXMLLoader.load(PlanchesterGUI.class.getResource("PlanchesterFrame.fxml")));

            URL url = PlanchesterGUI.class.getResource("CSS/stylesheet.css");
            if (url == null) {
                throw new UnexpectedException("CSS Resource not found. Aborting.");
            }
            String css = url.toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image("file:src/Presentation/Images/logoplanchester.png"));
            primaryStage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void closePlanchester() {
        DatabaseSessionManager.closeSession();
        Platform.exit();
        System.exit(0);
    }
}