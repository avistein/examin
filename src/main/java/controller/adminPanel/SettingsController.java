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

    private FileHandlingService fileHandlingService;

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
    private Button profileSettingsChooseImageButton;

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

    private String profileImagePath;

    /*--------------------End of Declaration & Initialization of variables of Profile Settings Tab--------------------*/


    /*-------------------------Declaration & Initialization of variables of University Info Tab-----------------------*/

    @FXML
    private Tab universityInfoTab;

    @FXML
    private TextField universityInfoNameTextField;

    @FXML
    private TextField copyrightYearTextField;

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

    private File universityInfoLogo;

    private Path settingsPropsFileLocation;

    /*--------------------End of Declaration & Initialization of variables of University Info Tab---------------------*/


    /*-------------------------Declaration & Initialization of variables of Email Settings Tab-----------------------*/

    @FXML
    private Tab emailSettingsTab;

    @FXML
    private TextField adminEmailIdTextField;

    @FXML
    private PasswordField sendGridApiKeyPasswordField;

    @FXML
    private Button emailSettingsSubmitButton;

    @FXML
    private GridPane emailSettingsMainGridPane;

    @FXML
    private StackPane emailSettingsStatusStackPane;

    @FXML
    private ProgressIndicator emailSettingsProgressIndicator;

    @FXML
    private ImageView emailSettingsStatusImageView;

    @FXML
    private Label emailSettingsStatusLabel;

    @FXML
    private HBox emailSettingsHboxButtons;

    private Path emailPropsFileLocation;

    /*---------------------End of Declaration & Initialization of variables of Email Settings Tab---------------------*/

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

                Scene mainScene = settingsTabPane.getScene();
                Label subSubTitleLabel = (Label) mainScene.lookup("#subSubTitle");
                if (newValue == profileSettingsTab) {

                    subSubTitleLabel.setText("/ Profile Settings");
                    initProfileSettingsTab();
                } else if (newValue == universityInfoTab) {

                    subSubTitleLabel.setText("/ University Information Settings");
                    initUniversityInfoTab();
                } else if (newValue == emailSettingsTab) {

                    subSubTitleLabel.setText("/ Email Settings");
                    initEmailSettingsTab();
                }
            }
        });
    }

    /*---------------------------------------Profile Settings Tab operation-------------------------------------------*/

    @FXML
    private void handleProfileSettingsChooseImageButtonAction() {

        //choose the profile picture from the file system
        FileChooser fileChooser = new FileChooser();
        profileSettingsConfigureFileChooser(fileChooser);
        File profileSettingsPictureImage = fileChooser.showOpenDialog(profileSettingsChooseImageButton.getScene()
                .getWindow());

        /*
        if an image is chosen, enable the submit button so that the admin can update the image location in the DB , and
        set the image path and set the imageview with that image.
         */
        if (profileSettingsPictureImage != null) {

            profileSettingsSubmitButton.setDisable(false);

            profileImagePath = "file:" + profileSettingsPictureImage.getAbsolutePath();

            profileSettingPictureImageView.setImage(new Image(profileImagePath));
        }
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

        //if a image is chosen from the filechooser then set the path with the location of that image
        if (!profileImagePath.isEmpty()) {

            admin.setProfileImagePath(profileImagePath);
        }

        //validateUniversityInfoItems first
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

                        //get the imageview of the admin panel
                        ImageView profileImage = (ImageView) mainScene.lookup("#profileImageImageView");

                        //set the display picture of the admin there
                        profileImage.setImage(new Image(admin.getProfileImagePath()));

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

        disableProfileItems();
        setProfileItems();
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
        }
        if (!ValidatorUtil.validateName(profileSettingsFirstNameTextField.getText().trim())) {

            alert.setContentText("Invalid First Name!");
            alert.show();
            return false;
        }
        if (!profileSettingsMiddleNameTextFiled.getText().isEmpty()) {

            if (!ValidatorUtil.validateName(profileSettingsMiddleNameTextFiled.getText())) {

                alert.setContentText("Invalid Middle Name!");
                alert.show();
                return false;
            }
        }
        if (!profileSettingsLastNameTextField.getText().isEmpty()) {

            if (!ValidatorUtil.validateName(profileSettingsLastNameTextField.getText())) {

                alert.setContentText("Invalid Last Name!");
                alert.show();
                return false;
            }
        }
        if (profileSettingsContactNoTextField.getText() == null || profileSettingsContactNoTextField.getText()
                .trim().isEmpty()) {

            alert.setContentText("Contact No. cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateContactNo(profileSettingsContactNoTextField.getText().trim())) {

            alert.setContentText("Invalid contact no!");
            alert.show();
            return false;
        }
        if (profileSettingsEmailIdTextField.getText() == null || profileSettingsEmailIdTextField.getText()
                .trim().isEmpty()) {

            alert.setContentText("Email ID cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateEmail(profileSettingsEmailIdTextField.getText().trim())) {

            alert.setContentText("Invalid Email ID!");
            alert.show();
            return false;
        }
        if (profileSettingsDojDatePicker.getValue() == null) {

            alert.setContentText("Date of Joining cannot be empty!");
            alert.show();
            return false;
        }
        if (profileSettingsDobDatePicker.getValue() == null) {

            alert.setContentText("Date of birth cannot be empty!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * Method for configuring the fileChooser to choose an Image for the display picture in the Profile Settings tab.
     */
    private void profileSettingsConfigureFileChooser(FileChooser fileChooser) {

        fileChooser.setTitle("Choose Image");

        //only .png,.jpg & .jpeg files can be chosen
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", ".jpeg"));
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
    private void disableProfileItems() {

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
     * This method sets the TextFields,TextArea and datePicker, display picture of the Profile Sections tab.
     */
    private void setProfileItems() {

        profileSettingsEmpIdLabel.setText(admin.getEmpId());
        profileSettingsFirstNameTextField.setText(admin.getFirstName());
        profileSettingsMiddleNameTextFiled.setText(admin.getMiddleName());
        profileSettingsLastNameTextField.setText(admin.getLastName());
        profileSettingsContactNoTextField.setText(admin.getContactNo());
        profileSettingsEmailIdTextField.setText(admin.getEmail());
        profileSettingsDojDatePicker.setValue(LocalDate.parse(admin.getDoj()));
        profileSettingsDobDatePicker.setValue(LocalDate.parse(admin.getDob()));
        profileSettingsAddressTextArea.setText(admin.getAddress());
        profileImagePath = "";

        if (Paths.get(admin.getProfileImagePath().replace("file:", "")).toFile().exists()) {

            profileSettingPictureImageView.setImage(new Image(admin.getProfileImagePath()));
        } else {

            profileSettingPictureImageView.setImage(new Image("/png/placeholder.png"));
        }
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

        /*choose an image for the logo from the file system*/
        FileChooser fileChooser = new FileChooser();
        universityInfoConfigureFileChooser(fileChooser);
        universityInfoLogo = fileChooser.showOpenDialog(universityInfoChooseLogoButton.getScene().getWindow());

        //only if a universityInfoLogo is chosen set the imageview and the label, otherwise reset the label
        if (universityInfoLogo != null) {

            universityInfoChosenLogoLabel.setText(universityInfoLogo.getName());
            universityInfoLogoImageView.setImage(new Image("file:" + universityInfoLogo.getAbsolutePath()));
        } else {
            universityInfoChosenLogoLabel.setText("");
        }
    }

    /**
     * Callback method to handle Submit Button in University Info tab.
     * <p>
     * Basically it checks for validation first and if validation is successful , then save properties in the
     * settings.properties universityInfoLogo in  the User's system.If the doesn't exist in the User's system then create it first.
     */
    @FXML
    private void handleUniversityInfoSubmitButtonAction() {

        //if validation is successful
        if (validateUniversityInfoItems()) {

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
            propMap.put("universityName", universityInfoNameTextField.getText().trim());
            propMap.put("universityLogoLocation", "file:" + universityInfoLogo.getAbsolutePath());
            propMap.put("copyrightYear", copyrightYearTextField.getText().trim());

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

        loadUniversityInfoProperties();
        deactivateUniversityInfoStatus();
    }

    /**
     * This method initializes the Path of the settings.properties and also sets the University Name
     * textField and logo imageview in the University Info tab.
     */
    private void initUniversityInfoTab() {

        settingsPropsFileLocation = Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, "settings.properties");
        universityInfoChosenLogoLabel.setText("");
        loadUniversityInfoProperties();
    }

    /**
     * This method is for ensuring that if the Admin wants to change the University Name , he has to change the
     * University logo.
     *
     * @return The result of the validation i.e. true or false.
     */
    private boolean validateUniversityInfoItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (universityInfoLogo == null) {

            alert.setHeaderText("No image is chosen!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * This method is used to load the properties from the settings.properties universityInfoLogo from the User's system
     * and set the TextField and ImageView according to the Properties values.
     */
    private void loadUniversityInfoProperties() {

        if (settingsPropsFileLocation.toFile().exists()) {

            Map<String, String> propMap = fileHandlingService.loadPropertiesValuesFromPropertiesFile
                    ("settings.properties", "universityName", "universityLogoLocation");

            universityInfoLogoImageView.setImage(new Image(propMap.get("universityLogoLocation")));
            universityInfoNameTextField.setText(propMap.get("universityName"));
            copyrightYearTextField.setText(propMap.get("copyrightYear"));
        }
    }

    /**
     * Method for configuring the fileChooser in the University Info tab.
     */
    private void universityInfoConfigureFileChooser(FileChooser fileChooser) {

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


    /*----------------------------------------Email Settings Tab operation--------------------------------------------*/

    @FXML
    private void handleEmailSettingsEditButtonAction() {

        enableEmailSettingsItems();
    }

    /**
     * Callback method to handle Submit Button in Email Settings tab.
     * <p>
     * Basically it checks for validation first and if validation is successful , then save properties in the
     * email.properties in  the User's system.If the doesn't exist in the User's system then create it first.
     */
    @FXML
    private void handleEmailSettingsSubmitButtonAction() {

        //if validation is successful
        if (validateEmailSettingsItems()) {

            //fade the background and display loading spinner
            emailSettingsMainGridPane.setOpacity(0.5);
            emailSettingsStatusStackPane.setVisible(true);
            emailSettingsProgressIndicator.setVisible(true);

            /*
            Create a HashMap of the following structure :
            Key : Property Key
            Value : Property Value
             */
            Map<String, String> propMap = new HashMap<>();
            propMap.put("adminEmailId", adminEmailIdTextField.getText());
            propMap.put("sendGridApiKey", sendGridApiKeyPasswordField.getText());

            Task<Boolean> createPropertiesFileTask = fileHandlingService.getCreatePropertiesFile(
                    "email.properties", propMap);
            new Thread(createPropertiesFileTask).start();

            createPropertiesFileTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    //operation finished , deactivate loading spinner and display status
                    emailSettingsProgressIndicator.setVisible(false);
                    emailSettingsStatusImageView.setVisible(true);
                    emailSettingsStatusLabel.setVisible(true);
                    emailSettingsHboxButtons.setVisible(true);

                    //display status
                    if (createPropertiesFileTask.getValue()) {

                        emailSettingsStatusImageView.setImage(new Image("/png/success.png"));
                        emailSettingsStatusLabel.setText("Email Settings Updated!");
                    } else {

                        emailSettingsStatusImageView.setImage(new Image("/png/critical error.png"));
                        emailSettingsStatusLabel.setText("Error in updating email settings!");
                    }
                }
            });
        }
    }

    /**
     * Callback method to handle the action of Ok button in the Email Settings tab.
     */
    @FXML
    private void handleEmailSettingsOkButtonAction() {

        loadEmailSettingsProperties();
        deactivateEmailSettingsStatus();
        disableEmailSettingsItems();
    }

    /**
     * This method initializes the Path of the email.properties and also sets the Admin Email ID and SendGrid Api key
     * fields in the Email Settings tab.
     */
    private void initEmailSettingsTab() {

        emailPropsFileLocation = Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, "email.properties");
        loadEmailSettingsProperties();
        disableEmailSettingsItems();
    }

    /**
     * This method is for ensuring that the Admin's email Id field and SendGrid api key aren't empty or invalid.
     *
     * @return The result of the validation i.e. true or false.
     */
    private boolean validateEmailSettingsItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (adminEmailIdTextField == null || adminEmailIdTextField.getText().trim().isEmpty()) {

            alert.setHeaderText("Admin Email ID cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateEmail(adminEmailIdTextField.getText().trim())) {

            alert.setHeaderText("Invalid Admin Email ID!");
            alert.show();
            return false;
        }
        if (sendGridApiKeyPasswordField == null || sendGridApiKeyPasswordField.getText().isEmpty()) {

            alert.setHeaderText("SendGrid API key cannot be empty!");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * This method is used to load the properties from the email.properties from the User's system
     * and set the email ID TextField according to the Properties values.
     */
    private void loadEmailSettingsProperties() {

        if (emailPropsFileLocation.toFile().exists()) {

            Map<String, String> propMap = fileHandlingService.loadPropertiesValuesFromPropertiesFile
                    ("email.properties", "adminEmailId", "sendGridApiKey");

            adminEmailIdTextField.setText(propMap.get("adminEmailId"));
            sendGridApiKeyPasswordField.setText(propMap.get("sendGridApiKey"));
        }
    }

    /**
     * This method deactivates the status stack pane and it's items in the Email Settings tab and also brings back the
     * UI as it was before updating the Settings.
     */
    @SuppressWarnings("Duplicates")
    private void deactivateEmailSettingsStatus() {

        emailSettingsMainGridPane.setOpacity(1);
        emailSettingsStatusStackPane.setVisible(false);
        emailSettingsStatusImageView.setVisible(false);
        emailSettingsHboxButtons.setVisible(false);
        emailSettingsStatusLabel.setVisible(false);
    }

    /**
     * Method to disable items of the Email Settings tab.
     */
    private void disableEmailSettingsItems() {

        adminEmailIdTextField.setDisable(true);
        sendGridApiKeyPasswordField.setDisable(true);
        emailSettingsSubmitButton.setDisable(true);
    }

    /**
     * Method to enable items of the Email Settings tab.
     */
    private void enableEmailSettingsItems() {

        adminEmailIdTextField.setDisable(false);
        sendGridApiKeyPasswordField.setDisable(false);
        emailSettingsSubmitButton.setDisable(false);
    }

    /*---------------------------------------End of Email Settings Tab operation--------------------------------------*/
}
