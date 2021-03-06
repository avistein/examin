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
import java.nio.file.Paths;
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

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label profNameLabel;

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

    @FXML
    private Label academicRankLabel;

    /*---------------------------------------End of Initialization & Declaration -------------------------------------*/

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

    /**
     * This method is used to set the Professor object for editing or deleting it.
     * Also sets the text of the labels.
     *
     * @param professor The professor to be set with for deletion or editing.
     */
    void setProfessorPojo(Professor professor) {

        this.professor = professor;

        //if the Image exists in that location, then set the ImageView with that, otherwise set a placeholder
        if (Paths.get(professor.getProfileImagePath()).toFile().exists()) {

            profileImageView.setImage(new Image("file:" + professor.getProfileImagePath()));
        } else {

            profileImageView.setImage(new Image("/png/placeholder.png"));
        }

        profNameLabel.setText(this.professor.getFirstName() + "'s ");
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
        academicRankLabel.setText(this.professor.getAcademicRank());
    }
}
