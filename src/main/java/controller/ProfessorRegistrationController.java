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
import model.Department;
import model.Professor;
import service.*;
import util.ValidatorUtil;

import javax.swing.plaf.basic.ComboPopup;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static util.ConstantsUtil.*;

/**
 * Controller class for a single professor registration.
 * for ProfessorRegistration.fxml
 *
 * @author Sourav Debnath
 */

public class ProfessorRegistrationController {

    /*---------------------------------Declaration and Initialization of variables-----------------------------------
    -----------------------The @FXML components are initialized when FXML document is being loaded.------------------
     */

    private Professor professor;

    private int editOrAddProfessorChoice;

    private ProfessorService professorService;

    private DepartmentService departmentService;

    private List<Department> listOfDepartment;

    @FXML
    private ComboBox<String> deptComboBox;

    @FXML
    private ComboBox<String> highestQualificationComboBox;

    @FXML
    private ComboBox<String> hodComboBox;

    @FXML
    private DatePicker dojDatePicker;

    @FXML
    private TextField profIdTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField middleNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private TextField emailIdTextField;

    @FXML
    private TextField contactNoTextField;

    @FXML
    private TextArea addressTextArea;

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
        departmentService = new DepartmentService();
        professorService = new ProfessorService();

        //initially adding of new student is opted
        editOrAddProfessorChoice = ADD_CHOICE;

        //get a list of available departments in the DB
        Task<List<Department>> deptTask = departmentService
                .getDepartmentsTask("");
        new Thread(deptTask).start();

        deptTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //get the list of departments to get the list of departments
                listOfDepartment = deptTask.getValue();

                //only when any department is present in the listofdepartments then only set the the department combo box
                if (!listOfDepartment.isEmpty()) {

                    List<String> items = new ArrayList<>();

                    //only add unique department to the profDeptComboBox
                    for (Department dept : listOfDepartment) {

                        if (!items.contains(dept.getDeptName()))
                            items.add(dept.getDeptName());
                    }

                    ObservableList<String> options = FXCollections.observableArrayList(items);

                    //set items in profDeptComboBox
                    deptComboBox.setItems(options);
                }

                //set items in the Highest Qualification Combo box
                highestQualificationComboBox.setItems(FXCollections.observableArrayList("B.tech"
                , "M.tech", "B.sc", "M.sc", "B.A", "M.A", "P.hd", "H.S"));

