package TeamF.client.configuration;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Configuration implements IConfigurable {
    // config symbols
    protected static final String ROOT_DIR = "Root_Dir";
    protected static final String START_URI = "Start_URI";
    protected static final String APP_NAME = "App_Name";
    protected static final String HEIGHT = "Height";
    protected static final String WIDTH = "Width";
    protected static final String SHOW_CLOSE_WARNING = "Show_Close_Warning";
    protected static final String SHOW_MENU_BAR = "Show_Menu_Bar";
    protected static final String OPEN_IN_WEBBROWSER = "Open_In_Webbrowser";

    private HashMap<String, Object> _keyValues;

    public Configuration() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        _keyValues = new HashMap<>();
        setRootDir("./");
        setStartURI("http://localhost:8080/views/example.html");
        setAppName("ConcertMaster");
        setHeight(-1);
        setWidth(-1);
        setShowCloseWarning(true);
        setShowMenuBar(true);
        setOpenInWebbrowser(false);
    }

    public String getRootDir() {
        return (String) _keyValues.get(ROOT_DIR);
    }

    public String getStartURI() {
        return (String) _keyValues.get(START_URI);
    }

    public String getAppName() {
        return (String) _keyValues.get(APP_NAME);
    }

    public int getHeight() {
        return (int) _keyValues.get(HEIGHT);
    }

    public int getWidth() {
        return (int) _keyValues.get(WIDTH);
    }

    public boolean getShowCloseWarning() {
        return (boolean) _keyValues.get(SHOW_CLOSE_WARNING);
    }

    public boolean getShowMenuBar() {
        return (boolean) _keyValues.get(SHOW_MENU_BAR);
    }

    public boolean getOpenInWebbrowser() {
        return (boolean) _keyValues.get(OPEN_IN_WEBBROWSER);
    }

    public void setRootDir(String rootDir) {
        _keyValues.put(ROOT_DIR, rootDir);
    }

    public void setStartURI(String startURI) {
        _keyValues.put(START_URI, startURI);
    }

    public void setAppName(String appName) {
        _keyValues.put(APP_NAME, appName);
    }

    public void setHeight(int height) {
        _keyValues.put(HEIGHT, height);
    }

    public void setWidth(int width) {
        _keyValues.put(WIDTH, width);
    }

    public void setShowCloseWarning(boolean showCloseWarning) {
        _keyValues.put(SHOW_CLOSE_WARNING, showCloseWarning);
    }

    public void setShowMenuBar(boolean showMenuBar) {
        _keyValues.put(SHOW_MENU_BAR, showMenuBar);
    }

    public void setOpenInWebbrowser(boolean openInWebbrowser) {
        _keyValues.put(OPEN_IN_WEBBROWSER, openInWebbrowser);
    }

    @Override
    public Set<Map.Entry<String, Object>> getList() {
        return  _keyValues.entrySet();
    }
}
