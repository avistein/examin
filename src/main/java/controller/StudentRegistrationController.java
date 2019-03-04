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
import java.util.ArrayList;
import java.util.List;
import static util.ConstantsUtil.*;

public class StudentRegistrationController {

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
                listOfCourses = coursesTask.getValue();
                if (!listOfCourses.isEmpty()) {
                    List<String> items = new ArrayList<>();
                    for (Course course : listOfCourses) {
                        if (!items.contains(course.getDegree()))
                            items.add(course.getDegree());
                    }
                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    degreeComboBox.setItems(options);
                }
                genderChoiceBox.setItems(FXCollections.observableArrayList("Male"
                        , "Female", "Others"));
            }
        });

    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleBatchNameComboBox() {
        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        if (batchNameComboBox.getValue() != null) {
            //System.out.println(event.toString());
            if (!listOfCourses.isEmpty()) {

                List<String> items = new ArrayList<>();
                int totalSemesters = 0;
                for (Course course : listOfCourses) {
                    if (course.getDegree().equals(degreeComboBox.getValue())
                            && course.getDiscipline().equals(disciplineComboBox.getValue()))
                        totalSemesters = Integer.parseInt(course.getDuration());
                }
                for (int i = 1; i <= totalSemesters; i++)
                    items.add(Integer.toString(i));
                ObservableList<String> options = FXCollections.observableArrayList(items);
                semesterComboBox.setItems(options);

            }
        }
    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDegreeComboBox() {

        disciplineComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getItems().clear();

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        if (degreeComboBox.getValue() != null) {
            if (!listOfCourses.isEmpty()) {
                List<String> items = new ArrayList<>();
                for (Course course : listOfCourses) {
                    if (course.getDegree().equals(degreeComboBox.getValue()))
                        if (!items.contains(course.getDiscipline()))
                            items.add(course.getDiscipline());
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                disciplineComboBox.setItems(options);

            }
        }
    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDisciplineComboBox() {

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

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
                    if (!listOfBatches.isEmpty()) {
                        List<String> items = new ArrayList<>();
                        for (Batch batch : listOfBatches) {
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



    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitButtonAction() {
        if (validate()) {

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

            for (Batch batch : listOfBatches) {

                if (batch.getDiscipline().equals(disciplineComboBox.getValue()) &&
                        batch.getDegree().equals(degreeComboBox.getValue()) &&
                        batch.getBatchName().equals(batchNameComboBox.getValue())) {
                    student.setBatchId(batch.getBatchId());
                }

            }

            Task<Integer> addStudentToDatabaseTask = studentService
                    .getAddStudentToDatabaseTask(student);

            new Thread(addStudentToDatabaseTask).start();

            addStudentToDatabaseTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    int status = addStudentToDatabaseTask.getValue();
                    progressIndicator.setVisible(false);
                    buttonsHbox.setVisible(true);
                    statusImageView.setVisible(true);
                    statusLabel.setVisible(true);
                    if (status == DATABASE_ERROR) {
                        statusImageView.setImage(new Image("/png/critical error.png"));
                        statusLabel.setText("Database Error!");
                    }
                    else if (status == SUCCESS) {
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
                ,regYearTextField.getText())) {
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
        }
        else if (!ValidatorUtil.validateEmail(emailTextField.getText())) {
            alert.setContentText("Invalid Email ID !");
            alert.show();
            return false;
        }
        else if (contactNoTextField.getText().isEmpty()) {
            alert.setContentText("Contact No. cannot be empty!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateContactNo(contactNoTextField.getText())) {
            alert.setContentText("Invalid Contact No.!");
            alert.show();
            return false;
        }
        else if (addressTextArea.getText().isEmpty()) {
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

    @FXML
    private void handleAddAnotherAndResetButtonAction(){
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

    @FXML
    private void handleBackAndCancelButtonAction() throws IOException {

        StackPane contentStackPane = (StackPane)rootAnchorPane.getParent();
        Parent studentRegistrationFxml = FXMLLoader.load(getClass()
                .getResource("/view/StudentsList.fxml"));
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(studentRegistrationFxml);
    }

}
