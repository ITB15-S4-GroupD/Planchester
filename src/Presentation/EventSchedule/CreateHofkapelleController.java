package Presentation.EventSchedule;

import Utils.Enum.EventType;
import javafx.fxml.FXML;

import javax.xml.bind.ValidationException;

/**
 * Created by Ina on 09.04.2017.
 */

public class CreateHofkapelleController extends CreateController {

    @Override
    @FXML
    protected void insertEventDuty() throws ValidationException {
        super.setEventType(EventType.Hofkapelle);
        super.insertEventDuty();
    }
}