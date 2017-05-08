package Presentation;

import Application.DatabaseSessionManager;
import Application.EventScheduleManager;
import Presentation.EventSchedule.EventScheduleController;
import Utils.Enum.AccountRole;
import Utils.MessageHelper;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Created by Christina on 06.05.2017.
 */
public class PlanchesterFrameController {

    @FXML private VBox VBox;
    @FXML private MenuBar menuBar;

    @FXML private Tab dutyRosterTab;
    @FXML private Tab eventscheduleTab;
    @FXML private Tab musicalWorksTab;
    @FXML private Tab instrumentsTab;
    @FXML private Tab useradministrationTab;
    @FXML private Tab supportTab;
    @FXML private Menu logoutMenu;


    @FXML private TabPane tabPane;

    @FXML
    public void initialize()  throws Exception {
        String accountRole = LoginController.loggedInUser.getAccountRole();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        eventscheduleTab.setContent(FXMLLoader.load(getClass().getResource("EventSchedule/EventSchedule.fxml")));

        if(accountRole.equals(AccountRole.Musician.toString()) || accountRole.equals(AccountRole.Section_representative.toString()) || accountRole.equals(AccountRole.Substitute.toString())) {
            tabPane.getTabs().remove(musicalWorksTab);
            tabPane.getTabs().remove(instrumentsTab);
            tabPane.getTabs().remove(useradministrationTab);
        }
    }

    @FXML
    public void logoutUser() {
        if(EventScheduleController.tryResetSideContent() == null) {
            ButtonType buttonType = MessageHelper.showLogoutUserMessage();
            if (buttonType.getText().equals("Cancel")) {
                //do nothing
            } else if (buttonType.getText().equals("Change User")) {
                PlanchesterGUI.showLogin();
            } else if (buttonType.getText().equals("Close Planchester")) {
                DatabaseSessionManager.closeSession();
                Platform.exit();
                System.exit(0);
            }
        }
    }
}
