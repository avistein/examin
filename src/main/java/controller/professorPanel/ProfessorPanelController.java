package controller.professorPanel;

import controller.*;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.*;
import service.ProfessorService;
import service.FileHandlingService;
import util.UISetterUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static util.ConstantsUtil.*;

/**
 * Controller class for professorPanel.fxml.
 * Loads the individual fxml upon clicking the buttons on left side.
 *
 * @author Sourav Debnath
 */
public class ProfessorPanelController {

    /*-------------------------------Initialization and declaration of variables-----------------------------------*/

    private ProfessorService professorService;

    private Professor professor;

    private User professorLoginDetails;

    private FXMLLoader loader;

    @FXML
    private Label copyrightYearLabel;

    @FXML
    private Label universityNameLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private ImageView profileImageImageView;

    @FXML
    private StackPane contentStackPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label subTitleLabel;

    @FXML
    private Label subSubTitleLabel;

    @FXML
    private Button dashboardButton;

    @FXML
    private Label lastLoginLabel;

    @FXML
    private Button logOutButton;

    /*--------------------------------------------End of Initialization-----------------------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void initialize() {

        //get the location of settings.properties to set the University logo
        Path path = Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, "settings.properties");

        //check if the settings.properties exists the user's system
        if (Files.exists(path)) {

            FileHandlingService fileHandlingService = new FileHandlingService();

            //get the location of University logo
            Map<String, String> propMap = fileHandlingService.loadPropertiesValuesFromPropertiesFile
                    ("settings.properties", "universityName", "copyrightYear");

            copyrightYearLabel.setText(propMap.get("copyrightYear") + ". ");
            universityNameLabel.setText(propMap.get("universityName"));
        }

        professorService = new ProfessorService();
    }

    /**
     * Callback method for handling Dashboard.
     * Opens ProfessorAndHodDashboard.fxml upon clicking Dashboard button
     */
    @FXML
    private void handleDashboardButtonAction(){

        FXMLLoader loader =  UISetterUtil.setContentUI("/view/ProfessorAndHodDashboard.fxml"
                , contentStackPane, subTitleLabel, subSubTitleLabel, "", "");

        ProfessorAndHodDashboardController professorAndHodDashboardController = loader.getController();

        professorAndHodDashboardController.initController(professor);
    }

    /**
     * Callback method for handling studentListButton.
     * Opens StudentSection.fxml upon clicking studentListButton.
     */
    @FXML
    private void handleStudentListButtonAction(){

        loader =  UISetterUtil.setContentUI("/view/StudentSection.fxml"
                , contentStackPane, subTitleLabel, subSubTitleLabel, "Student", "/ Student List");

        StudentSectionController studentSectionController = loader.getController();
        studentSectionController.initController(PROFESSOR_GID, professor.getDeptName());
    }

    @FXML
    private void handleMarksListButtonAction(){

        loader =  UISetterUtil.setContentUI("/view/MarksSection.fxml", contentStackPane
                , subTitleLabel, subSubTitleLabel, "Marks List", "");

        MarksSectionController marksSectionController = loader.getController();

        marksSectionController.initController(professor);
    }

    /**
     * Callback method to handle Setting button.
     * This loads the Settings UI when clicked on Setting button.
     *
     */
    @FXML
    private void handleProfessorSettingsButtonAction() {


        loader =  UISetterUtil.setContentUI("/view/ProfessorAndHodSettings.fxml", contentStackPane
                , subTitleLabel, subSubTitleLabel, "Settings", "/ Profile Settings");

        //get the controller
        ProfessorAndHodSettingsController professorAndHodSettingsController = loader.getController();

        //send the admin profile details to the controller
        professorAndHodSettingsController.initController(professor);
    }

    /**
     * Callback method to handle Change Password button.
     * This loads the Change Password UI when clicked on Change Password button.
     *
     */
    @FXML
    private void handleChangePasswordButtonAction(){

        loader =  UISetterUtil.setContentUI("/view/ChangePassword.fxml"
                , contentStackPane, subTitleLabel, subSubTitleLabel, "Change Password", "");

        ChangePasswordController changePasswordController = loader.getController();

        changePasswordController.initController(professorLoginDetails);
    }

    /**
     * Callback method to handle Log Out button action.
     * This closes the main stage and loads the Login Stage.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleLogOutButtonAction() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you really want to logout?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            //get the main stage
            Stage professorPanelStage = (Stage) logOutButton.getScene().getWindow();

            //close the main stage
            professorPanelStage.hide();

            //create login stage
            Stage loginStage = new Stage();

            //set the login stage
            UISetterUtil.setStage("/view/login/Login.fxml", loginStage
                    , PROJECT_NAME, 350, 300);

            //show the login stage
            loginStage.show();
        }
    }

    /**
     * This method sets the details of the professor in the professor panel
     *
     * @param professorLoginDetails The User object used to login to the system
     */
    @SuppressWarnings("Duplicates")
    public void setProfessorProfileDetails(User professorLoginDetails) {

        this.professorLoginDetails = professorLoginDetails;

        final String additionalQuery = "where v_prof_id=?";
        Task<List<Professor>> professorsTask = professorService
                .getProfessorTask(additionalQuery, professorLoginDetails.getUserId());

        new Thread(professorsTask).start();

        professorsTask.setOnSucceeded(new EventHandler<>() {

            @Override
            public void handle(WorkerStateEvent event) {

                professor = professorsTask.getValue().get(0);

                roleLabel.setText("Professor, " + professor.getDeptName());

                //set the profile image if it exists ,otherwise set a placeholder
                if (Paths.get(professor.getProfileImagePath().replace("file:", "")).toFile().exists()) {

                    profileImageImageView.setImage(new Image(professor.getProfileImagePath()));
                } else {

                    profileImageImageView.setImage(new Image("/png/placeholder.png"));
                }
                userIdLabel.setText(professorLoginDetails.getUserId());
                nameLabel.setText(professor.getFirstName() + " " + professor.getMiddleName()
                        + " " + professor.getLastName());
                lastLoginLabel.setText(professorLoginDetails.getLastLoginTimeStamp());

                dashboardButton.fire();
            }
        });
    }
}

