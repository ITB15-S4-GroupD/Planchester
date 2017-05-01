package Application;

import Domain.EventDutyModel;
import Domain.MusicalWorkModel;
import Persistence.*;
import Persistence.Entities.*;

import java.util.*;

/**
 * Created by Garvin u. Malena on 4/24/2017.
 *
 * @Project Planchester
 */
public class CalculateMusicianCapacity {

    private static PersistanceFacade persistanceFacade = new PersistanceFacade();
    private static HashMap<String, Integer> difference = new HashMap<String, Integer>();

    public static HashMap<String, Integer> checkCapacityInRange(Calendar eventstart, Calendar eventend) {
        List<EventDutyModel> allEvents = getAllEventsDuring(eventstart, eventend);
        HashMap<String, Integer> requiredDutyInstrumentation = getInstrumentationByMusicalWorks(allEvents);
        HashMap<String, Integer> allMusicians = getAllMusicians();

        Iterator it = requiredDutyInstrumentation.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = (String) pair.getKey();
            Integer value = (Integer) pair.getValue();

            if(allMusicians.get(key) < value) {
                difference.put(key, (value - allMusicians.get(key)));
            }
        }
        return difference;
    }

    private static HashMap<String, Integer> getAllMusicians() {
        LinkedList<String> instruments = new LinkedList<String>();
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

        int maxPartPlayers = 0;
        HashMap<String, Integer> availableMusicians = new HashMap<String, Integer>();
        for (String currentPart : instruments) {
            Integer amount = MusicianPartRDBMapper.getCountedMusicians(currentPart);
            availableMusicians.put(currentPart, amount);
        }
        return availableMusicians;
    }

    private static HashMap<String, Integer> getInstrumentationByMusicalWorks(List<EventDutyModel> events) {
        //Alernative Insturmentation can be ignored (this is not in the 1. timebox...)
       HashMap<String, Integer> instrumentationForAllEvents = new HashMap<String, Integer>();
        instrumentationForAllEvents.put("violin1", 0);
        instrumentationForAllEvents.put("violin2", 0);
        instrumentationForAllEvents.put("viola", 0);
        instrumentationForAllEvents.put("violincello", 0);
        instrumentationForAllEvents.put("doublebass", 0);
        instrumentationForAllEvents.put("oboe", 0);
        instrumentationForAllEvents.put("flute", 0);
        instrumentationForAllEvents.put("clarinet", 0);
        instrumentationForAllEvents.put("bassoon", 0);
        instrumentationForAllEvents.put("horn", 0);
        instrumentationForAllEvents.put("trumpet", 0);
        instrumentationForAllEvents.put("trombone", 0);
        instrumentationForAllEvents.put("tube", 0);
        instrumentationForAllEvents.put("kettledrum", 0);
        instrumentationForAllEvents.put("percussion", 0);
        instrumentationForAllEvents.put("harp", 0);

        for (EventDutyModel eventDuty : events) {
            HashMap<String, Integer> instrumentation = new HashMap<String, Integer>();
            instrumentation.put("violin1", 0);
            instrumentation.put("violin2", 0);
            instrumentation.put("viola", 0);
            instrumentation.put("violincello", 0);
            instrumentation.put("doublebass", 0);
            instrumentation.put("oboe", 0);
            instrumentation.put("flute", 0);
            instrumentation.put("clarinet", 0);
            instrumentation.put("bassoon", 0);
            instrumentation.put("horn", 0);
            instrumentation.put("trumpet", 0);
            instrumentation.put("trombone", 0);
            instrumentation.put("tube", 0);
            instrumentation.put("kettledrum", 0);
            instrumentation.put("percussion", 0);
            instrumentation.put("harp", 0);

            List<Integer> instrumentationIDs = getInstrumentationIDsForMusicalWorks(eventDuty);

            for (Integer instrumentationID : instrumentationIDs) {

                HashMap<String, Integer> wood = getAmountWoodInstrumentation(instrumentationID);
                if (instrumentation.get("flute") < wood.get("flute"))
                    instrumentation.put("flute", wood.get("flute"));
                if (instrumentation.get("oboe") < wood.get("oboe"))
                    instrumentation.put("oboe", wood.get("oboe"));
                if (instrumentation.get("clarinet") < wood.get("clarinet"))
                    instrumentation.put("clarinet", wood.get("clarinet"));
                if (instrumentation.get("bassoon") < wood.get("bassoon"))
                    instrumentation.put("bassoon", wood.get("bassoon"));

                HashMap<String, Integer> string = getAmountStringInstrumentation(instrumentationID);
                if (instrumentation.get("violin1") < string.get("violin1"))
                    instrumentation.put("violin1", string.get("violin1"));
                if (instrumentation.get("violin2") < string.get("violin2"))
                    instrumentation.put("violin2", string.get("violin2"));
                if (instrumentation.get("viola") < string.get("viola"))
                    instrumentation.put("viola", string.get("viola"));
                if (instrumentation.get("violincello") < string.get("violincello"))
                    instrumentation.put("violincello", string.get("violincello"));
                if (instrumentation.get("doublebass") < string.get("doublebass"))
                    instrumentation.put("doublebass", string.get("doublebass"));

                HashMap<String, Integer> percussion = getAmountPercussionInstrumentation(instrumentationID);
                if (instrumentation.get("kettledrum") < percussion.get("kettledrum"))
                    instrumentation.put("kettledrum", percussion.get("kettledrum"));
                if (instrumentation.get("percussion") < percussion.get("percussion"))
                    instrumentation.put("percussion", percussion.get("percussion"));
                if (instrumentation.get("harp") < percussion.get("harp"))
                    instrumentation.put("harp", percussion.get("harp"));

                HashMap<String, Integer> brass = getAmountBrassInstrumentation(instrumentationID);
                if (instrumentation.get("horn") < brass.get("horn"))
                    instrumentation.put("horn", brass.get("horn"));
                if (instrumentation.get("trumpet") < brass.get("trumpet"))
                    instrumentation.put("trumpet", brass.get("trumpet"));
                if (instrumentation.get("trombone") < brass.get("trombone"))
                    instrumentation.put("trombone", brass.get("trombone"));
                if (instrumentation.get("tube") < brass.get("tube"))
                    instrumentation.put("tube", brass.get("tube"));
            }

            Iterator it = instrumentation.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                instrumentationForAllEvents.put((String)pair.getKey(), instrumentationForAllEvents.get(pair.getKey()) + (Integer) pair.getValue());
            }
        }
        return instrumentationForAllEvents;
    }

    private static List<Integer> getInstrumentationIDsForMusicalWorks(EventDutyModel eventDutyModel) {
        List<Integer> ids = new ArrayList<Integer>();
        List<Integer> musicalWorks = EventDutyMusicalWorkRDBMapper.getAllMusicalWorkIDsByEventID(eventDutyModel.getEventDutyId());
        for (Integer musicalWorkID : musicalWorks) {
            List<Integer> instrumentationIDs = MusicalWorkRDBMapper.getInstrumentationIDByMusicalWorkID(musicalWorkID);
            ids.addAll(instrumentationIDs);
        }
        return ids;
    }

    private static List<EventDutyModel> getAllEventsDuring(Calendar eventstart, Calendar eventend) {
        List<EventDutyEntity> eventDuties = EventDutyRDBMapper.getAllEventDutiesInRange(eventstart, eventend);

        List<EventDutyModel> eventDutyModelList = new ArrayList<EventDutyModel>();
        for(EventDutyEntity eventDutyEntity : eventDuties) {
            eventDutyModelList.add(createEventDutyModel(eventDutyEntity));
        }
        return eventDutyModelList;
    }

    private static HashMap<String, Integer> getAmountWoodInstrumentation(int instrumentationIDs) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        WoodInstrumentationEntity woodInstrumentationEntity = (WoodInstrumentationEntity) persistanceFacade.get(instrumentationIDs, WoodInstrumentationEntity.class);
        instrumentationMap.put("flute", woodInstrumentationEntity.getFlute());
        instrumentationMap.put("oboe", woodInstrumentationEntity.getOboe());
        instrumentationMap.put("clarinet", woodInstrumentationEntity.getClarinet());
        instrumentationMap.put("bassoon", woodInstrumentationEntity.getBassoon());
        return instrumentationMap;
    }

    private static HashMap<String, Integer> getAmountStringInstrumentation(int instrumentationIDs) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        StringInstrumentationEntity stringInstrumentationEntity = (StringInstrumentationEntity) persistanceFacade.get(instrumentationIDs, StringInstrumentationEntity.class);
        instrumentationMap.put("violin1", stringInstrumentationEntity.getViolin1());
        instrumentationMap.put("violin2", stringInstrumentationEntity.getViolin2());
        instrumentationMap.put("viola", stringInstrumentationEntity.getViola());
        instrumentationMap.put("violincello", stringInstrumentationEntity.getViolincello());
        instrumentationMap.put("doublebass", stringInstrumentationEntity.getDoublebass());
        return instrumentationMap;
    }

    private static HashMap<String, Integer> getAmountPercussionInstrumentation(int instrumentationIDs) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        PercussionInstrumentationEntity percussionInstrumentationEntity = (PercussionInstrumentationEntity) persistanceFacade.get(instrumentationIDs, PercussionInstrumentationEntity.class);
        instrumentationMap.put("kettledrum", percussionInstrumentationEntity.getKettledrum());
        instrumentationMap.put("percussion", percussionInstrumentationEntity.getPercussion());
        instrumentationMap.put("harp", percussionInstrumentationEntity.getHarp());
        return instrumentationMap;
    }

    private static HashMap<String, Integer> getAmountBrassInstrumentation(int instrumentationIDs) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        BrassInstrumentationEntity brassInstrumentationEntity = (BrassInstrumentationEntity) persistanceFacade.get(instrumentationIDs, BrassInstrumentationEntity.class);
        instrumentationMap.put("horn", brassInstrumentationEntity.getHorn());
        instrumentationMap.put("trumpet", brassInstrumentationEntity.getTrumpet());
        instrumentationMap.put("trombone", brassInstrumentationEntity.getTrombone());
        instrumentationMap.put("tube", brassInstrumentationEntity.getTube());
        return instrumentationMap;
    }

    private static EventDutyModel createEventDutyModel(EventDutyEntity eventDutyEntity) {
        EventDutyModel eventDutyModel = new EventDutyModel();
        eventDutyModel.setEventDutyId(eventDutyEntity.getEventDutyId());
        eventDutyModel.setName(eventDutyEntity.getName());
        eventDutyModel.setDescription(eventDutyEntity.getDescription());
        eventDutyModel.setStarttime(eventDutyEntity.getStarttime());
        eventDutyModel.setEndtime(eventDutyEntity.getEndtime());
        eventDutyModel.setEventType(eventDutyEntity.getEventType().toString());
        eventDutyModel.setEventStatus(eventDutyEntity.getEventStatus().toString());
        eventDutyModel.setConductor(eventDutyEntity.getConductor());
        eventDutyModel.setLocation(eventDutyEntity.getLocation());
        eventDutyModel.setDefaultPoints(eventDutyEntity.getDefaultPoints());
        eventDutyModel.setInstrumentation(eventDutyEntity.getInstrumentation());
        eventDutyModel.setRehearsalFor(eventDutyEntity.getRehearsalFor());
        return eventDutyModel;
    }

    private static MusicalWorkModel createMusicalWorkModel(MusicalWorkEntity musicalWorkEntity) {
        MusicalWorkModel musicalWorkModel = new MusicalWorkModel();
        musicalWorkModel.setId(musicalWorkEntity.getMusicalWorkId());
        musicalWorkModel.setInstrumentationId(musicalWorkEntity.getInstrumentationId());
        musicalWorkModel.setName(musicalWorkEntity.getName());
        musicalWorkModel.setComposer(musicalWorkEntity.getComposer());
        return musicalWorkModel;
    }
}