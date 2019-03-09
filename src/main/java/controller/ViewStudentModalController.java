package controller;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.Student;
import service.StudentService;
import static util.ConstantsUtil.*;
import java.util.Optional;

public class ViewStudentModalController {

    private Student student;

    private boolean studentDeletedStatus;

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
    private void initialize(){
        studentService = new StudentService();
        studentDeletedStatus = false;
    }

    @FXML
    private void handleEditButtonAction(){

    }

    @FXML
    private void handleDeleteButtonAction(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Are you really want to delete?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            mainGridPane.setOpacity(0.5);
            statusStackPane.setVisible(true);
            progressIndicator.setVisible(true);
            Task<Integer> deleteStudentTask = studentService
                    .getDeleteStudentTask(student);
            new Thread(deleteStudentTask).start();
            deleteStudentTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    int status = deleteStudentTask.getValue();
                    progressIndicator.setVisible(false);
                    statusImageView.setVisible(true);
                    statusLabel.setVisible(true);
                    if(status == DATABASE_ERROR){
                        statusImageView.setImage(new Image("/png/critical error.png"));
                        statusLabel.setText("Database Error!");
                        studentDeletedStatus = false;
                    }
                    else if(status == SUCCESS){
                        statusImageView.setImage(new Image("/png/success.png"));
                        statusLabel.setText("Successfully Deleted!");
                        studentDeletedStatus = true;
                    }
                    else if(status == DATA_DEPENDENCY_ERROR){
                        statusImageView.setImage(new Image("/png/error.png"));
                        statusLabel.setText("Cannot delete student!");
                        studentDeletedStatus = false;
                    }
                    else {
                        statusImageView.setImage(new Image("/png/error.png"));
                        statusLabel.setText("Student not found!");
                        studentDeletedStatus = false;
                    }
                }
            });
        }
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

    boolean getStudentDeletedStatus(){
        return studentDeletedStatus;
    }

}
