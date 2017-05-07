package Domain;

import Application.DTO.EventDutyDTO;
import Utils.Enum.AccountRole;
import javafx.scene.control.Tab;

import java.util.Collection;
import java.util.List;

/**
 * Created by Mir on 04.05.2017.
 */
public class PermissionObject implements PermissionRestrain {
    private PermissionRestrain userRestrain = null;

    public PermissionObject(AccountRole role) {

System.out.println("PermissionObject builds Object for role: " + role.toString());
        switch (role.toString()) {

            case "Musician":
                userRestrain = MusicianRestrain.getInstance();
                break;
            case "Administrator":
                userRestrain = AdministratorRestrain.getInstance();
                break;
            case "Manager":
                userRestrain = ManagerRestrain.getInstance();
                break;
            case "Substitute":
                userRestrain = SubstituteRestrain.getInstance();
                break;
            case "Section_representative":
                userRestrain = SectionRepresentativeRestrain.getInstance();
                break;
            case "Orchestral_facility_manager":
                userRestrain = OrchestralFacilityManagerRestrain.getInstance();
                break;
            case "Music_librarian":
                userRestrain = MusicLibrarianRestrain.getInstance();
                break;

                default: break;
        }
    }


    @Override
    public Collection<Tab> constrainMainTabs(Collection<Tab> allTabs) {
System.out.println("PermissionObject calls on PermissionRestrain-Object of Class: " + userRestrain.getClass().getSimpleName());
        return userRestrain.constrainMainTabs(allTabs);
    }

    @Override
    public void constrainDutyRoster() {
        userRestrain.constrainDutyRoster();
    }

    @Override
    public List<EventDutyDTO> constrainViewableEventsForEventSchedule(List<EventDutyDTO> unrestrictedList) {
        return userRestrain.constrainViewableEventsForEventSchedule(unrestrictedList);
    }

    @Override
    public boolean isVisibleButtonPublishEvents() {
        return userRestrain.isVisibleButtonPublishEvents();
    }

    @Override
    public boolean isVisibleMenuAddNewEvent() {
        return userRestrain.isVisibleMenuAddNewEvent();
    }

    @Override
    public boolean isVisibleEditEvent() {
        return userRestrain.isVisibleEditEvent();
    }

    @Override
    public String FitTitleOnEventDetails(String text) {
        return userRestrain.FitTitleOnEventDetails(text);
    }

    @Override
    public void constrainMusicalWorks() {
        userRestrain.constrainMusicalWorks();
    }

    @Override
    public void constrainInstruments() {
        userRestrain.constrainInstruments();
    }

    @Override
    public void constrainUserAdministration() {
        userRestrain.constrainUserAdministration();
    }

    @Override
    public void constrainSupport() {
        userRestrain.constrainSupport();
    }
}




