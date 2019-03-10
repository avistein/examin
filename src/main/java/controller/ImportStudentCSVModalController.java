package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import model.Student;
import service.StudentService;
import util.CSVUtil;
import util.ValidatorUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Controller class for ImportStudentCSVModal.fxml.
 *
 * @author Avik Sarkar
 */
public class ImportStudentCSVModalController {

    /*---------------------Initialization and declaration of variables-----------------------*/

    private StudentService studentService;
    private File file;
    private boolean tableUpdateStatus;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private StackPane statusStackPane;

    @FXML
    private ImageView statusImageView;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label statusLabel;

    @FXML
    private Button chooseFileButton;

    @FXML
    private TextFlow csvInstructionsTextFlow;

    @FXML
    private Button submitButton;

    @FXML
    private Label chosenFileLabel;

    @FXML
    private ComboBox<String> firstNameComboBox;

    @FXML
    private ComboBox<String> lastNameComboBox;

    @FXML
    private ComboBox<String> middleNameComboBox;

    @FXML
    private ComboBox<String> batchNameComboBox;

    @FXML
    private ComboBox<String> degreeComboBox;

    @FXML
    private ComboBox<String> disciplineComboBox;

    @FXML
    private ComboBox<String> regYearComboBox;

    @FXML
    private ComboBox<String> currSemesterComboBox;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private ComboBox<String> dobComboBox;

    @FXML
    private ComboBox<String> guardianNameComboBox;

    @FXML
    private ComboBox<String> motherNameComboBox;

    @FXML
    private ComboBox<String> addressComboBox;

    @FXML
    private ComboBox<String> emailComboBox;

    @FXML
    private ComboBox<String> contactNoComboBox;

    @FXML
    private ComboBox<String> guardianContactNoComboBox;

    @FXML
    private ComboBox<String> regIdComboBox;

    @FXML
    private ComboBox<String> rollNoComboBox;

    /*--------------------------End of declaration and initialization------------------*/

    /**
     * This method is called once all the components of the fxml document is loaded
     */
    public void initialize() {

        studentService = new StudentService();

        //by default table of the studentList will not be updated
        tableUpdateStatus = false;

        //setting the csv import instruction
        Text text1 = new Text("File must be comma delimited CSV file\n");
        Text text2 = new Text("Open excel save as comma delimited CSV file\n");
        Text text3 = new Text("Make sure there is no heading or any other content" +
                " than column header and values in CSV file\n");
        Text text4 = new Text("Any date column should have values in the format " +
                "YYYY-MM-DD");
        csvInstructionsTextFlow.getChildren().addAll(text1, text2, text3, text4);
    }


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

