package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import model.Professor;
import service.FileHandlingService;
import service.ProfessorService;
import util.CSVUtil;
import util.ValidatorUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;


/**
 * Controller class for ImportProfessorCSVModal.fxml.
 *
 * @author Sourav Debnath
 */
public class ImportProfessorCSVModalController {

    /*---------------------------------Initialization and declaration of variables-------------------------------*/

    private ProfessorService professorService;
    private File file;
    private boolean tableUpdateStatus;
    private FileHandlingService fileHandlingService;

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
    private ComboBox<String> dobComboBox;

    @FXML
    private ComboBox<String> deptComboBox;

    @FXML
    private ComboBox<String> profIdComboBox;

    @FXML
    private ComboBox<String> addressComboBox;

    @FXML
    private ComboBox<String> emailIdComboBox;

    @FXML
    private ComboBox<String> dojComboBox;

    @FXML
    private ComboBox<String> highestQualificationComboBox;

    @FXML
    private ComboBox<String> contactNoComboBox;

    @FXML
    private ComboBox<String> hodStatusComboBox;

    /*-----------------------------------End of declaration and initialization----------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @SuppressWarnings("Duplicates")
    public void initialize() {

        professorService = new ProfessorService();

        fileHandlingService = new FileHandlingService();
        //by default table of the professorList will not be updated
        tableUpdateStatus = false;
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
    @SuppressWarnings("Duplicates")
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

            //checking if all 12 columns are present in the csv file uploaded
            if (list.size() == 13) {
                setComboBoxes(list);
                submitButton.setDisable(false);
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("CSV file should have 12 columns!");
                alert.show();
                chosenFileLabel.setText("");
                submitButton.setDisable(true);
                unSetComboBoxes();
            }
        }
    }


    /**
     * Callback method to handle SampleCsvHyperLinkAction.
     * <p>
     * Basically this method opens the sample CSV in the User's System with an appropriate application.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSampleCsvHyperLinkAction() {

        /*
        Couldn't find a way to open the CSV file from a relative file path.Also file opening doesn't work inside the JAR
        package,so I had to get the input stream of the sample csv , create a new file in the USER's system in the
        predefined location , copy the input stream to the new file.
        I wish I could have found a better way!
         */

        //location of the sample CSV
        String sampleCsvFilePath = "/csv/professorSample.csv";

        //location where the new csv will be created in the user's system
        String filePath = USER_HOME + FILE_SEPARATOR + ROOT_DIR + FILE_SEPARATOR
                + CSV_DIR + FILE_SEPARATOR + "professorSample.csv";

