package Application;

import Persistence.Entities.EventDutySectionDutyRosterEntity;
import Persistence.Entities.SectionDutyRosterEntity;
import Persistence.PersistanceFacade;
import Utils.Enum.DutyRosterStatus;
import Utils.MessageHelper;

import java.time.Month;
import java.time.Year;
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
                && p.getSectionDutyRosterBySectionDutyRoster().getDutyRosterStatus().equals(DutyRosterStatus.Unpublished.toString())
        );

        if(!eventDutySectionDutyRosterEntities.isEmpty()) {
            for(EventDutySectionDutyRosterEntity eventDutySectionDutyRosterEntity : eventDutySectionDutyRosterEntities) {
                PersistanceFacade<SectionDutyRosterEntity> sectionDutyRosterEntityPersistanceFacade = new PersistanceFacade(SectionDutyRosterEntity.class);
                SectionDutyRosterEntity sectionDutyRosterEntity = eventDutySectionDutyRosterEntity.getSectionDutyRosterBySectionDutyRoster();
                sectionDutyRosterEntity.setDutyRosterStatus(DutyRosterStatus.Published.toString());
                sectionDutyRosterEntityPersistanceFacade.put(sectionDutyRosterEntity);
            }
            MessageHelper.showInformationMessage("All events of the month " + month.toString().toLowerCase() + " have been published");
        }
    }
}