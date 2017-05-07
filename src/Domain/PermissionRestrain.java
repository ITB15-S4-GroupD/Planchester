package Domain;

import Application.DTO.EventDutyDTO;
import Presentation.EventSchedule.EventScheduleController;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Collection;
import java.util.List;

/**
 * Created by Mir on 05.05.2017.
 */
public interface PermissionRestrain {

    public Collection<Tab> constrainMainTabs(Collection<Tab> allTabs);
    public void constrainDutyRoster();
    //Event Schedule
    public List<EventDutyDTO> constrainViewableEventsForEventSchedule(List<EventDutyDTO> unrestrictedList);
    public boolean isVisibleButtonPublishEvents();
    public boolean isVisibleMenuAddNewEvent();
    public boolean isVisibleEditEvent();
    public String FitTitleOnEventDetails(String text);
    public void constrainMusicalWorks();
    public void constrainInstruments();
    public void constrainUserAdministration();
    public void constrainSupport();
}
