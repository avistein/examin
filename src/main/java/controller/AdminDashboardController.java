package controller;

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
import javafx.stage.FileChooser;
import model.Holiday;
import service.HolidayService;
import util.CSVUtil;
import util.ValidatorUtil;

import java.io.File;
import java.util.*;

import static util.ConstantsUtil.*;

public class AdminDashboardController {

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

    private ObservableList<Holiday> holidayObsList;

    private File file;

    @FXML
    private void initialize() {

        holidayService = new HolidayService();
        holidayObsList = FXCollections.observableArrayList();

        initHolidayCols();
        populateHolidayTable();
    }

    @FXML
    private void handleTotalStudentsVboxOnMouseClickedAction() {

    }

    @FXML
    private void handleTotalProfessorsVboxOnMouseClickedAction() {

    }

    @FXML
    private void handleTotalExamCellMembersVboxOnMouseClickedAction() {

    }

    @FXML
    private void handleTotalOfficeMembersVboxOnMouseClickedAction() {

    }

    @FXML
    private void handleTotalDepartmentsVboxOnMouseClickedAction() {

    }

    @FXML
    private void handleTotalCoursesVboxOnMouseClickedAction() {

    }

    @FXML
    private void handleTotalBatchesVboxOnMouseClickedAction() {

    }

    @FXML
    private void handleTotalSubjectsVboxOnMouseClickedAction() {

    }

    @FXML
    private void handleTotalClassroomsVboxOnMouseClickedAction() {

    }

    @FXML
    private void handleChooseFileButtonAction() {

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());

