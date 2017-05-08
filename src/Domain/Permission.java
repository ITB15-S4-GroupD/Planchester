package Domain;

import Application.DTO.EventDutyDTO;
import Utils.Enum.AccountRole;
import Utils.Enum.EventStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mir on 04.05.2017.
 */
public class Permission {

    private AccountRole accountRole;
    private boolean editEventSchedule = false;
    private boolean publishEventSchedule = false;
    private boolean editDutyRoster = false;
    private boolean publishDutyRoster = false;
    private boolean editMusicalWork = false;
    private boolean editInstrument = false;

    public Permission(AccountRole accountRole) {
        this.accountRole = accountRole;
        if(AccountRole.Administrator.equals(accountRole) || AccountRole.Manager.equals(accountRole)) {
            editEventSchedule = true;
            publishEventSchedule = true;
            editDutyRoster = true;
            publishDutyRoster = true;
            editMusicalWork = true;
            editInstrument = true;
        } else if(AccountRole.Musician.equals(accountRole) || AccountRole.Substitute.equals(accountRole)) {

        } else if(AccountRole.Music_librarian.equals(accountRole)) {
            editMusicalWork = true;
        } else if(AccountRole.Orchestral_facility_manager.equals(accountRole)) {
            editInstrument = true;
        } else if(AccountRole.Section_representative.equals(accountRole)) {
            editDutyRoster = true;
            publishDutyRoster = true;
        }
    }

    public List<EventDutyDTO> getViewableEventsForEventSchedule(List<EventDutyDTO> eventDutyDTOList) {
        List<EventDutyDTO> newList = new ArrayList<>();
        for(EventDutyDTO eventDutyDTO : eventDutyDTOList) {
            if(!eventDutyDTO.getEventStatus().equals(EventStatus.Published)) {
                if(accountRole.equals(AccountRole.Administrator) || accountRole.equals(AccountRole.Manager)) {
                    newList.add(eventDutyDTO);
                }
            } else {
                newList.add(eventDutyDTO);
            }
        }
        return newList;
    }

    public boolean isEditEventSchedule() {
        return editEventSchedule;
    }

    public boolean isPublishEventSchedule() {
        return publishEventSchedule;
    }

    public boolean isEditDutyRoster() {
        return editDutyRoster;
    }

    public boolean isPublishDutyRoster() {
        return publishDutyRoster;
    }

    public boolean isEditMusicalWork() {
        return editMusicalWork;
    }

    public boolean isEditInstrument() {
        return editInstrument;
    }
}