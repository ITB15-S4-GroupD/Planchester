package TeamF.client.helper.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import TeamF.client.controls.numberfield.NumberField;
import TeamF.client.entities.KeyValuePair;
import TeamF.client.exceptions.NumberRangeException;
import TeamF.jsonconnector.entities.SpecialInstrumentation;
import TeamF.jsonconnector.enums.InstrumentType;
import TeamF.jsonconnector.enums.SectionGroupType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InstrumentationHelper {
    public static List<SpecialInstrumentation> getSpecialInstrumentation(List<SpecialInstrumentationEntity> _specialInstrumentationEntityList) {
        List<SpecialInstrumentation> specialInstrumentationList = new LinkedList<>();
        SpecialInstrumentation specialInstrumentation;

        for(SpecialInstrumentationEntity item : _specialInstrumentationEntityList) {
            if (item.getSpecialInstrumentationNumberField().getNumber().intValue() > 0 && item.getSpecialInstrumentationComboBox().getSelectionModel().getSelectedItem() != null) {
                if (!specialInstrumentationList.contains(item.getSectionTypeComboBox().getSelectionModel().getSelectedItem().getValue())) {
                    specialInstrumentation = new SpecialInstrumentation();
                    specialInstrumentation.setSpecialInstrumentationID(item.getSpecialInstrumentationID());
                    specialInstrumentation.setSectionType(String.valueOf(item.getSectionTypeComboBox().getSelectionModel().getSelectedItem()));
                    specialInstrumentation.setSpecialInstrumentationCount(item.getSpecialInstrumentationNumberField().getNumber().intValue());
                    specialInstrumentation.setSpecialInstrumentation(String.valueOf(item.getSpecialInstrumentationComboBox().getSelectionModel().getSelectedItem().getValue()));
                    specialInstrumentationList.add(specialInstrumentation);
                }
            }
        }
        return specialInstrumentationList;
    }

    public static void addListeners(List<SpecialInstrumentationEntity> specialInstrumentationEntityList, GridPane specialInstrumentationContent,
                                    ComboBox<KeyValuePair> specialInstrumentationSectionGroupComboBox, ComboBox<KeyValuePair> specialInstrumentationInstrumentTypeComboBox,
                                    NumberField specialInstrumentationNumberField, Button specialInstrumentationButton) {
        specialInstrumentationSectionGroupComboBox.setOnAction(event -> {
            specialInstrumentationInstrumentTypeComboBox.setItems(TeamF.client.helper.gui.InstrumentationHelper.getInstrumentTypes((SectionGroupType) specialInstrumentationSectionGroupComboBox.getSelectionModel().getSelectedItem().getValue()));
            specialInstrumentationInstrumentTypeComboBox.getSelectionModel().selectFirst();
        } );

        specialInstrumentationButton.setOnAction(event -> {
            TeamF.client.helper.gui.InstrumentationHelper.addSpecialInstrumentationItem(0, specialInstrumentationSectionGroupComboBox.getSelectionModel().getSelectedItem(), specialInstrumentationInstrumentTypeComboBox.getSelectionModel().getSelectedItem(), specialInstrumentationNumberField.getNumber().intValue(),
                    specialInstrumentationEntityList, specialInstrumentationContent, specialInstrumentationSectionGroupComboBox, specialInstrumentationNumberField);
        });
    }

    public static void resetSpecialInstrumentation(List<SpecialInstrumentationEntity> _specialInstrumentationEntityList, Pane contentPane) {
        for(SpecialInstrumentationEntity item : _specialInstrumentationEntityList) {
            item.getSpecialInstrumentationNumberField().setNumber(BigDecimal.ZERO);
            item.getSectionTypeComboBox().getSelectionModel().selectFirst();
            item.getSpecialInstrumentationComboBox().setItems(getInstrumentTypes((SectionGroupType) item.getSectionTypeComboBox().getSelectionModel().getSelectedItem().getValue()));
            item.getSpecialInstrumentationComboBox().getSelectionModel().selectFirst();
            contentPane.getChildren().remove(item.getPane());
        }

        _specialInstrumentationEntityList.clear();
    }


    public static void addSpecialInstrumentationItem(int id, KeyValuePair sectionType, KeyValuePair specialInstrumentation, int specialInstrumentationCount,
                                                     List<SpecialInstrumentationEntity> specialInstrumentationEntityList, GridPane specialInstrumentationContent,
                                                     ComboBox<KeyValuePair> specialInstrumentationSectionGroupComboBox, NumberField specialInstrumentationNumberField) {
        GridPane tmpPane = new GridPane();

        ComboBox<KeyValuePair> sectionTypeComboBox = new ComboBox<>(specialInstrumentationSectionGroupComboBox.getItems());
        sectionTypeComboBox.getSelectionModel().select(sectionType);
        sectionTypeComboBox.setMaxWidth(100);
        sectionTypeComboBox.setMinWidth(100);
        tmpPane.addColumn(0, sectionTypeComboBox);

        ComboBox<KeyValuePair> specialInstrumentationComboBox = new ComboBox<>(TeamF.client.helper.gui.InstrumentationHelper.getInstrumentTypes((SectionGroupType) sectionTypeComboBox.getSelectionModel().getSelectedItem().getValue()));
        specialInstrumentationComboBox.getSelectionModel().selectFirst();
        specialInstrumentationComboBox.setMaxWidth(100);
        specialInstrumentationComboBox.setMinWidth(100);
        tmpPane.addColumn(1, specialInstrumentationComboBox);

        NumberField tmpNumberField = null;
        try {
            tmpNumberField = new NumberField(specialInstrumentationCount, specialInstrumentationNumberField.getMinValue().intValue(), specialInstrumentationNumberField.getMaxValue().intValue());
            tmpPane.addColumn(2, tmpNumberField);
            tmpNumberField.setMaxWidth(60);
            tmpNumberField.setStyle("-fx-opacity: 1");
        } catch (NumberRangeException e) {
        }

        sectionTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if(((TeamF.client.helper.gui.InstrumentationHelper.getInstrumentTypes((SectionGroupType) sectionTypeComboBox.getSelectionModel().
                        getSelectedItem().getValue())))!=null) {
                    specialInstrumentationComboBox.setItems((TeamF.client.helper.gui.InstrumentationHelper.getInstrumentTypes((SectionGroupType) sectionTypeComboBox.getSelectionModel().
                            getSelectedItem().getValue())));
                    specialInstrumentationComboBox.getSelectionModel().selectFirst();
                }
            }
        });

        Button tmpButton = new Button("-");
        tmpPane.addColumn(3, tmpButton);

        specialInstrumentationContent.addRow(specialInstrumentationEntityList.size()+1, tmpPane);
        specialInstrumentationContent.setColumnSpan(tmpPane, 4);
        SpecialInstrumentationEntity specialInstrumentationEntity = new SpecialInstrumentationEntity(id, sectionTypeComboBox, specialInstrumentationComboBox, tmpNumberField, tmpPane);

        tmpButton.setOnAction(e -> removeSpecialInstrumentationItem(specialInstrumentationEntity, specialInstrumentationContent, specialInstrumentationEntityList));

        specialInstrumentationEntityList.add(specialInstrumentationEntity);
    }

    protected static void removeSpecialInstrumentationItem(SpecialInstrumentationEntity specialInstrumentationEntity, Pane specialInstrumentationContent, List<SpecialInstrumentationEntity> specialInstrumentationEntityList) {
        specialInstrumentationContent.getChildren().remove(specialInstrumentationEntity.getPane());
        specialInstrumentationEntityList.remove(specialInstrumentationEntity);
    }

    public static ObservableList<KeyValuePair> getSectionGroupTypeList() {
        ObservableList<KeyValuePair> list = FXCollections.observableArrayList(
                new KeyValuePair("String", SectionGroupType.STRING),
                new KeyValuePair("Woodwind", SectionGroupType.WOODWIND),
                new KeyValuePair("Brass", SectionGroupType.BRASS),
                new KeyValuePair("Percussion", SectionGroupType.PERCUSSION));

        return list;
    }

    public static ObservableList<KeyValuePair> getInstrumentTypes(SectionGroupType sectionGroupType) {
        ObservableList<KeyValuePair> list = FXCollections.observableArrayList();

        switch (sectionGroupType) {
            case STRING:
                break;
            case BRASS:
                list.addAll(
                        new KeyValuePair("French Horn", InstrumentType.FRENCHHORN),
                        new KeyValuePair("Bass Trombone", InstrumentType.BASSTROMBONE),
                        new KeyValuePair("Contrabass Trombone", InstrumentType.CONTRABASSTROMBONE),
                        new KeyValuePair("Euphonium", InstrumentType.EUPHONIUM),
                        new KeyValuePair("Wagner Tuba", InstrumentType.WAGNERTUBA),
                        new KeyValuePair("Cimbasso", InstrumentType.CIMBASSO)
                );

                break;
            case WOODWIND:
                list.addAll(
                        new KeyValuePair("English Horn", InstrumentType.ENGLISHHORN),
                        new KeyValuePair("Basset Horn", InstrumentType.BASSETHORN),
                        new KeyValuePair("Piccolo", InstrumentType.PICCOLO),
                        new KeyValuePair("Bass Clarinet", InstrumentType.BASSCLARINET),
                        new KeyValuePair("Heckelphone", InstrumentType.HECKELPHONE),
                        new KeyValuePair("Contrabassoon", InstrumentType.CONTRABASSOON),
                        new KeyValuePair("Saxophone", InstrumentType.SAXOPHONE)
                );

                break;
            case PERCUSSION:
                list.addAll(
                        new KeyValuePair("Piano", InstrumentType.PIANO),
                        new KeyValuePair("Celesta", InstrumentType.CELESTA),
                        new KeyValuePair("Organ", InstrumentType.ORGAN),
                        new KeyValuePair("Cembalo", InstrumentType.CEMBALO),
                        new KeyValuePair("Keyboard", InstrumentType.KEYBOARD),
                        new KeyValuePair("Accordeon", InstrumentType.ACCORDEON),
                        new KeyValuePair("Bandoneon", InstrumentType.BANDONEON),
                        new KeyValuePair("Guitar", InstrumentType.GUITAR),
                        new KeyValuePair("Mandolin", InstrumentType.MANDOLIN)
                );

                break;
        }

        return list;
    }


    //TODO: bring lists in order
    //Same order likeGetSectionTypeList, do not change!
    public static LinkedList<List> getSectionInstrumentPos() {
        List<InstrumentType> string= Arrays.asList();
        List<InstrumentType> woodwind= Arrays.asList(InstrumentType.ENGLISHHORN, InstrumentType.BASSETHORN, InstrumentType.PICCOLO, InstrumentType.BASSCLARINET, InstrumentType.HECKELPHONE, InstrumentType.CONTRABASSOON, InstrumentType.SAXOPHONE);
        List<InstrumentType> brass= Arrays.asList(InstrumentType.FRENCHHORN, InstrumentType.BASSTROMBONE, InstrumentType.CONTRABASSTROMBONE, InstrumentType.EUPHONIUM, InstrumentType.WAGNERTUBA, InstrumentType.CIMBASSO);
        List<InstrumentType> percussion= Arrays.asList(InstrumentType.PIANO, InstrumentType.CELESTA, InstrumentType.ORGAN, InstrumentType.CEMBALO, InstrumentType.KEYBOARD, InstrumentType.ACCORDEON, InstrumentType.BANDONEON, InstrumentType.GUITAR, InstrumentType.MANDOLIN);

        LinkedList<List> sections = new LinkedList<>();
        sections.add(string);
        sections.add(woodwind);
        sections.add(brass);
        sections.add(percussion);

        return sections;

    }
    public static int[] getInstrumentsPos(String instrumentType) {
        List sections = getSectionInstrumentPos();
        int sectionPos = 0;
        int instrumentPos = 0;
        for (int i = 0; i < sections.size(); i++) {
            List instruments = (List) sections.get(i);
            for (int j = 0; j < instruments.size(); j++) {
                if (String.valueOf(instruments.get(j)).toLowerCase().toString().equals(instrumentType.toLowerCase().toString())) {
                    sectionPos = i;
                    instrumentPos = j;
                }
            }
        }
        return new int[]{sectionPos, instrumentPos};
    }
}
