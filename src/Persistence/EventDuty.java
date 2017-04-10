package Persistence;

import Domain.Model.EventDutyEntity;
import Domain.Model.InstrumentEntity;
import Domain.PresentationModels.EventDutyDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by julia on 10.04.2017.
 */
public class EventDuty {

    public static List<EventDutyDTO> getAllEventDuty() {
        List<EventDutyDTO> eventDutyList = new ArrayList<>();

        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");// populates the data of the configuration file

        // creating session factory object
        SessionFactory factory = cfg.buildSessionFactory();

        // creating session object
        Session session = factory.openSession();

        // creating transaction object
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("FROM EventDutyEntity");
        List list = query.list();

        for(Object o : list){
            EventDutyEntity eventDuty = (EventDutyEntity) o;
            eventDutyList.add(new EventDutyDTO(eventDuty));
        }

        transaction.commit();
        session.close();
        return eventDutyList;
    }

    public static List<EventDutyDTO> getEventDutyByWeek(Date start, Date end) {
       //TODO Julia
        return null;
    }
}
