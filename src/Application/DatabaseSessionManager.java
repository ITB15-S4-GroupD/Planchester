package Application;

import Persistence.DatabaseConnectionHandler;

/**
 * Created by julia on 26.04.2017.
 */
public class DatabaseSessionManager {

    public static void beginSession() {
        DatabaseConnectionHandler.getInstance().readConfiguration();
    }

    public static void closeSession() {
        DatabaseConnectionHandler.getInstance().closeSession();
    }
}