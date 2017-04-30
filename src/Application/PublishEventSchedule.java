package Application;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.*;
import Domain.EventDutyModel;
import Domain.MusicalWorkModel;
import Persistence.Entities.*;
import Persistence.EventDutyRDBMapper;
import Persistence.Entities.EventDutyEntity;
import Utils.MessageHelper;
import Utils.Validator;
import javax.xml.bind.ValidationException;


/**
 * Created by julia on 28.04.2017.
 */
public class PublishEventSchedule {

    public static void publish(Year year, Month month) {
        //Calculate first and last day of month; note that Month starts with 0
        EventDutyModel eventDM;
        LocalDateTime start = LocalDateTime.of(year.getValue(), month.getValue()+1, 1, 0,0,0);
        Calendar firstOfMonth = Calendar.getInstance();
        firstOfMonth.set(year.getValue(), month.getValue(), 1);
        firstOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        firstOfMonth.set(Calendar.MINUTE, 0);
        firstOfMonth.set(Calendar.SECOND, 0);
        Calendar lastOfMonth = Calendar.getInstance();
        lastOfMonth.set(year.getValue(), month.getValue(), start.toLocalDate().lengthOfMonth());
        lastOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        lastOfMonth.set(Calendar.MINUTE, 59);
        lastOfMonth.set(Calendar.SECOND, 59);

        List<EventDutyEntity> dutiesInRange = EventDutyRDBMapper.getEventDutyInRange(firstOfMonth, lastOfMonth);

        for(EventDutyEntity evt : dutiesInRange){
            eventDM = createEventDutyModel(evt);
            try{ eventDM.validate(); }catch (ValidationException val){
                MessageHelper.showErrorAlertMessage("Please complete duty " + evt.getEventType() + ", " + evt.getStarttime() +
                                                    "\n" + val.getMessage() );
                return;
            }

            if(!hardValid(evt))return;
        }

        //Lade alle Termine in diesem Monat

        //für alle Termine in diesem Monat: gehe jeden Termin einzeln in einer for-Schleife durch und schaue ob alle Felder außer die nullable Felder natürlich, gesetzt sind
        //falls bei einem Termin ein feld nicht gesetzt ist: return warnung
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

    private static MusicalWorkModel getMusicalWorkModel(MusicalWorkEntity musicalWorkEntity) {
        MusicalWorkModel musicalWorkModel = new MusicalWorkModel();
        musicalWorkModel.setName(musicalWorkEntity.getName());
        musicalWorkModel.setComposer(musicalWorkEntity.getComposer());
        musicalWorkModel.setId(musicalWorkEntity.getMusicalWorkId());
        musicalWorkModel.setInstrumentationId(musicalWorkEntity.getInstrumentationId());
        return musicalWorkModel;
    }
}
