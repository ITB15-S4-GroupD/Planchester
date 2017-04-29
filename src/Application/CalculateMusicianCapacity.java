package Application;

import Domain.EventDutyModel;
import Persistence.DatabaseConnectionHandler;
import Persistence.Entities.EventDutyEntity;
import Persistence.EventDutyRDBMapper;
import Utils.DateHelper;
import org.hibernate.Session;

import java.util.*;

/**
 * Created by Scapegoat on 4/24/2017.
 *
 * @Project Planchester
 */
public class CalculateMusicianCapacity {

    public HashMap<String, Integer> checkCapacity(Calendar start, Calendar end) {
        List<Integer> allEvents = getAllEventsDuring(start, end);
        HashMap<String, Integer> maxInstrumentation = getInstrumentation(allEvents);
        HashMap<String, Integer> allMusicians = getAllMusicians();

        String key;
        HashMap<String, Integer> difference = allMusicians;

        for (Map.Entry<String, Integer> data : allMusicians.entrySet()) {
            if (maxInstrumentation.entrySet().contains(data.getKey())) {
                /**
                 if (maxInstrumentation.get(data.getKey()) > data.getValue()) {
                 return false;
                 }*/

                if (maxInstrumentation.get(data.getKey()) < data.getValue()) {
                    return null;
                } else {
                    difference.put(data.getKey(), (data.getValue() - maxInstrumentation.get(data.getValue())));
                }
            }
        }
        return difference;
    }

    private HashMap<String, Integer> getAllMusicians() {

        LinkedList<String> instruments = new LinkedList<>();
        instruments.addFirst("flute");
        instruments.add("violin1");
        instruments.add("violin2");
        instruments.add("viola");
        instruments.add("doublebass");
        instruments.add("oboe");
        instruments.add("clarinet");
        instruments.add("bassoon");
        instruments.add("horn");
        instruments.add("trumpet");
        instruments.add("trombone");
        instruments.add("tube");
        instruments.add("kettledrum");
        instruments.add("harp");
        instruments.add("violincello");
        instruments.addLast("percussion");
        int maxPartPlayers;
        int partID;
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        HashMap<String, Integer> availableMusicians = null;
        for (String currentPart : instruments) {

            List<Integer> tempPartID = session.createQuery("select PartEntity.partId from PartEntity join PartEntity on PartEntity.partType = PartTypeEntity.partTypeId where PartTypeEntity.partType like '" + currentPart + "'").list();
            partID = tempPartID.get(0);
            List<Integer> amount = session.createQuery("select count (*) from MusicianPartEntity where PartEntity.partId is '" + String.valueOf(partID) + "'").list();
            maxPartPlayers = amount.get(0);

            availableMusicians.put(currentPart, maxPartPlayers);
        }
        return availableMusicians;
    }

    private HashMap<String, Integer> getInstrumentation(List<Integer> eventsIDs) {
        HashMap<String, Integer> instrumentation = new HashMap<>();
        instrumentation.put("flute", 0);
        instrumentation.put("oboe", 0);
        instrumentation.put("clarinet", 0);
        instrumentation.put("bassoon", 0);
        instrumentation.put("horn", 0);
        instrumentation.put("trumpet", 0);
        instrumentation.put("trombone", 0);
        instrumentation.put("tube", 0);
        instrumentation.put("violin1", 0);
        instrumentation.put("violin2", 0);
        instrumentation.put("viola", 0);
        instrumentation.put("violincello", 0);
        instrumentation.put("doublebass", 0);
        instrumentation.put("kettledrum", 0);
        instrumentation.put("percussion", 0);
        instrumentation.put("harp", 0);

        // List<MusicalWork> musicalWorks;
        for (Integer eventID : eventsIDs) {
            //musicalWorks = e.getMusicalWork();//bzw getMusicalWorkID
            //for (MusicalWork m : musicalWorks) {
            List<Integer> ID = getInstrumentationID(eventID);

            for (Integer id : ID) {

                HashMap<String, Integer> wood = getAmountWoodInstrumentation(id);
                if (instrumentation.get("flute") < wood.get("flute"))
                    instrumentation.put("flute", wood.get("flute"));
                if (instrumentation.get("oboe") < wood.get("oboe")) instrumentation.put("oboe", wood.get("oboe"));
                if (instrumentation.get("clarinet") < wood.get("clarinet"))
                    instrumentation.put("clarinet", wood.get("clarinet"));
                if (instrumentation.get("bassoon") < wood.get("bassoon"))
                    instrumentation.put("bassoon", wood.get("bassoon"));

                HashMap<String, Integer> string = getAmountStringInstrumentation(id);
                if (instrumentation.get("violin1") < wood.get("violin1"))
                    instrumentation.put("violin1", wood.get("violin1"));
                if (instrumentation.get("violin2") < wood.get("violin2"))
                    instrumentation.put("violin2", wood.get("violin2"));
                if (instrumentation.get("viola") < wood.get("viola"))
                    instrumentation.put("viola", wood.get("viola"));
                if (instrumentation.get("violincello") < wood.get("violincello"))
                    instrumentation.put("violincello", wood.get("violincello"));
                if (instrumentation.get("doublebass") < wood.get("doublebass"))
                    instrumentation.put("doublebass", wood.get("doublebass"));

                HashMap<String, Integer> percussion = getAmountPercussionInstrumentation(id);
                if (instrumentation.get("kettledrum") < wood.get("kettledrum"))
                    instrumentation.put("kettledrum", wood.get("kettledrum"));
                if (instrumentation.get("percussion") < wood.get("percussion"))
                    instrumentation.put("percussion", wood.get("percussion"));
                if (instrumentation.get("harp") < wood.get("harp")) instrumentation.put("harp", wood.get("harp"));

                HashMap<String, Integer> brass = getAmountBrassInstrumentation(id);
                if (instrumentation.get("horn") < wood.get("horn")) instrumentation.put("horn", wood.get("horn"));
                if (instrumentation.get("trumpet") < wood.get("trumpet"))
                    instrumentation.put("trumpet", wood.get("trumpet"));
                if (instrumentation.get("trombone") < wood.get("trombone"))
                    instrumentation.put("trombone", wood.get("trombone"));
                if (instrumentation.get("tube") < wood.get("tube")) instrumentation.put("tube", wood.get("tube"));
            }
        }
        return instrumentation;
    }

