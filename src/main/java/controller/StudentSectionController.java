package controller;

import command.PromotionCommand;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Batch;
import model.Course;
import model.Student;
import service.BatchService;
import service.CourseService;
import service.StudentService;
import util.UISetterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static util.ConstantsUtil.*;

/**
 * Controller class for StudentSection.fxml
 *
 * @author Avik Sarkar
 */
public class StudentSectionController {

    /*------------------------------Declaration and initialization of variables------------------------------*/

    private StudentService studentService;
    private CourseService courseService;
    private BatchService batchService;
    private List<Batch> listOfBatches;
    private List<Student> listOfStudents;
    private List<Course> listOfCourses;
    private ObservableList<Student> studentObsList;

    @FXML
    private GridPane studentListGridPane;

    @FXML
    private StackPane statusStackPane;

    @FXML
    private Label titleLabel;

    @FXML
    private Button promoteButton;

    @FXML
    private Button addButton;

    @FXML
    private Button importButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ComboBox<String> batchNameComboBox;

    @FXML
    private ComboBox<String> degreeComboBox;

    @FXML
    private ComboBox<String> disciplineComboBox;

    @FXML
    private ComboBox<String> semesterComboBox;

    @FXML
    private TableView<Student> studentTableView;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableColumn<Student, String> regIdCol;

    @FXML
    private TableColumn<Student, String> rollNoCol;

    @FXML
    private TableColumn<Student, String> firstNameCol;

    @FXML
    private TableColumn<Student, String> middleNameCol;

    @FXML
    private TableColumn<Student, String> lastNameCol;

    @FXML
    private TableColumn<Student, String> guardianNameCol;

    @FXML
    private TableColumn<Student, String> contactNoCol;

    @FXML
    private TableColumn<Student, String> degreeCol;

    @FXML
    private TableColumn<Student, String> disciplineCol;

    @FXML
    private TableColumn<Student, String> semesterCol;

    @FXML
    private TableColumn<Student, String> batchCol;

    @FXML
    private TableColumn<Student, String> regYearCol;

    @FXML
    private TableColumn<Student, Boolean> selectCol;

    private int gid;

    /*-----------------------------------------End of initialization-------------------------------------------*/

