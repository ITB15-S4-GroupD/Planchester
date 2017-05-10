package Application;

import Domain.Models.EventDutyModel;
import Persistence.Entities.*;
import Persistence.PersistanceFacade;
import Utils.Enum.DutyDispositionStatus;
import Utils.Enum.SectionType;
import Utils.MessageHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static Application.EventScheduleManager.createEventDutyModel;

/**
 * Created by Bernd u. Garvin on 5/8/2017.
 *
 * @Project Planchester
 */
public class DutyRoster {

    private Calendar firstOfMonth = Calendar.getInstance();
    private Calendar firstOfNextMonth = Calendar.getInstance();
    private SectionType section;
    private PartTypeEntity partTypeEntity;

    public boolean validateMonth(Year year, Month month) {
        //zweimal selbe parameter übergeben, da aufruf und logik bei setter
        setFirstOfMonth(year, month);
        setFirstOfNextMonth(year, month);

        section = AccountAdministrationManager.getInstance().getSectionType();
        Collection<MusicianPartEntity> parts = AccountAdministrationManager.getInstance().getLoggedInAccount().getPersonAccountId().getMusicianPartsByPersonId();

        for(MusicianPartEntity musicianPartEntity : parts) {
            partTypeEntity = musicianPartEntity.getPartByPart().getPartTypeByPartType();
        }

        for (EventDutyEntity event : DutyRosterManager.getDutyRosterEntitiesInRange(firstOfMonth, firstOfNextMonth)) {
            if(validateDuty(event) == false) {
                return false;
            }
        }

        return true;
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
            case Violincello:
                required = stringInstrumentationEntity.getViolincello();
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

        int adressed = getCountMusicicansForEventAndPart(event, partTypeEntity, DutyDispositionStatus.Normal);

        if(required > adressed) {
            int missing = required - adressed;
            StringBuilder warning = new StringBuilder();
            warning.append("There are not enough musicians assigned for ");
            warning.append(event.getEventType());
            warning.append(" ");
            warning.append(event.getName());
            warning.append(" on ");
            warning.append(event.getStarttime().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            warning.append(".\n");
            warning.append(missing);
            warning.append(" ");
            warning.append(partTypeEntity.getPartType());
            warning.append(" are missing.");
            MessageHelper.showErrorAlertMessage(warning.toString());
            return false;
        }
        return true;
    }

    public int getCountMusicicansForEventAndPart(EventDutyEntity eventDutyEntity, PartTypeEntity partTypeEntity, DutyDispositionStatus dutyDispositionStatus) {
        PersistanceFacade<DutyDispositionEntity> dutyDispositionEntityPersistanceFacade = new PersistanceFacade<>(DutyDispositionEntity.class);

        return dutyDispositionEntityPersistanceFacade.list(p -> p.getEventDuty() == eventDutyEntity.getEventDutyId()
                && p.getPersonByMusician().getMusicianPartsByPersonId().stream().filter(c -> c.getPartByPart().equals(partTypeEntity)) != null
                && p.getDutyDispositionStatus().equals(dutyDispositionStatus.toString())
        ).size();
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
