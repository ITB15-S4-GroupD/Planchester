import javafx.fxml.FXML;
import jfxtras.scene.control.agenda.Agenda;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Controller {

    @FXML
    private void selectionChanged() {
        System.out.println("selection changed");
    }

    @FXML
    private void navigateOneWeekBackClicked() {
        Agenda agenda = (Agenda) Main.scene.lookup("#agenda");

        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();

        agenda.setDisplayedLocalDateTime(displayedDate.minus(7, ChronoUnit.DAYS));
    }

    @FXML
    private void navigateOneWeekForwardClicked() {
        Agenda agenda = (Agenda) Main.scene.lookup("#agenda");

        LocalDateTime displayedDate = agenda.getDisplayedLocalDateTime();

        agenda.setDisplayedLocalDateTime(displayedDate.plus(7, ChronoUnit.DAYS));
    }

}
