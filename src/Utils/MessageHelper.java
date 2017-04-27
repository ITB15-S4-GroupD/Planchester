package Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Created by julia on 27.04.2017.
 */
public class MessageHelper {
    public static void showErrorAlertMessage(String errormessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Planchester Error");
        alert.setHeaderText(errormessage);
        alert.showAndWait();
    }

    public static ButtonType showConfirmationMessage(String confirmationMessage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Planchester Message");
        alert.setHeaderText(confirmationMessage);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        alert.showAndWait();

        return alert.getResult();
    }
}