package controller.adminPanel;

import controller.ChangePasswordController;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.ExamCellMember;
import model.User;
import service.ExamCellMemberService;
import util.UISetterUtil;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static util.ConstantsUtil.PROJECT_NAME;

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
    private Button dashboardButton;

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

        roleLabel.setText("Admin");
        examCellMemberService = new ExamCellMemberService();
        dashboardButton.fire();
    }

    /**
     * Callback method for handling Dashboard.
     * Opens Dashboard.fxml upon clicking Dashboard button.
     */
    @FXML
    private void handleDashboardButtonAction() throws IOException {

        Parent dashboardFxml = FXMLLoader.load(getClass()
                .getResource("/view/adminPanel/Dashboard.fxml"));
        subTitleLabel.setText("");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(dashboardFxml);
    }

    /**
     * Callback method for handling studentListButton.
     * Opens StudentsList.fxml upon clicking studentListButton.
     */
    @FXML
    private void handleStudentListButtonAction() throws IOException {

        Parent studentsListFxml = FXMLLoader.load(getClass()
                .getResource("/view/StudentsList.fxml"));
        subTitleLabel.setText("Student");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(studentsListFxml);
    }

    /**
     * Callback method for handling professorListButtonAction.
     * Opens StudentsList.fxml upon clicking professorListButtonAction.
     */
    @FXML
    private void handleProfessorListButtonAction() throws IOException {

        Parent professorListFxml = FXMLLoader.load(getClass()
                .getResource("/view/ProfessorsList.fxml"));
        subTitleLabel.setText("Professor");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(professorListFxml);
    }

    /**
     * Callback method for handling examCellMemberListButton.
     * Opens StudentsList.fxml upon clicking examCellMemberListButton.
     */
    @FXML
    private void handleExamCellMemberListButtonAction() throws IOException {

        Parent examCellMemberListFxml = FXMLLoader.load(getClass()
                .getResource("/view/adminPanel/ExamCellMembersList.fxml"));
        subTitleLabel.setText("Exam Cell Member");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(examCellMemberListFxml);
    }

    /**
     * Callback method for handling clicking event on Academic Administration Button
     *
     * @throws IOException Load exception on loading the fxml
     */
    @FXML
    private void handleAcademicAdministrationButtonAction() throws IOException {

        Parent academicAdministrationFxml = FXMLLoader.load(getClass()
                .getResource("/view/adminPanel/AcademicAdministration.fxml"));
        subTitleLabel.setText("Academic Administration");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(academicAdministrationFxml);
    }

    @FXML
    private void handleExamsListButtonAction() {

    }

    @FXML
    private void handleMarksListButtonAction() {

    }

    @FXML
    private void handleNoticesListButtonAction() {

    }

    @FXML
    private void handleReportsListButtonAction() {

    }

    /**
     * Callback method to handle Setting button.
     * This loads the Settings UI when clicked on Setting button.
     *
     * @throws IOException Load exception during loading the FXML document.
     */
    @FXML
    private void handleAdminSettingsButtonAction() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminPanel/Settings.fxml"));

        Parent adminSettingsFxml = loader.load();

        //get the controller
        SettingsController settingsController = loader.getController();

        //send the admin profile details to the controller
        settingsController.setAdminProfileDetails(admin);

        subTitleLabel.setText("Settings");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(adminSettingsFxml);
    }

    /**
     * Callback method to handle Change Password button.
     * This loads the Change Password UI when clicked on Change Password button.
     *
     * @throws IOException Load exception during loading the FXML document.
     */
    @FXML
    private void handleChangePasswordButtonAction() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/view/adminPanel/ChangePassword.fxml"));

        Parent adminSettingsFxml = loader.load();

        ChangePasswordController changePasswordController = loader.getController();

        changePasswordController.setUserLoginDetails(adminLogin);
        subTitleLabel.setText("Change Password");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(adminSettingsFxml);
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
            }
        });
    }
}

