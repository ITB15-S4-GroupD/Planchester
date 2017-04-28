package Application;

import Application.DTO.EventDutyDTO;
import Persistence.Entities.EventDutyEntity;
import Domain.EventDutyModel;
import Persistence.EventDutyRDBMapper;
import Persistence.PersistanceFacade;
import Utils.DateHelper;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class EventScheduleManager {
    private static PersistanceFacade persistanceFacade = new PersistanceFacade();
    private static Calendar loadedEventsStartdate; //start of the already loaded calendar
    private static Calendar loadedEventsEnddate; //end of the already loaded calendar

    public static void createConcertPerformance(EventDutyDTO eventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(eventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);
        persistanceFacade.put(eventDutyEntity);
    }

    public static void createOperaPerformance(EventDutyDTO eventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(eventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);
        persistanceFacade.put(eventDutyEntity);
    }

    public static void createTourPerformance(EventDutyDTO eventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(eventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);
        persistanceFacade.put(eventDutyEntity);

    }

    public static void createHofkapellePerformance(EventDutyDTO eventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(eventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);
        persistanceFacade.put(eventDutyEntity);
    }

    public static void updateConcertPerformance(EventDutyDTO eventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(eventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);
        persistanceFacade.put(eventDutyEntity);
    }

    public static void updateOperaPerformance(EventDutyDTO eventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(eventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);
        persistanceFacade.put(eventDutyEntity);
    }

    public static void updateTourPerformance(EventDutyDTO eventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(eventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);
        persistanceFacade.put(eventDutyEntity);
    }

    public static void updateHofkapellePerformance(EventDutyDTO eventDutyDTO) throws ValidationException {
        EventDutyModel eventDutyModel = createEventDutyModel(eventDutyDTO);
        eventDutyModel.validate();
        EventDutyEntity eventDutyEntity = createEventDutyEntity(eventDutyModel);
        persistanceFacade.put(eventDutyEntity);
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
        return eventDutyModel;
    }


}