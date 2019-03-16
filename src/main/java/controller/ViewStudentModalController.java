package controller;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Student;
import service.StudentService;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import static util.ConstantsUtil.*;

/**
 * Controller class for ViewStudentModal.fxml.
 *
 * @author Avik Sarkar
 */
public class ViewStudentModalController {

    /*--------------------------------Initialization & Declaration of variables----------------------------------*/

    private Student student;

    private boolean studentDeletedStatus;

    private StudentService studentService;

    @FXML
    private Label nameLabelInTitle;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label nameLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label dobLabel;

    @FXML
    private Label regIdLabel;

    @FXML
    private Label rollNoLabel;

    @FXML
    private Label regYearLabel;

    @FXML
    private Label batchNameLabel;

    @FXML
    private Label disciplineLabel;

    @FXML
    private Label degreeLabel;

    @FXML
    private Label currSemesterLabel;

    @FXML
    private Label motherNameLabel;

    @FXML
    private Label guardianNameLabel;

    @FXML
    private Label contactNoLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label guardianContactNoLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private StackPane statusStackPane;

    @FXML
    private ImageView statusImageView;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label statusLabel;

    /*--------------------------------End of Initialization & Declaration ----------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    private void initialize() {

        studentService = new StudentService();

        //default status, no student has been deleted
        studentDeletedStatus = false;
    }

    /**
     * Callback method to handle the Edit Button action.
     *
     * @throws IOException Load exception while loading the FXML document.
     */
    @FXML
    private void handleEditButtonAction() throws IOException {

        //get the stage of the modal window
        Stage modalStage = (Stage) mainGridPane.getScene().getWindow();

        //get the parent stage of the modal window, i.e. the mainStage of the application
        Stage mainStage = (Stage) modalStage.getOwner();

        //get the main scene of the application
        Scene mainScene = mainStage.getScene();

        //get the contentStackPane here
        StackPane contentStackPane = (StackPane) mainScene.lookup("#contentStackPane");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudentRegistration.fxml"));
        Parent studentRegistrationFxml = loader.load();

        //get the controller of StudentRegistration.fxml
        StudentRegistrationController studentRegistrationController = loader.getController();

        //send the Student object to be edited
        studentRegistrationController.setStudentPojo(student);

        //send the edit signal
        studentRegistrationController.setEditSignal(EDIT_CHOICE);

        //close the modal window
        modalStage.hide();

        //place the StudentRegistration.fxml in the scene
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(studentRegistrationFxml);
    }

    @FXML
    private void handleDeleteButtonAction() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Are you really want to delete?");

        Optional<ButtonType> result = alert.showAndWait();

        //if OK is pressed in the Confirmation alert
        if (result.get() == ButtonType.OK) {

            /*fade the background and show loading spinner*/
            mainGridPane.setOpacity(0.5);
            statusStackPane.setVisible(true);
            progressIndicator.setVisible(true);

            Task<Integer> deleteStudentTask = studentService
                    .getDeleteStudentTask(student);
            new Thread(deleteStudentTask).start();

            deleteStudentTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    //get the status of the deletion operation
                    int status = deleteStudentTask.getValue();

                    /*disable the loading spinner and display status*/
                    progressIndicator.setVisible(false);
                    statusImageView.setVisible(true);
                    statusLabel.setVisible(true);

                    if (status == DATABASE_ERROR) {

                        statusImageView.setImage(new Image("/png/critical error.png"));
                        statusLabel.setText("Database Error!");
                        studentDeletedStatus = false;
                    } else if (status == SUCCESS) {

                        statusImageView.setImage(new Image("/png/success.png"));
                        statusLabel.setText("Successfully Deleted!");
                        studentDeletedStatus = true;
                    } else if (status == DATA_DEPENDENCY_ERROR) {

                        statusImageView.setImage(new Image("/png/error.png"));
                        statusLabel.setText("Cannot delete student!");
                        studentDeletedStatus = false;
                    } else {

                        statusImageView.setImage(new Image("/png/error.png"));
                        statusLabel.setText("Student not found!");
                        studentDeletedStatus = false;
                    }
                }
            });
        }
    }

    /**
     * This method is used to set the Student object for editing or deleting it.
     * Also sets the text of the labels.
     *
     * @param student The student to be set with for deletion or editing.
     */
    public void setStudentPojo(Student student) {

        this.student = student;

        //if the Image exists in that location, then set the ImageView with that, otherwise set a placeholder
        if (Paths.get(student.getProfileImagePath()).toFile().exists()) {

            profileImageView.setImage(new Image("file:" + student.getProfileImagePath()));
        } else {

            profileImageView.setImage(new Image("/png/placeholder.png"));
        }
        nameLabelInTitle.setText(this.student.getFirstName() + "'s");
        nameLabel.setText(this.student.getFirstName() + " "
                + this.student.getMiddleName() + " " +
                this.student.getLastName());
        genderLabel.setText(this.student.getGender());
        dobLabel.setText(this.student.getDob());
        regIdLabel.setText(this.student.getRegId());
        rollNoLabel.setText(this.student.getRollNo());
        regYearLabel.setText(this.student.getRegYear());
        batchNameLabel.setText(this.student.getBatchName());
        disciplineLabel.setText(this.student.getDiscipline());
        degreeLabel.setText(this.student.getDegree());
        currSemesterLabel.setText(this.student.getCurrSemester());
        motherNameLabel.setText(this.student.getMotherName());
        guardianNameLabel.setText(this.student.getGuardianName());
        contactNoLabel.setText(this.student.getContactNo());
        emailLabel.setText(this.student.getEmail());
        guardianContactNoLabel.setText(this.student.getGuardianContactNo());
        addressLabel.setText(this.student.getAddress());
    }

    /**
     * This method is used to send the status to the controller calling it whether a particular student is deleted or
     * not.
     *
     * @return The deletion status in boolean type.
     */
    public boolean getStudentDeletedStatus() {

        return studentDeletedStatus;
    }
}
