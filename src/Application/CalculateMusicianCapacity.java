package Application;

import Domain.Models.EventDutyModel;
import Domain.Models.MusicalWorkModel;
import Persistence.*;
import Persistence.Entities.*;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;

import java.util.*;

import static Utils.DateHelper.convertCalendarToTimestamp;

/**
 * Created by Garvin u. Malena on 4/24/2017.
 *
 * @Project Planchester
 */
public class CalculateMusicianCapacity {

    private static HashMap<String, Integer> difference = new HashMap<>();

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
        LinkedList<String> instruments = new LinkedList<>();
        instruments.addFirst("Flute");
        instruments.add("First Violin");
        instruments.add("Second Violin");
        instruments.add("Viola");
        instruments.add("Violoncello");
        instruments.add("Double Bass");
        instruments.add("English Horn");
        instruments.add("French Horn");
        instruments.add("Basset Horn");
        instruments.add("Trumpet");
        instruments.add("Trombone");
        instruments.add("Bass Trombone");
        instruments.add("Contrabass Trombone");
        instruments.add("Tuba");
        instruments.add("Kettledrum");
        instruments.add("Percussion");
        instruments.add("Harp");
        instruments.add("Piccolo");
        instruments.add("Oboe");
        instruments.add("Clarinet");
        instruments.add("Bass Clarinet");
        instruments.add("Bassoon");
        instruments.add("Heckelphone");
        instruments.add("Contrabassoon");
        instruments.add("Heckelphone");
        instruments.add("Saxophone");

