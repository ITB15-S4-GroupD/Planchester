package Application;

import Application.DTO.EventDutyDTO;
import Domain.Models.EventDutyModel;
import Persistence.Entities.EventDutyEntity;
import Persistence.Entities.EventDutySectionDutyRosterEntity;
import Persistence.Entities.SectionDutyRosterEntity;
import Persistence.PersistanceFacade;
import Presentation.EventSchedule.EventScheduleController;
import Utils.Enum.DutyRosterStatus;
import Utils.Enum.EventStatus;
import Utils.MessageHelper;
import Utils.Validator;

import javax.xml.bind.ValidationException;
import java.time.Month;
import java.time.Year;
import java.util.Collection;
import java.util.List;

/**
 * Created by timo on 11.05.2017
 */
public class PublishDutyRoster {

    private static PersistanceFacade<EventDutySectionDutyRosterEntity> eventDutySectionDutyRosterEntityPersistanceFacade = new PersistanceFacade(EventDutySectionDutyRosterEntity.class);

    public static void publish(Year year, Month month) {
        List<EventDutySectionDutyRosterEntity> eventDutySectionDutyRosterEntities = eventDutySectionDutyRosterEntityPersistanceFacade.list(p ->
                p.getEventDutyByEventDuty().getStarttime().toLocalDateTime().getMonth().equals(month)
                && p.getEventDutyByEventDuty().getStarttime().toLocalDateTime().getYear() == year.getValue()
                && p.getSectionDutyRosterBySectionDutyRoster().getSectionType().equals(AccountAdministrationManager.getInstance().getSectionType().toString())
                && p.getSectionDutyRosterBySectionDutyRoster().getDutyRosterStatus().equals(DutyRosterStatus.Unpublished)
        );

        if(!eventDutySectionDutyRosterEntities.isEmpty()) {
            SectionDutyRosterEntity sectionDutyRosterEntity = eventDutySectionDutyRosterEntities.get(0).getSectionDutyRosterBySectionDutyRoster();
            PersistanceFacade<SectionDutyRosterEntity> sectionDutyRosterEntityPersistanceFacade = new PersistanceFacade(SectionDutyRosterEntity.class);
            sectionDutyRosterEntityPersistanceFacade.put(sectionDutyRosterEntity);
        }
    }
}