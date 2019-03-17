package controller.login;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Email;
import model.User;
import service.EmailService;
import service.FileHandlingService;
import service.UserService;
import util.PasswordGenUtil;
import util.UISetterUtil;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 *
 */
public class ForgotPasswordController {

    /*--------------------------------Declaration and initialization of variables-------------------------------------*/

    @FXML
    private GridPane mainGridPane;

    @FXML
    private StackPane statusStackPane;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ImageView statusImageView;

    @FXML
    private Label statusLabel;

    @FXML
    private Label msgLabel;

    @FXML
    private TextField userIdTextField;

    private User user;

    private UserService userService;

    private EmailService emailService;

    private FileHandlingService fileHandlingService;

    /*---------------------------------------------End of initialization----------------------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    private void initialize() {

        user = new User();
        userService = new UserService();
        emailService = new EmailService();
        fileHandlingService = new FileHandlingService();
    }

    /**
     * Callback method to send the user a new password on clicking the Submit button.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitButtonAction() {

        //if the user has typed something in the User name textField then only go ahead, otherwise show error msg
        if (validate()) {

            //fade the background and show a loading spinner
            mainGridPane.setOpacity(0.1);
            statusStackPane.setVisible(true);
            progressIndicator.setVisible(true);

            //get a new random password and it's hash
            Map<String, String> passMap = PasswordGenUtil.generateNewPassword();

            String password = passMap.get("password");
            String hashedPassword = passMap.get("hashedPassword");

            user.setUserId(userIdTextField.getText());
            user.setPassword(hashedPassword);

            Task<Integer> updateUserPassTask = userService.getUpdateUserPassTask(user);
            new Thread(updateUserPassTask).start();

            updateUserPassTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    //gee the status of updating the user's password in the db with the new generated pass
                    int status = updateUserPassTask.getValue();

                    //show status of the update in the db
                    statusImageView.setVisible(true);
                    statusLabel.setVisible(true);


                    //display status
                    if (status == DATABASE_ERROR) {

                        progressIndicator.setVisible(false);
                        statusImageView.setImage(new Image("png/critical error.png"));
                        statusLabel.setText("Database Error!");
                        msgLabel.setVisible(true);
                    } else if (status == SUCCESS) {

                        //password updated in the db, now send user an email containing the new password
                        statusImageView.setImage(null);
                        statusLabel.setText("Password generated , sending email now!");
                        sendEmail(password);
                    } else {

                        progressIndicator.setVisible(false);
                        statusImageView.setImage(new Image("png/error.png"));
                        statusLabel.setText("User doesn't exist in the system yet!");
                        msgLabel.setVisible(true);
                    }
                }
            });
        }
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

    /**
     * Callback method to handle click event on the Status Stack Pane.
     * This method brings back the normal UI as it was before clicking on the Submit button.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleStatusStackPaneMouseClickedAction() {

        mainGridPane.setOpacity(1);
        statusStackPane.setVisible(false);
        progressIndicator.setVisible(false);
        statusImageView.setVisible(false);
        statusLabel.setVisible(false);
        msgLabel.setVisible(false);
    }

    /**
     * This method sends an email containing the newly generated password to the user.
     * <p>
     * This method at first gets the User's contact details like email ID and name from the database, then it gets the
     * admin's email address from the email.properties and then sends the newly generated password to user's email
     * address.
     *
     * @param password The newly generated password.
     */
    private void sendEmail(String password) {

        //get the name and email ID of the user
        Task<List<User>> userContactTask = userService.getUserContactTask("where v_user_id=?"
                , user.getUserId());
        new Thread(userContactTask).start();

        userContactTask.setOnSucceeded(new EventHandler<>() {

            @Override
            public void handle(WorkerStateEvent event) {

                user.setName(userContactTask.getValue().get(0).getName());
                user.setEmail(userContactTask.getValue().get(0).getEmail());

                //if the email.properties file exists in the system then only send email
                if (Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, "email.properties").toFile().exists()) {

                    //get the admin's email address
                    String senderEmail = fileHandlingService.loadPropertiesValuesFromPropertiesFile
                            ("email.properties", "adminEmailId").get("adminEmailId");

                    //get the user's email address
                    String recipientEmail = user.getEmail();

                    //set the subject of the email
                    String subject = "Your examin password has been reset";

                    //body of the email
                    String body = "<div>Dear " + user.getName() + ",<p>Your examin password for User ID "
                            + user.getUserId() + " has been reset. Your new password is " + password +
                            "<p>Thanks & Regards,<br>admin";

                    //content type of the email
                    String contentType = "text/html";

                    //create new email object
                    Email email = new Email(senderEmail, recipientEmail, subject, body, contentType);


                    Task<Boolean> sendEmailTask = emailService.getSendEmailTask(email);
                    new Thread(sendEmailTask).start();

                    sendEmailTask.setOnSucceeded(new EventHandler<>() {
                        @Override
                        public void handle(WorkerStateEvent event) {

                            //everything is complete,so now disable the progress indicator
                            progressIndicator.setVisible(false);

                            //display status of email sending operation
                            if (sendEmailTask.getValue()) {

                                statusImageView.setImage(new Image("png/success.png"));
                                statusLabel.setText("New password has been mailed to your registered email " +
                                        "successfully!");
                                msgLabel.setVisible(true);
                                userIdTextField.clear();
                            } else {

                                statusImageView.setImage(new Image("png/error.png"));
                                statusLabel.setText("Unable to send email!");
                                msgLabel.setVisible(true);
                            }
                        }
                    });
                }

                //email.properties doesn't exist ,so can't send email
                else {

                    progressIndicator.setVisible(false);
                    statusImageView.setImage(new Image("png/error.png"));
                    statusLabel.setText("Unable to send email!");
                    msgLabel.setVisible(true);
                }
            }
        });
    }

    /**
     * This method ensures that the User ID textfield is not empty.
     *
     * @return Return the status of the validation i.e. true or false.
     */
    private boolean validate() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (userIdTextField.getText() == null || userIdTextField.getText().trim().isEmpty()) {

            alert.setHeaderText("Please enter a User ID!");
            alert.show();
            return false;
        }
        return true;
    }
}
