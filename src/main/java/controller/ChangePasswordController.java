package controller;

import command.LoginCommand;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import service.UserService;

import static util.ConstantsUtil.*;

/**
 * Controller class for /ChangePassword.fxml
 *
 * @author Avik Sarkar
 */
public class ChangePasswordController {

    /*---------------------------------Initialization and declaration of variables------------------------------------*/

    @FXML
    private GridPane mainGridPane;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmNewPasswordField;

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

    private LoginCommand loginCommand;

    private User userLoginDetails;

    private UserService userService;

    /*--------------------------------------------End of Initialization-----------------------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    public void initController(User userLoginDetails) {

        loginCommand = new LoginCommand();
        userService = new UserService();

        this.userLoginDetails = userLoginDetails;
    }

    /**
     * Callback method to handle action of Submit Button.
     * <p>
     * This method basically at first checks if the Current Password entered by the User matches with the password
     * present in the database for that User. If it matches , take the new password entered by the User ,hash it and
     * store it in the database. It it doesn't match , display an error status.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitButtonAction() {

        if (validate()) {

            //match the current password entered by user with the password in the db
            int gid = loginCommand.authenticateLogin(currentPasswordField.getText(), userLoginDetails);

            //if it matches
            if (gid == ADMIN_GID || gid == EXAM_CELL_MEMBER_GID || gid == PROFESSOR_GID || gid == PROFESSOR_HOD_GID) {

                //hash the pass
                String hashedPassword = BCrypt.hashpw(newPasswordField.getText(), BCrypt.gensalt());
                userLoginDetails.setPassword(hashedPassword);

                //fade the background and show loading spinner
                mainGridPane.setOpacity(0.2);
                statusStackPane.setVisible(true);
                progressIndicator.setVisible(true);

                Task<Integer> updateUserPassTask = userService.getUpdateUserPassTask(userLoginDetails);
                new Thread(updateUserPassTask).start();

                updateUserPassTask.setOnSucceeded(new EventHandler<>() {

                    @Override
                    public void handle(WorkerStateEvent event) {

                        //password update operation status
                        int status = updateUserPassTask.getValue();

                        //password update operation is done, deactivate loading spinner and show status
                        progressIndicator.setVisible(false);
                        statusImageView.setVisible(true);
                        statusLabel.setVisible(true);
                        msgLabel.setVisible(true);

                        //display status
                        if (status == DATABASE_ERROR) {

                            statusImageView.setImage(new Image("png/critical error.png"));
                            statusLabel.setText("Database Error!");
                        } else if (status == SUCCESS) {

                            statusImageView.setImage(new Image("png/success.png"));
                            statusLabel.setText("Successfully changed password!");
                        } else {

                            statusImageView.setImage(new Image("png/error.png"));
                            statusLabel.setText("User doesn't exist!");
                        }
                    }
                });
            }

            //current password entered by the user didn't match with the password in db
            else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Current password is wrong or invalid!");
                alert.show();
            }
        }
    }

    /**
     * This method brings back the normal UI as it was before changing the password.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleStatusStackPaneOnMouseClicked() {

        if(!progressIndicator.isVisible()){

            mainGridPane.setOpacity(1);
            statusStackPane.setVisible(false);
            statusImageView.setVisible(false);
            statusLabel.setVisible(false);
            msgLabel.setVisible(false);
            newPasswordField.clear();
            confirmNewPasswordField.clear();
            currentPasswordField.clear();
        }
    }

    /**
     * Method to validate the textFields data.
     *
     * @return Result of the validation i.e. true or false.
     */
    private boolean validate() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (currentPasswordField.getText().isEmpty()) {

            alert.setHeaderText("Please enter your current password!");
            alert.show();
            return false;
        }
        if (newPasswordField.getText().isEmpty()) {

            alert.setHeaderText("Please enter new password!");
            alert.show();
            return false;
        }
        if (confirmNewPasswordField.getText().isEmpty()) {

            alert.setHeaderText("Please confirm your new password!");
            alert.show();
            return false;
        }
        if (!newPasswordField.getText().equals(confirmNewPasswordField.getText())) {

            alert.setHeaderText("Passwords don't match!");
            alert.show();
            return false;
        }
        return true;
    }
}
