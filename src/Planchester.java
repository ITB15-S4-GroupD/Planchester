import Presentation.PlanchesterGUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class Planchester extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PlanchesterGUI gui = new PlanchesterGUI();
        gui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
