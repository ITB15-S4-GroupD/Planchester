package TeamF.client.pages.instrumentationmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import jfxtras.labs.scene.control.BigDecimalField;
import TeamF.client.controls.numberfield.NumberField;
import TeamF.client.converter.InstrumentationConverter;
import TeamF.client.entities.KeyValuePair;
import TeamF.client.exceptions.NumberRangeException;
import TeamF.client.helper.ErrorMessageHelper;
import TeamF.client.pages.BaseTablePage;
import TeamF.client.helper.gui.SpecialInstrumentationEntity;
import TeamF.jsonconnector.entities.*;
import TeamF.jsonconnector.entities.Error;
import TeamF.jsonconnector.entities.special.errorlist.InstrumentationErrorList;
import TeamF.jsonconnector.enums.*;
import TeamF.jsonconnector.enums.errors.InstrumentationError;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class InstrumentationManagement extends BaseTablePage<InstrumentationErrorList, Instrumentation, Instrumentation, Instrumentation, InstrumentationParameter> {
    private TableView<Instrumentation> _instrumentationTable;
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

    private GridPane _newDataPane;
    private List<BigDecimalField> _decimalFields;

    public InstrumentationManagement() {

    }

    @Override
    public void initialize() {
        if (_initialize != null) {
            _initialize.doAction(null);
        }
        final URL Style = ClassLoader.getSystemResource("TeamF/style/stylesheetInstrumentation.css");
        getStylesheets().add(Style.toString());
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

        _instrumentationTable = new TableView<>();
        _instrumentationTable.setEditable(false);
        _instrumentationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _instrumentationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        _instrumentationTable.getColumns().addListener((ListChangeListener) change -> {
            change.next();
            if (change.wasReplaced()) {
                update();
            }
        });

        _instrumentationTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                _editButton.setDisable(false);
                _deleteButton.setDisable(false);
            } else {
                _editButton.setDisable(true);
                _deleteButton.setDisable(true);
            }
        });

        _newDataPane = getNewInstrumentationDataPane();
        HBox buttonsBox = new HBox(_editButton, _deleteButton);
        buttonsBox.setSpacing(10);


        VBox root = new VBox();
        root.getChildren().addAll(_newDataPane, _instrumentationTable, buttonsBox);
        root.setSpacing(5);
        BorderPane borderPane = new BorderPane();
        borderPane.setId("borderPane");
        borderPane.setCenter(root);
        setCenter(borderPane);
    }

    private GridPane getNewInstrumentationDataPane() {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);

        pane.getColumnConstraints().addAll(new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90),
                new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(120));

        Label titleInstrumentation = new Label("Add Instrumentation");
        titleInstrumentation.setId("titleInstrumentation");
        titleInstrumentation.setMinWidth(200);

        pane.addRow(0, titleInstrumentation);

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
        _addButton.setMinWidth(100);
        _addButton.setVisible(true);

        _addButton.setOnAction(e -> {
            if (_firstViolinField.getNumber().toPlainString().isEmpty() || _secondViolinField.getNumber().toPlainString().isEmpty() || _violaField.getNumber().toPlainString().isEmpty() || _violoncelloField.getNumber().toPlainString().isEmpty() ||
                    _doublebassField.getNumber().toPlainString().isEmpty() || _fluteField.getNumber().toPlainString().isEmpty() || _oboeField.getNumber().toPlainString().isEmpty() || _clarinetField.getNumber().toPlainString().isEmpty() ||
                    _bassoonField.getNumber().toPlainString().isEmpty() || _hornField.getNumber().toPlainString().isEmpty() || _trumpetField.getNumber().toPlainString().isEmpty() || _tromboneField.getNumber().toPlainString().isEmpty() || _tubeField.getNumber().toPlainString().isEmpty()
                    || _kettledrumField.getNumber().toPlainString().isEmpty() || _percussionField.getNumber().toPlainString().isEmpty() || _harpField.getNumber().toPlainString().isEmpty()) {
                validate(_decimalFields);
                showValuesMissingMessage();
            } else {
                addInstrumentation();
            }
        });

        _editButton = new Button("Edit");
        _editButton.setDisable(true);
        _editButton.setMinWidth(125);
        _editButton.setOnAction(e -> {
            clearInstrumentsComboboxes();
            _instrumentationTable.setDisable(true);
            _addButton.setVisible(false);
            _updateButton.setVisible(true);
            _editButton.setDisable(true);
            _deleteButton.setDisable(true);
            _cancelButton.setText("Cancel");
             fillFields(_instrumentationTable.getSelectionModel().getSelectedItem());
        });

        _deleteButton = new Button("Delete");
        _deleteButton.setDisable(true);
        _deleteButton.setMinWidth(125);
        _deleteButton.setOnAction(e -> deleteInstrumentation());

        _updateButton = new Button("Update");
        _updateButton.setMinWidth(100);
        _updateButton.setVisible(false);
        _updateButton.setOnAction(e -> {
            _instrumentationTable.setDisable(false);
            editInstrumentation();
            reset();
        });

        _cancelButton = new Button("Clear");
        _cancelButton.setMinWidth(100);
        _cancelButton.setOnAction(e -> {
            _instrumentationTable.setDisable(false);
            reset();
        });

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
        _specialInstrumentationSectionGroupComboBox = new ComboBox<>(TeamF.client.helper.gui.InstrumentationHelper.getSectionGroupTypeList());
        _specialInstrumentationSectionGroupComboBox.setMaxWidth(100);
        _specialInstrumentationSectionGroupComboBox.setMinWidth(100);
        _specialInstrumentationSectionGroupComboBox.getSelectionModel().selectFirst();
        _specialInstrumentationContent.addColumn(0, _specialInstrumentationSectionGroupComboBox);
        _specialInstrumentationInstrumentTypeComboBox = new ComboBox<>(TeamF.client.helper.gui.InstrumentationHelper.getInstrumentTypes((SectionGroupType) _specialInstrumentationSectionGroupComboBox.getSelectionModel().getSelectedItem().getValue()));
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
        SpecialInstrumentationEntity instrumentationEntityDefault = new SpecialInstrumentationEntity(0, _specialInstrumentationSectionGroupComboBox, _specialInstrumentationInstrumentTypeComboBox,_specialInstrumentationNumberField,_specialInstrumentationContent);
        _specialInstrumentationEntityList.add(instrumentationEntityDefault);

        _specialInstrumentationContent.addColumn(3, _specialInstrumentationButton);
        _specialInstrumentationPane = new ScrollPane(_specialInstrumentationContent);
        _specialInstrumentationPane.setMaxHeight(250);
        _specialInstrumentationPane.setMinWidth(300);

        pane.add(_specialInstrumentationPane, 8, 3);
        pane.setRowSpan(_specialInstrumentationPane, 6);
        pane.setColumnSpan(_specialInstrumentationPane, 4);

        TeamF.client.helper.gui.InstrumentationHelper.addListeners(_specialInstrumentationEntityList, _specialInstrumentationContent, _specialInstrumentationSectionGroupComboBox,
                _specialInstrumentationInstrumentTypeComboBox, _specialInstrumentationNumberField, _specialInstrumentationButton);

        return pane;
    }

    public void addInstrumentation() {
        if (_create != null) {
            Instrumentation instrumentation = new Instrumentation();
            setInstrumentation(instrumentation);

            InstrumentationErrorList resultInstrumentationErrorList = _create.doAction(instrumentation);

            if (resultInstrumentationErrorList != null && resultInstrumentationErrorList.getKeyValueList() != null) {
                List<Pair<JSONObjectEntity, List<Error>>> errorList = InstrumentationConverter.getAbstractList(resultInstrumentationErrorList.getKeyValueList());
                String tmpErrorText = ErrorMessageHelper.getErrorMessage(errorList);

                if (tmpErrorText.isEmpty() && resultInstrumentationErrorList.getKeyValueList().size() == 1 && resultInstrumentationErrorList.getKeyValueList().get(0).getKey() != null && resultInstrumentationErrorList.getKeyValueList().get(0).getKey().getInstrumentationID() > 0) {
                    showSuccessMessage("Successful", tmpErrorText);

                    _instrumentationTable.getItems().add(resultInstrumentationErrorList.getKeyValueList().get(0).getKey());
                    update();
                    reset();
                } else {
                    showErrorMessage("Error", tmpErrorText);
                    markInvalidFields(errorList);
                }
            } else {
                showTryAgainLaterErrorMessage();
            }
        }
    }

    public void editInstrumentation() {
        if (_edit != null) {
            Instrumentation instrumentation = _instrumentationTable.getSelectionModel().getSelectedItem();
            setInstrumentation(instrumentation);

            InstrumentationErrorList resultInstrumentationErrorList = _edit.doAction(instrumentation);

            if (resultInstrumentationErrorList != null && resultInstrumentationErrorList.getKeyValueList() != null) {
                List<Pair<JSONObjectEntity, List<Error>>> errorList = InstrumentationConverter.getAbstractList(resultInstrumentationErrorList.getKeyValueList());
                String tmpErrorText = ErrorMessageHelper.getErrorMessage(errorList);

                if (tmpErrorText.isEmpty() && resultInstrumentationErrorList.getKeyValueList().size() == 1) {
                    showSuccessMessage("Successful", tmpErrorText);

                    _instrumentationTable.getItems().remove(instrumentation);
                    _instrumentationTable.getItems().add(resultInstrumentationErrorList.getKeyValueList().get(0).getKey());
                    update();
                    reset();
                } else {
                    showErrorMessage("Error", tmpErrorText);
                    markInvalidFields(errorList);
                }
            } else {
                showTryAgainLaterErrorMessage();
            }
        }
    }

    public void deleteInstrumentation() {
        if (_delete != null) {
            Instrumentation instrumentation = new Instrumentation();
            InstrumentationErrorList resultInstrumentationErrorList = _create.doAction(instrumentation);

            if (resultInstrumentationErrorList != null && resultInstrumentationErrorList.getKeyValueList() != null) {
                List<Pair<JSONObjectEntity, List<Error>>> errorList = InstrumentationConverter.getAbstractList(resultInstrumentationErrorList.getKeyValueList());
                String tmpErrorText = ErrorMessageHelper.getErrorMessage(errorList);

                if (tmpErrorText.isEmpty() && resultInstrumentationErrorList.getKeyValueList().size() == 1 && resultInstrumentationErrorList.getKeyValueList().get(0).getKey() != null && resultInstrumentationErrorList.getKeyValueList().get(0).getKey().getInstrumentationID() > 0) {
                    showSuccessMessage("Successful", tmpErrorText);

                    _instrumentationTable.getItems().remove(resultInstrumentationErrorList.getKeyValueList().get(0).getKey());
                    update();
                } else {
                    showErrorMessage("Error", tmpErrorText);
                }
            } else {
                showTryAgainLaterErrorMessage();
            }
        }
    }

    private void setInstrumentation(Instrumentation instrumentation) {
        instrumentation.setViolin1(_firstViolinField.getNumber().intValue());
        instrumentation.setViolin2(_secondViolinField.getNumber().intValue());
        instrumentation.setViola(_violaField.getNumber().intValue());
        instrumentation.setViolincello(_violoncelloField.getNumber().intValue());
        instrumentation.setDoublebass(_doublebassField.getNumber().intValue());

        instrumentation.setFlute(_fluteField.getNumber().intValue());
        instrumentation.setOboe(_oboeField.getNumber().intValue());
        instrumentation.setClarinet(_clarinetField.getNumber().intValue());
        instrumentation.setBassoon(_bassoonField.getNumber().intValue());

        instrumentation.setHorn(_hornField.getNumber().intValue());
        instrumentation.setTrumpet(_trumpetField.getNumber().intValue());
        instrumentation.setTrombone(_tromboneField.getNumber().intValue());
        instrumentation.setTube(_tubeField.getNumber().intValue());

        instrumentation.setKettledrum(_kettledrumField.getNumber().intValue());
        instrumentation.setPercussion(_percussionField.getNumber().intValue());
        instrumentation.setHarp(_harpField.getNumber().intValue());

        List<SpecialInstrumentation> specialInstrumentationList = TeamF.client.helper.gui.InstrumentationHelper.getSpecialInstrumentation(_specialInstrumentationEntityList);
        if(specialInstrumentationList.size()>0) {
            instrumentation.setSpecialInstrumentation(specialInstrumentationList);
        }
    }

    private void loadList() {
        if (_loadList != null) {
            InstrumentationParameter instrumentationParameter = new InstrumentationParameter();
            List<Instrumentation> instrumentationList = _loadList.doAction(instrumentationParameter);

            if (instrumentationList != null) {
                _instrumentationTable.setItems(FXCollections.observableList(instrumentationList));
                update();
            } else {
                showTryAgainLaterErrorMessage();
            }
        }
    }

    @Override
    public void load() {
        if (_load != null) {
        }
        loadList();
    }

    @Override
    public void update() {
        _instrumentationTable.getColumns().clear();
        _instrumentationTable.getColumns().addAll(InstrumentationHelper.getIdColumn(), InstrumentationHelper.getInstrumentationColumn());
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

    public void fillFields(Instrumentation instrumentation) {
        if (instrumentation != null) {
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
        _instrumentationTable.getSelectionModel().clearSelection();
        _instrumentationTable.setDisable(false);
        _editButton.setDisable(true);
        _deleteButton.setDisable(true);
        _updateButton.setVisible(false);
        _addButton.setVisible(true);
        _cancelButton.setText("Clear");
        for (BigDecimalField field : _decimalFields) {
            field.setNumber(new BigDecimal(0));
            field.setStyle("-fx-border-color: transparent");
        }

        TeamF.client.helper.gui.InstrumentationHelper.resetSpecialInstrumentation(_specialInstrumentationEntityList, _specialInstrumentationContent);
        _specialInstrumentationEntityList.clear();
        SpecialInstrumentationEntity instrumentationEntityDefault = new SpecialInstrumentationEntity(0, _specialInstrumentationSectionGroupComboBox, _specialInstrumentationInstrumentTypeComboBox,_specialInstrumentationNumberField,_specialInstrumentationContent);
        _specialInstrumentationEntityList.add(instrumentationEntityDefault);
    }

    private void clearInstrumentsComboboxes() {
        for(SpecialInstrumentationEntity item : _specialInstrumentationEntityList) {
            _specialInstrumentationContent.getChildren().remove(item.getPane());
        }

        TeamF.client.helper.gui.InstrumentationHelper.resetSpecialInstrumentation(_specialInstrumentationEntityList, _specialInstrumentationContent);
        _specialInstrumentationEntityList.clear();
        SpecialInstrumentationEntity instrumentationEntityDefault = new SpecialInstrumentationEntity(0, _specialInstrumentationSectionGroupComboBox, _specialInstrumentationInstrumentTypeComboBox,_specialInstrumentationNumberField,_specialInstrumentationContent);
        _specialInstrumentationEntityList.add(instrumentationEntityDefault);
    }

    private void markInvalidFields(List<Pair<JSONObjectEntity, List<Error>>> occuredErrors) {
        //setBorder();
        String error;
        List<Error> errorList=occuredErrors.get(0).getValue();
        for(int x=0;x<errorList.size();x++) {
            error = errorList.get(x).getKey().toString();
            if (error.equals(InstrumentationError.ALLZERO.toString())) {
                for (BigDecimalField field : _decimalFields) {
                    field.setStyle("-fx-border-color: red");
                }
            }

        }
    }

    private void setBorder() {
        for (BigDecimalField field : _decimalFields) {
            field.setStyle("-fx-border-color: green");
        }
    }

    private void setNumberfieldWidth(){
        for(BigDecimalField field: _decimalFields){
            field.setMaxWidth(60);
        }
    }
}
