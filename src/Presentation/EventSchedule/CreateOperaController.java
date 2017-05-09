package Presentation.EventSchedule;

import Utils.Enum.EventType;
import javafx.fxml.FXML;

import javax.xml.bind.ValidationException;

/**
 * Created by Ina on 08.04.2017.
 */
public class CreateOperaController extends CreateController{

    @FXML
    @Override
    protected void insertEventDuty() throws ValidationException {
        super.setEventType(EventType.Opera);
        super.insertEventDuty();
    }

    @FXML
    @Override
    public void editInstrumentation() {
        super.setMusicalWorkMulitpleSelection(false);
        super.editInstrumentation();
    }
}