package Application;

import java.time.Month;
import java.time.Year;
import java.util.Calendar;

/**
 * Created by julia on 28.04.2017.
 */
public class PublishEventSchedule {
    /*



		        List list = session.createQuery("FROM EventDutyEntity WHERE MONTH(starttime) = '"
                + month
                + "' AND YEAR(starttime) = '"
                + year
                + "'").list();
     */

    public static void publish(Year year, Month month) {
        //Calculate first and last day of month
        Calendar firstOfMonth = Calendar.getInstance();
        firstOfMonth.set(year.getValue(), month.getValue(), 1);
        Calendar lastOfMonth = Calendar.getInstance();
        lastOfMonth.set(year.getValue(), month.getValue(), lastOfMonth.getActualMaximum(Calendar.DATE));

        //Lade alle Termine in diesem Monat

        //für alle Termine in diesem Monat: gehe jeden Termin einzeln in einer for-Schleife durch und schaue ob alle Felder außer die nullable Felder natürlich, gesetzt sind
        //falls bei einem Termin ein feld nicht gesetzt ist: return warnung
    }
}
