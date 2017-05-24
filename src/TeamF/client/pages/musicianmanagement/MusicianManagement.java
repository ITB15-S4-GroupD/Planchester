package TeamF.client.pages.musicianmanagement;

import TeamF.Application.PersonApplication;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import TeamF.client.converter.PersonConverter;
import TeamF.client.entities.KeyValuePair;
import TeamF.client.helper.ErrorMessageHelper;
import TeamF.client.pages.BaseTablePage;
import TeamF.jsonconnector.entities.*;
import TeamF.jsonconnector.entities.Error;
import TeamF.jsonconnector.entities.special.errorlist.PersonErrorList;
import TeamF.jsonconnector.enums.*;
import TeamF.jsonconnector.enums.InstrumentType;
import TeamF.jsonconnector.enums.properties.AccountProperty;
import TeamF.jsonconnector.enums.properties.PersonProperty;
import TeamF.jsonconnector.interfaces.JSONObjectEntity;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MusicianManagement extends BaseTablePage<PersonErrorList, Person, Person, PersonReturnValue, PersonParameter> {
    private TextField _firstNameField;
    private TextField _lastNameField;
    private TextField _addressField;
    private TextField _emailField;
    private TextField _phoneField;
    private TextField _usernameField;

    private TableView<Person> _musicianTable;

    private ComboBox<KeyValuePair> _comboBoxSectionType;
    private ComboBox<KeyValuePair> _comboBoxRole;
    private ComboBox<KeyValuePair> _comboBoxGender;
    private ComboBox<KeyValuePair> _comboBoxAccountRole;

    private final ObservableList<KeyValuePair> _personRoleList = MusicianTableHelper.getPersonRoleList();
    private final ObservableList<KeyValuePair> _accountRoleList = MusicianTableHelper.getAccountRoleList();
    private final ObservableList<KeyValuePair> _genderList = MusicianTableHelper.getGenderList();
    private final ObservableList<KeyValuePair> _sectionTypeList = MusicianTableHelper.getSectionTypeList();

    private Button _addButton;
    private Button _updateButton;
    private Button _editButton;
    private Button _deleteButton;
    private Button _cancelButton;

    private List<TextField> _fieldsList;
    private List<ComboBox> _comboboxList;

    private ObservableList<Person> _masterData = FXCollections.observableArrayList();
    private ObservableList<Person> _filteredData = FXCollections.observableArrayList();
    private TextField _filterField;

    private ScrollPane _insrumentPane;
    private GridPane _instrumentContent;
    private ComboBox<KeyValuePair> _comboBoxInstrumentType;
    private Button _addAnotherInstrumentButton;
    private List<MusicianInstrumentEntity> _playedInstrumentsList;


    @Override
    public void initialize() {
        if (_initialize != null) {
            _initialize.doAction(null);
        }
        final URL Style = ClassLoader.getSystemResource("TeamF/style/stylesheetMusicianManagement.css");
        getStylesheets().add(Style.toString());

        //textfields
        _firstNameField = new TextField();
        _lastNameField = new TextField();
        _addressField = new TextField();
        _emailField = new TextField();
        _phoneField = new TextField();
        _usernameField = new TextField();
        _playedInstrumentsList = new LinkedList();

        _fieldsList = new ArrayList(){{
            add(_firstNameField);
            add(_lastNameField);
            add(_addressField);
            add(_emailField);
            add(_phoneField);
            add(_usernameField);
        }};

        //listener for marking inputfields
        addListener();

        //comboboxes
        _comboBoxSectionType = new ComboBox<>(_sectionTypeList);
        _comboBoxInstrumentType = new ComboBox<>();
        _comboBoxInstrumentType.setMaxWidth(130);
        _comboBoxInstrumentType.setMinWidth(130);
        _comboBoxRole = new ComboBox<>(_personRoleList);
        _comboBoxGender = new ComboBox<>(_genderList);
        _comboBoxAccountRole = new ComboBox<>(_accountRoleList);
        _comboBoxInstrumentType.setItems(MusicianTableHelper.getInstrumentTypeList((SectionType) _comboBoxSectionType.getItems().get(0).getValue()));

        _comboboxList = new ArrayList(){{
            add(_comboBoxSectionType);
            add(_comboBoxAccountRole);
            add(_comboBoxGender);
            add(_comboBoxInstrumentType);
            add(_comboBoxRole);
        }};

        _comboBoxSectionType.getSelectionModel().selectFirst();
        _comboBoxInstrumentType.getSelectionModel().selectFirst();
        _comboBoxGender.getSelectionModel().selectFirst();
        _comboBoxAccountRole.getSelectionModel().selectFirst();
        _comboBoxRole.getSelectionModel().selectFirst();


        _musicianTable = new TableView<>();
        _musicianTable.setEditable(false);
        _musicianTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _musicianTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        _musicianTable.getColumns().addListener((ListChangeListener) change -> {
            change.next();
            if (change.wasReplaced()) {
                update();
            }
        });

        _musicianTable.getItems().addListener((ListChangeListener<Person>) change -> updateFilteredData());
        _musicianTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
        _musicianTable.setItems(_filteredData);
        _masterData.addListener((ListChangeListener<Person>) change -> updateFilteredData());

        _musicianTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                _editButton.setDisable(false);
                _deleteButton.setDisable(false);
            } else {
                _editButton.setDisable(true);
                _deleteButton.setDisable(true);
            }
        });

        _comboBoxSectionType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                _comboBoxInstrumentType.setItems(MusicianTableHelper.getInstrumentTypeList((SectionType) newValue.getValue()));
                    _comboBoxInstrumentType.getSelectionModel().selectFirst();
                    clearInstrumentsComboboxes();

            }
        });

        //if Role equals External-Musician-->Account fields are disabled
        _comboBoxRole.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.getValue().equals(PersonRole.External_musician)) {
                    _usernameField.setDisable(true);
                    _comboBoxAccountRole.setDisable(true);
                    _usernameField.clear();
                    _usernameField.setStyle("-fx-border-color: transparent");
                } else {
                    _usernameField.setDisable(false);
                    _comboBoxAccountRole.setDisable(false);
                }
                if (newValue.getValue().equals(PersonRole.Manager) || newValue.getValue().equals(PersonRole.Music_librarian)
                        || newValue.getValue().equals(PersonRole.Orchestral_facility_manager)) {
                    _instrumentContent.setDisable(true);
                    _comboBoxSectionType.setDisable(true);
                } else {
                    _instrumentContent.setDisable(false);
                    _comboBoxSectionType.setDisable(false);
                }
            }

        });

        _editButton = new Button("Edit");
        _editButton.setDisable(true);
        _editButton.setMinWidth(125);
        _editButton.setOnAction(e -> {
            clearInstrumentsComboboxes();
            _musicianTable.setDisable(true);
            _addButton.setVisible(false);
            _updateButton.setVisible(true);
            _editButton.setDisable(true);
            _deleteButton.setDisable(true);
            _cancelButton.setText("Cancel");
            _filterField.setDisable(true);
            fillFields(_musicianTable.getSelectionModel().getSelectedItem());
        });

        _deleteButton = new Button("Delete");
        _deleteButton.setDisable(true);
        _deleteButton.setMinWidth(125);
        _deleteButton.setOnAction(e -> removePerson());

        _filterField = new TextField();
        _filterField.setPromptText("First- or Lastname");
        _filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.trim().isEmpty()) {
                updateFilteredData();
            }else{
                updateFilteredData();
            }
        });

        HBox buttonsBox = new HBox(_editButton, _deleteButton,new Label("Search:"), _filterField);
        buttonsBox.setSpacing(10);
        VBox root = new VBox();
        GridPane newDataPane = getNewPersonDataPane();
        root.getChildren().addAll(newDataPane, _musicianTable, buttonsBox);
        root.setSpacing(5);
        BorderPane borderPane = new BorderPane();
        borderPane.setId("borderPane");
        borderPane.setCenter(root);
        setCenter(borderPane);
    }

    public GridPane getNewPersonDataPane() {

        Label titleMusician = new Label("Add Employee");
        titleMusician.setId("titleMusician");
        titleMusician.setMinWidth(200);

        GridPane pane = new GridPane();
        pane.gridLinesVisibleProperty().set(false);
        pane.getColumnConstraints().addAll(new ColumnConstraints(160), new ColumnConstraints(160),
                new ColumnConstraints(160), new ColumnConstraints(160), new ColumnConstraints(160));
        pane.setHgap(15);
        pane.setVgap(10);

        pane.addRow(0,titleMusician);
        pane.add(new Label("Orchestra Role:*"), 0, 1);
        pane.add(_comboBoxRole, 0, 2);
        pane.add(new Label("Section:*"), 1, 1);
        pane.add(_comboBoxSectionType, 1, 2);

        pane.add(new Label("Sex:*"), 0, 3);
        pane.add(_comboBoxGender, 0, 4);
        pane.add(new Label("First Name:*"), 1, 3);
        pane.add(_firstNameField, 1, 4);
        pane.add(new Label("Last Name:*"), 2, 3);
        pane.add(_lastNameField, 2, 4);

        pane.add(new Label("Street:*"), 0, 5);
        pane.add(_addressField, 0, 6);
        pane.add(new Label("Phone Number:*"), 1, 5);
        pane.add(_phoneField, 1, 6);
        pane.add(new Label("Email:*"), 2, 5);
        pane.add(_emailField, 2, 6);

        pane.add(new Label("Username:*"), 3, 3);
        pane.add(_usernameField, 3, 4);
        pane.add(new Label("Account Role:*"), 3, 5);
        pane.add(_comboBoxAccountRole, 3, 6);

        pane.addRow(7, new Label(" "));
        pane.addRow(8, new Label(" "));

        _updateButton = new Button("Update");
        _updateButton.setMinWidth(100);
        _updateButton.setVisible(false);
        _updateButton.setOnAction(e -> {
            _musicianTable.setDisable(false);
            editPerson();
        });

        _addButton = new Button("Add");
        _addButton.setMinWidth(100);
        _addButton.setVisible(true);
        _addButton.setOnAction(e -> {
            ArrayList<TextField> fields = new ArrayList<>();
            fields.add(_firstNameField);
            fields.add(_lastNameField);
            fields.add(_addressField);
            fields.add(_emailField);
            fields.add(_phoneField);

            if (_firstNameField.getText().trim().isEmpty() || _lastNameField.getText().trim().isEmpty() || _addressField.getText().trim().isEmpty()
                    || _emailField.getText().trim().isEmpty() || _phoneField.getText().trim().isEmpty()|| ((!_usernameField.isDisable())&&_usernameField.getText().trim().isEmpty())) {
                if ((!_usernameField.isDisable())) {
                    fields.add(_usernameField);
                }
                validate(fields);
                showValuesMissingMessage();
            } else {
                addPerson();
            }
        });

        _cancelButton = new Button("Clear");
        _cancelButton.setMinWidth(100);
        _cancelButton.setOnAction(e -> {
            _musicianTable.setDisable(false);
            reset();
        });

        pane.add(_addButton, 4, 7);
        pane.add(_updateButton, 4, 7);
        pane.add(_cancelButton, 0, 7);
        Label labelRequired = new Label("*...Required Fields");
        labelRequired.setMinWidth(100);
        pane.add(labelRequired, 0, 8);


        _instrumentContent = new GridPane();
        _instrumentContent.setMaxHeight(140);
        _instrumentContent.setMaxWidth(180);
        _instrumentContent.add( _comboBoxInstrumentType,0,2);
         _addAnotherInstrumentButton = new Button("+");

        MusicianInstrumentEntity instrumentationEntityDefault=new MusicianInstrumentEntity(0,_comboBoxInstrumentType, _instrumentContent);
        _playedInstrumentsList.add(instrumentationEntityDefault);
        _addAnotherInstrumentButton.setOnAction(event ->{
                if(_comboBoxInstrumentType.getItems().size()<=1){
                    return;
                }
                addInstruments(0, _comboBoxInstrumentType.getSelectionModel().getSelectedItem());});

        _instrumentContent.add(_addAnotherInstrumentButton,2,2 );
        _instrumentContent.add(new Label("Instruments:*"),0,0);
        _insrumentPane = new ScrollPane(_instrumentContent);
        _insrumentPane.setMaxHeight(140);
        _insrumentPane.setMaxWidth(180);

        pane.add(_insrumentPane, 4, 1);
        pane.setRowSpan(_insrumentPane, 6);
        pane.setColumnSpan(_insrumentPane, 4);

        return pane;
    }

    private void addInstruments(int id, KeyValuePair sectionType) {
        GridPane tmpPane = new GridPane();


        ComboBox<KeyValuePair> tmpComboBox = new ComboBox<>();
        tmpComboBox.setItems(MusicianTableHelper.getInstrumentTypeList((SectionType) _comboBoxSectionType.getItems().get(_comboBoxSectionType.getSelectionModel().getSelectedIndex()).getValue()));
        tmpComboBox.getSelectionModel().selectFirst();
        tmpComboBox.setMaxWidth(130);
        tmpComboBox.setMinWidth(130);
        tmpPane.addColumn(0, tmpComboBox);

        Button tmpButton = new Button("-");
        tmpPane.addColumn(3, tmpButton);
        int i=_playedInstrumentsList.size()+1;
        _instrumentContent.addRow(i+1, tmpPane);
        _instrumentContent.setColumnSpan(tmpPane, 4);
        MusicianInstrumentEntity specialInstrumentationEntity = new MusicianInstrumentEntity(id, tmpComboBox, tmpPane);

        tmpButton.setOnAction(e -> removeInstrumentsComboBoxes(specialInstrumentationEntity));

        _playedInstrumentsList.add(specialInstrumentationEntity);
    }

    private void removeInstrumentsComboBoxes(MusicianInstrumentEntity specialInstrumentationEntity) {
        _instrumentContent.getChildren().remove(specialInstrumentationEntity.getPane());
        _playedInstrumentsList.remove(specialInstrumentationEntity);
    }

    public void addPerson() {
        Person person = new Person();
        if(_comboBoxRole.getSelectionModel().getSelectedItem().getValue().equals(PersonRole.External_musician)) {
            setPerson(person, false);
        }else {
            setPerson(person, true);
        }

        PersonApplication personApplication = new PersonApplication();
        personApplication.add(person);

        load();
    }

    public void editPerson() {
        Person person = _musicianTable.getSelectionModel().getSelectedItem();
        setPerson(person, false);

        PersonApplication personApplication = new PersonApplication();
        personApplication.edit(person);

        load();
    }

    public void removePerson() {
        Person person = _musicianTable.getSelectionModel().getSelectedItem();
        setPerson(person, false);

        PersonApplication personApplication = new PersonApplication();
        personApplication.remove(person);

        load();
    }

    private void setPerson(Person person, boolean createAccount) {
        person.setFirstname(_firstNameField.getText());
        person.setLastname(_lastNameField.getText());
        person.setAddress(_addressField.getText());
        person.setEmail(_emailField.getText());
        person.setPhoneNumber(_phoneField.getText());
        person.setGender((Gender) _comboBoxGender.getSelectionModel().getSelectedItem().getValue());

        List<InstrumentType> instrumentsList = new LinkedList<>();
        if (!_comboBoxInstrumentType.isDisable()) {
            for(MusicianInstrumentEntity item : _playedInstrumentsList) {
                if(!instrumentsList.contains(item.getSectionTypeComboBox().getSelectionModel().getSelectedItem().getValue())){
                instrumentsList.add((InstrumentType)item.getSectionTypeComboBox().getSelectionModel().getSelectedItem().getValue());
                }
            }
            person.setInstrumentTypeList(instrumentsList);
            }

        person.setPersonRole((PersonRole) _comboBoxRole.getSelectionModel().getSelectedItem().getValue());

        if (!_comboBoxAccountRole.isDisable() && !_usernameField.isDisable()) {
            if (createAccount) {
                Account account = new Account();
                person.setAccount(account);
            }
            if (person.getAccount() != null) {
                person.getAccount().setUsername(_usernameField.getText());
                person.getAccount().setRole((AccountRole) _comboBoxAccountRole.getSelectionModel().getSelectedItem().getValue());
            }
        }
    }

    @Override
    public void load() {
        PersonApplication personApplication = new PersonApplication();
        List<Person> personList = personApplication.getAllMusicians();

        if (personList != null) {
            //_musicianTable.setItems(FXCollections.observableList(personList));
            _masterData.setAll(personList);
            if(_filterField!=null){
                _filterField.clear();
            }
            update();
        } else {
            showTryAgainLaterErrorMessage();
        }
    }

    @Override
    public void update() {
        _musicianTable.getColumns().clear();
        _musicianTable.getColumns().addAll(MusicianTableHelper.getIdColumn(), MusicianTableHelper.getFirstNameColumn(),
                MusicianTableHelper.getLastNameColumn(), MusicianTableHelper.getStreetColumn(),
                MusicianTableHelper.getZipCodeColumn(), MusicianTableHelper.getPhonenumberColumn(),
                MusicianTableHelper.getRoleColumn(), MusicianTableHelper.getInstrumentColumn());
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

    public void fillFields(Person person) {
        if (person.getFirstname() != null) {
            _firstNameField.setText(person.getFirstname());
        }
        if (person.getLastname() != null) {
            _lastNameField.setText(person.getLastname());
        }
        if (person.getAddress() != null) {
            _addressField.setText(person.getAddress());
        }
        if (person.getEmail() != null) {
            _emailField.setText(person.getEmail());
        }
        if (person.getPhoneNumber() != null) {
            _phoneField.setText(person.getPhoneNumber());
        }

        if (person.getAccount() != null && person.getAccount().getUsername() != null) {
            _usernameField.setText(person.getAccount().getUsername().toString());
        }

        if (person.getAccount() != null && person.getAccount().getRole() != null) {
            _comboBoxAccountRole.getSelectionModel().select(MusicianTableHelper.getAccountPos(person.getAccount().getRole()));
        }

        if (person.getPersonRole() != null) {
            if (MusicianTableHelper.getRolePos(person.getPersonRole()) >= 0) {
                _comboBoxRole.getSelectionModel().select(MusicianTableHelper.getRolePos(person.getPersonRole()));
                if (person.getPersonRole().equals(PersonRole.External_musician)) {
                    _comboBoxAccountRole.setDisable(true);
                }
                if (person.getPersonRole().equals(PersonRole.Manager) || person.getPersonRole().equals(PersonRole.Orchestral_facility_manager) || person.getPersonRole().equals(PersonRole.Music_librarian)) {
                    _instrumentContent.setDisable(true);
                    _comboBoxSectionType.setDisable(true);
                } else {
                    _instrumentContent.setDisable(false);
                    _comboBoxSectionType.setDisable(false);
                }
            }
        }

        if (person.getInstrumentTypeList() != null) {
            int[] positions = MusicianTableHelper.getFirstInstrumentPos(person.getInstrumentTypeList().get(0));
            if (positions[0] >= 0 && (positions[1] >= 0)) {
                _comboBoxSectionType.getSelectionModel().select(positions[0]);
                _comboBoxInstrumentType.getSelectionModel().select(positions[1]);
            }
            List<InstrumentType> instruments = person.getInstrumentTypeList();
            for (int i = 1; i < instruments.size(); i++) {
                int[] positions2 = MusicianTableHelper.getFirstInstrumentPos(person.getInstrumentTypeList().get(i));
                addInstruments(0, _comboBoxSectionType.getSelectionModel().getSelectedItem());
                _playedInstrumentsList.get(i).getSectionTypeComboBox().getSelectionModel().select(positions2[1]);
            }

        }

        if (person.getGender() != null) {
            if (MusicianTableHelper.getGenderPos(person.getGender()) >= 0) {
                _comboBoxGender.getSelectionModel().select(MusicianTableHelper.getGenderPos(person.getGender()));
            }
        }

        if (person.getAccount() != null && person.getAccount().getRole() != null) {
            if (MusicianTableHelper.getAccountPos(person.getAccount().getRole()) >= 0) {
                _comboBoxAccountRole.getSelectionModel().select(MusicianTableHelper.getAccountPos(person.getAccount().getRole()));
            }
        }

        _usernameField.setDisable(true);
    }

    private void clearInstrumentsComboboxes() {
        for(MusicianInstrumentEntity item : _playedInstrumentsList) {
            _instrumentContent.getChildren().remove(item.getPane());
        }
        _playedInstrumentsList.clear();
        _comboBoxInstrumentType.setItems(MusicianTableHelper.getInstrumentTypeList((SectionType) _comboBoxSectionType.getItems().get(_comboBoxSectionType.getSelectionModel().getSelectedIndex()).getValue()));
        _comboBoxInstrumentType.getSelectionModel().selectFirst();
        MusicianInstrumentEntity instrumentationEntityDefault=new MusicianInstrumentEntity(0,_comboBoxInstrumentType, _instrumentContent);
        _playedInstrumentsList.add(instrumentationEntityDefault);
    }

    private void markInvalidFields(List<Pair<JSONObjectEntity, List<Error>>> occuredErrors) {
        setBorder();
        String error;
        List<Error> errorList=occuredErrors.get(0).getValue();
        for(int x=0;x<errorList.size();x++) {
            error = errorList.get(x).getKey().toString();
            if (error.equals(AccountProperty.USERNAME.toString())) {
                _usernameField.setStyle("-fx-border-color: red");
            }
            if (error.equals(AccountProperty.ACCOUNT_ROLE.toString())) {
                _comboBoxAccountRole.setStyle("-fx-border-color: red");
            }
            if (error.equals(PersonProperty.FIRSTNAME.toString())) {
                _firstNameField.setStyle("-fx-border-color: red");
            }
            if (error.equals(PersonProperty.LASTNAME.toString())) {
                _lastNameField.setStyle("-fx-border-color: red");
            }
            if (error.equals(PersonProperty.ADDRESS.toString())) {
                _addressField.setStyle("-fx-border-color: red");
            }
            if (error.equals(PersonProperty.PHONE_NUMBER.toString())) {
                _phoneField.setStyle("-fx-border-color: red");
            }
            if (error.equals(PersonProperty.EMAIL.toString())) {
                _emailField.setStyle("-fx-border-color: red");
            }
            if (error.equals(PersonProperty.GENDER.toString())) {
                _comboBoxGender.setStyle("-fx-border-color: red");
            }
            if (error.equals(PersonProperty.PERSON_ROLE.toString())) {
                _comboBoxRole.setStyle("-fx-border-color: red");
            }
        }
    }

    private void setBorder() {
        for (TextField field : _fieldsList) {
            if(!field.isDisable()){
                field.setStyle("-fx-border-color: green");
            }
        }
        for (ComboBox comboBox : _comboboxList) {
            if(!comboBox.isDisable()){
                comboBox.setStyle("-fx-border-color: green");
            }
        }
    }

    private void reset() {
        for(TextField field:_fieldsList){
            field.clear();
            field.setStyle("-fx-border-color: transparent");
            field.setDisable(false);
        }
        for(ComboBox comboBox:_comboboxList){
            comboBox.setStyle("-fx-border-color: transparent");
            comboBox.getSelectionModel().selectFirst();
            comboBox.setDisable(false);
        }
        _addButton.setVisible(true);
        _editButton.setDisable(true);
        _deleteButton.setDisable(true);
        _updateButton.setVisible(false);
        _cancelButton.setText("Clear");
        _filterField.setDisable(false);
        _musicianTable.getSelectionModel().clearSelection();
        _instrumentContent.setDisable(false);

        for(MusicianInstrumentEntity item : _playedInstrumentsList) {
            _instrumentContent.getChildren().remove(item.getPane());
        }
        _playedInstrumentsList.clear();
        _comboBoxInstrumentType.setItems(MusicianTableHelper.getInstrumentTypeList((SectionType) _comboBoxSectionType.getItems().get(_comboBoxSectionType.getSelectionModel().getSelectedIndex()).getValue()));
        _comboBoxInstrumentType.getSelectionModel().selectFirst();
        MusicianInstrumentEntity instrumentationEntityDefault=new MusicianInstrumentEntity(0,_comboBoxInstrumentType, _instrumentContent);
        _playedInstrumentsList.add(instrumentationEntityDefault);

    }

    private void addListener() {
        for (TextField field : _fieldsList) {
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

    //Search Filter methods
    private void updateFilteredData() {
        _filteredData.clear();

        for (Person p : _masterData) {
            if (matchesFilter(p)) {
                _filteredData.add(p);
            }
        }
        _musicianTable.setItems(_filteredData);
        reapplyTableSortOrder();
    }


    private boolean matchesFilter(Person person) {
        String filterString = _filterField.getText();
        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
            return true;
        }

        String lowerCaseFilterString = filterString.toLowerCase();

        if (person.getFirstname().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (person.getLastname().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        }

        return false;
    }

    private void reapplyTableSortOrder() {
        ArrayList<TableColumn<Person, ?>> sortOrder = new ArrayList<>(_musicianTable.getSortOrder());
        _musicianTable.getSortOrder().clear();
        _musicianTable.getSortOrder().addAll(sortOrder);
    }
}
