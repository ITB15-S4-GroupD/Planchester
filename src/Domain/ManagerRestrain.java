package Domain;

import Application.DTO.EventDutyDTO;
import javafx.scene.control.Tab;

import java.util.Collection;
import java.util.List;

/**
 * Created by Mir on 06.05.2017.
 */
public final class ManagerRestrain implements PermissionRestrain {
    private static ManagerRestrain restr = null;

    static ManagerRestrain getInstance() {
            if(restr == null)restr = new ManagerRestrain();
            return restr;
    }

    private ManagerRestrain() {}

    @Override
    public Collection<Tab> constrainMainTabs(Collection<Tab> allTabs) {
        //no restrains for Manager
        return allTabs;
    }

    @Override
    public List<EventDutyDTO> constrainViewableEventsForEventSchedule(List<EventDutyDTO> unrestrictedList) {
        return unrestrictedList;
    }

    @Override
    public boolean isVisibleButtonPublishEvents() {
        return true;
    }

    @Override
    public boolean isVisibleMenuAddNewEvent() {
        return true;
    }

    @Override
    public boolean isVisibleEditEvent() {
        return true;
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
