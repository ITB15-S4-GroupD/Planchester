package Application;

import Domain.Models.EventDutyModel;
import Persistence.*;
import Persistence.Entities.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Garvin u. Malena on 4/24/2017.
 *
 * @Project Planchester
 */
public class CalculateMusicianCapacity {

    private static HashMap<String, Integer> difference = new HashMap<>();
    private static PersistanceFacade<EventDutyEntity> eventDutyEntityPersistanceFacade = new PersistanceFacade<>(EventDutyEntity.class);

    public static HashMap<String, Integer> checkCapacityInRange(Timestamp eventstart, Timestamp eventend) {
        List<EventDutyEntity> allEvents = getAllEventsDuring(eventstart, eventend);
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
        // wood
        instruments.add("Flute");
        instruments.add("Oboe");
        instruments.add("Clarinet");
        instruments.add("Bassoon");

        // string
        instruments.add("First Violin");
        instruments.add("Second Violin");
        instruments.add("Viola");
        instruments.add("Violoncello");
        instruments.add("Double Bass");

        // percussion
        instruments.add("Kettledrum");
        instruments.add("Percussion");
        instruments.add("Harp");

        // brass
        instruments.add("French Horn");
        instruments.add("Trumpet");
        instruments.add("Trombone");
        instruments.add("Tuba");

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

    private static HashMap<String, Integer> getInstrumentationByMusicalWorks(List<EventDutyEntity> events) {
        HashMap<String, Integer> instrumentation = new HashMap<>();
        for (EventDutyEntity eventDuty : events) {
            // wood
            instrumentation.put("Flute", 0);
            instrumentation.put("Oboe", 0);
            instrumentation.put("Clarinet", 0);
            instrumentation.put("Bassoon", 0);

            // string
            instrumentation.put("First Violin", 0);
            instrumentation.put("Second Violin", 0);
            instrumentation.put("Viola", 0);
            instrumentation.put("Violoncello", 0);
            instrumentation.put("Double Bass", 0);

            // percussion
            instrumentation.put("Kettledrum", 0);
            instrumentation.put("Percussion", 0);
            instrumentation.put("Harp", 0);

            // brass
            instrumentation.put("French Horn", 0);
            instrumentation.put("Trumpet", 0);
            instrumentation.put("Trombone", 0);
            instrumentation.put("Tuba", 0);

            Collection<EventDutyMusicalWorkEntity> eventDutyMusicalWorkEntities = eventDuty.getEventDutyMusicalWorksByEventDutyId();
            for(EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity : eventDutyMusicalWorkEntities) {

                InstrumentationEntity instrumentationEntity = eventDutyMusicalWorkEntity.getMusicalWorkByMusicalWork().getInstrumentationByInstrumentationId();

                HashMap<String, Integer> wood = getAmountWoodInstrumentation(instrumentationEntity.getWoodInstrumentationByWoodInstrumentation());
                if (instrumentation.get("Flute") < wood.get("Flute"))
                    instrumentation.put("Flute", wood.get("Flute"));
                if (instrumentation.get("Oboe") < wood.get("Oboe"))
                    instrumentation.put("Oboe", wood.get("Oboe"));
                if (instrumentation.get("Clarinet") < wood.get("Clarinet"))
                    instrumentation.put("Clarinet", wood.get("Clarinet"));
                if (instrumentation.get("Bassoon") < wood.get("Bassoon"))
                    instrumentation.put("Bassoon", wood.get("Bassoon"));

                HashMap<String, Integer> string = getAmountStringInstrumentation(instrumentationEntity.getStringInstrumentationByStringInstrumentation());
                if (instrumentation.get("First Violin") < string.get("First Violin"))
                    instrumentation.put("First Violin", string.get("First Violin"));
                if (instrumentation.get("Second Violin") < string.get("Second Violin"))
                    instrumentation.put("Second Violin", string.get("Second Violin"));
                if (instrumentation.get("Viola") < string.get("Viola"))
                    instrumentation.put("Viola", string.get("Viola"));
                if (instrumentation.get("Violoncello") < string.get("Violoncello"))
                    instrumentation.put("Violoncello", string.get("Violoncello"));
                if (instrumentation.get("Double Bass") < string.get("Double Bass"))
                    instrumentation.put("Double Bass", string.get("Double Bass"));

                HashMap<String, Integer> percussion = getAmountPercussionInstrumentation(instrumentationEntity.getPercussionInstrumentationByPercussionInstrumentation());
                if (instrumentation.get("Kettledrum") < percussion.get("Kettledrum"))
                    instrumentation.put("Kettledrum", percussion.get("Kettledrum"));
                if (instrumentation.get("Percussion") < percussion.get("Percussion"))
                    instrumentation.put("Percussion", percussion.get("Percussion"));
                if (instrumentation.get("Harp") < percussion.get("Harp"))
                    instrumentation.put("Harp", percussion.get("Harp"));

                HashMap<String, Integer> brass = getAmountBrassInstrumentation(instrumentationEntity.getBrassInstrumentationByBrassInstrumentation());
                if (instrumentation.get("French Horn") < brass.get("French Horn"))
                    instrumentation.put("French Horn", brass.get("French Horn"));
                if (instrumentation.get("Trumpet") < brass.get("Trumpet"))
                    instrumentation.put("Trumpet", brass.get("Trumpet"));
                if (instrumentation.get("Trombone") < brass.get("Trombone"))
                    instrumentation.put("Trombone", brass.get("Trombone"));
                if (instrumentation.get("Tuba") < brass.get("Tuba"))
                    instrumentation.put("Tuba", brass.get("Tuba"));
            }
        }
        return instrumentation;
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

    private static List<EventDutyEntity> getAllEventsDuring(Timestamp eventstart, Timestamp eventend) {

        // event start before and ends after
        // event starts inside
        // event ends inside
        // is event

        return eventDutyEntityPersistanceFacade.list(p ->
                (p.getStarttime().before(eventstart) && p.getEndtime().after(eventend))
                || (p.getStarttime().after(eventstart) && p.getStarttime().before(eventend))
                || (p.getEndtime().after(eventstart) && p.getEndtime().before(eventend))
                || (p.getEndtime().equals(eventend) && p.getStarttime().equals(eventstart))
        );
    }

    private static HashMap<String, Integer> getAmountWoodInstrumentation(WoodInstrumentationEntity woodInstrumentationEntity) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        instrumentationMap.put("Flute", woodInstrumentationEntity.getFlute());
        instrumentationMap.put("Oboe", woodInstrumentationEntity.getOboe());
        instrumentationMap.put("Clarinet", woodInstrumentationEntity.getClarinet());
        instrumentationMap.put("Bassoon", woodInstrumentationEntity.getBassoon());
        return instrumentationMap;
    }

    private static HashMap<String, Integer> getAmountStringInstrumentation(StringInstrumentationEntity stringInstrumentationEntity) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        instrumentationMap.put("First Violin", stringInstrumentationEntity.getViolin1());
        instrumentationMap.put("Second Violin", stringInstrumentationEntity.getViolin2());
        instrumentationMap.put("Viola", stringInstrumentationEntity.getViola());
        instrumentationMap.put("Violoncello", stringInstrumentationEntity.getViolincello());
        instrumentationMap.put("Double Bass", stringInstrumentationEntity.getDoublebass());
        return instrumentationMap;
    }

    private static HashMap<String, Integer> getAmountPercussionInstrumentation(PercussionInstrumentationEntity percussionInstrumentationEntity) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        instrumentationMap.put("Kettledrum", percussionInstrumentationEntity.getKettledrum());
        instrumentationMap.put("Percussion", percussionInstrumentationEntity.getPercussion());
        instrumentationMap.put("Harp", percussionInstrumentationEntity.getHarp());
        return instrumentationMap;
    }

    private static HashMap<String, Integer> getAmountBrassInstrumentation(BrassInstrumentationEntity brassInstrumentationEntity) {
        HashMap<String, Integer> instrumentationMap = new HashMap<>();
        instrumentationMap.put("French Horn", brassInstrumentationEntity.getHorn());
        instrumentationMap.put("Trumpet", brassInstrumentationEntity.getTrumpet());
        instrumentationMap.put("Trombone", brassInstrumentationEntity.getTrombone());
        instrumentationMap.put("Tuba", brassInstrumentationEntity.getTube());
        return instrumentationMap;
    }
}