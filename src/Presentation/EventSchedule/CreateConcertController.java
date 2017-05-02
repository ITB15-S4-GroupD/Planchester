package Presentation.EventSchedule;

import Utils.Enum.EventType;
import javafx.fxml.FXML;
import javax.xml.bind.ValidationException;

/**
 * Created by Christina on 20.04.2017.
 */

public class CreateConcertController extends CreateController {

    @Override
    @FXML
    protected void insertEventDuty() throws ValidationException {
        super.setEventType(EventType.Concert);
        super.insertEventDuty();
    }
}