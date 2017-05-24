package TeamF.client.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AlertHelper {
    public static void showSuccessMessage(String headerText, String contentText, Pane pane) {
        showMessage("Success", headerText, contentText, Alert.AlertType.INFORMATION, new ImageView("check.png"), null, pane);
    }

    public static void showErrorMessage(String headerText, String contentText, Pane pane) {
        showMessage("Error", headerText, contentText, Alert.AlertType.ERROR, null, null, pane);
    }

    public static Boolean showWarningMessage(String headerText, String contentText, String okButtonLabel, Pane pane) {
        ButtonType okButton = new ButtonType(okButtonLabel);
        ButtonType cancelButton = new ButtonType("Cancel");
        List<ButtonType> buttonList = new LinkedList<>();
        buttonList.add(okButton);
        buttonList.add(cancelButton);

        Optional optional = showMessage("Warning", headerText, contentText, Alert.AlertType.WARNING, null, buttonList, pane);

        if(optional != null) {
            if(optional.get().equals(okButton)) {
                return true;
            } else {
                return false;
            }
        }

        return null;
    }

    public static void showValuesMissingMessage(Pane pane) {
        showMessage("Values Missing","Please fill in all the information in the form.", "You can not save. Please fill in missing data!", Alert.AlertType.WARNING, null, null, pane);
    }

    public static void showTryAgainLaterErrorMessage(Pane pane) {
        showMessage("Error", "The data could not be loaded.\nPlease try it again later or contact your System-Administrator!", "", Alert.AlertType.ERROR, null, null, pane);
    }

    private static Optional showMessage(String title, String headerText, String contentText, Alert.AlertType type, ImageView icon, List<ButtonType> buttonTypeList, Pane pane) {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        pane.setDisable(true);

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.getDialogPane().setMaxHeight(graphicsDevice.getDisplayMode().getHeight() * 0.9);
        alert.getDialogPane().setMaxWidth(graphicsDevice.getDisplayMode().getWidth() * 0.9);

        if(icon != null) {
            alert.setGraphic(icon);
        }

        alert.setHeaderText(headerText);

        Label contentTextElement = new Label(contentText);
        contentTextElement.setWrapText(true);
        ScrollPane contentTextElementScrollPane = new ScrollPane(contentTextElement);
        alert.getDialogPane().setContent(contentTextElementScrollPane);

        if(buttonTypeList != null) {
            alert.getButtonTypes().setAll(buttonTypeList);
        }

        Optional optional = alert.showAndWait();
        alert.close();

        pane.setDisable(false);

        return optional;
    }
}
