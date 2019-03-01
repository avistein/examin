package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import java.io.IOException;

/**
 * Controller class for Admin.fxml.
 * Loads the individual fxml upon clicking the buttons on left side.
 * @author Avik Sarkar
 */
public class AdminController{

    @FXML
    private Pane listPane;

    /**
     * Callback method for handling studentListButton.
     * Opens StudentsList.fxml upon clicking studentListButton.
     */
    @FXML
    private void handleStudentListButtonAction() throws IOException {

        Parent studentsListFxml = FXMLLoader.load(getClass().getResource("/view/StudentsList.fxml"));
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentsListFxml);
    }

    /**
     * Callback method for handling professorListButtonAction.
     * Opens StudentsList.fxml upon clicking professorListButtonAction.
     */
    @FXML
    private void handleProfessorListButtonAction() throws IOException{

        Parent studentsListFxml = FXMLLoader.load(getClass().getResource("/view/ProfessorsList.fxml"));
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentsListFxml);
    }

    /**
     * Callback method for handling examCellMemberListButton.
     * Opens StudentsList.fxml upon clicking examCellMemberListButton.
     */
    @FXML
    private void handleExamCellMemberListButtonAction() throws IOException{

        Parent studentsListFxml = FXMLLoader.load(getClass().getResource("/view/ExamCellMembersList.fxml"));
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentsListFxml);
    }
}
