package Presentation;

import Application.DatabaseSessionManager;
import Utils.Enum.AccountRole;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.rmi.UnexpectedException;

public class PlanchesterGUI {
    public static Scene scene;

    public void start(Stage primaryStage) throws Exception {
        DatabaseSessionManager.beginSession();

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
                DatabaseSessionManager.closeSession();
                Platform.exit();
                System.exit(0);
            }
        });

        LoginController.stage = primaryStage;
    }

    private void showPlanchesterGUI(Stage primaryStage) throws Exception {
        primaryStage = new Stage();
        primaryStage.setTitle("Planchester");
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(t -> {
            DatabaseSessionManager.closeSession();
            Platform.exit();
            System.exit(0);
        });

        scene = new Scene(FXMLLoader.load(getClass().getResource("PlanchesterFrame.fxml")));

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
}