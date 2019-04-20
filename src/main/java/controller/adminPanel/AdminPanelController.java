package controller.adminPanel;

import controller.ChangePasswordController;
import controller.MarksSectionController;
import controller.ProfessorSectionController;
import controller.StudentSectionController;
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
import service.ExamCellMemberService;
import service.FileHandlingService;
import util.UISetterUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static util.ConstantsUtil.*;

/**
 * Controller class for adminPanel.fxml.
 * Loads the individual fxml upon clicking the buttons on left side.
 *
 * @author Avik Sarkar
 */
public class AdminPanelController {

    /*-------------------------------Initialization and declaration of variables-----------------------------------*/

    private ExamCellMemberService examCellMemberService;

    private ExamCellMember admin;

    private User adminLogin;

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


        roleLabel.setText("Admin");
        examCellMemberService = new ExamCellMemberService();
        dashboardButton.fire();
    }

    /**
     * Callback method for handling Dashboard.
     * Opens Dashboard.fxml upon clicking Dashboard button.
     */
    @FXML
    private void handleDashboardButtonAction(){

        UISetterUtil.setContentUI("/view/adminPanel/dashboard.fxml", contentStackPane, subTitleLabel
                , subSubTitleLabel, "", "");
    }

    /**
     * Callback method for handling studentListButton.
     * Opens StudentSection.fxml upon clicking studentListButton.
     */
    @FXML
    private void handleStudentListButtonAction(){

        FXMLLoader loader =  UISetterUtil.setContentUI("/view/StudentSection.fxml", contentStackPane
                , subTitleLabel, subSubTitleLabel, "Student", "/ Student List");

        StudentSectionController studentSectionController = loader.getController();
        studentSectionController.initController(ADMIN_GID, "");
    }

    /**
     * Callback method for handling professorListButtonAction.
     * Opens StudentSection.fxml upon clicking professorListButtonAction.
     */
    @FXML
    private void handleProfessorListButtonAction(){

        FXMLLoader loader =  UISetterUtil.setContentUI("/view/ProfessorSection.fxml", contentStackPane
                , subTitleLabel, subSubTitleLabel, "Professor", "/ Professor List");

        ProfessorSectionController professorSectionController = loader.getController();
        professorSectionController.initController(ADMIN_GID, "");
    }

    /**
     * Callback method for handling clicking event on Academic Administration Button
     *
     */
    @FXML
    private void handleAcademicAdministrationButtonAction(){

        UISetterUtil.setContentUI("/view/adminPanel/AcademicAdministration.fxml", contentStackPane
                , subTitleLabel, subSubTitleLabel, "Academic Administration", "/ Departments");
    }

    @FXML
    private void handleExamsAdministrationButtonAction(){

        UISetterUtil.setContentUI("/view/adminPanel/ExamAdministration.fxml", contentStackPane
                , subTitleLabel, subSubTitleLabel, "Exam Administration", "/ Create Exam");
    }

    @FXML
    private void handleMarksListButtonAction(){

        FXMLLoader loader =  UISetterUtil.setContentUI("/view/MarksSection.fxml", contentStackPane
                , subTitleLabel, subSubTitleLabel, "Marks List", "");

        MarksSectionController marksSectionController = loader.getController();

        marksSectionController.initController(null);
    }

    /**
     * Callback method to handle Setting button.
     * This loads the Settings UI when clicked on Setting button.
     *
     */
    @FXML
    private void handleAdminSettingsButtonAction() {


        FXMLLoader loader =  UISetterUtil.setContentUI("/view/adminPanel/Settings.fxml", contentStackPane
                , subTitleLabel, subSubTitleLabel, "Settings", "/ Profile Settings");

        //get the controller
        SettingsController settingsController = loader.getController();

        //send the admin profile details to the controller
        settingsController.setAdminProfileDetails(admin);
    }

    /**
     * Callback method to handle Change Password button.
     * This loads the Change Password UI when clicked on Change Password button.
     *
     * @throws IOException Load exception during loading the FXML document.
     */
    @FXML
    private void handleChangePasswordButtonAction() throws IOException {

        FXMLLoader loader =  UISetterUtil.setContentUI("/view/ChangePassword.fxml"
                , contentStackPane, subTitleLabel, subSubTitleLabel, "Change Password", "");

        ChangePasswordController changePasswordController = loader.getController();

        changePasswordController.initController(adminLogin);
    }

    /**
     * Callback method to handle Log Out button action.
     * This closes the main stage and loads the Login Stage.
     */
    @FXML
    private void handleLogOutButtonAction() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Do you really want to logout?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            //get the main stage
            Stage adminPanelStage = (Stage) logOutButton.getScene().getWindow();

            //close the main stage
            adminPanelStage.hide();

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
     * This method sets the details of the admin in the admin panel
     *
     * @param adminLogin The User object used to login to the system
     */
    public void setAdminProfileDetails(User adminLogin) {

        this.adminLogin = adminLogin;

        final String additionalQuery = "where v_emp_id=?";
        Task<List<ExamCellMember>> examCellMembersTask = examCellMemberService
                .getExamCellMembersTask(additionalQuery, adminLogin.getUserId());

        new Thread(examCellMembersTask).start();

        examCellMembersTask.setOnSucceeded(new EventHandler<>() {

            @Override
            public void handle(WorkerStateEvent event) {

                admin = examCellMembersTask.getValue().get(0);

                //set the profile image if it exists ,otherwise set a placeholder
                if (Paths.get(admin.getProfileImagePath().replace("file:", "")).toFile().exists()) {

                    profileImageImageView.setImage(new Image(admin.getProfileImagePath()));
                } else {

                    profileImageImageView.setImage(new Image("/png/placeholder.png"));
                }
                userIdLabel.setText(adminLogin.getUserId());
                nameLabel.setText(admin.getFirstName() + " " + admin.getMiddleName()
                        + " " + admin.getLastName());
                lastLoginLabel.setText(adminLogin.getLastLoginTimeStamp());
            }
        });
    }
}

