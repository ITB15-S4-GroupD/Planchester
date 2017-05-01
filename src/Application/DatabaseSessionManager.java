package Application;

import Persistence.DatabaseConnectionHandler;

/**
 * Created by julia on 26.04.2017.
 */
public class DatabaseSessionManager {

    public static void readConfiguration() {
        DatabaseConnectionHandler.getInstance().readConfiguration();
    }
}