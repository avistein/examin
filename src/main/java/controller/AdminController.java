package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class AdminController {

    @FXML
    private Pane studentListPane;

    @FXML
    private void handleStudentListButtonAction(ActionEvent event) throws Exception{
        Parent studentsListFxml = FXMLLoader.load(getClass().getResource("/view/StudentsList.fxml"));
        studentListPane.getChildren().removeAll();
        studentListPane.getChildren().setAll(studentsListFxml);
    }

    @FXML
    private void handleProfessorListButtonAction(ActionEvent event) throws Exception{
        Parent studentsListFxml = FXMLLoader.load(getClass().getResource("/view/ProfessorsList.fxml"));
        studentListPane.getChildren().removeAll();
        studentListPane.getChildren().setAll(studentsListFxml);
    }

    @FXML
    private void handleExamCellMemberListButtonAction(ActionEvent event) throws Exception{
        Parent studentsListFxml = FXMLLoader.load(getClass().getResource("/view/ExamCellMembersList.fxml"));
        studentListPane.getChildren().removeAll();
        studentListPane.getChildren().setAll(studentsListFxml);
    }
}
