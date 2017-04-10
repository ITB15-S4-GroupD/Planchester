import Domain.Model.InstrumentEntity;
import Presentation.PlanchesterGUI;
import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class Planchester extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");// populates the data of the
        // configuration file

        // creating seession factory object
        SessionFactory factory = cfg.buildSessionFactory();

        // creating session object
        Session session = factory.openSession();

        // creating transaction object
        Transaction t = session.beginTransaction();

        Query query = session.createQuery("FROM InstrumentEntity");
        java.util.List list = query.list();

        for(Object o : list){
            InstrumentEntity ie = (InstrumentEntity) o;

            System.out.println(ie.getDescription());
        }

        t.commit();
        session.close();
        */

        PlanchesterGUI gui = new PlanchesterGUI();
        gui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