                //set items in the Hod Status combo box
                hodComboBox.setItems(FXCollections.observableArrayList("1", "0"));
            }
        });

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
            Creates a new Professor object and sets it's data.
             */
            Professor professor = new Professor();

            professor.setProfId(profIdTextField.getText().trim());
            professor.setFirstName(firstNameTextField.getText().trim());
            professor.setMiddleName(middleNameTextField.getText().trim());
            professor.setLastName(lastNameTextField.getText().trim());
            professor.setDob(dobDatePicker.getValue().toString());
            professor.setContactNo(contactNoTextField.getText().trim());
            professor.setAddress(addressTextArea.getText().trim());
            professor.setEmail(emailIdTextField.getText().trim());
            professor.setDoj(dojDatePicker.getValue().toString());
            professor.setHighestQualification(highestQualificationComboBox.getValue());
            professor.setHodStatus(Integer.parseInt(hodComboBox.getValue()));
            professor.setDeptName(deptComboBox.getValue());
            //professor.setDepartment(deptComboBox.getValue());



            //if edit signal is sent
            if (editOrAddProfessorChoice == EDIT_CHOICE) {

                Task<Integer> updateProfessorTask = professorService
                        .getUpdateProfessorTask(professor);

                new Thread(updateProfessorTask).start();

                updateProfessorTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of the update Professor
                        int status = updateProfessorTask.getValue();

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
                            statusLabel.setText("Added Successfully!");
                        } else {

                            statusImageView.setImage(new Image("/png/error.png"));
                            statusLabel.setText("Professor already exists!");
                        }
                    }
                });
            }

            //if add new professor is chosen
            else {

                Task<Integer> addProfessorToDatabaseTask = professorService
                        .getAddProfessorToDatabaseTask(professor);

                new Thread(addProfessorToDatabaseTask).start();

                addProfessorToDatabaseTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        //get the status of the update Professor
                        int status = addProfessorToDatabaseTask.getValue();

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
                            statusLabel.setText("Added Successfully!");
                        } else {

                            statusImageView.setImage(new Image("/png/error.png"));
                            statusLabel.setText("Professor already exists!");
                        }
                    }
                });
            }

        }
    }

    /**
     * This method validates a single professor and displays msg if validation fails.
     *
     * @return the validation status
     */
    private boolean validate() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (deptComboBox.getValue() == null) {
            alert.setContentText("Please select a department!");
            alert.show();
            return false;
        }
        else if (highestQualificationComboBox.getValue() == null) {
            alert.setContentText("Please select highest qualification!");
            alert.show();
            return false;
        }
        else if (hodComboBox.getValue() == null) {
            alert.setContentText("Please select Hod status!");
            alert.show();
            return false;
        }
        else if (dojDatePicker.getValue() == null) {
            alert.setContentText("Please choose a date of joining!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateDateFormat(dobDatePicker.getValue().toString())) {

            alert.setContentText("Invalid date of joining format!");
            alert.show();
            return false;
        }
        else if (profIdTextField.getText().isEmpty()) {
            alert.setContentText("Please enter professor Id!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateId(profIdTextField.getText().trim())) {

            alert.setContentText("Invalid Professor Id!");
            alert.show();
            return false;
        }
        else if (firstNameTextField.getText().isEmpty()) {
            alert.setContentText("First Name cannot be empty!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateName(firstNameTextField.getText().trim())) {

            alert.setContentText("Invalid First Name!");
            alert.show();
            return false;
        }
        else if (!middleNameTextField.getText().trim().isEmpty()) {

            if (!ValidatorUtil.validateName(middleNameTextField.getText().trim())) {

                alert.setContentText("Invalid Middle Name!");
                alert.show();
                return false;
            } else {

                return true;
            }
        }
        else if (!lastNameTextField.getText().trim().isEmpty()) {

            if (!ValidatorUtil.validateName(lastNameTextField.getText().trim())) {

                alert.setContentText("Invalid Last Name!");
                alert.show();
                return false;
            } else {

                return true;
            }
        }
        else if (dobDatePicker.getValue() == null) {
            alert.setContentText("Please choose a date of birth!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateDateFormat(dobDatePicker.getValue().toString())) {

            alert.setContentText("Invalid Date of Birth Format!");
            alert.show();
            return false;
        }
        else if (emailIdTextField.getText().isEmpty()) {
            alert.setContentText("Email ID cannot be empty!");
            alert.show();
            return false;
        }
        else if (!ValidatorUtil.validateEmail(emailIdTextField.getText())) {
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
        }
        return true;
    }


    /**
     * Callback method to handle Add Another button and Reset button.
     * Deactivates loading spinner and status and clears the textfields and the comboboxes.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleAddAnotherAndResetButtonAction(){

        //add another or reset means adding a new professor
        editOrAddProfessorChoice = ADD_CHOICE;

        statusStackPane.setVisible(false);
        progressIndicator.setVisible(false);
        buttonsHbox.setVisible(false);
        statusImageView.setVisible(false);
        statusLabel.setVisible(false);
        mainGridPane.setOpacity(1);
        firstNameTextField.clear();
        middleNameTextField.clear();
        lastNameTextField.clear();
        dobDatePicker.getEditor().clear();
        emailIdTextField.clear();
        addressTextArea.clear();
        contactNoTextField.clear();
        deptComboBox.getSelectionModel().clearSelection();
        highestQualificationComboBox.getSelectionModel().clearSelection();
        hodComboBox.getSelectionModel().clearSelection();
        dojDatePicker.getEditor().clear();
        profIdTextField.clear();
        hodComboBox.setDisable(false);
        highestQualificationComboBox.setDisable(false);
        deptComboBox.setDisable(false);
    }

    /**
     * Callback method to handle Back and Cancel button.
     * Opens the ProfessorsList scene on clicking on them.
     *
     * @throws IOException Load exception while loading the fxml document.
     */
    @FXML
    private void handleBackAndCancelButtonAction() throws IOException {

        StackPane contentStackPane = (StackPane)rootAnchorPane.getParent();
        Parent professorRegistrationFxml = FXMLLoader.load(getClass()
                .getResource("/view/ProfessorSection.fxml"));
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(professorRegistrationFxml);
    }

    /**
     * This method is used to set the edit command and prepare the Registration form for editing.
     *
     * @param editSignal EDIT_CHOICE
     */
    @SuppressWarnings("Duplicates")
    public void setEditSignal(int editSignal) {

        editOrAddProfessorChoice = editSignal;
        firstNameTextField.setText(professor.getFirstName());
        middleNameTextField.setText(professor.getMiddleName());
        lastNameTextField.setText(professor.getLastName());
        dobDatePicker.setValue(LocalDate.parse(professor.getDob()));
        dojDatePicker.setValue(LocalDate.parse(professor.getDoj()));
        emailIdTextField.setText(professor.getEmail());
        addressTextArea.setText(professor.getAddress());
        profIdTextField.setText(professor.getProfId());
        contactNoTextField.setText(professor.getContactNo());
        deptComboBox.setDisable(true);
        highestQualificationComboBox.setDisable(true);
        hodComboBox.setDisable(true);
        deptComboBox.setValue(professor.getDeptName());
        highestQualificationComboBox.setValue(professor.getHighestQualification());
        hodComboBox.setValue(Integer.toString(professor.getHodStatus()));
        profIdTextField.setDisable(true);
    }

    /**
     * This method is used to set the Professor object with the selected Professor object in the tableView for editing.
     *
     * @param professor The object with which the Professor object will be set.
     */
    public void setProfessorPojo(Professor professor) {

        this.professor = professor;
    }

}
