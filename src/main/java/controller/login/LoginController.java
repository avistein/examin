package controller.login;

import command.LoginCommand;
import controller.adminPanel.AdminPanelController;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.User;
import service.UserService;
import util.UISetterUtil;

import java.util.List;

import static util.ConstantsUtil.*;

/**
 * Controller class for handling Login job.
 * for Login.fxml
 *
 * @author Avik Sarkar
 */
public class LoginController {

    /*--------------------Declaration and initialization of variables--------------------*/

    private LoginCommand loginCommand;

    private Stage mainStage;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private StackPane statusStackPane;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField forgotPasswordUserNameField;

    @FXML
    private PasswordField passwordField;

    private UserService userService;


    /*-------------------------------End of initialization--------------------------------*/

    /**
     * This method is called once the FXML document is loaded.
     */
    @FXML
    public void initialize() {
        loginCommand = new LoginCommand();
        userService = new UserService();
        mainStage = new Stage();
        mainStage.setMaximized(true);
    }

    /**
     * Callback method for handle Sign In button click event and set the panel  or display error msg.
     */
    @FXML
    private void handleSignInButtonAction() {

        //get the stage of the Login screen
        Stage loginStage = (Stage) userNameField.getScene().getWindow();

        //Background faded once the Sign In button is clicked.
        mainGridPane.setOpacity(0.5);

        //this should display a progress spinner before database connection
        statusStackPane.setVisible(true);

        String username = userNameField.getText().trim();
        String password = passwordField.getText();

        Task<List<User>> usersTask = userService.getUsersTask("where v_user_id=?", username);
        new Thread(usersTask).start();
        //saveThread.setDaemon(true);
        //saveThread.start();
        usersTask.setOnSucceeded(new EventHandler<>() {

            @Override
            public void handle(WorkerStateEvent event) {

                //db connection and work done, so deactivate Status.
                statusStackPane.setVisible(false);
                mainGridPane.setOpacity(1);

                //by default status is login err.
                int status = LOGIN_ERROR;

                //authenticates the username and password of the user
                if (!usersTask.getValue().isEmpty()) {
                    status = loginCommand.authenticateLogin(password
                            , usersTask.getValue().get(0));
                }

                //Open different panel for different types of user on successful  login.
                switch (status) {

                    case LOGIN_ERROR:
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Invalid Username or Password");
                        alert.show();
                        mainGridPane.setOpacity(1);
                        userNameField.clear();
                        passwordField.clear();
                        break;

                    case ADMIN_GID:

                        FXMLLoader loader = UISetterUtil.setStage("/view/adminPanel/AdminPanel.fxml", mainStage
                                , PROJECT_NAME, 768, 1024);
                        AdminPanelController adminPanelController = loader
                                .getController();
                        adminPanelController.setAdminProfileDetails
                                (username.trim());
                        mainStage.show();
                        loginStage.hide();
                        break;

                    case EXAM_CELL_MEMBER_GID:

                        loader = UISetterUtil.setStage("/view/examCellMemberPanel/ExamCellMemberPanel.fxml", mainStage
                                , PROJECT_NAME, 768, 1024);
                        mainStage.show();
                        loginStage.hide();

                        break;

                    case PROFESSOR_HOD_GID:

                        loader = UISetterUtil.setStage("/view/professorHodPanel/ProfessorHodPanel.fxml", mainStage
                                , PROJECT_NAME, 768, 1024);
                        mainStage.show();
                        loginStage.hide();

                        break;

                    case PROFESSOR_GID:

                        loader = UISetterUtil.setStage("/view/professorPanel/ProfessorPanel.fxml", mainStage
                                , PROJECT_NAME, 768, 1024);
                        mainStage.show();
                        loginStage.hide();

                        break;

                }
            }
        });
    }

    /**
     * Callback method to set the Forgot Password scene
     */
    @FXML
    private void handleForgotPasswordAction() {
        Stage loginStage = (Stage) userNameField.getScene().getWindow();
        UISetterUtil.setStage("/view/login/ForgotPassword.fxml", loginStage
                , PROJECT_NAME, 400, 400);
        loginStage.show();
    }

    /**
     * Callback method to send the user a new password on clicking the Send Password button.
     */
    @FXML
    private void handleSendPasswordAction() {

        String username = forgotPasswordUserNameField.getText();
        loginCommand.resetPassword(username);
    }

    /**
     * Callback method to get back the Login window once reset password job is done or whenever
     * the user wants to go back.
     */
    @FXML
    private void handleBackToLoginHyperlinkAction() {

        Stage loginStage = (Stage) forgotPasswordUserNameField.getScene().getWindow();
        UISetterUtil.setStage("/view/login/Login.fxml", loginStage
                , PROJECT_NAME, 400, 400);
        loginStage.show();
    }
}

