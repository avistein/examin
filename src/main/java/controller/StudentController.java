package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Course;
import model.Student;
import service.CourseService;
import service.StudentService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentController {

    private StudentService studentService;

    private CourseService courseService;

    @FXML
    private Button addStudentButton;

    @FXML
    private Button printButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button importButton;

    @FXML
    private ComboBox<String> degreeComboBox;

    @FXML
    private ComboBox<String> disciplineComboBox;

    @FXML
    private ComboBox<Integer> semesterComboBox;

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
    public void initialize() {
        studentService = new StudentService();
        courseService = new CourseService();
        List<Course> listOfCourses = courseService.getCourseData("");
        if(!listOfCourses.isEmpty()) {
            List<String> items = new ArrayList<>();
            for (Course course: listOfCourses) {
                items.add(course.getDegree());
            }
            ObservableList<String> options = FXCollections.observableArrayList(items);
            degreeComboBox.setItems(options);
        }
    }


    @FXML
    private void  handleDegreeComboBox(){
        final String query = "where v_degree=?";
        List<Course> listOfCourses = courseService.getCourseData(query, degreeComboBox.getValue());
        if(!listOfCourses.isEmpty()) {
            List<String> items = new ArrayList<>();
            for (Course course : listOfCourses) {
                items.add(course.getDiscipline());
            }
            ObservableList<String> options = FXCollections.observableArrayList(items);
            disciplineComboBox.setItems(options);
        }
    }

    @FXML
    private void handleDisciplineComboBox(){

        final String query = "where v_degree=? and v_discipline=?";
        List<Course> listOfCourses = courseService.getCourseData(query, degreeComboBox.getValue(), disciplineComboBox.getValue());
        if(!listOfCourses.isEmpty()) {
            List<Integer> items = new ArrayList<>();
            int totalSemesters =Integer.parseInt(listOfCourses.get(0).getDuration());
            for (int i = 1; i <= totalSemesters; i++)
                items.add(i);
            ObservableList<Integer> options = FXCollections.observableArrayList(items);
            semesterComboBox.setItems(options);
        }
    }

    @FXML
    private void handleSemesterComboBox(){
        initCol();
        populateTable();
    }

    private void initCol(){
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        middleNameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        regIdCol.setCellValueFactory(new PropertyValueFactory<>("regId"));
        rollNoCol.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        guardianNameCol.setCellValueFactory(new PropertyValueFactory<>("guardianName"));
        contactNoCol.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
    }

    private void populateTable(){
        final String additionalQuery = "natural join t_course natural join t_batch_details" +
                " where v_degree=? and v_discipline=? and v_semester=? ";
        List<Student> list = studentService.getStudentData(additionalQuery, degreeComboBox.getValue(),
                disciplineComboBox.getValue(), semesterComboBox.getValue().toString());
        ObservableList<Student> items = FXCollections.observableArrayList(list);
        studentTable.setItems(items);
    }

    @FXML
    private void handleImportButtonAction() throws IOException {
        Stage importStudentListModalWindow = new Stage();
        importStudentListModalWindow.setTitle("Import Student List");
        importStudentListModalWindow.initModality(Modality.WINDOW_MODAL);
        Stage parentStage = (Stage) importButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ImportCSVModal.fxml"));
        Parent root = loader.load();
        ImportCSVModalController importCSVModalController = loader.getController();
        importCSVModalController.setTableName("t_student");
        importStudentListModalWindow.setScene(new Scene(root,500,500));
        importStudentListModalWindow.initOwner(parentStage);
        importStudentListModalWindow.show();
    }


}
