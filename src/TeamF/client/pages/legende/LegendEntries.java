package TeamF.client.pages.legende;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Sample data for a table view
 */
public class LegendEntries {
    final static public ObservableList data = FXCollections.observableArrayList(
            new LegendEntries("Opera", "OP", "DODGERBLUE"),
            new LegendEntries("Concert", "CO", "SADDLEBROWN"),
            new LegendEntries("Hofkapelle", "HK", "DARKMAGENTA"),
            new LegendEntries("Rehearsal", "RH", "DARKSLATEBLUE"),
            new LegendEntries("Tour", "T", "DARKSALMON"),
            new LegendEntries("Non Musical Event", "NM", "DARKCYAN")
    );

    final private String eventtype;
    final private String code;
    final private String color;

    public LegendEntries(String name, String owesMe, String color) {
        this.eventtype = name;
        this.code = owesMe;
        this.color = color;
    }

    public String getEventtype() {
        return eventtype;
    }

    public String getCode() {
        return code;
    }

    public String getColor() {
        return color;
    }
}