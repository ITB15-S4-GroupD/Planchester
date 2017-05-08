package Application;

import Application.DTO.EventDutyDTO;
import Application.DTO.MusicalWorkDTO;
import Domain.EventDutyModel;
import Persistence.DutyDispositionRDBMapper;
import Persistence.Entities.DutyDispositionEntity;
import Persistence.Entities.EventDutyEntity;
import Persistence.EventDutyRDBMapper;
import Persistence.PersistanceFacade;
import Utils.DateHelper;
import Utils.MessageHelper;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class DutyRosterManager {

    private static PersistanceFacade persistanceFacade = new PersistanceFacade();

    private static Calendar loadedEventsStartdate; //start of the already loaded calendar
    private static Calendar loadedEventsEnddate; //end of the already loaded calendar

    public static List<EventDutyDTO> getDutyRosterListForCurrentWeek() {
        Calendar today = Calendar.getInstance();
        return getDutyRosterInRange(DateHelper.getStartOfWeek(today), DateHelper.getEndOfWeek(today));
    }

    public static List<EventDutyDTO> getDutyRosterListForWeek(Calendar cal) {
        return getDutyRosterInRange(DateHelper.getStartOfWeek(cal), DateHelper.getEndOfWeek(cal));
    }

    private static List<EventDutyDTO> getDutyRosterInRange(Calendar startdayOfWeek, Calendar enddayOfWeek) {
        if(loadedEventsStartdate != null && loadedEventsEnddate != null &&
                loadedEventsStartdate.compareTo(startdayOfWeek) <= 0 && loadedEventsEnddate.compareTo(enddayOfWeek) >= 0) {
            return new ArrayList<>();
        }

        DutyDispositionRDBMapper rdbMapper = (DutyDispositionRDBMapper) persistanceFacade.getMapper(DutyDispositionEntity.class);
        List<EventDutyEntity> eventDuties = rdbMapper.getDutyRosterInRange(startdayOfWeek, enddayOfWeek);

        List<EventDutyModel> eventDutyModelList = new ArrayList<>();
        for(EventDutyEntity eventDutyEntity : eventDuties) {
            eventDutyModelList.add(createEventDutyModel(eventDutyEntity));
        }
        List<EventDutyDTO> eventDutyDTOList = new ArrayList<>();
        for(EventDutyModel eventDutyModel : eventDutyModelList) {
            eventDutyDTOList.add(createEventDutyDTO(eventDutyModel));
        }
        setLoadedEventsStartAndEnddate(startdayOfWeek, enddayOfWeek);
        return eventDutyDTOList;
    }

}