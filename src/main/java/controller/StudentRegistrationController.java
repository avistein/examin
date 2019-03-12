package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.Batch;
import model.Course;
import model.Student;
import service.BatchService;
import service.CourseService;
import service.StudentService;
import util.ValidatorUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static util.ConstantsUtil.*;

/**
 * Controller class for a single student registration.
 * for StudentRegistration.fxml
 * @author Avik Sarkar
 */
public class StudentRegistrationController {

    /*--------------------Declaration and Initialization of variables--------------
    The @FXML components are initialized when FXML document is being loaded.
     */

    private Student student;

    private int editOrAddStudentChoice;

    private CourseService courseService;

    private BatchService batchService;

    private StudentService studentService;

    private List<Course> listOfCourses;

    private List<Batch> listOfBatches;

    @FXML
    private ComboBox<String> batchNameComboBox;

    @FXML
    private ComboBox<String> degreeComboBox;

    @FXML
    private ComboBox<String> disciplineComboBox;

    @FXML
    private ComboBox<String> semesterComboBox;

    @FXML
    private TextField regYearTextField;

    @FXML
    private TextField rollNoTextField;

    @FXML
    private TextField regIdTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField middleNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField contactNoTextField;

    @FXML
    private TextArea addressTextArea;

    @FXML
    private TextField guardianNameTextField;

    @FXML
    private TextField motherNameTextField;

    @FXML
    private TextField guardianContactNoTextField;

    @FXML
    private ImageView statusImageView;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label statusLabel;

    @FXML
    private StackPane statusStackPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private HBox buttonsHbox;

    /*---------------------End of declaration and initialization--------------------*/

