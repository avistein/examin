package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Department;
import model.Professor;
import service.DepartmentService;
import service.ProfessorService;
import util.UISetterUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Controller class for ProfessorSection.fxml
 *
 * @author Sourav Debnath
 */
public class ProfessorListController {

    /*------------------------------Declaration and initialization of variables------------------------------*/

    private ProfessorService professorService;

    private DepartmentService departmentService;

    private List<Professor> listOfProfessors;

    private List<Department> listOfDepartment;

    private FilteredList<Professor> professorFilteredItems;

    private ObservableList<Professor> professorObsList;

    @FXML
    private TabPane professorSectionTabPane;

    @FXML
    private Label titleLabel;

    @FXML
    private Button importButton;

    @FXML
    private ComboBox<String> profDeptComboBox;

    @FXML
    private TableView<Professor> professorTableView;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableColumn<Professor, String> profIdCol;

    @FXML
    private TableColumn<Professor, String> profNameCol;

    @FXML
    private TableColumn<Professor, String> academicRankCol;

    @FXML
    private TableColumn<Professor, String> deptCol;

    @FXML
    private TableColumn<Professor, String> hodStatusCol;

    @FXML
    private TableColumn<Professor, String> subCol;

    @FXML
    private TableColumn<Professor, String> emailCol;

    @FXML
    private TableColumn<Professor, String> contactNoCol;


    /*-----------------------------------------End of initialization-------------------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    public void initialize() {
        professorService = new ProfessorService();
        departmentService = new DepartmentService();

        //initialize columns of the professorTableView
        initCol();

        //initialize this for the professorTableView
        professorObsList = FXCollections.observableArrayList();

        //get the list of departments available in the db
        Task<List<Department>> deptTask = departmentService
                .getDepartmentsTask("");
        new Thread(deptTask).start();

        deptTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //store the list of all departments available in the DB
                listOfDepartment = deptTask.getValue();

                //only if there's any department in the db
                if (!listOfDepartment.isEmpty()) {

                    List<String> items = new ArrayList<>();

                    for (Department dept : listOfDepartment) {

                        //add only unique department items to deptCombobox
                        if (!items.contains(dept.getDeptName()))
                            items.add(dept.getDeptName());
                    }

                    //choosing this will display professors from all department
                    items.add("all");

                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    profDeptComboBox.setItems(options);
                }
            }
        });

    }


    /**
     * Callback method for deptComboBox.
     * Clears table items and populate the tableView.
     * the deptComboBoxes.
     */
    @FXML
    private void handleProfDeptComboBox() {

        //clear the tableView data first
        professorObsList.clear();

        //only if a department is selected , populate the table
        if (profDeptComboBox.getValue() != null) {

            titleLabel.setText("List of " + profDeptComboBox.getValue() + " " + "professors");
            populateTable();
        }

    }


    /**
     * This method initializes the columns of the Professor Table.
     */
    @SuppressWarnings("Duplicates")
    private void initCol() {

        profIdCol.setCellValueFactory(new PropertyValueFactory<>("profId"));
        profNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        academicRankCol.setCellValueFactory(new PropertyValueFactory<>("highestQualification"));
        deptCol.setCellValueFactory(new PropertyValueFactory<>("deptName"));
        hodStatusCol.setCellValueFactory(new PropertyValueFactory<>("hodStatus"));
        subCol.setCellValueFactory(new PropertyValueFactory<>(""));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        contactNoCol.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

    }


    /**
     * This method populates and updates the Student Table.
     */
    @SuppressWarnings("Duplicates")
    private void populateTable() {

        String additionalQuery = "";

        Task<List<Professor>> professorTask;

        /*
        If 'all' is chosen in the profDeptComboBox, then get all professors from the DB, otherwise get professors of the
        particular Department.
         */
        if(profDeptComboBox.getValue().equals("all")){
            professorTask = professorService.getProfessorTask(additionalQuery);
        }
        else{
            additionalQuery = "where v_dept_name=?";
            professorTask = professorService.getProfessorTask(additionalQuery, profDeptComboBox.getValue());
        }


        new Thread(professorTask).start();

        professorTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //get list of professors
                listOfProfessors = professorTask.getValue();
                professorObsList.setAll(listOfProfessors);

                /*
                Wrap the Observable List in a FilteredList, for the search feature; null means no predicate i.e. always
                true, so display all professors in the TableView at first.
                 */
                FilteredList<Professor> professorFilteredItems = new FilteredList<>(professorObsList, null);

                //FilteredList is unmodifiable, so wrap it in a SortedList for sorting functionality in the TableView
                SortedList<Professor> professorSortedList = new SortedList<>(professorFilteredItems);

                /*
                Attach a listener to the Search field to display only those Students in the TableView that matches with the
                Search box data.
                 */
                searchTextField.textProperty().addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                        //set the predicate which will be used for filtering Professor
                        professorFilteredItems.setPredicate(new Predicate<>() {

                            @Override
                            public boolean test(Professor professor) {

                                String lowerCaseFilter = newValue.toLowerCase();

                                if (newValue.isEmpty())
                                    return true;

                                //String lowerCaseFilter = newValue.toLowerCase();

                                else if (professor.getFirstName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getMiddleName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getLastName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getProfId().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getDeptName().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getContactNo().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getHighestQualification().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if (professor.getEmail().toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                else if ((Integer.toString(professor.getHodStatus())).toLowerCase().contains(lowerCaseFilter))
                                    return true;
                                return false;
                            }
                        });
                    }
                });

