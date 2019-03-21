package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;
import service.*;
import util.CSVUtil;
import util.UISetterUtil;
import util.ValidatorUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static util.ConstantsUtil.*;

/**
 * Controller class for ProfessorSection.fxml
 *
 * @author Sourav Debnath
 * @author Avik Sarkar
 */
public class ProfessorSectionController {

    /*------------------------------Declaration and initialization of variables---------------------------------------*/

    String courseId = "";

    /*------------------------------End of Declaration and initialization of variables--------------------------------*/


    /*-------------------------Declaration and initialization of variables of Professor List Tab----------------------*/
    ObservableList<SubjectAllocation> subAllocObsList;
    @FXML
    private TabPane professorSectionTabPane;
    private ProfessorService professorService;
    private DepartmentService departmentService;
    private List<Professor> listOfProfessors;
    private List<Department> listOfDepartment;
    private ObservableList<Professor> professorObsList;
    @FXML
    private Tab professorsListTab;
    @FXML
    private Label titleLabel;
    @FXML
    private Button importButton;
    @FXML
    private ComboBox<String> profDeptComboBox;
    @FXML
    private TableView<Professor> professorTableView;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableColumn<Professor, String> profIdCol;
    @FXML
    private TableColumn<Professor, String> firstNameCol;
    @FXML
    private TableColumn<Professor, String> middleNameCol;
    @FXML
    private TableColumn<Professor, String> lastNameCol;
    @FXML
    private TableColumn<Professor, String> academicRankCol;
    @FXML
    private TableColumn<Professor, String> deptCol;
    @FXML
    private TableColumn<Professor, String> hodStatusCol;
    @FXML
    private TableColumn<Professor, List<Subject>> subCol;


    /*-------------------End of Declaration and initialization of variables of Professor List Tab---------------------*/


    /*---------------------Declaration and initialization of variables of Subject Allocation Tab----------------------*/
    @FXML
    private TableColumn<Professor, String> emailCol;
    @FXML
    private TableColumn<Professor, String> contactNoCol;
    @FXML
    private Tab subjectAllocationTab;
    @FXML
    private GridPane importCsvMainGridPane;
    @FXML
    private StackPane importCsvStatusStackPane;
    @FXML
    private ImageView importCsvStatusImageView;
    @FXML
    private ProgressIndicator importCsvProgressIndicator;
    @FXML
    private Label importCsvStatusLabel;
    @FXML
    private Label chosenFileLabel;
    @FXML
    private Button chooseFileButton;
    @FXML
    private ComboBox<String> subCsvSubNameComboBox;
    @FXML
    private ComboBox<String> subCsvDegreeComboBox;
    @FXML
    private ComboBox<String> subCsvProfIdComboBox;
    @FXML
    private ComboBox<String> subCsvDisciplineComboBox;
    @FXML
    private ComboBox<String> subCsvSubIdComboBox;
    @FXML
    private TableView<SubjectAllocation> subAllocTableView;
    @FXML
    private TableColumn<SubjectAllocation, String> subAllocCourseIdCol;
    @FXML
    private TableColumn<SubjectAllocation, String> subAllocSubjectIdCol;
    @FXML
    private TableColumn<SubjectAllocation, String> subAllocProfIdCol;
    @FXML
    private ComboBox<String> subAllocFormDeptComboBox;
    @FXML
    private ComboBox<String> subAllocFormDegreeComboBox;
    @FXML
    private ComboBox<String> subAllocFormDisciplineComboBox;
    @FXML
    private ComboBox<String> subAllocFormSubjectIdComboBox;
    @FXML
    private ComboBox<String> subAllocFormProfIdComboBox;
    @FXML
    private Label subAllocFormProfNameLabel;
    @FXML
    private Button submitCsvButton;
    @FXML
    private GridPane subAllocFormMainGridPane;
    @FXML
    private StackPane subAllocFormStatusStackPane;
    @FXML
    private ProgressIndicator subAllocFormProgressIndicator;
    @FXML
    private Label subAllocFormStatusLabel;
    @FXML
    private ImageView subAllocFormStatusImageView;
    @FXML
    private Label subAllocFormMsgLabel;
    @FXML
    private GridPane subAllocListMainGridPane;
    @FXML
    private StackPane subAllocListStatusStackPane;
    @FXML
    private ProgressIndicator subAllocListProgressIndicator;
    @FXML
    private Label subAllocListStatusLabel;
    @FXML
    private ImageView subAllocListStatusImageView;
    @FXML
    private Label subAllocListMsgLabel;
    private SubjectService subjectService;
    private CourseService courseService;
    private FileHandlingService fileHandlingService;
    private File file;
    private List<Course> listOfCourses;
    private List<Professor> listOfProfInSubAllocForm;