        /*
        If a file is chosen get the column names from the file and set ComboBoxes
        with the column names' list else unset ComboBoxes.
         */
        if(file != null) {
            fileChosenLabel.setText(file.getName());
            List<String> list = CSVUtil.getColumnNames(file);
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

        importHolidaysCsvMainGridPane.setOpacity(0.5);
        importHolidayCsvStatusStackPane.setVisible(true);
        importHolidayCsvProgressIndicator.setVisible(true);

        Task<List<Holiday>> loadHolidaysFromCsvToMemoryTask = holidayService
                .getLoadHolidaysFromCsvToMemoryTask(file, map);
        new Thread(loadHolidaysFromCsvToMemoryTask).start();

        loadHolidaysFromCsvToMemoryTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                List<Holiday> listOfHolidaysFromCsv = new ArrayList<>();
                listOfHolidaysFromCsv.addAll(loadHolidaysFromCsvToMemoryTask.getValue());

                int currRowInCsv = 1;
                boolean csvDataStatus = false;
                for(Holiday holiday : listOfHolidaysFromCsv){

                    csvDataStatus = validate(holiday, currRowInCsv++);
                    if(!csvDataStatus) {

                        deactivateImportHolidaysCsvProgressAndStatus();
                        break;
                    }
                }

                if(csvDataStatus) {

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
                            if (status == DATABASE_ERROR) {
                                importHolidayCsvStatusImageView.setImage(new Image("/png/critical error.png"));
                                importHolidayCsvStatusLabel.setText("Database Error!");
                            } else if (status == SUCCESS) {
                                importHolidayCsvStatusImageView.setImage(new Image("/png/success.png"));
                                importHolidayCsvStatusLabel.setText("Successfully added all students!");
                                populateHolidayTable();
                            } else {
                                importHolidayCsvStatusImageView.setImage(new Image("/png/error.png"));
                                importHolidayCsvStatusLabel.setText("One or more students already exist!");
                                populateHolidayTable();
                            }
                        }
                    });
                }
            }
        });
    }

    @FXML
    private void handleImportCsvOkButtonAction(){

        deactivateImportHolidaysCsvProgressAndStatus();
    }

    private boolean validate(Holiday holiday, int currHolidayIndex) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (holiday.getHolidayId() == null || holiday.getHolidayId().trim().isEmpty()) {
            alert.setContentText("Holiday ID cannot be empty in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateId(holiday.getHolidayId().trim())) {
            alert.setContentText("Invalid Holiday ID in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        else if (holiday.getHolidayName() == null || holiday.getHolidayName().trim().isEmpty()) {
            alert.setContentText("Holiday name cannot be empty in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateAcademicItem(holiday.getHolidayName().trim())) {
            alert.setContentText("Invalid Holiday Name in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        else if (holiday.getStartDate() == null || holiday.getStartDate().trim().isEmpty()) {
            alert.setContentText("Start date cannot be empty in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateDateFormat(holiday.getStartDate().trim())) {
            alert.setContentText("Invalid start date in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        else if (holiday.getEndDate() == null || holiday.getEndDate().trim().isEmpty()) {
            alert.setContentText("End date cannot be empty in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateDateFormat(holiday.getEndDate().trim())) {
            alert.setContentText("Invalid end date in Row : " + currHolidayIndex + "!");
            alert.show();
            return false;
        }
        return true;
    }

    @SuppressWarnings("Duplicates")
    private void deactivateImportHolidaysCsvProgressAndStatus(){

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
     * Method for setting the ComboBoxes list of items.
     * @param list List of columns extracted from the CSV file.
     */
    @SuppressWarnings("Duplicates")
    private void setComboBoxes(List<String> list){

        ObservableList<String>  options = FXCollections.observableArrayList(list);

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
    private void unSetComboBoxes(){

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
     *
     */
    private void configureFileChooser(FileChooser fileChooser){

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

        Holiday holiday = holidaysTableView.getSelectionModel().getSelectedItem();

        if(holiday != null){

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete");
                alert.setHeaderText("Are you really want to delete?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){

                    holidaysListMainGridPane.setOpacity(0.5);
                    holidaysListStatusStackPane.setVisible(true);
                    holidaysListProgressIndicator.setVisible(true);

                    Task<Integer> deleteHolidayTask = holidayService
                            .getDeleteHolidayTask(holiday);
                    new Thread(deleteHolidayTask).start();

                    deleteHolidayTask.setOnSucceeded(new EventHandler<>() {
                        @Override
                        public void handle(WorkerStateEvent event) {

                            int status = deleteHolidayTask.getValue();
                            holidaysListProgressIndicator.setVisible(false);
                            holidayListStatusImageView.setVisible(true);
                            holidaysListStatusLabel.setVisible(true);
                            holidaysListHboxButtons.setVisible(true);
                            if(status == DATABASE_ERROR){

                                holidayListStatusImageView.setImage(new Image("/png/critical error.png"));
                                holidaysListStatusLabel.setText("Database Error!");
                            }
                            else if(status == SUCCESS){

                                holidayListStatusImageView.setImage(new Image("/png/success.png"));
                                holidaysListStatusLabel.setText("Successfully Deleted!");
                                populateHolidayTable();
                            }
                            else if(status == DATA_DEPENDENCY_ERROR){

                                holidayListStatusImageView.setImage(new Image("/png/error.png"));
                                holidaysListStatusLabel.setText("Cannot delete holiday!");
                            }
                            else {

                                holidayListStatusImageView.setImage(new Image("/png/error.png"));
                                holidaysListStatusLabel.setText("Holiday not found!");
                            }
                        }
                    });
                }
        }
    }

    @FXML
    private void handleHolidaysListOkButtonAction(){

        deactivateHolidaysListProgressAndStatus();
    }

    /**
     *
     * Initializes the columns in the holiday table.
     */
    private void initHolidayCols(){

        holidayIdCol.setCellValueFactory(new PropertyValueFactory<>("holidayId"));
        holidayNameCol.setCellValueFactory(new PropertyValueFactory<>("holidayName"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }

    /**
     * Populates and updates the holiday table.
     */
    private void populateHolidayTable(){

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

    @SuppressWarnings("Duplicates")
    private void deactivateHolidaysListProgressAndStatus(){

        holidaysListMainGridPane.setOpacity(1);
        holidaysListProgressIndicator.setVisible(false);
        holidayListStatusImageView.setVisible(false);
        holidaysListStatusStackPane.setVisible(false);
        holidaysListHboxButtons.setVisible(false);
    }

}
