package TeamF.client.pages;

import java.util.List;

public abstract class BaseTablePage<R, V, LL, L, S> extends BasePage<R, V, L, S> {
    protected PageAction<R, V> _onChange;
    protected PageAction<List<LL>, S> _loadList;

    /**
     * sets the event handler for recognizing when a table content changed
     * @param action
     */
    public void setOnListChanged(PageAction<R, V> action) {
        _onChange = action;
    }

    /**
     * sets the event handler for changing the list content of the component
     * @param action
     */
    public void setOnLoadList(PageAction<List<LL>, S> action) {
        _loadList = action;
    }
}
