package controller;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import model.ExamCellMember;
import service.ExamCellMemberService;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for Admin.fxml.
 * Loads the individual fxml upon clicking the buttons on left side.
 *
 * @author Avik Sarkar
 */
public class AdminController {

    /*---------------------Initialization and declaration of variables-----------------------*/

    private ExamCellMemberService examCellMemberService;

    private ExamCellMember admin;

    @FXML
    private Label userIdLabel;

    @FXML
    private StackPane contentStackPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label subTitleLabel;

    /*--------------------------End of Initialization-------------------------------------*/

    /**
     * This method is called once all the components in the fxml has been loaded successfully
     */
    @FXML
    private void initialize() {
        roleLabel.setText("Admin");
        examCellMemberService = new ExamCellMemberService();
    }

    /**
     * Callback method for handling studentListButton.
     * Opens StudentsList.fxml upon clicking studentListButton.
     */
    @FXML
    private void handleStudentListButtonAction() throws IOException {

        Parent studentsListFxml = FXMLLoader.load(getClass()
                .getResource("/view/StudentsList.fxml"));
        subTitleLabel.setText("Student");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(studentsListFxml);
    }

    /**
     * Callback method for handling professorListButtonAction.
     * Opens StudentsList.fxml upon clicking professorListButtonAction.
     */
    @FXML
    private void handleProfessorListButtonAction() throws IOException {

        Parent studentsListFxml = FXMLLoader.load(getClass()
                .getResource("/view/ProfessorsList.fxml"));
        subTitleLabel.setText("Professor");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(studentsListFxml);
    }

    /**
     * Callback method for handling examCellMemberListButton.
     * Opens StudentsList.fxml upon clicking examCellMemberListButton.
     */
    @FXML
    private void handleExamCellMemberListButtonAction() throws IOException {

        Parent studentsListFxml = FXMLLoader.load(getClass()
                .getResource("/view/ExamCellMembersList.fxml"));
        subTitleLabel.setText("Exam Cell Member");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(studentsListFxml);
    }

    /**
     * Callback method for handling clicking event on Academic Administration Button
     *
     * @throws IOException Load exception on loading the fxml
     */
    @FXML
    private void handleAcademicAdministrationButtonAction() throws IOException {
        Parent studentsListFxml = FXMLLoader.load(getClass()
                .getResource("/view/AcademicAdministration.fxml"));
        subTitleLabel.setText("Academic Administration");
        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(studentsListFxml);
    }

    @FXML
    private void handleExamsListButtonAction() {

    }

    @FXML
    private void handleMarksListButtonAction() {

    }

    @FXML
    private void handleNoticesListButtonAction() {

    }

    @FXML
    private void handleReportsListButtonAction() {

    }

    @FXML
    private void handleAdminSettingsButtonAction() {

    }

    /**
     * This method sets the details of the admin in the admin panel
     *
     * @param userId The userId used to login to the system
     */
    void setAdminProfileDetails(String userId) {

        final String additionalQuery = "where v_emp_id=?";
        Task<List<ExamCellMember>> examCellMembersTask = examCellMemberService
                .getExamCellMembersTask(additionalQuery, userId);

        new Thread(examCellMembersTask).start();

        examCellMembersTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {
                admin = examCellMembersTask.getValue().get(0);
                userIdLabel.setText(userId);
                nameLabel.setText(admin.getFirstName() + " " + admin.getMiddleName()
                        + " " + admin.getLastName());
            }
        });

    }

}

