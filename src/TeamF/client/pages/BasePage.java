package TeamF.client.pages;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import jfxtras.labs.scene.control.BigDecimalField;
import TeamF.client.helper.AlertHelper;
import TeamF.client.pages.interfaces.BasePageControl;
import javax.lang.model.type.NullType;
import java.util.List;

public abstract class BasePage<R, V, L, S> extends BorderPane implements BasePageControl {
    /**
     * Extends the behaviour of the page without loosing the base functionality and without inheriting from BasePage.
     * It also separates the the action behaviour from the GUI components and does not allow to access the GUI components
     * so easily.
     *
     * The handlers should not display any visual dialogs. Instead they should return the error messages to the GUI (not an Exception)
     *
     * Can be implemented by the page which inherits from it, but it's not necessary
     * e.g. NotImplemented can be thrown (not recommend nor necessary).
     * The standard behaviour doesn't call the attached event handler when it's not implemented.
     * (implemented actions should be documented in JavaDoc)
     */
    protected PageAction<Void, NullType> _initialize;
    /**
     * return null to show that an error occurred
     */
    protected PageAction<L, S> _load;
    /**
     * return null to show that an error occurred
     */
    protected PageAction<R, V> _create;
    /**
     * return null to show that an error occurred
     */
    protected PageAction<R, V> _edit;
    /**
     * return null to show that an error occurred
     */
    protected PageAction<R, V> _update;
    /**
     * return null to show that an error occurred
     */
    protected PageAction<R, V> _delete;
    /**
     * return null to show that an error occurred
     */
    protected PageAction<R, V> _save;
    /**
     * return null to show that an error occurred
     */
    protected PageAction<Boolean, V> _exit;

    /**
     * sets the event handler for initializing the component (is only called once)
     * @param action event handler
     */
    public void setOnInitialize(PageAction<Void, NullType> action) {
        _initialize = action;
    }

    /**
     * sets the event handler for loading the component (is called on every page reload)
     * @param action event handler
     */
    public void setOnLoad(PageAction<L, S> action) {
        _load = action;
    }

    /**
     * sets the event handler for creating an object in the component
     * @param action event handler
     */
    public void setOnCreate(PageAction<R, V> action) {
        _create = action;
    }

    /**
     * sets the event handler for editing an object in the component
     * @param action event handler
     */
    public void setOnEdit(PageAction<R, V> action) {
        _edit = action;
    }

    /**
     * sets the event handler for updating an object in the component
     * @param action event handler
     */
    public void setOnUpdate(PageAction<R,V> action) {
        _update = action;
    }

    /**
     * sets the event handler for deleting an object in the component
     * @param action event handler  return null for R when the object was deleted
     */
    public void setOnDelete(PageAction<R, V> action) {
        _delete = action;
    }

    /**
     * sets the event handler for saving an object in the component
     * @param action event handler
     */
    public void setOnSave(PageAction<R, V> action) {
        _save = action;
    }

    /**
     * sets the event handler for exiting the component
     * @param action event handler
     */
    public void setOnExit(PageAction<Boolean, V> action) {
        _exit = action;
    }

    protected void validate(List<? extends Control> fields) {
        for (Control field : fields) {
            if(field instanceof TextField) {
                validate(((TextField) field).getText(), field);
            } else if(field instanceof BigDecimalField) {
                validate(((BigDecimalField) field).getText(), field);
            }
        }
    }

    protected void validate(String value, Control control) {
        if(value == null || value.isEmpty()) {
            control.setStyle("-fx-border-color: red");
        } else {
            control.setStyle("-fx-border-color: green");
        }
    }

    public void showSuccessMessage(String headerText, String contentText) {
        AlertHelper.showSuccessMessage(headerText, contentText, this);
    }

    public void showErrorMessage(String headerText, String contentText) {
        AlertHelper.showErrorMessage(headerText, contentText, this);
    }

    public Boolean showWarningMessage(String headerText, String contentText, String okButtonLabel) {
        return AlertHelper.showWarningMessage(headerText, contentText, okButtonLabel, this);
    }

    public void showValuesMissingMessage() {
        AlertHelper.showValuesMissingMessage(this);
    }

    public void showTryAgainLaterErrorMessage() {
        AlertHelper.showTryAgainLaterErrorMessage(this);
    }
}