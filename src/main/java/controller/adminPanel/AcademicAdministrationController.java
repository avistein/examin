package controller.adminPanel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.*;
import service.*;
import util.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;

import static util.ConstantsUtil.*;

/**
 * Controller class for AcademicAdministration.fxml
 *
 * @author Avik Sarkar
 */
public class AcademicAdministrationController {

    /*------------------------Declaration & initialization of items common to all tabs--------------------------------*/

    @FXML
    private TabPane academicAdministrationTabPane;

    @FXML
    private Tab departmentsTab;

    @FXML
    private Tab coursesTab;

    @FXML
    private Tab batchesTab;

    @FXML
    private Tab subjectsTab;

    @FXML
    private Tab classRoomsTab;

    /*------------------------------------------End of initialization-------------------------------------------------*/


    /*--------------------------Initialization and declaration of Department tab's components.------------------------*/

    @FXML
    private TextField deptNameTextfield;

    @FXML
    private TextField buildingNameTextField;

    @FXML
    private TableView<Department> deptTableView;

    @FXML
    private TableColumn<Department, String> deptDeptNameCol;

    @FXML
    private TableColumn<Department, String> deptBuildingNameCol;

    @FXML
    private GridPane addDeptMainGridPane;

    @FXML
    private StackPane addDeptStatusStackPane;

    @FXML
    private ImageView addDeptStatusImageView;

    @FXML
    private ProgressIndicator addDeptProgressIndicator;

    @FXML
    private Label addDeptStatusLabel;

    @FXML
    private HBox addDeptButtonsHbox;

    @FXML
    private GridPane deptListMainGridPane;

    @FXML
    private StackPane deptListStatusStackPane;

    @FXML
    private ImageView deptListStatusImageView;

    @FXML
    private ProgressIndicator deptListProgressIndicator;

    @FXML
    private Label deptListStatusLabel;

    @FXML
    private HBox deptListButtonsHbox;

    private DepartmentService departmentService;
    private ObservableList<Department> departmentObsList;
    private int deptAddOrEditChoice;

    /*-------------------------------End of initialization of Department's tab components-----------------------------*/


    /*-------------------------Initialization and declaration of Courses tab's components-----------------------------*/

    @FXML
    private ComboBox<String> courseDeptNameComboBox;

    @FXML
    private TextField courseIdTextField;

    @FXML
    private TextField degreeTextField;

    @FXML
    private TextField disciplineTextField;

    @FXML
    private TextField durationTextField;

    @FXML
    private TableView<Course> courseTableView;

    @FXML
    private TableColumn<Course, String> courseDeptNameCol;

    @FXML
    private TableColumn<Course, String> courseCourseIdCol;

    @FXML
    private TableColumn<Course, String> courseDegreeCol;

    @FXML
    private TableColumn<Course, String> courseDisciplineCol;

    @FXML
    private TableColumn<Course, String> courseDurationCol;

    @FXML
    private GridPane addCourseMainGridPane;

    @FXML
    private StackPane addCourseStatusStackPane;

    @FXML
    private ImageView addCourseStatusImageView;

    @FXML
    private ProgressIndicator addCourseProgressIndicator;

    @FXML
    private Label addCourseStatusLabel;

    @FXML
    private HBox addCourseButtonsHbox;

    @FXML
    private GridPane courseListMainGridPane;

    @FXML
    private StackPane courseListStatusStackPane;

    @FXML
    private ImageView courseListStatusImageView;

    @FXML
    private ProgressIndicator courseListProgressIndicator;

    @FXML
    private Label courseListStatusLabel;

    @FXML
    private HBox courseListButtonsHbox;

    private CourseService courseService;
    private ObservableList<Course> courseObsList;
    private int courseAddOrEditChoice;

    /*--------------------------------End of initialization of Courses tab's components-------------------------------*/

    /*-------------------------Initialization and declaration of Batches tab's components-----------------------------*/

    @FXML
    private ComboBox<String> batchesDegreeComboBox;

    @FXML
    private ComboBox<String> batchesDisciplineComboBox;

    @FXML
    private TextField batchIdTextField;

    @FXML
    private TextField batchNameTextField;

    @FXML
    private TableView<Batch> batchTableView;

    @FXML
    private TableColumn<Batch, String> batchesDeptNameCol;

    @FXML
    private TableColumn<Batch, String> batchesBatchIdCol;

    @FXML
    private TableColumn<Batch, String> batchesBatchNameCol;

    @FXML
    private TableColumn<Batch, String> batchesDegreeCol;

    @FXML
    private TableColumn<Batch, String> batchesDisciplineCol;

    @FXML
    private GridPane addBatchMainGridPane;

    @FXML
    private StackPane addBatchStatusStackPane;

    @FXML
    private ImageView addBatchStatusImageView;

    @FXML
    private ProgressIndicator addBatchProgressIndicator;

    @FXML
    private Label addBatchStatusLabel;

    @FXML
    private HBox addBatchButtonHbox;

    @FXML
    private GridPane batchListMainGridPane;

    @FXML
    private StackPane batchListStatusStackPane;

    @FXML
    private ImageView batchListStatusImageView;

    @FXML
    private ProgressIndicator batchListProgressIndicator;

    @FXML
    private Label batchListStatusLabel;

    @FXML
    private HBox batchListButtonHbox;

    private BatchService batchService;
    private ObservableList<Batch> batchObsList;
    private int batchAddOrEditChoice;
    private List<Course> listOfCourses;

    /*-------------------------------------End of initialization of Batches tab's components--------------------------*/


    /*-------------------------Initialization and declaration of Subjects tab's components-----------------------------*/

    @FXML
    private ComboBox<String> subjectsDegreeComboBox;

    @FXML
    private ComboBox<String> subjectsDisciplineComboBox;

    @FXML
    private ComboBox<String> subjectsSemesterComboBox;

    @FXML
    private TextField subjectIdTextField;

    @FXML
    private TextField subjectNameTextField;

    @FXML
    private ChoiceBox<String> subjectTypeChoiceBox;

    @FXML
    private TextField creditTextField;

    @FXML
    private TextField fullMarksTextField;

    @FXML
    private TableView<Subject> subjectsTableView;

    @FXML
    private TableColumn<Subject, String> subFullMarksCol;

    @FXML
    private TableColumn<Subject, String> subSubIdCol;

    @FXML
    private TableColumn<Subject, String> subDegreeCol;

    @FXML
    private TableColumn<Subject, String> subDisciplineCol;

    @FXML
    private TableColumn<Subject, String> subSemesterCol;

    @FXML
    private TableColumn<Subject, String> subSubNameCol;

    @FXML
    private TableColumn<Subject, String> subSubTypeCol;

    @FXML
    private TableColumn<Subject, String> subCreditCol;

    @FXML
    private GridPane addSubjectsMainGridPane;

    @FXML
    private StackPane addSubjectsStatusStackPane;

    @FXML
    private ProgressIndicator addSubjectsProgressIndicator;

    @FXML
    private ImageView addSubjectsStatusImageView;

    @FXML
    private Label addSubjectsStatusLabel;

    @FXML
    private HBox addSubjectsButtonHbox;

    @FXML
    private GridPane subjectsListMainGridPane;

    @FXML
    private StackPane subjectsListStatusStackPane;

    @FXML
    private ProgressIndicator subjectsListProgressIndicator;

    @FXML
    private ImageView subjectsListStatusImageView;

    @FXML
    private Label subjectsListStatusLabel;

    @FXML
    private HBox subjectsListButtonHbox;

    private SubjectService subjectService;
    private ObservableList<Subject> subjectObsList;
    private int subjectAddOrEditChoice;

    /*-------------------------------------End of initialization of Subjects tab's components--------------------------*/


    /*-------------------------Initialization and declaration of Classrooms tab's components-----------------------------*/

    @FXML
    private TextField roomNoTextField;

    @FXML
    private TextField capacityTextField;

    @FXML
    private TextField rowTextField;

    @FXML
    private TextField columnTextField;

    @FXML
    private TableView<Classroom> classroomsTableView;

    @FXML
    private TableColumn<Classroom, String> classroomRoomNoCol;

    @FXML
    private TableColumn<Classroom, String> classroomCapacityCol;

    @FXML
    private TableColumn<Classroom, String> classroomRowCol;

    @FXML
    private TableColumn<Classroom, String> classroomColumnCol;

    @FXML
    private GridPane addClassroomMainGridPane;

    @FXML
    private StackPane addClassroomStatusStackPane;

    @FXML
    private ImageView addClassroomStatusImageView;

    @FXML
    private ProgressIndicator addClassroomProgressIndicator;

    @FXML
    private Label addClassroomStatusLabel;

    @FXML
    private HBox addClassroomButtonHbox;

    @FXML
    private GridPane classroomListMainGridPane;

    @FXML
    private StackPane classroomListStatusStackPane;

    @FXML
    private ImageView classroomListStatusImageView;

    @FXML
    private ProgressIndicator classroomListProgressIndicator;

