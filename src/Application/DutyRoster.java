package Application;

import Domain.Models.EventDutyModel;
import Persistence.Entities.*;
import Persistence.PersistanceFacade;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Month;
import java.time.Year;
import java.util.*;

import static Application.EventScheduleManager.createEventDutyModel;

/**
 * Created by Bernd u. Garvin on 5/8/2017.
 *
 * @Project Planchester
 */
public class DutyRoster {

    private Calendar firstOfMonth = Calendar.getInstance();
    private Calendar firstOfNextMonth = Calendar.getInstance();
    private String section;
    private MusicianPartEntity musicianPart;
    private List<EventDutyModel> eventsDuring;

    public void validateMonth(Year year, Month month) {
        //zweimal selbe parameter übergeben, da aufruf und logik bei setter
        this.setFirstOfMonth(year, month);
        this.setFirstOfNextMonth(year, month);

        Collection<PersonEntity> persons = AccountAdministrationManager.getInstance().getLoggedInAccount().getPeopleByAccountId();
        for (PersonEntity person : persons) {
            Collection<MusicianPartEntity> parts = person.getMusicianPartsByPersonId();
            for (MusicianPartEntity part : parts) {
                part.getPartByPart().getSectionType();
                this.section = part.getPartByPart().getSectionType().toString();
                this.musicianPart = part;
            }
        }

        this.eventsDuring = this.getEventsDuringMonth(year, month);

        for (EventDutyModel event : this.eventsDuring) {
            this.validateDuty(event);
        }

    }

    public void validateDuty(EventDutyModel event) {
        Integer instrumentationID = event.getInstrumentation();
        int amount = 0;
        PersistanceFacade<StringInstrumentationEntity> persistenceFacadeString;
        PersistanceFacade<WoodInstrumentationEntity> persistenceFacadeWood;
        PersistanceFacade<BrassInstrumentationEntity> persistenceFacadeBrass;
        PersistanceFacade<PercussionInstrumentationEntity> persistenceFacadePercussion;
        StringInstrumentationEntity stringInstrumentationEntity;
        WoodInstrumentationEntity woodInstrumentationEntity;
        BrassInstrumentationEntity brassInstrumentationEntity;
        PercussionInstrumentationEntity percussionInstrumentationEntity;

        switch (this.section) {
            case ("Violin1"):
                persistenceFacadeString = new PersistanceFacade<>(StringInstrumentationEntity.class);
                stringInstrumentationEntity = persistenceFacadeString.get(instrumentationID);
                amount = stringInstrumentationEntity.getViolin1();
                break;
            case "Violin2":
                persistenceFacadeString = new PersistanceFacade<>(StringInstrumentationEntity.class);
                stringInstrumentationEntity = persistenceFacadeString.get(instrumentationID);
                amount = stringInstrumentationEntity.getViolin2();
                break;
            case "Viola":
                persistenceFacadeString = new PersistanceFacade<>(StringInstrumentationEntity.class);
                stringInstrumentationEntity = persistenceFacadeString.get(instrumentationID);
                amount = stringInstrumentationEntity.getViola();
                break;
            case "Violincello":
                persistenceFacadeString = new PersistanceFacade<>(StringInstrumentationEntity.class);
                stringInstrumentationEntity = persistenceFacadeString.get(instrumentationID);
                amount = stringInstrumentationEntity.getViolincello();
                break;
            case "Doublebass":
                persistenceFacadeString = new PersistanceFacade<>(StringInstrumentationEntity.class);
                stringInstrumentationEntity = persistenceFacadeString.get(instrumentationID);
                amount = stringInstrumentationEntity.getDoublebass();
                break;

            case "Woodwind":
                persistenceFacadeWood = new PersistanceFacade<>(WoodInstrumentationEntity.class);
                woodInstrumentationEntity = persistenceFacadeWood.get(instrumentationID);
                amount = woodInstrumentationEntity.getBassoon() + woodInstrumentationEntity.getClarinet() + woodInstrumentationEntity.getFlute() + woodInstrumentationEntity.getOboe();
                break;

            case "Brass":
                persistenceFacadeBrass = new PersistanceFacade<>(BrassInstrumentationEntity.class);
                brassInstrumentationEntity = persistenceFacadeBrass.get(instrumentationID);
                amount = brassInstrumentationEntity.getHorn() + brassInstrumentationEntity.getTrombone() + brassInstrumentationEntity.getTrumpet() + brassInstrumentationEntity.getTube();
                break;

            case "Percussion":
                persistenceFacadePercussion = new PersistanceFacade<>(PercussionInstrumentationEntity.class);
                percussionInstrumentationEntity = persistenceFacadePercussion.get(instrumentationID);
                amount = percussionInstrumentationEntity.getHarp() + percussionInstrumentationEntity.getKettledrum() + percussionInstrumentationEntity.getPercussion();
                break;

            default:
                //do nothing
        }

        this.checkIfEnoughMusiciansAreAdressed(this.section, amount, event);
    }

    public void checkIfEnoughMusiciansAreAdressed(String section, int necessary, EventDutyModel eventDutyModel){
        //Vergleich benötigte mit zugewiesenen Musikern
        DutyDispositionEntity dutyDispositionEntity = null;
        EventDutyEntity eventDutyEntity;
        dutyDispositionEntity.setEventDuty(eventDutyModel.getEventDutyId());
        eventDutyEntity = dutyDispositionEntity.getEventDutyByEventDuty();
        MusicianPartEntity mp;
        dutyDispositionEntity.getMusician();

    }

    public List<EventDutyModel> getEventsDuringMonth(Year year, Month month) {
        this.setFirstOfMonth(year, month);
        this.setFirstOfNextMonth(year, month); 

        PersistanceFacade<EventDutyEntity> eventDutyEntityPersistanceFacade = new PersistanceFacade<>(EventDutyEntity.class);
        List<EventDutyEntity> eventDuties = eventDutyEntityPersistanceFacade.list(p -> p.getStarttime().after(this.getFirstOfMonth().getTime())
                && p.getStarttime().before(this.getFirstOfNextMonth().getTime()));

        List<EventDutyModel> eventDutyModelList = new ArrayList<>();
        for (EventDutyEntity eventDutyEntity : eventDuties) {
            eventDutyModelList.add(createEventDutyModel(eventDutyEntity));
        }
        return eventDutyModelList;
    }

    /**
     * Getters and Setters
     */

    public String getSection() {
        return this.section;
    }

    public void setFirstOfMonth(Year year, Month month) {
        this.firstOfMonth.set(year.getValue(), month.getValue() - 1, 1);
    }

    public Calendar getFirstOfMonth() {
        return this.firstOfMonth;
    }

    public void setFirstOfNextMonth(Year year, Month month) {
        this.firstOfNextMonth.set(year.getValue(), month.getValue(), 1);
    }

    public Calendar getFirstOfNextMonth() {
        return firstOfNextMonth;
    }

}
