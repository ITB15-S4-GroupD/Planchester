package TeamF.client.singletons;

import TeamF.client.pages.legende.LegendTable;

public class LegendSingleton {
    private static LegendTable _legendTable;

    private LegendSingleton() {
    }

    public static LegendTable getInstance() {
        if (_legendTable == null) {
            _legendTable = new LegendTable();
        }

        return _legendTable;
    }
}