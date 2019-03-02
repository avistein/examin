package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import model.ExamCellMember;
import model.User;
import service.ExamCellMemberService;
import service.UserService;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for Admin.fxml.
 * Loads the individual fxml upon clicking the buttons on left side.
 * @author Avik Sarkar
 */
public class AdminController{

    private ExamCellMemberService examCellMemberService;

    private ExamCellMember admin;

    @FXML
    private Label userIdLabel;

    @FXML
    private Pane listPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label subTitleLabel;

    @FXML
    private void initialize(){
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
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentsListFxml);
    }

    /**
     * Callback method for handling professorListButtonAction.
     * Opens StudentsList.fxml upon clicking professorListButtonAction.
     */
    @FXML
    private void handleProfessorListButtonAction() throws IOException{

        Parent studentsListFxml = FXMLLoader.load(getClass()
                .getResource("/view/ProfessorsList.fxml"));
        subTitleLabel.setText("Professor");
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentsListFxml);
    }

    /**
     * Callback method for handling examCellMemberListButton.
     * Opens StudentsList.fxml upon clicking examCellMemberListButton.
     */
    @FXML
    private void handleExamCellMemberListButtonAction() throws IOException{

        Parent studentsListFxml = FXMLLoader.load(getClass()
                .getResource("/view/ExamCellMembersList.fxml"));
        subTitleLabel.setText("Exam Cell Member");
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentsListFxml);
    }

    @FXML
    private void handleAcademicAdministrationButtonAction() throws IOException{
        Parent studentsListFxml = FXMLLoader.load(getClass()
                .getResource("/view/AcademicAdministration.fxml"));
        subTitleLabel.setText("Academic Administration");
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentsListFxml);
    }

    @FXML
    private void handleExamsListButtonAction(){

    }

    @FXML
    private void handleMarksListButtonAction(){

    }

    @FXML
    private void handleNoticesListButtonAction(){

    }

    @FXML
    private void handleReportsListButtonAction(){

    }

    @FXML
    private void handleAdminSettingsButtonAction(){

    }

    void setAdminProfileDetails(String userId){

        String additionalQuery = "where v_emp_id=?";
        List<ExamCellMember> examCellMember = examCellMemberService
                .getExamCellMemberData(additionalQuery, userId);
        admin = examCellMember.get(0);
        userIdLabel.setText(userId);
        nameLabel.setText(admin.getFirstName() + " " + admin.getMiddleName()
                + " " + admin.getLastName());
    }

}

