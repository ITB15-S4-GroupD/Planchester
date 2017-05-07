package Domain;

import Application.DTO.EventDutyDTO;
import Presentation.EventSchedule.EventScheduleController;
import Utils.Enum.EventStatus;
import javafx.scene.control.Tab;

import java.util.*;

/**
 * Created by Mir on 05.05.2017.
 */
public final class MusicianRestrain implements PermissionRestrain {
    private static MusicianRestrain restr = null;

    static MusicianRestrain getInstance() {
        if(restr == null)restr = new MusicianRestrain();
        return restr;
    }

    private MusicianRestrain() {
    }

    @Override
    public Collection<Tab> constrainMainTabs(Collection<Tab> allTabs) {
        ArrayList<Tab> restrained = new ArrayList<>();

        for( Tab t : allTabs){
            if( t.getId().equals("TbDutyRoster") || t.getId().equals("TbEventSchedule")
                    || t.getId().equals("TbSupport")){
                restrained.add(t);
            }
        }
        return restrained;
    }

    @Override
    public List<EventDutyDTO> constrainViewableEventsForEventSchedule(List<EventDutyDTO> unrestrictedList) {
        List<EventDutyDTO> restricted = new ArrayList<>();

        for(EventDutyDTO evt : unrestrictedList){
            if(evt.getEventStatus().equals(EventStatus.Published)){
                restricted.add(evt);
            }
        }
        return restricted;
    }

    @Override
    public boolean isVisibleButtonPublishEvents() {
        return false;
    }

    @Override
    public boolean isVisibleMenuAddNewEvent() {
        return false;
    }

    @Override
    public boolean isVisibleEditEvent() {
        return false;
    }

    @Override
    public String FitTitleOnEventDetails(String text) {
        return "Details " + text;
    }

    @Override
    public void constrainDutyRoster() {
    }

    @Override
    public void constrainMusicalWorks() {
    }

    @Override
    public void constrainInstruments() {
    }

    @Override
    public void constrainUserAdministration() {
    }

    @Override
    public void constrainSupport() {
    }
}
