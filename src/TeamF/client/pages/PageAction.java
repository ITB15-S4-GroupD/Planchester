package TeamF.client.pages;

/**
 * Event for an action on a page
 * @param <R> return value
 * @param <V> input value
 */
public abstract class PageAction<R, V> {
    public abstract R doAction(V value);
}
