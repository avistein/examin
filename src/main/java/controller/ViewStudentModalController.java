package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import model.Student;
import service.StudentService;

import java.util.Optional;

public class ViewStudentModalController {

    private Student student;

    private StudentService studentService;

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
        studentService = new StudentService();
    }

    @FXML
    private void handleEditButtonAction(){

    }

    @FXML
    private void handleDeleteButtonAction(){
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Delete");
//        alert.setHeaderText("Are you really want to delete?");
//        Optional<ButtonType> result = alert.showAndWait();
//        if(result.get() == ButtonType.OK){
//            studentService.deleteStudent(student);
//        }
    }

    void setStudentPojo(Student student){
        this.student = student;
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


}