    public void initController(int gid, String deptName){

        studentService = new StudentService();
        courseService = new CourseService();
        batchService = new BatchService();

        //initialize columns of the studentTableView
        initCol();

        //initialize this for the studentTableView
        studentObsList = FXCollections.observableArrayList();

        this.gid = gid;
        if(gid == PROFESSOR_GID){

            promoteButton.setVisible(false);
            addButton.setVisible(false);
            importButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        Task<List<Course>> coursesTask;
        if(deptName.isEmpty()){

            //get the list of courses available in the db
            coursesTask = courseService.getCoursesTask("");
        }
        else{

            //get the list of courses available in the db
            coursesTask = courseService.getCoursesTask("WHERE v_dept_name=?", deptName);
        }

        new Thread(coursesTask).start();

        coursesTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //store the list of all courses available in the DB
                listOfCourses = coursesTask.getValue();

                //only if there's any course in the db

                List<String> items = new ArrayList<>();

                for (Course course : listOfCourses) {

                    //add only unique degree items to degree combobox
                    if (!items.contains(course.getDegree())) {

                        items.add(course.getDegree());
                    }
                }

                //choosing this will display students from all degrees
                items.add("all");

                ObservableList<String> options = FXCollections.observableArrayList(items);
                degreeComboBox.setItems(options);
            }
        });
    }

    /**
     * Callback method for DegreeComboBox.
     * Clears all other comboBoxes,table items and sets the disciplineComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDegreeComboBox() {

        //clears all combo boxes upon selecting a degree
        disciplineComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getItems().clear();

        //clears the tableView
        studentObsList.clear();

        /*
        If 'all' is chosen , disable other comboBoxes
        and display all students data.
         */
        if (degreeComboBox.getValue().equals("all")) {

            disciplineComboBox.setDisable(true);
            batchNameComboBox.setDisable(true);
            semesterComboBox.setDisable(true);
            populateTable();
        } else if (degreeComboBox.getValue() != null) {

            disciplineComboBox.setDisable(false);
            batchNameComboBox.setDisable(false);
            semesterComboBox.setDisable(false);

            //Add every discipline which comes under the selected degree
            List<String> items = new ArrayList<>();
            for (Course course : listOfCourses) {

                if (course.getDegree().equals(degreeComboBox.getValue()))

                    //add the disciplines to the discipline comboBox for particular degree
                    if (!items.contains(course.getDiscipline()))
                        items.add(course.getDiscipline());
            }
            ObservableList<String> options = FXCollections.observableArrayList(items);
            disciplineComboBox.setItems(options);
        }
    }

    /**
     * Callback method for disciplineComboBox.
     * Clears batchNameComboBox,semesterComboBox & table items and sets
     * the batchNameComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDisciplineComboBox() {

        //clears batch combo box and semester combo box upon selecting a discipline
        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        //clears the tableView data
        studentObsList.clear();

        //only if a discipline is selected
        if (disciplineComboBox.getValue() != null) {

            final String additionalQuery = "where v_degree=? and v_discipline =?";

            //get all batches for particular degree and discipline
            Task<List<Batch>> batchesTask = batchService.getBatchesTask(additionalQuery, degreeComboBox.getValue()
                    , disciplineComboBox.getValue());
            new Thread(batchesTask).start();

            batchesTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    listOfBatches = batchesTask.getValue();

                    List<String> items = new ArrayList<>();

                    for (Batch batch : listOfBatches) {

                        //Add unique batch names to the batchComboBox for the corresponding degree and discipline
                        if (!items.contains(batch.getBatchName())) {

                            items.add(batch.getBatchName());
                        }
                    }
                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    batchNameComboBox.setItems(options);
                }
            });
        }
    }

    /**
     * Callback method for batchNameComboBox.
     * Clears semesterComboBox & table items and sets
     * the semesterComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleBatchNameComboBox() {

        //clears the semester combo box upon selecting a batch name
        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        //clears the tableView data
        studentObsList.clear();

        //only if a batchName is selected
        if (batchNameComboBox.getValue() != null) {

            List<String> items = new ArrayList<>();
            int totalSemesters = 0;
                /*
                Get the total no.of semesters for the selected Degree,discipline &
                set the semesterComboBox with semester values from 1 to total no.
                 */
            for (Course course : listOfCourses) {

                if (course.getDegree().equals(degreeComboBox.getValue())
                        && course.getDiscipline().equals(disciplineComboBox.getValue())) {

                    totalSemesters = Integer.parseInt(course.getDuration());
                }
            }
            //set the semester combo box from 1 to totalSemesters
            for (int i = 1; i <= totalSemesters; i++) {

                items.add(Integer.toString(i));
            }
            ObservableList<String> options = FXCollections.observableArrayList(items);
            semesterComboBox.setItems(options);
        }
    }

    /**
     * Callback method for semesterComboBox.
     * Clears table items and populate the tableView.
     * the semesterComboBoxes.
     */
    @FXML
    private void handleSemesterComboBox() {

        //clear the tableView data first
        studentObsList.clear();

        //only if a semester is selected , populate the table
        if (semesterComboBox.getValue() != null) {

            titleLabel.setText("List of " + batchNameComboBox.getValue() + " " + degreeComboBox.getValue() +
                    " " + disciplineComboBox.getValue() + " Semester" + semesterComboBox.getValue() +
                    " students");
            populateTable();
        }
    }

    /**
     * Callback method for ImportButton to import the students from the Csv file.
     */
    @FXML
    private void handleImportButtonAction() {

        //create a new stage first
        Stage importStudentListModalWindow = new Stage();

        //get the stage where the Import Button is situated
        Stage parentStage = (Stage) importButton.getScene().getWindow();

        FXMLLoader loader = UISetterUtil.setModalWindow("/view/ImportStudentCSVModal.fxml"
                , importStudentListModalWindow, parentStage, "Import Student List");

        //get the controller
        ImportStudentCSVModalController importStudentCSVModalController = loader.getController();

        /*
        showAndWait() since we need to get the status for updating the table from the Import Student controller.
        The application thread is blocked here and goes to execute the Import modal operations and only when the Import
        modal window is closed , the tableUpdateStatus is sent from the Import modal controller.
         */
        importStudentListModalWindow.showAndWait();

        //get the tableUpdateStatus
        boolean tableUpdateStatus = importStudentCSVModalController.getTableUpdateStatus();

        /*
        If any Student is imported from the CSV to DB and if a degree is selected in the ComboBox then update the Table.
         */
        if (tableUpdateStatus && degreeComboBox.getValue() != null) {

            //if 'all' is selected then update the table
            if (degreeComboBox.getValue().equals("all")) {

                populateTable();
            }

            //if all other combo boxes has some selection ,then only update the table
            else if (disciplineComboBox.getValue() != null && batchNameComboBox.getValue() != null
                    && semesterComboBox.getValue() != null) {

                populateTable();
            }
        }

    }

    @FXML
    private void handleStudentTableViewOnMouseClicked(){

        //get the selected student in the TableView
        Student student = studentTableView.getSelectionModel().getSelectedItem();

        //only if a Student is selected in the TableView.
        if (student != null) {

            //create a modal window
            Stage viewStudentModalWindow = new Stage();

            //get the main stage
            Stage parentStage = (Stage) importButton.getScene().getWindow();

            //set the modal window
            FXMLLoader loader = UISetterUtil.setModalWindow("/view/ViewStudentModal.fxml"
                    , viewStudentModalWindow, parentStage, "Student");

            //get the controller
            ViewStudentModalController viewStudentModalController = loader.getController();

            viewStudentModalController.initController(gid);

            //send the Student to the controller
            viewStudentModalController.setStudentPojo(student);

            /*
            showAndWait() ensures that the data studentDeletedStatus is fetched after the modal window is closed.
            This method blocks the UI thread here.
             */
            viewStudentModalWindow.show();

            studentTableView.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handlePromoteStudentButtonAction() {

        List<Student> studentList = new ArrayList<>();
        for(Student student : studentObsList){

            if(student.isSelected()){

                studentList.add(student);
            }
        }

        if (!studentList.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Do you want to promote the selected students ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                PromotionCommand promotionCommand = new PromotionCommand();

                studentListGridPane.setOpacity(0.2);
                statusStackPane.setVisible(true);

                Task<Integer> promoteStudentTask = promotionCommand.getPromoteStudentTask(studentList);
                new Thread(promoteStudentTask).start();

                promoteStudentTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        int status = promoteStudentTask.getValue();

                        studentListGridPane.setOpacity(1);
                        statusStackPane.setVisible(false);

                        if (status == DATABASE_ERROR) {

                            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                            alert1.setHeaderText("Error in database!");
                            alert1.show();
                        } else if (status == SUCCESS) {

                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setHeaderText("Student promotion successful!");
                            alert1.show();
                            populateTable();
                        } else if (status == PROMOTION_ERROR) {

                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setHeaderText("Few students have pending backlogs! Others have been promoted!");
                            alert1.show();
                            populateTable();
                        } else {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setHeaderText("Few students doesn't exist in the system! Others have been promoted! ");
                            alert1.show();
                            populateTable();
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method to handle ADD STUDENT Button action.
     *
     */
    @FXML
    private void handleAddStudentButtonAction(){

        Scene mainScene = titleLabel.getScene();
        Label subSubTitleLabel = (Label) mainScene.lookup("#subSubTitle");
        Label subTitleLabel = (Label) mainScene.lookup("#subTitle");

        //get the stackPane first in which the content is loaded
        StackPane contentStackPane = (StackPane) studentListGridPane.getParent().getParent();

        FXMLLoader loader =  UISetterUtil.setContentUI("/view/StudentRegistration.fxml", contentStackPane
                , subTitleLabel, subSubTitleLabel, "Student", "/ Add Student");

        StudentRegistrationController studentRegistrationController = loader.getController();
        studentRegistrationController.initController("");
    }

    @FXML
    private void handleDeleteStudentButtonAction(){

        List<Student> studentList = new ArrayList<>();
        for(Student student : studentObsList){

            if(student.isSelected()){

                studentList.add(student);
            }
        }
        if(!studentList.isEmpty()){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Do you want to delete the selected students ?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK) {

                studentListGridPane.setOpacity(0.2);
                statusStackPane.setVisible(true);

                Task<Integer> deleteStudentsTask = studentService.getDeleteStudentsTask(studentList);
                new Thread(deleteStudentsTask).start();

                deleteStudentsTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        int status = deleteStudentsTask.getValue();

                        studentListGridPane.setOpacity(1);
                        statusStackPane.setVisible(false);

                        if (status == DATABASE_ERROR) {

                            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                            alert1.setHeaderText("Error in database!");
                            alert1.show();
                        } else if (status == SUCCESS) {

                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setHeaderText("Deletion of students is successful!");
                            alert1.show();
                            populateTable();
                        }  else {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setHeaderText(status + " students have been deleted successfully!");
                            alert1.show();
                            populateTable();
                        }
                    }
                });
            }
        }
    }
    /**
     * This method initializes the columns of the Student Table.
     */
    private void initCol() {

        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        regIdCol.setCellValueFactory(new PropertyValueFactory<>("regId"));
        rollNoCol.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        guardianNameCol.setCellValueFactory(new PropertyValueFactory<>("guardianName"));
        contactNoCol.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        degreeCol.setCellValueFactory(new PropertyValueFactory<>("degree"));
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("currSemester"));
        disciplineCol.setCellValueFactory(new PropertyValueFactory<>("discipline"));
        batchCol.setCellValueFactory(new PropertyValueFactory<>("batchName"));
        regYearCol.setCellValueFactory(new PropertyValueFactory<>("regYear"));
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
        selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
    }

    @FXML
    public void handleSelectAllCheckBoxAction(ActionEvent e){

        for(Student student : studentObsList){

            student.setSelected(((CheckBox) e.getSource()).isSelected());
        }
    }
    /**
     * This method populates and updates the Student Table.
     */
    private void populateTable() {

        String additionalQuery = "";

        Task<List<Student>> studentsTask;

        /*
        If 'all' is chosen in the Degree ComboBox, then get all students from the DB, otherwise get students of the
        particular Course and Batch and Semester.
         */
        if (degreeComboBox.getValue().equals("all")) {

            studentsTask = studentService.getStudentTask(additionalQuery);
        } else {

            additionalQuery = "where v_degree=? and v_discipline=? " +
                    "and v_batch_name=? and int_curr_semester=?";
            studentsTask = studentService.getStudentTask(additionalQuery
                    , degreeComboBox.getValue(), disciplineComboBox.getValue()
                    , batchNameComboBox.getValue(), semesterComboBox.getValue());
        }

        new Thread(studentsTask).start();

        studentsTask.setOnSucceeded(new EventHandler<>() {

            @Override
            public void handle(WorkerStateEvent event) {

                //get list of students
                listOfStudents = studentsTask.getValue();
                studentObsList.setAll(listOfStudents);

                /*
                Wrap the Observable List in a FilteredList, for the search feature; null means no predicate i.e. always
                true, so display all students in the TableView at first.
                 */
                FilteredList<Student> studentFilteredItems = new FilteredList<>(studentObsList, null);

                //FilteredList is unmodifiable, so wrap it in a SortedList for sorting functionality in the TableView
                SortedList<Student> studentSortedList = new SortedList<>(studentFilteredItems);

                /*
                Attach a listener to the Search field to display only those Students in the TableView that matches with the
                Search box data.
                 */
                searchTextField.textProperty().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                        //set the predicate which will be used for filtering Student
                        studentFilteredItems.setPredicate(new Predicate<>() {

                            @Override
                            public boolean test(Student student) {

                                String lowerCaseFilter = newValue.toLowerCase();

                                if (newValue.isEmpty()) {

                                    return true;
                                } else if (student.getFirstName().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getMiddleName().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getLastName().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getRollNo().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getRegId().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getDegree().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getDiscipline().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getCurrSemester().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getBatchName().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getRegYear().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getGuardianName().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                } else if (student.getContactNo().toLowerCase().contains(lowerCaseFilter)) {

                                    return true;
                                }
                                return false;
                            }
                        });
                    }
                });

                //set items in the TableView
                studentTableView.setItems(studentSortedList);

                //attach the comparator ,so that sorting can be done
                studentSortedList.comparatorProperty().bind(studentTableView.comparatorProperty());
            }
        });

    }
}
