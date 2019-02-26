package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Batch;
import model.Course;
import model.Student;
import service.BatchService;
import service.CourseService;
import service.StudentService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentListController {

    private StudentService studentService;

    private CourseService courseService;

    private BatchService batchService;

    private List<Batch> listOfBatches;

    private List<Student> listOfStudents;

    private List<Course> listOfCourses;

    @FXML
    private Label titleLabel ;

    @FXML
    private Button addStudentButton;

    @FXML
    private Button printButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button importButton;

    @FXML
    private ComboBox<String> batchNameComboBox;

    @FXML
    private ComboBox<String> degreeComboBox;

    @FXML
    private ComboBox<String> disciplineComboBox;

    @FXML
    private ComboBox<String> semesterComboBox;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, String> regIdCol;

    @FXML
    private TableColumn<Student, String> rollNoCol;

    @FXML
    private TableColumn<Student, String> firstNameCol;

    @FXML
    private TableColumn<Student, String> middleNameCol;

    @FXML
    private TableColumn<Student, String> lastNameCol;

    @FXML
    private TableColumn<Student, String> guardianNameCol;

    @FXML
    private TableColumn<Student, String> contactNoCol;

    @FXML
    private TableColumn<Student, String> degreeCol;

    @FXML
    private TableColumn<Student, String> disciplineCol;

    @FXML
    private TableColumn<Student, String> semesterCol;

    @FXML
    private TableColumn<Student, String> batchCol;

    @SuppressWarnings("Duplicates")
    @FXML
    public void initialize() {
        studentService = new StudentService();
        courseService = new CourseService();
        batchService = new BatchService();
        listOfCourses = courseService.getCourseData();

        if(!listOfCourses.isEmpty()) {
            List<String> items = new ArrayList<>();
            for (Course course: listOfCourses) {
                if(!items.contains(course.getDegree()))
                    items.add(course.getDegree());
            }
            ObservableList<String> options = FXCollections.observableArrayList(items);
            degreeComboBox.setItems(options);
        }
    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void  handleDegreeComboBox(ActionEvent event){

        disciplineComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getItems().clear();

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        studentTable.getItems().clear();

       // System.out.println(event.toString());
        if (!listOfCourses.isEmpty()) {
            List<String> items = new ArrayList<>();
            for (Course course : listOfCourses) {
                if (course.getDegree().equals(degreeComboBox.getValue()))
                    if (!items.contains(course.getDiscipline()))
                        items.add(course.getDiscipline());
            }
            ObservableList<String> options = FXCollections.observableArrayList(items);
            disciplineComboBox.setItems(options);

        }
    }

    @FXML
    private void handleDisciplineComboBox(ActionEvent event){

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();
        studentTable.getItems().clear();

        if(disciplineComboBox.getValue() != null) {
            //System.out.println(event.toString());
            String additionalQuery = "where v_degree=? and v_discipline =?";
            listOfBatches = batchService.getBatchData(additionalQuery, degreeComboBox.getValue()
                    , disciplineComboBox.getValue());

            if (!listOfBatches.isEmpty()) {
                List<String> items = new ArrayList<>();
                for (Batch batch : listOfBatches) {
                    if (!items.contains(batch.getBatchName()))
                        items.add(batch.getBatchName());
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                batchNameComboBox.setItems(options);

            }
        }

    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleBatchNameComboBox(ActionEvent event){

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();
        studentTable.getItems().clear();

        if(batchNameComboBox.getValue() != null) {
            //System.out.println(event.toString());
            if (!listOfCourses.isEmpty()) {

                List<String> items = new ArrayList<>();
                int totalSemesters = 0;
                for (Course course : listOfCourses) {
                    if (course.getDegree().equals(degreeComboBox.getValue())
                            && course.getDiscipline().equals(disciplineComboBox.getValue()))
                        totalSemesters = Integer.parseInt(course.getDuration());
                }
                for (int i = 1; i <= totalSemesters; i++)
                    items.add(Integer.toString(i));
                ObservableList<String> options = FXCollections.observableArrayList(items);
                semesterComboBox.setItems(options);

            }
        }
    }

    @FXML
    private void handleSemesterComboBox(ActionEvent event){

        studentTable.getItems().clear();

        if(semesterComboBox.getValue() != null) {
            //System.out.println(event.toString());
            initCol();
            populateTable();
        }

    }

    private void initCol(){

        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        regIdCol.setCellValueFactory(new PropertyValueFactory<>("regId"));
        rollNoCol.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        guardianNameCol.setCellValueFactory(new PropertyValueFactory<>("guardianName"));
        contactNoCol.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        degreeCol.setCellValueFactory(new PropertyValueFactory<>("degree"));
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("currSemester"));
        disciplineCol.setCellValueFactory(new PropertyValueFactory<>("discipline"));
        batchCol.setCellValueFactory(new PropertyValueFactory<>("batchName"));

    }

    private void populateTable(){

        String additionalQuery = "where v_degree=? and v_discipline=? " +
                "and v_batch_name=? and v_curr_semester=?";
        listOfStudents = studentService.getStudentData(additionalQuery
                , degreeComboBox.getValue(), disciplineComboBox.getValue()
                , batchNameComboBox.getValue(), semesterComboBox.getValue());

        ObservableList<Student> items = FXCollections.observableArrayList(listOfStudents);
        studentTable.setItems(items);
    }

    @FXML
    private void handleImportButtonAction() throws IOException {
        Stage importStudentListModalWindow = new Stage();
        importStudentListModalWindow.setTitle("Import Student List");
        importStudentListModalWindow.initModality(Modality.WINDOW_MODAL);
        Stage parentStage = (Stage) importButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ImportStudentCSVModal.fxml"));
        Parent root = loader.load();
        ImportStudentCSVModalController importStudentCSVModalController = loader.getController();
        importStudentListModalWindow.setScene(new Scene(root));
        importStudentListModalWindow.initOwner(parentStage);
        importStudentListModalWindow.showAndWait();
        boolean tableUpdateStatus = importStudentCSVModalController.getTableUpdateStatus();
        if(tableUpdateStatus)
            populateTable();
    }
}