                //set items in the TableView
                professorTableView.setItems(professorFilteredItems);

                //attach the comparator ,so that sorting can be done
                professorSortedList.comparatorProperty().bind(professorTableView.comparatorProperty());
            }
        });

    }


    /**
     * Callback method for ImportButton to import the professors from the Csv file.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleImportButtonAction() throws IOException {

        //create a new stage first
        Stage importProfessorListModalWindow = new Stage();

        //get the stage where the Import Button is situated
        Stage parentStage = (Stage) importButton.getScene().getWindow();

        FXMLLoader loader = UISetterUtil.setModalWindow("/view/ImportProfessorCSVModal.fxml"
                , importProfessorListModalWindow, parentStage, "Import Professor List");

        //get the controller
        ImportProfessorCSVModalController importProfessorCSVModalController = loader.getController();

        /*
        showAndWait() since we need to get the status for updating the table from the Import Professor controller.
        The application thread is blocked here and goes to execute the Import modal operations and only when the Import
        modal window is closed , the tableUpdateStatus is sent from the Import modal controller.
         */
        importProfessorListModalWindow.showAndWait();

        //get the tableUpdateStatus
        boolean tableUpdateStatus = importProfessorCSVModalController.getTableUpdateStatus();

        /*
        If any Professor is imported from the CSV to DB and if a department is selected in the ComboBox then update the Table.
         */
        if (tableUpdateStatus && profDeptComboBox.getValue() != null) {

            //if 'all' is selected then update the table
            if (profDeptComboBox.getValue().equals("all")) {

                populateTable();
            }

            //if all other combo boxes has some selection ,then only update the table
            else if (profDeptComboBox.getValue() != null) {

                populateTable();
            }
        }



        //importProfessorListModalWindow.setResizable(false);


        //importProfessorListModalWindow.setTitle("Import Professor List");
        //importProfessorListModalWindow.initModality(Modality.WINDOW_MODAL);


        //Parent root = loader.load();

        //importProfessorListModalWindow.setScene(new Scene(root));
        //importProfessorListModalWindow.initOwner(parentStage);



    }


    /**
     * Callback method to handle View Student button action.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleViewProfessorButtonAction() {

        //get the selected professor in the TableView
        Professor professor = professorTableView.getSelectionModel().getSelectedItem();

        //only if a Professor is selected in the TableView.
        if (professor != null) {

            //create a modal window
            Stage viewProfessorModalWindow = new Stage();

            //get the main stage
            Stage parentStage = (Stage) importButton.getScene().getWindow();

            //set the modal window
            FXMLLoader loader = UISetterUtil.setModalWindow("/view/ViewProfessorModal.fxml"
                    , viewProfessorModalWindow, parentStage, "Professor");

            //get the controller
            ViewProfessorModalController viewProfessorModalController = loader.getController();

            //send the Professor to the controller
            viewProfessorModalController.setProfessorPojo(professor);

            /*
            showAndWait() ensures that the data professorDeletedStatus is fetched after the modal window is closed.
            This method blocks the UI thread here.
             */
            viewProfessorModalWindow.showAndWait();

            //if a professor is deleted in the DB, remove the professor from the TableView
            if (viewProfessorModalController.getProfessorDeletedStatus()) {

                professorObsList.remove(professor);
            }
        }
    }


    /**
     * Callback method to handle ADD PROFESSOR Button action.
     *
     * @throws IOException Error while loading the FXML file.
     */
    @FXML
    private void handleAddProfButtonAction() throws IOException {

        //get the stackPane first in which the content is loaded
        StackPane contentStackPane = (StackPane) professorSectionTabPane.getParent();

        Parent professorRegistrationFxml = FXMLLoader.load(getClass()
                .getResource("/view/ProfessorRegistration.fxml"));

        contentStackPane.getChildren().removeAll();
        contentStackPane.getChildren().setAll(professorRegistrationFxml);
    }



   /* @FXML
    private void handleMouseClickOnTableViewItem() throws IOException{
        Professor professor = professorTableView.getSelectionModel().getSelectedItem();

        if(professor != null) {

            Stage viewProfessorModalWindow = new Stage();
            viewProfessorModalWindow.setTitle("Professor");
            viewProfessorModalWindow.initModality(Modality.WINDOW_MODAL);
            Stage parentStage = (Stage) importButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/view/ViewProfessorModal.fxml"));
            Parent root = loader.load();
            ViewProfessorModalController viewProfessorModalController = loader.getController();
            viewProfessorModalWindow.setScene(new Scene(root));
            viewProfessorModalWindow.initOwner(parentStage);
            viewProfessorModalController.setProfessorPojo(professor);
            viewProfessorModalWindow.showAndWait();
            if(viewProfessorModalController.getProfessorDeletedStatus())
                professorObsList.remove(professor);
        }
    }
*/

}
