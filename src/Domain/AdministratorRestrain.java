package Domain;

import Application.DTO.EventDutyDTO;
import javafx.scene.control.Tab;

import java.util.Collection;
import java.util.List;

/**
 * Created by Mir on 06.05.2017.
 */
public final class AdministratorRestrain implements PermissionRestrain {
    private static AdministratorRestrain restr = null;

    static AdministratorRestrain getInstance() {
        if(restr == null)restr = new AdministratorRestrain();
        return restr;
    }

    private AdministratorRestrain() {}

    @Override
    public Collection<Tab> constrainMainTabs(Collection<Tab> allTabs) {
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
    public String FitTitleOnEventDetails(String text) {
        return "Edit " + text;
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
