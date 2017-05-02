package Application;

import java.time.Month;
import java.time.Year;
import java.util.*;
import Application.DTO.EventDutyDTO;
import Application.DTO.MusicalWorkDTO;
import Domain.EventDutyModel;
import Domain.MusicalWorkModel;
import Persistence.Entities.*;
import Persistence.EventDutyRDBMapper;
import Persistence.Entities.EventDutyEntity;
import Presentation.EventSchedule.EventScheduleController;
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.MessageHelper;
import Utils.Validator;
import javax.xml.bind.ValidationException;

/**
 * Created by julia on 28.04.2017.
 */
public class PublishEventSchedule {

    public static EventDutyDTO publish(Year year, Month month) {
        //Calculate first and last day of month; note that Month starts with 0
        EventDutyModel eventDutyModel;

        Calendar firstOfMonth = Calendar.getInstance();
        firstOfMonth.set(year.getValue(), month.getValue()-1, 1);

        Calendar firstNextMoneth = Calendar.getInstance();
        firstNextMoneth.set(year.getValue(), month.getValue(), 1);

        List<EventDutyEntity> dutiesInRange = EventDutyRDBMapper.getEventDutyInRange(firstOfMonth, firstNextMoneth);

        for(EventDutyEntity evt : dutiesInRange){
            eventDutyModel = EventScheduleManager.createEventDutyModel(evt);
            try{ eventDutyModel.validate(); }catch (ValidationException val){
                MessageHelper.showErrorAlertMessage("Please complete duty " + evt.getEventType() + ", " + evt.getStarttime() +
                                                    "\n" + val.getMessage() );
                return EventScheduleManager.createEventDutyDTO(eventDutyModel);
            }

            if(!hardValid(evt))EventScheduleManager.createEventDutyDTO(eventDutyModel);

            eventDutyModel.setEventStatus(EventStatus.Published.toString());
            try {
                EventScheduleManager.updateEventDuty(EventScheduleManager.createEventDutyDTO(eventDutyModel),EventScheduleManager.createEventDutyDTO(eventDutyModel));
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }

        MessageHelper.showInformationMessage("All events of the month " + month.toString().toLowerCase() + " have been published");
        return null;
    }

    private static boolean hardValid(EventDutyEntity duty){
        String info = "Please complete duty " + duty.getEventType() + ", " + duty.getStarttime() + "\n" ;

        try{ Validator.validatePublishLocation(duty.getLocation()); }
        catch (ValidationException val){
            MessageHelper.showErrorAlertMessage(info + val.getMessage() );
            return false;
        }

        try{ Validator.validatePublishPoints(duty.getDefaultPoints()); }
        catch (ValidationException val){
            MessageHelper.showErrorAlertMessage(info + val.getMessage() );
            return false;
        }

        if(duty.getEventType().equals("Opera")){
            try{ Validator.validatePublishConductor(duty.getConductor()); }
            catch (ValidationException val){
                MessageHelper.showErrorAlertMessage( info + val.getMessage() );
                return false;
            }
        }
        return true;
    }
}