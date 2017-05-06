package Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.HashMap;
import java.util.Map;

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

    public static void showInformationMessage(String informationMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Planchester Message");
        alert.setHeaderText(informationMessage);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
    }

    public static void showWarningMusicianCapacityMessage(HashMap<String, Integer> musicanCapacityMap) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Planchester Warning");

        StringBuilder warning = new StringBuilder();
        warning.append("There are not enough musicians available on this date. Following instruments are missing:\n");

        for (Map.Entry<String, Integer> musicianCapacity : musicanCapacityMap.entrySet()) {
            warning.append(musicianCapacity.getKey() + ": " + musicianCapacity.getValue() + "\n");
        }
        alert.setHeaderText(warning.toString());
        alert.showAndWait();
    }

    public static ButtonType showLogoutUserMessage() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Planchester Logout Warning");
        alert.setHeaderText("Are you sure?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(new ButtonType("Change User"));
        alert.getButtonTypes().add(new ButtonType("Close Planchester"));
        alert.getButtonTypes().add(new ButtonType("Cancel"));
        alert.showAndWait();
        return alert.getResult();
    }
}