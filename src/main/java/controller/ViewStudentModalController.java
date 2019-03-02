package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Student;

public class ViewStudentModalController {


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
    private void initialize(){

    }

    @FXML
    private void handleEditButtonAction(){

    }

    @FXML
    private void handleDeleteButtonAction(){

    }

    void setStudentPojo(Student student){
        nameLabel.setText(student.getFirstName() + " " + student.getMiddleName()
                + " " + student.getLastName());
        genderLabel.setText(student.getGender());
        dobLabel.setText(student.getDob());
        regIdLabel.setText(student.getRegId());
        rollNoLabel.setText(student.getRollNo());
        regYearLabel.setText(student.getRegYear());
        batchNameLabel.setText(student.getBatchName());
        disciplineLabel.setText(student.getDiscipline());
        degreeLabel.setText(student.getDegree());
        currSemesterLabel.setText(student.getCurrSemester());
        motherNameLabel.setText(student.getMotherName());
        guardianNameLabel.setText(student.getGuardianName());
        contactNoLabel.setText(student.getContactNo());
        emailLabel.setText(student.getEmail());
        guardianContactNoLabel.setText(student.getGuardianContactNo());
        addressLabel.setText(student.getAddress());
    }


}