        try {

            //if professorSample.csv doesn't exist in the User's System , then only create it
            if (Files.notExists(Paths.get(filePath))) {

                //get the content of the sampleCsv File as InputStream
                InputStream in = getClass().getResourceAsStream(sampleCsvFilePath);

                //create the new csv file in the user's system and copy the contents of sampleCsv file to it
                Task<Boolean> createAndWriteToFileTask = fileHandlingService.getCreateAndWriteToFileTask
                        (in.readAllBytes(), CSV_DIR, "professorSample.csv");
                new Thread(createAndWriteToFileTask).start();
            }

            //open the CSV file with the appropriate Application available in the user's system
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    /**
     * Callback method for submitButton.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitButtonAction() {

        /*
        Create a HashMap and set up the following :
        Key : Professor's Attribute.
        Value : The name of the column extracted from the CSV file corresponding
                to that attribute.
         */
        Map<String, String> map = new HashMap<>();
        map.put("firstName", firstNameComboBox.getValue());
        map.put("middleName", middleNameComboBox.getValue());
        map.put("lastName", lastNameComboBox.getValue());
        map.put("dob", dobComboBox.getValue());
        map.put("deptName", deptComboBox.getValue());
        map.put("profId", profIdComboBox.getValue());
        map.put("address", addressComboBox.getValue());
        map.put("email", emailIdComboBox.getValue());
        map.put("doj", dojComboBox.getValue());
        map.put("highestQualification", highestQualificationComboBox.getValue());
        map.put("contactNo", contactNoComboBox.getValue());
        map.put("hodStatus", hodStatusComboBox.getValue());


        //display the loading spinner and fade the background
        mainGridPane.setOpacity(0.5);
        statusStackPane.setVisible(true);
        progressIndicator.setVisible(true);


        /*
        At first all the professors of the CSV file is loaded into an ArrayList.
        Then a validation is run on the list of professors to check if they comply the rules.
        Then all the professors of the list is loaded into the DB.
         */

        Task<List<Professor>> loadProfessorFromCsvToMemoryTask = professorService
                .getLoadProfessorFromCsvToMemoryTask(file, map);
        new Thread(loadProfessorFromCsvToMemoryTask).start();

        loadProfessorFromCsvToMemoryTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                List<Professor> listOfProfessorsFromCsv = new ArrayList<>(loadProfessorFromCsvToMemoryTask.getValue());

                //index of the row containing professor details currently under inspection
                int currRowInCsv = 1;
                boolean csvDataStatus = false;
                for (Professor professor : listOfProfessorsFromCsv) {

                    //validate the current row containing professor details
                    csvDataStatus = validate(professor, currRowInCsv++);

                    /*if any error found for any particular professor in the csv ,
                    stop uploading the whole csv to db and display error msg
                     */
                    if (!csvDataStatus) {

                        deactivateProgressAndStatus();
                        break;
                    }
                }


                if (csvDataStatus) {

                    Task<Integer> addProfessorFromMemoryToDatabaseTask = professorService
                            .getAddProfessorFromMemoryToDataBaseTask(listOfProfessorsFromCsv);
                    new Thread(addProfessorFromMemoryToDatabaseTask).start();

                    addProfessorFromMemoryToDatabaseTask.setOnSucceeded(new EventHandler<>() {
                        @Override
                        public void handle(WorkerStateEvent event) {

                            //get the status of the INSERT operation
                            int status = addProfessorFromMemoryToDatabaseTask.getValue();

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
                                statusLabel.setText("Successfully added all professors!");
                                tableUpdateStatus = true;
                            } else if (status == 0) {

                                statusImageView.setImage(new Image("/png/error.png"));
                                statusLabel.setText("All professors in the CSV already exists in the database!");
                                tableUpdateStatus = false;
                            } else {

                                statusImageView.setImage(new Image("/png/error.png"));
                                statusLabel.setText(status + " professors added to the database!");
                                tableUpdateStatus = true;
                            }
                        }
                    });
                }
            }
        });

    }


    /**
     * This method validates a particular professor.
     *
     * @param professor          The student from Csv file.
     * @param currProfessorIndex Current index of the professor in the csv file.
     * @return The validation status.
     */
    @SuppressWarnings("Duplicates")
    private boolean validate(Professor professor, int currProfessorIndex) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (professor.getDeptName() == null || professor.getDeptName().trim().isEmpty()) {

            alert.setContentText("Department cannot be empty in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(professor.getDeptName().trim())) {

            alert.setContentText("Invalid Department in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (professor.getHighestQualification() == null || professor.getHighestQualification().trim().isEmpty()) {

            alert.setContentText("Highest Qualification cannot be empty in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(professor.getHighestQualification().trim())) {

            alert.setContentText("Invalid Highest Qualification in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (professor.getHodStatus() == null || professor.getHodStatus().trim().isEmpty()) {

            alert.setContentText("Hod Status cannot be empty in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(professor.getHodStatus().trim())) {

            alert.setContentText("Invalid Hod Status in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (professor.getProfId() == null || professor.getProfId().trim().isEmpty()) {

            alert.setContentText("Professor ID cannot be empty in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateId(professor.getProfId().trim())) {

            alert.setContentText("Invalid Professor ID in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (professor.getFirstName() == null || professor.getFirstName().trim().isEmpty()) {

            alert.setContentText("First Name cannot be empty in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateName(professor.getFirstName().trim())) {

            alert.setContentText("Invalid First Name in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (!professor.getMiddleName().trim().isEmpty()) {

            if (!ValidatorUtil.validateName(professor.getMiddleName().trim())) {

                alert.setContentText("Invalid Middle Name in Row : " + currProfessorIndex + "!");
                alert.show();
                return false;
            }
        }
        if (!professor.getLastName().trim().isEmpty()) {

            if (!ValidatorUtil.validateName(professor.getLastName().trim())) {

                alert.setContentText("Invalid Last Name in Row : " + currProfessorIndex + "!");
                alert.show();
                return false;
            }
        }
        if (professor.getDob() == null || professor.getDob().trim().isEmpty()) {

            alert.setContentText("DOB cannot be empty in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateDateFormat(professor.getDob().trim())) {

            alert.setContentText("Invalid DOB format in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (professor.getDoj() == null || professor.getDoj().trim().isEmpty()) {

            alert.setContentText("DOJ cannot be empty in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateDateFormat(professor.getDoj().trim())) {

            alert.setContentText("Invalid DOJ format in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (professor.getEmail() == null || professor.getEmail().trim().isEmpty()) {

            alert.setContentText("Email ID cannot be empty in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateEmail(professor.getEmail().trim())) {

            alert.setContentText("Invalid Email ID in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (professor.getContactNo() == null || professor.getContactNo().trim().isEmpty()) {

            alert.setContentText("Contact No. cannot be empty in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateContactNo(professor.getContactNo().trim())) {

            alert.setContentText("Invalid Contact No. in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        if (professor.getAddress() == null || professor.getAddress().trim().isEmpty()) {

            alert.setContentText("Address cannot be empty in Row : " + currProfessorIndex + "!");
            alert.show();
            return false;
        }
        return true;
    }


    /**
     * Callback method for statusAnchorPane Mouse Clicked.
     * <p>
     * On clicking the statusAnchorPane it will go away and mainGridPane will be normal from faded.
     * comboBoxes will be unset
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleStatusStackPaneMouseClickedAction() {

        deactivateProgressAndStatus();
    }


    /**
     * This method deactivates the progress indicator,status and un-sets the comboboxes.
     */
    @SuppressWarnings("Duplicates")
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

        dobComboBox.setDisable(false);
        dobComboBox.setItems(options);
        dobComboBox.setValue(list.get(3));

        deptComboBox.setDisable(false);
        deptComboBox.setItems(options);
        deptComboBox.setValue(list.get(4));

        profIdComboBox.setDisable(false);
        profIdComboBox.setItems(options);
        profIdComboBox.setValue(list.get(5));

        addressComboBox.setDisable(false);
        addressComboBox.setItems(options);
        addressComboBox.setValue(list.get(6));

        emailIdComboBox.setDisable(false);
        emailIdComboBox.setItems(options);
        emailIdComboBox.setValue(list.get(7));

        dojComboBox.setDisable(false);
        dojComboBox.setItems(options);
        dojComboBox.setValue(list.get(8));

        highestQualificationComboBox.setDisable(false);
        highestQualificationComboBox.setItems(options);
        highestQualificationComboBox.setValue(list.get(9));

        contactNoComboBox.setDisable(false);
        contactNoComboBox.setItems(options);
        contactNoComboBox.setValue(list.get(10));

        hodStatusComboBox.setDisable(false);
        hodStatusComboBox.setItems(options);
        hodStatusComboBox.setValue(list.get(11));

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

        dobComboBox.setDisable(true);
        dobComboBox.setValue("");

        deptComboBox.setDisable(true);
        deptComboBox.setValue("");

        profIdComboBox.setDisable(true);
        profIdComboBox.setValue("");

        addressComboBox.setDisable(true);
        addressComboBox.setValue("");

        emailIdComboBox.setDisable(true);
        emailIdComboBox.setValue("");

        dojComboBox.setDisable(true);
        dojComboBox.setValue("");

        highestQualificationComboBox.setDisable(true);
        highestQualificationComboBox.setValue("");

        contactNoComboBox.setDisable(true);
        contactNoComboBox.setValue("");

        hodStatusComboBox.setDisable(true);
        hodStatusComboBox.setValue("");

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
     * ProfessorSection.fxml will be updated or not.
     */
    boolean getTableUpdateStatus() {

        return tableUpdateStatus;
    }
}
