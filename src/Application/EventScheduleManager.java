package Application;

import Application.DTO.EventDutyDTO;
import Application.DTO.InstrumentationDTO;
import Application.DTO.MusicalWorkDTO;
import Domain.Models.InstrumentationModel;
import Domain.Models.MusicalWorkModel;
import Persistence.Entities.EventDutyEntity;
import Domain.Models.EventDutyModel;
import Persistence.Entities.EventDutyMusicalWorkEntity;
import Persistence.Entities.InstrumentationEntity;
import Persistence.Entities.MusicalWorkEntity;
import Persistence.PersistanceFacade;
import Utils.DateHelper;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.MessageHelper;
import javax.xml.bind.ValidationException;
import java.util.*;
import static Utils.DateHelper.convertCalendarToTimestamp;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventScheduleManager {
    private static PersistanceFacade<EventDutyEntity> eventDutyEntityPersistanceFacade = new PersistanceFacade(EventDutyEntity.class);
    private static PersistanceFacade<EventDutyMusicalWorkEntity> eventDutyMusicalWorkEntityPersistanceFacade = new PersistanceFacade(EventDutyMusicalWorkEntity.class);

    public static EventDutyDTO createEventDuty(EventDutyDTO eventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(eventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);

        eventDutyEntity = eventDutyEntityPersistanceFacade.put(eventDutyEntity);

        // create connection between event duty and musical work
        if(eventDutyDTO.getMusicalWorks() != null) {
            for(MusicalWorkDTO musicalWorkDTO : eventDutyDTO.getMusicalWorks()) {
                if(musicalWorkDTO != null) {
                    createEventDutyMusicalWorks(eventDutyEntity, musicalWorkDTO);
                }
            }
        }

        HashMap<String, Integer> musicanCapacityMap = CalculateMusicianCapacity.checkCapacityInRange(eventDutyModel.getStartTime(), eventDutyModel.getEndTime());
        if(!musicanCapacityMap.isEmpty()) {
            MessageHelper.showWarningMusicianCapacityMessage(musicanCapacityMap, eventDutyDTO);
        }

        eventDutyDTO.setEventDutyId(eventDutyEntity.getEventDutyId());
        return eventDutyDTO;
    }

    public static void updateEventDuty(EventDutyDTO newEventDutyDTO, EventDutyDTO oldEventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(newEventDutyDTO);
        eventDutyModel.validate();

        // get object from db
        EventDutyEntity eventDutyEntity = eventDutyEntityPersistanceFacade.get(eventDutyModel.getEventDutyId());

        // update object
        eventDutyEntity.setName(eventDutyModel.getName());
        eventDutyEntity.setDescription(eventDutyModel.getDescription());
        eventDutyEntity.setStarttime(eventDutyModel.getStartTime());
        eventDutyEntity.setEndtime(eventDutyModel.getEndTime());
        eventDutyEntity.setEventType(eventDutyModel.getEventType().toString());
        eventDutyEntity.setEventStatus(eventDutyModel.getEventStatus().toString());
        eventDutyEntity.setConductor(eventDutyModel.getConductor());
        eventDutyEntity.setLocation(eventDutyModel.getLocation());
        eventDutyEntity.setDefaultPoints(eventDutyModel.getPoints() == null ? 0.0 : Double.valueOf(eventDutyModel.getPoints()));
        eventDutyEntity.setInstrumentation(eventDutyModel.getInstrumentationId());
        eventDutyEntity.setRehearsalFor(eventDutyModel.getRehearsalFor());

        // check for changes in musical works
        // remove all musical works which did exist but now don't
        if(oldEventDutyDTO.getMusicalWorks() != null) {
            for (MusicalWorkDTO musicalWorkDTO : oldEventDutyDTO.getMusicalWorks()) {
                if (newEventDutyDTO.getMusicalWorks() == null || !newEventDutyDTO.getMusicalWorks().contains(musicalWorkDTO)) {
                    removeEventDutyMusicalWorks(eventDutyEntity, musicalWorkDTO);
                }
            }
        }

        // add all new musical works which did not exist before
        if(newEventDutyDTO.getMusicalWorks() != null) {
            for (MusicalWorkDTO musicalWorkDTO : newEventDutyDTO.getMusicalWorks()) {
                if (oldEventDutyDTO.getMusicalWorks() == null || !oldEventDutyDTO.getMusicalWorks().contains(musicalWorkDTO)) {
                    createEventDutyMusicalWorks(eventDutyEntity,musicalWorkDTO);
                }
            }
        }

        // save updated object
        eventDutyEntityPersistanceFacade.put(eventDutyEntity);

        HashMap<String, Integer> musicanCapacityMap = CalculateMusicianCapacity.checkCapacityInRange(eventDutyModel.getStartTime(), eventDutyModel.getEndTime());
        if(!musicanCapacityMap.isEmpty()) {
            MessageHelper.showWarningMusicianCapacityMessage(musicanCapacityMap, newEventDutyDTO);
        }
    }

    public static List<EventDutyDTO> getEventDutyListForCurrentWeek() {
        Calendar today = Calendar.getInstance();
        return getEventDutyInRange(DateHelper.getStartOfWeek(today), DateHelper.getEndOfWeek(today));
    }

    public static List<EventDutyDTO> getEventDutyListForWeek(Calendar cal) {
        return getEventDutyInRange(DateHelper.getStartOfWeek(cal), DateHelper.getEndOfWeek(cal));
    }

    public static List<EventDutyDTO> getAllRehearsalsOfEventDuty(EventDutyDTO eventDutyDTO) {
        if(eventDutyDTO.getEventDutyId() != null) {
            List<EventDutyEntity> eventDuties = eventDutyEntityPersistanceFacade.list(p -> p.getRehearsalFor() == eventDutyDTO.getEventDutyId());

            List<EventDutyModel> eventDutyModelList = new ArrayList<>();
            for (EventDutyEntity eventDutyEntity : eventDuties) {
                eventDutyModelList.add(createEventDutyModel(eventDutyEntity));
            }
            List<EventDutyDTO> eventDutyDTOList = new ArrayList<>();
            for (EventDutyModel eventDutyModel : eventDutyModelList) {
                eventDutyDTOList.add(createEventDutyDTO(eventDutyModel));
            }

            return AccountAdministrationManager.getInstance().getUserPermissions().getViewableEventsForEventSchedule(eventDutyDTOList);
        }
        return new ArrayList<>();
    }

    private static List<EventDutyDTO> getEventDutyInRange(Calendar startdayOfWeek, Calendar enddayOfWeek) {
        startdayOfWeek.setTimeInMillis(startdayOfWeek.getTimeInMillis() - 1);

        List<EventDutyEntity> eventDuties = eventDutyEntityPersistanceFacade.list(p -> p.getStarttime().after(convertCalendarToTimestamp(startdayOfWeek))
                && p.getStarttime().before(convertCalendarToTimestamp(enddayOfWeek)));

        List<EventDutyModel> eventDutyModelList = new ArrayList<>();
        for (EventDutyEntity eventDutyEntity : eventDuties) {
            eventDutyModelList.add(createEventDutyModel(eventDutyEntity));
        }

        List<EventDutyDTO> eventDutyDTOList = new ArrayList<>();
        for (EventDutyModel eventDutyModel : eventDutyModelList) {
            eventDutyDTOList.add(createEventDutyDTO(eventDutyModel));
        }

        return AccountAdministrationManager.getInstance().getUserPermissions().getViewableEventsForEventSchedule(eventDutyDTOList);
    }

    public static void removeEventDutyMusicalWorks(EventDutyEntity eventDutyEntity, MusicalWorkDTO musicalWorkDTO) {
        EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity = eventDutyMusicalWorkEntityPersistanceFacade.get(
                p -> p.getEventDuty() == eventDutyEntity.getEventDutyId() && p.getMusicalWork() == musicalWorkDTO.getId());
        eventDutyMusicalWorkEntityPersistanceFacade.remove(eventDutyMusicalWorkEntity);
    }

    public static void createEventDutyMusicalWorks(EventDutyEntity eventDutyEntity, MusicalWorkDTO musicalWorkDTO) {
        EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity = new EventDutyMusicalWorkEntity();
        eventDutyMusicalWorkEntity.setEventDuty(eventDutyEntity.getEventDutyId());
        eventDutyMusicalWorkEntity.setMusicalWork(musicalWorkDTO.getId());
        eventDutyMusicalWorkEntityPersistanceFacade.put(eventDutyMusicalWorkEntity);
    }

    public static MusicalWorkModel getMusicalWorkModel(MusicalWorkDTO musicalWorkDTO) {
        MusicalWorkModel musicalWorkModel = new MusicalWorkModel();
        musicalWorkModel.setName(musicalWorkDTO.getName());
        musicalWorkModel.setComposer(musicalWorkDTO.getComposer());
        musicalWorkModel.setId(musicalWorkDTO.getId());
        musicalWorkModel.setInstrumentationId(musicalWorkDTO.getInstrumentationId());
        return musicalWorkModel;
    }

    public static MusicalWorkModel getMusicalWorkModel(MusicalWorkEntity musicalWorkEntity) {
        MusicalWorkModel musicalWorkModel = new MusicalWorkModel();
        musicalWorkModel.setName(musicalWorkEntity.getName());
        musicalWorkModel.setComposer(musicalWorkEntity.getComposer());
        musicalWorkModel.setId(musicalWorkEntity.getMusicalWorkId());
        musicalWorkModel.setInstrumentationId(musicalWorkEntity.getInstrumentationId());
        return musicalWorkModel;
    }

    public static MusicalWorkDTO getMusicalWorkDTO(MusicalWorkModel musicalWorkModel) {
        MusicalWorkDTO musicalWorkDTO = new MusicalWorkDTO();
        musicalWorkDTO.setName(musicalWorkModel.getName());
        musicalWorkDTO.setComposer(musicalWorkModel.getComposer());
        musicalWorkDTO.setId(musicalWorkModel.getId());
        musicalWorkDTO.setInstrumentationId(musicalWorkModel.getInstrumentationId());
        return musicalWorkDTO;
    }

    public static MusicalWorkEntity getMusicalWorkEntity(MusicalWorkModel musicalWorkModel) {
        MusicalWorkEntity musicalWorkEntity = new MusicalWorkEntity();
        musicalWorkEntity.setName(musicalWorkModel.getName());
        musicalWorkEntity.setComposer(musicalWorkModel.getComposer());
        musicalWorkEntity.setMusicalWorkId(musicalWorkModel.getId());
        musicalWorkEntity.setInstrumentationId(musicalWorkModel.getInstrumentationId());
        return musicalWorkEntity;
    }

    public static InstrumentationModel getInstrumentationModel(InstrumentationDTO instrumentation) {
        InstrumentationModel instrumentationModel = new InstrumentationModel();
        instrumentationModel.setBasson(instrumentation.getBasson());
        instrumentationModel.setClarinet(instrumentation.getClarinet());
        instrumentationModel.setDescription(instrumentation.getDescription());
        instrumentationModel.setDoublebass(instrumentation.getDoublebass());
        instrumentationModel.setFirstViolin(instrumentation.getFirstViolin());
        instrumentationModel.setFlute(instrumentation.getFlute());
        instrumentationModel.setHarp(instrumentation.getHarp());
        instrumentationModel.setHorn(instrumentation.getHorn());
        instrumentationModel.setKettledrum(instrumentation.getKettledrum());
        instrumentationModel.setOboe(instrumentation.getOboe());
        instrumentationModel.setPercussion(instrumentation.getPercussion());
        instrumentationModel.setSecondViolin(instrumentation.getSecondViolin());
        instrumentationModel.setTrombone(instrumentation.getTrombone());
        instrumentationModel.setTrumpet(instrumentation.getTrumpet());
        instrumentationModel.setTube(instrumentation.getTube());
        instrumentationModel.setViola(instrumentation.getViola());
        instrumentationModel.setVioloncello(instrumentation.getVioloncello());
        return instrumentationModel;
    }

    public static InstrumentationModel getInstrumentationModel(InstrumentationEntity instrumentationEntity){
        if(instrumentationEntity == null ) {
            return null;
        }
        InstrumentationModel instrumentationModel = new InstrumentationModel();
        instrumentationModel.setBasson(instrumentationEntity.getWoodInstrumentationByWoodInstrumentation().getBassoon());
        instrumentationModel.setClarinet(instrumentationEntity.getWoodInstrumentationByWoodInstrumentation().getClarinet());
        instrumentationModel.setDescription(instrumentationEntity.getPercussionInstrumentationByPercussionInstrumentation().getPercussionDescription());
        instrumentationModel.setDoublebass(instrumentationEntity.getStringInstrumentationByStringInstrumentation().getDoublebass());
        instrumentationModel.setFirstViolin(instrumentationEntity.getStringInstrumentationByStringInstrumentation().getViolin1());
        instrumentationModel.setFlute(instrumentationEntity.getWoodInstrumentationByWoodInstrumentation().getFlute());
        instrumentationModel.setHarp(instrumentationEntity.getPercussionInstrumentationByPercussionInstrumentation().getHarp());
        instrumentationModel.setHorn(instrumentationEntity.getBrassInstrumentationByBrassInstrumentation().getHorn());
        instrumentationModel.setKettledrum(instrumentationEntity.getPercussionInstrumentationByPercussionInstrumentation().getKettledrum());
        instrumentationModel.setOboe(instrumentationEntity.getWoodInstrumentationByWoodInstrumentation().getOboe());
        instrumentationModel.setPercussion(instrumentationEntity.getPercussionInstrumentationByPercussionInstrumentation().getPercussion());
        instrumentationModel.setSecondViolin(instrumentationEntity.getStringInstrumentationByStringInstrumentation().getViolin2());
        instrumentationModel.setTrombone(instrumentationEntity.getBrassInstrumentationByBrassInstrumentation().getTrombone());
        instrumentationModel.setTrumpet(instrumentationEntity.getBrassInstrumentationByBrassInstrumentation().getTrumpet());
        instrumentationModel.setTube(instrumentationEntity.getBrassInstrumentationByBrassInstrumentation().getTube());
        instrumentationModel.setViola(instrumentationEntity.getStringInstrumentationByStringInstrumentation().getViola());
        instrumentationModel.setVioloncello(instrumentationEntity.getStringInstrumentationByStringInstrumentation().getViolincello());
        return instrumentationModel;
    }

    public static InstrumentationDTO getInstrumentationDTO(InstrumentationModel instrumentationModel) {
        InstrumentationDTO instrumentationDTO = new InstrumentationDTO();
        instrumentationDTO.setBasson(instrumentationModel.getBasson());
        instrumentationDTO.setClarinet(instrumentationModel.getClarinet());
        instrumentationDTO.setDescription(instrumentationModel.getDescription());
        instrumentationDTO.setDoublebass(instrumentationModel.getDoublebass());
        instrumentationDTO.setFirstViolin(instrumentationModel.getFirstViolin());
        instrumentationDTO.setFlute(instrumentationModel.getFlute());
        instrumentationDTO.setHarp(instrumentationModel.getHarp());
        instrumentationDTO.setHorn(instrumentationModel.getHorn());
        instrumentationDTO.setKettledrum(instrumentationModel.getKettledrum());
        instrumentationDTO.setOboe(instrumentationModel.getOboe());
        instrumentationDTO.setPercussion(instrumentationModel.getPercussion());
        instrumentationDTO.setSecondViolin(instrumentationModel.getSecondViolin());
        instrumentationDTO.setTrombone(instrumentationModel.getTrombone());
        instrumentationDTO.setTrumpet(instrumentationModel.getTrumpet());
        instrumentationDTO.setTube(instrumentationModel.getTube());
        instrumentationDTO.setViola(instrumentationModel.getViola());
        instrumentationDTO.setVioloncello(instrumentationModel.getVioloncello());
        return instrumentationDTO;
    }

    public static EventDutyModel createEventDutyModel(EventDutyDTO eventDutyDTO) {
        EventDutyModel eventDutyModel = new EventDutyModel();
        eventDutyModel.setEventDutyId(eventDutyDTO.getEventDutyId());
        eventDutyModel.setName(eventDutyDTO.getName());
        eventDutyModel.setDescription(eventDutyDTO.getDescription());
        eventDutyModel.setStartTime(eventDutyDTO.getStartTime());
        eventDutyModel.setEndTime(eventDutyDTO.getEndTime());
        eventDutyModel.setEventType(eventDutyDTO.getEventType());
        eventDutyModel.setEventStatus(eventDutyDTO.getEventStatus());
        eventDutyModel.setConductor(eventDutyDTO.getConductor());
        eventDutyModel.setLocation(eventDutyDTO.getLocation());
        eventDutyModel.setPoints(eventDutyDTO.getPoints());
        eventDutyModel.setInstrumentation(eventDutyDTO.getInstrumentation());
        eventDutyModel.setRehearsalFor(eventDutyDTO.getRehearsalFor());
        eventDutyModel.setDutyRosterStatus(eventDutyDTO.getDutyRosterStatus());
        if(eventDutyDTO.getMusicalWorks() != null && !eventDutyDTO.getMusicalWorks().isEmpty()) {
            List<MusicalWorkModel> musicalWorkModels = new ArrayList<>();
            for (MusicalWorkDTO musicalWorkDTO : eventDutyDTO.getMusicalWorks()) {
                if(musicalWorkDTO != null) {
                    musicalWorkModels.add(getMusicalWorkModel(musicalWorkDTO));
                }
            }
            eventDutyModel.setMusicalWorks(musicalWorkModels);
        }
        return eventDutyModel;
    }

    public static EventDutyEntity createEventDutyEntity(EventDutyModel eventDutyModel) {
        EventDutyEntity eventDutyEntity = new EventDutyEntity();
        if(eventDutyModel.getEventDutyId() != null) {
            eventDutyEntity.setEventDutyId(eventDutyModel.getEventDutyId());
        }
        eventDutyEntity.setName(eventDutyModel.getName());
        eventDutyEntity.setDescription(eventDutyModel.getDescription());
        eventDutyEntity.setStarttime(eventDutyModel.getStartTime());
        eventDutyEntity.setEndtime(eventDutyModel.getEndTime());
        eventDutyEntity.setEventType(eventDutyModel.getEventType().toString());
        eventDutyEntity.setEventStatus(eventDutyModel.getEventStatus().toString());
        eventDutyEntity.setConductor(eventDutyModel.getConductor());
        eventDutyEntity.setLocation(eventDutyModel.getLocation());
        eventDutyEntity.setDefaultPoints(eventDutyModel.getPoints() == null ? 0.0 : Double.valueOf(eventDutyModel.getPoints()));
        eventDutyEntity.setInstrumentation(eventDutyModel.getInstrumentationId());
        eventDutyEntity.setRehearsalFor(eventDutyModel.getRehearsalFor());

        if(eventDutyModel.getMusicalWorks() != null) {
            Collection<EventDutyMusicalWorkEntity> collection;
            if(eventDutyEntity.getEventDutyMusicalWorksByEventDutyId() != null) {
                collection = eventDutyEntity.getEventDutyMusicalWorksByEventDutyId();
            } else {
                collection = new HashSet<>();
            }
            for (MusicalWorkModel musicalWorkModel : eventDutyModel.getMusicalWorks()) {
                if(musicalWorkModel != null) {
                    EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity = new EventDutyMusicalWorkEntity();
                    eventDutyMusicalWorkEntity.setMusicalWork(musicalWorkModel.getId());
                    if(eventDutyModel.getEventDutyId() != null){
                        eventDutyMusicalWorkEntity.setEventDuty(eventDutyModel.getEventDutyId());
                    }
                    collection.add(eventDutyMusicalWorkEntity);
                }
            }
            eventDutyEntity.setEventDutyMusicalWorksByEventDutyId(collection);
        }

        return eventDutyEntity;
    }

    public static EventDutyDTO createEventDutyDTO (EventDutyModel eventDutyModel) {
        EventDutyDTO eventDutyDTO = new EventDutyDTO();
        eventDutyDTO.setEventDutyId(eventDutyModel.getEventDutyId());
        eventDutyDTO.setName(eventDutyModel.getName());
        eventDutyDTO.setDescription(eventDutyModel.getDescription());
        eventDutyDTO.setStartTime(eventDutyModel.getStartTime());
        eventDutyDTO.setEndTime(eventDutyModel.getEndTime());
        eventDutyDTO.setEventType(EventType.valueOf(eventDutyModel.getEventType().toString()));
        eventDutyDTO.setEventStatus(EventStatus.valueOf(eventDutyModel.getEventStatus().toString()));
        eventDutyDTO.setConductor(eventDutyModel.getConductor());
        eventDutyDTO.setLocation(eventDutyModel.getLocation());
        eventDutyDTO.setPoints(eventDutyModel.getPoints());
        eventDutyDTO.setInstrumentation(eventDutyModel.getInstrumentation());
        eventDutyDTO.setRehearsalFor(eventDutyModel.getRehearsalFor());
        eventDutyDTO.setDutyRosterStatus(eventDutyModel.getDutyRosterStatus());

        if(eventDutyModel.getMusicalWorks() != null) {
            List<MusicalWorkDTO> musicalWorkDTOS = new ArrayList<>();
            for (MusicalWorkModel musicalWorkModel : eventDutyModel.getMusicalWorks()) {
                musicalWorkDTOS.add(getMusicalWorkDTO(musicalWorkModel));
            }
            eventDutyDTO.setMusicalWorks(musicalWorkDTOS);
        }
        return eventDutyDTO;
    }

    public static EventDutyModel createEventDutyModel(EventDutyEntity eventDutyEntity) {
        EventDutyModel eventDutyModel = new EventDutyModel();
        eventDutyModel.setEventDutyId(eventDutyEntity.getEventDutyId());
        eventDutyModel.setName(eventDutyEntity.getName());
        eventDutyModel.setDescription(eventDutyEntity.getDescription());
        eventDutyModel.setStartTime(eventDutyEntity.getStarttime());
        eventDutyModel.setEndTime(eventDutyEntity.getEndtime());
        eventDutyModel.setEventType(EventType.valueOf(eventDutyEntity.getEventType()));
        eventDutyModel.setEventStatus(EventStatus.valueOf(eventDutyEntity.getEventStatus()));
        eventDutyModel.setConductor(eventDutyEntity.getConductor());
        eventDutyModel.setLocation(eventDutyEntity.getLocation());
        eventDutyModel.setPoints(eventDutyEntity.getDefaultPoints());
        eventDutyModel.setInstrumentation(getInstrumentationModel(eventDutyEntity.getInstrumentationByInstrumentation()));
        eventDutyModel.setRehearsalFor(eventDutyEntity.getRehearsalFor());

        if(eventDutyEntity.getEventDutyMusicalWorksByEventDutyId() != null && !eventDutyEntity.getEventDutyMusicalWorksByEventDutyId().isEmpty()) {
            List<MusicalWorkModel> musicalWorkModels = new ArrayList<>();
            for (EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity : eventDutyEntity.getEventDutyMusicalWorksByEventDutyId()) {
                MusicalWorkEntity musicalWorkEntity = eventDutyMusicalWorkEntity.getMusicalWorkByMusicalWork();
                musicalWorkModels.add(getMusicalWorkModel(musicalWorkEntity));
            }
            eventDutyModel.setMusicalWorks(musicalWorkModels);
        }

        return eventDutyModel;
    }

    public static List<EventDutyDTO> getAllUnpublishedMonths() {
        List<EventDutyEntity> eventDuties = eventDutyEntityPersistanceFacade.list(p -> p.getEventStatus().equals(EventStatus.Unpublished.toString()));

        List<EventDutyModel> eventDutyModelList = new ArrayList<>();
        for(EventDutyEntity eventDutyEntity : eventDuties) {
            eventDutyModelList.add(createEventDutyModel(eventDutyEntity));
        }
        List<EventDutyDTO> eventDutyDTOList = new ArrayList<>();
        for(EventDutyModel eventDutyModel : eventDutyModelList) {
            eventDutyDTOList.add(createEventDutyDTO(eventDutyModel));
        }
        return eventDutyDTOList;
    }
}