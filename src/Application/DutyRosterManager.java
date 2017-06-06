package Application;

import Application.DTO.EventDutyDTO;
import Domain.Models.EventDutyModel;
import Persistence.Entities.*;
import Persistence.PersistanceFacade;
import Utils.DateHelper;
import Utils.Enum.*;
import Utils.MessageHelper;

import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static Application.EventScheduleManager.createEventDutyDTO;
import static Application.EventScheduleManager.createEventDutyModel;

/**
 * Created by timorzipa on 06/04/2017.
 */
public class DutyRosterManager {

    private Calendar firstOfMonth = Calendar.getInstance();
    private Calendar firstOfNextMonth = Calendar.getInstance();
    private SectionType section;
    private static PersistanceFacade<EventDutyEntity> eventDutyEntityPersistanceFacade = new PersistanceFacade(EventDutyEntity.class);
    private static PersistanceFacade<EventDutySectionDutyRosterEntity> eventDutySectionDutyRosterEntityPersistanceFacade = new PersistanceFacade(EventDutySectionDutyRosterEntity.class);

    public static List<EventDutyDTO> getDutyRosterListForCurrentWeek() {
        Calendar today = Calendar.getInstance();
        return getDutyRosterInRange(DateHelper.getStartOfWeek(today), DateHelper.getEndOfWeek(today));
    }

    public static List<EventDutyDTO> getDutyRosterListForWeek(Calendar cal) {
        return getDutyRosterInRange(DateHelper.getStartOfWeek(cal), DateHelper.getEndOfWeek(cal));
    }

    private static List<EventDutyDTO> getDutyRosterInRange(Calendar startdayOfWeek, Calendar enddayOfWeek) {

        startdayOfWeek.setTimeInMillis(startdayOfWeek.getTimeInMillis()-1);

        AccountRole accountRole = AccountAdministrationManager.getInstance().getAccountRole();

        if(AccountAdministrationManager.getInstance().getSectionType() == null) {
            return null;
        }

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

        return eventDutyDTOList;
    }

