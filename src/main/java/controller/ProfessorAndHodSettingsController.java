package controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import model.Professor;
import service.ProfessorService;
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
 * Controller class for /professorPanel/Settings.Fxml
 *
 * @author Sourav Debnath
 */
public class ProfessorAndHodSettingsController {

    private FileHandlingService fileHandlingService;

    /*-------------------------------Declaration & Initialization of variables---------------------------------------*/

    @FXML
    private AnchorPane settingsAnchorPane;

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
    private TextField profileSettingsHighestQualificationTextField;

    @FXML
    private Button profileSettingsSubmitButton;

    private Professor professor;

    private ProfessorService professorService;

    private String profileImagePath;

    /*--------------------------------End of Declaration & Initialization of variables------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    public void initController(Professor professor) {

        professorService = new ProfessorService();
        fileHandlingService = new FileHandlingService();

        this.professor = professor;

        initProfileSettingsTab();
    }

    /**
     *  Callback Method to handle action of profileSettingsChoseImageButton.
     *  <p>
     *  This method sets profile image of professor in profileSettingsPictureImageView.
     */
    @SuppressWarnings("Duplicates")
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

        professor.setFirstName(profileSettingsFirstNameTextField.getText().trim());
        professor.setMiddleName(profileSettingsMiddleNameTextFiled.getText().trim());
        professor.setLastName(profileSettingsLastNameTextField.getText().trim());
        professor.setContactNo(profileSettingsContactNoTextField.getText().trim());
        professor.setEmail(profileSettingsEmailIdTextField.getText().trim());
        professor.setDoj(String.valueOf(profileSettingsDojDatePicker.getValue()));
        professor.setDob(String.valueOf(profileSettingsDobDatePicker.getValue()));
        professor.setAddress(profileSettingsAddressTextArea.getText().trim());
        professor.setHighestQualification(profileSettingsHighestQualificationTextField.getText().trim());

        //if a image is chosen from the filechooser then set the path with the location of that image
        if (!profileImagePath.isEmpty()) {

            professor.setProfileImagePath(profileImagePath);
        }

        //validateUniversityInfoItems first
        if (validateProfileSettingsItems()) {

            //fade the background and show loading spinner
            profileSettingsStatusStackPane.setVisible(true);
            profileSettingsProgressIndicator.setVisible(true);
            profileSettingsMainGridPane.setOpacity(0.5);

            Task<Integer> updateProfessorProfileDetailsTask = professorService.getUpdateProfessorTask(professor);
            new Thread(updateProfessorProfileDetailsTask).start();

            updateProfessorProfileDetailsTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    //get the status of update operation
                    int profileUpdateStatus = updateProfessorProfileDetailsTask.getValue();

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
                        Scene mainScene = settingsAnchorPane.getScene();

                        //get the label which is situated in the Subtitle
                        Label nameLabelInProfessorPanel = (Label) mainScene.lookup("#nameLabel");

                        //get the imageview of the admin panel
                        ImageView profileImage = (ImageView) mainScene.lookup("#profileImageImageView");

                        //set the display picture of the admin there
                        profileImage.setImage(new Image(professor.getProfileImagePath()));

                        //update the name in the Subtitle
                        nameLabelInProfessorPanel.setText(professor.getFirstName() + " " + professor.getMiddleName() + " "
                                + professor.getLastName());
                    } else {

                        profileSettingsStatusImageView.setImage(new Image("/png/error.png"));
                        profileSettingsStatusLabel.setText("Profile doesn't exist!");
                    }
                }
            });
        }
    }

    /**
     * Callback method to handle OK button in the Profile Settings.
     * <p>
     * This method disables the status stack pane and sets the UI as it was earlier before editing.
     */
    @FXML
    private void handleProfileSettingsOkButtonAction() {

        initProfileSettingsTab();
        deactivateProfileSettingsStatus();
    }

    /**
     * This method initializes items in the Profile Settings.
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
    @SuppressWarnings("Duplicates")
    private boolean validateProfileSettingsItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (profileSettingsFirstNameTextField.getText() == null || profileSettingsFirstNameTextField.getText().trim()
                .isEmpty()) {

            alert.setContentText("First Name cannot be empty!");
            alert.show();
            return false;
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
        if (profileSettingsHighestQualificationTextField.getText() == null ||
            profileSettingsHighestQualificationTextField.getText().trim().isEmpty()) {

            alert.setContentText("Highest Qualification cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateAcademicItem(profileSettingsHighestQualificationTextField.getText().trim())) {

            alert.setContentText("Invalid Highest Qualification!");
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
        profileSettingsHighestQualificationTextField.setDisable(false);
        profileSettingsSubmitButton.setDisable(false);
    }

    /**
     * This method disables the TextFields of the Profile Settings and also disables the Submit button for editing.
     */
    @SuppressWarnings("Duplicates")
    private void disableProfileItems() {

        profileSettingsFirstNameTextField.setDisable(true);
        profileSettingsMiddleNameTextFiled.setDisable(true);
        profileSettingsLastNameTextField.setDisable(true);
        profileSettingsContactNoTextField.setDisable(true);
        profileSettingsEmailIdTextField.setDisable(true);
        profileSettingsDojDatePicker.setDisable(true);
        profileSettingsDobDatePicker.setDisable(true);
        profileSettingsAddressTextArea.setDisable(true);
        profileSettingsHighestQualificationTextField.setDisable(true);
        profileSettingsSubmitButton.setDisable(true);
    }

    /**
     * This method sets the TextFields,TextArea and datePicker, display picture of the Profile Settings.
     */
    @SuppressWarnings("Duplicates")
    private void setProfileItems() {

        profileSettingsEmpIdLabel.setText(professor.getProfId());
        profileSettingsFirstNameTextField.setText(professor.getFirstName());
        profileSettingsMiddleNameTextFiled.setText(professor.getMiddleName());
        profileSettingsLastNameTextField.setText(professor.getLastName());
        profileSettingsContactNoTextField.setText(professor.getContactNo());
        profileSettingsEmailIdTextField.setText(professor.getEmail());
        profileSettingsDojDatePicker.setValue(LocalDate.parse(professor.getDoj()));
        profileSettingsDobDatePicker.setValue(LocalDate.parse(professor.getDob()));
        profileSettingsAddressTextArea.setText(professor.getAddress());
        profileSettingsHighestQualificationTextField.setText(professor.getHighestQualification());
        profileImagePath = "";

        if (Paths.get(professor.getProfileImagePath().replace("file:", "")).toFile().exists()) {

            profileSettingPictureImageView.setImage(new Image(professor.getProfileImagePath()));
        } else {

            profileSettingPictureImageView.setImage(new Image("/png/placeholder.png"));
        }
    }

    /**
     * This method deactivates the status stack pane and it's items in the Profile Settings and also brings back the
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
}