        int maxPartPlayers = 0;
        HashMap<String, Integer> availableMusicians = new HashMap<>();
        for (String currentPart : instruments) {

            PersistanceFacade<PartTypeEntity> partTypeEntityPersistanceFacade = new PersistanceFacade<>(PartTypeEntity.class);
            PartTypeEntity partTypeEntity = partTypeEntityPersistanceFacade.get(p -> p.getPartType().toLowerCase().equals(currentPart.toLowerCase()));

            PersistanceFacade<PartEntity> partEntityPersistanceFacade = new PersistanceFacade<>(PartEntity.class);
            PartEntity partEntity = partEntityPersistanceFacade.get(p -> p.getPartType() == partTypeEntity.getPartTypeId());

            Integer amount;
            if(partEntity != null) {
                PersistanceFacade<MusicianPartEntity> musicianPartEntityPersistanceFacade = new PersistanceFacade<>(MusicianPartEntity.class);
                amount = musicianPartEntityPersistanceFacade.list(p -> p.getPart() == partEntity.getPartId()).size();
            } else {
                amount = 0;
            }
            availableMusicians.put(currentPart, amount);
        }
        return availableMusicians;
    }

    private static HashMap<String, Integer> getInstrumentationByMusicalWorks(List<EventDutyModel> events) {
        //Alernative Insturmentation can be ignored (this is not in the 1. timebox...)
       HashMap<String, Integer> instrumentationForAllEvents = new HashMap<>();
        instrumentationForAllEvents.put("Flute", 0);
        instrumentationForAllEvents.put("First Violin", 0);
        instrumentationForAllEvents.put("Second Violin", 0);
        instrumentationForAllEvents.put("Viola", 0);
        instrumentationForAllEvents.put("Violoncello", 0);
        instrumentationForAllEvents.put("Double Bass", 0);
        instrumentationForAllEvents.put("English Horn", 0);
        instrumentationForAllEvents.put("French Horn", 0);
        instrumentationForAllEvents.put("Basset Horn", 0);
        instrumentationForAllEvents.put("Trumpet", 0);
        instrumentationForAllEvents.put("Trombone", 0);
        instrumentationForAllEvents.put("Bass Trombone", 0);
        instrumentationForAllEvents.put("Contrabass Trombone", 0);
        instrumentationForAllEvents.put("Tuba", 0);
        instrumentationForAllEvents.put("Kettledrum", 0);
        instrumentationForAllEvents.put("Percussion", 0);
        instrumentationForAllEvents.put("Harp", 0);
        instrumentationForAllEvents.put("Piccolo", 0);
        instrumentationForAllEvents.put("Oboe", 0);
        instrumentationForAllEvents.put("Clarinet", 0);
        instrumentationForAllEvents.put("Bass Clarinet", 0);
        instrumentationForAllEvents.put("Bassoon", 0);
        instrumentationForAllEvents.put("Heckelphone", 0);
        instrumentationForAllEvents.put("Contrabassoon", 0);
        instrumentationForAllEvents.put("Heckelphone", 0);
        instrumentationForAllEvents.put("Saxophone", 0);

        for (EventDutyModel eventDuty : events) {
            HashMap<String, Integer> instrumentation = new HashMap<String, Integer>();
            instrumentation.put("Flute", 0);
            instrumentation.put("First Violin", 0);
            instrumentation.put("Second Violin", 0);
            instrumentation.put("Viola", 0);
            instrumentation.put("Violoncello", 0);
            instrumentation.put("Double Bass", 0);
            instrumentation.put("English Horn", 0);
            instrumentation.put("French Horn", 0);
            instrumentation.put("Basset Horn", 0);
            instrumentation.put("Trumpet", 0);
            instrumentation.put("Trombone", 0);
            instrumentation.put("Bass Trombone", 0);
            instrumentation.put("Contrabass Trombone", 0);
            instrumentation.put("Tuba", 0);
            instrumentation.put("Kettledrum", 0);
            instrumentation.put("Percussion", 0);
            instrumentation.put("Harp", 0);
            instrumentation.put("Piccolo", 0);
            instrumentation.put("Oboe", 0);
            instrumentation.put("Clarinet", 0);
            instrumentation.put("Bass Clarinet", 0);
            instrumentation.put("Bassoon", 0);
            instrumentation.put("Heckelphone", 0);
            instrumentation.put("Contrabassoon", 0);
            instrumentation.put("Heckelphone", 0);
            instrumentation.put("Saxophone", 0);

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
        List<Integer> ids = new ArrayList<>();

        PersistanceFacade<EventDutyMusicalWorkEntity> eventDutyMusicalWorkEntityPersistanceFacade = new PersistanceFacade<>(EventDutyMusicalWorkEntity.class);
        List<EventDutyMusicalWorkEntity> eventDutyMusicalWorkEntities = eventDutyMusicalWorkEntityPersistanceFacade.list(p -> p.getEventDuty() == eventDutyModel.getEventDutyId());

        for (EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity : eventDutyMusicalWorkEntities) {

            PersistanceFacade<MusicalWorkEntity> musicalWorkEntityPersistanceFacade = new PersistanceFacade<>(MusicalWorkEntity.class);
            MusicalWorkEntity musicalWork = musicalWorkEntityPersistanceFacade.get(p -> p.getMusicalWorkId() == eventDutyMusicalWorkEntity.getMusicalWork());

            ids.add(musicalWork.getInstrumentationId());
        }
        return ids;
    }

    private static List<EventDutyModel> getAllEventsDuring(Calendar eventstart, Calendar eventend) {


        PersistanceFacade<EventDutyEntity> eventDutyEntityPersistanceFacade = new PersistanceFacade<>(EventDutyEntity.class);
        List<EventDutyEntity> eventDuties = eventDutyEntityPersistanceFacade.list(p -> p.getStarttime().after(convertCalendarToTimestamp(eventstart))
                && p.getStarttime().before(convertCalendarToTimestamp(eventend)));

        List<EventDutyModel> eventDutyModelList = new ArrayList<>();
        for(EventDutyEntity eventDutyEntity : eventDuties) {
            eventDutyModelList.add(createEventDutyModel(eventDutyEntity));
        }
        return eventDutyModelList;
    }

    private static HashMap<String, Integer> getAmountWoodInstrumentation(int instrumentationID) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        PersistanceFacade<WoodInstrumentationEntity> persistanceFacade = new PersistanceFacade<>(WoodInstrumentationEntity.class);
        WoodInstrumentationEntity woodInstrumentationEntity = persistanceFacade.get(instrumentationID);
        instrumentationMap.put("flute", woodInstrumentationEntity.getFlute());
        instrumentationMap.put("oboe", woodInstrumentationEntity.getOboe());
        instrumentationMap.put("clarinet", woodInstrumentationEntity.getClarinet());
        instrumentationMap.put("bassoon", woodInstrumentationEntity.getBassoon());
        return instrumentationMap;
    }

    private static HashMap<String, Integer> getAmountStringInstrumentation(int instrumentationID) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        PersistanceFacade<StringInstrumentationEntity> persistanceFacade = new PersistanceFacade<>(StringInstrumentationEntity.class);
        StringInstrumentationEntity stringInstrumentationEntity = persistanceFacade.get(instrumentationID);
        instrumentationMap.put("violin1", stringInstrumentationEntity.getViolin1());
        instrumentationMap.put("violin2", stringInstrumentationEntity.getViolin2());
        instrumentationMap.put("viola", stringInstrumentationEntity.getViola());
        instrumentationMap.put("violincello", stringInstrumentationEntity.getViolincello());
        instrumentationMap.put("doublebass", stringInstrumentationEntity.getDoublebass());
        return instrumentationMap;
    }

    private static HashMap<String, Integer> getAmountPercussionInstrumentation(int instrumentationID) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        PersistanceFacade<PercussionInstrumentationEntity> persistanceFacade = new PersistanceFacade<>(PercussionInstrumentationEntity.class);
        PercussionInstrumentationEntity percussionInstrumentationEntity = persistanceFacade.get(instrumentationID);
        instrumentationMap.put("kettledrum", percussionInstrumentationEntity.getKettledrum());
        instrumentationMap.put("percussion", percussionInstrumentationEntity.getPercussion());
        instrumentationMap.put("harp", percussionInstrumentationEntity.getHarp());
        return instrumentationMap;
    }

    private static HashMap<String, Integer> getAmountBrassInstrumentation(int instrumentationID) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        PersistanceFacade<BrassInstrumentationEntity> persistanceFacade = new PersistanceFacade<>(BrassInstrumentationEntity.class);
        BrassInstrumentationEntity brassInstrumentationEntity = persistanceFacade.get(instrumentationID);
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
        eventDutyModel.setStartTime(eventDutyEntity.getStarttime());
        eventDutyModel.setEndTime(eventDutyEntity.getEndtime());
        eventDutyModel.setEventType(EventType.valueOf(eventDutyEntity.getEventType().toString()));
        eventDutyModel.setEventStatus(EventStatus.valueOf(eventDutyEntity.getEventStatus().toString()));
        eventDutyModel.setConductor(eventDutyEntity.getConductor());
        eventDutyModel.setLocation(eventDutyEntity.getLocation());
        eventDutyModel.setPoints(eventDutyEntity.getDefaultPoints());
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