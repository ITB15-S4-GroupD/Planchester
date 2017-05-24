package TeamF.client.pages.musicalwork;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import TeamF.client.entities.KeyValuePair;
import TeamF.jsonconnector.entities.MusicalWork;
import TeamF.jsonconnector.enums.InstrumentType;
import TeamF.jsonconnector.enums.SectionGroupType;
import TeamF.jsonconnector.enums.SectionType;

public class MusicalWorkHelper {

    public static TableColumn<MusicalWork, Integer> getIdColumn() {
        TableColumn<MusicalWork, Integer> idCol = new TableColumn<>("Id");
        PropertyValueFactory<MusicalWork, Integer> idCellValueFactory = new PropertyValueFactory<>("musicalWorkID");
        idCol.setCellValueFactory(idCellValueFactory);
        return idCol;
    }

    public static TableColumn<MusicalWork, String> getMusicalWorkNameColumn() {
        TableColumn<MusicalWork, String> musicalWorkNameCol = new TableColumn<>("MusicalWork Name");
        PropertyValueFactory<MusicalWork, String> musicalWorkNameCellValueFactory = new PropertyValueFactory<>("name");
        musicalWorkNameCol.setCellValueFactory(musicalWorkNameCellValueFactory);
        return musicalWorkNameCol;
    }

    public static TableColumn<MusicalWork, String> getComposerColumn() {
        TableColumn<MusicalWork, String> composerCol = new TableColumn<>("Composer name");
        PropertyValueFactory<MusicalWork, String> composerCellValueFactory = new PropertyValueFactory<>("composer");
        composerCol.setCellValueFactory(composerCellValueFactory);
        return composerCol;
    }

    public static TableColumn<MusicalWork, String> getInstrumentationColumn() {
        TableColumn<MusicalWork, String> instrumentationCol = new TableColumn<>("Instrumentation");
        PropertyValueFactory<MusicalWork, String> instrumentationCellValueFactory = new PropertyValueFactory<>("instrumentation");
        instrumentationCol.setCellValueFactory(instrumentationCellValueFactory);
        return instrumentationCol;
    }
}

