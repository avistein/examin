package controller;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Professor;
import service.ProfessorService;

import java.io.IOException;
import java.util.Optional;

import static util.ConstantsUtil.*;

/**
 * Controller class for ViewProfessorModal.fxml.
 *
 * @author Sourav Debnath
 */

public class ViewProfessorModalController {

    /*------------------------------------Initialization & Declaration of variables-----------------------------------*/

    private Professor professor;

    private boolean professorDeletedStatus;

    private ProfessorService professorService;

    @FXML
    private Label nameLabel;

    @FXML
    private Label dobLabel;

    @FXML
    private Label profIdLabel;

    @FXML
    private Label deptLabel;

    @FXML
    private Label highestQualificationLabel;

    @FXML
    private Label dojLabel;

    @FXML
    private Label contactNoLabel;

    @FXML
    private Label emailIdLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private CheckBox hodCheckBox;

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

    /*---------------------------------------End of Initialization & Declaration -------------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    private void initialize() {
        professorService = new ProfessorService();

        //default status, no professor has been deleted
        professorDeletedStatus = false;
    }

    /**
     * Callback method to handle the Edit Button action.
     *
     * @throws IOException Load exception while loading the FXML document.
     */
    @SuppressWarnings("Duplicates")
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource
                ("/view/ProfessorRegistration.fxml"));
        Parent professorRegistrationFxml = loader.load();

        //get the controller of ProfessorRegistration.fxml
        ProfessorRegistrationController professorRegistrationController = loader.getController();

        //send the Student object to be edited
        professorRegistrationController.setProfessorPojo(professor);

        //send the edit signal
        professorRegistrationController.setEditSignal(EDIT_CHOICE);

        //close the modal window
        modalStage.hide();

        //place the ProfessorRegistration.fxml in the scene
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(professorRegistrationFxml);
    }

    @FXML
    private void handleDeleteButtonAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Are you really want to delete?");

        Optional<ButtonType> result = alert.showAndWait();

        //if OK is pressed in the Confirmation alert
        if (result.get() == ButtonType.OK) {

            mainGridPane.setOpacity(0.5);
            statusStackPane.setVisible(true);
            progressIndicator.setVisible(true);

            Task<Integer> deleteProfessorTask = professorService
                    .getDeleteProfessorTask(professor);
            new Thread(deleteProfessorTask).start();

            deleteProfessorTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    //get the status of the deletion operation
                    int status = deleteProfessorTask.getValue();

                    /*disable the loading spinner and display status*/
                    progressIndicator.setVisible(false);
                    statusImageView.setVisible(true);
                    statusLabel.setVisible(true);

                    if (status == DATABASE_ERROR) {
                        statusImageView.setImage(new Image("/png/critical error.png"));
                        statusLabel.setText("Database Error!");
                        professorDeletedStatus = false;
                    } else if (status == SUCCESS) {
                        statusImageView.setImage(new Image("/png/success.png"));
                        statusLabel.setText("Successfully Deleted!");
                        professorDeletedStatus = true;
                    } else if (status == DATA_DEPENDENCY_ERROR) {

                        statusImageView.setImage(new Image("/png/error.png"));
                        statusLabel.setText("Cannot delete professor!");
                        professorDeletedStatus = false;
                    } else {
                        statusImageView.setImage(new Image("/png/error.png"));
                        statusLabel.setText("Professor not found!");
                        professorDeletedStatus = false;
                    }
                }
            });
        }
    }

    /**
     * This method is used to set the Professor object for editing or deleting it.
     * Also sets the text of the labels.
     *
     * @param professor The professor to be set with for deletion or editing.
     */
    void setProfessorPojo(Professor professor) {

        this.professor = professor;

        nameLabel.setText(this.professor.getFirstName() + " " + this.professor.getMiddleName() + " " +
                this.professor.getLastName());
        dobLabel.setText(this.professor.getDob());
        profIdLabel.setText(this.professor.getProfId());
        highestQualificationLabel.setText(this.professor.getHighestQualification());
        dojLabel.setText(this.professor.getDoj());
        contactNoLabel.setText(this.professor.getContactNo());
        emailIdLabel.setText(this.professor.getEmail());
        addressLabel.setText(this.professor.getAddress());
        if (this.professor.getHodStatus().equals("HOD")) {

            hodCheckBox.setSelected(true);
        }
        deptLabel.setText(this.professor.getDeptName());
    }

    /**
     * This method is used to send the status to the controller calling it whether a particular professor is deleted or
     * not.
     *
     * @return The deletion status in boolean type.
     */
    public boolean getProfessorDeletedStatus() {

        return professorDeletedStatus;
    }
}
