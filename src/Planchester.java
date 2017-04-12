import Domain.Model.InstrumentEntity;
import Presentation.PlanchesterGUI;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

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
