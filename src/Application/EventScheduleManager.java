package Application;

import Application.DTO.EventDutyDTO;
import Application.DTO.InstrumentationDTO;
import Application.DTO.MusicalWorkDTO;
import Domain.InstrumentationModel;
import Domain.MusicalWorkModel;
import Persistence.Entities.EventDutyEntity;
import Domain.EventDutyModel;
import Persistence.Entities.EventDutyMusicalWorkEntity;
import Persistence.Entities.InstrumentationEntity;
import Persistence.Entities.MusicalWorkEntity;
import Persistence.EventDutyMusicalWorkRDBMapper;
import Persistence.EventDutyRDBMapper;
import Persistence.PersistanceFacade;
import Utils.DateHelper;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventScheduleManager {
    private static PersistanceFacade persistanceFacade = new PersistanceFacade();
    private static Calendar loadedEventsStartdate; //start of the already loaded calendar
    private static Calendar loadedEventsEnddate; //end of the already loaded calendar

    public static void createEventDuty(EventDutyDTO eventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(eventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);
        eventDutyEntity = (EventDutyEntity) persistanceFacade.put(eventDutyEntity);

        // create connection between event duty and musical work
        if(eventDutyDTO.getMusicalWorks() != null) {
            for(MusicalWorkDTO musicalWorkDTO : eventDutyDTO.getMusicalWorks()) {
                if(musicalWorkDTO != null) {
                    EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity = new EventDutyMusicalWorkEntity();
                    eventDutyMusicalWorkEntity.setEventDuty(eventDutyEntity.getEventDutyId());
                    eventDutyMusicalWorkEntity.setMusicalWork(musicalWorkDTO.getId());
                    persistanceFacade.put(eventDutyMusicalWorkEntity);
                }
            }
        }
    }

    public static void updateEventDuty(EventDutyDTO newEventDutyDTO, EventDutyDTO oldEventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(newEventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);
        persistanceFacade.put(eventDutyEntity);

        // check for changes in musical works
            // remove all musical works which did exist but now dont
            if(oldEventDutyDTO.getMusicalWorks() != null) {
                for (MusicalWorkDTO musicalWorkDTO : oldEventDutyDTO.getMusicalWorks()) {
                    if (newEventDutyDTO.getMusicalWorks() == null || !newEventDutyDTO.getMusicalWorks().contains(musicalWorkDTO)) {
                        EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity = new EventDutyMusicalWorkEntity();
                        eventDutyMusicalWorkEntity.setEventDuty(eventDutyEntity.getEventDutyId());
                        eventDutyMusicalWorkEntity.setMusicalWork(musicalWorkDTO.getId());
                        EventDutyMusicalWorkRDBMapper.remove(eventDutyMusicalWorkEntity);
                    }
                }
            }
            // add all new musical works which did not exist before
            if(newEventDutyDTO.getMusicalWorks() != null) {
                for (MusicalWorkDTO musicalWorkDTO : newEventDutyDTO.getMusicalWorks()) {
                    if ( musicalWorkDTO != null && (oldEventDutyDTO.getMusicalWorks() == null || !oldEventDutyDTO.getMusicalWorks().contains(musicalWorkDTO)) ) {
                        EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity = new EventDutyMusicalWorkEntity();
                        eventDutyMusicalWorkEntity.setEventDuty(eventDutyEntity.getEventDutyId());
                        eventDutyMusicalWorkEntity.setMusicalWork(musicalWorkDTO.getId());
                        persistanceFacade.put(eventDutyMusicalWorkEntity);
                    }
                }
            }
    }

	public static void createNonMusicalPerformance(EventDutyDTO eventDutyDTO) {
		
	}

    public static List<EventDutyDTO> getEventDutyListForCurrentWeek() {
        Calendar today = Calendar.getInstance();
        return getEventDutyInRange(DateHelper.getStartOfWeek(today), DateHelper.getEndOfWeek(today));
    }

    public static List<EventDutyDTO> getEventDutyListForWeek(Calendar cal) {
        return getEventDutyInRange(DateHelper.getStartOfWeek(cal), DateHelper.getEndOfWeek(cal));
    }

    private static List<EventDutyDTO> getEventDutyInRange(Calendar startdayOfWeek, Calendar enddayOfWeek) {
        if(loadedEventsStartdate != null && loadedEventsEnddate != null &&
                loadedEventsStartdate.compareTo(startdayOfWeek) <= 0 && loadedEventsEnddate.compareTo(enddayOfWeek) >= 0) {
            return new ArrayList<EventDutyDTO>();
        }
        List<EventDutyEntity> eventDuties = EventDutyRDBMapper.getEventDutyInRange(startdayOfWeek, enddayOfWeek);

        List<EventDutyModel> eventDutyModelList = new ArrayList<EventDutyModel>();
        for(EventDutyEntity eventDutyEntity : eventDuties) {
            eventDutyModelList.add(createEventDutyModel(eventDutyEntity));
        }
        List<EventDutyDTO> eventDutyDTOList = new ArrayList<EventDutyDTO>();
        for(EventDutyModel eventDutyModel : eventDutyModelList) {
            eventDutyDTOList.add(createEventDutyDTO(eventDutyModel));
        }
        setLoadedEventsStartAndEnddate(startdayOfWeek, enddayOfWeek);
        return eventDutyDTOList;
    }

    private static void setLoadedEventsStartAndEnddate(Calendar start, Calendar end) {
        if(loadedEventsStartdate == null) {
            loadedEventsStartdate = start;
        } else if(loadedEventsStartdate.after(start)) {
            loadedEventsStartdate = start;
        }

        if(loadedEventsEnddate == null) {
            loadedEventsEnddate = end;
        } else if (loadedEventsEnddate.before((end))) {
            loadedEventsEnddate = end;
        }
    }

    private static MusicalWorkModel getMusicalWorkModel(MusicalWorkDTO musicalWorkDTO) {
        MusicalWorkModel musicalWorkModel = new MusicalWorkModel();
        musicalWorkModel.setName(musicalWorkDTO.getName());
        musicalWorkModel.setComposer(musicalWorkDTO.getComposer());
        musicalWorkModel.setId(musicalWorkDTO.getId());
        musicalWorkModel.setInstrumentationId(musicalWorkDTO.getInstrumentationId());
        return musicalWorkModel;
    }

    private static MusicalWorkModel getMusicalWorkModel(MusicalWorkEntity musicalWorkEntity) {
        MusicalWorkModel musicalWorkModel = new MusicalWorkModel();
        musicalWorkModel.setName(musicalWorkEntity.getName());
        musicalWorkModel.setComposer(musicalWorkEntity.getComposer());
        musicalWorkModel.setId(musicalWorkEntity.getMusicalWorkId());
        musicalWorkModel.setInstrumentationId(musicalWorkEntity.getInstrumentationId());
        return musicalWorkModel;
    }

    private static MusicalWorkDTO getMusicalWorkDTO(MusicalWorkModel musicalWorkModel) {
        MusicalWorkDTO musicalWorkDTO = new MusicalWorkDTO();
        musicalWorkDTO.setName(musicalWorkModel.getName());
        musicalWorkDTO.setComposer(musicalWorkModel.getComposer());
        musicalWorkDTO.setId(musicalWorkModel.getId());
        musicalWorkDTO.setInstrumentationId(musicalWorkModel.getInstrumentationId());
        return musicalWorkDTO;
    }

    private static MusicalWorkEntity getMusicalWorkEntity(MusicalWorkModel musicalWorkModel) {
        MusicalWorkEntity musicalWorkEntity = new MusicalWorkEntity();
        musicalWorkEntity.setName(musicalWorkModel.getName());
        musicalWorkEntity.setComposer(musicalWorkModel.getComposer());
        musicalWorkEntity.setMusicalWorkId(musicalWorkModel.getId());
        musicalWorkEntity.setInstrumentationId(musicalWorkModel.getInstrumentationId());
        return musicalWorkEntity;
    }

    private static InstrumentationModel getInstrumentationModel(InstrumentationDTO instrumentation) {
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

    private static EventDutyModel createEventDutyModel(EventDutyDTO eventDutyDTO) {
        EventDutyModel eventDutyModel = new EventDutyModel();
        eventDutyModel.setEventDutyId(eventDutyDTO.getEventDutyID());
        eventDutyModel.setName(eventDutyDTO.getName());
        eventDutyModel.setDescription(eventDutyDTO.getDescription());
        eventDutyModel.setStarttime(eventDutyDTO.getStartTime());
        eventDutyModel.setEndtime(eventDutyDTO.getEndTime());
        eventDutyModel.setEventType(eventDutyDTO.getEventType().toString());
        eventDutyModel.setEventStatus(eventDutyDTO.getEventStatus().toString());
        eventDutyModel.setConductor(eventDutyDTO.getConductor());
        eventDutyModel.setLocation(eventDutyDTO.getEventLocation());
        eventDutyModel.setDefaultPoints(eventDutyDTO.getPoints());
        eventDutyModel.setInstrumentation(eventDutyDTO.getInstrumentation());
        eventDutyModel.setRehearsalFor(eventDutyDTO.getRehearsalFor());
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

    private static EventDutyEntity createEventDutyEntity(EventDutyModel eventDutyModel) {
        EventDutyEntity eventDutyEntity = new EventDutyEntity();
        if(eventDutyModel.getEventDutyId() != null) {
            eventDutyEntity.setEventDutyId(eventDutyModel.getEventDutyId());
        }
        eventDutyEntity.setName(eventDutyModel.getName());
        eventDutyEntity.setDescription(eventDutyModel.getDescription());
        eventDutyEntity.setStarttime(eventDutyModel.getStarttime());
        eventDutyEntity.setEndtime(eventDutyModel.getEndtime());
        eventDutyEntity.setEventType(eventDutyModel.getEventType());
        eventDutyEntity.setEventStatus(eventDutyModel.getEventStatus());
        eventDutyEntity.setConductor(eventDutyModel.getConductor());
        eventDutyEntity.setLocation(eventDutyModel.getLocation());
        eventDutyEntity.setDefaultPoints(eventDutyModel.getDefaultPoints() == null ? 0.0 : Double.valueOf(eventDutyModel.getDefaultPoints()));
        eventDutyEntity.setInstrumentation(eventDutyModel.getInstrumentation());
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


    private static EventDutyDTO createEventDutyDTO (EventDutyModel eventDutyModel) {
        EventDutyDTO eventDutyDTO = new EventDutyDTO();
        eventDutyDTO.setEventDutyID(eventDutyModel.getEventDutyId());
        eventDutyDTO.setName(eventDutyModel.getName());
        eventDutyDTO.setDescription(eventDutyModel.getDescription());
        eventDutyDTO.setStartTime(eventDutyModel.getStarttime());
        eventDutyDTO.setEndTime(eventDutyModel.getEndtime());
        eventDutyDTO.setEventType(EventType.valueOf(eventDutyModel.getEventType()));
        eventDutyDTO.setEventStatus(EventStatus.valueOf(eventDutyModel.getEventStatus()));
        eventDutyDTO.setConductor(eventDutyModel.getConductor());
        eventDutyDTO.setEventLocation(eventDutyModel.getLocation());
        eventDutyDTO.setPoints(eventDutyModel.getDefaultPoints());
        eventDutyDTO.setInstrumentation(eventDutyModel.getInstrumentation());
        eventDutyDTO.setRehearsalFor(eventDutyModel.getRehearsalFor());

        if(eventDutyModel.getMusicalWorks() != null) {
            List<MusicalWorkDTO> musicalWorkDTOS = new ArrayList<>();
            for (MusicalWorkModel musicalWorkModel : eventDutyModel.getMusicalWorks()) {
                musicalWorkDTOS.add(getMusicalWorkDTO(musicalWorkModel));
            }
            eventDutyDTO.setMusicalWorks(musicalWorkDTOS);
        }
        return eventDutyDTO;
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

        if(!eventDutyEntity.getEventDutyMusicalWorksByEventDutyId().isEmpty()) {
            List<MusicalWorkModel> musicalWorkModels = new ArrayList<>();
            for (EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity : eventDutyEntity.getEventDutyMusicalWorksByEventDutyId()) {
                MusicalWorkEntity musicalWorkEntity = eventDutyMusicalWorkEntity.getMusicalWorkByMusicalWork();
                musicalWorkModels.add(getMusicalWorkModel(musicalWorkEntity));
            }
            eventDutyModel.setMusicalWorks(musicalWorkModels);
        }

        return eventDutyModel;
    }
}