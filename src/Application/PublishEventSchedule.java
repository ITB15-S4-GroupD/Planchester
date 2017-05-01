package Application;

import java.time.LocalDateTime;
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
import Utils.Enum.EventStatus;
import Utils.Enum.EventType;
import Utils.MessageHelper;
import Utils.Validator;
import javax.xml.bind.ValidationException;

/**
 * Created by julia on 28.04.2017.
 */
public class PublishEventSchedule {

    public static void publish(Year year, Month month) {
        //Calculate first and last day of month; note that Month starts with 0
        EventDutyModel eventDutyModel;

        Calendar firstOfMonth = Calendar.getInstance();
        firstOfMonth.set(year.getValue(), month.getValue()-1, 1);

        Calendar firstNextMoneth = Calendar.getInstance();
        firstNextMoneth.set(year.getValue(), month.getValue(), 1);

        List<EventDutyEntity> dutiesInRange = EventDutyRDBMapper.getEventDutyInRange(firstOfMonth, firstNextMoneth);

        for(EventDutyEntity evt : dutiesInRange){
            eventDutyModel = createEventDutyModel(evt);
            try{ eventDutyModel.validate(); }catch (ValidationException val){
                MessageHelper.showErrorAlertMessage("Please complete duty " + evt.getEventType() + ", " + evt.getStarttime() +
                                                    "\n" + val.getMessage() );
                return;
            }

            if(!hardValid(evt))return;

            eventDutyModel.setEventStatus(EventStatus.Unpublished.toString());
            try {
                EventScheduleManager.updateEventDuty(createEventDutyDTO(eventDutyModel),createEventDutyDTO(eventDutyModel));
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }

        MessageHelper.showInformationMessage("All events of the month " + month.toString().toLowerCase() + " have been published");
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

    //If the methode createEventDutyModel in EventScheduleManager was protected duplicating code here would not be necessary.
    private static EventDutyModel createEventDutyModel(EventDutyEntity ede){
        EventDutyModel eventDutyModel = new EventDutyModel();
        eventDutyModel.setEventDutyId(ede.getEventDutyId());
        eventDutyModel.setName(ede.getName());
        eventDutyModel.setDescription(ede.getDescription());
        eventDutyModel.setStarttime(ede.getStarttime());
        eventDutyModel.setEndtime(ede.getEndtime());
        eventDutyModel.setEventType(ede.getEventType().toString());
        eventDutyModel.setEventStatus(ede.getEventStatus().toString());
        eventDutyModel.setConductor(ede.getConductor());
        eventDutyModel.setLocation(ede.getLocation());
        eventDutyModel.setDefaultPoints(ede.getDefaultPoints());
        eventDutyModel.setInstrumentation(ede.getInstrumentation());
        eventDutyModel.setRehearsalFor(ede.getRehearsalFor());
        if(!ede.getEventDutyMusicalWorksByEventDutyId().isEmpty()) {
            List<MusicalWorkModel> musicalWorkModels = new ArrayList<>();
            for (EventDutyMusicalWorkEntity eventDutyMusicalWorkEntity : ede.getEventDutyMusicalWorksByEventDutyId()) {
                MusicalWorkEntity musicalWorkEntity = eventDutyMusicalWorkEntity.getMusicalWorkByMusicalWork();
                musicalWorkModels.add(getMusicalWorkModel(musicalWorkEntity));
            }
            eventDutyModel.setMusicalWorks(musicalWorkModels);
        }
        return eventDutyModel;
    }

    private static EventDutyDTO createEventDutyDTO (EventDutyModel eventDutyModel) {
        EventDutyDTO eventDutyDTO = new EventDutyDTO();
        eventDutyDTO.setEventDutyID(eventDutyModel.getEventDutyId());
        eventDutyDTO.setName(eventDutyModel.getName());
        eventDutyDTO.setDescription(eventDutyModel.getDescription());
        eventDutyDTO.setStartTime(eventDutyModel.getStarttime());
        eventDutyDTO.setEndTime(eventDutyModel.getEndtime());
        eventDutyDTO.setEventType(EventType.valueOf(eventDutyModel.getEventType()));
        eventDutyDTO.setEventStatus(EventStatus.valueOf(eventDutyModel.getEventStatus()));
        eventDutyDTO.setConductor(eventDutyModel.getConductor());
        eventDutyDTO.setEventLocation(eventDutyModel.getLocation());
        eventDutyDTO.setPoints(eventDutyModel.getDefaultPoints());
        eventDutyDTO.setInstrumentation(eventDutyModel.getInstrumentation());
        eventDutyDTO.setRehearsalFor(eventDutyModel.getRehearsalFor());

        if(eventDutyModel.getMusicalWorks() != null) {
            List<MusicalWorkDTO> musicalWorkDTOS = new ArrayList<>();
            for (MusicalWorkModel musicalWorkModel : eventDutyModel.getMusicalWorks()) {
                musicalWorkDTOS.add(getMusicalWorkDTO(musicalWorkModel));
            }
            eventDutyDTO.setMusicalWorks(musicalWorkDTOS);
        }
        return eventDutyDTO;
    }

    private static MusicalWorkDTO getMusicalWorkDTO(MusicalWorkModel musicalWorkModel) {
        MusicalWorkDTO musicalWorkDTO = new MusicalWorkDTO();
        musicalWorkDTO.setName(musicalWorkModel.getName());
        musicalWorkDTO.setComposer(musicalWorkModel.getComposer());
        musicalWorkDTO.setId(musicalWorkModel.getId());
        musicalWorkDTO.setInstrumentationId(musicalWorkModel.getInstrumentationId());
        return musicalWorkDTO;
    }

    private static MusicalWorkModel getMusicalWorkModel(MusicalWorkEntity musicalWorkEntity) {
        MusicalWorkModel musicalWorkModel = new MusicalWorkModel();
        musicalWorkModel.setName(musicalWorkEntity.getName());
        musicalWorkModel.setComposer(musicalWorkEntity.getComposer());
        musicalWorkModel.setId(musicalWorkEntity.getMusicalWorkId());
        musicalWorkModel.setInstrumentationId(musicalWorkEntity.getInstrumentationId());
        return musicalWorkModel;
    }
}