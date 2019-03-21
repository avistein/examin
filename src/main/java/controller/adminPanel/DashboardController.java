package controller.adminPanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import model.Holiday;
import service.*;
import util.CSVUtil;
import util.ValidatorUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

import static util.ConstantsUtil.*;

/**
 * Controller for /adminPanel/Dashboard.fxml
 *
 * @author Avik Sarkar
 */
public class DashboardController {

    /*-----------------------------------End of declaration and initialization----------------------------------*/

    @FXML
    private Label totalStudentsLabel;

    @FXML
    private Label totalProfessorsLabel;

    @FXML
    private Label totalExamCellMembersLabel;

    @FXML
    private Label totalOfficeMembersLabel;

    @FXML
    private Label totalDeptsLabel;

    @FXML
    private Label totalCoursesLabel;

    @FXML
    private Label totalBatchesLabel;

    @FXML
    private Label totalSubjectsLabel;

    @FXML
    private Label totalClassroomsLabel;

    @FXML
    private Button chooseFileButton;

    @FXML
    private Label fileChosenLabel;

    @FXML
    private ComboBox<String> holidayIdComboBox;

    @FXML
    private ComboBox<String> holidayNameComboBox;

    @FXML
    private ComboBox<String> startDateComboBox;

    @FXML
    private ComboBox<String> endDateComboBox;

    @FXML
    private Button submitHolidayButton;

    @FXML
    private HBox importCsvHboxButtons;

    @FXML
    private TableView<Holiday> holidaysTableView;

    @FXML
    private TableColumn<Holiday, String> holidayIdCol;

    @FXML
    private TableColumn<Holiday, String> holidayNameCol;

    @FXML
    private TableColumn<Holiday, String> startDateCol;

    @FXML
    private TableColumn<Holiday, String> endDateCol;

    @FXML
    private GridPane importHolidaysCsvMainGridPane;

    @FXML
    private StackPane importHolidayCsvStatusStackPane;

    @FXML
    private ProgressIndicator importHolidayCsvProgressIndicator;

    @FXML
    private ImageView importHolidayCsvStatusImageView;

    @FXML
    private Label importHolidayCsvStatusLabel;

    @FXML
    private GridPane holidaysListMainGridPane;

    @FXML
    private StackPane holidaysListStatusStackPane;

    @FXML
    private ProgressIndicator holidaysListProgressIndicator;

    @FXML
    private ImageView holidayListStatusImageView;

    @FXML
    private Label holidaysListStatusLabel;

    @FXML
    private HBox holidaysListHboxButtons;

    private HolidayService holidayService;

    private StudentService studentService;

    private BatchService batchService;

    private CourseService courseService;

    private SubjectService subjectService;

    private DepartmentService departmentService;

    private ExamCellMemberService examCellMemberService;

    private ProfessorService professorService;

    private FileHandlingService fileHandlingService;

    private ObservableList<Holiday> holidayObsList;

    private File file;

    private Scene adminPanelScene;

    /*-----------------------------------End of declaration and initialization----------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    private void initialize() {

        holidayService = new HolidayService();
        studentService = new StudentService();
        subjectService = new SubjectService();
        batchService = new BatchService();
        courseService = new CourseService();
        departmentService = new DepartmentService();
        examCellMemberService = new ExamCellMemberService();
        professorService = new ProfessorService();
        fileHandlingService = new FileHandlingService();

        //for the tableView
        holidayObsList = FXCollections.observableArrayList();

        /*
        Sets total count of item in the respective labels.
         */
        updateTotalBatchesCount();
        updateTotalCoursesCount();
        updateTotalDeptsCount();
        updateTotalExamCellMembersCount();
        updateTotalStudentsCount();
        updateTotalProfessorsCount();
        updateTotalSubjectsCount();