    /*-----------------End of Declaration and initialization of variables of Subject Allocation Tab-------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @SuppressWarnings("Duplicates")
    @FXML

    public void initialize() {

        professorService = new ProfessorService();
        departmentService = new DepartmentService();
        courseService = new CourseService();
        subjectService = new SubjectService();
        fileHandlingService = new FileHandlingService();

        //initialize the professor list tab
        initProfessorsListTab();

        //initialize columns of the professorTableView
        initProfessorListCols();

        initSubAllocCols();
        //initialize the respective tabs if it is selected
        professorSectionTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {

            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {

                if (newValue == professorsListTab) {

                    initProfessorsListTab();
                } else if (newValue == subjectAllocationTab) {

                    initSubAllocationTab();
                }
            }
        });
    }

    /*---------------------------------------------Professor List Tab-------------------------------------------------*/

    /**
     * Callback method for deptComboBox.
     * Clears table items and populate the tableView.
     */
    @FXML
    private void handleProfDeptComboBox() {

        //clear the tableView data first
        professorObsList.clear();

        titleLabel.setText("List of " + profDeptComboBox.getValue() + " " + "professors");
        populateTable();
    }

    /**
     * Initialization of Professor List Tab
     */
    private void initProfessorsListTab() {

        //initialize this for the professorTableView
        professorObsList = FXCollections.observableArrayList();

        //get the list of departments available in the db
        Task<List<Department>> deptTask = departmentService
                .getDepartmentsTask("");
        new Thread(deptTask).start();

        deptTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //store the list of all departments available in the DB
                listOfDepartment = deptTask.getValue();

                //only if there's any department in the db
                if (!listOfDepartment.isEmpty()) {

                    List<String> items = new ArrayList<>();

                    for (Department dept : listOfDepartment) {

                        //add only unique department items to deptCombobox
                        if (!items.contains(dept.getDeptName()))
                            items.add(dept.getDeptName());
                    }

                    //choosing this will display professors from all department
                    items.add("all");

                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    profDeptComboBox.setItems(options);
                }
            }
        });
    }

    /**
     * This method initializes the columns of the Professor Table.
     */
    @SuppressWarnings("Duplicates")
    private void initProfessorListCols() {

        profIdCol.setCellValueFactory(new PropertyValueFactory<>("profId"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        academicRankCol.setCellValueFactory(new PropertyValueFactory<>("highestQualification"));
        deptCol.setCellValueFactory(new PropertyValueFactory<>("deptName"));
        hodStatusCol.setCellValueFactory(new PropertyValueFactory<>("hodStatus"));
        subCol.setCellValueFactory(new PropertyValueFactory<>("subjects"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        contactNoCol.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        subCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Professor, List<Subject>> call(TableColumn<Professor, List<Subject>> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(List<Subject> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.stream().map(Subject::getSubName)
                                    .collect(Collectors.joining("\n")));
                        }
                    }
                };
            }
        });
    }


    /**
     * This method populates and updates the Professor Table.
     */
    @SuppressWarnings("Duplicates")
    private void populateTable() {

        String additionalQuery = "";

        Task<List<Professor>> professorTask;

        /*
        If 'all' is chosen in the profDeptComboBox, then get all professors from the DB, otherwise get professors of the
        particular Department.
         */
        if (profDeptComboBox.getValue().equals("all")) {
            professorTask = professorService.getProfessorTask(additionalQuery);
        } else {
            additionalQuery = "where v_dept_name=?";
            professorTask = professorService.getProfessorTask(additionalQuery, profDeptComboBox.getValue());
        }


        new Thread(professorTask).start();

        professorTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //get list of professors
                listOfProfessors = professorTask.getValue();
                professorObsList.setAll(listOfProfessors);

                /*
                Wrap the Observable List in a FilteredList, for the search feature; null means no predicate i.e. always
                true, so display all professors in the TableView at first.
                 */
                FilteredList<Professor> professorFilteredItems = new FilteredList<>(professorObsList, null);

                //FilteredList is unmodifiable, so wrap it in a SortedList for sorting functionality in the TableView
                SortedList<Professor> professorSortedList = new SortedList<>(professorFilteredItems);

                /*
                Attach a listener to the Search field to display only those Students in the TableView that matches with the
                Search box data.
                 */
                searchTextField.textProperty().addListener(new ChangeListener<>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                        //set the predicate which will be used for filtering Professor
                        professorFilteredItems.setPredicate(new Predicate<>() {

                            @Override
                            public boolean test(Professor professor) {

                                String lowerCaseFilter = newValue.toLowerCase();

                                if (newValue.isEmpty())
                                    return true;
                                else if (professor.getFirstName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getMiddleName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getLastName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getProfId().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getDeptName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getContactNo().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getHighestQualification().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getEmail().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getHodStatus().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                return false;
                            }
                        });
                    }
                });

                //attach the comparator ,so that sorting can be done
                professorSortedList.comparatorProperty().bind(professorTableView.comparatorProperty());

                //set items in the TableView
                professorTableView.setItems(professorSortedList);
            }
        });
    }


    /**
     * Callback method for ImportButton to import the professors from the Csv file.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleImportButtonAction() throws IOException {

        //create a new stage first
        Stage importProfessorListModalWindow = new Stage();

        //get the stage where the Import Button is situated
        Stage parentStage = (Stage) importButton.getScene().getWindow();

        FXMLLoader loader = UISetterUtil.setModalWindow("/view/ImportProfessorCSVModal.fxml"
                , importProfessorListModalWindow, parentStage, "Import Professor List");

        //get the controller
        ImportProfessorCSVModalController importProfessorCSVModalController = loader.getController();

        /*
        showAndWait() since we need to get the status for updating the table from the Import Professor controller.
        The application thread is blocked here and goes to execute the Import modal operations and only when the Import
        modal window is closed , the tableUpdateStatus is sent from the Import modal controller.
         */
        importProfessorListModalWindow.showAndWait();

        //get the tableUpdateStatus
        boolean tableUpdateStatus = importProfessorCSVModalController.getTableUpdateStatus();

        /*
        If any Professor is imported from the CSV to DB and if a department is selected in the ComboBox then update the Table.
         */
        if (tableUpdateStatus && profDeptComboBox.getValue() != null) {

            populateTable();
        }
    }


    /**
     * Callback method to handle View Student button action.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleViewProfessorButtonAction() {

        //get the selected professor in the TableView
        Professor professor = professorTableView.getSelectionModel().getSelectedItem();

        //only if a Professor is selected in the TableView.
        if (professor != null) {

            //create a modal window
            Stage viewProfessorModalWindow = new Stage();

            //get the main stage
            Stage parentStage = (Stage) importButton.getScene().getWindow();

            //set the modal window
            FXMLLoader loader = UISetterUtil.setModalWindow("/view/ViewProfessorModal.fxml"
                    , viewProfessorModalWindow, parentStage, "Professor");

            //get the controller
            ViewProfessorModalController viewProfessorModalController = loader.getController();

            //send the Professor to the controller
            viewProfessorModalController.setProfessorPojo(professor);

            /*
            showAndWait() ensures that the data professorDeletedStatus is fetched after the modal window is closed.
            This method blocks the UI thread here.
             */
            viewProfessorModalWindow.showAndWait();

            //if a professor is deleted in the DB, remove the professor from the TableView
            if (viewProfessorModalController.getProfessorDeletedStatus()) {

                professorObsList.remove(professor);
            }
        }
    }


    /**
     * Callback method to handle ADD PROFESSOR Button action.
     *
     * @throws IOException Error while loading the FXML file.
     */
    @FXML
    private void handleAddProfButtonAction() throws IOException {

        //get the stackPane first in which the content is loaded
        StackPane contentStackPane = (StackPane) professorSectionTabPane.getParent();

        Parent professorRegistrationFxml = FXMLLoader.load(getClass()
                .getResource("/view/ProfessorRegistration.fxml"));

        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(professorRegistrationFxml);
    }

    /*---------------------------------------End of Professor List Tab------------------------------------------------*/


    /*----------------------------------------Subject Allocation Tab--------------------------------------------------*/

    /*------------------------------------------Import Subject Allocation CSV-----------------------------------------*/

    /**
     * Callback method for choosing a file from the directories.
     */
    @FXML
    private void handleChooseFileButtonAction() {

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());

        /*
        Only when a file is chosen, get the column names from the file and set ComboBoxes
        with the column names' list else unset ComboBoxes.
         */
        if (file != null) {

            chosenFileLabel.setText(file.getName());
            List<String> list = CSVUtil.getColumnNames(file);

            //checking if all 5 columns are present in the csv file uploaded
            if (list.size() == 5) {

                setComboBoxes(list);
                submitCsvButton.setDisable(false);
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("CSV file should have 5 columns!");
                alert.show();
                chosenFileLabel.setText("");
                submitCsvButton.setDisable(true);
                unSetComboBoxes();
            }
        }
    }

    /**
     * Callback method to handle SampleCsvHyperLinkAction.
     * <p>
     * Basically this method opens the sample CSV in the User's System with an appropriate application.
     */
    @FXML
    private void handleSampleCsvHyperlinkAction() {

        /*
        Couldn't find a way to open the CSV file from a relative file path.Also file opening doesn't work inside the JAR
        package,so I had to get the input stream of the sample csv , create a new file in the USER's system in the
        predefined location , copy the input stream to the new file.
        I wish I could have found a better way!
         */

        //location of the sample CSV
        String sampleCsvFilePath = "/csv/subjectAllocSample.csv";

        //location where the new csv will be created in the user's system
        String filePath = USER_HOME + FILE_SEPARATOR + ROOT_DIR + FILE_SEPARATOR
                + CSV_DIR + FILE_SEPARATOR + "subjectAllocSample.csv";
        try {

            //if subjectAllocSample.csv doesn't exist in the User's System , then only create it
            if (!Paths.get(filePath).toFile().exists()) {

                //get the content of the sampleCsv File as InputStream
                InputStream in = getClass().getResourceAsStream(sampleCsvFilePath);
                //create the new csv file in the user's system and copy the contents of sampleCsv file to it
                Task<Boolean> createAndWriteToFileTask = fileHandlingService.getCreateAndWriteToFileTask
                        (in.readAllBytes(), CSV_DIR, "subjectAllocSample.csv");
                new Thread(createAndWriteToFileTask).start();
            }

            //open the CSV file with the appropriate Application available in the user's system
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * Callback method to handle the Submit Button in the Import Subject Allocation Csv section.
     * <p>
     * This method at first gets all the Subject Allocation entries from the CSV file, then it goes for validation, if
     * validation is successful then the data is loaded into the DB otherwise display an error status.
     */
    @FXML
    private void handleSubmitCsvButton() {

        /*
        Create a HashMap and set up the following :
        Key : SubjectAllocation object data.
        Value : The name of the column extracted from the CSV file corresponding
                to that attribute.
         */
        Map<String, String> map = new HashMap<>();

        map.put("degree", subCsvDegreeComboBox.getValue());
        map.put("discipline", subCsvDisciplineComboBox.getValue());
        map.put("profId", subCsvProfIdComboBox.getValue());
        map.put("subId", subCsvSubIdComboBox.getValue());
        map.put("subName", subCsvSubNameComboBox.getValue());

        //display the loading spinner and fade the background
        importCsvMainGridPane.setOpacity(0.2);
        importCsvStatusStackPane.setVisible(true);
        importCsvProgressIndicator.setVisible(true);

        /*
        At first all the Subject Allocations of the CSV file is loaded into an ArrayList.
        Then a validation is run on the list of Subject Allocation to check if they comply the rules.
        Then all the Subject Allocation of the list is loaded into the DB.
         */
        Task<List<SubjectAllocation>> loadSubjectAllocationFromCsvToMemoryTask = subjectService
                .getLoadSubjectAllocationFromCsvToMemoryTask(file, map);
        new Thread(loadSubjectAllocationFromCsvToMemoryTask).start();

        loadSubjectAllocationFromCsvToMemoryTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                List<SubjectAllocation> listOfSubAllocFromCsv = new ArrayList<>
                        (loadSubjectAllocationFromCsvToMemoryTask.getValue());

                //index of the row containing student details currently under inspection
                int currRowInCsv = 1;
                boolean csvDataStatus = false;
                for (SubjectAllocation subjectAllocation : listOfSubAllocFromCsv) {

                    //validateCsv the current row containing Subject Allocation details
                    csvDataStatus = validateCsv(subjectAllocation, currRowInCsv++);

                    /*if any error found for any particular Subject Allocation in the csv ,stop uploading the whole
                    csv to db and display error msg
                     */
                    if (!csvDataStatus) {

                        deactivateProgressAndStatus();
                        break;
                    }
                }

                //no error found
                if (csvDataStatus) {

                    Task<Integer> addSubAllocFromMemoryToDbTask = subjectService
                            .getAddSubAllocFromMemoryToDbTask(listOfSubAllocFromCsv);
                    new Thread(addSubAllocFromMemoryToDbTask).start();

                    addSubAllocFromMemoryToDbTask.setOnSucceeded(new EventHandler<>() {
                        @Override
                        public void handle(WorkerStateEvent event) {

                            //get the status of the INSERT operation
                            int status = addSubAllocFromMemoryToDbTask.getValue();

                            //disable progress indicator and display status got from the method above
                            importCsvProgressIndicator.setVisible(false);
                            importCsvStatusImageView.setVisible(true);
                            importCsvStatusLabel.setVisible(true);

                            if (status == DATABASE_ERROR) {

                                importCsvStatusImageView.setImage(new javafx.scene.image.Image
                                        ("/png/critical error.png"));
                                importCsvStatusLabel.setText("Database Error!");
                            } else if (status == SUCCESS) {

                                importCsvStatusImageView.setImage(new javafx.scene.image.Image("/png/success.png"));
                                importCsvStatusLabel.setText("Successfully allocated subjects to professors!");
                            } else if (status == 0) {

                                importCsvStatusImageView.setImage(new javafx.scene.image.Image("/png/error.png"));
                                importCsvStatusLabel.setText("All subjects are already allocated to professors in " +
                                        "the database!");
                            } else {

                                importCsvStatusImageView.setImage(new Image("/png/error.png"));
                                importCsvStatusLabel.setText(status + " subject allocations are added to the database!");
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * This method validates a particular Subject Allocation entry in the CSV file.
     *
     * @param subjectAllocation The subjectAllocation entry from Csv file.
     * @param currSubAllocIndex Current index of the subjectAllocation entry in the csv file.
     * @return The validation status.
     */
    private boolean validateCsv(SubjectAllocation subjectAllocation, int currSubAllocIndex) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (subjectAllocation.getProfId() == null || subjectAllocation.getProfId().trim().isEmpty()) {

            alert.setContentText("Professor ID cannot be empty in Row : " + currSubAllocIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateId(subjectAllocation.getProfId().trim())) {

            alert.setContentText("Invalid Prof ID in Row : " + currSubAllocIndex + "!");
            alert.show();
            return false;
        }
        if (subjectAllocation.getDegree() == null || subjectAllocation.getDegree().trim().isEmpty()) {

            alert.setContentText("Degree of the subject cannot be empty in Row : " + currSubAllocIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(subjectAllocation.getDegree().trim())) {

            alert.setContentText("Invalid Degree of subject in Row : " + currSubAllocIndex + "!");
            alert.show();
            return false;
        }
        if (subjectAllocation.getDiscipline() == null || subjectAllocation.getDiscipline().trim().isEmpty()) {

            alert.setContentText("Discipline of the subject cannot be empty in Row : " + currSubAllocIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(subjectAllocation.getDiscipline().trim())) {

            alert.setContentText("Invalid Discipline of subject in Row : " + currSubAllocIndex + "!");
            alert.show();
            return false;
        }
        if (subjectAllocation.getSubId() == null || subjectAllocation.getSubId().trim().isEmpty()) {

            alert.setContentText("Subject ID cannot be empty in Row : " + currSubAllocIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateId(subjectAllocation.getSubId().trim())) {

            alert.setContentText("Invalid Subject ID in Row : " + currSubAllocIndex + "!");
            alert.show();
            return false;
        }
        if (subjectAllocation.getSubName() == null || subjectAllocation.getSubName().trim().isEmpty()) {

            alert.setContentText("Subject Name cannot be empty in Row : " + currSubAllocIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(subjectAllocation.getSubName().trim())) {

            alert.setContentText("Invalid Subject Name in Row : " + currSubAllocIndex + "!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * Callback method for statusAnchorPane Mouse Clicked of the Import Subject Allocation Csv.
     * <p>
     * On clicking the statusAnchorPane of the Import Subject Allocation section it will go away and mainGridPane
     * will be normal from faded and all the comboBoxes will be unset.
     */
    @FXML
    private void handleImportCsvStatusStackPaneMouseClickedAction() {

        deactivateProgressAndStatus();
    }

    /**
     * This method deactivates the progress indicator,status and un-sets the comboboxes.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateProgressAndStatus() {

        importCsvMainGridPane.setOpacity(1);
        importCsvProgressIndicator.setVisible(false);
        importCsvStatusImageView.setVisible(false);
        importCsvStatusLabel.setVisible(false);
        importCsvStatusStackPane.setVisible(false);
        unSetComboBoxes();
        chosenFileLabel.setText("");
        submitCsvButton.setDisable(true);
    }

    /**
     * Method for setting the ComboBoxes list of items.
     *
     * @param list List of columns extracted from the CSV file.
     */
    @SuppressWarnings("Duplicates")
    private void setComboBoxes(List<String> list) {

        ObservableList<String> options = FXCollections.observableArrayList(list);

        subCsvDegreeComboBox.setDisable(false);
        subCsvDegreeComboBox.setItems(options);
        subCsvDegreeComboBox.setValue(list.get(0));

        subCsvDisciplineComboBox.setDisable(false);
        subCsvDisciplineComboBox.setItems(options);
        subCsvDisciplineComboBox.setValue(list.get(1));

        subCsvProfIdComboBox.setDisable(false);
        subCsvProfIdComboBox.setItems(options);
        subCsvProfIdComboBox.setValue(list.get(2));

        subCsvSubIdComboBox.setDisable(false);
        subCsvSubIdComboBox.setItems(options);
        subCsvSubIdComboBox.setValue(list.get(3));

        subCsvSubNameComboBox.setDisable(false);
        subCsvSubNameComboBox.setItems(options);
        subCsvSubNameComboBox.setValue(list.get(4));
    }

    /**
     * Method for disabling and un-setting the ComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    private void unSetComboBoxes() {

        subCsvDegreeComboBox.setDisable(true);
        subCsvDegreeComboBox.setValue("");

        subCsvDisciplineComboBox.setDisable(true);
        subCsvDisciplineComboBox.setValue("");

        subCsvProfIdComboBox.setDisable(true);
        subCsvProfIdComboBox.setValue("");

        subCsvSubIdComboBox.setDisable(true);
        subCsvSubIdComboBox.setValue("");

        subCsvSubNameComboBox.setDisable(true);
        subCsvSubNameComboBox.setValue("");
    }

    /**
     * Method for configuring the fileChooser
     */
    private void configureFileChooser(FileChooser fileChooser) {

        fileChooser.setTitle("Import CSV file");

        //only .csv files can be chosen
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV", "*.csv"));
    }

    /*-------------------------------------Subject Allocation List tableView------------------------------------------*/

    /**
     * This method is used to initialize the Subject Allocation tab.
     * Basically it populates the tableView with any existing data in the database.
     */
    private void initSubAllocationTab() {

        subAllocObsList = FXCollections.observableArrayList();
        populateSubAllocTable();
        //get the list of departments available in the db
        Task<List<Department>> deptTask = departmentService
                .getDepartmentsTask("");
        new Thread(deptTask).start();

        deptTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                List<String> items = new ArrayList<>();

                for (Department dept : deptTask.getValue()) {

                    //add only unique department items to Sub Allocation form deptCombobox
                    if (!items.contains(dept.getDeptName()))
                        items.add(dept.getDeptName());
                }

                ObservableList<String> options = FXCollections.observableArrayList(items);
                subAllocFormDeptComboBox.setItems(options);
            }
        });
    }

    /**
     * This method is used to populate the Subject Allocation table.
     */
    @SuppressWarnings("Duplicates")
    private void populateSubAllocTable() {

        String additionalQuery = "";

        Task<List<SubjectAllocation>> subAllocTask = subjectService.getSubAllocTask(additionalQuery);
        new Thread(subAllocTask).start();

        subAllocTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                subAllocObsList.setAll(subAllocTask.getValue());
                subAllocTableView.setItems(subAllocObsList);
            }
        });
    }

    /**
     * This method is used to initialize the columns of the Subject Allocation table.
     */
    private void initSubAllocCols() {

        subAllocCourseIdCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        subAllocSubjectIdCol.setCellValueFactory(new PropertyValueFactory<>("subId"));
        subAllocProfIdCol.setCellValueFactory(new PropertyValueFactory<>("profId"));
    }

    /**
     * Callback method to handle the action of Delete button in the Subject Allocation list tableView.
     *
     * This basically deletes a single Subject Allocation from the table as well as the db.
     */
    @FXML
    private void handleDeleteSubAllocButtonAction() {

        SubjectAllocation subjectAllocation = subAllocTableView.getSelectionModel().getSelectedItem();

        if (subjectAllocation != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Do you really want to delete?");

            Optional<ButtonType> result = alert.showAndWait();

            //if 'yes' is selected for deletion
            if (result.get() == ButtonType.OK) {

                //fade the background and display a loading spinner
                subAllocListMainGridPane.setOpacity(0.2);
                subAllocListStatusStackPane.setVisible(true);
                subAllocListProgressIndicator.setVisible(true);

                Task<Integer> deleteSubAllocTask = subjectService.getDeleteSubAllocTask(subjectAllocation);
                new Thread(deleteSubAllocTask).start();

                deleteSubAllocTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //delete operation finished , so disable the progress indicator and display status
                        subAllocListStatusLabel.setVisible(true);
                        subAllocListProgressIndicator.setVisible(false);
                        subAllocListStatusImageView.setVisible(true);
                        subAllocListMsgLabel.setVisible(true);

                        int status = deleteSubAllocTask.getValue();

                        if (status == DATABASE_ERROR) {

                            subAllocListStatusImageView.setImage(new Image("/png/critical error.png"));
                            subAllocListStatusLabel.setText("Error!");
                        } else if (status == SUCCESS) {

                            subAllocListStatusImageView.setImage(new Image("/png/success.png"));
                            subAllocListStatusLabel.setText("Deleted Successfully!");
                            subAllocObsList.remove(subjectAllocation);
                        } else if (status == DATA_DEPENDENCY_ERROR) {

                            subAllocListStatusImageView.setImage(new Image("/png/error.png"));
                            subAllocListStatusLabel.setText("Cannot delete subject allocation!");
                        } else {

                            subAllocListStatusImageView.setImage(new Image("/png/error.png"));
                            subAllocListStatusLabel.setText("Subject hasn't been assigned to the professor yet!");
                        }

                    }
                });
            }
        }
    }

    /**
     * Method is used to handle the click event of the Status pane in the Subject Allocation list tableView.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubAllocListStatusStackPaneOnClicked() {

        subAllocListMainGridPane.setOpacity(1);
        subAllocListStatusStackPane.setVisible(false);
        subAllocListProgressIndicator.setVisible(false);
        subAllocListStatusImageView.setVisible(false);
        subAllocListStatusLabel.setVisible(false);
        subAllocListMsgLabel.setVisible(false);
    }


    /*---------------------------------------------Subject Allocation Form--------------------------------------------*/

    /**
     * Callback method to handle selection of a Department from the Dept Combo Box in the Subject Allocation Form.
     */
    @FXML
    private void handleDeptFormComboBox() {

        subAllocFormDegreeComboBox.getSelectionModel().clearSelection();
        subAllocFormDegreeComboBox.getItems().clear();

        subAllocFormProfIdComboBox.getSelectionModel().clearSelection();
        subAllocFormProfIdComboBox.getItems().clear();

        //get the list of Courses available in the db for that particular Dept.
        Task<List<Course>> coursesTask = courseService.getCoursesTask("where v_dept_name=?"
                , subAllocFormDeptComboBox.getValue());
        new Thread(coursesTask).start();

        coursesTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //store the list of all courses available in the DB for that particular department
                listOfCourses = coursesTask.getValue();

                List<String> items = new ArrayList<>();

                for (Course course : listOfCourses) {

                    //add only unique degree items to Degree combo box
                    if (!items.contains(course.getDegree()))
                        items.add(course.getDegree());
                }

                ObservableList<String> options = FXCollections.observableArrayList(items);
                subAllocFormDegreeComboBox.setItems(options);
            }
        });

        //get the list of professors for that particular department
        Task<List<Professor>> profsTask = professorService.getProfessorTask("where v_dept_name=?"
                , subAllocFormDeptComboBox.getValue());
        new Thread(profsTask).start();

        profsTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                listOfProfInSubAllocForm = profsTask.getValue();

                List<String> items = new ArrayList<>();

                for (Professor professor : listOfProfInSubAllocForm) {

                    items.add(professor.getProfId());
                }

                ObservableList<String> options = FXCollections.observableArrayList(items);
                subAllocFormProfIdComboBox.setItems(options);
            }
        });
    }

    /**
     * This method is used to handle the selection of a Degree from the Degree Combo Box in the Subject Allocation Form.
     */
    @FXML
    private void handleSubDegreeFormComboBox() {

        subAllocFormDisciplineComboBox.getSelectionModel().clearSelection();
        subAllocFormDisciplineComboBox.getItems().clear();

        if (subAllocFormDegreeComboBox.getValue() != null) {

            List<String> items = new ArrayList<>();

            for (Course course : listOfCourses) {

                if (course.getDegree().equals(subAllocFormDegreeComboBox.getValue())) {

                    //add only unique discipline items to discipline Combobox for that particular dept and degree
                    if (!items.contains(course.getDiscipline()))
                        items.add(course.getDiscipline());
                }
            }

            ObservableList<String> options = FXCollections.observableArrayList(items);
            subAllocFormDisciplineComboBox.setItems(options);
        }
    }

    /**
     * Method to handle the selection of Discipline from the combo Boxes in the Subject Allocation Form.
     */
    @FXML
    private void handleSubDisciplineFormComboBox() {

        subAllocFormSubjectIdComboBox.getSelectionModel().clearSelection();
        subAllocFormSubjectIdComboBox.getItems().clear();

        if (subAllocFormDisciplineComboBox.getValue() != null) {

            //get the course ID for the particualr degree and discipline
            for (Course course : listOfCourses) {

                if (subAllocFormDegreeComboBox.getValue().equals(course.getDegree())
                        && subAllocFormDisciplineComboBox.getValue().equals(course.getDiscipline())) {

                    courseId = course.getCourseId();
                }
            }

            //get the subjects for that particualr course ID
            Task<List<Subject>> subjectsTask = subjectService.getSubjectsTask("where v_course_id=?"
                    , courseId);
            new Thread(subjectsTask).start();

            subjectsTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    List<String> items = new ArrayList<>();

                    for (Subject subject : subjectsTask.getValue()) {

                        if (!items.contains(subject.getSubId())) {

                            items.add(subject.getSubId());
                        }
                    }

                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    subAllocFormSubjectIdComboBox.setItems(options);
                }
            });
        }
    }

    /**
     * This method is used to handle the selection of Prof ID from the comboBox in the Subject Allocation form.
     */
    @FXML
    private void handleSubProfFormComboBox() {

        if (subAllocFormProfIdComboBox.getValue() != null) {

            for (Professor professor : listOfProfInSubAllocForm) {

                if (subAllocFormProfIdComboBox.getValue().equals(professor.getProfId())) {

                    //set the name of the Professor for that respective Prof ID.
                    subAllocFormProfNameLabel.setText(professor.getFirstName() + " " + professor.getMiddleName()
                            + " " + professor.getLastName());
                }
            }
        }
    }

    /**
     * Method to handle the Submit Button event of the Subject Allocation Form.
     */
    @FXML
    private void handleSubmitFormButtonAction() {

        //if all data is inserted , then only proceed for addition of the data into the database
        if (validateSubAllocForm()) {

            //fade the background and display a loading spinner
            subAllocFormMainGridPane.setOpacity(0.2);
            subAllocFormStatusStackPane.setVisible(true);
            subAllocFormProgressIndicator.setVisible(true);

            //create a new subject allocation instance
            SubjectAllocation subjectAllocation = new SubjectAllocation();

            subjectAllocation.setProfId(subAllocFormProfIdComboBox.getValue());
            subjectAllocation.setSubId(subAllocFormSubjectIdComboBox.getValue());
            subjectAllocation.setCourseId(courseId);

            Task<Integer> addSubAllocTask = subjectService.getAddSubAllocTask(subjectAllocation);
            new Thread(addSubAllocTask).start();

            addSubAllocTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    //INSERT operation finished, so disable the loading spinner and display status
                    subAllocFormProgressIndicator.setVisible(false);
                    subAllocFormStatusImageView.setVisible(true);
                    subAllocFormStatusLabel.setVisible(true);
                    subAllocFormMsgLabel.setVisible(true);

                    int status = addSubAllocTask.getValue();

                    if (status == DATABASE_ERROR) {

                        subAllocFormStatusImageView.setImage(new Image("/png/critical error.png"));
                        subAllocFormStatusLabel.setText("Error!");
                    } else if (status == SUCCESS) {

                        subAllocFormStatusImageView.setImage(new Image("/png/success.png"));
                        subAllocFormStatusLabel.setText("Successfully Assigned a subject to a professor!");
                        subAllocObsList.add(subjectAllocation);
                    } else {

                        subAllocFormStatusImageView.setImage(new Image("/png/error.png"));
                        subAllocFormStatusLabel.setText("Subject is already assigned to the particular professor!");
                    }
                }
            });
        }
    }

    /**
     * This method is used to handle the event of the Reset button of the Subject Allocation form.
     */
    @FXML
    private void handleResetFormButtonAction() {

        deactivateSubAllocStatusAndProgressIndicator();
        unSetSubAllocFormComboBoxes();
    }

    /**
     * This method is used to handle the click event of the Status Stack pane of the Subject Allocation form.
     * <p>
     * After the "add" operation of a Subject Allocation is done, the status stack pane is clicked and the
     * status label, msg label and the progress indicator goes away, the main UI comes back and all the
     * comboboxes are reset.
     */
    @FXML
    private void handleSubAllocFormStatusStackPaneOnClicked() {

        deactivateSubAllocStatusAndProgressIndicator();
        unSetSubAllocFormComboBoxes();
    }

    /**
     * Method to deactivate the status pane and brings the UI back to what it was before adding a Subject Allocation.
     * <p>
     * This is for Subject Allocation form.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateSubAllocStatusAndProgressIndicator() {

        subAllocFormStatusStackPane.setVisible(false);
        subAllocFormMainGridPane.setOpacity(1);
        subAllocFormProgressIndicator.setVisible(false);
        subAllocFormStatusImageView.setVisible(false);
        subAllocFormStatusLabel.setVisible(false);
        subAllocFormMsgLabel.setVisible(false);
    }

    /**
     * Method to reset all the comboboxes of the Subject Allocation form when clicking on the Reset button or
     * after one Subject Allocation has been added to the Db.
     */
    private void unSetSubAllocFormComboBoxes() {

        subAllocFormDeptComboBox.setValue(null);
        subAllocFormProfIdComboBox.setValue(null);
        subAllocFormDisciplineComboBox.setValue(null);
        subAllocFormDegreeComboBox.setValue(null);
        subAllocFormSubjectIdComboBox.setValue(null);
    }

    /**
     * Method to validate a single subject allocation entry.
     *
     * @return The result of the valdiation.
     */
    private boolean validateSubAllocForm() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (subAllocFormDeptComboBox.getValue() == null) {

            alert.setHeaderText("Please choose a department!");
            alert.show();
            return false;
        }
        if (subAllocFormDegreeComboBox.getValue() == null) {

            alert.setHeaderText("Please choose a degree!");
            alert.show();
            return false;
        }
        if (subAllocFormDisciplineComboBox.getValue() == null) {

            alert.setHeaderText("Please choose a disicipline!");
            alert.show();
            return false;
        }
        if (subAllocFormSubjectIdComboBox.getValue() == null) {

            alert.setHeaderText("Please choose a Subject ID!");
            alert.show();
            return false;
        }
        if (subAllocFormProfIdComboBox.getValue() == null) {

            alert.setHeaderText("Please choose a Professor ID!");
            alert.show();
            return false;
        }
        return true;
    }


    /*------------------------------------------End of Subject Allocation Tab-----------------------------------------*/

}
