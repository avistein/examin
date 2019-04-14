package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import model.Batch;
import model.Course;
import model.Student;
import service.BatchService;
import service.CourseService;
import service.StudentService;
import util.ValidatorUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static util.ConstantsUtil.*;

/**
 * Controller class for a single student registration.
 * for StudentRegistration.fxml
 *
 * @author Avik Sarkar
 */
public class StudentRegistrationController {

    /*---------------------------------Declaration and Initialization of variables-----------------------------------
    -----------------------The @FXML components are initialized when FXML document is being loaded.------------------
     */

    private Student student;

    private int editOrAddStudentChoice;

    private CourseService courseService;

    private BatchService batchService;

    private StudentService studentService;

    private List<Course> listOfCourses;

    private List<Batch> listOfBatches;

    private File imageFile;

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
    private ImageView profileImageImageView;

    @FXML
    private Button chooseImageButton;

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

    /*------------------------------------End of declaration and initialization----------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    private void initialize() {

        courseService = new CourseService();
        batchService = new BatchService();
        studentService = new StudentService();

        //initially adding of new student is opted
        editOrAddStudentChoice = ADD_CHOICE;

        //get a list of available courses in the DB
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

                    //only add unique degrees to the degreeComboBox
                    for (Course course : listOfCourses) {

                        if (!items.contains(course.getDegree()))
                            items.add(course.getDegree());
                    }

                    ObservableList<String> options = FXCollections.observableArrayList(items);

                    //set items in degreeComboBox
                    degreeComboBox.setItems(options);
                }

                //set items in the gender choice box
                genderChoiceBox.setItems(FXCollections.observableArrayList("Male"
                        , "Female", "Others"));
            }
        });
    }

    /**
     * Callback method for handling an item selection in the Degree ComboBox.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDegreeComboBox() {

        /*
        Whenever any degree is selected ,clear all other combo boxes.
         */
        disciplineComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getItems().clear();

        //only if a degree is selected
        if (degreeComboBox.getValue() != null) {

            //only if there is any course in the db
            if (!listOfCourses.isEmpty()) {

                List<String> items = new ArrayList<>();

                for (Course course : listOfCourses) {

                    //sets the unique discipline items for particular degree
                    if (course.getDegree().equals(degreeComboBox.getValue()))

                        if (!items.contains(course.getDiscipline()))
                            items.add(course.getDiscipline());
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                disciplineComboBox.setItems(options);
            }
        }
    }

    /**
     * Callback method for handling Discipline ComboBox.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDisciplineComboBox() {

        //clear batch combo box and semester combo box whenever a discipline is selected
        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        //only if an discipline is selected this will be executed
        if (disciplineComboBox.getValue() != null) {

            final String additionalQuery = "where v_degree=? and v_discipline =?";

            //get the batches for that corresponding degree and discipline
            Task<List<Batch>> batchesTask = batchService
                    .getBatchesTask(additionalQuery, degreeComboBox.getValue()
                            , disciplineComboBox.getValue());
            new Thread(batchesTask).start();

            batchesTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    //store the list of batches for the corresponding degree and discipline
                    listOfBatches = batchesTask.getValue();

                    //only if there is any batch in the db
                    if (!listOfBatches.isEmpty()) {

                        List<String> items = new ArrayList<>();

                        for (Batch batch : listOfBatches) {

                            //sets the unique batch name items for particular degree and discipline
                            if (!items.contains(batch.getBatchName()))
                                items.add(batch.getBatchName());
                        }
                        ObservableList<String> options = FXCollections.observableArrayList(items);
                        batchNameComboBox.setItems(options);
                    }
                }
            });
        }
    }

    /**
     * Callback method that handles an item selection in the BatchNameComboBox.
     * Also sets the items in the Semester combo box.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleBatchNameComboBox() {

        //whenever any batch name is selected ,clear the semesterComboBox
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
            }
        }
    }

    @FXML
    private void handleChooseImageButtonAction() {

        //choose a profile image for the student from the system
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        imageFile = fileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());

        //if an image is chosen then set the imageView with that image
        if (imageFile != null) {

            profileImageImageView.setImage(new Image("file:" + imageFile.getAbsoluteFile()));
        }
    }

    /**
     * Callback method to handle Submit button.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitButtonAction() {

        //at first validate all the fields
        if (validate()) {

            //fades the bg and display loading spinner
            mainGridPane.setOpacity(0.5);
            statusStackPane.setVisible(true);
            progressIndicator.setVisible(true);

            /*
            Creates a new Student object and sets it's data.
             */
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

