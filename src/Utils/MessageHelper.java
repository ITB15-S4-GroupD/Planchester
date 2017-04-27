package Utils;

import javafx.scene.control.Alert;

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
}
