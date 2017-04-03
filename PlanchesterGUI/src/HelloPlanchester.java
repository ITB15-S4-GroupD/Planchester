import javafx.fxml.FXML;

/**
 * Created by julia on 26.03.2017.
 */
public class HelloPlanchester {

    public static void helloTeam() {
        System.out.println("Hello Team D!");
        System.out.println("Welcome");
        System.out.println("Hello Christina! Can you push me :D?");
    }

    //change2
    public static void helloTeamD() {
        System.out.println("Hello Team D 2.0!");
    }

    @FXML
    private void selectionChanged() {
        // Button was clicked, do something...
        System.out.print("selectionChanged");
    }
}