package TeamF.client.helper.gui;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import TeamF.client.controls.numberfield.NumberField;
import TeamF.client.entities.KeyValuePair;

public class SpecialInstrumentationEntity {
    private int _specialInstrumentationID = 0;
    private ComboBox<KeyValuePair> _sectionTypeComboBox;
    private ComboBox<KeyValuePair> _specialInstrumentationTextField;
    private NumberField _specialInstrumentationNumberField;
    private Pane _pane;

    public SpecialInstrumentationEntity(int specialInstrumentationID, ComboBox<KeyValuePair> sectionTypeComboBox, ComboBox<KeyValuePair> specialInstrumentTextField, NumberField specialInstrumentNumberField, Pane pane) {
        _sectionTypeComboBox = sectionTypeComboBox;
        _specialInstrumentationTextField = specialInstrumentTextField;
        _specialInstrumentationNumberField = specialInstrumentNumberField;
        _specialInstrumentationID = specialInstrumentationID;
        _pane = pane;
    }

    public int getSpecialInstrumentationID() {
        return _specialInstrumentationID;
    }

    public ComboBox<KeyValuePair> getSectionTypeComboBox() {
        return _sectionTypeComboBox;
    }

    public ComboBox<KeyValuePair> getSpecialInstrumentationComboBox() {
        return _specialInstrumentationTextField;
    }

    public NumberField getSpecialInstrumentationNumberField() {
        return _specialInstrumentationNumberField;
    }

    public Pane getPane() {
        return _pane;
    }
}
