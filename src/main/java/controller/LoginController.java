package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.SceneSetterUtil;
import service.LoginService;

public class LoginController {

    private LoginService loginService;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField forgotPasswordUserNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button forgotPasswordButton;

    @FXML
    private Button sendPasswordButton;

    public LoginController() {
        loginService = new LoginService();
    }

    @FXML
    private void handleSignInButtonAction(ActionEvent event) throws Exception{

        String username = userNameField.getText();

        String password = passwordField.getText();

        int status = loginService.authenticateLogin(username, password);

        switch (status){

            case 0:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Invalid Username or Password");
                alert.show();
                break;

            case 1:
                SceneSetterUtil.setScene("/view/Admin.fxml","Admin Panel", event);
                break;

            case 2:
                SceneSetterUtil.setScene("/view/ExamCellMember.fxml","Admin Panel", event);
                break;

            case 3:
                SceneSetterUtil.setScene("/view/ProfessorHOD.fxml","Admin Panel", event);
                break;

            case 4:
                SceneSetterUtil.setScene("/view/Professor.fxml","Admin Panel", event);
                break;
        }

    }

    @FXML
    private void handleForgotPasswordAction(ActionEvent event) throws Exception{
        SceneSetterUtil.setScene("/view/ForgotPassword.fxml","Reset Password", event);
    }

    @FXML
    private void handleSendPasswordAction(ActionEvent event) throws Exception{

        String username = forgotPasswordUserNameField.getText();
        loginService.resetPassword(username);
    }

    @FXML
    private void handleBackToLoginHyperlinkAction(ActionEvent event) throws Exception{
        SceneSetterUtil.setScene("/view/Login.fxml","Login", event);
    }
}

