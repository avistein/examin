package controller.login;

import command.LoginCommand;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.UISetterUtil;

import static util.ConstantsUtil.PROJECT_NAME;

public class ForgotPasswordController {

    /*--------------------------------Declaration and initialization of variables-------------------------------------*/

    @FXML
    private TextField userIdTextField;

    private LoginCommand loginCommand;

    /*---------------------------------------------End of initialization----------------------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    private void initialize() {

        loginCommand = new LoginCommand();
    }

    /**
     * Callback method to send the user a new password on clicking the Send Password button.
     */
    @FXML
    private void handleSubmitButtonAction() {

        String username = userIdTextField.getText();
        loginCommand.resetPassword(username);
    }

    /**
     * Callback method to get back the Login window once reset password job is done or whenever
     * the user wants to go back.
     */
    @FXML
    private void handleBackToLoginHyperlinkAction() {

        //get back the login stage
        Stage loginStage = (Stage) userIdTextField.getScene().getWindow();

        //attach back the Login scene in the stage
        UISetterUtil.setStage("/view/login/Login.fxml", loginStage
                , PROJECT_NAME, 350, 300);
    }
}