    /**
     * This method is called once the whole FXML document is done loading.
     */
    @FXML
    private void initialize() {

        courseService = new CourseService();
        batchService = new BatchService();
        studentService = new StudentService();

        Task<List<Course>> coursesTask = courseService
                .getCoursesTask("");
        new Thread(coursesTask).start();

        coursesTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //get the list of courses to get the list of degrees
                listOfCourses = coursesTask.getValue();

                //only when any course is present in the listofcourses then only set the the degree combo box
                if (!listOfCourses.isEmpty()) {

                    List<String> items = new ArrayList<>();
                    for (Course course : listOfCourses) {
                        if (!items.contains(course.getDegree()))
                            items.add(course.getDegree());
                    }
                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    degreeComboBox.setItems(options);
                    if(editOrAddStudentChoice == EDIT_CHOICE)
                        degreeComboBox.setValue(student.getDegree());
                }

                //set items in the gender choice box
                genderChoiceBox.setItems(FXCollections.observableArrayList("Male"
                        , "Female", "Others"));
            }
        });
    }

    /**
     * This method handles an item selection in the BatchNameComboBox.
     * Also sets the items in the Semester combo box.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleBatchNameComboBox() {

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        //execute only if a batch name is selected
        if (batchNameComboBox.getValue() != null) {

            if (!listOfCourses.isEmpty()) {

                List<String> items = new ArrayList<>();

                int totalSemesters = 0;

                for (Course course : listOfCourses) {

                    if (course.getDegree().equals(degreeComboBox.getValue())
                            && course.getDiscipline().equals(disciplineComboBox.getValue()))

                        //get the duration of the particular course
                        totalSemesters = Integer.parseInt(course.getDuration());
                }

                //set the semester items from 1 to totalSemesters
                for (int i = 1; i <= totalSemesters; i++)
                    items.add(Integer.toString(i));
                ObservableList<String> options = FXCollections.observableArrayList(items);
                semesterComboBox.setItems(options);
                if(editOrAddStudentChoice == EDIT_CHOICE)
                    semesterComboBox.setValue(student.getCurrSemester());
            }
        }
    }

    /**
     * Callback method for handling the Degree ComboBox.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDegreeComboBox() {

        disciplineComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getItems().clear();

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        //only if a degree is selected
        if (degreeComboBox.getValue() != null) {

            //only if there is any course in the db
            if (!listOfCourses.isEmpty()) {

                List<String> items = new ArrayList<>();
                for (Course course : listOfCourses) {

                    //sets the discipline items for particular degree
                    if (course.getDegree().equals(degreeComboBox.getValue()))

                        if (!items.contains(course.getDiscipline()))
                            items.add(course.getDiscipline());
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                disciplineComboBox.setItems(options);
                if(editOrAddStudentChoice == EDIT_CHOICE)
                    disciplineComboBox.setValue(student.getDiscipline());
            }
        }
    }

    /**
     * Callback method for handling Discipline ComboBox.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDisciplineComboBox() {

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        //only if an discipline is selected this will be executed
        if (disciplineComboBox.getValue() != null) {

            final String additionalQuery = "where v_degree=? and v_discipline =?";

            Task<List<Batch>> batchesTask = batchService
                    .getBatchesTask(additionalQuery, degreeComboBox.getValue()
                            , disciplineComboBox.getValue());
            new Thread(batchesTask).start();

            batchesTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    listOfBatches = batchesTask.getValue();

                    //only if there is any batch in the db
                    if (!listOfBatches.isEmpty()) {

                        List<String> items = new ArrayList<>();

                        for (Batch batch : listOfBatches) {

                            //sets the batch name items for particular degree and discipline
                            if (!items.contains(batch.getBatchName()))
                                items.add(batch.getBatchName());
                        }
                        ObservableList<String> options = FXCollections.observableArrayList(items);
                        batchNameComboBox.setItems(options);
                        if(editOrAddStudentChoice == EDIT_CHOICE)
                            batchNameComboBox.setValue(student.getBatchName());
                    }
                }
            });
        }
    }

    /**
     * Callback method to handle Submit button.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitButtonAction() {

        if (validate()) {


            //fades the bg and display loading spinner
            mainGridPane.setOpacity(0.5);
            statusStackPane.setVisible(true);
            progressIndicator.setVisible(true);

            Student student = new Student();
            student.setFirstName(firstNameTextField.getText().trim());
            student.setMiddleName(middleNameTextField.getText().trim());
            student.setLastName(lastNameTextField.getText().trim());
            student.setDob(dobDatePicker.getValue().toString());
            student.setGender(genderChoiceBox.getValue());
            student.setRegYear(regYearTextField.getText().trim());
            student.setEmail(emailTextField.getText().trim());
            student.setAddress(addressTextArea.getText().trim());
            student.setMotherName(motherNameTextField.getText().trim());
            student.setGuardianContactNo(guardianContactNoTextField.getText().trim());
            student.setRegId(regIdTextField.getText().trim());
            student.setRollNo(rollNoTextField.getText().trim());
            student.setContactNo(contactNoTextField.getText().trim());
            student.setGuardianName(guardianNameTextField.getText().trim());
            student.setCurrSemester(semesterComboBox.getValue());
            student.setBatchName(batchNameComboBox.getValue());
            student.setDiscipline(disciplineComboBox.getValue());
            student.setDegree(degreeComboBox.getValue());

            //get the batch ids for the respective degree,discipline and batch chosen
            for (Batch batch : listOfBatches) {

                if (batch.getDiscipline().equals(disciplineComboBox.getValue()) &&
                        batch.getDegree().equals(degreeComboBox.getValue()) &&
                        batch.getBatchName().equals(batchNameComboBox.getValue())) {

                    student.setBatchId(batch.getBatchId());
                }

            }

            if(editOrAddStudentChoice == EDIT_CHOICE ){

                Task<Integer> updateStudentTask = studentService.getUpdateStudentTask(student);
                new Thread(updateStudentTask).start();

                updateStudentTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        int status = updateStudentTask.getValue();

                        progressIndicator.setVisible(false);
                        buttonsHbox.setVisible(true);
                        statusImageView.setVisible(true);
                        statusLabel.setVisible(true);

                        //display several status
                        if (status == DATABASE_ERROR) {

                            statusImageView.setImage(new Image("/png/critical error.png"));
                            statusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            statusImageView.setImage(new Image("/png/success.png"));
                            statusLabel.setText("Edited Successfully!");
                        } else {
                            statusImageView.setImage(new Image("/png/error.png"));
                            statusLabel.setText("Student doesn't exist!");
                        }
                    }
                });

                regIdTextField.setDisable(false);
                rollNoTextField.setDisable(false);
            }
            else {
                Task<Integer> addStudentToDatabaseTask = studentService.getAddStudentToDatabaseTask(student);
                new Thread(addStudentToDatabaseTask).start();

                addStudentToDatabaseTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        int status = addStudentToDatabaseTask.getValue();

                        //hides the loading spinner and shows status
                        progressIndicator.setVisible(false);
                        buttonsHbox.setVisible(true);
                        statusImageView.setVisible(true);
                        statusLabel.setVisible(true);

                        //display several status
                        if (status == DATABASE_ERROR) {

                            statusImageView.setImage(new Image("/png/critical error.png"));
                            statusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            statusImageView.setImage(new Image("/png/success.png"));
                            statusLabel.setText("Added Successfully!");
                        } else {

                            statusImageView.setImage(new Image("/png/error.png"));
                            statusLabel.setText("Student already exists!");
                        }
                    }
                });
            }
        }
    }

    /**
     * This method validates a single student and displays msg if validation fails.
     *
     * @return the validation status
     */
    private boolean validate() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (degreeComboBox.getValue() == null) {

            alert.setContentText("Please select a degree!");
            alert.show();
            return false;
        } else if (disciplineComboBox.getValue() == null) {

            alert.setContentText("Please select a discipline!");
            alert.show();
            return false;
        } else if (batchNameComboBox.getValue() == null) {

            alert.setContentText("Please select a batch!");
            alert.show();
            return false;
        } else if (semesterComboBox.getValue() == null) {

            alert.setContentText("Please select a semester!");
            alert.show();
            return false;
        } else if (regYearTextField.getText().isEmpty()) {

            alert.setContentText("Registration Year cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateRegYear(batchNameComboBox.getValue()
                , regYearTextField.getText())) {

            alert.setContentText("Invalid Registration Year or not within batch range!");
            alert.show();
            return false;
        } else if (regIdTextField.getText().isEmpty()) {

            alert.setContentText("Registration ID cannot be empty!");
            alert.show();
            return false;
        } else if (rollNoTextField.getText().isEmpty()) {

            alert.setContentText("Roll No. cannot be empty!");
            return false;
        } else if (firstNameTextField.getText().isEmpty()) {

            alert.setContentText("First Name cannot be empty!");
            alert.show();
            return false;
        } else if (dobDatePicker.getValue() == null) {

            alert.setContentText("Please choose a date of birth!");
            alert.show();
            return false;
        } else if (genderChoiceBox.getValue() == null) {

            alert.setContentText("Please select a gender!");
            alert.show();
            return false;
        } else if (emailTextField.getText().isEmpty()) {

            alert.setContentText("Email ID cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateEmail(emailTextField.getText())) {

            alert.setContentText("Invalid Email ID !");
            alert.show();
            return false;
        } else if (contactNoTextField.getText().isEmpty()) {

            alert.setContentText("Contact No. cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateContactNo(contactNoTextField.getText())) {

            alert.setContentText("Invalid Contact No.!");
            alert.show();
            return false;
        } else if (addressTextArea.getText().isEmpty()) {

            alert.setContentText("Address cannot be empty!");
            alert.show();
            return false;
        } else if (guardianNameTextField.getText().isEmpty()) {

            alert.setContentText("Guardian/Father's Name cannot be empty!");
            alert.show();
            return false;
        } else if (guardianContactNoTextField.getText().isEmpty()) {

            alert.setContentText("Guardian Contact No. cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateContactNo(guardianContactNoTextField
                .getText())) {

            alert.setContentText("Invalid Guardian Contact No.!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * Callback method to handle Add Another button and Reset button.
     * Deactivates loading spinner and status and clears the textfields and the comboboxes.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleAddAnotherAndResetButtonAction() {
        statusStackPane.setVisible(false);
        progressIndicator.setVisible(false);
        buttonsHbox.setVisible(false);
        statusImageView.setVisible(false);
        statusLabel.setVisible(false);
        mainGridPane.setOpacity(1);
        firstNameTextField.clear();
        middleNameTextField.clear();
        lastNameTextField.clear();
        degreeComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getSelectionModel().clearSelection();
        regYearTextField.clear();
        regIdTextField.clear();
        rollNoTextField.clear();
        genderChoiceBox.getSelectionModel().clearSelection();
        dobDatePicker.getEditor().clear();
        emailTextField.clear();
        addressTextArea.clear();
        contactNoTextField.clear();
        guardianNameTextField.clear();
        motherNameTextField.clear();
        guardianContactNoTextField.clear();
    }

    /**
     * Callback method to handle Back and Cancel button.
     * Opens the StudentsList scene on clicking on them.
     *
     * @throws IOException Load exception while loading the fxml document.
     */
    @FXML
    private void handleBackAndCancelButtonAction() throws IOException {

        StackPane contentStackPane = (StackPane) rootAnchorPane.getParent();
        Parent studentRegistrationFxml = FXMLLoader.load(getClass()
                .getResource("/view/StudentsList.fxml"));
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(studentRegistrationFxml);
    }

    public void setEditSignal(int editSignal){

        editOrAddStudentChoice = editSignal;
        firstNameTextField.setText(student.getFirstName());
        middleNameTextField.setText(student.getMiddleName());
        lastNameTextField.setText(student.getLastName());
        dobDatePicker.setValue(LocalDate.parse(student.getDob()));
        genderChoiceBox.setValue(student.getGender());
        regYearTextField.setText(student.getRegYear());
        emailTextField.setText(student.getEmail());
        addressTextArea.setText(student.getAddress());
        motherNameTextField.setText(student.getMotherName());
        guardianContactNoTextField.setText(student.getGuardianContactNo());
        regIdTextField.setText(student.getRegId());
        rollNoTextField.setText(student.getRollNo());
        contactNoTextField.setText(student.getContactNo());
        guardianNameTextField.setText(student.getGuardianName());
        semesterComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getSelectionModel().clearSelection();
        degreeComboBox.getSelectionModel().clearSelection();
        regIdTextField.setDisable(true);
        rollNoTextField.setDisable(true);

    }

    public void setStudentPojo(Student student){

        this.student = student;
    }
}
