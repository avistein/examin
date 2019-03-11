package controller;

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
import model.Course;
import model.Department;
import service.CourseService;
import service.DepartmentService;

import java.util.ArrayList;
import java.util.List;

import static util.ConstantsUtil.*;

/**
 * Controller class for AcademicAdministration.fxml
 *
 * @author Avik Sarkar
 */
public class AcademicAdministrationController {

    /*------------------Declaration & initialization of items common to all tabs-----------*/

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

    /*--------------------------End of initialization----------------------------------------*/


    /*-------------Initialization and declaration of Department tab's components.------------*/

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

    /*---------------End of initialization of Department's tab components-----------------*/


    /*----------Initialization and declaration of Courses tab's components----------------*/

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

    /*-----------------End of initialization of Courses tab's components------------------*/


    /**
     *
     */
    @FXML
    private void initialize() {
        deptAddOrEditChoice = ADD_CHOICE;
        courseAddOrEditChoice = ADD_CHOICE;

        departmentService = new DepartmentService();
        departmentObsList = FXCollections.observableArrayList();

        courseService = new CourseService();
        courseObsList = FXCollections.observableArrayList();

        initDeptCol();
        populateDeptTable();

        initCourseCol();

        //initialize the respective tabs if it is selected
        academicAdministrationTabPane.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                        if (newValue == departmentsTab) {
                            initDepartmentsTab();
                        } else if (newValue == coursesTab) {
                            initCoursesTab();
                        } else if (newValue == batchesTab) {

                        } else if (newValue == subjectsTab) {

                        } else if (newValue == classRoomsTab) {

                        }
                    }
                });
    }


    /*-----------------End of initialization of items--------------------*/


    /*--------------------------------------------------------------------
    Departments Tab operations:
    Add,edit,delete department and view them in the table.
     ---------------------------------------------------------------------*/

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
//        else if(ValidatorUtil.validateDeptName(deptNameTextfield.getText())){
//
//            alert.setContentText("Invalid department name!");
//            alert.show();
//            return false;
//        }
        if (buildingNameTextField.getText() == null || buildingNameTextField.getText().isEmpty()) {

            alert.setContentText("Building Name cannot be empty!");
            alert.show();
            return false;
        }
//        else if(ValidatorUtil.validateDeptName(buildingNameTextField.getText())){
//
//            alert.setContentText("Invalid building name!");
//            alert.show();
//            return false;
//        }
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

    /*----------------End of Department Tabs operations-----------------*/


    /*------------------------------------------------------------------
    Courses Tab operations:
    Add,edit,delete course and view them in the table.
     ------------------------------------------------------------------*/

    /**
     * Initialization of Courses Tab
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

                        int status = addCourseToDatabaseTask.getValue();

                        //add operation finished on database, disable loading spinner and show status
                        deactivateAddCourseProgressIndicator();

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

                courseAddOrEditChoice = ADD_CHOICE;

                Task<Integer> editCourseTask = courseService
                        .getUpdateCourseTask(newCourse);
                new Thread(editCourseTask).start();

                editCourseTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        int status = editCourseTask.getValue();

                        //edit operation on database finished, disable loading spinner
                        deactivateAddCourseProgressIndicator();

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

                        int status = deleteCourseTask.getValue();

                        //delete operation finished on database, disable the loading spinner and show status
                        deactivateCourseListProgressIndicator();

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
        } else if (courseIdTextField.getText() == null || courseIdTextField.getText().isEmpty()) {

            alert.setContentText("Course ID cannot be empty!");
            alert.show();
            return false;
        }
//        else if(ValidatorUtil.validateDeptName(courseIdTextField.getText().trim())){
//
//            alert.setContentText("Invalid Course ID!");
//            alert.show();
//            return false;
//        }
        else if (degreeTextField.getText() == null || degreeTextField.getText().isEmpty()) {

            alert.setContentText("Degree cannot be empty!");
            alert.show();
            return false;
        }
//        else if(ValidatorUtil.validateDeptName(degreeTextField.getText().trim())){
//
//            alert.setContentText("Invalid degree!");
//            alert.show();
//            return false;
//        }
        else if (disciplineTextField.getText() == null || disciplineTextField.getText().isEmpty()) {

            alert.setContentText("Discipline cannot be empty!");
            alert.show();
            return false;
        }
//        else if (disciplineTextField.getText() == null || degreeTextField.getText().isEmpty()) {
//
//            alert.setContentText("Invalid discipline!");
//            alert.show();
//            return false;
//        }
        else if (durationTextField.getText() == null || durationTextField.getText().isEmpty()) {

            alert.setContentText("Duration cannot be empty!");
            alert.show();
            return false;
        }
//        else if (degreeTextField.getText() == null || degreeTextField.getText().isEmpty()) {
//
//            alert.setContentText("Invalid duration!");
//            alert.show();
//            return false;
//        }
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

    /*------------------End of Courses Tab Operation---------------------*/


    /*--------------------------------------------------------------------
    Batches Tab operations:
    Add,edit,delete batch and view them in the table.
     --------------------------------------------------------------------*/

    @FXML
    private void handleBatchesDeptNameComboBox() {

    }

    @FXML
    private void handleBatchesDegreeComboBox() {

    }

    @FXML
    private void handleBatchesDisciplineComboBox() {

    }

    @FXML
    private void handleSubmitBatchesButtonAction() {

    }

    @FXML
    private void handleResetBatchesButtonAction() {

    }

    @FXML
    private void handleEditBatchButtonAction() {

    }

    @FXML
    private void handleDeleteBatchButtonAction() {

    }

    /*--------------End of Batches tab Operation----------------------*/


    /*-----------------------------------------------------------------
    Subjects Tab operations:
    Add,edit,delete subject and view them in the table.
     ------------------------------------------------------------------*/

    @FXML
    private void handleSubTabDegreeComboBox() {

    }

    @FXML
    private void handleSubTabDisciplineComboBox() {

    }

    @FXML
    private void handleSubTabProfNameComboBox() {

    }

    @FXML
    private void handleResetSubButtonAction() {

    }

    @FXML
    private void handleSubmitSubjectButtonAction() {

    }

    @FXML
    private void handleEditSubjectButtonAction() {

    }

    @FXML
    private void handleDeleteSubjectButtonAction() {

    }

    /*-------------------End of Subjects Tab operation--------------------*/


    /*---------------------------------------------------------------------
    Classrooms Tab operations:
    Add,edit,delete classroom and view them in the table.
     ----------------------------------------------------------------------*/

    @FXML
    private void handleClassRoomDegreeComboBox() {

    }

    @FXML
    private void handleClassRoomDisciplineComboBox() {

    }

    @FXML
    private void handleSubmitClassroomButtonAction() {

    }

    @FXML
    private void handleResetClassroomsButtonAction() {

    }

    @FXML
    private void handleEditClassRoomButtonAction() {

    }

    @FXML
    private void handleDeleteClassRoomButtonAction() {

    }

    /*-------------------End of Class Rooms tab operation-----------------*/

}
