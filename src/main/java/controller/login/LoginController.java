package controller.login;

import command.LoginCommand;
import controller.adminPanel.AdminPanelController;
import controller.professorHodPanel.ProfessorHodPanelController;
import controller.professorPanel.ProfessorPanelController;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.User;
import service.FileHandlingService;
import service.UserService;
import util.UISetterUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Controller class for handling Login job.
 * for Login.fxml
 *
 * @author Avik Sarkar
 */
public class LoginController {

    private LoginCommand loginCommand;

    private Stage mainStage;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private ImageView universityLogoImageView;

    @FXML
    private StackPane statusStackPane;

    @FXML
    private TextField userIdTextField;

    @FXML
    private PasswordField passwordField;

    private UserService userService;


    /*-----------------------------------------End of initialization-------------------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    public void initialize() {

        loginCommand = new LoginCommand();
        userService = new UserService();

        //get the location of settings.properties to set the University logo
        Path path = Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, "settings.properties");

        //check if the settings.properties exists the user's system
        if (Files.exists(path)) {

            FileHandlingService fileHandlingService = new FileHandlingService();

            //get the location of University logo
            Map<String, String> propMap = fileHandlingService.loadPropertiesValuesFromPropertiesFile
                    ("settings.properties", "universityLogoLocation");

            universityLogoImageView.setImage(new Image(propMap.get("universityLogoLocation")));
        }

        //create the main stage of the application
        mainStage = new Stage();

        //the stage should always be in maximized mode
        mainStage.setMaximized(true);
    }

    /**
     * Callback method for handle Sign In button click event and set the panel  or display error msg.
     */
    @FXML
    private void handleLoginButtonAction() {

        if (validateLoginItems()) {

            //get the stage of the Login screen
            Stage loginStage = (Stage) userIdTextField.getScene().getWindow();

            //Background faded once the Sign In button is clicked.
            mainGridPane.setOpacity(0.5);

            //this should display a progress spinner before database connection
            statusStackPane.setVisible(true);

            String username = userIdTextField.getText().trim();
            String password = passwordField.getText();

            //get the task to get login details of the userId
            Task<List<User>> usersTask = userService.getUsersLoginDetailsTask("WHERE v_user_id=?", username);
            new Thread(usersTask).start();

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

                        //set the status of authentication
                        status = loginCommand.authenticateLogin(password
                                , usersTask.getValue().get(0));

                        //if login is successful then update the login timestamp
                        if (status != LOGIN_ERROR) {

                            Task<Integer> updateUserLastLoginTimeStampTask = userService.getUpdateUserLastLoginTimeStampTask
                                    (usersTask.getValue().get(0));
                            new Thread(updateUserLastLoginTimeStampTask).start();
                        }
                    }

                    //Open different panel for different types of user on successful  login.
                    switch (status) {

                        case LOGIN_ERROR:

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("Invalid Username or Password");
                            alert.show();
                            mainGridPane.setOpacity(1);
                            userIdTextField.clear();
                            passwordField.clear();
                            break;

                        case ADMIN_GID:

                            FXMLLoader loader = UISetterUtil.setStage("/view/adminPanel/AdminPanel.fxml"
                                    , mainStage, PROJECT_NAME, 768, 1024);

                            AdminPanelController adminPanelController = loader.getController();

                            //send admin's login details to the admin panel controller
                            adminPanelController.setAdminProfileDetails(usersTask.getValue().get(0));

                            mainStage.show();
                            loginStage.hide();

                            break;

                        case PROFESSOR_HOD_GID:

                            loader = UISetterUtil.setStage("/view/professorHodPanel/ProfessorHodPanel.fxml"
                                    , mainStage, PROJECT_NAME, 768, 1024);

                            ProfessorHodPanelController professorHodPanelController = loader.getController();

                            professorHodPanelController.setProfessorHodProfileDetails(usersTask.getValue().get(0));

                            mainStage.show();
                            loginStage.hide();

                            break;

                        case PROFESSOR_GID:

                            loader = UISetterUtil.setStage("/view/professorPanel/ProfessorPanel.fxml", mainStage
                                    , PROJECT_NAME, 768, 1024);

                            ProfessorPanelController professorPanelController = loader.getController();

                            professorPanelController.setProfessorProfileDetails(usersTask.getValue().get(0));

                            mainStage.show();
                            loginStage.hide();

                            break;
                    }
                }
            });
        }
    }

    /**
     * Callback method to set the Forgot Password scene
     */
    @FXML
    private void handleForgotPasswordHyperlinkAction() {

        //get the login stage
        Stage loginStage = (Stage) userIdTextField.getScene().getWindow();

        //attach the ForgotPassword scene in the stage
        UISetterUtil.setStage("/view/login/ForgotPassword.fxml", loginStage
                , PROJECT_NAME, 350, 300);
        //loginStage.show();
    }

    /**
     * This method is used to validate items in the Login UI.
     */
    private boolean validateLoginItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (userIdTextField.getText().trim().isEmpty()) {

            alert.setHeaderText("User ID cannot be empty!");
            alert.show();
            return false;
        } else if (passwordField.getText().isEmpty()) {

            alert.setHeaderText("Password cannot be empty!");
            alert.show();
            return false;
        }
        return true;
    }
}

