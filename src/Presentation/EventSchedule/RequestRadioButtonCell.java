package Presentation.EventSchedule;

import Utils.Enum.RequestTypeGUI;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import java.util.EnumSet;

/**
 * Created by timorzipa on 25/05/2017.
 */
public class RequestRadioButtonCell<S,T extends Enum<T>> extends TableCell<S,T> {

    private EnumSet<RequestTypeGUI> enumeration;

    public RequestRadioButtonCell(EnumSet<RequestTypeGUI> enumeration) {
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
            for (Enum<RequestTypeGUI> enumElement : enumeration) {
                RadioButton radioButton = new RadioButton(enumElement.toString());
                radioButton.setUserData(enumElement);
                radioButton.setToggleGroup(group);
                hb.getChildren().add(radioButton);
                if (enumElement.equals(item)) {
                    radioButton.setSelected(true);
                }
            }

            // issue events on change of the selected radio button
            group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                RequestEntry wishEntry = (RequestEntry) getTableView().getItems().get(getIndex());
                wishEntry.setEdited(true);
                wishEntry.setRequestType((RequestTypeGUI) newValue.getUserData());
            });
            setGraphic(hb);
        }
    }
}
