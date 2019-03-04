package controller;

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
import command.LoginCommand;
import static util.ConstantsUtil.*;
import java.io.IOException;
import java.util.List;

public class LoginController {

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

    public LoginController() {
        loginCommand = new LoginCommand();
        userService = new UserService();
        mainStage = new Stage();
        mainStage.setTitle("examin - Examination Management Tool");
        mainStage.setResizable(false);
    }

    @FXML
    private void handleSignInButtonAction(ActionEvent event1){

        Stage loginStage = (Stage)((Node)event1.getSource())
                .getScene().getWindow();
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
            public void handle(WorkerStateEvent event){

                statusStackPane.setVisible(false);
                mainGridPane.setOpacity(0.5);

                int status = LOGIN_ERROR;

                if(!usersTask.getValue().isEmpty()) {
                    status = loginCommand.authenticateLogin(password
                            , usersTask.getValue().get(0));
                }

                Parent root;

                switch (status){

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
                        }
                        catch (IOException e){
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

    @FXML
    private void handleForgotPasswordAction(ActionEvent event) throws Exception{
        SceneSetterUtil.setScene("/view/ForgotPassword.fxml","Reset Password", event);
    }

    @FXML
    private void handleSendPasswordAction(){

        String username = forgotPasswordUserNameField.getText();
        loginCommand.resetPassword(username);
    }

    @FXML
    private void handleBackToLoginHyperlinkAction(ActionEvent event) throws Exception{
        SceneSetterUtil.setScene("/view/Login.fxml","Login", event);
    }
}

