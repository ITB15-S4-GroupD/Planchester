package TeamF.client.controls.numberfield;

import jfxtras.labs.scene.control.BigDecimalField;
import TeamF.client.exceptions.NumberRangeException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.*;

public class NumberField extends BigDecimalField {
    private boolean _hasDoubleValue = false;

    public NumberField() {
        setStepwidth(new BigDecimal(1));
    }

    public NumberField(Double value, double minValue, double maxValue) throws NumberRangeException {
        this(value);
        setMinValue(new BigDecimal(minValue));
        setMaxValue(new BigDecimal(maxValue));
    }

    public NumberField(Integer value, int minValue, int maxValue) throws NumberRangeException {
        this(value);
        setStyle("-fx-opacity: 1");
        setMinValue(new BigDecimal(minValue));
        setMaxValue(new BigDecimal(maxValue));
    }

    public NumberField(Double value) {
        this();
        setFormat(new DecimalFormat());

        if(value != null) {
            setNumber(new BigDecimal(value));
        } else {
            setNumber(null);
        }

        setHasDoubleValue(true);
    }

    public NumberField(Integer value) {
        this();
        DecimalFormat format = new DecimalFormat("#");
        setFormat(format);

        if(value != null) {
            setNumber(new BigDecimal(value));
        } else {
            setNumber(null);
        }
    }

    public boolean getHasDoubleValue() {
        return _hasDoubleValue;
    }

    private void setHasDoubleValue(boolean value) {
        _hasDoubleValue = value;
    }

    @Override
    public String getUserAgentStylesheet() {
        // workaround for jfxtras
        final String Style = ClassLoader.getSystemResource("TeamF/style/BigDecimalField.css").toExternalForm();
        return Style;
    }
}
