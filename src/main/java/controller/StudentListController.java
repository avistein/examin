package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Batch;
import model.Course;
import model.Student;
import service.BatchService;
import service.CourseService;
import service.StudentService;
import util.SceneSetterUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class StudentListController {

    private StudentService studentService;

    private CourseService courseService;

    private BatchService batchService;

    private List<Batch> listOfBatches;

    private List<Student> listOfStudents;

    private List<Course> listOfCourses;

    private FilteredList<Student> studentFilteredItems;

    private ObservableList<Student> studentObsList;

    @FXML
    private GridPane studentListGridPane;

    @FXML
    private Label titleLabel;

    @FXML
    private Button addStudentButton;


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
    private TextField searchTextField;

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

    @FXML
    private TableColumn<Student, String> regYearCol;

    @SuppressWarnings("Duplicates")
    @FXML
    public void initialize() {
        studentService = new StudentService();
        courseService = new CourseService();
        batchService = new BatchService();
        studentObsList = FXCollections.observableArrayList();
        listOfCourses = courseService.getCourseData();
        initCol();
        if (!listOfCourses.isEmpty()) {
            List<String> items = new ArrayList<>();
            for (Course course : listOfCourses) {
                if (!items.contains(course.getDegree()))
                    items.add(course.getDegree());
            }
            items.add("all");
            ObservableList<String> options = FXCollections.observableArrayList(items);
            degreeComboBox.setItems(options);
        }
    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDegreeComboBox(ActionEvent event) {

        disciplineComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getItems().clear();

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        studentObsList.clear();

        if(degreeComboBox.getValue().equals("all")){
            disciplineComboBox.setDisable(true);
            batchNameComboBox.setDisable(true);
            semesterComboBox.setDisable(true);
            populateTable();
        }
        else if(degreeComboBox.getValue() != null){

            disciplineComboBox.setDisable(false);
            batchNameComboBox.setDisable(false);
            semesterComboBox.setDisable(false);

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
    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDisciplineComboBox(ActionEvent event) {

        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        studentObsList.clear();

        if (disciplineComboBox.getValue() != null) {
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
    private void handleBatchNameComboBox(ActionEvent event) {

        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();
        studentObsList.clear();

        if (batchNameComboBox.getValue() != null) {
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
    private void handleSemesterComboBox(ActionEvent event) {

        studentObsList.clear();

        if (semesterComboBox.getValue() != null) {
            //System.out.println(event.toString());
            titleLabel.setText("List of " + batchNameComboBox.getValue() + " " + degreeComboBox.getValue() +
                    " " + disciplineComboBox.getValue() + " Semester" + semesterComboBox.getValue() +
                    " students");
            populateTable();
        }

    }

    private void initCol() {

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
        regYearCol.setCellValueFactory(new PropertyValueFactory<>("regYear"));

    }

    private void populateTable() {
        String additionalQuery = "";
        if(degreeComboBox.getValue().equals("all")){
            listOfStudents = studentService.getStudentData(additionalQuery);
        }
        else {
            additionalQuery = "where v_degree=? and v_discipline=? " +
                    "and v_batch_name=? and v_curr_semester=?";
            listOfStudents = studentService.getStudentData(additionalQuery
                    , degreeComboBox.getValue(), disciplineComboBox.getValue()
                    , batchNameComboBox.getValue(), semesterComboBox.getValue());
        }
        studentObsList.setAll(listOfStudents);
        studentFilteredItems = new FilteredList<>(studentObsList, null);
        searchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                studentFilteredItems.setPredicate(new Predicate<Student>() {
                    @Override
                    public boolean test(Student student) {

                        if (newValue == null || newValue.isEmpty())
                            return true;
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (student.getFirstName().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getMiddleName().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getLastName().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getRollNo().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getRegId().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getDegree().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getDiscipline().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getCurrSemester().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getBatchName().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getRegYear().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getGuardianName().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        else if (student.getContactNo().toLowerCase().contains(lowerCaseFilter))
                            return true;
                        return false;
                    }
                });
            }
        });

        studentTable.setItems(studentFilteredItems);
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
        if (tableUpdateStatus)
            populateTable();
    }

    @FXML
    private void handleAddStudentButtonAction() throws IOException {
        Pane listPane = (Pane) studentListGridPane.getParent();
        Parent studentRegistrationFxml = FXMLLoader.load(getClass()
                .getResource("/view/StudentRegistration.fxml"));
        listPane.getChildren().removeAll();
        listPane.getChildren().setAll(studentRegistrationFxml);
    }
}
