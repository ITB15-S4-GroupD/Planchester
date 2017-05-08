package Presentation.Login;

import Application.AccountAdministrationManager;
import Persistence.AccountRDBMapper;
import Persistence.Entities.AccountEntity;
import Presentation.PlanchesterGUI;
import Utils.MessageHelper;
import Utils.PlanchesterMessages;
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
    //public static AccountEntity loggedInUser;
    public static Stage stage;

    @FXML
    private void login() {
        AccountAdministrationManager.getInstance().setAccount(username.getText(), password.getText());

        if(AccountAdministrationManager.getInstance().getLoggedInAccount() == null) {
            MessageHelper.showErrorAlertMessage(PlanchesterMessages.LOGIN_FAILED);
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
    public void cancel() {
        PlanchesterGUI.primaryStage.fireEvent(
            new WindowEvent(
                PlanchesterGUI.primaryStage,
                WindowEvent.WINDOW_CLOSE_REQUEST
            )
        );
    }
}