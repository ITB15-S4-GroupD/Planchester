package Application;

import Domain.EventDutyModel;
import Domain.MusicalWorkModel;
import Persistence.*;
import Persistence.Entities.*;
import org.hibernate.Session;

import java.util.*;

/**
 * Created by Garvin u. Malena on 4/24/2017.
 *
 * @Project Planchester
 */
public class CalculateMusicianCapacity {

    PersistanceFacade persistanceFacade = new PersistanceFacade();
    HashMap<String, Integer> difference = new HashMap<>();

    public HashMap<String, Integer> checkCapacityInRange(Calendar eventstart, Calendar eventend) {
        List<EventDutyModel> allEvents = getAllEventsDuring(eventstart, eventend);
        HashMap<String, Integer> maxInstrumentation = getInstrumentationByMusicalWorks(allEvents);
        HashMap<String, Integer> allMusicians = getAllMusicians();

        String key;
        difference = allMusicians;
        for (Map.Entry<String, Integer> neededInstrumentAmount : allMusicians.entrySet()) {
            if (maxInstrumentation.entrySet().contains(neededInstrumentAmount.getKey())) {
                if (maxInstrumentation.get(neededInstrumentAmount.getKey()) < neededInstrumentAmount.getValue()) {
                    return null;
                } else {
                    difference.put(neededInstrumentAmount.getKey(), (neededInstrumentAmount.getValue() - maxInstrumentation.get(neededInstrumentAmount.getValue())));
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
            List<Integer> amount = MusicianPartRDBMapper.getCountedMusicians(currentPart);
            if(amount != null || amount.isEmpty()) {
                maxPartPlayers = amount.get(0);
                availableMusicians.put(currentPart, maxPartPlayers);
            } else {
                //no one plays this instrument
                difference.put(currentPart, null);
            }
        }
        return availableMusicians;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private HashMap<String, Integer> getInstrumentationByMusicalWorks(List<EventDutyModel> events) {
        //Alernative Insturmentation can be ignored (this is not in the 1. timebox...)
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

        for (EventDutyModel eventDuty : events) {
            List<Integer> instrumentationIDs = getInstrumentationIDsForMusicalWorks(eventDuty);

            for (Integer instrumentationID : instrumentationIDs) {

                HashMap<String, Integer> wood = getAmountWoodInstrumentation(instrumentationID);
                if (instrumentation.get("flute") < wood.get("flute"))
                    instrumentation.put("flute", wood.get("flute"));
                if (instrumentation.get("oboe") < wood.get("oboe")) instrumentation.put("oboe", wood.get("oboe"));
                if (instrumentation.get("clarinet") < wood.get("clarinet"))
                    instrumentation.put("clarinet", wood.get("clarinet"));
                if (instrumentation.get("bassoon") < wood.get("bassoon"))
                    instrumentation.put("bassoon", wood.get("bassoon"));

                HashMap<String, Integer> string = getAmountStringInstrumentation(instrumentationID);
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

                HashMap<String, Integer> percussion = getAmountPercussionInstrumentation(instrumentationID);
                if (instrumentation.get("kettledrum") < wood.get("kettledrum"))
                    instrumentation.put("kettledrum", wood.get("kettledrum"));
                if (instrumentation.get("percussion") < wood.get("percussion"))
                    instrumentation.put("percussion", wood.get("percussion"));
                if (instrumentation.get("harp") < wood.get("harp")) instrumentation.put("harp", wood.get("harp"));

                HashMap<String, Integer> brass = getAmountBrassInstrumentation(instrumentationID);
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

    private List<Integer> getInstrumentationIDsForMusicalWorks(EventDutyModel eventDutyModel) {
        List<Integer> ids = null;
        List<Integer> musicalWorks = EventDutyMusicalWorkRDBMapper.getAllMusicalWorkIDsByEventID(eventDutyModel.getEventDutyId());
        for (Integer musicalWorkID : musicalWorks) {
            ids.addAll(MusicalWorkRDBMapper.getInstrumentationIDByMusicalWorkID(musicalWorkID));
        }
        return ids;
    }

    private List<EventDutyModel> getAllEventsDuring(Calendar eventstart, Calendar eventend) {
        List<EventDutyEntity> eventDuties = EventDutyRDBMapper.getAllEventDutiesInRange(eventstart, eventend);

        List<EventDutyModel> eventDutyModelList = new ArrayList<EventDutyModel>();
        for(EventDutyEntity eventDutyEntity : eventDuties) {
            eventDutyModelList.add(createEventDutyModel(eventDutyEntity));
        }
        DatabaseConnectionHandler.getInstance().commitTransaction();
        return eventDutyModelList;
    }

    private HashMap<String, Integer> getAmountWoodInstrumentation(int instrumentationIDs) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        WoodInstrumentationEntity woodInstrumentationEntity = (WoodInstrumentationEntity) persistanceFacade.get(instrumentationIDs, WoodInstrumentationEntity.class);
        instrumentationMap.put("flute", woodInstrumentationEntity.getFlute());
        instrumentationMap.put("oboe", woodInstrumentationEntity.getOboe());
        instrumentationMap.put("clarinet", woodInstrumentationEntity.getClarinet());
        instrumentationMap.put("bassoon", woodInstrumentationEntity.getBassoon());
        return instrumentationMap;
    }

    private HashMap<String, Integer> getAmountStringInstrumentation(int instrumentationIDs) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        StringInstrumentationEntity stringInstrumentationEntity = (StringInstrumentationEntity) persistanceFacade.get(instrumentationIDs, StringInstrumentationEntity.class);
        instrumentationMap.put("violin1", stringInstrumentationEntity.getViolin1());
        instrumentationMap.put("violin2", stringInstrumentationEntity.getViolin2());
        instrumentationMap.put("viola", stringInstrumentationEntity.getViola());
        instrumentationMap.put("violincello", stringInstrumentationEntity.getViolincello());
        instrumentationMap.put("doublebass", stringInstrumentationEntity.getDoublebass());
        return instrumentationMap;
    }

    private HashMap<String, Integer> getAmountPercussionInstrumentation(int instrumentationIDs) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        PercussionInstrumentationEntity percussionInstrumentationEntity = (PercussionInstrumentationEntity) persistanceFacade.get(instrumentationIDs, PercussionInstrumentationEntity.class);
        instrumentationMap.put("kettledrum", percussionInstrumentationEntity.getKettledrum());
        instrumentationMap.put("percussion", percussionInstrumentationEntity.getPercussion());
        instrumentationMap.put("harp", percussionInstrumentationEntity.getHarp());
        return instrumentationMap;
    }

    private HashMap<String, Integer> getAmountBrassInstrumentation(int instrumentationIDs) {
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