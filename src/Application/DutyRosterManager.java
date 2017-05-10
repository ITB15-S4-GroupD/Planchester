package Application;

import Application.DTO.EventDutyDTO;
import Application.DTO.MusicalWorkDTO;
import Domain.Models.EventDutyModel;
import Persistence.Entities.DutyDispositionEntity;
import Persistence.Entities.EventDutyEntity;
import Persistence.Entities.EventDutySectionDutyRosterEntity;
import Persistence.PersistanceFacade;
import Utils.DateHelper;
import Utils.Enum.AccountRole;
import Utils.Enum.DutyRosterStatus;
import Utils.MessageHelper;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static Application.EventScheduleManager.createEventDutyDTO;
import static Application.EventScheduleManager.createEventDutyModel;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class DutyRosterManager {

    private static PersistanceFacade<EventDutyEntity> eventDutyEntityPersistanceFacade = new PersistanceFacade(EventDutyEntity.class);
    private static PersistanceFacade<EventDutySectionDutyRosterEntity> eventDutySectionDutyRosterEntityPersistanceFacade = new PersistanceFacade(EventDutySectionDutyRosterEntity.class);

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

        startdayOfWeek.setTimeInMillis(startdayOfWeek.getTimeInMillis()-1);

        AccountRole accountRole = AccountAdministrationManager.getInstance().getAccountRole();

        // get all events for user section in specified time space and for his account role
        List<EventDutySectionDutyRosterEntity> eventDutySectionDutyRosterEntities = eventDutySectionDutyRosterEntityPersistanceFacade.list(p ->
                (p.getEventDutyByEventDuty().getStarttime().after(DateHelper.convertCalendarToTimestamp(startdayOfWeek))
                && p.getEventDutyByEventDuty().getStarttime().before(DateHelper.convertCalendarToTimestamp(enddayOfWeek)))
                && (p.getSectionDutyRosterBySectionDutyRoster().getSectionType().equals(AccountAdministrationManager.getInstance().getSectionType().toString()))
                && (accountRole.equals(AccountRole.Section_representative)
                        || p.getSectionDutyRosterBySectionDutyRoster().getDutyRosterStatus().equals(DutyRosterStatus.Published.toString()))
        );

        List<EventDutyModel> eventDutyModelList = new ArrayList<>();
        for(EventDutySectionDutyRosterEntity eventDutySectionDutyRosterEntity : eventDutySectionDutyRosterEntities) {
            EventDutyEntity eventDutyEntity = eventDutySectionDutyRosterEntity.getEventDutyByEventDuty();

            EventDutyModel eventDutyModel = createEventDutyModel(eventDutyEntity);
            eventDutyModel.setDutyRosterStatus(DutyRosterStatus.valueOf(eventDutySectionDutyRosterEntity.getSectionDutyRosterBySectionDutyRoster().getDutyRosterStatus()));
            eventDutyModelList.add(eventDutyModel);
        }

        List<EventDutyDTO> eventDutyDTOList = new ArrayList<>();
        for(EventDutyModel eventDutyModel : eventDutyModelList) {
            eventDutyDTOList.add(createEventDutyDTO(eventDutyModel));
        }
        setLoadedEventsStartAndEnddate(startdayOfWeek, enddayOfWeek);

        return eventDutyDTOList;
    }

    public static List<EventDutyEntity> getDutyRosterEntitiesInRange(Calendar startdayOfWeek, Calendar enddayOfWeek) {
        if(loadedEventsStartdate != null && loadedEventsEnddate != null &&
                loadedEventsStartdate.compareTo(startdayOfWeek) <= 0 && loadedEventsEnddate.compareTo(enddayOfWeek) >= 0) {
            return new ArrayList<>();
        }

        startdayOfWeek.setTimeInMillis(startdayOfWeek.getTimeInMillis()-1);

        AccountRole accountRole = AccountAdministrationManager.getInstance().getAccountRole();

        // get all events for user section in specified time space and for his account role
        List<EventDutySectionDutyRosterEntity> eventDutySectionDutyRosterEntities = eventDutySectionDutyRosterEntityPersistanceFacade.list(p ->
                (p.getEventDutyByEventDuty().getStarttime().after(DateHelper.convertCalendarToTimestamp(startdayOfWeek))
                        && p.getEventDutyByEventDuty().getStarttime().before(DateHelper.convertCalendarToTimestamp(enddayOfWeek)))
                        && (p.getSectionDutyRosterBySectionDutyRoster().getSectionType().equals(AccountAdministrationManager.getInstance().getSectionType().toString()))
                        && (accountRole.equals(AccountRole.Section_representative)
                        || p.getSectionDutyRosterBySectionDutyRoster().getDutyRosterStatus().equals(DutyRosterStatus.Published.toString()))
        );

        List<EventDutyEntity> eventDutyEntities = new ArrayList<>();
        for(EventDutySectionDutyRosterEntity eventDutySectionDutyRosterEntity : eventDutySectionDutyRosterEntities) {
            eventDutyEntities.add(eventDutySectionDutyRosterEntity.getEventDutyByEventDuty());
        }

        return eventDutyEntities;
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
}