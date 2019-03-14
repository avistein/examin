package controller.adminPanel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import model.ExamCellMember;
import service.ExamCellMemberService;
import service.FileHandlingService;
import util.ValidatorUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Controller class for /adminPanel/Settings.Fxml
 *
 * @author Avik Sarkar
 */
public class SettingsController {


    @FXML
    private TabPane settingsTabPane;

    /*-------------------------Declaration & Initialization of variables of Profile Settings Tab----------------------*/

    @FXML
    private Tab profileSettingsTab;

    @FXML
    private GridPane profileSettingsMainGridPane;

    @FXML
    private StackPane profileSettingsStatusStackPane;

    @FXML
    private ProgressIndicator profileSettingsProgressIndicator;

    @FXML
    private ImageView profileSettingsStatusImageView;

    @FXML
    private Label profileSettingsStatusLabel;

    @FXML
    private ImageView profileSettingPictureImageView;

    @FXML
    private HBox profileSettingsHboxButtons;

    @FXML
    private TextField profileSettingsFirstNameTextField;

    @FXML
    private TextField profileSettingsMiddleNameTextFiled;

    @FXML
    private TextField profileSettingsLastNameTextField;

    @FXML
    private TextField profileSettingsContactNoTextField;

    @FXML
    private Label profileSettingsEmpIdLabel;

    @FXML
    private TextField profileSettingsEmailIdTextField;

    @FXML
    private DatePicker profileSettingsDojDatePicker;

    @FXML
    private DatePicker profileSettingsDobDatePicker;

    @FXML
    private TextArea profileSettingsAddressTextArea;

    @FXML
    private Button profileSettingsSubmitButton;

    private ExamCellMember admin;

    private ExamCellMemberService examCellMemberService;

    /*--------------------End of Declaration & Initialization of variables of Profile Settings Tab--------------------*/


    /*-------------------------Declaration & Initialization of variables of University Info Tab-----------------------*/

    @FXML
    private Tab universityInfoTab;

    @FXML
    private TextField universityInfoNameTextField;

    @FXML
    private ImageView universityInfoLogoImageView;

    @FXML
    private Button universityInfoChooseLogoButton;

    @FXML
    private Label universityInfoChosenLogoLabel;

    @FXML
    private GridPane universityInfoMainGridPane;

    @FXML
    private StackPane universityInfoStatusStackPane;

    @FXML
    private ProgressIndicator universityInfoProgressIndicator;

    @FXML
    private ImageView universityInfoStatusImageView;

    @FXML
    private Label universityInfoStatusLabel;

    @FXML
    private HBox universityInfoHboxButtons;

    private File file;

    private FileHandlingService fileHandlingService;

    private Path path;