    @FXML
    private Label classroomListStatusLabel;

    @FXML
    private HBox classroomListButtonHbox;

    private ClassRoomService classroomService;
    private ObservableList<Classroom> classroomObsList;
    private int classroomAddOrEditChoice;

    /*-------------------------------------End of initialization of Classrooms tab's components--------------------------*/


    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    private void initialize() {

        deptAddOrEditChoice = ADD_CHOICE;
        courseAddOrEditChoice = ADD_CHOICE;
        batchAddOrEditChoice = ADD_CHOICE;
        subjectAddOrEditChoice = ADD_CHOICE;
        classroomAddOrEditChoice = ADD_CHOICE;

        departmentService = new DepartmentService();
        departmentObsList = FXCollections.observableArrayList();

        courseService = new CourseService();
        courseObsList = FXCollections.observableArrayList();

        batchService = new BatchService();
        batchObsList = FXCollections.observableArrayList();

        subjectService = new SubjectService();
        subjectObsList = FXCollections.observableArrayList();

        classroomService = new ClassRoomService();
        classroomObsList = FXCollections.observableArrayList();

        //initialize columns of dept table and populate dept table
        initDeptCol();
        populateDeptTable();

        //only initialize columns of course table
        initCourseCol();

        initBatchCol();

        initSubjectCol();

        initClassroomCol();

        //initialize the respective tabs if it is selected
        academicAdministrationTabPane.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<>() {

                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {

                        if (newValue == departmentsTab) {

                            initDepartmentsTab();
                        } else if (newValue == coursesTab) {

                            initCoursesTab();
                        } else if (newValue == batchesTab) {

                            initBatchesTab();
                        } else if (newValue == subjectsTab) {

                            initSubjectsTab();
                        } else if (newValue == classRoomsTab) {

                            initClassroomsTab();
                        }
                    }
                });
    }


    /*-----------------------------------------End of initialization of items-----------------------------------------*/


    /*------------------------------------------------------------------------------------------------------------------
    Departments Tab operations:
    Add,edit,delete department and view them in the table.
     -----------------------------------------------------------------------------------------------------------------*/

    /**
     * Initialization of Departments Tab
     */
    private void initDepartmentsTab() {

        populateDeptTable();
    }

    /**
     * Callback method for Submit Button.
     * Handles both addition of new departments & modification of
     * department.
     */
    @FXML
    private void handleSubmitDeptButtonAction() {

        String deptName = deptNameTextfield.getText().trim();
        String building = buildingNameTextField.getText().trim();

        //add or edit dept only when they are valid
        if (validateDeptItems()) {

            //display the loading spinner
            activateAddDeptProgressIndicator();

            Department newDepartment = new Department();
            newDepartment.setDeptName(deptName);
            newDepartment.setBuildingName(building);

            //'add department' chosen
            if (deptAddOrEditChoice == ADD_CHOICE) {

                Task<Integer> addDepartmentToDatabaseTask = departmentService
                        .getAddDepartmentToDatabaseTask(newDepartment);
                new Thread(addDepartmentToDatabaseTask).start();

                addDepartmentToDatabaseTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        int status = addDepartmentToDatabaseTask.getValue();

                        //add operation finished on database, disable loading spinner and show status
                        deactivateAddDeptProgressIndicator();

                        if (status == DATABASE_ERROR) {

                            addDeptStatusImageView.setImage(new Image("/png/critical error.png"));
                            addDeptStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            addDeptStatusImageView.setImage(new Image("/png/success.png"));
                            addDeptStatusLabel.setText("Added Successfully!");
                            populateDeptTable();
                        } else {

                            addDeptStatusImageView.setImage(new Image("/png/error.png"));
                            addDeptStatusLabel.setText("Department already exists!");
                        }
                    }
                });
            }

            //'edit department' chosen
            else {

                deptAddOrEditChoice = ADD_CHOICE;

                Task<Integer> editDepartmentTask = departmentService
                        .getUpdateDepartmentTask(newDepartment);
                new Thread(editDepartmentTask).start();

                editDepartmentTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        int status = editDepartmentTask.getValue();

                        //edit operation on database finished, disable loading spinner
                        deactivateAddDeptProgressIndicator();

                        if (status == DATABASE_ERROR) {

                            addDeptStatusImageView.setImage(new Image("/png/critical error.png"));
                            addDeptStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            addDeptStatusImageView.setImage(new Image("/png/success.png"));
                            addDeptStatusLabel.setText("Edited Successfully!");
                            populateDeptTable();
                        } else if (status == DATA_INEXISTENT_ERROR) {

                            addDeptStatusImageView.setImage(new Image("/png/error.png"));
                            addDeptStatusLabel.setText("Department doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method for both Ok button and Reset button in the Add Department section.
     * It basically clears all department's text fields and sets up the content area for
     * new addition or modification of another department.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDeptOkAndResetButtonAction() {

        deactivateAddDeptStatusStackPane();
        deptNameTextfield.clear();
        buildingNameTextField.clear();
        deptNameTextfield.setDisable(false);
    }

    /**
     * Callback method for Edit button in the Department's List section.
     * It sets up the Add Department form for modification of the department.
     */
    @FXML
    private void handleEditDeptButtonAction() {

        //get the selected dept from the table
        Department department = deptTableView.getSelectionModel().getSelectedItem();

        //Trigger edit only when a department is selected in the table
        if (department != null) {

            //set the choice to Edit Department
            deptAddOrEditChoice = EDIT_CHOICE;

            //dept name is immutable,as it is primary key
            deptNameTextfield.setDisable(true);

            //sets the department name from the column to the textfield
            deptNameTextfield.setText(department.getDeptName());

            //sets the building name from the column to the textfield
            buildingNameTextField.setText(department.getBuildingName());
        }
    }

    /**
     * Callback method for Delete button in Department List section.
     */
    @FXML
    private void handleDeleteDeptButtonAction() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you really want to delete?");

        //get the selected department from the table
        Department department = deptTableView.getSelectionModel()
                .getSelectedItem();

        //Trigger delete only when a department is selected in the table.
        if (department != null) {

            alert.showAndWait();
            ButtonType result = alert.getResult();

            if (result == ButtonType.OK) {

                //display a loading spinner to indicate that delete is taking place
                activateDeptListProgressIndicator();

                Task<Integer> deleteDepartmentTask = departmentService
                        .getDeleteDepartmentTask(department.getDeptName());
                new Thread(deleteDepartmentTask).start();

                deleteDepartmentTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        int status = deleteDepartmentTask.getValue();

                        //delete operation finished on database, disable the loading spinner and show status
                        deactivateDeptListProgressIndicator();

                        if (status == DATABASE_ERROR) {

                            deptListStatusImageView.setImage(new Image("/png/critical error.png"));
                            deptListStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            deptListStatusImageView.setImage(new Image("/png/success.png"));
                            deptListStatusLabel.setText("Deleted Successfully!");
                            populateDeptTable();
                        } else if (status == DATA_DEPENDENCY_ERROR) {

                            deptListStatusImageView.setImage(new Image("/png/error.png"));
                            deptListStatusLabel.setText("Cannot delete department!");
                        } else if (status == DATA_INEXISTENT_ERROR) {

                            deptListStatusImageView.setImage(new Image("/png/error.png"));
                            deptListStatusLabel.setText("Department doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method for Ok button in the Dept List section's deptListStatusStackPane.
     * Brings the table back into focus.
     */
    @FXML
    private void handleDeptListOkButtonAction() {
        deactivateDeptListStatusStackPane();
    }

    /**
     * This method validates all textfields in the Department Tab
     *
     * @return A boolean value indicating the validation result.
     */
    private boolean validateDeptItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (deptNameTextfield.getText() == null || deptNameTextfield.getText().isEmpty()) {

            alert.setContentText("Department Name cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(deptNameTextfield.getText())) {

            alert.setContentText("Invalid department name!");
            alert.show();
            return false;
        }
        if (buildingNameTextField.getText() == null || buildingNameTextField.getText().isEmpty()) {

            alert.setContentText("Building Name cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(buildingNameTextField.getText())) {

            alert.setContentText("Invalid building name!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * This method initialize the columns of the Department TableView.
     */
    private void initDeptCol() {

        deptDeptNameCol.setCellValueFactory(new PropertyValueFactory<>("deptName"));
        deptBuildingNameCol.setCellValueFactory(new PropertyValueFactory<>("buildingName"));
    }

    /**
     * This method populates/updates the Department Table.
     */
    @SuppressWarnings("Duplicates")
    private void populateDeptTable() {

        Task<List<Department>> departmentsTask;
        departmentsTask = departmentService.getDepartmentsTask("");
        new Thread(departmentsTask).start();

        departmentsTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //get a ArrayList of departments and set the ObsList with it
                departmentObsList.setAll(departmentsTask.getValue());

                //set the tableview
                deptTableView.setItems(departmentObsList);
            }
        });
    }

    /**
     * This method activates the loading spinner in the Add Department section.
     */
    private void activateAddDeptProgressIndicator() {

        addDeptMainGridPane.setOpacity(0.5);
        addDeptStatusStackPane.setVisible(true);
        addDeptProgressIndicator.setVisible(true);
    }

    /**
     * This method deactivates the loading spinner and displays the status
     * in the Add Department section.
     */
    private void deactivateAddDeptProgressIndicator() {

        addDeptProgressIndicator.setVisible(false);
        addDeptButtonsHbox.setVisible(true);
        addDeptStatusImageView.setVisible(true);
        addDeptStatusLabel.setVisible(true);
    }

    /**
     * This method activates the loading spinner in the Department List section.
     */
    private void activateDeptListProgressIndicator() {

        deptListMainGridPane.setOpacity(0.5);
        deptListStatusStackPane.setVisible(true);
        deptListProgressIndicator.setVisible(true);
    }

    /**
     * This method deactivates the loading spinner and activates the status
     * in the Department List section.
     */
    private void deactivateDeptListProgressIndicator() {

        deptListProgressIndicator.setVisible(false);
        deptListButtonsHbox.setVisible(true);
        deptListStatusImageView.setVisible(true);
        deptListStatusLabel.setVisible(true);
    }

    /**
     * This method deactivates the whole status stack pane,including the loading
     * spinner & status in the Add Dept section.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateAddDeptStatusStackPane() {
        addDeptStatusStackPane.setVisible(false);
        addDeptProgressIndicator.setVisible(false);
        addDeptButtonsHbox.setVisible(false);
        addDeptStatusImageView.setVisible(false);
        addDeptStatusLabel.setVisible(false);
        addDeptMainGridPane.setOpacity(1);
    }

    /**
     * This method deactivates the whole status stack pane,including the loading
     * spinner & status in the Dept List section.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateDeptListStatusStackPane() {

        deptListStatusStackPane.setVisible(false);
        deptListProgressIndicator.setVisible(false);
        deptListButtonsHbox.setVisible(false);
        deptListStatusImageView.setVisible(false);
        deptListStatusLabel.setVisible(false);
        deptListMainGridPane.setOpacity(1);
    }

    /*----------------------------------------End of Department Tabs operations---------------------------------------*/


    /*------------------------------------------------------------------------------------------------------------------
    Courses Tab operations:
    Add,edit,delete course and view them in the table.
     -----------------------------------------------------------------------------------------------------------------*/

    /**
     * Initialization of Courses Tab : adds department to Dept Combo Box
     */
    private void initCoursesTab() {

        populateCourseTable();

        Task<List<Department>> departmentsTask = departmentService.getDepartmentsTask("");
        new Thread(departmentsTask).start();

        departmentsTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                List<Department> listOfDepartments = departmentsTask.getValue();

                //Add department names to the batchComboBox
                if (!listOfDepartments.isEmpty()) {

                    List<String> items = new ArrayList<>();
                    for (Department department : listOfDepartments) {

                        items.add(department.getDeptName());
                    }
                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    courseDeptNameComboBox.setItems(options);
                }
            }
        });

    }

    /**
     * Callback method to handle action of Submit Button.
     * <p>
     * This method either adds a new Course to the DB or updates an existing Course in the DB.
     */
    @FXML
    private void handleSubmitCourseButtonAction() {

        String deptName = courseDeptNameComboBox.getValue();
        String courseId = courseIdTextField.getText().trim();
        String degree = degreeTextField.getText().trim();
        String discipline = disciplineTextField.getText().trim();
        String duration = durationTextField.getText().trim();

        //add or edit course only when they are valid
        if (validateCourseItems()) {

            //display the loading spinner
            activateAddCourseProgressIndicator();

            Course newCourse = new Course();

            //initialize the newly created course object
            newCourse.setDeptName(deptName);
            newCourse.setCourseId(courseId);
            newCourse.setDegree(degree);
            newCourse.setDiscipline(discipline);
            newCourse.setDuration(duration);

            //'add chosen' chosen
            if (courseAddOrEditChoice == ADD_CHOICE) {

                Task<Integer> addCourseToDatabaseTask = courseService
                        .getAddCourseToDatabaseTask(newCourse);
                new Thread(addCourseToDatabaseTask).start();

                addCourseToDatabaseTask.setOnSucceeded(new EventHandler<>() {

                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of insertion operation
                        int status = addCourseToDatabaseTask.getValue();

                        //add operation finished on database, disable loading spinner and show status
                        deactivateAddCourseProgressIndicator();


                        //display status
                        if (status == DATABASE_ERROR) {

                            addCourseStatusImageView.setImage(new Image("/png/critical error.png"));
                            addCourseStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            addCourseStatusImageView.setImage(new Image("/png/success.png"));
                            addCourseStatusLabel.setText("Course added Successfully!");
                            populateCourseTable();
                        } else {

                            addCourseStatusImageView.setImage(new Image("/png/error.png"));
                            addCourseStatusLabel.setText("Course already exists!");
                        }
                    }
                });
            }

            //'edit course' chosen
            else {

                //change the status to ADD_CHOICE, since editing is done
                courseAddOrEditChoice = ADD_CHOICE;

                Task<Integer> editCourseTask = courseService
                        .getUpdateCourseTask(newCourse);
                new Thread(editCourseTask).start();

                editCourseTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of update operation
                        int status = editCourseTask.getValue();

                        //edit operation on database finished, disable loading spinner
                        deactivateAddCourseProgressIndicator();

                        //display status
                        if (status == DATABASE_ERROR) {

                            addCourseStatusImageView.setImage(new Image("/png/critical error.png"));
                            addCourseStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            addCourseStatusImageView.setImage(new Image("/png/success.png"));
                            addCourseStatusLabel.setText("Course Edited Successfully!");
                            populateCourseTable();
                        } else if (status == DATA_INEXISTENT_ERROR) {

                            addCourseStatusImageView.setImage(new Image("/png/error.png"));
                            addCourseStatusLabel.setText("Course doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method to handle Ok and Reset button action.
     * <p>
     * This disables the progress indicator,status and clears the textfields and comboBoxes for addition of new
     * course.
     */
    @FXML
    private void handleCourseOkAndResetButtonAction() {

        deactivateAddCourseStatusStackPane();
        courseDeptNameComboBox.setDisable(false);
        courseDeptNameComboBox.getSelectionModel().clearSelection();
        courseIdTextField.setDisable(false);
        courseIdTextField.clear();
        degreeTextField.clear();
        disciplineTextField.clear();
        durationTextField.clear();
    }

    /**
     * Callback method to handle action of Edit Course button.
     * <p>
     * This method basically sets the Course detials to be edited in the respective textfields and comboBoxes and
     * sets the Edit Signal so that the {@link #handleSubmitCourseButtonAction()} works on updating the course.
     */
    @FXML
    private void handleEditCourseButtonAction() {

        //get the selected course from the table
        Course course = courseTableView.getSelectionModel().getSelectedItem();

        //Trigger edit only when a course is selected in the table
        if (course != null) {

            //set the choice to Edit Course
            courseAddOrEditChoice = EDIT_CHOICE;

            //department selection cannot be edited
            courseDeptNameComboBox.setDisable(true);

            //course is immutable,as it is primary key
            courseIdTextField.setDisable(true);

            //sets the selection of the dept name from the column to the combobox
            courseDeptNameComboBox.setValue(course.getDeptName());

            //sets the course id from the column to the textfield
            courseIdTextField.setText(course.getCourseId());

            //sets the degree from the column to the textfield
            degreeTextField.setText(course.getDegree());

            //sets the discipline from the column to the textfield
            disciplineTextField.setText(course.getDiscipline());

            //sets the duration from the column to the textfield
            durationTextField.setText(course.getDuration());
        }
    }

    /**
     * Callback method to handle action of Delete Course button.
     * This method deletes the selected Course in table from the DB.
     */
    @FXML
    private void handleDeleteCourseButtonAction() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you really want to delete?");

        //get the selected course from the table
        Course course = courseTableView.getSelectionModel()
                .getSelectedItem();

        //Trigger delete only when a course is selected in the table.
        if (course != null) {

            alert.showAndWait();
            ButtonType result = alert.getResult();

            if (result == ButtonType.OK) {

                //display a loading spinner to indicate that delete is taking place
                activateCourseListProgressIndicator();

                Task<Integer> deleteCourseTask = courseService
                        .getDeleteCourseTask(course.getCourseId());
                new Thread(deleteCourseTask).start();

                deleteCourseTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get delete operation status
                        int status = deleteCourseTask.getValue();

                        //delete operation finished on database, disable the loading spinner and show status
                        deactivateCourseListProgressIndicator();

                        //display status
                        if (status == DATABASE_ERROR) {

                            courseListStatusImageView.setImage(new Image("/png/critical error.png"));
                            courseListStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            courseListStatusImageView.setImage(new Image("/png/success.png"));
                            courseListStatusLabel.setText("Deleted Successfully!");
                            populateCourseTable();
                        } else if (status == DATA_DEPENDENCY_ERROR) {

                            courseListStatusImageView.setImage(new Image("/png/error.png"));
                            courseListStatusLabel.setText("Cannot delete course!");
                        } else if (status == DATA_INEXISTENT_ERROR) {

                            courseListStatusImageView.setImage(new Image("/png/error.png"));
                            courseListStatusLabel.setText("Course doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method for Ok button in the Course List section's courseListStatusStackPane.
     * Brings the table back into focus.
     */
    @FXML
    private void handleCourseListOkButtonAction() {

        deactivateCourseListStatusStackPane();
    }

    /**
     * This method validates all textfields in the Course Tab
     *
     * @return A boolean value indicating the validation result.
     */
    private boolean validateCourseItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (courseDeptNameComboBox.getValue() == null) {

            alert.setContentText("Please select a department!");
            alert.show();
            return false;
        }
        if (courseIdTextField.getText() == null || courseIdTextField.getText().isEmpty()) {

            alert.setContentText("Course ID cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(courseIdTextField.getText().trim())) {

            alert.setContentText("Invalid Course ID!");
            alert.show();
            return false;
        }
        if (degreeTextField.getText() == null || degreeTextField.getText().isEmpty()) {

            alert.setContentText("Degree cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(degreeTextField.getText().trim())) {

            alert.setContentText("Invalid degree!");
            alert.show();
            return false;
        }
        if (disciplineTextField.getText() == null || disciplineTextField.getText().isEmpty()) {

            alert.setContentText("Discipline cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(disciplineTextField.getText().trim())) {

            alert.setContentText("Invalid discipline!");
            alert.show();
            return false;
        }
        if (durationTextField.getText() == null || durationTextField.getText().isEmpty()) {

            alert.setContentText("Duration cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateSemester(durationTextField.getText().trim())) {

            alert.setContentText("Invalid duration!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * This method initialize the columns of the Course TableView.
     */
    private void initCourseCol() {

        courseDeptNameCol.setCellValueFactory(new PropertyValueFactory<>("deptName"));
        courseCourseIdCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseDegreeCol.setCellValueFactory(new PropertyValueFactory<>("degree"));
        courseDisciplineCol.setCellValueFactory(new PropertyValueFactory<>("discipline"));
        courseDurationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }

    /**
     * This method populates/updates the Department Table.
     */
    @SuppressWarnings("Duplicates")
    private void populateCourseTable() {

        Task<List<Course>> coursesTask;
        coursesTask = courseService.getCoursesTask("");
        new Thread(coursesTask).start();

        coursesTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //get a ArrayList of courses and set the ObsList with it
                courseObsList.setAll(coursesTask.getValue());

                //set the tableview
                courseTableView.setItems(courseObsList);
            }
        });
    }

    /**
     * This method activates the loading spinner in the Add Course section.
     */
    private void activateAddCourseProgressIndicator() {

        addCourseMainGridPane.setOpacity(0.5);
        addCourseStatusStackPane.setVisible(true);
        addCourseProgressIndicator.setVisible(true);
    }

    /**
     * This method deactivates the loading spinner and displays the status
     * in the Add Course section.
     */
    private void deactivateAddCourseProgressIndicator() {

        addCourseProgressIndicator.setVisible(false);
        addCourseButtonsHbox.setVisible(true);
        addCourseStatusImageView.setVisible(true);
        addCourseStatusLabel.setVisible(true);
    }

    /**
     * This method activates the loading spinner in the Course List section.
     */
    private void activateCourseListProgressIndicator() {

        courseListMainGridPane.setOpacity(0.5);
        courseListStatusStackPane.setVisible(true);
        courseListProgressIndicator.setVisible(true);
    }

    /**
     * This method deactivates the loading spinner and activates the status
     * in the Course List section.
     */
    private void deactivateCourseListProgressIndicator() {

        courseListProgressIndicator.setVisible(false);
        courseListButtonsHbox.setVisible(true);
        courseListStatusImageView.setVisible(true);
        courseListStatusLabel.setVisible(true);
    }

    /**
     * This method deactivates the whole status stack pane,including the loading
     * spinner & status in the Add Course section.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateAddCourseStatusStackPane() {

        addCourseStatusStackPane.setVisible(false);
        addCourseProgressIndicator.setVisible(false);
        addCourseButtonsHbox.setVisible(false);
        addCourseStatusImageView.setVisible(false);
        addCourseStatusLabel.setVisible(false);
        addCourseMainGridPane.setOpacity(1);
    }

    /**
     * This method deactivates the whole status stack pane,including the loading
     * spinner & status in the Course List section.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateCourseListStatusStackPane() {

        courseListStatusStackPane.setVisible(false);
        courseListProgressIndicator.setVisible(false);
        courseListButtonsHbox.setVisible(false);
        courseListStatusImageView.setVisible(false);
        courseListStatusLabel.setVisible(false);
        courseListMainGridPane.setOpacity(1);
    }

    /*--------------------------------------------End of Courses Tab Operation----------------------------------------*/


    /*------------------------------------------------------------------------------------------------------------------
    Batches Tab operations:
    Add,edit,delete batch and view them in the table.
     -----------------------------------------------------------------------------------------------------------------*/

    /**
     * Initialization of Batches Tab : adds department to Dept Combo Box
     */
    @SuppressWarnings("Duplicates")
    private void initBatchesTab() {

        populateBatchTable();

        Task<List<Course>> coursesTask = courseService.getCoursesTask("");
        new Thread(coursesTask).start();

        coursesTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                listOfCourses = coursesTask.getValue();

                List<String> items = new ArrayList<>();
                for (Course course : listOfCourses) {

                    if (!items.contains(course.getDegree())) {
                        items.add(course.getDegree());
                    }
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                batchesDegreeComboBox.setItems(options);
            }
        });
    }


    /**
     * Callback method to handle action of Degree Combo Box : add discipline in discipline Combo Box
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleBatchesDegreeComboBox() {

        if (batchAddOrEditChoice != EDIT_CHOICE) {

            batchesDisciplineComboBox.getSelectionModel().clearSelection();
            batchesDisciplineComboBox.getItems().clear();

            List<String> items = new ArrayList<>();

            //Add degree names to the degreeComboBox
            for (Course course : listOfCourses) {

                if (course.getDegree().equals(batchesDegreeComboBox.getValue())) {

                    //add the disciplines to the discipline comboBox for particular degree
                    if (!items.contains(course.getDiscipline())) {

                        items.add(course.getDiscipline());
                    }
                }
            }
            ObservableList<String> options = FXCollections.observableArrayList(items);
            batchesDisciplineComboBox.setItems(options);
        }
    }

    /**
     * Callback method to handle action of Submit Button.
     * <p>
     * This method either adds a new Batch to the DB or updates an existing Batch in the DB.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitBatchesButtonAction() {

        String batchId = batchIdTextField.getText().trim();
        String batchName = batchNameTextField.getText().trim();

        //add or edit batch only when they are valid
        if (validateBatchItems()) {

            //display the loading spinner
            activateAddBatchProgressIndicator();

            Batch newBatch = new Batch();

            //initialize the newly created batch object
            newBatch.setBatchId(batchId);
            newBatch.setBatchName(batchName);
            newBatch.setDegree(batchesDegreeComboBox.getValue());
            newBatch.setDiscipline(batchesDisciplineComboBox.getValue());

            //'add batch' chosen
            if (batchAddOrEditChoice == ADD_CHOICE) {

                Task<Integer> addBatchToDatabaseTask = batchService
                        .getAddBatchToDatabaseTask(newBatch);
                new Thread(addBatchToDatabaseTask).start();

                addBatchToDatabaseTask.setOnSucceeded(new EventHandler<>() {

                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of insertion operation
                        int status = addBatchToDatabaseTask.getValue();

                        //add operation finished on database, disable loading spinner and show status
                        deactivateAddBatchProgressIndicator();

                        //display status
                        if (status == DATABASE_ERROR) {

                            addBatchStatusImageView.setImage(new Image("/png/critical error.png"));
                            addBatchStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            addBatchStatusImageView.setImage(new Image("/png/success.png"));
                            addBatchStatusLabel.setText("Batch added Successfully!");
                            populateBatchTable();
                        } else {

                            addBatchStatusImageView.setImage(new Image("/png/error.png"));
                            addBatchStatusLabel.setText("Batch already exists!");
                        }
                    }
                });
            }

            //'edit course' chosen
            else {

                //change the status to ADD_CHOICE, since editing is done
                batchAddOrEditChoice = ADD_CHOICE;

                Task<Integer> editBatchTask = batchService
                        .getUpdateBatchTask(newBatch);
                new Thread(editBatchTask).start();

                editBatchTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of update operation
                        int status = editBatchTask.getValue();

                        //edit operation on database finished, disable loading spinner
                        deactivateAddBatchProgressIndicator();

                        //display status
                        if (status == DATABASE_ERROR) {

                            addBatchStatusImageView.setImage(new Image("/png/critical error.png"));
                            addBatchStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            addBatchStatusImageView.setImage(new Image("/png/success.png"));
                            addBatchStatusLabel.setText("Batch Edited Successfully!");
                            populateBatchTable();
                        } else if (status == DATA_INEXISTENT_ERROR) {

                            addBatchStatusImageView.setImage(new Image("/png/error.png"));
                            addBatchStatusLabel.setText("Batch doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method to handle Reset button action.
     * <p>
     * This disables the progress indicator,status and clears the textfields and comboBoxes for addition of new
     * batch.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleBatchOkAndResetButtonAction() {

        deactivateAddBatchStatusStackPane();

        batchesDegreeComboBox.setDisable(false);
        batchesDegreeComboBox.setValue(null);
        batchesDisciplineComboBox.setDisable(false);
        batchesDisciplineComboBox.setValue(null);
        batchIdTextField.setDisable(false);
        batchIdTextField.clear();
        batchNameTextField.clear();
    }

    /**
     * Callback method to handle action of Edit Batch button.
     * <p>
     * This method basically sets the Batch detials to be edited in the respective textfields and comboBoxes and
     * sets the Edit Signal so that the {@link #handleSubmitBatchesButtonAction()} works on updating the batch.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleEditBatchButtonAction() {

        //get the selected batch from the table
        Batch batch = batchTableView.getSelectionModel().getSelectedItem();

        //Trigger edit only when a batch is selected in the table
        if (batch != null) {

            //set the choice to Edit Batch
            batchAddOrEditChoice = EDIT_CHOICE;

            //degree selection cannot be edited
            batchesDegreeComboBox.setDisable(true);

            //discipline section cannot be edited
            batchesDisciplineComboBox.setDisable(true);

            //batch id is immutable,as it is primary key
            batchIdTextField.setDisable(true);

            //sets the selection of the degree from the column to the combobox
            batchesDegreeComboBox.setValue(batch.getDegree());

            //sets th selection of the discipline from the column to the combobox
            batchesDisciplineComboBox.setValue(batch.getDiscipline());

            //sets the batch id from the column to the textfield
            batchIdTextField.setText(batch.getBatchId());

            //sets the batch name from the column to the textfield
            batchNameTextField.setText(batch.getBatchName());
        }
    }

    /**
     * Callback method to handle action of Delete Batch button.
     * This method deletes the selected Batch in table from the DB.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDeleteBatchButtonAction() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you really want to delete?");

        //get the selected batch from the table
        Batch batch = batchTableView.getSelectionModel().getSelectedItem();

        //Trigger delete only when a batch is selected in the table.
        if (batch != null) {

            alert.showAndWait();
            ButtonType result = alert.getResult();

            if (result == ButtonType.OK) {

                //display a loading spinner to indicate that delete is taking place
                activateBatchListProgressIndicator();

                Task<Integer> deleteBatchTask = batchService
                        .getDeleteBatchTask(batch.getBatchId());
                new Thread(deleteBatchTask).start();

                deleteBatchTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get delete operation status
                        int status = deleteBatchTask.getValue();

                        //delete operation finished on database, disable the loading spinner and show status
                        deactivateBatchListProgressIndicator();

                        //display status
                        if (status == DATABASE_ERROR) {

                            batchListStatusImageView.setImage(new Image("/png/critical error.png"));
                            batchListStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            batchListStatusImageView.setImage(new Image("/png/success.png"));
                            batchListStatusLabel.setText("Deleted Successfully!");
                            populateBatchTable();
                        } else if (status == DATA_DEPENDENCY_ERROR) {

                            batchListStatusImageView.setImage(new Image("/png/error.png"));
                            batchListStatusLabel.setText("Cannot delete batch!");
                        } else if (status == DATA_INEXISTENT_ERROR) {

                            batchListStatusImageView.setImage(new Image("/png/error.png"));
                            batchListStatusLabel.setText("Batch doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method for Ok button in the Batch List section's batchListStatusStackPane.
     * Brings the table back into focus.
     */
    @FXML
    private void handleBatchListOkButtonAction() {

        deactivateBatchListStatusStackPane();
    }

    /**
     * This method validates all textfields in the Batch Tab
     *
     * @return A boolean value indicating the validation result.
     */
    @SuppressWarnings("Duplicates")
    private boolean validateBatchItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);


        if (batchesDegreeComboBox.getValue() == null) {

            alert.setContentText("Please select a degree!");
            alert.show();
            return false;
        }
        if (batchesDisciplineComboBox.getValue() == null) {

            alert.setContentText("Please select a discipline!");
            alert.show();
            return false;
        }
        if (batchIdTextField.getText() == null || batchIdTextField.getText().isEmpty()) {

            alert.setContentText("Batch ID cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateId(batchIdTextField.getText().trim())) {

            alert.setContentText("Invalid Batch ID!");
            alert.show();
            return false;
        }
        if (batchNameTextField.getText() == null || batchNameTextField.getText().isEmpty()) {

            alert.setContentText("Batch Name cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateBatchName(batchNameTextField.getText().trim())) {

            alert.setContentText("Invalid Batch Name!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * This method initialize the columns of the Batch TableView.
     */
    private void initBatchCol() {

        batchesDeptNameCol.setCellValueFactory(new PropertyValueFactory<>("deptName"));
        batchesBatchIdCol.setCellValueFactory(new PropertyValueFactory<>("batchId"));
        batchesBatchNameCol.setCellValueFactory(new PropertyValueFactory<>("batchName"));
        batchesDegreeCol.setCellValueFactory(new PropertyValueFactory<>("degree"));
        batchesDisciplineCol.setCellValueFactory(new PropertyValueFactory<>("discipline"));
    }

    /**
     * This method populates/updates the Batch Table.
     */
    @SuppressWarnings("Duplicates")
    private void populateBatchTable() {

        Task<List<Batch>> batchesTask = batchService.getBatchesTask("");
        new Thread(batchesTask).start();

        batchesTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //get a ArrayList of batches and set the ObsList with it
                batchObsList.setAll(batchesTask.getValue());

                //set the tableView
                batchTableView.setItems(batchObsList);
            }
        });
    }

    /**
     * This method activates the loading spinner in the Add Batch section.
     */
    private void activateAddBatchProgressIndicator() {

        addBatchMainGridPane.setOpacity(0.5);
        addBatchStatusStackPane.setVisible(true);
        addBatchProgressIndicator.setVisible(true);
    }

    /**
     * This method deactivates the loading spinner and displays the status
     * in the Add Batch section.
     */
    private void deactivateAddBatchProgressIndicator() {

        addBatchProgressIndicator.setVisible(false);
        addBatchButtonHbox.setVisible(true);
        addBatchStatusImageView.setVisible(true);
        addBatchStatusLabel.setVisible(true);
    }

    /**
     * This method activates the loading spinner in the Batch List section.
     */
    private void activateBatchListProgressIndicator() {

        batchListMainGridPane.setOpacity(0.5);
        batchListStatusStackPane.setVisible(true);
        batchListProgressIndicator.setVisible(true);
    }

    /**
     * This method deactivates the loading spinner and activates the status
     * in the Batch List section.
     */
    private void deactivateBatchListProgressIndicator() {

        batchListProgressIndicator.setVisible(false);
        batchListButtonHbox.setVisible(true);
        batchListStatusImageView.setVisible(true);
        batchListStatusLabel.setVisible(true);
    }

    /**
     * This method deactivates the whole status stack pane,including the loading
     * spinner & status in the Add Batch section.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateAddBatchStatusStackPane() {

        addBatchStatusStackPane.setVisible(false);
        addBatchProgressIndicator.setVisible(false);
        addBatchButtonHbox.setVisible(false);
        addBatchStatusImageView.setVisible(false);
        addBatchStatusLabel.setVisible(false);
        addBatchMainGridPane.setOpacity(1);
    }

    /**
     * This method deactivates the whole status stack pane,including the loading
     * spinner & status in the Batch List section.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateBatchListStatusStackPane() {

        batchListStatusStackPane.setVisible(false);
        batchListProgressIndicator.setVisible(false);
        batchListButtonHbox.setVisible(false);
        batchListStatusImageView.setVisible(false);
        batchListStatusLabel.setVisible(false);
        batchListMainGridPane.setOpacity(1);
    }


    /*------------------------------------------End of Batches tab Operation------------------------------------------*/


    /*-----------------------------------------------------------------------------------------------------------------
    Subjects Tab operations:
    Add,edit,delete subject and view them in the table.
     -----------------------------------------------------------------------------------------------------------------*/

    /**
     * Initialization of Subjects Tab : adds degree to Degree Combo Box
     */
    @SuppressWarnings("Duplicates")
    private void initSubjectsTab() {

        populateSubjectTable();

        subjectTypeChoiceBox.setItems(FXCollections.observableArrayList("Theory", "Practical"));

        Task<List<Course>> coursesTask = courseService.getCoursesTask("");
        new Thread(coursesTask).start();

        coursesTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                listOfCourses = coursesTask.getValue();

                List<String> items = new ArrayList<>();

                //Add degree to the degreeComboBox
                for (Course course : listOfCourses) {

                    //add only unique degree items to degree combobox
                    if (!items.contains(course.getDegree())) {

                        items.add(course.getDegree());
                    }
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                subjectsDegreeComboBox.setItems(options);
            }
        });
    }

    /**
     * Callback method to handle action of Degree Combo Box : add discipline in discipline Combo Box
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubjectsDegreeComboBox() {

        if (subjectAddOrEditChoice != EDIT_CHOICE) {

            subjectsDisciplineComboBox.getSelectionModel().clearSelection();
            subjectsDisciplineComboBox.getItems().clear();

            if (subjectsDegreeComboBox.getValue() != null) {

                List<String> items = new ArrayList<>();
                for (Course course : listOfCourses) {

                    if (course.getDegree().equals(subjectsDegreeComboBox.getValue()))

                        //add the disciplines to the discipline comboBox for particular degree
                        if (!items.contains(course.getDiscipline()))
                            items.add(course.getDiscipline());
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                subjectsDisciplineComboBox.setItems(options);
            }
        }
    }

    /**
     * Callback method to handle action of Semester Combo Box : add semester in semester Combo Box
     */
    @FXML
    private void handleSubjectsDisciplineComboBox() {

        if (subjectAddOrEditChoice != EDIT_CHOICE) {

            subjectsSemesterComboBox.getSelectionModel().clearSelection();
            subjectsSemesterComboBox.getItems().clear();

            if (subjectsDisciplineComboBox.getValue() != null) {

                int duration = 0;

                for (Course course : listOfCourses) {

                    if (course.getDegree().equals(subjectsDegreeComboBox.getValue())
                            && course.getDiscipline().equals(subjectsDisciplineComboBox.getValue())) {

                        duration = Integer.parseInt(course.getDuration());
                    }
                }

                List<String> items = new ArrayList<>();
                for (int loop = 1; loop <= duration; loop++) {

                    items.add(Integer.toString(loop));
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                subjectsSemesterComboBox.setItems(options);
            }
        }
    }

    /**
     * Callback method to handle action of Submit Button.
     * <p>
     * This method either adds a new Subject to the DB or updates an existing Subject in the DB.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitSubjectButtonAction() {

        String semester = subjectsSemesterComboBox.getValue();
        String subId = subjectIdTextField.getText().trim();
        String subName = subjectNameTextField.getText().trim();
        String subType = subjectTypeChoiceBox.getValue();
        String credit = creditTextField.getText().trim();
        String fullMarks = fullMarksTextField.getText().trim();
        String degree = subjectsDegreeComboBox.getValue();
        String discipline = subjectsDisciplineComboBox.getValue();

        //add or edit subjects only when they are valid
        if (validateSubjectItems()) {

            //display the loading spinner
            activateAddSubjectProgressIndicator();

            Subject newSubject = new Subject();

            //initialize the newly created subject object
            newSubject.setSemester(semester);
            newSubject.setSubId(subId);
            newSubject.setSubName(subName);
            newSubject.setSubType(subType);
            newSubject.setCredit(credit);
            newSubject.setFullMarks(fullMarks);
            newSubject.setDegree(degree);
            newSubject.setDiscipline(discipline);

            //'add subject' chosen
            if (subjectAddOrEditChoice == ADD_CHOICE) {

                Task<Integer> addSubjectToDatabaseTask = subjectService
                        .getAddSubjectToDatabaseTask(newSubject);
                new Thread(addSubjectToDatabaseTask).start();

                addSubjectToDatabaseTask.setOnSucceeded(new EventHandler<>() {

                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of insertion operation
                        int status = addSubjectToDatabaseTask.getValue();

                        //add operation finished on database, disable loading spinner and show status
                        deactivateAddSubjectProgressIndicator();


                        //display status
                        if (status == DATABASE_ERROR) {

                            addSubjectsStatusImageView.setImage(new Image("/png/critical error.png"));
                            addSubjectsStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            addSubjectsStatusImageView.setImage(new Image("/png/success.png"));
                            addSubjectsStatusLabel.setText("Subject added Successfully!");
                            populateSubjectTable();
                        } else {

                            addSubjectsStatusImageView.setImage(new Image("/png/error.png"));
                            addSubjectsStatusLabel.setText("Subject already exists!");
                        }
                    }
                });
            }

            //'edit subject' chosen
            else {

                //change the status to ADD_CHOICE, since editing is done
                subjectAddOrEditChoice = ADD_CHOICE;

                Task<Integer> editSubjectTask = subjectService
                        .getUpdateSubjectTask(newSubject);
                new Thread(editSubjectTask).start();

                editSubjectTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of update operation
                        int status = editSubjectTask.getValue();

                        //edit operation on database finished, disable loading spinner
                        deactivateAddSubjectProgressIndicator();

                        //display status
                        if (status == DATABASE_ERROR) {

                            addSubjectsStatusImageView.setImage(new Image("/png/critical error.png"));
                            addSubjectsStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            addSubjectsStatusImageView.setImage(new Image("/png/success.png"));
                            addSubjectsStatusLabel.setText("Subject Edited Successfully!");
                            populateSubjectTable();
                        } else if (status == DATA_INEXISTENT_ERROR) {

                            addSubjectsStatusImageView.setImage(new Image("/png/error.png"));
                            addSubjectsStatusLabel.setText("Subject doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method to handle Reset button action.
     * <p>
     * This disables the progress indicator,status and clears the textfields and comboBoxes for addition of new
     * subject.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubjectsOkAndResetButtonAction() {

        deactivateAddSubjectStatusStackPane();
        subjectsDegreeComboBox.setDisable(false);
        subjectsDegreeComboBox.setValue(null);
        subjectsDisciplineComboBox.setDisable(false);
        subjectsDisciplineComboBox.setValue(null);
        subjectsSemesterComboBox.setDisable(false);
        subjectsSemesterComboBox.setValue(null);
        subjectTypeChoiceBox.setDisable(false);
        subjectTypeChoiceBox.setValue(null);
        subjectIdTextField.setDisable(false);
        subjectIdTextField.clear();
        subjectNameTextField.clear();
        creditTextField.clear();
        fullMarksTextField.clear();
    }

    /**
     * Callback method to handle action of Edit Subject button.
     * <p>
     * This method basically sets the Subject details to be edited in the respective textfields and comboBoxes and
     * sets the Edit Signal so that the {@link #handleSubmitSubjectButtonAction()} works on updating the subject.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleEditSubjectButtonAction() {
        //get the selected subject from the table
        Subject subject = subjectsTableView.getSelectionModel().getSelectedItem();

        //Trigger edit only when a subject is selected in the table
        if (subject != null) {

            //set the choice to Edit Subject
            subjectAddOrEditChoice = EDIT_CHOICE;

            //degree selection cannot be edited
            subjectsDegreeComboBox.setDisable(true);

            //discipline section cannot be edited
            subjectsDisciplineComboBox.setDisable(true);

            //subject id is immutable,as it is primary key
            subjectIdTextField.setDisable(true);

            //sets the subject id from the column to the textfield
            subjectIdTextField.setText(subject.getSubId());

            //sets the selection of the degree from the column to the combobox
            subjectsDegreeComboBox.setValue(subject.getDegree());

            //sets the selection of the discipline from the column to the combobox
            subjectsDisciplineComboBox.setValue(subject.getDiscipline());

            //sets the selection of the semester from the column to the combobox
            subjectsSemesterComboBox.setValue(subject.getSemester());

            //sets the subject name from the column to the textfield
            subjectNameTextField.setText(subject.getSubName());

            //sets the selection of the subject type from the column to the combobox
            subjectTypeChoiceBox.setValue(subject.getSubType());

            //sets the credit from the column to the textfield
            creditTextField.setText(subject.getCredit());

            //sets the full marks from the column to the combobox
            fullMarksTextField.setText(subject.getFullMarks());
        }
    }

    /**
     * Callback method to handle action of Delete Subject button.
     * This method deletes the selected Subject in table from the DB.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDeleteSubjectButtonAction() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you really want to delete?");

        //get the selected subject from the table
        Subject subject = subjectsTableView.getSelectionModel().getSelectedItem();

        //Trigger delete only when a subject is selected in the table.
        if (subject != null) {

            alert.showAndWait();
            ButtonType result = alert.getResult();

            if (result == ButtonType.OK) {

                //display a loading spinner to indicate that delete is taking place
                activateSubjectListProgressIndicator();

                Task<Integer> deleteSubjectTask = subjectService
                        .getDeleteSubjectTask(subject.getSubId());
                new Thread(deleteSubjectTask).start();

                deleteSubjectTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get delete operation status
                        int status = deleteSubjectTask.getValue();

                        //delete operation finished on database, disable the loading spinner and show status
                        deactivateSubjectListProgressIndicator();

                        //display status
                        if (status == DATABASE_ERROR) {

                            subjectsListStatusImageView.setImage(new Image("/png/critical error.png"));
                            subjectsListStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            subjectsListStatusImageView.setImage(new Image("/png/success.png"));
                            subjectsListStatusLabel.setText("Deleted Successfully!");
                            populateSubjectTable();
                        } else if (status == DATA_DEPENDENCY_ERROR) {

                            subjectsListStatusImageView.setImage(new Image("/png/error.png"));
                            subjectsListStatusLabel.setText("Cannot delete subject!");
                        } else if (status == DATA_INEXISTENT_ERROR) {

                            subjectsListStatusImageView.setImage(new Image("/png/error.png"));
                            subjectsListStatusLabel.setText("Subject doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method for Ok button in the Subject List section's subjectsListStatusStackPane.
     * Brings the table back into focus.
     */
    @FXML
    private void handleSubjectsListOkButtonAction() {

        deactivateSubjectListStatusStackPane();
    }

    /**
     * This method validates all textfields in the Subject Tab
     *
     * @return A boolean value indicating the validation result.
     */
    @SuppressWarnings("Duplicates")
    private boolean validateSubjectItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (subjectsDegreeComboBox.getValue() == null) {

            alert.setContentText("Please select a degree!");
            alert.show();
            return false;
        }
        if (subjectsDisciplineComboBox.getValue() == null) {

            alert.setContentText("Please select a discipline!");
            alert.show();
            return false;
        }
        if (subjectsSemesterComboBox.getValue() == null) {

            alert.setContentText("Please select a Semester!");
            alert.show();
            return false;
        }
        if (subjectIdTextField.getText() == null || subjectIdTextField.getText().isEmpty()) {

            alert.setContentText("Subject Id cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateId(subjectIdTextField.getText().trim())) {

            alert.setContentText("Invalid Subject Id!");
            alert.show();
            return false;
        }
        if (subjectNameTextField.getText() == null || subjectNameTextField.getText().isEmpty()) {

            alert.setContentText("Subject Name cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateName(subjectNameTextField.getText().trim())) {

            alert.setContentText("Invalid Subject Name!");
            alert.show();
            return false;
        }
        if (subjectTypeChoiceBox.getValue() == null) {

            alert.setContentText("Subject Type cannot be empty!");
            alert.show();
            return false;
        }
        if (creditTextField.getText() == null || creditTextField.getText().isEmpty()) {

            alert.setContentText("Credit cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateNumber(creditTextField.getText().trim())) {

            alert.setContentText("Invalid Subject Credit!");
            alert.show();
            return false;
        }
        if (fullMarksTextField.getText() == null || fullMarksTextField.getText().isEmpty()) {

            alert.setContentText("Full Marks cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateNumber(fullMarksTextField.getText().trim())) {

            alert.setContentText("Invalid Full Marks!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * This method initialize the columns of the Subject TableView.
     */
    private void initSubjectCol() {

        subSubIdCol.setCellValueFactory(new PropertyValueFactory<>("subId"));
        subDegreeCol.setCellValueFactory(new PropertyValueFactory<>("degree"));
        subDisciplineCol.setCellValueFactory(new PropertyValueFactory<>("discipline"));
        subSemesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        subSubNameCol.setCellValueFactory(new PropertyValueFactory<>("subName"));
        subSubTypeCol.setCellValueFactory(new PropertyValueFactory<>("subType"));
        subCreditCol.setCellValueFactory(new PropertyValueFactory<>("credit"));
        subFullMarksCol.setCellValueFactory(new PropertyValueFactory<>("fullMarks"));
    }

    /**
     * This method populates/updates the Subject Table.
     */
    @SuppressWarnings("Duplicates")
    private void populateSubjectTable() {

        Task<List<Subject>> subjectTask;
        subjectTask = subjectService.getSubjectsTask("");
        new Thread(subjectTask).start();

        subjectTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //get a ArrayList of subjects and set the ObsList with it

                subjectObsList.setAll(subjectTask.getValue());

                //set the tableview
                subjectsTableView.setItems(subjectObsList);
            }
        });
    }

    /**
     * This method activates the loading spinner in the Add Subject section.
     */
    private void activateAddSubjectProgressIndicator() {

        addSubjectsMainGridPane.setOpacity(0.5);
        addSubjectsStatusStackPane.setVisible(true);
        addSubjectsProgressIndicator.setVisible(true);
    }

    /**
     * This method deactivates the loading spinner and displays the status
     * in the Add Subject section.
     */
    private void deactivateAddSubjectProgressIndicator() {

        addSubjectsProgressIndicator.setVisible(false);
        addSubjectsButtonHbox.setVisible(true);
        addSubjectsStatusImageView.setVisible(true);
        addSubjectsStatusLabel.setVisible(true);
    }

    /**
     * This method activates the loading spinner in the Subject List section.
     */
    private void activateSubjectListProgressIndicator() {

        subjectsListMainGridPane.setOpacity(0.5);
        subjectsListStatusStackPane.setVisible(true);
        subjectsListProgressIndicator.setVisible(true);
    }

    /**
     * This method deactivates the loading spinner and activates the status
     * in the Subjects List section.
     */
    private void deactivateSubjectListProgressIndicator() {

        subjectsListProgressIndicator.setVisible(false);
        subjectsListButtonHbox.setVisible(true);
        subjectsListStatusImageView.setVisible(true);
        subjectsListStatusLabel.setVisible(true);
    }

    /**
     * This method deactivates the whole status stack pane,including the loading
     * spinner & status in the Add Subject section.
     */
    private void deactivateAddSubjectStatusStackPane() {

        addSubjectsStatusStackPane.setVisible(false);
        addSubjectsProgressIndicator.setVisible(false);
        addSubjectsButtonHbox.setVisible(false);
        addSubjectsStatusImageView.setVisible(false);
        addSubjectsStatusLabel.setVisible(false);
        addSubjectsMainGridPane.setOpacity(1);
    }

    /**
     * This method deactivates the whole status stack pane,including the loading
     * spinner & status in the Subjects List section.
     */
    private void deactivateSubjectListStatusStackPane() {

        subjectsListStatusStackPane.setVisible(false);
        subjectsListProgressIndicator.setVisible(false);
        subjectsListButtonHbox.setVisible(false);
        subjectsListStatusImageView.setVisible(false);
        subjectsListStatusLabel.setVisible(false);
        subjectsListMainGridPane.setOpacity(1);
    }

    
    /*--------------------------------------------------End of Subjects Tab operation---------------------------------*/


    /*------------------------------------------------------------------------------------------------------------------
    Classrooms Tab operations:
    Add,edit,delete classroom and view them in the table.
     -----------------------------------------------------------------------------------------------------------------*/

    /**
     * Initialization of Classrooms Tab : adds degree to Degree Combo Box
     */
    @SuppressWarnings("Duplicates")
    private void initClassroomsTab() {

        populateClassroomTable();
    }

    /**
     * Callback method to handle action of Submit Button.
     * <p>
     * This method either adds a new Classroom to the DB or updates an existing Classroom in the DB.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleClassroomSubmitButtonAction() {

        String roomNo = roomNoTextField.getText().trim();
        String capacity = capacityTextField.getText().trim();
        String row = rowTextField.getText().trim();
        String column = columnTextField.getText().trim();

        //add or edit classroom only when they are valid
        if (validateClassroomItems()) {

            //display the loading spinner
            activateAddClassroomProgressIndicator();

            Classroom newClassroom = new Classroom();

            //initialize the newly created classroom object
            newClassroom.setRoomNo(roomNo);
            newClassroom.setCapacity(capacity);
            newClassroom.setNoOfRows(row);
            newClassroom.setNoOfCols(column);

            //'add classroom' chosen
            if (classroomAddOrEditChoice == ADD_CHOICE) {

                Task<Integer> addClassroomToDatabaseTask = classroomService
                        .getAddClassroomToDatabaseTask(newClassroom);
                new Thread(addClassroomToDatabaseTask).start();

                addClassroomToDatabaseTask.setOnSucceeded(new EventHandler<>() {

                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of insertion operation
                        int status = addClassroomToDatabaseTask.getValue();

                        //add operation finished on database, disable loading spinner and show status
                        deactivateAddClassroomProgressIndicator();


                        //display status
                        if (status == DATABASE_ERROR) {

                            addClassroomStatusImageView.setImage(new Image("/png/critical error.png"));
                            addClassroomStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            addClassroomStatusImageView.setImage(new Image("/png/success.png"));
                            addClassroomStatusLabel.setText("Classroom added Successfully!");
                            populateClassroomTable();
                        } else {

                            addClassroomStatusImageView.setImage(new Image("/png/error.png"));
                            addClassroomStatusLabel.setText("Classroom already exists!");
                        }
                    }
                });
            }

            //'edit classroom' chosen
            else {

                //change the status to ADD_CHOICE, since editing is done
                classroomAddOrEditChoice = ADD_CHOICE;

                Task<Integer> editClassroomTask = classroomService
                        .getUpdateClassroomTask(newClassroom);
                new Thread(editClassroomTask).start();

                editClassroomTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of update operation
                        int status = editClassroomTask.getValue();

                        //edit operation on database finished, disable loading spinner
                        deactivateAddClassroomProgressIndicator();

                        //display status
                        if (status == DATABASE_ERROR) {

                            addClassroomStatusImageView.setImage(new Image("/png/critical error.png"));
                            addClassroomStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            addClassroomStatusImageView.setImage(new Image("/png/success.png"));
                            addClassroomStatusLabel.setText("Classroom Edited Successfully!");
                            populateClassroomTable();
                        } else if (status == DATA_INEXISTENT_ERROR) {

                            addClassroomStatusImageView.setImage(new Image("/png/error.png"));
                            addClassroomStatusLabel.setText("Classroom doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method to handle Reset button action.
     * <p>
     * This disables the progress indicator,status and clears the textfields and comboBoxes for addition of new
     * classroom.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleClassroomOkAndResetButtonAction() {

        deactivateAddClassroomStatusStackPane();
        roomNoTextField.setDisable(false);
        roomNoTextField.clear();
        capacityTextField.clear();
        rowTextField.clear();
        columnTextField.clear();
    }

    /**
     * Callback method to handle action of Edit Classroom button.
     * <p>
     * This method basically sets the Classroom details to be edited in the respective textfields and comboBoxes and
     * sets the Edit Signal so that the {@link #handleClassroomSubmitButtonAction()} works on updating the classroom.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleEditClassroomButtonAction() {

        //get the selected classroom from the table
        Classroom classroom = classroomsTableView.getSelectionModel().getSelectedItem();

        //Trigger edit only when a classroom is selected in the table
        if (classroom != null) {

            //set the choice to Edit Classroom
            classroomAddOrEditChoice = EDIT_CHOICE;

            //room no is immutable,as it is primary key
            roomNoTextField.setDisable(true);

            //sets the room no from the column to the textfield
            roomNoTextField.setText(classroom.getRoomNo());

            //sets the capacity from the column to the textfield
            capacityTextField.setText(classroom.getCapacity());

            //sets the row n from  the column to the textfield
            rowTextField.setText(classroom.getNoOfRows());

            //sets the column no from the column to the textfield
            columnTextField.setText(classroom.getNoOfCols());
        }
    }

    /**
     * Callback method to handle action of Delete Classroom button.
     * This method deletes the selected Classroom in table from the DB.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDeleteClassroomButtonAction() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you really want to delete?");

        //get the selected classroom from the table
        Classroom classroom = classroomsTableView.getSelectionModel().getSelectedItem();

        //Trigger delete only when a classroom is selected in the table.
        if (classroom != null) {

            alert.showAndWait();
            ButtonType result = alert.getResult();

            if (result == ButtonType.OK) {

                //display a loading spinner to indicate that delete is taking place
                activateClassroomListProgressIndicator();

                Task<Integer> deleteClassroomTask = classroomService
                        .getDeleteClassroomTask(classroom.getRoomNo());
                new Thread(deleteClassroomTask).start();

                deleteClassroomTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get delete operation status
                        int status = deleteClassroomTask.getValue();

                        //delete operation finished on database, disable the loading spinner and show status
                        deactivateClassroomListProgressIndicator();

                        //display status
                        if (status == DATABASE_ERROR) {

                            classroomListStatusImageView.setImage(new Image("/png/critical error.png"));
                            classroomListStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            classroomListStatusImageView.setImage(new Image("/png/success.png"));
                            classroomListStatusLabel.setText("Deleted Successfully!");
                            populateClassroomTable();
                        } else if (status == DATA_DEPENDENCY_ERROR) {

                            classroomListStatusImageView.setImage(new Image("/png/error.png"));
                            classroomListStatusLabel.setText("Cannot delete classroom!");
                        } else if (status == DATA_INEXISTENT_ERROR) {

                            classroomListStatusImageView.setImage(new Image("/png/error.png"));
                            classroomListStatusLabel.setText("Classroom doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method for Ok button in the Classroom List section's classroomListStatusStackPane.
     * Brings the table back into focus.
     */
    @FXML
    private void handleClassroomListOkButtonAction() {

        deactivateClassroomListStatusStackPane();
    }

    /**
     * This method validates all textfields in the Classroom Tab
     *
     * @return A boolean value indicating the validation result.
     */
    @SuppressWarnings("Duplicates")
    private boolean validateClassroomItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (roomNoTextField.getText() == null || roomNoTextField.getText().isEmpty()) {

            alert.setContentText("Room No cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateNumber(roomNoTextField.getText().trim())) {

            alert.setContentText("Invalid Room no!");
            alert.show();
            return false;
        } else if (capacityTextField.getText() == null || capacityTextField.getText().isEmpty()) {

            alert.setContentText("Room Capacity cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateNumber(capacityTextField.getText().trim())) {

            alert.setContentText("Invalid Room Capacity!");
            alert.show();
            return false;
        } else if (rowTextField.getText() == null || rowTextField.getText().isEmpty()) {

            alert.setContentText("No of Row cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateNumber(rowTextField.getText().trim())) {

            alert.setContentText("Invalid No of Row!");
            alert.show();
            return false;
        } else if (columnTextField.getText() == null || columnTextField.getText().isEmpty()) {

            alert.setContentText("No of Column cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateNumber(columnTextField.getText().trim())) {

            alert.setContentText("Invalid No of Column!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * This method initialize the columns of the Classroom TableView.
     */
    private void initClassroomCol() {

        classroomRoomNoCol.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        classroomCapacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        classroomRowCol.setCellValueFactory(new PropertyValueFactory<>("noOfRows"));
        classroomColumnCol.setCellValueFactory(new PropertyValueFactory<>("noOfCols"));
    }

    /**
     * This method populates/updates the Classroom Table.
     */
    @SuppressWarnings("Duplicates")
    private void populateClassroomTable() {

        Task<List<Classroom>> classroomTask;
        classroomTask = classroomService.getClassroomTask("");
        new Thread(classroomTask).start();

        classroomTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //get a ArrayList of classrooms and set the ObsList with it
                classroomObsList.setAll(classroomTask.getValue());

                //set the tableview
                classroomsTableView.setItems(classroomObsList);
            }
        });
    }

    /**
     * This method activates the loading spinner in the Add Classroom section.
     */
    private void activateAddClassroomProgressIndicator() {

        addClassroomMainGridPane.setOpacity(0.5);
        addClassroomStatusStackPane.setVisible(true);
        addClassroomProgressIndicator.setVisible(true);
    }

    /**
     * This method deactivates the loading spinner and displays the status
     * in the Add Classroom section.
     */
    private void deactivateAddClassroomProgressIndicator() {

        addClassroomProgressIndicator.setVisible(false);
        addClassroomButtonHbox.setVisible(true);
        addClassroomStatusImageView.setVisible(true);
        addClassroomStatusLabel.setVisible(true);
    }

    /**
     * This method activates the loading spinner in the Classroom List section.
     */
    private void activateClassroomListProgressIndicator() {

        classroomListMainGridPane.setOpacity(0.5);
        classroomListStatusStackPane.setVisible(true);
        classroomListProgressIndicator.setVisible(true);
    }

    /**
     * This method deactivates the loading spinner and activates the status
     * in the Classroom List section.
     */
    private void deactivateClassroomListProgressIndicator() {

        classroomListProgressIndicator.setVisible(false);
        classroomListButtonHbox.setVisible(true);
        classroomListStatusImageView.setVisible(true);
        classroomListStatusLabel.setVisible(true);
    }

    /**
     * This method deactivates the whole status stack pane,including the loading
     * spinner & status in the Add Classroom section.
     */
    private void deactivateAddClassroomStatusStackPane() {

        addClassroomStatusStackPane.setVisible(false);
        addClassroomProgressIndicator.setVisible(false);
        addClassroomButtonHbox.setVisible(false);
        addClassroomStatusImageView.setVisible(false);
        addClassroomStatusLabel.setVisible(false);
        addClassroomMainGridPane.setOpacity(1);
    }

    /**
     * This method deactivates the whole status stack pane,including the loading
     * spinner & status in the Classroom List section.
     */
    private void deactivateClassroomListStatusStackPane() {

        classroomListStatusStackPane.setVisible(false);
        classroomListProgressIndicator.setVisible(false);
        classroomListButtonHbox.setVisible(false);
        classroomListStatusImageView.setVisible(false);
        classroomListStatusLabel.setVisible(false);
        classroomListMainGridPane.setOpacity(1);
    }

    /*------------------------------------------End of Class Rooms tab operation--------------------------------------*/
}
