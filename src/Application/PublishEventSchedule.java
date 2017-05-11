package Application;

import java.sql.Timestamp;
import java.time.Month;
import java.time.Year;
import java.util.*;
import Application.DTO.EventDutyDTO;
import Domain.Models.EventDutyModel;
import Persistence.Entities.EventDutyEntity;
import Persistence.PersistanceFacade;
import Presentation.EventSchedule.EventScheduleController;
import Utils.Enum.EventStatus;
import Utils.MessageHelper;
import Utils.Validator;
import javax.xml.bind.ValidationException;

import static Utils.DateHelper.convertCalendarToTimestamp;

/**
 * Created by julia on 28.04.2017.
 */
public class PublishEventSchedule {

    private static PersistanceFacade<EventDutyEntity> eventDutyEntityPersistanceFacade = new PersistanceFacade(EventDutyEntity.class);

    public static EventDutyDTO publish(Year year, Month month) {
        //Calculate first and last day of month; note that Month starts with 0
        EventDutyModel eventDutyModel;

        List<EventDutyEntity> dutiesInRange = eventDutyEntityPersistanceFacade.list(
                p -> p.getStarttime().toLocalDateTime().getMonth().equals(month)
                && p.getEventStatus().equals(EventStatus.Unpublished.toString()));

        for(EventDutyEntity evt : dutiesInRange){
            eventDutyModel = EventScheduleManager.createEventDutyModel(evt);
            try{
                eventDutyModel.validate();
            } catch (ValidationException val){
                MessageHelper.showErrorAlertMessage("Please complete event " + evt.getEventType() + ", " + evt.getStarttime() +
                                                    "\n" + val.getMessage() );
                return EventScheduleManager.createEventDutyDTO(eventDutyModel);
            }

            if(!hardValid(evt)) {
                return EventScheduleManager.createEventDutyDTO(eventDutyModel);
            }
        }
      
        for(EventDutyEntity eventDutyEntity: dutiesInRange) {
            eventDutyEntity.setEventStatus(EventStatus.Published.toString());
            eventDutyEntityPersistanceFacade.put(eventDutyEntity);
        }

        EventScheduleController.reload();
        MessageHelper.showInformationMessage("All events of the month " + month.toString().toLowerCase() + " have been published");
        return null;
    }

    private static boolean hardValid(EventDutyEntity duty){
        String info = "Please complete event " + duty.getEventType() + ", " + duty.getStarttime() + "\n" ;

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