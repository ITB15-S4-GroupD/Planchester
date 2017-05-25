package Presentation.EventSchedule;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import java.util.EnumSet;

/**
 * Created by timorzipa on 25/05/2017.
 */
public class WishRadioButtonCell<S,T extends Enum<T>> extends TableCell<S,T> {

    private EnumSet<T> enumeration;

    public WishRadioButtonCell(EnumSet<T> enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    protected void updateItem(T item, boolean empty)
    {
        super.updateItem(item, empty);
        if (!empty)
        {
            // gui setup
            HBox hb = new HBox(7);
            hb.setAlignment(Pos.CENTER);
            final ToggleGroup group = new ToggleGroup();

            // create a radio button for each 'element' of the enumeration
            for (Enum<T> enumElement : enumeration) {
                RadioButton radioButton = new RadioButton(enumElement.toString());
                radioButton.setUserData(enumElement);
                radioButton.setToggleGroup(group);
                hb.getChildren().add(radioButton);
                if (enumElement.equals(item)) {
                    radioButton.setSelected(true);
                }
            }

            // issue events on change of the selected radio button
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

                @SuppressWarnings("unchecked")
                @Override
                public void changed(ObservableValue<? extends Toggle> observable,
                                    Toggle oldValue, Toggle newValue) {
                    getTableView().edit(getIndex(), getTableColumn());
                    WishRadioButtonCell.this.commitEdit((T) newValue.getUserData());
                }
            });
            setGraphic(hb);
        }
    }
}
