package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;


public class AdminController{


    @FXML
    private Pane listPane;

    @FXML
    private void handleStudentListButtonAction(ActionEvent event) throws Exception{
       // studentListPane.setVisible(true);
        Parent studentsListFxml = FXMLLoader.load(getClass().getResource("/view/StudentsList.fxml"));
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentsListFxml);
    }

    @FXML
    private void handleProfessorListButtonAction(ActionEvent event) throws Exception{
        Parent studentsListFxml = FXMLLoader.load(getClass().getResource("/view/ProfessorsList.fxml"));
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentsListFxml);
    }

    @FXML
    private void handleExamCellMemberListButtonAction(ActionEvent event) throws Exception{
        Parent studentsListFxml = FXMLLoader.load(getClass().getResource("/view/ExamCellMembersList.fxml"));
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentsListFxml);
    }
}