            //if edit signal is sent
            if (editOrAddStudentChoice == EDIT_CHOICE) {

                /*
                If a image is chosen from the FileChooser, then set the path of the image in Student.profileImagePath,
                otherwise set the previous location i.e. the location specified in the database before editing.
                 */
                if (imageFile != null) {

                    student.setProfileImagePath(imageFile.getAbsolutePath());
                } else {
                    student.setProfileImagePath(this.student.getProfileImagePath());
                }

                Task<Integer> updateStudentTask = studentService.getUpdateStudentTask(student);
                new Thread(updateStudentTask).start();

                updateStudentTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of the update Student
                        int status = updateStudentTask.getValue();

                        /*
                        Disable the loading spinner and show status
                         */
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

            }

            //if add new student is chosen
            else if (editOrAddStudentChoice == ADD_CHOICE) {

                //if a image is chosen from the FileChooser, then set the path of the image in Student.profileImagePath
                if (imageFile != null) {

                    student.setProfileImagePath(imageFile.getAbsolutePath());
                }
                //get the batch id for the respective degree,discipline and batch chosen
                for (Batch batch : listOfBatches) {

                    if (batch.getDiscipline().equals(disciplineComboBox.getValue()) &&
                            batch.getDegree().equals(degreeComboBox.getValue()) &&
                            batch.getBatchName().equals(batchNameComboBox.getValue())) {

                        student.setBatchId(batch.getBatchId());
                    }

                }

                Task<Integer> addStudentToDatabaseTask = studentService.getAddStudentToDatabaseTask(student);
                new Thread(addStudentToDatabaseTask).start();

                addStudentToDatabaseTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of insertion of new student into the DB
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
        }
        if (disciplineComboBox.getValue() == null) {

            alert.setContentText("Please select a discipline!");
            alert.show();
            return false;
        }
        if (batchNameComboBox.getValue() == null) {

            alert.setContentText("Please select a batch!");
            alert.show();
            return false;
        }
        if (semesterComboBox.getValue() == null) {

            alert.setContentText("Please select a semester!");
            alert.show();
            return false;
        }
        if (regYearTextField.getText().isEmpty()) {

            alert.setContentText("Registration Year cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateRegYear(batchNameComboBox.getValue()
                , regYearTextField.getText())) {

            alert.setContentText("Invalid Registration Year or not within batch range!");
            alert.show();
            return false;
        }
        if (regIdTextField.getText().isEmpty()) {

            alert.setContentText("Registration ID cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateId(regIdTextField.getText().trim())) {

            alert.setContentText("Invalid Registration ID!");
            alert.show();
            return false;
        }
        if (rollNoTextField.getText().isEmpty()) {

            alert.setContentText("Roll No. cannot be empty!");
            return false;
        }
        if (!ValidatorUtil.validateId(rollNoTextField.getText().trim())) {

            alert.setContentText("Invalid Roll No.!");
            return false;
        }
        if (firstNameTextField.getText().isEmpty()) {

            alert.setContentText("First Name cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateName(firstNameTextField.getText().trim())) {

            alert.setContentText("Invalid First Name!");
            alert.show();
            return false;
        }
        if (!middleNameTextField.getText().trim().isEmpty()) {

            if (!ValidatorUtil.validateName(middleNameTextField.getText().trim())) {

                alert.setContentText("Invalid Middle Name!");
                alert.show();
                return false;
            }
        }
        if (!lastNameTextField.getText().trim().isEmpty()) {

            if (!ValidatorUtil.validateName(lastNameTextField.getText().trim())) {

                alert.setContentText("Invalid Last Name!");
                alert.show();
                return false;
            }
        }
        if (dobDatePicker.getValue() == null) {

            alert.setContentText("Please choose a date of birth!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateDateFormat(String.valueOf(dobDatePicker.getValue()))) {

            alert.setContentText("Invalid date of birth format!");
            alert.show();
            return false;
        }
        if (genderChoiceBox.getValue() == null) {

            alert.setContentText("Please select a gender!");
            alert.show();
            return false;
        }
        if (emailTextField.getText().isEmpty()) {

            alert.setContentText("Email ID cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateEmail(emailTextField.getText())) {

            alert.setContentText("Invalid Email ID !");
            alert.show();
            return false;
        }
        if (contactNoTextField.getText().isEmpty()) {

            alert.setContentText("Contact No. cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateContactNo(contactNoTextField.getText())) {

            alert.setContentText("Invalid Contact No.!");
            alert.show();
            return false;
        }
        if (addressTextArea.getText().isEmpty()) {

            alert.setContentText("Address cannot be empty!");
            alert.show();
            return false;
        }
        if (guardianNameTextField.getText().isEmpty()) {

            alert.setContentText("Guardian/Father's Name cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateName(guardianNameTextField.getText().trim())) {

            alert.setContentText("Invalid Guardian/Father's Name!");
            alert.show();
            return false;
        }
        if (!motherNameTextField.getText().trim().isEmpty()) {

            if (!ValidatorUtil.validateName(motherNameTextField.getText().trim())) {

                alert.setContentText("Invalid Mother's Name!");
                alert.show();
                return false;
            }
        }
        if (guardianContactNoTextField.getText().isEmpty()) {

            alert.setContentText("Guardian Contact No. cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateContactNo(guardianContactNoTextField
                .getText())) {

            alert.setContentText("Invalid Guardian Contact No.!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * Method for configuring the fileChooser.
     */
    private void configureFileChooser(FileChooser fileChooser) {

        fileChooser.setTitle("Choose Image");

        //only .png,.jpg and .jpeg files can be chosen
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
    }

    /**
     * Callback method to handle Add Another button and Reset button.
     * <p>
     * Deactivates loading spinner and status and clears the textfields and the comboboxes and sets the profile
     * imageview with a placeholder.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleAddAnotherAndResetButtonAction() {

        //add another or reset means adding a new student
        editOrAddStudentChoice = ADD_CHOICE;

        statusStackPane.setVisible(false);
        progressIndicator.setVisible(false);
        buttonsHbox.setVisible(false);
        statusImageView.setVisible(false);
        statusLabel.setVisible(false);
        mainGridPane.setOpacity(1);
        firstNameTextField.clear();
        middleNameTextField.clear();
        lastNameTextField.clear();
        degreeComboBox.setValue(null);
        disciplineComboBox.setValue(null);
        batchNameComboBox.setValue(null);
        semesterComboBox.setValue(null);
        regYearTextField.clear();
        regIdTextField.clear();
        rollNoTextField.clear();
        genderChoiceBox.setValue(null);
        dobDatePicker.setValue(null);
        emailTextField.clear();
        addressTextArea.clear();
        contactNoTextField.clear();
        guardianNameTextField.clear();
        motherNameTextField.clear();
        guardianContactNoTextField.clear();
        regIdTextField.setDisable(false);
        rollNoTextField.setDisable(false);
        batchNameComboBox.setDisable(false);
        disciplineComboBox.setDisable(false);
        degreeComboBox.setDisable(false);
        semesterComboBox.setDisable(false);
        profileImageImageView.setImage(new Image("/png/placeholder.png"));
    }

    /**
     * Callback method to handle Back and Cancel button.
     * Opens the StudentsList scene on clicking on them.
     *
     * @throws IOException Load exception while loading the fxml document.
     */
    @FXML
    private void handleBackAndCancelButtonAction(){

        Scene mainScene = mainGridPane.getScene();
        Button professorSectionButton = (Button) mainScene.lookup("#studentButton");
        professorSectionButton.fire();
    }

    /**
     * This method is used to set the edit command and prepare the Registration form for editing.
     *
     * @param editSignal EDIT_CHOICE
     */
    @SuppressWarnings("Duplicates")
    public void setEditSignal(int editSignal) {

        editOrAddStudentChoice = editSignal;

        //if the Image exists in the location ,set the ImageView with that ,otherwise set with a placeholder
        if (Paths.get(student.getProfileImagePath()).toFile().exists()) {

            profileImageImageView.setImage(new Image("file:" + student.getProfileImagePath()));
        } else {

            profileImageImageView.setImage(new Image("/png/placeholder.png"));
        }

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
        batchNameComboBox.setDisable(true);
        disciplineComboBox.setDisable(true);
        degreeComboBox.setDisable(true);
        semesterComboBox.setDisable(true);
        degreeComboBox.setValue(student.getDegree());
        disciplineComboBox.setValue(student.getDiscipline());
        batchNameComboBox.setValue(student.getBatchName());
        semesterComboBox.setValue(student.getCurrSemester());
        regIdTextField.setDisable(true);
        rollNoTextField.setDisable(true);
    }

    /**
     * This method is used the set the Student object with the selected Student object in the tableView for editing.
     *
     * @param student The object with which the Student object will be set.
     */
    public void setStudentPojo(Student student) {

        this.student = student;
    }
}
