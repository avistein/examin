package controller.adminPanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Professor;
import service.ProfessorService;

import java.util.ArrayList;
import java.util.List;


public class ChooseProfessorForInvigilationDutyModalController {

    @FXML
    private TableView<Professor> professorTableView;

    @FXML
    private TableColumn<Professor, Boolean> selectCol;

    @FXML
    private TableColumn<Professor, String> profIdCol;

    @FXML
    private TableColumn<Professor, String> firstNameCol;

    @FXML
    private TableColumn<Professor, String> middleNameCol;

    @FXML
    private TableColumn<Professor, String> lastNameCol;

    @FXML
    private TableColumn<Professor, String> deptCol;

    @FXML
    private Button submitButton;

    private Stage modalStage;
    private ProfessorService professorService;

    private ObservableList<Professor> profObsList;
    private List<Professor> invigilatorsList;


    @FXML
    private void initialize() {

        professorService = new ProfessorService();
        profObsList = FXCollections.observableArrayList();
        invigilatorsList = new ArrayList<>();
        initCols();
        populateTable();
    }

    @FXML
    private void handleSelectAllCheckBoxAction(ActionEvent event) {

        for(Professor professor : profObsList){

            professor.setSelected(((CheckBox)event.getSource()).isSelected());
        }
    }

    @FXML
    private void handleSubmitButtonAction() {

        for (Professor professor : profObsList) {

            if (professor.isSelected()) {

                invigilatorsList.add(professor);
            }
        }
        if (invigilatorsList.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No professor is selected!");
            alert.show();
        } else {

            modalStage = (Stage) submitButton.getScene().getWindow();
            modalStage.hide();
        }
    }

    @FXML
    private void handleCancelButtonAction() {

        modalStage = (Stage) submitButton.getScene().getWindow();
        modalStage.hide();
    }

    private void initCols() {

        profIdCol.setCellValueFactory(new PropertyValueFactory<>("profId"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        deptCol.setCellValueFactory(new PropertyValueFactory<>("deptName"));
        selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
    }


    private void populateTable() {

        String additionalQuery = "";

        Task<List<Professor>> professorTask = professorService.getProfessorTask(additionalQuery);

        new Thread(professorTask).start();

        professorTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //set items in the TableView
                profObsList.setAll(professorTask.getValue());
                professorTableView.setItems(profObsList);

            }
        });
    }

    public List<Professor> getProfessorsList() {

        return invigilatorsList;
    }
}
