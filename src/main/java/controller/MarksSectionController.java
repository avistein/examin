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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import service.CourseService;
import service.ExamService;
import service.MarksService;
import util.UISetterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MarksSectionController {

    @FXML
    private ChoiceBox<String> examTypeChoiceBox;

    @FXML
    private ComboBox<String> academicYearComboBox;

    @FXML
    private Label examIdLabel;

    @FXML
    private ComboBox<String> degreeComboBox;

    @FXML
    private ComboBox<String> disciplineComboBox;

    @FXML
    private ComboBox<String> subjectComboBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button addMarksButton;

    @FXML
    private Button importMarksButton;

    @FXML
    private TableView<Marks> marksListTableView;

    @FXML
    private TableColumn<Marks, String> regIdCol;

    @FXML
    private TableColumn<Marks, String> marksCol;

    private MarksService marksService;

    private ExamService examService;

    private CourseService courseService;

    private List<ExamDetails> examDetailsList;

    private List<Course> courseList;

    String courseId;
    String examId;
    List<Exam> examList;

    @FXML
    private void initialize(){

        marksService = new MarksService();
        examService = new ExamService();
        courseService = new CourseService();

        initCol();

        examTypeChoiceBox.setItems(FXCollections.observableArrayList("Theory", "Practical"));
    }

    @FXML
    private void handleExamTypeChoiceBox(){

        academicYearComboBox.getSelectionModel().clearSelection();
        academicYearComboBox.getItems().clear();

        examIdLabel.setText("");

        String additionalQuery = "WHERE v_exam_type=?";

        Task<List<ExamDetails>> examDetailsTask = examService.getExamDetailsTask(additionalQuery
                , examTypeChoiceBox.getValue());
        new Thread(examDetailsTask).start();

        examDetailsTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                examDetailsList = examDetailsTask.getValue();
                List<String> items = new ArrayList<>();
                for(ExamDetails examDetails : examDetailsList){

                    items.add(examDetails.getAcademicYear());
                }
                ObservableList<String> options = FXCollections.observableArrayList(items);
                academicYearComboBox.setItems(options);
            }
        });
    }

    @FXML
    private void handleAcademicYearComboBox(){

        degreeComboBox.getSelectionModel().clearSelection();
        degreeComboBox.getItems().clear();

        if(examDetailsList != null && academicYearComboBox.getValue() != null){

            for(ExamDetails examDetails : examDetailsList){

                if(examTypeChoiceBox.getValue().equals(examDetails.getExamType())
                        && academicYearComboBox.getValue().equals(examDetails.getAcademicYear())){

                    examIdLabel.setText(examDetails.getExamDetailsId());
                }
            }
            String additionalQuery = "NATURAL JOIN t_exam_time_table WHERE v_exam_details_id=?";
            Task<List<Course>> courseTask = courseService.getCoursesTask(additionalQuery, examIdLabel.getText());
            new Thread(courseTask).start();

            courseTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    courseList = courseTask.getValue();
                    List<String> items = new ArrayList<>();
                    for(Course course : courseList){

                        if(!items.contains(course.getDegree())){

                            items.add(course.getDegree());
                        }
                    }
                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    degreeComboBox.setItems(options);
                }
            });
        }
    }

    @FXML
    private void handleDegreeComboBox(){

        disciplineComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getItems().clear();

        if(degreeComboBox.getValue() != null) {

            List<String> items = new ArrayList<>();
            for (Course course : courseList) {

                if (course.getDegree().equals(degreeComboBox.getValue()))

                    //add the disciplines to the discipline comboBox for particular degree
                    if (!items.contains(course.getDiscipline()))
                        items.add(course.getDiscipline());
            }
            ObservableList<String> options = FXCollections.observableArrayList(items);
            disciplineComboBox.setItems(options);
        }
    }

    @FXML
    private void handleDisciplineComboBox(){

        if(courseList != null && disciplineComboBox.getValue() != null) {

            for(Course course : courseList){

                if(course.getDegree().equals(degreeComboBox.getValue())
                        && course.getDiscipline().equals(disciplineComboBox.getValue())){

                    courseId = course.getCourseId();
                }
            }
            String additionalQuery = "WHERE v_course_id=?";
            Task<List<Exam>> examRoutineTask = examService.getExamRoutineTask(additionalQuery, courseId);
            new Thread(examRoutineTask).start();

            examRoutineTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    examList = examRoutineTask.getValue();
                    List<String> items = new ArrayList<>();
                    for(Exam exam : examList){

                        items.add(exam.getSubId());
                    }
                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    subjectComboBox.setItems(options);
                }
            });
        }
    }

    @FXML
    private void handleSubjectComboBox(){

        populateMarksTable();
    }

    @FXML
    private void handleAddMarksButtonAction(){

        Marks marks = marksListTableView.getSelectionModel().getSelectedItem();

        if(marks != null && examId != null){

            //create a modal window
            Stage addMarksModal = new Stage();

            //get the main stage
            Stage parentStage = (Stage) addMarksButton.getScene().getWindow();

            //set the modal window
            FXMLLoader loader = UISetterUtil.setModalWindow("/view/AddMarksModal.fxml"
                    , addMarksModal, parentStage, "Import Marks");

            //get the controller
            AddMarksModalController addMarksModalController = loader.getController();


            marks.setExamId(examId);

            addMarksModalController.setMarksDetails(marks);

            addMarksModal.showAndWait();

            if(addMarksModalController.getTableUpdateStatus()){

                populateMarksTable();
            }
        }
    }

    @FXML
    private void handleImportMarksButtonAction(){

        if(isAllComboBoxesSelected() && examId != null){

            //create a modal window
            Stage importMarksCsvModal = new Stage();

            //get the main stage
            Stage parentStage = (Stage) importMarksButton.getScene().getWindow();

            //set the modal window
            FXMLLoader loader = UISetterUtil.setModalWindow("/view/ImportMarksCSVModal.fxml"
                    , importMarksCsvModal, parentStage, "Import Marks");

            //get the controller
            ImportMarksCsvModalController importMarksCsvModalController = loader.getController();
            importMarksCsvModalController.setExamId(examId);

            importMarksCsvModal.showAndWait();

            if(importMarksCsvModalController.getTableUpdateStatus()){

                populateMarksTable();
            }
        }
    }

    private boolean isAllComboBoxesSelected(){

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if(examTypeChoiceBox.getValue() == null){

            alert.setHeaderText("Please select the exam type!");
            alert.show();
            return false;
        }
        if(academicYearComboBox.getValue() == null){

            alert.setHeaderText("Please select the Academic Year!");
            alert.show();
            return false;
        }
        if(degreeComboBox.getValue() == null){

            alert.setHeaderText("Please select the degree!");
            alert.show();
            return false;
        }
        if(disciplineComboBox.getValue() == null){

            alert.setHeaderText("Please select the discipline!");
            alert.show();
            return false;
        }
        if(subjectComboBox.getValue() == null){

            alert.setHeaderText("Please select the Subject ID!");
            alert.show();
            return false;
        }
        return true;
    }
    private void initCol(){

        regIdCol.setCellValueFactory(new PropertyValueFactory<>("regId"));
        marksCol.setCellValueFactory(new PropertyValueFactory<>("obtainedMarks"));
    }

    private void populateMarksTable(){

        if(examList != null) {

            for (Exam exam : examList) {

                if (exam.getCourseId().equals(courseId) && exam.getSubId().equals(subjectComboBox.getValue())) {

                    examId = exam.getExamId();
                }
            }

            String additionalQuery = "WHERE int_exam_id=?";

            Task<List<Marks>> marksDataTask = marksService.getMarksDataTask(additionalQuery, examId);
            new Thread(marksDataTask).start();

            marksDataTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    ObservableList<Marks> marksObsList = FXCollections.observableArrayList(marksDataTask.getValue());

                    FilteredList<Marks> marksFilteredList = new FilteredList<>(marksObsList, null);

                    //FilteredList is unmodifiable, so wrap it in a SortedList for sorting functionality in the TableView
                    SortedList<Marks> marksSortedList = new SortedList<>(marksFilteredList);

                    /*
                    Attach a listener to the Search field to display only those Students in the TableView that matches
                    with the Search box data.
                    */
                    searchTextField.textProperty().addListener(new ChangeListener<String>() {

                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue
                                , String newValue) {

                            //set the predicate which will be used for filtering Student
                            marksFilteredList.setPredicate(new Predicate<>() {

                                @Override
                                public boolean test(Marks marks) {

                                    String lowerCaseFilter = newValue.toLowerCase();

                                    if (newValue.isEmpty()) {

                                        return true;
                                    } else if (marks.getRegId().contains(lowerCaseFilter)) {

                                        return true;
                                    } else if (marks.getObtainedMarks().contains(newValue)) {

                                        return true;
                                    }
                                    return false;
                                }
                            });
                        }
                    });

                    //set items in the TableView
                    marksListTableView.setItems(marksSortedList);

                    //attach the comparator ,so that sorting can be done
                    marksSortedList.comparatorProperty().bind(marksListTableView.comparatorProperty());
                }
            });
        }
    }
}