    private List<Integer> getInstrumentationID(int eventID) {
        List<Integer> ID = null;


        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List<Integer> alternative = session.createQuery("SELECT alternativeInstrumentation FROM EventDutyMusicalWorkEntity WHERE eventDuty=" + eventID).list();

        List<Integer> list = session.createQuery("SELECT musicalWork FROM EventDutyMusicalWorkEntity WHERE eventDuty=" + eventID + " AND alternativeInstrumentation=null").list();

        for (Integer integer : list) {
            List<Integer> add = session.createQuery("SELECT instrumentationId FROM MusicalWorkEntity WHERE musicalWorkId=" + list).list();
            ID.addAll(add);

        }


        ID.addAll(alternative);

        DatabaseConnectionHandler.getInstance().commitTransaction();

        return ID;
    }

    private HashMap<String, Integer> getAmountWoodInstrumentation(int ID) {
        HashMap<String, Integer> instrumentation = new HashMap<>();
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List<Integer> list = session.createQuery("FROM WoodInstrumentationEntity WHERE woodInstrumentationId=" + ID).list();
        HashMap<String, Integer> wood = new HashMap<>();
        instrumentation.put("flute", list.get(1));
        instrumentation.put("oboe", list.get(2));
        instrumentation.put("clarinet", list.get(3));
        instrumentation.put("bassoon", list.get(4));

        DatabaseConnectionHandler.getInstance().commitTransaction();
        return wood;
    }

    private HashMap<String, Integer> getAmountStringInstrumentation(int ID) {
        HashMap<String, Integer> instrumentation = new HashMap<>();
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List<Integer> list = session.createQuery("FROM StringInstrumentationEntity WHERE stringInstrumentationId=" + ID).list();
        HashMap<String, Integer> string = new HashMap<>();
        instrumentation.put("violin1", list.get(1));
        instrumentation.put("violin2", list.get(2));
        instrumentation.put("viola", list.get(3));
        instrumentation.put("violincello", list.get(4));
        instrumentation.put("doublebass", list.get(5));


        DatabaseConnectionHandler.getInstance().commitTransaction();
        return string;
    }

    private HashMap<String, Integer> getAmountPercussionInstrumentation(int ID) {
        HashMap<String, Integer> instrumentation = new HashMap<>();
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List<Integer> list = session.createQuery("FROM PercussionInstrumentationEntity WHERE percussionInstrumentationId=" + ID).list();
        HashMap<String, Integer> percussion = new HashMap<>();
        instrumentation.put("kettledrum", list.get(1));
        instrumentation.put("percussion", list.get(2));
        instrumentation.put("harp", list.get(3));

        DatabaseConnectionHandler.getInstance().commitTransaction();
        return percussion;
    }

    private HashMap<String, Integer> getAmountBrassInstrumentation(int ID) {
        HashMap<String, Integer> instrumentation = new HashMap<>();
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List<Integer> list = session.createQuery("FROM BrassInstrumentationEntity WHERE brassInstrumentationId=" + ID).list();
        HashMap<String, Integer> bras = new HashMap<>();
        instrumentation.put("horn", list.get(1));
        instrumentation.put("trumpet", list.get(2));
        instrumentation.put("trombone", list.get(3));
        instrumentation.put("tube", list.get(4));

        DatabaseConnectionHandler.getInstance().commitTransaction();
        return bras;
    }


    private List<Integer> getAllEventsDuring(Calendar start, Calendar end) {
        Session session = DatabaseConnectionHandler.getInstance().beginTransaction();
        List<Integer> list = session.createQuery("Select eventDutyId FROM EventDutyEntity WHERE starttime < '"
                + DateHelper.convertCalendarToTimestamp(start)
                + "' AND endtime > '"
                + DateHelper.convertCalendarToTimestamp(end)
                + "' OR (starttime >= '"
                + DateHelper.convertCalendarToTimestamp(start)
                + "' AND starttime <= '"
                + DateHelper.convertCalendarToTimestamp(end)
                + "')OR (endtime >= '"
                + DateHelper.convertCalendarToTimestamp(start)
                + "' AND endtime <= '"
                + DateHelper.convertCalendarToTimestamp(end)
                + "')").list();
/*
        List<EventDutyModel> eventList = new ArrayList<EventDutyModel>();
        for (Object obj : list) {
            EventDutyEntity eventDutyEntity = (EventDutyEntity) obj;
            eventList.add(new EventDutyModel());
        }
*/
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return list;
    }
}