    public static List<EventDutyEntity> getDutyRosterEntitiesInRange(Calendar startdayOfWeek, Calendar enddayOfWeek) {

        startdayOfWeek.setTimeInMillis(startdayOfWeek.getTimeInMillis()-1);

        AccountRole accountRole = AccountAdministrationManager.getInstance().getAccountRole();

        if(AccountAdministrationManager.getInstance().getSectionType() == null) {
            return null;
            //return new ArrayList<>();
        }

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

    public static List<EventDutyDTO> getAllUnpublishedMonths() {
        AccountRole accountRole = AccountAdministrationManager.getInstance().getAccountRole();

        if(AccountAdministrationManager.getInstance().getSectionType() == null) {
            return null;
        }

        // get all events for user section in specified time space and for his account role
        List<EventDutySectionDutyRosterEntity> eventDutySectionDutyRosterEntities = eventDutySectionDutyRosterEntityPersistanceFacade.list(p ->
                (
                        p.getSectionDutyRosterBySectionDutyRoster().getSectionType().equals(AccountAdministrationManager.getInstance().getSectionType().toString()))
                        && AccountRole.Section_representative.equals(accountRole)
                        && DutyRosterStatus.Unpublished.toString().equals(p.getSectionDutyRosterBySectionDutyRoster().getDutyRosterStatus())
        );

        List<EventDutyEntity> eventDuties = new ArrayList<>();
        for(EventDutySectionDutyRosterEntity eventDutySectionDutyRosterEntity : eventDutySectionDutyRosterEntities) {
            eventDuties.add(eventDutySectionDutyRosterEntity.getEventDutyByEventDuty());
        }
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

    public EventDutyDTO validateMonth(Year year, Month month) {
        //zweimal selbe parameter übergeben, da aufruf und logik bei setter
        setFirstOfMonth(year, month);
        setFirstOfNextMonth(year, month);

        section = AccountAdministrationManager.getInstance().getSectionType();
        Collection<MusicianPartEntity> parts = AccountAdministrationManager.getInstance().getLoggedInAccount().getPersonAccountId().getMusicianPartsByPersonId();

        for (EventDutyEntity event : DutyRosterManager.getDutyRosterEntitiesInRange(firstOfMonth, firstOfNextMonth)) {
            if(validateDuty(event) == false) {
                EventDutyModel eventDutyModel = EventScheduleManager.createEventDutyModel(event);
                EventDutyDTO eventDutyDTO = EventScheduleManager.createEventDutyDTO(eventDutyModel);
                return eventDutyDTO;
            }
        }

        return null;
    }

    public boolean validateDuty(EventDutyEntity event) {
        int required = 0;
        if(event.getInstrumentationByInstrumentation() == null) {
            StringBuilder warning = new StringBuilder();
            warning.append("Instrumentation for ");
            warning.append(event.getEventType());
            warning.append(" ");
            warning.append(event.getName());
            warning.append(" on ");
            warning.append(event.getStarttime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            warning.append(" is missing.");
            MessageHelper.showErrorAlertMessage(warning.toString());
            return false;
        }

        StringInstrumentationEntity stringInstrumentationEntity = event.getInstrumentationByInstrumentation().getStringInstrumentationByStringInstrumentation();
        switch (section) {
            case Violin1:
                required = stringInstrumentationEntity.getViolin1();
                break;
            case Violin2:
                required = stringInstrumentationEntity.getViolin2();
                break;
            case Viola:
                required = stringInstrumentationEntity.getViola();
                break;
            case Violoncello:
                required = stringInstrumentationEntity.getVioloncello();
                break;
            case Doublebass:
                required = stringInstrumentationEntity.getDoublebass();
                break;

            case Woodwind:
                WoodInstrumentationEntity woodInstrumentationEntity = event.getInstrumentationByInstrumentation().getWoodInstrumentationByWoodInstrumentation();
                required = woodInstrumentationEntity.getBassoon() + woodInstrumentationEntity.getClarinet() + woodInstrumentationEntity.getFlute() + woodInstrumentationEntity.getOboe();
                break;

            case Brass:
                BrassInstrumentationEntity brassInstrumentationEntity = event.getInstrumentationByInstrumentation().getBrassInstrumentationByBrassInstrumentation();
                required = brassInstrumentationEntity.getHorn() + brassInstrumentationEntity.getTrombone() + brassInstrumentationEntity.getTrumpet() + brassInstrumentationEntity.getTube();
                break;

            case Percussion:
                PercussionInstrumentationEntity percussionInstrumentationEntity = event.getInstrumentationByInstrumentation().getPercussionInstrumentationByPercussionInstrumentation();
                required = percussionInstrumentationEntity.getHarp() + percussionInstrumentationEntity.getKettledrum() + percussionInstrumentationEntity.getPercussion();
                break;
        }

        int adressed = getCountMusicicansForEventAndSection(event, section, DutyDispositionStatus.Normal);
        int spare = getCountMusicicansForEventAndSection(event, section, DutyDispositionStatus.Spare);
        boolean sectionLeader = getSectionLeaderForEventAndSection(event, section);

        StringBuilder warning = new StringBuilder();

        if(required > adressed && spare < 1) {
            int missing = required - adressed;
            warning.append("There are not enough musicians assigned for ");
            warning.append(event.getEventType());
            warning.append(" ");
            warning.append(event.getName());
            warning.append(" on ");
            warning.append(event.getStarttime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            warning.append(".\n");
            if(missing == 1) {
                warning.append("Two musicians, including the spare one, are missing.");
            } else {
                missing++;
                warning.append(missing);
                warning.append(" musicians, including the spare one, are missing.");
            }
        } else if(spare < 1) {
            warning.append("There is no spare musician assigned for ");
            warning.append(event.getEventType());
            warning.append(" ");
            warning.append(event.getName());
            warning.append(" on ");
            warning.append(event.getStarttime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            warning.append(".\n");
        } else if(required > adressed) {
            int missing = required - adressed;
            warning.append("There are not enough musicians assigned for ");
            warning.append(event.getEventType());
            warning.append(" ");
            warning.append(event.getName());
            warning.append(" on ");
            warning.append(event.getStarttime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            warning.append(".\n");
            if(missing == 1) {
                warning.append("One musicians is missing.");
            } else {
                warning.append(missing);
                warning.append(" musicians are missing.");
            }
        }

        if(required > adressed || spare < 1) {
            MessageHelper.showErrorAlertMessage(warning.toString());
            return false;
        }

        if(getSectionLeaderForEventAndSection(event, section) == false) {
            warning = new StringBuilder();
            warning.append("There is no section leader assigned for ");
            warning.append(event.getEventType());
            warning.append(" ");
            warning.append(event.getName());
            warning.append(" on ");
            warning.append(event.getStarttime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            warning.append(".\n");
            MessageHelper.showErrorAlertMessage(warning.toString());
            return false;
        }
        return true;
    }

    public boolean getSectionLeaderForEventAndSection(EventDutyEntity eventDutyEntity, SectionType sectionType) {
        PersistanceFacade<DutyDispositionEntity> dutyDispositionEntityPersistanceFacade = new PersistanceFacade<>(DutyDispositionEntity.class);

        List<DutyDispositionEntity> dutyDispositionEntities = dutyDispositionEntityPersistanceFacade.list(p -> p.getEventDuty() == eventDutyEntity.getEventDutyId()
                && p.getPersonByMusician().getMusicianPartsByPersonId().stream().anyMatch(c -> c.getPartByPart().getSectionType().equals(sectionType.toString()))
                && p.getPersonByMusician().getPersonOrchestraRolesByPersonId().stream().anyMatch(y -> y.getOrchestraRole().equals(OrchestraRole.Section_leader.toString()))
        );

        if(dutyDispositionEntities.size() > 0) {
            return true;
        }
        return false;
    }

    public int getCountMusicicansForEventAndSection(EventDutyEntity eventDutyEntity, SectionType sectionType, DutyDispositionStatus dutyDispositionStatus) {
        PersistanceFacade<DutyDispositionEntity> dutyDispositionEntityPersistanceFacade = new PersistanceFacade<>(DutyDispositionEntity.class);

        List<DutyDispositionEntity> dutyDispositionEntities = dutyDispositionEntityPersistanceFacade.list(p -> p.getEventDuty() == eventDutyEntity.getEventDutyId()
                && p.getPersonByMusician().getMusicianPartsByPersonId().stream().anyMatch(c -> c.getPartByPart().getSectionType().equals(sectionType.toString()))
                && p.getDutyDispositionStatus().equals(dutyDispositionStatus.toString())
        );

        return dutyDispositionEntities.size();
    }

    public static List<String> getAdressedMusicicansForEventAndPart(EventDutyDTO eventDutyDTO, String partType, DutyDispositionStatus dutyDispositionStatus) {
        PersistanceFacade<DutyDispositionEntity> dutyDispositionEntityPersistanceFacade = new PersistanceFacade<>(DutyDispositionEntity.class);
        PersistanceFacade<PartTypeEntity> partTypeEntityPersistanceFacade = new PersistanceFacade<>(PartTypeEntity.class);

        PartTypeEntity partTypeEntity = partTypeEntityPersistanceFacade.get(p -> p.getPartType().equals(partType));

        if(partTypeEntity == null) {
            return null;
        }

        List<DutyDispositionEntity> dutyDispositionEntities;
        dutyDispositionEntities = dutyDispositionEntityPersistanceFacade.list(p ->
                p.getEventDuty() == eventDutyDTO.getEventDutyId()
                        && p.getPersonByMusician().getMusicianPartsByPersonId().stream().anyMatch(c -> c.getPart() == partTypeEntity.getPartTypeId())
                        && p.getDutyDispositionStatus().equals(dutyDispositionStatus.toString())
        );

        List<String> musicians = new ArrayList<>();
        for(DutyDispositionEntity dutyDispositionEntity : dutyDispositionEntities) {
            musicians.add(dutyDispositionEntity.getPersonByMusician().getFirstname() + " " + dutyDispositionEntity.getPersonByMusician().getLastname());
        }
        return musicians;
    }

    public static String getSpareMusicicansForEventAndSectionType(EventDutyDTO eventDutyDTO, SectionType sectionType) {
        PersistanceFacade<DutyDispositionEntity> dutyDispositionEntityPersistanceFacade = new PersistanceFacade<>(DutyDispositionEntity.class);

        DutyDispositionEntity dutyDispositionEntity = dutyDispositionEntityPersistanceFacade.get(p ->
                p.getEventDuty() == eventDutyDTO.getEventDutyId()
                        && p.getPersonByMusician().getMusicianPartsByPersonId().stream().anyMatch(c -> c.getPartByPart().getSectionType().equals(sectionType.toString()))
                        && p.getDutyDispositionStatus().equals(DutyDispositionStatus.Spare.toString())
        );

        if (dutyDispositionEntity == null) {
            return null;
        } else {
            return dutyDispositionEntity.getPersonByMusician().getFirstname() + " " + dutyDispositionEntity.getPersonByMusician().getLastname();
        }
    }

    /**
     * Getters and Setters
     */

    public void setFirstOfMonth(Year year, Month month) {
        this.firstOfMonth.set(year.getValue(), month.getValue() - 1, 1);
    }

    public void setFirstOfNextMonth(Year year, Month month) {
        this.firstOfNextMonth.set(year.getValue(), month.getValue(), 1);
    }
}
