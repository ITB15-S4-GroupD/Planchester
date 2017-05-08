package Presentation;

import Application.AccountAdministrationManager;
import Application.DatabaseSessionManager;
import Presentation.EventSchedule.EventScheduleController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

<<<<<<< HEAD
//        primaryStage.setTitle("Planchester Login");
//        scene = new Scene(FXMLLoader.load(getClass().getResource("Login.fxml")));
//        primaryStage.setScene(scene);
//        primaryStage.getIcons().add(new Image("file:src/Presentation/Images/logoplanchester.png"));
//        primaryStage.show();

//        primaryStage.setOnCloseRequest(t -> {
//            if(LoginController.loggedInUser != null) {
=======
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
>>>>>>> master
                try {
                    showPlanchesterGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
<<<<<<< HEAD
//            } else {
//                Platform.exit();
//                System.exit(0);
//            }
//        });

//        LoginController.stage = primaryStage;
=======
            } else {
                closePlanchester();
            }
        });
>>>>>>> master
    }

    private static void showPlanchesterGUI() {
        try {
            primaryStage = new Stage();
            primaryStage.setTitle("Planchester");
            primaryStage.setMaximized(true);

            primaryStage.setOnCloseRequest(t -> {
                closePlanchester();
            });

<<<<<<< HEAD
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:src/Presentation/Images/logoplanchester.png"));
        primaryStage.show();
    }

    private TabPane createTabs() throws java.io.IOException {
//        String accountRole = LoginController.loggedInUser.getAccountRole();

        TabPane tabPane = new TabPane();

        Tab dutyRoster = new Tab();
        dutyRoster.setText("Duty Roster");
        dutyRoster.setContent(FXMLLoader.load(getClass().getResource("DutyRoster/DutyRoster.fxml")));
        tabPane.getTabs().add(dutyRoster);

        Tab eventSchedule = new Tab();
        eventSchedule.setText("Event Schedule");
        eventSchedule.setContent(FXMLLoader.load(getClass().getResource("EventSchedule/EventSchedule.fxml")));
        tabPane.getTabs().add(eventSchedule);

//        if(accountRole.equals(AccountRole.Manager.toString()) || accountRole.equals(AccountRole.Administrator.toString())) {
            Tab musicalWorks = new Tab();
            musicalWorks.setText("Musical Works");
            tabPane.getTabs().add(musicalWorks);

            Tab instruments = new Tab();
            instruments.setText("Instruments");
            tabPane.getTabs().add(instruments);

            Tab userAdministration = new Tab();
            userAdministration.setText("User Administration");
            tabPane.getTabs().add(userAdministration);
//        }
=======
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
>>>>>>> master

    private static void closePlanchester() {
        DatabaseSessionManager.closeSession();
        Platform.exit();
        System.exit(0);
    }
}