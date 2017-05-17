package TeamF.Hibernate.facade;

import Persistence.Entities.AccountEntity;
import Persistence.Entities.MusicianPartEntity;
import Persistence.Entities.PersonEntity;
import TeamF.Domain.entities.Account;
import TeamF.Domain.entities.Person;
import TeamF.Domain.enums.InstrumentType;
import TeamF.Domain.enums.PersonRole;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PersonFacade extends BaseDatabaseFacade<Person> {
    private static AccountFacade _accountFacade = new AccountFacade();
    private static InstrumentTypeFacade _instrumentTypeFacade = new InstrumentTypeFacade();

    public PersonFacade() {
        super();
    }

    public PersonFacade(EntityManager session) {
        super(session);
    }

    /**
     * Function to get all Musicians. Returns a List of Persons
     *
     * @return musicians      List<Person>         returns a list of persons
     */
    public List<Person> getAllMusicians() {
        EntityManager session = getCurrentSession();

        Query query = session.createQuery("from PersonEntity");

        List<PersonEntity> musicianEntities = query.getResultList();
        List<Person> musicians = new ArrayList<>();
        Person person;

        for (PersonEntity entity : musicianEntities) {
            person = convertToPerson(entity);

            Collection<String> playedInstruments = _instrumentTypeFacade.getPlayedInstrumentsByPersonId(entity.getPersonId());

            if(playedInstruments != null && playedInstruments.size() > 0) {
                // set only the first item because musicians cannot play multiple instruments in the orchestra (is only a feature for the future)
                for (String instrumentType: playedInstruments) {
                    //person.addPlayedInstrument(InstrumentType.valueOf(instrumentType));
                    try {
                        person.addPlayedInstrument(InstrumentType.valueOf(instrumentType.replace(" ", "").toUpperCase()));
                    } catch (Exception e) {
                    }
                }
            }

            musicians.add(person);
        }

        return musicians;
    }

    private Integer register(Person person) {
        EntityManager session = getCurrentSession();
        session.getTransaction().begin();

        PersonEntity personEntity = convertToPersonEntity(person);

        if (!(person.getPersonRole().equals(PersonRole.External_musician) || personEntity.getPersonId()> 0)) {
            Account account = person.getAccount();
            AccountEntity accountEntity = _accountFacade.convertToAccountEntity(account);
            session.persist(accountEntity);

            personEntity.setAccount(accountEntity.getAccountId());
        }

        try {
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }

        session.getTransaction().begin();

        if (personEntity.getPersonId() > 0){
            session.merge(personEntity);
        } else {
            session.persist(personEntity);
            session.flush();
        }

        try {
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }

        session.getTransaction().begin();

        List<MusicianPartEntity> musicianPartEntities = getMusicianPartEntityFromPerson(person);

        for (MusicianPartEntity musicianPartEntity: musicianPartEntities) {
            musicianPartEntity.setMusician(personEntity.getPersonId());
            session.persist(musicianPartEntity);
        }

        try {
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }

        return personEntity.getPersonId();
    }

    public static boolean contains(String test) {

        for (InstrumentType c : InstrumentType.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }

    protected PersonEntity convertToPersonEntity(Person person) {
        PersonEntity entity = new PersonEntity();

        entity.setPersonId(person.getPersonID());
        entity.setAddress(person.getAddress());
        entity.setEmail(person.getEmail());
        entity.setFirstname(person.getFirstname());
        entity.setLastname(person.getLastname());
        entity.setInitials(person.getInitials());
        entity.setGender(person.getGender());
        entity.setPhoneNumber(person.getPhoneNumber());
        entity.setPersonRole(String.valueOf(person.getPersonRole()));

        return entity;
    }

    protected List<MusicianPartEntity> getMusicianPartEntityFromPerson (Person person) {
        List<MusicianPartEntity> musicianPartEntities = new LinkedList();

        for (InstrumentType instrumentType : person.getPlayedInstruments()) {
            MusicianPartEntity musicianPartEntity = new MusicianPartEntity();
            musicianPartEntity.setPart(_instrumentTypeFacade.getPartIdByPlayedInstrument(instrumentType));
            musicianPartEntities.add(musicianPartEntity);
        }

        return musicianPartEntities;
    }

    /**
     * Function to convert a PersonEntity object to a Person. Returns the Person after creating and setting information from PersonyEntity object.
     * @return person      Person        returns a person object
     */
    private Person convertToPerson(PersonEntity pe) {
        Person person = new Person();

        person.setPersonID(pe.getPersonId());
        person.setFirstname(pe.getFirstname());
        person.setLastname(pe.getLastname());
        person.setAddress(pe.getAddress());
        person.setEmail(pe.getEmail());
        person.setGender(pe.getGender());
        person.setInitials(pe.getInitials());
        person.setPhoneNumber(pe.getPhoneNumber());
        person.setPersonRole(PersonRole.valueOf(pe.getPersonRole().toString()));

        AccountEntity accountEntity = pe.getAccountByAccount();

        if(accountEntity != null) {
            person.setAccount(_accountFacade.convertToAccount(accountEntity));
        }

        return person;
    }

    @Override
    public int add(Person value) {
        return register(value);
    }

    @Override
    public int update(Person value) {
        return 0;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