        /*
        Initialize columns and populate tableView.
         */
        initHolidayCols();
        populateHolidayTable();
    }

    /**
     * Callback method to open the StudentList.fxml UI when clicked on the Vbox.
     */
    @FXML
    private void handleTotalStudentsVboxOnMouseClickedAction() {

        adminPanelScene = chooseFileButton.getScene();
        Button studentListButton = (Button) (adminPanelScene.lookup("#studentButton"));
        studentListButton.fire();
    }

    /**
     * Callback method to open the /adminPanel/ProfessorList.fxml UI when clicked on the Vbox.
     */
    @FXML
    private void handleTotalProfessorsVboxOnMouseClickedAction() {

        adminPanelScene = chooseFileButton.getScene();
        Button professorListButton = (Button) (adminPanelScene.lookup("#professorButton"));
        professorListButton.fire();
    }

    /**
     * Callback method to open the /adminPanel/ExamCellMemberList.fxml UI when clicked on the Vbox.
     */
    @FXML
    private void handleTotalExamCellMembersVboxOnMouseClickedAction() {

    }

    /**
     * Callback method to open the /adminPanel/OfficeMembers.fxml UI when clicked on the Vbox.
     */
    @FXML
    private void handleTotalOfficeMembersVboxOnMouseClickedAction() {

    }

    /**
     * Callback method to open the /adminPanel/AcademicAdministration.fxml UI when clicked on the Vbox.
     */
    @FXML
    private void handleTotalDepartmentsVboxOnMouseClickedAction() {

        adminPanelScene = chooseFileButton.getScene();
        Button academicAdministrationButton = (Button) (adminPanelScene.lookup("#academicAdministrationButton"));
        academicAdministrationButton.fire();
    }

    /**
     * Callback method to open the /adminPanel/AcademicAdministration.fxml UI when clicked on the Vbox.
     */
    @FXML
    private void handleTotalCoursesVboxOnMouseClickedAction() {

        adminPanelScene = chooseFileButton.getScene();
        Button academicAdministrationButton = (Button) (adminPanelScene.lookup("#academicAdministrationButton"));
        academicAdministrationButton.fire();
    }

    /**
     * Callback method to open the /adminPanel/AcademicAdministration.fxml UI when clicked on the Vbox.
     */
    @FXML
    private void handleTotalBatchesVboxOnMouseClickedAction() {

        adminPanelScene = chooseFileButton.getScene();
        Button academicAdministrationButton = (Button) (adminPanelScene.lookup("#academicAdministrationButton"));
        academicAdministrationButton.fire();
    }

    /**
     * Callback method to open the /adminPanel/AcademicAdministration.fxml UI when clicked on the Vbox.
     */
    @FXML
    private void handleTotalSubjectsVboxOnMouseClickedAction() {

        adminPanelScene = chooseFileButton.getScene();
        Button academicAdministrationButton = (Button) (adminPanelScene.lookup("#academicAdministrationButton"));
        academicAdministrationButton.fire();
    }

    /**
     * Callback method to open the /adminPanel/AcademicAdministration.fxmln UI when clicked on the Vbox.
     */
    @FXML
    private void handleTotalClassroomsVboxOnMouseClickedAction() {

        adminPanelScene = chooseFileButton.getScene();
        Button academicAdministrationButton = (Button) (adminPanelScene.lookup("#academicAdministrationButton"));
        academicAdministrationButton.fire();
    }

    /**
     * This method is used to count the total no. of Students in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalStudentsCount() {

        Task<Integer> studentsCountTask = studentService.getStudentsCountTask();
        new Thread(studentsCountTask).start();

        studentsCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalStudents = studentsCountTask.getValue();
                totalStudentsLabel.setText(Integer.toString(totalStudents));
            }
        });
    }

    /**
     * This method is used to count the total no. of Batches in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalBatchesCount() {

        Task<Integer> batchesCountTask = batchService.getBatchesCountTask();
        new Thread(batchesCountTask).start();

        batchesCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalBatches = batchesCountTask.getValue();
                totalBatchesLabel.setText(Integer.toString(totalBatches));
            }
        });
    }

    /**
     * This method is used to count the total no. of Departments in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalDeptsCount() {

        Task<Integer> deptsCountTask = departmentService.getDepartmentsCountTask();
        new Thread(deptsCountTask).start();

        deptsCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalDepts = deptsCountTask.getValue();
                totalDeptsLabel.setText(Integer.toString(totalDepts));
            }
        });
    }

    /**
     * This method is used to count the total no. of Subejcts in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalSubjectsCount() {

        Task<Integer> subjectsCountTask = subjectService.getSubjectsCountTask();
        new Thread(subjectsCountTask).start();

        subjectsCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalSubjects = subjectsCountTask.getValue();
                totalSubjectsLabel.setText(Integer.toString(totalSubjects));
            }
        });
    }

    /**
     * This method is used to count the total no. of Courses in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalCoursesCount() {

        Task<Integer> coursesCountTask = courseService.getCoursesCountTask();
        new Thread(coursesCountTask).start();

        coursesCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalCourses = coursesCountTask.getValue();
                totalCoursesLabel.setText(Integer.toString(totalCourses));
            }
        });
    }

    /**
     * This method is used to count the total no. of Courses in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalProfessorsCount() {

        Task<Integer> profsCountTask = professorService.getProfessorsCountTask();
        new Thread(profsCountTask).start();

        profsCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalProfs = profsCountTask.getValue();
                totalProfessorsLabel.setText(Integer.toString(totalProfs));
            }
        });
    }

    /**
     * This method is used to count the total no. of Exam Cell Members in the DB by getting the appropriate task
     * and update the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalExamCellMembersCount() {

        Task<Integer> examCellMembersCountTask = examCellMemberService.getExamCellMembersCountTask();
        new Thread(examCellMembersCountTask).start();

        examCellMembersCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalExamCellMembers = examCellMembersCountTask.getValue();
                totalExamCellMembersLabel.setText(Integer.toString(totalExamCellMembers));
            }
        });
    }

    /**
     * Callback method to allow the user to choose the CSV file and set the comboBoxes with the columns names in the
     * CSV file.
     */
    @FXML
    private void handleChooseFileButtonAction() {

        //open the file chooser window to allow the user to choose the CSV file
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());

        /*
        If a file is chosen get the column names from the file and set ComboBoxes
        with the column names' list else unset ComboBoxes.
         */
        if (file != null) {

            fileChosenLabel.setText(file.getName());

            //get the list of columns in the CSV files
            List<String> list = CSVUtil.getColumnNames(file);

            //4 columns must be there in the CSV
            if (list.size() == 4) {

                setComboBoxes(list);
                submitHolidayButton.setDisable(false);
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("CSV file should have 4 columns!");
                alert.show();
                fileChosenLabel.setText("");
                submitHolidayButton.setDisable(true);
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
        String sampleCsvFilePath = "/csv/holidaySample.csv";

        //location where the new csv will be created in the user's system
        String filePath = USER_HOME + FILE_SEPARATOR + ROOT_DIR + FILE_SEPARATOR
                + CSV_DIR + FILE_SEPARATOR + "holidaySample.csv";

        try {

            //if holidaySample.csv doesn't exist in the User's System , then only create it
            if (!Paths.get(filePath).toFile().exists()) {

                //get the content of the sampleCsv File as InputStream
                InputStream in = getClass().getResourceAsStream(sampleCsvFilePath);

                //create the new csv file in the user's system and copy the contents of sampleCsv file to it
                Task<Boolean> createAndWriteToFileTask = fileHandlingService.getCreateAndWriteToFileTask
                        (in.readAllBytes(), CSV_DIR, "holidaySample.csv");
                new Thread(createAndWriteToFileTask).start();

            }

            //open the csv file with the available application
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Callback method to handle Submit Button action.
     * <p>
     * Basically this method loads a CSV into an ArrayList first and then loads that ArrayList data into the
     * database.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitHolidayButtonAction() {

        /*
        Create a HashMap and set up the following :
        Key : Holiday's Attribute.
        Value : The name of the column extracted from the CSV file corresponding
                to that attribute.
         */
        Map<String, String> map = new HashMap<>();
        map.put("holidayId", holidayIdComboBox.getValue());
        map.put("holidayName", holidayNameComboBox.getValue());
        map.put("startDate", startDateComboBox.getValue());
        map.put("endDate", endDateComboBox.getValue());

        //background faded and loading spinner visible
        importHolidaysCsvMainGridPane.setOpacity(0.5);
        importHolidayCsvStatusStackPane.setVisible(true);
        importHolidayCsvProgressIndicator.setVisible(true);

        Task<List<Holiday>> loadHolidaysFromCsvToMemoryTask = holidayService
                .getLoadHolidaysFromCsvToMemoryTask(file, map);
        new Thread(loadHolidaysFromCsvToMemoryTask).start();

        loadHolidaysFromCsvToMemoryTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //get the list of holidays from the CSV file
                List<Holiday> listOfHolidaysFromCsv = new ArrayList<>(loadHolidaysFromCsvToMemoryTask.getValue());

                //initially at row 1
                int currRowInCsv = 1;

                //by default validation status is false
                boolean csvDataStatus = false;

                //validate each Holiday in the list
                for (Holiday holiday : listOfHolidaysFromCsv) {

                    csvDataStatus = validate(holiday, currRowInCsv++);

                    //validation fails, so disable Progress Indicator and show status
                    if (!csvDataStatus) {

                        deactivateImportHolidaysCsvProgressAndStatus();
                        break;
                    }
                }

                //all Holidays the list have passed validation
                if (csvDataStatus) {

                    //load the Holidays from ArrayList to database
                    Task<Integer> addHolidayFromMemoryToDataBaseTask = holidayService
                            .getAddHolidayFromMemoryToDataBaseTask(listOfHolidaysFromCsv);
                    new Thread(addHolidayFromMemoryToDataBaseTask).start();

                    addHolidayFromMemoryToDataBaseTask.setOnSucceeded(new EventHandler<>() {
                        @Override
                        public void handle(WorkerStateEvent event) {

                            int status = addHolidayFromMemoryToDataBaseTask.getValue();

                            importHolidayCsvProgressIndicator.setVisible(false);
                            importHolidayCsvStatusImageView.setVisible(true);
                            importHolidayCsvStatusLabel.setVisible(true);
                            importCsvHboxButtons.setVisible(true);

                            //display status
                            if (status == DATABASE_ERROR) {

                                importHolidayCsvStatusImageView.setImage(new Image("/png/critical error.png"));
                                importHolidayCsvStatusLabel.setText("Database Error!");
                            } else if (status == SUCCESS) {

                                importHolidayCsvStatusImageView.setImage(new Image("/png/success.png"));
                                importHolidayCsvStatusLabel.setText("Successfully added all Holidays!");
                                populateHolidayTable();
                            } else if (status == 0) {

                                importHolidayCsvStatusImageView.setImage(new Image("/png/error.png"));
                                importHolidayCsvStatusLabel.setText("All holidays in the CSV already exists in the " +
                                        "database!");
                            } else {

                                importHolidayCsvStatusImageView.setImage(new Image("/png/error.png"));
                                importHolidayCsvStatusLabel.setText(status + " holidays added to the database!");
                                populateHolidayTable();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Callback method to disable the Progress Indicator and status when clicked on the OK button.
     */
    @FXML
    private void handleImportCsvOkButtonAction() {

        deactivateImportHolidaysCsvProgressAndStatus();
    }

    /**
     * This method is used to validate items of the Holiday objects in the CSV file.
     *
     * @param holiday          The holiday object to be validated.
     * @param currHolidayIndex Current index of the Holiday object in the CSV file.
     * @return The validation result.
     */
    private boolean validate(Holiday holiday, int currHolidayIndex) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (holiday.getHolidayId() == null || holiday.getHolidayId().trim().isEmpty()) {

            alert.setContentText("Holiday ID cannot be empty in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateId(holiday.getHolidayId().trim())) {

            alert.setContentText("Invalid Holiday ID in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        if (holiday.getHolidayName() == null || holiday.getHolidayName().trim().isEmpty()) {

            alert.setContentText("Holiday name cannot be empty in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(holiday.getHolidayName().trim())) {

            alert.setContentText("Invalid Holiday Name in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        if (holiday.getStartDate() == null || holiday.getStartDate().trim().isEmpty()) {

            alert.setContentText("Start date cannot be empty in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateDateFormat(holiday.getStartDate().trim())) {

            alert.setContentText("Invalid start date in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        if (holiday.getEndDate() == null || holiday.getEndDate().trim().isEmpty()) {

            alert.setContentText("End date cannot be empty in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateDateFormat(holiday.getEndDate().trim())) {

            alert.setContentText("Invalid end date in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * This method sets the background to normal and sets off the loading spinner and the status in the Import Holidays
     * section.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateImportHolidaysCsvProgressAndStatus() {

        importHolidaysCsvMainGridPane.setOpacity(1);
        importHolidayCsvProgressIndicator.setVisible(false);
        importHolidayCsvStatusImageView.setVisible(false);
        importHolidayCsvStatusStackPane.setVisible(false);
        importCsvHboxButtons.setVisible(false);
        unSetComboBoxes();
        fileChosenLabel.setText("");
        submitHolidayButton.setDisable(true);
    }

    /**
     * Method for enabling the ComboBoxes sand setting them with list of columns from the CSV.
     *
     * @param list List of columns extracted from the CSV file.
     */
    @SuppressWarnings("Duplicates")
    private void setComboBoxes(List<String> list) {

        ObservableList<String> options = FXCollections.observableArrayList(list);

        holidayIdComboBox.setDisable(false);
        holidayIdComboBox.setItems(options);
        holidayIdComboBox.setValue(list.get(0));

        holidayNameComboBox.setDisable(false);
        holidayNameComboBox.setItems(options);
        holidayNameComboBox.setValue(list.get(1));

        startDateComboBox.setDisable(false);
        startDateComboBox.setItems(options);
        startDateComboBox.setValue(list.get(2));

        endDateComboBox.setDisable(false);
        endDateComboBox.setItems(options);
        endDateComboBox.setValue(list.get(3));
    }

    /**
     * Method for disabling and un-setting the ComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    private void unSetComboBoxes() {

        holidayIdComboBox.setDisable(true);
        holidayIdComboBox.setValue("");

        holidayNameComboBox.setDisable(true);
        holidayNameComboBox.setValue("");

        startDateComboBox.setDisable(true);
        startDateComboBox.setValue("");

        endDateComboBox.setDisable(true);
        endDateComboBox.setValue("");
    }

    /**
     * Method for configuring the fileChooser
     */
    private void configureFileChooser(FileChooser fileChooser) {

        fileChooser.setTitle("Import Holiday CSV file");
        //only .csv files can be chosen
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV", "*.csv"));
    }

    /*---------------------------------------------Holidays TableView operation-------------------------*/

    /**
     * Callback method for Delete Button
     * Deletes a particular holiday upon clicking the button.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDeleteHolidayButtonAction() {

        //get the selected Holiday in the TableView.
        Holiday holiday = holidaysTableView.getSelectionModel().getSelectedItem();

        //only when a Holiday is selected
        if (holiday != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Are you really want to delete?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                //fade the background and display laoding spinner
                holidaysListMainGridPane.setOpacity(0.5);
                holidaysListStatusStackPane.setVisible(true);
                holidaysListProgressIndicator.setVisible(true);

                Task<Integer> deleteHolidayTask = holidayService
                        .getDeleteHolidayTask(holiday);
                new Thread(deleteHolidayTask).start();

                deleteHolidayTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get status of the deletion operation
                        int status = deleteHolidayTask.getValue();

                        //deletion operation completed ,so disable loading spinner and show status
                        holidaysListProgressIndicator.setVisible(false);
                        holidayListStatusImageView.setVisible(true);
                        holidaysListStatusLabel.setVisible(true);
                        holidaysListHboxButtons.setVisible(true);

                        //display status
                        if (status == DATABASE_ERROR) {

                            holidayListStatusImageView.setImage(new Image("/png/critical error.png"));
                            holidaysListStatusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            holidayListStatusImageView.setImage(new Image("/png/success.png"));
                            holidaysListStatusLabel.setText("Successfully Deleted!");
                            populateHolidayTable();
                        } else if (status == DATA_DEPENDENCY_ERROR) {

                            holidayListStatusImageView.setImage(new Image("/png/error.png"));
                            holidaysListStatusLabel.setText("Cannot delete holiday!");
                        } else {

                            holidayListStatusImageView.setImage(new Image("/png/error.png"));
                            holidaysListStatusLabel.setText("Holiday not found!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Callback method to handle Ok button action in the Holiday List section.
     */
    @FXML
    private void handleHolidaysListOkButtonAction() {

        deactivateHolidaysListProgressAndStatus();
    }

    /**
     * Initializes the columns in the holiday table.
     */
    private void initHolidayCols() {

        holidayIdCol.setCellValueFactory(new PropertyValueFactory<>("holidayId"));
        holidayNameCol.setCellValueFactory(new PropertyValueFactory<>("holidayName"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }

    /**
     * Populates and updates the holiday table.
     */
    @SuppressWarnings("Duplicates")
    private void populateHolidayTable() {

        //get the task to get list of holidays from the DB
        Task<List<Holiday>> holidaysTask = holidayService.getHolidaysTask();
        new Thread(holidaysTask).start();

        holidaysTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                holidayObsList.setAll(holidaysTask.getValue());
                holidaysTableView.setItems(holidayObsList);
            }
        });
    }

    /**
     * This method sets the background to normal and sets off the loading spinner and the status in the Holiday List
     * section.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateHolidaysListProgressAndStatus() {

        holidaysListMainGridPane.setOpacity(1);
        holidaysListProgressIndicator.setVisible(false);
        holidayListStatusImageView.setVisible(false);
        holidaysListStatusStackPane.setVisible(false);
        holidaysListHboxButtons.setVisible(false);
    }
}
