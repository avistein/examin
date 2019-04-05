package controller.adminPanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    private List<Professor> listOfProfessors;


    @FXML
    private void initialize() {

        professorService = new ProfessorService();
        professorTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listOfProfessors = new ArrayList<>();
        initCols();
        populateTable();
    }

    @FXML
    private void handleSelectAllButtonAction() {

        professorTableView.getSelectionModel().selectAll();
    }

    @FXML
    private void handleDeselectAllButtonAction(){

        professorTableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSubmitButtonAction() {

        listOfProfessors = professorTableView.getSelectionModel().getSelectedItems();
        if (listOfProfessors.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No professor is selected!");
            alert.show();
        } else {

            modalStage = (Stage)submitButton.getScene().getWindow();
            modalStage.hide();
        }
    }

    @FXML
    private void handleCancelButtonAction() {

        modalStage = (Stage)submitButton.getScene().getWindow();
        modalStage.hide();
    }

    private void initCols() {

        profIdCol.setCellValueFactory(new PropertyValueFactory<>("profId"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        deptCol.setCellValueFactory(new PropertyValueFactory<>("deptName"));
    }


    private void populateTable() {

        String additionalQuery = "";

        Task<List<Professor>> professorTask = professorService.getProfessorTask(additionalQuery);

        new Thread(professorTask).start();

        professorTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //set items in the TableView
                professorTableView.setItems(FXCollections.observableArrayList(professorTask.getValue()));

            }
        });
    }

    public List<Professor> getProfessorsList(){

        return listOfProfessors;
    }
}
