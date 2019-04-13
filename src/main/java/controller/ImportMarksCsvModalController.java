package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import model.Marks;
import service.FileHandlingService;
import service.MarksService;
import util.CSVUtil;
import util.ValidatorUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

public class ImportMarksCsvModalController {

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
    private TextFlow csvInstructionsTextFlow;

    @FXML
    private Button chooseFileButton;

    @FXML
    private Label chosenFileLabel;

    @FXML
    private Hyperlink sampleCsvHyperLink;

    @FXML
    private ComboBox<String> regIdComboBox;

    @FXML
    private ComboBox<String> obtainedMarksComboBox;

    @FXML
    private Button submitButton;

    private MarksService marksService;
    private FileHandlingService fileHandlingService;
    private boolean tableUpdateStatus;
    private File file;
    private String courseId;
    private String subId;

    @FXML
    public void initialize() {

        marksService = new MarksService();

        fileHandlingService = new FileHandlingService();
        //by default table of the studentList will not be updated
        tableUpdateStatus = false;

        //setting the csv import instruction
        Text text1 = new Text("File must be comma delimited CSV file\n");
        Text text2 = new Text("In MS Excel save as comma delimited CSV file\n");
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
            if (list.size() == 2) {

                setComboBoxes(list);
                submitButton.setDisable(false);
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("CSV file should have 2 columns!");
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
    @FXML
    private void handleSampleCsvHyperLinkAction() {

        /*
        Couldn't find a way to open the CSV file from a relative file path.Also file opening doesn't work inside the JAR
        package,so I had to get the input stream of the sample csv , create a new file in the USER's system in the
        predefined location , copy the input stream to the new file.
        I wish I could have found a better way!
         */

        //location of the sample CSV
        String sampleCsvFilePath = "/csv/marksSample.csv";

        //location where the new csv will be created in the user's system
        String filePath = USER_HOME + FILE_SEPARATOR + ROOT_DIR + FILE_SEPARATOR
                + CSV_DIR + FILE_SEPARATOR + "marksSample.csv";

        try {

            //if studentSample.csv doesn't exist in the User's System , then only create it
            if (!Paths.get(filePath).toFile().exists()) {

                //get the content of the sampleCsv File as InputStream
                InputStream in = getClass().getResourceAsStream(sampleCsvFilePath);

                //create the new csv file in the user's system and copy the contents of sampleCsv file to it
                Task<Boolean> createAndWriteToFileTask = fileHandlingService.getCreateAndWriteToFileTask
                        (in.readAllBytes(), CSV_DIR, "marksSample.csv");
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
    @FXML
    private void handleSubmitButtonAction() {

        /*
        Create a HashMap and set up the following :
        Key : Student's Attribute.
        Value : The name of the column extracted from the CSV file corresponding
                to that attribute.
         */
        Map<String, String> map = new HashMap<>();
        map.put("regId", regIdComboBox.getValue());
        map.put("obtainedMarks", obtainedMarksComboBox.getValue());

        //display the loading spinner and fade the background
        mainGridPane.setOpacity(0.5);
        statusStackPane.setVisible(true);
        progressIndicator.setVisible(true);

        /*
        At first all the students of the CSV file is loaded into an ArrayList.
        Then a validation is run on the list of students to check if they comply the rules.
        Then all the students of the list is loaded into the DB.
         */
        Task<List<Marks>> loadMarksFromCsvToMemoryTask = marksService.getLoadMarksFromCsvToMemoryTask(file, map);
        new Thread(loadMarksFromCsvToMemoryTask).start();

        loadMarksFromCsvToMemoryTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                List<Marks> marksListFromCsv = new ArrayList<>(loadMarksFromCsvToMemoryTask.getValue());

                //index of the row containing student details currently under inspection
                int currRowInCsv = 1;
                boolean csvDataStatus = false;
                for (Marks marks : marksListFromCsv) {

                    //validate the current row containing student details
                    csvDataStatus = validate(marks, currRowInCsv++);

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

                    Task<Integer> addMarksFromMemoryToDataBaseTask = marksService.getAddMarksFromMemoryToDataBaseTask
                            (marksListFromCsv, courseId, subId);
                    new Thread(addMarksFromMemoryToDataBaseTask).start();

                    addMarksFromMemoryToDataBaseTask.setOnSucceeded(new EventHandler<>() {
                        @Override
                        public void handle(WorkerStateEvent event) {

                            //get the status of the INSERT operation
                            int status = addMarksFromMemoryToDataBaseTask.getValue();

                            //disable progress indicator and display status got from the method above
                            progressIndicator.setVisible(false);
                            statusImageView.setVisible(true);
                            statusLabel.setVisible(true);

                            if (status == DATABASE_ERROR) {

                                statusImageView.setImage(new javafx.scene.image.Image("/png/critical error.png"));
                                statusLabel.setText("Database Error!");
                                tableUpdateStatus = false;
                            } else if (status == SUCCESS) {

                                statusImageView.setImage(new javafx.scene.image.Image("/png/success.png"));
                                statusLabel.setText("Successfully added marks to students!");
                                tableUpdateStatus = true;
                            }
                            else if (status == 0) {

                                statusImageView.setImage(new javafx.scene.image.Image("/png/error.png"));
                                statusLabel.setText("No students with the provided Reg Id gives this exam!");
                                tableUpdateStatus = false;
                            } else {

                                statusImageView.setImage(new Image("/png/error.png"));
                                statusLabel.setText("Marks added to " + status + "students who have given this exam !");
                                tableUpdateStatus = true;
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean validate(Marks marks, int currMarksIndex) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (marks.getRegId() == null || marks.getRegId().trim().isEmpty()) {

            alert.setContentText("Registration ID cannot be empty in Row : " + currMarksIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateId(marks.getRegId().trim())) {

            alert.setContentText("Invalid Registration ID in Row : " + currMarksIndex + "!");
            alert.show();
            return false;
        }
        if (marks.getObtainedMarks() == null || marks.getObtainedMarks().trim().isEmpty()) {

            alert.setContentText("Obtained Marks cannot be empty in Row : " + currMarksIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateNumber(marks.getObtainedMarks().trim())) {

            alert.setContentText("Invalid Obtained Marks in Row : " + currMarksIndex + "!");
            alert.show();
            return false;
        }
        return true;
    }


    /**
     * Callback method for statusAnchorPane Mouse Clicked.
     * <p>
     * On clicking the statusAnchorPane it will go away and mainGridPane will be normal from faded and all the
     * comboBoxes will be unset.
     */
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



    private void setComboBoxes(List<String> list) {

        ObservableList<String> options = FXCollections.observableArrayList(list);

        regIdComboBox.setDisable(false);
        regIdComboBox.setItems(options);
        regIdComboBox.setValue(list.get(0));

        obtainedMarksComboBox.setDisable(false);
        obtainedMarksComboBox.setItems(options);
        obtainedMarksComboBox.setValue(list.get(1));
    }

    /**
     * Method for disabling and un-setting the ComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    private void unSetComboBoxes() {

        regIdComboBox.setDisable(true);
        regIdComboBox.setValue("");

        obtainedMarksComboBox.setDisable(true);
        obtainedMarksComboBox.setValue("");
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
     * StudentSection.fxml will be updated or not.
     */
    public boolean getTableUpdateStatus() {

        return tableUpdateStatus;
    }

    public void setSubjectDetails(String courseId, String subId){

        this.courseId = courseId;
        this.subId = subId;
    }
}

