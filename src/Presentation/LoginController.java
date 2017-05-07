package Presentation;

import Application.AccountAdministrationManager;
import Persistence.AccountRDBMapper;
import Persistence.Entities.AccountEntity;
import Utils.MessageHelper;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        AccountEntity loggedInUser = AccountAdministrationManager.getAccount(username.getText(), password.getText());
        if(loggedInUser == null) {
            MessageHelper.showErrorAlertMessage("Wrong username or password");
        } else {
            AccountAdministrationManager.setLoggedInUser(loggedInUser);
            stage.fireEvent(
                 new WindowEvent(
                    stage,
                    WindowEvent.WINDOW_CLOSE_REQUEST
                )
            );
        }
    }

    @FXML
    private void cancel() {
        stage.fireEvent(
            new WindowEvent(
                stage,
                WindowEvent.WINDOW_CLOSE_REQUEST
            )
        );
    }

}