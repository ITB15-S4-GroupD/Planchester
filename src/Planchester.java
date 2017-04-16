import Presentation.PlanchesterGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Locale;

public class Planchester extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(Locale.UK);

        PlanchesterGUI gui = new PlanchesterGUI();
        gui.start(primaryStage);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
