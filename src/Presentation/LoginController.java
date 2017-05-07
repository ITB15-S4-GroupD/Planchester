package Presentation;

import Application.AccountAdministrationManager;
import Persistence.AccountRDBMapper;
import Persistence.Entities.AccountEntity;
import Utils.MessageHelper;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



/**
 * Created by Christina on 04.05.2017.
 */
public class LoginController {

    @FXML private TextField username;
    @FXML private PasswordField password;
    public static AccountEntity loggedInUser;

    @FXML
    public void initialize() {
        loggedInUser = null;
    }

    @FXML
    private void login() {
        loggedInUser = AccountAdministrationManager.getAccount(username.getText(), password.getText());
        if(loggedInUser == null) {
            MessageHelper.showErrorAlertMessage("Wrong username or password");
        } else {
            PlanchesterGUI.primaryStage.fireEvent(
                 new WindowEvent(
                    PlanchesterGUI.primaryStage,
                    WindowEvent.WINDOW_CLOSE_REQUEST
                )
            );
        }
    }

    //handles the Enter-Key-Button for faster login.
    @FXML
    public void handleEnterPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            login();
        }
    }

    @FXML
    private void cancel() {
        PlanchesterGUI.primaryStage.fireEvent(
            new WindowEvent(
                PlanchesterGUI.primaryStage,
                WindowEvent.WINDOW_CLOSE_REQUEST
            )
        );
    }

}