    /*--------------------End of Declaration & Initialization of variables of University Info Tab---------------------*/


    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    private void initialize() {

        examCellMemberService = new ExamCellMemberService();

        fileHandlingService = new FileHandlingService();

        settingsTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {

            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {

                if (newValue == profileSettingsTab) {

                    initProfileSettingsTab();
                } else if (newValue == universityInfoTab) {

                    initUniversityInfoTab();
                }
//                else if(newValue == emailSettingsTab){
//
//
//                }
            }
        });
    }

    /*---------------------------------------Profile Settings Tab operation-------------------------------------------*/

    @FXML
    private void handleEditProfilePictureButtonAction() {

    }

    /**
     * Callback method to handle action Edit Button in Profile Settings tab.
     * <p>
     * This method basically enables the TextFields for editing and also enables the Submit Button.
     */
    @FXML
    private void handleProfileSettingsEditButtonAction() {

        enableTextFields();
    }

    /**
     * Callback method to handle action of Submit Button in the Profile Settings tab.
     * <p>
     * This method basically checks for validation of the items first and then updates those items in the DB.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleProfileSettingsSubmitButtonAction() {

        admin.setFirstName(profileSettingsFirstNameTextField.getText().trim());
        admin.setMiddleName(profileSettingsMiddleNameTextFiled.getText().trim());
        admin.setLastName(profileSettingsLastNameTextField.getText().trim());
        admin.setContactNo(profileSettingsContactNoTextField.getText().trim());
        admin.setEmail(profileSettingsEmailIdTextField.getText().trim());
        admin.setDoj(String.valueOf(profileSettingsDojDatePicker.getValue()));
        admin.setDob(String.valueOf(profileSettingsDobDatePicker.getValue()));
        admin.setAddress(profileSettingsAddressTextArea.getText().trim());

        //validate first
        if (validateProfileSettingsItems()) {

            //fade the background and show loading spinner
            profileSettingsStatusStackPane.setVisible(true);
            profileSettingsProgressIndicator.setVisible(true);
            profileSettingsMainGridPane.setOpacity(0.5);

            Task<Integer> updateAdminProfileDetailsTask = examCellMemberService.getUpdateExamCellMemberTask(admin);
            new Thread(updateAdminProfileDetailsTask).start();

            updateAdminProfileDetailsTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    //get the status of update operation
                    int profileUpdateStatus = updateAdminProfileDetailsTask.getValue();

                    //update operation finished, so now disable loading spinner and show status
                    profileSettingsProgressIndicator.setVisible(false);
                    profileSettingsStatusImageView.setVisible(true);
                    profileSettingsStatusLabel.setVisible(true);
                    profileSettingsHboxButtons.setVisible(true);

                    //display several status
                    if (profileUpdateStatus == DATABASE_ERROR) {

                        profileSettingsStatusImageView.setImage(new Image("/png/critical error.png"));
                        profileSettingsStatusLabel.setText("Database Error!");
                    } else if (profileUpdateStatus == SUCCESS) {

                        profileSettingsStatusImageView.setImage(new Image("/png/success.png"));
                        profileSettingsStatusLabel.setText("Edited Successfully!");

                        //get the main scene
                        Scene mainScene = settingsTabPane.getScene();

                        //get the label which is situated in the Subtitle
                        Label nameLabelInAdminPanel = (Label) mainScene.lookup("#nameLabel");

                        //update the name in the Subtitle
                        nameLabelInAdminPanel.setText(admin.getFirstName() + " " + admin.getMiddleName() + " "
                                + admin.getLastName());
                    } else {

                        profileSettingsStatusImageView.setImage(new Image("/png/error.png"));
                        profileSettingsStatusLabel.setText("Profile doesn't exist!");
                    }
                }
            });
        }
    }

    /**
     * Callback method to handle OK button in the Profile Sections tab.
     * <p>
     * This method disables the status stack pane and sets the UI as it was earlier before editing.
     */
    @FXML
    private void handleProfileSettingsOkButtonAction() {

        initProfileSettingsTab();
        deactivateProfileSettingsStatus();
    }

    /**
     * This method initializes items in the Profile Settings tab.
     */
    private void initProfileSettingsTab() {

        disableTextFields();
        setTextFields();
    }

    /**
     * This method validates all textfields in the Department Tab
     *
     * @return A boolean value indicating the validation result.
     */
    private boolean validateProfileSettingsItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (profileSettingsFirstNameTextField.getText() == null || profileSettingsFirstNameTextField.getText().trim()
                .isEmpty()) {

            alert.setContentText("First Name cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateName(profileSettingsFirstNameTextField.getText().trim())) {

            alert.setContentText("Invalid First Name!");
            alert.show();
            return false;
        } else if (!profileSettingsMiddleNameTextFiled.getText().isEmpty()) {

            if (!ValidatorUtil.validateName(profileSettingsMiddleNameTextFiled.getText())) {

                alert.setContentText("Invalid Middle Name!");
                alert.show();
                return false;
            } else {

                return true;
            }
        } else if (!profileSettingsLastNameTextField.getText().isEmpty()) {

            if (!ValidatorUtil.validateName(profileSettingsLastNameTextField.getText())) {

                alert.setContentText("Invalid Last Name!");
                alert.show();
                return false;
            } else {

                return true;
            }
        } else if (profileSettingsContactNoTextField.getText() == null || profileSettingsContactNoTextField.getText()
                .trim().isEmpty()) {

            alert.setContentText("Contact No. cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateContactNo(profileSettingsContactNoTextField.getText().trim())) {

            alert.setContentText("Invalid contact no!");
            alert.show();
            return false;
        } else if (profileSettingsEmailIdTextField.getText() == null || profileSettingsEmailIdTextField.getText()
                .trim().isEmpty()) {

            alert.setContentText("Email ID cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateEmail(profileSettingsEmailIdTextField.getText().trim())) {

            alert.setContentText("Invalid Email ID!");
            alert.show();
            return false;
        } else if (profileSettingsDojDatePicker.getValue() == null) {

            alert.setContentText("Date of Joining cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateEmail(String.valueOf(profileSettingsDojDatePicker.getValue()))) {

            alert.setContentText("Invalid date of joining format!");
            alert.show();
            return false;
        } else if (profileSettingsDobDatePicker.getValue() == null) {

            alert.setContentText("Date of birth cannot be empty!");
            alert.show();
            return false;
        } else if (!ValidatorUtil.validateEmail(String.valueOf(profileSettingsDobDatePicker.getValue()))) {

            alert.setContentText("Invalid date of birth format!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * This method enables the TextFields of the Profile Sections tab and also enables the Submit button for editing.
     */
    @SuppressWarnings("Duplicates")
    private void enableTextFields() {

        profileSettingsFirstNameTextField.setDisable(false);
        profileSettingsMiddleNameTextFiled.setDisable(false);
        profileSettingsLastNameTextField.setDisable(false);
        profileSettingsContactNoTextField.setDisable(false);
        profileSettingsEmailIdTextField.setDisable(false);
        profileSettingsDojDatePicker.setDisable(false);
        profileSettingsDobDatePicker.setDisable(false);
        profileSettingsAddressTextArea.setDisable(false);
        profileSettingsSubmitButton.setDisable(false);
    }

    /**
     * This method disables the TextFields of the Profile Sections tab and also disables the Submit button for editing.
     */
    private void disableTextFields() {

        profileSettingsFirstNameTextField.setDisable(true);
        profileSettingsMiddleNameTextFiled.setDisable(true);
        profileSettingsLastNameTextField.setDisable(true);
        profileSettingsContactNoTextField.setDisable(true);
        profileSettingsEmailIdTextField.setDisable(true);
        profileSettingsDojDatePicker.setDisable(true);
        profileSettingsDobDatePicker.setDisable(true);
        profileSettingsAddressTextArea.setDisable(true);
        profileSettingsSubmitButton.setDisable(true);
    }

    /**
     * This method sets the TextFields,TextArea and datePicker of the Profile Sections tab.
     */
    private void setTextFields() {

        profileSettingsEmpIdLabel.setText(admin.getEmpId());
        profileSettingsFirstNameTextField.setText(admin.getFirstName());
        profileSettingsMiddleNameTextFiled.setText(admin.getMiddleName());
        profileSettingsLastNameTextField.setText(admin.getLastName());
        profileSettingsContactNoTextField.setText(admin.getContactNo());
        profileSettingsEmailIdTextField.setText(admin.getEmail());
        profileSettingsDojDatePicker.setValue(LocalDate.parse(admin.getDoj()));
        profileSettingsDobDatePicker.setValue(LocalDate.parse(admin.getDob()));
        profileSettingsAddressTextArea.setText(admin.getAddress());
    }

    /**
     * This method deactivates the status stack pane and it's items in the Profile Settings tab and also brings back the
     * UI as it was before updating the Profile Settings.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateProfileSettingsStatus() {

        profileSettingsMainGridPane.setOpacity(1);
        profileSettingsStatusStackPane.setVisible(false);
        profileSettingsStatusImageView.setVisible(false);
        profileSettingsHboxButtons.setVisible(false);
        profileSettingsStatusLabel.setVisible(false);
    }

    /**
     * This method initializes the admin object with the object sent by the
     * {@link controller.adminPanel.AdminPanelController}.
     *
     * @param admin The object which would be used to initialize this.admin.
     */
    void setAdminProfileDetails(ExamCellMember admin) {

        this.admin = admin;
        initProfileSettingsTab();
    }

    /*------------------------------------End of Profile Settings Tab operation---------------------------------------*/


    /*---------------------------------------University Info Tab operation--------------------------------------------*/

    /**
     * Callback method to handle action of Choose Logo button in the University Info tab.
     * <p>
     * This method allows the user to choose an logo from his/her system.
     */
    @FXML
    private void handleUniversityInfoChooseLogoButtonAction() {

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(universityInfoChooseLogoButton.getScene().getWindow());

        //only if a file is chosen, otherwise reset the label
        if (file != null) {

            universityInfoChosenLogoLabel.setText(file.getName());
            universityInfoLogoImageView.setImage(new Image("file:" + file.getAbsolutePath()));
        } else {
            universityInfoChosenLogoLabel.setText("");
        }
    }

    /**
     * Callback method to handle Submit Button in University Info tab.
     * <p>
     * Basically it checks for validation first and if validation is successful , then save properties in the
     * settings.properties file in  the User's system.If the doesn't exist in the User's system then create it first.
     */
    @FXML
    private void handleUniversityInfoSubmitButtonAction() {

        //if validation is successful
        if (validate()) {

            //fade the background and display loading spinner
            universityInfoMainGridPane.setOpacity(0.5);
            universityInfoStatusStackPane.setVisible(true);
            universityInfoProgressIndicator.setVisible(true);

            /*
            Create a HashMap of the following structure :
            Key : Property Key
            Value : Property Value
             */
            Map<String, String> propMap = new HashMap<>();
            propMap.put("universityName", universityInfoNameTextField.getText());
            propMap.put("universityLogoLocation", "file:" + file.getAbsolutePath());

            Task<Boolean> createPropertiesFileTask = fileHandlingService.getCreatePropertiesFile(
                    "settings.properties", propMap);
            new Thread(createPropertiesFileTask).start();

            createPropertiesFileTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    //operation finished , deactivate loading spinner and display status
                    universityInfoProgressIndicator.setVisible(false);
                    universityInfoStatusImageView.setVisible(true);
                    universityInfoStatusLabel.setVisible(true);
                    universityInfoHboxButtons.setVisible(true);

                    //display status
                    if (createPropertiesFileTask.getValue()) {

                        universityInfoStatusImageView.setImage(new Image("/png/success.png"));
                        universityInfoStatusLabel.setText("Settings Updated!");
                    } else {

                        universityInfoStatusImageView.setImage(new Image("/png/critical error.png"));
                        universityInfoStatusLabel.setText("Error!");
                    }
                }
            });
        }
    }

    /**
     * Callback method to handle the action of Ok button in the University Info tab.
     */
    @FXML
    private void handleUniversityInfoOkButtonAction() {

        loadProperties();
        deactivateUniversityInfoStatus();
    }

    /**
     * This method initializes the Path of the settings.properties file and also sets the University Name textField and
     * logo imageview in the University Info tab.
     */
    private void initUniversityInfoTab() {

        path = Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, "settings.properties");
        universityInfoChosenLogoLabel.setText("");
        loadProperties();
    }

    /**
     * This method is for ensuring that if the Admin wants to change the University Name , he has to change the
     * University logo.
     *
     * @return The result of the validation i.e. true or false.
     */
    private boolean validate() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (file == null) {

            alert.setHeaderText("No image is chosen!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * This method is used to load the properties from the settings.properties file from the User's system and set
     * the TextField and ImageView according to the Properties values.
     */
    private void loadProperties() {

        if (Files.exists(path)) {

            Map<String, String> propMap = fileHandlingService.loadPropertiesValuesFromPropertiesFile
                    ("settings.properties", "universityName", "universityLogoLocation");

            universityInfoLogoImageView.setImage(new Image(propMap.get("universityLogoLocation")));
            universityInfoNameTextField.setText(propMap.get("universityName"));
        }
    }

    /**
     * Method for configuring the fileChooser
     */
    private void configureFileChooser(FileChooser fileChooser) {

        fileChooser.setTitle("Choose Logo");

        //only .png files can be chosen
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG", "*.png"));
    }

    /**
     * This method deactivates the status stack pane and it's items in the University Info tab and also brings back the
     * UI as it was before updating the Settings.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateUniversityInfoStatus() {

        universityInfoMainGridPane.setOpacity(1);
        universityInfoStatusStackPane.setVisible(false);
        universityInfoStatusImageView.setVisible(false);
        universityInfoHboxButtons.setVisible(false);
        universityInfoStatusLabel.setVisible(false);
    }

    /*--------------------------------------End of University Info Tab operation--------------------------------------*/
}
