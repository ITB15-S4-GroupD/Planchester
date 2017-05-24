package TeamF.Application;

import Persistence.Entities.AccountEntity;
import Persistence.Entities.InstrumentEntity;
import Persistence.Entities.MusicalWorkEntity;
import Persistence.Entities.PersonEntity;
import Persistence.PersistanceFacade;
import TeamF.Domain.enums.AccountRole;
import TeamF.Domain.enums.InstrumentType;
import TeamF.Domain.enums.PersonRole;
import TeamF.jsonconnector.enums.*;
import javafx.util.Pair;
import TeamF.Hibernate.facade.AccountFacade;
import TeamF.Hibernate.facade.PersonFacade;
import TeamF.Domain.entities.Account;
import TeamF.Domain.entities.Person;
import TeamF.Domain.enums.*;
import TeamF.Domain.interfaces.DomainEntity;
import TeamF.Domain.logic.AccountLogic;
import TeamF.Domain.logic.DomainEntityManager;
import TeamF.Domain.logic.PersonLogic;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PersonApplication {

    PersistanceFacade<PersonEntity> personEntityPersistanceFacade = new PersistanceFacade<>(PersonEntity.class);

    public List<TeamF.jsonconnector.entities.Person> getAllMusicians() {
        List<TeamF.jsonconnector.entities.Person> persons = new ArrayList<>();

        for(PersonEntity personEntity : personEntityPersistanceFacade.list(null)) {
            persons.add(createPerson(personEntity));
        }
        return persons;
    }

    private TeamF.jsonconnector.entities.Person createPerson(PersonEntity personEntity) {
        TeamF.jsonconnector.entities.Person person = new TeamF.jsonconnector.entities.Person();

        TeamF.jsonconnector.entities.Account account = null;
        if(personEntity.getAccountByAccount() != null) {
            account = new TeamF.jsonconnector.entities.Account();
            account.setAccountID(personEntity.getAccountByAccount().getAccountId());
            account.setPassword(personEntity.getAccountByAccount().getPassword());
            account.setRole(TeamF.jsonconnector.enums.AccountRole.valueOf(personEntity.getAccountByAccount().getAccountRole()));
            account.setUsername(personEntity.getAccountByAccount().getUsername());
        }
        person.setPersonID(personEntity.getPersonId());
        person.setAccount(account);
        person.setAddress(personEntity.getAddress());
        person.setEmail(personEntity.getEmail());
        person.setFirstname(personEntity.getFirstname());
        person.setLastname(personEntity.getLastname());
        person.setInitials(personEntity.getInitials());
        if(personEntity.getGender().equals("m")) {
            person.setGender(Gender.MALE);
        } else {
            person.setGender(Gender.FEMALE);
        }
        person.setPhoneNumber(personEntity.getPhoneNumber());
        person.setPersonRole(TeamF.jsonconnector.enums.PersonRole.valueOf(personEntity.getPersonRole()));

        return person;
    }

    public List<Pair<InstrumentType, List<Person>>> getMusicianListByPlayedInstrumentType(List<Person> persons) {
        List<Pair<InstrumentType, List<Person>>> list = new LinkedList<>();
        List<Person> instrumentList = new LinkedList<>();

        for (InstrumentType instrumentType : InstrumentType.values()) {
            Pair<InstrumentType, List<Person>> pair = new Pair<>(instrumentType, instrumentList);

            for (Person person : persons) {
                for(InstrumentType instrument : person.getPlayedInstruments()) {
                    if(instrument == instrumentType) {
                        instrumentList.add(person);
                    }
                }
            }

            list.add(pair);
        }

        return list;
    }

    public Pair<PersonEntity, List<Pair<String, String>>> add(TeamF.jsonconnector.entities.Person person) {
        PersonEntity newPerson = new PersonEntity();
        newPerson.setPersonId(person.getPersonID());
        newPerson.setFirstname(person.getFirstname());
        newPerson.setLastname(person.getLastname());
        newPerson.setGender(person.getGender().toString());
        newPerson.setAddress(person.getAddress());
        newPerson.setEmail(person.getEmail());
        newPerson.setPhoneNumber(person.getPhoneNumber());
        newPerson.setPersonRole(person.getPersonRole().toString());
        //person.addInstrument(instrument);
        if (!(newPerson.getPersonRole().equals(PersonRole.Manager)||
                newPerson.getPersonRole().equals(PersonRole.Music_librarian))||
                newPerson.getPersonRole().equals(PersonRole.Orchestral_facility_manager)) {

            List<InstrumentEntity> instrumentEntities = new ArrayList<>();

            for(TeamF.jsonconnector.enums.InstrumentType instrumentType : person.getInstrumentTypeList()) {
                PersistanceFacade<InstrumentEntity> instrumentEntityPersistanceFacade = new PersistanceFacade<>(InstrumentEntity.class);
                InstrumentEntity instrumentEntity = instrumentEntityPersistanceFacade.get(p ->
                        p.getInstrumentTypeByInstrumentType().getInstrumentType().equals(instrumentType.toString()));
                instrumentEntities.add(instrumentEntity);
            }


            newPerson.setInstrumentsByPersonId(instrumentEntities);
        }

        if(person.getFirstname() != null && person.getLastname() != null
                && person.getFirstname().length() > 0 && person.getLastname().length() > 0) {
            newPerson.setInitials("" + person.getFirstname().charAt(0) + person.getLastname().charAt(0));
        }

        AccountEntity account = new AccountEntity();
        account.setUsername(person.getAccount().getUsername());
        account.setAccountRole(person.getAccount().getRole().toString());

        SecureRandom random = new SecureRandom();
        // @TODO: save a hashed value to the DB
        account.setPassword(new BigInteger(130, random).toString(32));

        PersistanceFacade<AccountEntity> accountEntityPersistanceFacade = new PersistanceFacade<>(AccountEntity.class);
        account = accountEntityPersistanceFacade.put(account);
        newPerson.setAccountByAccount(account);

        newPerson = personEntityPersistanceFacade.put(newPerson);

        return new Pair<>(newPerson, new LinkedList<>());
    }

    public void edit(TeamF.jsonconnector.entities.Person person) {
        PersonEntity newPerson = personEntityPersistanceFacade.get(person.getPersonID());
        newPerson.setPersonId(person.getPersonID());
        newPerson.setFirstname(person.getFirstname());
        newPerson.setLastname(person.getLastname());
        newPerson.setGender(person.getGender().toString());
        newPerson.setAddress(person.getAddress());
        newPerson.setEmail(person.getEmail());
        newPerson.setPhoneNumber(person.getPhoneNumber());
        newPerson.setPersonRole(person.getPersonRole().toString());
        //person.addInstrument(instrument);
        if (!(newPerson.getPersonRole().equals(PersonRole.Manager)||
                newPerson.getPersonRole().equals(PersonRole.Music_librarian))||
                newPerson.getPersonRole().equals(PersonRole.Orchestral_facility_manager)) {

            List<InstrumentEntity> instrumentEntities = new ArrayList<>();

            for(TeamF.jsonconnector.enums.InstrumentType instrumentType : person.getInstrumentTypeList()) {
                PersistanceFacade<InstrumentEntity> instrumentEntityPersistanceFacade = new PersistanceFacade<>(InstrumentEntity.class);
                InstrumentEntity instrumentEntity = instrumentEntityPersistanceFacade.get(p ->
                        p.getInstrumentTypeByInstrumentType().getInstrumentType().equals(instrumentType.toString()));
                instrumentEntities.add(instrumentEntity);
            }


            newPerson.setInstrumentsByPersonId(instrumentEntities);
        }

        if(person.getFirstname() != null && person.getLastname() != null
                && person.getFirstname().length() > 0 && person.getLastname().length() > 0) {
            newPerson.setInitials("" + person.getFirstname().charAt(0) + person.getLastname().charAt(0));
        }

        personEntityPersistanceFacade.put(newPerson);
    }

    public void remove(TeamF.jsonconnector.entities.Person person) {
        personEntityPersistanceFacade.remove(person.getPersonID());
    }
}
