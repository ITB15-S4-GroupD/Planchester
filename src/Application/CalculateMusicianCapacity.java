package Application;

/**
 * Created by Scapegoat on 4/24/2017.
 *
 * @Project Planchester
 */
public class CalculateMusicianCapacity {

//    private void getMusicians() {
//
//    }
//
//    //TODO: alles was rot ist
//    private HashMap<String, Integer> getInstrumentation(List<EventDutyModel> events) {
//        HashMap<String, Integer> instrumentation = new HashMap<>();
//        instrumentation.put("flute", 0);
//        instrumentation.put("oboe", 0);
//        instrumentation.put("clarinet", 0);
//        instrumentation.put("bassoon", 0);
//        instrumentation.put("horn", 0);
//        instrumentation.put("trumpet", 0);
//        instrumentation.put("trombone", 0);
//        instrumentation.put("tube", 0);
//        instrumentation.put("violin1", 0);
//        instrumentation.put("violin2", 0);
//        instrumentation.put("viola", 0);
//        instrumentation.put("violincello", 0);
//        instrumentation.put("doublebass", 0);
//        instrumentation.put("kettledrum", 0);
//        instrumentation.put("percussion", 0);
//        instrumentation.put("harp", 0);
//
//        List<MusicalWork> musicalWorks;
//        for (EventDutyModel e : events) {
//            musicalWorks = e.getMusicalWork();//bzw getMusicalWorkID
//            for (MusicalWork m : musicalWorks) {
//
//                int ID = getInstrumentationID(m);
//
//                HashMap<String, Integer> wood = getAmountWoodInstrumentation(ID);
//                if (instrumentation.get("flute") < wood.get("flute")) instrumentation.put("flute", wood.get("flute"));
//                if (instrumentation.get("oboe") < wood.get("oboe")) instrumentation.put("oboe", wood.get("oboe"));
//                if (instrumentation.get("clarinet") < wood.get("clarinet"))
//                    instrumentation.put("clarinet", wood.get("clarinet"));
//                if (instrumentation.get("bassoon") < wood.get("bassoon"))
//                    instrumentation.put("bassoon", wood.get("bassoon"));
//
//                HashMap<String, Integer> string = getAmountStringInstrumentation(ID);
//                if (instrumentation.get("violin1") < wood.get("violin1"))
//                    instrumentation.put("violin1", wood.get("violin1"));
//                if (instrumentation.get("violin2") < wood.get("violin2"))
//                    instrumentation.put("violin2", wood.get("violin2"));
//                if (instrumentation.get("viola") < wood.get("viola")) instrumentation.put("viola", wood.get("viola"));
//                if (instrumentation.get("violincello") < wood.get("violincello"))
//                    instrumentation.put("violincello", wood.get("violincello"));
//                if (instrumentation.get("doublebass") < wood.get("doublebass"))
//                    instrumentation.put("doublebass", wood.get("doublebass"));
//
//                HashMap<String, Integer> percussion = getAmountPercussionInstrumentation(ID);
//                if (instrumentation.get("kettledrum") < wood.get("kettledrum"))
//                    instrumentation.put("kettledrum", wood.get("kettledrum"));
//                if (instrumentation.get("percussion") < wood.get("percussion"))
//                    instrumentation.put("percussion", wood.get("percussion"));
//                if (instrumentation.get("harp") < wood.get("harp")) instrumentation.put("harp", wood.get("harp"));
//
//                HashMap<String, Integer> brass = getAmountBrassInstrumentation(ID);
//                if (instrumentation.get("horn") < wood.get("horn")) instrumentation.put("horn", wood.get("horn"));
//                if (instrumentation.get("trumpet") < wood.get("trumpet"))
//                    instrumentation.put("trumpet", wood.get("trumpet"));
//                if (instrumentation.get("trombone") < wood.get("trombone"))
//                    instrumentation.put("trombone", wood.get("trombone"));
//                if (instrumentation.get("tube") < wood.get("tube")) instrumentation.put("tube", wood.get("tube"));
//            }
//        }
//        return instrumentation;
//    }
//
//    private int getInstrumentationID(int musicalWorkID) {
//        int ID;
//
//        Session session = DatabaseConnectionHandler.beginSession();
//        List list = session.createQuery("SELECT instrumentationId FROM MusicalWorkEntity WHERE musicalWorkId=" + musicalWorkID).list();
//
//        ID = (int) list.get(0);
//
//        DatabaseConnectionHandler.closeSession(session);
//
//        return ID;
//    }
//
//    private HashMap<String, Integer> getAmountWoodInstrumentation(int ID) {
//        Session session = DatabaseConnectionHandler.beginSession();
//        List list = session.createQuery("FROM WoodInstrumentationEntity WHERE woodInstrumentationId=" + ID).list();
//        HashMap<String, Integer> wood = new HashMap<>();
//        instrumentation.put("flute", list.get(1));
//        instrumentation.put("oboe", list.get(2));
//        instrumentation.put("clarinet", list.get(3));
//        instrumentation.put("bassoon", list.get(4));
//
//        DatabaseConnectionHandler.closeSession(session);
//        return wood;
//    }
//
//    private HashMap<String, Integer> getAmountStringInstrumentation(int ID) {
//        Session session = DatabaseConnectionHandler.beginSession();
//        List list = session.createQuery("FROM StringInstrumentationEntity WHERE stringInstrumentationId=" + ID).list();
//        HashMap<String, Integer> string = new HashMap<>();
//        instrumentation.put("violin1", list.get(1));
//        instrumentation.put("violin2", list.get(2));
//        instrumentation.put("viola", list.get(3));
//        instrumentation.put("violincello", list.get(4));
//        instrumentation.put("doublebass", list.get(5));
//
//
//        DatabaseConnectionHandler.closeSession(session);
//        return string;
//    }
//
//    private HashMap<String, Integer> getAmountPercussionInstrumentation(int ID) {
//        Session session = DatabaseConnectionHandler.beginSession();
//        List list = session.createQuery("FROM PercussionInstrumentationEntity WHERE percussionInstrumentationId=" + ID).list();
//        HashMap<String, Integer> percussion = new HashMap<>();
//        instrumentation.put("kettledrum", list.get(1));
//        instrumentation.put("percussion", list.get(2));
//        instrumentation.put("harp", list.get(3));
//
//        DatabaseConnectionHandler.closeSession(session);
//        return percussion;
//    }
//
//    private HashMap<String, Integer> getAmountBrassInstrumentation(int ID) {
//        Session session = DatabaseConnectionHandler.beginSession();
//        List list = session.createQuery("FROM BrassInstrumentationEntity WHERE brassInstrumentationId=" + ID).list();
//        HashMap<String, Integer> bras = new HashMap<>();
//        instrumentation.put("horn", list.get(1));
//        instrumentation.put("trumpet", list.get(2));
//        instrumentation.put("trombone", list.get(3));
//        instrumentation.put("tube", list.get(4));
//
//        DatabaseConnectionHandler.closeSession(session);
//        return bras;
//    }
//
//    private void checkCapacity(EventDutyRDBMapper event) {
//        //EventDutyRDBMapper.getEventDutyInRange()
//    }
//
//    private List<EventDutyModel> getAllEventsDuring(Calendar start, Calendar end) {
//        Session session = DatabaseConnectionHandler.beginSession();
//        List list = session.createQuery("FROM EventDutyEntity WHERE starttime < '"
//                + DateHelper.convertCalendarToTimestamp(start)
//                + "' AND endtime > '"
//                + DateHelper.convertCalendarToTimestamp(end)
//                + "' OR (starttime >= '"
//                + DateHelper.convertCalendarToTimestamp(start)
//                + "' AND starttime <= '"
//                + DateHelper.convertCalendarToTimestamp(end)
//                + "')OR (endtime >= '"
//                + DateHelper.convertCalendarToTimestamp(start)
//                + "' AND endtime <= '"
//                + DateHelper.convertCalendarToTimestamp(end)
//                + "')").list();
//
//        List<EventDutyModel> eventList = new ArrayList<EventDutyModel>();
//        for (Object o : list) {
//            EventDutyEntity eventDutyEntity = (EventDutyEntity) o;
//            eventList.add(new EventDutyModel(eventDutyEntity));
//        }
//        DatabaseConnectionHandler.closeSession(session);
//        return eventList;
//    }
}