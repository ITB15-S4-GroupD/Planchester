package Application;

import Application.DTO.EventDutyDTO;
import Domain.Models.EventDutyModel;
import Persistence.Entities.*;
import Persistence.PersistanceFacade;
import Utils.Enum.DutyDispositionStatus;
import Utils.Enum.OrchestraRole;
import Utils.Enum.SectionType;
import Utils.MessageHelper;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Bernd u. Garvin on 5/8/2017.
 *
 * @Project Planchester
 */
public class DutyRoster {

    private Calendar firstOfMonth = Calendar.getInstance();
    private Calendar firstOfNextMonth = Calendar.getInstance();
    private SectionType section;

    /**
     *
     * @param year
     * @param month
     * @return  null : successful
     *          EventDutyDTO : event which failed validation
     */
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