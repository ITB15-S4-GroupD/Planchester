package Presentation;

import Application.AccountAdministrationManager;
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
        AccountRole accountRole = AccountAdministrationManager.getInstance().getAccountRole();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        eventscheduleTab.setContent(FXMLLoader.load(getClass().getResource("EventSchedule/EventSchedule.fxml")));
        dutyRosterTab.setContent(FXMLLoader.load(getClass().getResource("DutyRoster/DutyRoster.fxml")));

        if(!AccountRole.Manager.equals(accountRole) && !AccountRole.Administrator.equals(accountRole)) {
            tabPane.getTabs().remove(useradministrationTab);
        }

        if(AccountRole.Musician.equals(accountRole)
                || accountRole.equals(AccountRole.Section_representative)
                || accountRole.equals(AccountRole.Substitute)) {
            tabPane.getTabs().remove(musicalWorksTab);
            tabPane.getTabs().remove(instrumentsTab);
        } if(AccountRole.Music_librarian.equals(accountRole)) {
            tabPane.getTabs().remove(dutyRosterTab);
            tabPane.getTabs().remove(instrumentsTab);
        } else if(AccountRole.Orchestral_facility_manager.equals(accountRole)) {
            tabPane.getTabs().remove(dutyRosterTab);
            tabPane.getTabs().remove(musicalWorksTab);
        } else if(AccountRole.Section_representative.equals(accountRole)) {
            tabPane.getTabs().remove(musicalWorksTab);
            tabPane.getTabs().remove(instrumentsTab);
        }
    }

    @FXML
    public void logoutUser() {
        if(EventScheduleController.tryResetSideContent() == null) {
            ButtonType buttonType = MessageHelper.showLogoutUserMessage();
            if (buttonType.getText().equals("Cancel")) {
                //do nothing
            } else if (buttonType.getText().equals("Change User")) {
                AccountAdministrationManager.getInstance().resetUser();
                PlanchesterGUI.showLogin();
            } else if (buttonType.getText().equals("Close Planchester")) {
                DatabaseSessionManager.closeSession();
                Platform.exit();
                System.exit(0);
            }
        }
    }
}