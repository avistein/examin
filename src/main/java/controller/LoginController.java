package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import util.SceneSetterUtil;
import service.LoginService;

public class LoginController {

    private LoginService loginService;

    private Stage mainStage;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private AnchorPane spinnerAnchorPane;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField forgotPasswordUserNameField;

    @FXML
    private PasswordField passwordField;

    public LoginController() {
        loginService = new LoginService();
        mainStage = new Stage();
        mainStage.setTitle("examin - Examination Management Tool");
    }

    @FXML
    private void handleSignInButtonAction(ActionEvent event) throws Exception{
        mainGridPane.setOpacity(0.5);
        spinnerAnchorPane.setVisible(true);
        String username = userNameField.getText();

        String password = passwordField.getText();

        int status = loginService.authenticateLogin(username.trim(), password);
        switch (status){

            case 0:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Invalid Username or Password");
                alert.show();
                break;

            case 1:
                FXMLLoader loader = new FXMLLoader(getClass()
                        .getResource("/view/Admin.fxml"));
                Parent root = loader.load();
                AdminController adminController = loader.getController();
                adminController.setAdminProfileDetails(username.trim());
                mainStage.setScene(new Scene(root, 1024, 768));
                ((Node)event.getSource()).getScene().getWindow().hide();
                mainStage.setMaximized(true);
                mainStage.show();
                break;

            case 2:
                root = FXMLLoader.load(SceneSetterUtil
                        .class.getResource("/view/ExamCellMember.fxml"));
                mainStage.setScene(new Scene(root, 1024, 768));
                ((Node)event.getSource()).getScene().getWindow().hide();
                mainStage.setMaximized(true);
                mainStage.show();
                break;

            case 3:
                root = FXMLLoader.load(SceneSetterUtil
                        .class.getResource("/view/ProfessorHOD.fxml"));
                mainStage.setScene(new Scene(root, 1024, 768));
                ((Node)event.getSource()).getScene().getWindow().hide();
                mainStage.setMaximized(true);
                mainStage.show();
                break;

            case 4:
                root = FXMLLoader.load(SceneSetterUtil
                        .class.getResource("/view/Professor.fxml"));
                mainStage.setScene(new Scene(root, 1024, 768));
                ((Node)event.getSource()).getScene().getWindow().hide();
                mainStage.setMaximized(true);
                mainStage.show();
                break;

        }

    }

    @FXML
    private void handleForgotPasswordAction(ActionEvent event) throws Exception{
        SceneSetterUtil.setScene("/view/ForgotPassword.fxml","Reset Password", event);
    }

    @FXML
    private void handleSendPasswordAction(){

        String username = forgotPasswordUserNameField.getText();
        loginService.resetPassword(username);
    }

    @FXML
    private void handleBackToLoginHyperlinkAction(ActionEvent event) throws Exception{
        SceneSetterUtil.setScene("/view/Login.fxml","Login", event);
    }
}

