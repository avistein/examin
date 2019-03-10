package controller;

import command.LoginCommand;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.User;
import service.UserService;
import util.SceneSetterUtil;

import java.io.IOException;
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
        mainStage.setTitle("examin - Examination Management Tool");
        mainStage.setResizable(false);
    }

    /**
     * Callback method for handle Sign In button click event and set the panel  or display error msg.
     *
     * @param event The event containing the click on the Sign in button
     */
    @FXML
    private void handleSignInButtonAction(ActionEvent event) {

        //get the stage of the Login screen
        Stage loginStage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

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

                Parent root;

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
                        FXMLLoader loader = new FXMLLoader(getClass()
                                .getResource("/view/Admin.fxml"));
                        try {
                            root = loader.load();
                            AdminController adminController = loader
                                    .getController();
                            adminController.setAdminProfileDetails
                                    (username.trim());
                            mainStage.setScene(new Scene(root, 1024
                                    , 768));
                            loginStage.hide();
                            mainStage.setMaximized(true);
                            mainStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;

                    case EXAM_CELL_MEMBER_GID:
                        try {
                            root = FXMLLoader.load(SceneSetterUtil
                                    .class.getResource("/view/ExamCellMember.fxml"));
                            mainStage.setScene(new Scene(root, 1024
                                    , 768));
                            loginStage.hide();
                            mainStage.setMaximized(true);
                            mainStage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;

                    case PROFESSOR_HOD_GID:

                        try {
                            root = FXMLLoader.load(SceneSetterUtil
                                    .class.getResource("/view/ProfessorHOD.fxml"));
                            mainStage.setScene(new Scene(root, 1024
                                    , 768));
                            loginStage.hide();
                            mainStage.setMaximized(true);
                            mainStage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;

                    case PROFESSOR_GID:
                        try {
                            root = FXMLLoader.load(SceneSetterUtil
                                    .class.getResource("/view/Professor.fxml"));
                            mainStage.setScene(new Scene(root, 1024
                                    , 768));
                            loginStage.hide();
                            mainStage.setMaximized(true);
                            mainStage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;

                }
            }
        });
    }

    /**
     * Callback method to set the Forgot Password scene
     *
     * @param event The click event containing the Forgot Password button click event.
     * @throws Exception Load exception while loading the fxml document.
     */
    @FXML
    private void handleForgotPasswordAction(ActionEvent event) throws Exception {
        SceneSetterUtil.setScene("/view/ForgotPassword.fxml", "Reset Password", event);
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
     * Callback method to get back the Login window once reset password job is done or as
     * per the user's choice.
     *
     * @param event Click event of the backtoLoginHyperlink
     * @throws Exception Load exception while loading the fxml document.
     */
    @FXML
    private void handleBackToLoginHyperlinkAction(ActionEvent event) throws Exception {
        SceneSetterUtil.setScene("/view/Login.fxml", "Login", event);
    }
}