            //checking if all 18 columns are present in the csv file uploaded
            if (list.size() == 18) {
                setComboBoxes(list);
                submitButton.setDisable(false);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("CSV file should have 18 columns!");
                alert.show();
                chosenFileLabel.setText("");
                submitButton.setDisable(true);
                unSetComboBoxes();
            }
        }
    }


    /**
     * Callback method for submitButton.
     */
    @FXML
    private void handleSubmitButtonAction() {

        /*
        Create a HashMap and set up the following :
        Key : Student's Attribute.
        Value : The name of the column extracted from the CSV file corresponding
                to that attribute.
         */
        Map<String, String> map = new HashMap<>();
        map.put("firstName", firstNameComboBox.getValue());
        map.put("middleName", middleNameComboBox.getValue());
        map.put("lastName", lastNameComboBox.getValue());
        map.put("batchName", batchNameComboBox.getValue());
        map.put("degree", degreeComboBox.getValue());
        map.put("discipline", disciplineComboBox.getValue());
        map.put("regYear", regYearComboBox.getValue());
        map.put("currSemester", currSemesterComboBox.getValue());
        map.put("gender", genderComboBox.getValue());
        map.put("dob", dobComboBox.getValue());
        map.put("guardianName", guardianNameComboBox.getValue());
        map.put("motherName", motherNameComboBox.getValue());
        map.put("address", addressComboBox.getValue());
        map.put("email", emailComboBox.getValue());
        map.put("contactNo", contactNoComboBox.getValue());
        map.put("guardianContactNo", guardianContactNoComboBox.getValue());
        map.put("regId", regIdComboBox.getValue());
        map.put("rollNo", rollNoComboBox.getValue());

        //display the loading spinner and fade the background
        mainGridPane.setOpacity(0.5);
        statusStackPane.setVisible(true);
        progressIndicator.setVisible(true);

        /*
        At first all the students of the CSV file is loaded into an ArrayList.
        Then a validation is run on the list of students to check if they comply the rules.
        Then all the students of the list is loaded into the DB.
         */
        Task<List<Student>> loadStudentFromCsvToMemoryTask = studentService
                .getLoadStudentFromCsvToMemoryTask(file, map);
        new Thread(loadStudentFromCsvToMemoryTask).start();

        loadStudentFromCsvToMemoryTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                List<Student> listOfStudentsFromCsv = new ArrayList<>();
                listOfStudentsFromCsv.addAll(loadStudentFromCsvToMemoryTask.getValue());

                //index of the row containing student details currently under inspection
                int currRowInCsv = 1;
                boolean csvDataStatus = false;
                for (Student student : listOfStudentsFromCsv) {

                    //validate the current row containing student details
                    csvDataStatus = validate(student, currRowInCsv++);

                    /*if any error found for any particular student in the csv ,
                    stop uploading the whole csv to db and display error msg
                     */
                    if (!csvDataStatus) {

                        deactivateProgressAndStatus();
                        break;
                    }
                }

                //no error found
                if (csvDataStatus) {

                    Task<Integer> addStudentFromMemoryToDataBaseTask = studentService
                            .getAddStudentFromMemoryToDataBaseTask(listOfStudentsFromCsv);
                    new Thread(addStudentFromMemoryToDataBaseTask).start();

                    addStudentFromMemoryToDataBaseTask.setOnSucceeded(new EventHandler<>() {
                        @Override
                        public void handle(WorkerStateEvent event) {

                            int status = addStudentFromMemoryToDataBaseTask.getValue();

                            //disable progress indicator and display status got from the method above
                            progressIndicator.setVisible(false);
                            statusImageView.setVisible(true);
                            statusLabel.setVisible(true);

                            if (status == DATABASE_ERROR) {
                                statusImageView.setImage(new Image("/png/critical error.png"));
                                statusLabel.setText("Database Error!");
                                tableUpdateStatus = false;
                            } else if (status == SUCCESS) {
                                statusImageView.setImage(new Image("/png/success.png"));
                                statusLabel.setText("Successfully added all students!");
                                tableUpdateStatus = true;
                            } else {
                                statusImageView.setImage(new Image("/png/error.png"));
                                statusLabel.setText("One or more students already exist!");
                                tableUpdateStatus = true;
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * This method validates a particular student.
     * @param student The student from Csv file.
     * @param currStudentIndex Current index of the student in the csv file.
     * @return The validation status.
     */
    private boolean validate(Student student, int currStudentIndex) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (student.getDegree() == null || student.getDegree().trim().isEmpty()) {
            alert.setContentText("Degree cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateAcademicItem(student.getDegree().trim())) {
            alert.setContentText("Invalid Degree in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (student.getDiscipline() == null || student.getDiscipline().trim().isEmpty()) {
            alert.setContentText("Discipline cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateAcademicItem(student.getDiscipline().trim())) {
            alert.setContentText("Invalid Discipline in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (student.getBatchName() == null || student.getBatchName().trim().isEmpty()) {
            alert.setContentText("Batch cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateBatchName(student.getBatchName().trim())) {
            alert.setContentText("Invalid Batch in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (student.getCurrSemester() == null || student.getCurrSemester().trim().isEmpty()) {
            alert.setContentText("Semester cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateSemester(student.getCurrSemester().trim())) {
            alert.setContentText("Invalid Semester in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (student.getRegYear() == null || student.getRegYear().trim().isEmpty()) {
            alert.setContentText("Registration Year cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateRegYear(student.getBatchName(), student.getRegYear())) {
            alert.setContentText("Invalid Registration Year or not within batch range in Row : "
                    + currStudentIndex + "!");
            System.out.println(student.getBatchName());
            System.out.println(student.getRegYear());
            alert.show();
            return false;
        } else if (student.getRegId() == null || student.getRegId().trim().isEmpty()) {
            alert.setContentText("Registration ID cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateId(student.getRegId().trim())) {
            alert.setContentText("Invalid Registration ID in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (student.getRollNo() == null || student.getRollNo().trim().isEmpty()) {
            alert.setContentText("Roll No. cannot be empty in Row : " + currStudentIndex + "!");
            return false;
        } else if (!ValidatorUtil.validateId(student.getRollNo().trim())) {
            alert.setContentText("Invalid Roll No. in Row : " + currStudentIndex + "!");
            return false;
        } else if (student.getFirstName() == null || student.getFirstName().trim().isEmpty()) {
            alert.setContentText("First Name cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateName(student.getFirstName().trim())) {
            alert.setContentText("Invalid First Name in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!student.getMiddleName().trim().isEmpty()) {

            if (!ValidatorUtil.validateName(student.getMiddleName().trim())) {
                alert.setContentText("Invalid Middle Name in Row : " + currStudentIndex + "!");
                alert.show();
                return false;
            } else
                return true;
        } else if (!student.getLastName().trim().isEmpty()) {

            if (!ValidatorUtil.validateName(student.getLastName().trim())) {
                alert.setContentText("Invalid Last Name in Row : " + currStudentIndex + "!");
                alert.show();
                return false;
            } else
                return true;
        } else if (student.getDob() == null || student.getDob().trim().isEmpty()) {
            alert.setContentText("DOB cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateDateFormat(student.getDob().trim())) {
            alert.setContentText("Invalid DOB format in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (student.getGender() == null || student.getGender().trim().isEmpty()) {
            alert.setContentText("Gender cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateGender(student.getGender().trim())) {
            alert.setContentText("Invalid Gender in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            alert.setContentText("Email ID cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateEmail(student.getEmail().trim())) {
            alert.setContentText("Invalid Email ID in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (student.getContactNo() == null || student.getContactNo().trim().isEmpty()) {
            alert.setContentText("Contact No. cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateContactNo(student.getContactNo().trim())) {
            alert.setContentText("Invalid Contact No. in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (student.getAddress() == null || student.getAddress().trim().isEmpty()) {
            alert.setContentText("Address cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (student.getGuardianName() == null || student.getGuardianName().trim().isEmpty()) {
            alert.setContentText("Guardian/Father's Name cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateName(student.getGuardianName().trim())) {
            alert.setContentText("Invalid Guardian/Father's Name in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!student.getMotherName().trim().isEmpty()) {

            if (!ValidatorUtil.validateName(student.getMotherName().trim())) {
                alert.setContentText("Invalid Mother's Name in Row : " + currStudentIndex + "!");
                alert.show();
                return false;
            } else
                return true;
        } else if (student.getGuardianContactNo() == null || student.getGuardianContactNo().trim().isEmpty()) {
            alert.setContentText("Guardian Contact No. cannot be empty in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateContactNo(student.getGuardianContactNo().trim())) {
            alert.setContentText("Invalid Guardian Contact No. in Row : " + currStudentIndex + "!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * Callback method for statusAnchorPane Mouse Clicked.
     *
     * On clicking the statusAnchorPane it will go away and
     * mainGridPane will be normal from faded.
     */
    @FXML
    private void handleStatusStackPaneMouseClickedAction() {

        deactivateProgressAndStatus();
    }

    /**
     * This method deactivates the progress indicator and the status.
     */
    private void deactivateProgressAndStatus() {

        mainGridPane.setOpacity(1);
        progressIndicator.setVisible(false);
        statusImageView.setVisible(false);
        statusLabel.setVisible(false);
        statusStackPane.setVisible(false);
        unSetComboBoxes();
        chosenFileLabel.setText("");
        submitButton.setDisable(true);
    }

    /**
     * Method for setting the ComboBoxes list of items.
     *
     * @param list List of columns extracted from the CSV file.
     */
    @SuppressWarnings("Duplicates")
    private void setComboBoxes(List<String> list) {

        ObservableList<String> options = FXCollections.observableArrayList(list);

        firstNameComboBox.setDisable(false);
        firstNameComboBox.setItems(options);
        firstNameComboBox.setValue(list.get(0));

        middleNameComboBox.setDisable(false);
        middleNameComboBox.setItems(options);
        middleNameComboBox.setValue(list.get(1));

        lastNameComboBox.setDisable(false);
        lastNameComboBox.setItems(options);
        lastNameComboBox.setValue(list.get(2));

        batchNameComboBox.setDisable(false);
        batchNameComboBox.setItems(options);
        batchNameComboBox.setValue(list.get(3));

        degreeComboBox.setDisable(false);
        degreeComboBox.setItems(options);
        degreeComboBox.setValue(list.get(4));

        disciplineComboBox.setDisable(false);
        disciplineComboBox.setItems(options);
        disciplineComboBox.setValue(list.get(5));

        regYearComboBox.setDisable(false);
        regYearComboBox.setItems(options);
        regYearComboBox.setValue(list.get(6));

        currSemesterComboBox.setDisable(false);
        currSemesterComboBox.setItems(options);
        currSemesterComboBox.setValue(list.get(7));

        regIdComboBox.setDisable(false);
        regIdComboBox.setItems(options);
        regIdComboBox.setValue(list.get(8));

        rollNoComboBox.setDisable(false);
        rollNoComboBox.setItems(options);
        rollNoComboBox.setValue(list.get(9));

        genderComboBox.setDisable(false);
        genderComboBox.setItems(options);
        genderComboBox.setValue(list.get(10));

        dobComboBox.setDisable(false);
        dobComboBox.setItems(options);
        dobComboBox.setValue(list.get(11));

        guardianNameComboBox.setDisable(false);
        guardianNameComboBox.setItems(options);
        guardianNameComboBox.setValue(list.get(12));

        motherNameComboBox.setDisable(false);
        motherNameComboBox.setItems(options);
        motherNameComboBox.setValue(list.get(13));

        addressComboBox.setDisable(false);
        addressComboBox.setItems(options);
        addressComboBox.setValue(list.get(14));

        contactNoComboBox.setDisable(false);
        contactNoComboBox.setItems(options);
        contactNoComboBox.setValue(list.get(15));

        emailComboBox.setDisable(false);
        emailComboBox.setItems(options);
        emailComboBox.setValue(list.get(16));

        guardianContactNoComboBox.setDisable(false);
        guardianContactNoComboBox.setItems(options);
        guardianContactNoComboBox.setValue(list.get(17));
    }

    /**
     * Method for disabling and un-setting the ComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    private void unSetComboBoxes() {

        firstNameComboBox.setDisable(true);
        firstNameComboBox.setValue("");

        middleNameComboBox.setDisable(true);
        middleNameComboBox.setValue("");

        lastNameComboBox.setDisable(true);
        lastNameComboBox.setValue("");

        batchNameComboBox.setDisable(true);
        batchNameComboBox.setValue("");

        degreeComboBox.setDisable(true);
        degreeComboBox.setValue("");

        disciplineComboBox.setDisable(true);
        disciplineComboBox.setValue("");

        regYearComboBox.setDisable(true);
        regYearComboBox.setValue("");

        currSemesterComboBox.setDisable(true);
        currSemesterComboBox.setValue("");

        regIdComboBox.setDisable(true);
        regIdComboBox.setValue("");

        rollNoComboBox.setDisable(true);
        rollNoComboBox.setValue("");

        genderComboBox.setDisable(true);
        genderComboBox.setValue("");

        dobComboBox.setDisable(true);
        dobComboBox.setValue("");

        guardianNameComboBox.setDisable(true);
        guardianNameComboBox.setValue("");

        motherNameComboBox.setDisable(true);
        motherNameComboBox.setValue("");

        addressComboBox.setDisable(true);
        addressComboBox.setValue("");

        contactNoComboBox.setDisable(true);
        contactNoComboBox.setValue("");

        emailComboBox.setDisable(true);
        emailComboBox.setValue("");

        guardianContactNoComboBox.setDisable(true);
        guardianContactNoComboBox.setValue("");
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

    /**
     * This method returns the status of the TableView updation.
     *
     * @return The status which determines whether the TableView in
     * StudentsList.fxml will be updated or not.
     */
    boolean getTableUpdateStatus() {

        return tableUpdateStatus;
    }

}

