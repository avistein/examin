package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import service.StudentService;
import util.CSVUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportStudentCSVModalController {


    private StudentService studentService;
    private File file;
    private boolean tableUpdateStatus;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private AnchorPane statusAnchorPane;

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
    private  ComboBox<String> regIdComboBox;

    @FXML
    private ComboBox<String> rollNoComboBox;


    public void initialize(){
        studentService = new StudentService();
        tableUpdateStatus = false;
        Text text1 = new Text("File must be comma delimited CSV file\n");
        Text text2 = new Text("Open excel save as comma delimited CSV file\n");
        Text text3 = new Text("Make sure there is no heading or any other content" +
                " than column header and values in CSV file\n");
        Text text4 = new Text("Any date column should have values in the format " +
                "YYYY-MM-DD");
        csvInstructionsTextFlow.getChildren().addAll(text1, text2, text3, text4);
    }

    @FXML
    private void handleChooseFileButtonAction(){
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());
        if(file != null) {
            chosenFileLabel.setText(file.getName());
            List<String> list = CSVUtil.getColumnNames(file);
            setComboBoxes(list);
            submitButton.setDisable(false);
        }
        else{
            chosenFileLabel.setText("");
            submitButton.setDisable(true);
            unSetComboBoxes();
        }
    }

    @FXML
    private void handleSubmitButtonAction(){

        Map<String, String> map = new HashMap<>();

        map.put("firstName", firstNameComboBox.getValue());
        map.put("middleName", middleNameComboBox.getValue());
        map.put("lastName", lastNameComboBox.getValue());
        map.put("batchName", batchNameComboBox.getValue());
        map.put("degree", degreeComboBox.getValue());
        map.put("discipline", disciplineComboBox.getValue());
        map.put("regYear", regYearComboBox.getValue());
        map.put("currSemester",currSemesterComboBox.getValue());
        map.put("gender", genderComboBox.getValue());
        map.put("dob", dobComboBox.getValue());
        map.put("guardianName", guardianNameComboBox.getValue());
        map.put("motherName", motherNameComboBox.getValue());
        map.put("address", addressComboBox.getValue());
        map.put("email", emailComboBox.getValue());
        map.put("contactNo", contactNoComboBox.getValue());
        map.put("guardianContactNo", guardianContactNoComboBox.getValue());
        map.put("regId" , regIdComboBox.getValue());
        map.put("rollNo", rollNoComboBox.getValue());

        mainGridPane.setOpacity(0.5);
        statusAnchorPane.setVisible(true);

        tableUpdateStatus = studentService.loadToDataBase(file, map);

        progressIndicator.setVisible(false);
        if(tableUpdateStatus){
            statusImageView.setImage(new Image("/png/success.png"));
            statusLabel.setText("Success!");
        }
        else{
            statusImageView.setImage(new Image("/png/error.png"));
            statusLabel.setText("Failed");
        }
    }

    @FXML
    private void handleStatusAnchorPaneMouseClickedAction(){
        mainGridPane.setOpacity(1);
        statusAnchorPane.setVisible(false);
    }

    @SuppressWarnings("Duplicates")
    private void setComboBoxes(List<String> list){
        ObservableList<String>  options = FXCollections.observableArrayList(list);

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

    @SuppressWarnings("Duplicates")
    private void unSetComboBoxes(){

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
    private void configureFileChooser(FileChooser fileChooser){
        fileChooser.setTitle("Import CSV file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV", "*.csv"));
    }

    public boolean getTableUpdateStatus(){
        return tableUpdateStatus;
    }

}

