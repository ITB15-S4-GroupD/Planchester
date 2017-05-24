package TeamF.client.pages.musicianmanagement;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import TeamF.client.controls.numberfield.NumberField;
import TeamF.client.entities.KeyValuePair;

public class MusicianInstrumentEntity {
    private int _specialInstrumentationID = 0;
    private ComboBox<KeyValuePair> _instrumentComboBox;
    private Pane _pane;

    public MusicianInstrumentEntity(int specialInstrumentationID, ComboBox<KeyValuePair> sectionTypeComboBox, Pane pane) {
        _instrumentComboBox = sectionTypeComboBox;
        _specialInstrumentationID = specialInstrumentationID;
        _pane = pane;
    }

    public int getSpecialInstrumentationID() {
        return _specialInstrumentationID;
    }

    public ComboBox<KeyValuePair> getSectionTypeComboBox() {
        return _instrumentComboBox;
    }


    public Pane getPane() {
        return _pane;
    }
}
