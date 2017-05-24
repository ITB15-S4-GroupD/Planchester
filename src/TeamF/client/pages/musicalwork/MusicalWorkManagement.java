package TeamF.client.pages.musicalwork;

import TeamF.Application.MusicalWorkApplication;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import jfxtras.labs.scene.control.BigDecimalField;
import TeamF.client.controls.numberfield.NumberField;
import TeamF.client.converter.MusicalWorkConverter;
import TeamF.client.entities.KeyValuePair;
import TeamF.client.exceptions.NumberRangeException;
import TeamF.client.helper.ErrorMessageHelper;
import TeamF.client.helper.gui.InstrumentationHelper;
import TeamF.client.helper.gui.SpecialInstrumentationEntity;
import TeamF.client.pages.BaseTablePage;
import TeamF.jsonconnector.entities.*;
import TeamF.jsonconnector.entities.Error;
import TeamF.jsonconnector.entities.special.errorlist.MusicalWorkErrorList;
import TeamF.jsonconnector.enums.SectionGroupType;
import TeamF.jsonconnector.enums.properties.MusicalWorkProperty;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MusicalWorkManagement extends BaseTablePage<MusicalWorkErrorList, MusicalWork, MusicalWork, MusicalWork, MusicalWorkParameter> {
    private TextField _nameField;
    private TextField _composerField;
    private TableView<MusicalWork> _workTable;
    private List<SpecialInstrumentationEntity> _specialInstrumentationEntityList;

    //String
    private NumberField _firstViolinField;
    private NumberField _secondViolinField;
    private NumberField _violaField;
    private NumberField _violoncelloField;
    private NumberField _doublebassField;

    //Wood
    private NumberField _fluteField;
    private NumberField _oboeField;
    private NumberField _clarinetField;
    private NumberField _bassoonField;

    //Brass
    private NumberField _hornField;
    private NumberField _trumpetField;
    private NumberField _tromboneField;
    private NumberField _tubeField;

    //Percussion
    private NumberField _kettledrumField;
    private NumberField _percussionField;
    private NumberField _harpField;
    private HBox _textfields;
    private List<BigDecimalField> _decimalFields;
    private List<TextField> _textFieldsList;

    //SpecialInstrumentation
    private ScrollPane _specialInstrumentationPane;
    private GridPane _specialInstrumentationContent;
    private ComboBox<KeyValuePair> _specialInstrumentationSectionGroupComboBox;
    private ComboBox<KeyValuePair> _specialInstrumentationInstrumentTypeComboBox;
    private NumberField _specialInstrumentationNumberField;
    private Button _specialInstrumentationButton;

    private Button _addButton;
    private Button _updateButton;
    private Button _editButton;
    private Button _deleteButton;
    private Button _cancelButton;

    private ObservableList<MusicalWork> _masterData = FXCollections.observableArrayList();
    private ObservableList<MusicalWork> _filteredData = FXCollections.observableArrayList();
    private TextField _filterField;


    public MusicalWorkManagement() {
    }

    @Override
    public void initialize() {
        if (_initialize != null) {
            _initialize.doAction(null);
        }
        final URL Style = ClassLoader.getSystemResource("TeamF/style/stylesheetMusicalWork.css");
        getStylesheets().add(Style.toString());
        _decimalFields = new ArrayList<>();
        _nameField = new TextField();
        _nameField.setMinWidth(200);
        _composerField = new TextField();
        _composerField.setMinWidth(200);
        _textFieldsList=new ArrayList(){{
            add(_nameField);
            add(_composerField);
        }};
        addListener();
        _specialInstrumentationEntityList = new LinkedList();

        _decimalFields = new ArrayList<>();
        try {
            _firstViolinField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_firstViolinField);
            _secondViolinField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_secondViolinField);
            _violaField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_violaField);
            _violoncelloField = new NumberField(0, 0, Integer.MAX_VALUE);;
            _decimalFields.add(_violoncelloField);
            _doublebassField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_doublebassField);

            _fluteField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_fluteField);
            _oboeField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_oboeField);
            _clarinetField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_clarinetField);
            _bassoonField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_bassoonField);

            _hornField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_hornField);
            _trumpetField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_trumpetField);
            _tromboneField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_tromboneField);
            _tubeField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_tubeField);

            _kettledrumField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_kettledrumField);
            _percussionField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_percussionField);
            _harpField = new NumberField(0, 0, Integer.MAX_VALUE);
            _decimalFields.add(_harpField);
        } catch (NumberRangeException e) {
        }

        setNumberfieldWidth();

        _workTable = new TableView<>();
        _workTable.setEditable(false);
        _workTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _workTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        _workTable.getColumns().addListener((ListChangeListener) change -> {
            change.next();
            if (change.wasReplaced()) {
                update();
            }
        });

        _workTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                _editButton.setDisable(false);
                _deleteButton.setDisable(false);
            } else {
                _editButton.setDisable(true);
                _deleteButton.setDisable(true);
            }
        });

        update();

        _filteredData.addAll(_masterData);
        _workTable.setItems(_filteredData);
        _masterData.addListener((ListChangeListener<MusicalWork>) change -> updateFilteredData());

        _workTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                _editButton.setDisable(false);
                _deleteButton.setDisable(false);
            } else {
                _editButton.setDisable(true);
                _deleteButton.setDisable(true);
            }
        });

        Pane newDataPane = getNewMusicalWorkDataPane();

        _deleteButton = new Button("Delete");
        _deleteButton.setDisable(true);
        _deleteButton.setMinWidth(125);
        _deleteButton.setOnAction(e -> deleteMusicalWork());

        _filterField = new TextField();
        _filterField.setPromptText("Name- or Composer");
        _filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty()) {
                updateFilteredData();
            }else{
                updateFilteredData();
            }
        });

        HBox buttonsBox = new HBox(_editButton, _deleteButton, new Label("Search:"),_filterField);
        buttonsBox.setSpacing(10);

        VBox root = new VBox();
        root.getChildren().addAll(newDataPane, _workTable, buttonsBox);
        root.setSpacing(5);
        BorderPane borderPane = new BorderPane();
        borderPane.setId("borderPane");

        borderPane.setCenter(root);
        setCenter(borderPane);
    }

    private Pane getNewMusicalWorkDataPane() {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);

        pane.getColumnConstraints().addAll(new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90),
                new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(120));

        Label titleMusicalWork = new Label("Add Musical Work");
        titleMusicalWork.setId("titleMusicalWork");
        titleMusicalWork.setMinWidth(200);

        _textfields = new HBox();
        _textfields.setMinWidth(600);
        _textfields.setSpacing(10);
        Label nameLabel = new Label("Name:*");
        nameLabel.setMinWidth(60);
        Label composerLabel = new Label("Composer:*");
        composerLabel.setMinWidth(60);
        _textfields.getChildren().addAll(nameLabel, _nameField, composerLabel, _composerField);
        pane.addRow(0, titleMusicalWork);
        pane.addRow(1, _textfields);

        //Strings
        pane.addRow(4, new Label("1. Violin:"), _firstViolinField);
        pane.addRow(5, new Label("2. Violin:"), _secondViolinField);
        pane.addRow(6, new Label("Viola:"), _violaField);
        pane.addRow(7, new Label("Violoncello:"), _violoncelloField);
        pane.addRow(8, new Label("Doublebass:"), _doublebassField);

        //Wood
        pane.addRow(4, new Label("Flute:"), _fluteField);
        pane.addRow(5, new Label("Oboe:"), _oboeField);
        pane.addRow(6, new Label("Clarinet:"), _clarinetField);
        pane.addRow(7, new Label("Bassoon:"), _bassoonField);

        //Brass
        pane.addRow(4, new Label("Horn:"), _hornField);
        pane.addRow(5, new Label("Trumpet:"), _trumpetField);
        pane.addRow(6, new Label("Trombone:"), _tromboneField);
        pane.addRow(7, new Label("Tube:"), _tubeField);

        //Percussion
        pane.addRow(4, new Label("Kettledrum:"), _kettledrumField);
        pane.addRow(5, new Label("Percussion:"), _percussionField);
        pane.addRow(6, new Label("Harp:"), _harpField);


        _addButton = new Button("Add");
        _addButton.setVisible(true);
        _addButton.setMinWidth(100);
        _addButton.setOnAction(e -> {
            if (_nameField.getText().isEmpty() || _composerField.getText().isEmpty() ||
                    _firstViolinField.getNumber().toPlainString().isEmpty() || _secondViolinField.getNumber().toPlainString().isEmpty() || _violaField.getNumber().toPlainString().isEmpty() || _violoncelloField.getNumber().toPlainString().isEmpty() ||
                    _doublebassField.getNumber().toPlainString().isEmpty() || _fluteField.getNumber().toPlainString().isEmpty() || _oboeField.getNumber().toPlainString().isEmpty() || _clarinetField.getNumber().toPlainString().isEmpty() ||
                    _bassoonField.getNumber().toPlainString().isEmpty() || _hornField.getNumber().toPlainString().isEmpty() || _trumpetField.getNumber().toPlainString().isEmpty() || _tromboneField.getNumber().toPlainString().isEmpty() || _tubeField.getNumber().toPlainString().isEmpty()
                    || _kettledrumField.getNumber().toPlainString().isEmpty() || _percussionField.getNumber().toPlainString().isEmpty() || _harpField.getNumber().toPlainString().isEmpty()) {
                validate(_textFieldsList);
                validate(_decimalFields);
                showValuesMissingMessage();
            } else {
                addMusicalWork();
            }
        });

        _editButton = new Button("Edit");
        _editButton.setDisable(true);
        _editButton.setMinWidth(125);
        _editButton.setOnAction(e -> {
            clearInstrumentsComboboxes();
            _workTable.setDisable(true);
            _addButton.setVisible(false);
            _updateButton.setVisible(true);
            _editButton.setDisable(true);
            _deleteButton.setDisable(true);
            _cancelButton.setText("Cancel");
            _filterField.setDisable(true);
            fillFields(_workTable.getSelectionModel().getSelectedItem());
        });

        _updateButton = new Button("Update");
        _updateButton.setMinWidth(100);
        _updateButton.setVisible(false);
        _updateButton.setOnAction(e -> {
            _workTable.setDisable(false);
            editWork();
        });

        _cancelButton = new Button("Clear");
        _cancelButton.setMinWidth(100);
        _cancelButton.setOnAction(e -> {
            _workTable.setDisable(false);
            reset();
        });

        pane.add(new Label("Instrumentation:"), 0, 2);
        pane.add(new Label("String:"), 0, 3);
        pane.add(new Label("Wood:"), 2, 3);
        pane.add(new Label("Brass:"), 4, 3);
        pane.add(new Label("Percussion:"), 6, 3);
        pane.add(new Label("Special Instruments:"), 8, 2);
        pane.add(_addButton, 8, 9);
        pane.add(_updateButton, 8, 9);
        pane.add(_cancelButton, 0, 9);
        Label labelRequired = new Label("*...Required Fields");
        labelRequired.setMinWidth(100);
        pane.add(labelRequired, 0, 10);

        _specialInstrumentationContent = new GridPane();
        _specialInstrumentationSectionGroupComboBox = new ComboBox<>(InstrumentationHelper.getSectionGroupTypeList());
        _specialInstrumentationSectionGroupComboBox.setMaxWidth(100);
        _specialInstrumentationSectionGroupComboBox.setMinWidth(100);
        _specialInstrumentationSectionGroupComboBox.getSelectionModel().selectFirst();
        _specialInstrumentationContent.addColumn(0, _specialInstrumentationSectionGroupComboBox);
        _specialInstrumentationInstrumentTypeComboBox = new ComboBox<>(InstrumentationHelper.getInstrumentTypes((SectionGroupType) _specialInstrumentationSectionGroupComboBox.getSelectionModel().getSelectedItem().getValue()));
        _specialInstrumentationInstrumentTypeComboBox.getSelectionModel().selectFirst();
        _specialInstrumentationInstrumentTypeComboBox.setMaxWidth(100);
        _specialInstrumentationInstrumentTypeComboBox.setMinWidth(100);
        _specialInstrumentationContent.addColumn(1, _specialInstrumentationInstrumentTypeComboBox);

        try {
            _specialInstrumentationNumberField = new NumberField(0, 0, Integer.MAX_VALUE);
        } catch (NumberRangeException e) {
        }

        _specialInstrumentationNumberField.setMaxWidth(60);
        _specialInstrumentationContent.addColumn(2, _specialInstrumentationNumberField);
        _specialInstrumentationButton = new Button("+");
        SpecialInstrumentationEntity instrumentationEntityDefault=new SpecialInstrumentationEntity(0, _specialInstrumentationSectionGroupComboBox, _specialInstrumentationInstrumentTypeComboBox,_specialInstrumentationNumberField,_specialInstrumentationContent);
        _specialInstrumentationEntityList.add(instrumentationEntityDefault);

        _specialInstrumentationContent.addColumn(3, _specialInstrumentationButton);
        _specialInstrumentationPane = new ScrollPane(_specialInstrumentationContent);
        _specialInstrumentationPane.setMaxHeight(250);
        _specialInstrumentationPane.setMinWidth(300);

        pane.add(_specialInstrumentationPane, 8, 3);
        pane.setRowSpan(_specialInstrumentationPane, 6);
        pane.setColumnSpan(_specialInstrumentationPane, 4);

        InstrumentationHelper.addListeners(_specialInstrumentationEntityList, _specialInstrumentationContent, _specialInstrumentationSectionGroupComboBox,
                _specialInstrumentationInstrumentTypeComboBox, _specialInstrumentationNumberField, _specialInstrumentationButton);

        return pane;
    }

    public void addMusicalWork() {
        MusicalWork musicalWork = new MusicalWork();
        setMusicalWork(musicalWork, true);

        MusicalWorkApplication musicalWorkApplication = new MusicalWorkApplication();
        musicalWorkApplication.addMusicalWork(musicalWork);

        load();
    }

    public void editWork() {
        MusicalWork musicalWork = _workTable.getSelectionModel().getSelectedItem();
        setMusicalWork(musicalWork, false);

        MusicalWorkApplication musicalWorkApplication = new MusicalWorkApplication();
        musicalWorkApplication.updateMusicalWork(musicalWork);

        load();
    }

    public void deleteMusicalWork() {
        MusicalWork musicalWork = _workTable.getSelectionModel().getSelectedItem();

        MusicalWorkApplication musicalWorkApplication = new MusicalWorkApplication();
        musicalWorkApplication.deleteMusicalWork(musicalWork);

        load();
    }

    private void setMusicalWork(MusicalWork musicalWork, boolean createInstrumentation) {
        musicalWork.setName(_nameField.getText());
        musicalWork.setComposer(_composerField.getText());

        if(createInstrumentation) {
            Instrumentation instrumentation = new Instrumentation();
            musicalWork.setInstrumentation(instrumentation);
        }

        musicalWork.getInstrumentation().setViolin1(_firstViolinField.getNumber().intValue());
        musicalWork.getInstrumentation().setViolin2(_secondViolinField.getNumber().intValue());
        musicalWork.getInstrumentation().setViola(_violaField.getNumber().intValue());
        musicalWork.getInstrumentation().setViolincello(_violoncelloField.getNumber().intValue());
        musicalWork.getInstrumentation().setDoublebass(_doublebassField.getNumber().intValue());

        musicalWork.getInstrumentation().setFlute(_fluteField.getNumber().intValue());
        musicalWork.getInstrumentation().setOboe(_oboeField.getNumber().intValue());
        musicalWork.getInstrumentation().setClarinet(_clarinetField.getNumber().intValue());
        musicalWork.getInstrumentation().setBassoon(_bassoonField.getNumber().intValue());

        musicalWork.getInstrumentation().setHorn(_hornField.getNumber().intValue());
        musicalWork.getInstrumentation().setTrumpet(_trumpetField.getNumber().intValue());
        musicalWork.getInstrumentation().setTrombone(_tromboneField.getNumber().intValue());
        musicalWork.getInstrumentation().setTube(_tubeField.getNumber().intValue());

        musicalWork.getInstrumentation().setKettledrum(_kettledrumField.getNumber().intValue());
        musicalWork.getInstrumentation().setPercussion(_percussionField.getNumber().intValue());
        musicalWork.getInstrumentation().setHarp(_harpField.getNumber().intValue());

        List<SpecialInstrumentation> specialInstrumentationList = TeamF.client.helper.gui.InstrumentationHelper.getSpecialInstrumentation(_specialInstrumentationEntityList);
        if(specialInstrumentationList.size()>0) {
            musicalWork.getInstrumentation().setSpecialInstrumentation(specialInstrumentationList);
        }
    }

    @Override
    public void load() {
        MusicalWorkApplication musicalWorkApplication = new MusicalWorkApplication();
        List<MusicalWork> musicalWorkList = musicalWorkApplication.getMusicalWorkList();

        if (musicalWorkList != null) {
            _masterData.setAll(musicalWorkList);
            update();
        } else {
            showTryAgainLaterErrorMessage();
        }
    }

    @Override
    public void update() {
        _workTable.getColumns().clear();
        _workTable.getColumns().addAll(MusicalWorkHelper.getIdColumn(), MusicalWorkHelper.getMusicalWorkNameColumn(),
                MusicalWorkHelper.getComposerColumn(), MusicalWorkHelper.getInstrumentationColumn());
    }

    @Override
    public void exit() {
        if (_exit != null) {
            _exit.doAction(null);
        }
    }

    @Override
    public void dispose() {
    }

    public void fillFields(MusicalWork musicalWork) {
        if (musicalWork.getComposer() != null) {
            _composerField.setText(musicalWork.getComposer());
        }
        if (musicalWork.getName() != null) {
            _nameField.setText(musicalWork.getName());
        }
        if (musicalWork.getInstrumentation() != null) {
            Instrumentation instrumentation = musicalWork.getInstrumentation();
            _firstViolinField.setNumber(new BigDecimal(instrumentation.getViolin1()));
            _secondViolinField.setNumber(new BigDecimal(instrumentation.getViolin2()));
            _violaField.setNumber(new BigDecimal(instrumentation.getViola()));
            _violoncelloField.setNumber(new BigDecimal(instrumentation.getViolincello()));
            _doublebassField.setNumber(new BigDecimal(instrumentation.getDoublebass()));

            _fluteField.setNumber(new BigDecimal(instrumentation.getFlute()));
            _oboeField.setNumber(new BigDecimal(instrumentation.getOboe()));
            _clarinetField.setNumber(new BigDecimal(instrumentation.getClarinet()));
            _bassoonField.setNumber(new BigDecimal(instrumentation.getBassoon()));

            _hornField.setNumber(new BigDecimal(instrumentation.getHorn()));
            _trumpetField.setNumber(new BigDecimal(instrumentation.getTrumpet()));
            _tromboneField.setNumber(new BigDecimal(instrumentation.getTrombone()));
            _tubeField.setNumber(new BigDecimal(instrumentation.getTube()));

            _kettledrumField.setNumber(new BigDecimal(instrumentation.getKettledrum()));
            _percussionField.setNumber(new BigDecimal(instrumentation.getPercussion()));
            _harpField.setNumber(new BigDecimal(instrumentation.getHarp()));

            if (instrumentation.getSpecialInstrumentation() != null) {
                int[] positions = TeamF.client.helper.gui.InstrumentationHelper.getInstrumentsPos(instrumentation.getSpecialInstrumentation().get(0).getSpecialInstrumentation().toString());
                if (positions[0] >= 0 && (positions[1] >= 0)) {
                    _specialInstrumentationSectionGroupComboBox.getSelectionModel().select(positions[0]);
                    _specialInstrumentationInstrumentTypeComboBox.getSelectionModel().select(positions[1]);
                }
                List<SpecialInstrumentation> instruments = instrumentation.getSpecialInstrumentation();
                for (int i = 1; i < instruments.size(); i++) {
                    int[] positions2 = TeamF.client.helper.gui.InstrumentationHelper.getInstrumentsPos(instrumentation.getSpecialInstrumentation().get(i).getSpecialInstrumentation());
                    TeamF.client.helper.gui.InstrumentationHelper.addSpecialInstrumentationItem(0, _specialInstrumentationSectionGroupComboBox.getSelectionModel().getSelectedItem(),_specialInstrumentationInstrumentTypeComboBox.getSelectionModel().getSelectedItem(),instruments.get(i).getSpecialInstrumentCount(), _specialInstrumentationEntityList, _specialInstrumentationContent, _specialInstrumentationSectionGroupComboBox, _specialInstrumentationNumberField);
                    _specialInstrumentationEntityList.get(i).getSectionTypeComboBox().getSelectionModel().select(positions2[0]);
                    _specialInstrumentationEntityList.get(i).getSpecialInstrumentationComboBox().getSelectionModel().select(positions2[1]);
                }

            }

        }
    }

    private void reset() {
        for (TextField field : _textFieldsList) {
            field.clear();
            field.setStyle("-fx-border-color: transparent");
        }

        _workTable.getSelectionModel().clearSelection();
        _addButton.setVisible(true);
        _editButton.setDisable(true);
        _deleteButton.setDisable(true);
        _updateButton.setVisible(false);
        _cancelButton.setText("Clear");
        _filterField.setDisable(false);

        for (BigDecimalField field : _decimalFields) {
            field.setNumber(new BigDecimal(0));
            field.setStyle("-fx-border-color: transparent");
        }

        InstrumentationHelper.resetSpecialInstrumentation(_specialInstrumentationEntityList, _specialInstrumentationContent);
        _specialInstrumentationEntityList.clear();
        SpecialInstrumentationEntity instrumentationEntityDefault = new SpecialInstrumentationEntity(0, _specialInstrumentationSectionGroupComboBox, _specialInstrumentationInstrumentTypeComboBox,_specialInstrumentationNumberField,_specialInstrumentationContent);
        _specialInstrumentationEntityList.add(instrumentationEntityDefault);
    }

    private void markInvalidFields(List<Pair<JSONObjectEntity, List<Error>>> occuredErrors) {
        setBorder();
        String error;
        List<Error> errorList=occuredErrors.get(0).getValue();
        for(int x=0;x<errorList.size();x++) {
            error = errorList.get(x).getKey().toString();
            if (error.equals(MusicalWorkProperty.CONDUCTOR.toString())) {
                _composerField.setStyle("-fx-border-color: red");
            }
            if (error.equals(MusicalWorkProperty.TITLE.toString())) {
                _nameField.setStyle("-fx-border-color: red");
            }
            if (error.equals(MusicalWorkProperty.INSTRUMENTATION.toString())) {
                for (BigDecimalField field : _decimalFields) {
                    field.setStyle("-fx-border-color: red");
                }
            }
        }
    }

    private void clearInstrumentsComboboxes() {
        for(SpecialInstrumentationEntity item : _specialInstrumentationEntityList) {
            _specialInstrumentationContent.getChildren().remove(item.getPane());
        }

        InstrumentationHelper.resetSpecialInstrumentation(_specialInstrumentationEntityList, _specialInstrumentationContent);
        _specialInstrumentationEntityList.clear();
        SpecialInstrumentationEntity instrumentationEntityDefault = new SpecialInstrumentationEntity(0, _specialInstrumentationSectionGroupComboBox, _specialInstrumentationInstrumentTypeComboBox,_specialInstrumentationNumberField,_specialInstrumentationContent);
        _specialInstrumentationEntityList.add(instrumentationEntityDefault);
    }

    private void setBorder() {
        for (TextField field : _textFieldsList) {
            field.setStyle("-fx-border-color: green");
        }
    }

    private void setNumberfieldWidth(){
        for(BigDecimalField field: _decimalFields){
            field.setMaxWidth(60);
        }
    }

    private void addListener() {
        for (TextField field : _textFieldsList) {
            field.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (field.getText().trim().isEmpty()) {
                    field.setStyle("-fx-border-color: red");
                } else {
                    field.setStyle("-fx-border-color: green");
                }
            });

            field.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                if (newValue1.trim().isEmpty()) {
                    field.setStyle("-fx-border-color: red");
                } else {
                    field.setStyle("-fx-border-color: green");
                }
            });
        }
    }

    private void updateFilteredData() {
        _filteredData.clear();

        for (MusicalWork m : _masterData) {
            if (matchesFilter(m)) {
                _filteredData.add(m);
            }
        }
        _workTable.setItems(_filteredData);
        reapplyTableSortOrder();
    }

    private boolean matchesFilter(MusicalWork musicalWork) {
        String filterString = _filterField.getText();
        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
            return true;
        }

        String lowerCaseFilterString = filterString.toLowerCase();

        if (musicalWork.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (musicalWork.getComposer().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        }

        return false;
    }

    private void reapplyTableSortOrder() {
        ArrayList<TableColumn<MusicalWork, ?>> sortOrder = new ArrayList<>(_workTable.getSortOrder());
        _workTable.getSortOrder().clear();
        _workTable.getSortOrder().addAll(sortOrder);
    }
}
