package TeamF.Application;

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
import java.util.LinkedList;
import java.util.List;

public class PersonApplication {
    private PersonFacade personFacade = new PersonFacade();
    private AccountFacade accountFacade = new AccountFacade();

    public PersonApplication() {
    }

    public void closeSession() {
        personFacade.closeSession();
    }

    public List<Person> getAllMusicians() {
        return personFacade.getAllMusicians();
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

    public Pair<DomainEntity, List<Pair<String, String>>> add(int id, String firstname, String lastname, String gender, String address,
                                                                   String email, String phoneNumber, int accountID, PersonRole personRole, String username,
                                                                   AccountRole accountRole, List<InstrumentType> instrumentTypeList) {
        Person person = new Person();
        person.setPersonID(id);
        person.setFirstname(firstname);
        person.setLastname(lastname);
        person.setGender(gender);
        person.setAddress(address);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
        person.setPersonRole(personRole);
        //person.addInstrument(instrument);
        if (!(person.getPersonRole().equals(PersonRole.Manager)||
                person.getPersonRole().equals(PersonRole.Music_librarian))||
                person.getPersonRole().equals(PersonRole.Orchestral_facility_manager)) {
            person.setPlayedInstruments(instrumentTypeList);
        }

        if(firstname != null && lastname != null && firstname.length() > 0 && lastname.length() > 0) {
            person.setInitials("" + firstname.charAt(0) + lastname.charAt(0));
        }

        Account account = new Account();
        account.setAccountID(accountID);
        account.setUsername(username);
        account.setRole(accountRole);

        SecureRandom random = new SecureRandom();
        // @TODO: save a hashed value to the DB
        account.setPassword(new BigInteger(130, random).toString(32));

        person.setAccount(account);

        PersonLogic personLogic = (PersonLogic) DomainEntityManager.getLogic(EntityType.PERSON);

        List<Account> accountList= accountFacade.getAllUserNames();
        List<Person> personList = getAllMusicians();


        AccountLogic accountLogic = (AccountLogic) DomainEntityManager.getLogic((EntityType.ACCOUNT));
        List<Pair<String, String>> errorList = personLogic.validate(person);

        if (!personRole.equals(PersonRole.External_musician)) {
            List<Pair<String, String>> errorList2 = accountLogic.validate(account);
            errorList.addAll(errorList2);
        }

        // do not check if it exists when we updating the person and the account
        if(person.getPersonID() <= 0) {
            for(Person p : personList){
                if(p.getFirstname().equals(firstname.trim()) && p.getLastname().equals(lastname.trim()) && p.getGender().equals(gender) && p.getAddress().equals(address.trim())
                        && p.getEmail().equals(email.trim()) && p.getPhoneNumber().equals(phoneNumber.trim()) && p.getPersonRole().equals(personRole) && p.getPlayedInstruments().equals(instrumentTypeList)){
                    errorList.add(new Pair<>(String.valueOf(PersonProperty.FIRSTNAME), "this musician already exists"));
                }
            }
            if (!personRole.equals(PersonRole.External_musician)) {
                for (Account ac : accountList) {
                    if (ac.getUsername().equals(username.trim())) {
                        errorList.add(new Pair<>(String.valueOf(AccountProperty.USERNAME), "username already exists"));
                    }
                }
            }
        }

        if(instrumentTypeList == null){
            errorList.add(new Pair<>(String.valueOf(PersonProperty.FIRSTNAME), "no instrument selected"));
        }

        if (errorList.size() > 0) {
            return new Pair<>(person, errorList);
        }

        Integer resultID = personFacade.add(person);
        person.setPersonID(resultID);

        return new Pair<>(person, new LinkedList<>());
    }
}
