package TeamF.client.pages.musicianmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import TeamF.client.entities.KeyValuePair;
import TeamF.jsonconnector.entities.Instrument;
import TeamF.jsonconnector.entities.Person;
import TeamF.jsonconnector.enums.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MusicianTableHelper {
    // Returns PersonTestData Id TableColumn
    public static TableColumn<Person, Integer> getIdColumn() {
        TableColumn<Person, Integer> idCol = new TableColumn<>("Id");
        PropertyValueFactory<Person, Integer> idCellValueFactory = new PropertyValueFactory<>("personID");
        idCol.setCellValueFactory(idCellValueFactory);
        return idCol;
    }

    // Returns First Name TableColumn
    public static TableColumn<Person, String> getFirstNameColumn() {
        TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
        PropertyValueFactory<Person, String> firstNameCellValueFactory = new PropertyValueFactory<>("firstname");
        firstNameCol.setCellValueFactory(firstNameCellValueFactory);
        return firstNameCol;
    }

    // Returns Last Name TableColumn
    public static TableColumn<Person, String> getLastNameColumn() {
        TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");
        PropertyValueFactory<Person, String> lastNameCellValueFactory = new PropertyValueFactory<>("lastname");
        lastNameCol.setCellValueFactory(lastNameCellValueFactory);
        return lastNameCol;
    }

    // Returns Street TableColumn
    public static TableColumn<Person, String> getStreetColumn() {
        TableColumn<Person, String> streetCol = new TableColumn<>("Address");
        PropertyValueFactory<Person, String> streetCellValueFactory = new PropertyValueFactory<>("address");
        streetCol.setCellValueFactory(streetCellValueFactory);
        return streetCol;
    }

    // Returns ZipCode TableColumn
    public static TableColumn<Person, String> getZipCodeColumn() {
        TableColumn<Person, String> zipCodeCol = new TableColumn<>("Email");
        PropertyValueFactory<Person, String> zipCodeCellValueFactory = new PropertyValueFactory<>("email");
        zipCodeCol.setCellValueFactory(zipCodeCellValueFactory);
        return zipCodeCol;
    }

    // Returns City TableColumn
    public static TableColumn<Person, String> getPhonenumberColumn() {
        TableColumn<Person, String> cityCol = new TableColumn<>("Phone Number");
        PropertyValueFactory<Person, String> cityCellValueFactory = new PropertyValueFactory<>("phoneNumber");
        cityCol.setCellValueFactory(cityCellValueFactory);
        return cityCol;
    }

    // Returns Phone TableColumn
    public static TableColumn<Person, String> getRoleColumn() {
        TableColumn<Person, String> cityCol = new TableColumn<>("Role");
        PropertyValueFactory<Person, String> cityCellValueFactory = new PropertyValueFactory<>("personRole");
        cityCol.setCellValueFactory(cityCellValueFactory);
        return cityCol;
    }

    public static TableColumn<Person, String> getInstrumentColumn() {
        TableColumn<Person, String> countryCol = new TableColumn<>("Instrument Type");
        PropertyValueFactory<Person, String> countryCellValueFactory = new PropertyValueFactory<>("listToString");
        countryCol.setCellValueFactory(countryCellValueFactory);
        return countryCol;
    }

    public static ObservableList<KeyValuePair> getPersonRoleList() {
        ObservableList<KeyValuePair> list = FXCollections.observableArrayList(
                new KeyValuePair("Musician", PersonRole.Musician),
                new KeyValuePair("External Musician", PersonRole.External_musician),
                new KeyValuePair("Manager", PersonRole.Manager),
                new KeyValuePair("Musical Librarian", PersonRole.Music_librarian),
                new KeyValuePair("Facility Manager", PersonRole.Orchestral_facility_manager),
                new KeyValuePair("Substitute", PersonRole.Substitute));

        return list;
    }

    public static ObservableList<KeyValuePair> getAccountRoleList() {
        ObservableList<KeyValuePair> list = FXCollections.observableArrayList(
                new KeyValuePair("Musician", AccountRole.Musician),
                new KeyValuePair("Administrator", AccountRole.Administrator),
                new KeyValuePair("Manager", AccountRole.Manager),
                new KeyValuePair("Section Representative", AccountRole.Section_representative),
                new KeyValuePair("Substitute", AccountRole.Substitute));

        return list;
    }

    public static ObservableList<KeyValuePair> getGenderList() {
        ObservableList<KeyValuePair> list = FXCollections.observableArrayList(
                new KeyValuePair("Female", Gender.FEMALE),
                new KeyValuePair("Male", Gender.MALE));

        return list;
    }

    public static ObservableList<KeyValuePair> getInstrumentTypeList(SectionType sectionType) {
        ObservableList<KeyValuePair> list = FXCollections.observableArrayList();

        switch (sectionType) {

            case BRASS:
                list.addAll(
                        new KeyValuePair("Horn", InstrumentType.HORN),
                        new KeyValuePair("Trombone", InstrumentType.TROMBONE),
                        new KeyValuePair("Trumpet", InstrumentType.TRUMPET),
                        new KeyValuePair("Tube", InstrumentType.TUBE),
                        new KeyValuePair("Cimbasso", InstrumentType.CIMBASSO),
                        new KeyValuePair("Wagner Tuba", InstrumentType.WAGNERTUBA),
                        new KeyValuePair("Euphonium", InstrumentType.EUPHONIUM),
                        new KeyValuePair("Saxophone", InstrumentType.SAXOPHONE)
                );
                break;
            case VIOLA:
                list.addAll(
                        new KeyValuePair("Viola", InstrumentType.VIOLA)
                );
                break;
            case VIOLIN1:
                list.addAll(
                        new KeyValuePair("1. Violin", InstrumentType.FIRSTVIOLIN)
                );
                break;
            case VIOLIN2:
                list.addAll(
                        new KeyValuePair("2. Violin", InstrumentType.SECONDVIOLIN)
                );
                break;
            case WOODWIND:
                list.addAll(
                        new KeyValuePair("Flute", InstrumentType.FLUTE),
                        new KeyValuePair("Oboe", InstrumentType.OBOE),
                        new KeyValuePair("Clarinet", InstrumentType.CLARINET),
                        new KeyValuePair("Bassoon", InstrumentType.BASSOON),
                        new KeyValuePair("Contrabassoon", InstrumentType.CONTRABASSOON),
                        new KeyValuePair("Heckelphone", InstrumentType.HECKELPHONE),
                        new KeyValuePair("Bassclarinet", InstrumentType.BASSCLARINET),
                        new KeyValuePair("Piccolo", InstrumentType.PICCOLO),
                        new KeyValuePair("Contrabasstrombone", InstrumentType.CONTRABASSTROMBONE),
                        new KeyValuePair("Basstrombone", InstrumentType.BASSTROMBONE),
                        new KeyValuePair("Bassethorn", InstrumentType.BASSETHORN),
                        new KeyValuePair("French Horn", InstrumentType.FRENCHHORN),
                        new KeyValuePair("English Horn", InstrumentType.ENGLISHHORN)
                );

                break;
            case DOUBLEBASS:
                list.addAll(
                        new KeyValuePair("Double Bass", InstrumentType.DOUBLEBASS)
                );

                break;
            case PERCUSSION:
                list.addAll(
                        new KeyValuePair("Percussion", InstrumentType.PERCUSSION),
                        new KeyValuePair("Harp", InstrumentType.HARP),
                        new KeyValuePair("Kettledrum", InstrumentType.KETTLEDRUM),
                        new KeyValuePair("Piano", InstrumentType.PIANO),
                        new KeyValuePair("Celesta", InstrumentType.CELESTA),
                        new KeyValuePair("Organ", InstrumentType.ORGAN),
                        new KeyValuePair("Cembalo", InstrumentType.CEMBALO),
                        new KeyValuePair("Accordeon", InstrumentType.ACCORDEON),
                        new KeyValuePair("Keyboard", InstrumentType.KEYBOARD),
                        new KeyValuePair("Bandoneon", InstrumentType.BANDONEON),
                        new KeyValuePair("Guitar", InstrumentType.GUITAR),
                        new KeyValuePair("Mandolin", InstrumentType.MANDOLIN)
                );

                break;
            case VIOLINCELLO:
                list.addAll(
                        new KeyValuePair("Violincello", InstrumentType.VIOLONCELLO)
                );

                break;
        }

        return list;
    }

    public static ObservableList<KeyValuePair> getSectionTypeList() {
        ObservableList<KeyValuePair> list = FXCollections.observableArrayList(
                new KeyValuePair("Brass", SectionType.BRASS),
                new KeyValuePair("Doublebass", SectionType.DOUBLEBASS),
                new KeyValuePair("Percussion", SectionType.PERCUSSION),
                new KeyValuePair("Viola", SectionType.VIOLA),
                new KeyValuePair("Violin 1", SectionType.VIOLIN1),
                new KeyValuePair("Violin 2", SectionType.VIOLIN2),
                new KeyValuePair("Violincello", SectionType.VIOLINCELLO),
                new KeyValuePair("Woodwind", SectionType.WOODWIND));

        return list;
    }

    //TODO: bring lists in order
    //Same order likeGetSectionTypeList, do not change!
    public static LinkedList<List> getSectionInstrumentPos() {
        List<InstrumentType> brass= Arrays.asList(InstrumentType.HORN,InstrumentType.TROMBONE, InstrumentType.TRUMPET,InstrumentType.TUBE,
                InstrumentType.CIMBASSO, InstrumentType.WAGNERTUBA, InstrumentType.EUPHONIUM, InstrumentType.SAXOPHONE);
        List<InstrumentType> viola= Arrays.asList(InstrumentType.VIOLA);
        List<InstrumentType> violin1= Arrays.asList(InstrumentType.FIRSTVIOLIN);
        List<InstrumentType> violin2= Arrays.asList(InstrumentType.SECONDVIOLIN);
        List<InstrumentType> woodwind= Arrays.asList(InstrumentType.FLUTE,InstrumentType.OBOE, InstrumentType.CLARINET,InstrumentType.BASSOON,
                InstrumentType.CONTRABASSOON, InstrumentType.HECKELPHONE, InstrumentType.BASSCLARINET, InstrumentType.PICCOLO, InstrumentType.CONTRABASSTROMBONE,
                InstrumentType.BASSTROMBONE, InstrumentType.BASSETHORN, InstrumentType.FRENCHHORN, InstrumentType.ENGLISHHORN);
        List<InstrumentType> doublebass= Arrays.asList(InstrumentType.DOUBLEBASS);
        List<InstrumentType> percussion= Arrays.asList(InstrumentType.PERCUSSION,InstrumentType.HARP, InstrumentType.KETTLEDRUM, InstrumentType.KETTLEDRUM, InstrumentType.PIANO,
                InstrumentType.CELESTA, InstrumentType.ORGAN, InstrumentType.CEMBALO, InstrumentType.ACCORDEON, InstrumentType.KEYBOARD, InstrumentType.BANDONEON,
                InstrumentType.GUITAR, InstrumentType.MANDOLIN);
        List<InstrumentType> violincello= Arrays.asList(InstrumentType.VIOLONCELLO);
        LinkedList<List> sections = new LinkedList<>();
        sections.add(brass);
        sections.add(doublebass);
        sections.add(percussion);
        sections.add(viola);
        sections.add(violin1);
        sections.add(violin2);
        sections.add(violincello);
        sections.add(woodwind);

        return sections;

    }

    public static int getRolePos(PersonRole personRole ) {
        int pos = 0;
        for (KeyValuePair role : getPersonRoleList()) {
            if (role.getValue().equals(personRole)) {
                return pos;
            }
            pos++;
        }
        return -1;

    }

    public static int getGenderPos(Gender personGender) {
        int pos = 0;
        for (KeyValuePair gender : getGenderList()) {
            if (gender.getValue().equals(personGender)) {
                return pos;
            }
            pos++;
        }
        return -1;

    }

    //Todo: change names
    public static int[] getFirstInstrumentPos(InstrumentType instrumentType) {
        List sections = getSectionInstrumentPos();
        int sectionPos = 0;
        int instrumentPos = 0;
        for (int i = 0; i < sections.size(); i++) {
            List instruments = (List) sections.get(i);
            for (int j = 0; j < instruments.size(); j++) {
                if (instruments.get(j).toString().equals(instrumentType.toString())) {
                    sectionPos = i;
                    instrumentPos = j;
                }
            }
        }
        return new int[]{sectionPos, instrumentPos};

    }


    public static int getAccountPos(AccountRole accountRole) {
        int pos = 0;
        for (KeyValuePair role : getAccountRoleList()) {
            if (role.getValue().equals(accountRole)) {
                return pos;
            }
            pos++;
        }
        return -1;

    }
}
