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
import service.*;
import util.UISetterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MarksSectionController {

    private String courseId;

    @FXML
    private ComboBox<String> degreeComboBox;
    @FXML
    private ComboBox<String> disciplineComboBox;
    @FXML
    private ComboBox<String> batchNameComboBox;
    @FXML
    private ComboBox<String> semesterComboBox;
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
    private SubjectService subjectService;
    private CourseService courseService;
    private BatchService batchService;
    private StudentService studentService;
    private List<Course> courseList;
    private Professor professor;
    private ObservableList<Marks> marksObsList;

    public void initController(Professor professor){

        this.professor = professor;

        marksService = new MarksService();
        courseService = new CourseService();
        subjectService = new SubjectService();
        batchService = new BatchService();
        studentService = new StudentService();

        marksObsList = FXCollections.observableArrayList();

        Task<List<Course>> coursesTask;

        if(professor != null){

            //get the list of courses available in the db
            coursesTask = courseService.getCoursesTask("WHERE v_course_id IN ('"
                    + professor.getSubjects().stream().map(Subject::getCourseId)
                    .collect(Collectors.joining("','")) + "')");
        }
       else {

            //get the list of courses available in the db
            coursesTask = courseService.getCoursesTask("");
        }
        new Thread(coursesTask).start();

        coursesTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                //store the list of all courses available in the DB
                courseList = coursesTask.getValue();

                //only if there's any course in the db

                List<String> items = new ArrayList<>();

                for (Course course : courseList) {

                    //add only unique degree items to degree combobox
                    if (!items.contains(course.getDegree())) {

                        items.add(course.getDegree());
                    }
                }

                ObservableList<String> options = FXCollections.observableArrayList(items);
                degreeComboBox.setItems(options);
            }
        });

        initCol();
    }

    @FXML
    private void handleDegreeComboBox() {

        disciplineComboBox.getSelectionModel().clearSelection();
        disciplineComboBox.getItems().clear();

        marksObsList.clear();

        if (degreeComboBox.getValue() != null) {

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


    /**
     * Callback method for disciplineComboBox.
     * Clears batchNameComboBox,semesterComboBox & table items and sets
     * the batchNameComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDisciplineComboBox() {

        //clears batch combo box and semester combo box upon selecting a discipline
        batchNameComboBox.getSelectionModel().clearSelection();
        batchNameComboBox.getItems().clear();

        marksObsList.clear();

        //only if a discipline is selected
        if (disciplineComboBox.getValue() != null) {

            final String additionalQuery = "where v_degree=? and v_discipline =?";

            //get all batches for particular degree and discipline
            Task<List<Batch>> batchesTask = batchService.getBatchesTask(additionalQuery, degreeComboBox.getValue()
                    , disciplineComboBox.getValue());
            new Thread(batchesTask).start();

            batchesTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    List<Batch> listOfBatches = batchesTask.getValue();

                    List<String> items = new ArrayList<>();

                    for (Batch batch : listOfBatches) {

                        //Add unique batch names to the batchComboBox for the corresponding degree and discipline
                        if (!items.contains(batch.getBatchName())) {

                            items.add(batch.getBatchName());
                        }
                    }
                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    batchNameComboBox.setItems(options);
                }
            });
        }
    }

    /**
     * Callback method for batchNameComboBox.
     * Clears semesterComboBox & table items and sets
     * the semesterComboBoxes.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleBatchNameComboBox() {

        //clears the semester combo box upon selecting a batch name
        semesterComboBox.getSelectionModel().clearSelection();
        semesterComboBox.getItems().clear();

        marksObsList.clear();

        //only if a batchName is selected
        if (batchNameComboBox.getValue() != null) {

            List<String> items = new ArrayList<>();
            int totalSemesters = 0;
                /*
                Get the total no.of semesters for the selected Degree,discipline &
                set the semesterComboBox with semester values from 1 to total no.
                 */
            for (Course course : courseList) {

                if (course.getDegree().equals(degreeComboBox.getValue())
                        && course.getDiscipline().equals(disciplineComboBox.getValue())) {

                    totalSemesters = Integer.parseInt(course.getDuration());
                }
            }
            //set the semester combo box from 1 to totalSemesters
            for (int i = 1; i <= totalSemesters; i++) {

                items.add(Integer.toString(i));
            }
            ObservableList<String> options = FXCollections.observableArrayList(items);
            semesterComboBox.setItems(options);
        }
    }

    /**
     * Callback method for semesterComboBox.
     * Clears table items and populate the tableView.
     * the semesterComboBoxes.
     */
    @FXML
    private void handleSemesterComboBox() {

        subjectComboBox.getSelectionModel().clearSelection();
        subjectComboBox.getItems().clear();

        marksObsList.clear();

        //only if a semester is selected , populate the table
        if (semesterComboBox.getValue() != null) {

            if (courseList != null && disciplineComboBox.getValue() != null) {

                for (Course course : courseList) {

                    if (course.getDegree().equals(degreeComboBox.getValue())
                            && course.getDiscipline().equals(disciplineComboBox.getValue())) {

                        courseId = course.getCourseId();
                    }
                }
                List<String> items = new ArrayList<>();

                if(professor != null){

                    for(Subject subject : professor.getSubjects()){

                        if(courseId.equals(subject.getCourseId())
                                && semesterComboBox.getValue().equals(subject.getSemester())){

                            items.add(subject.getSubId());
                        }
                    }
                    ObservableList<String> options = FXCollections.observableArrayList(items);
                    subjectComboBox.setItems(options);
                }
                else {

                    Task<List<Subject>> subjectDataTask = subjectService.getSubjectsTask("WHERE v_course_id=? AND int_semester=?"
                            , courseId, semesterComboBox.getValue());
                    new Thread(subjectDataTask).start();

                    subjectDataTask.setOnSucceeded(new EventHandler<>() {
                        @Override
                        public void handle(WorkerStateEvent event) {

                            List<Subject> subjectList = subjectDataTask.getValue();

                            for (Subject subject : subjectList) {

                                items.add(subject.getSubId());
                            }
                            ObservableList<String> options = FXCollections.observableArrayList(items);
                            subjectComboBox.setItems(options);
                        }
                    });
                }

            }
        }
    }

    @FXML
    private void handleSubjectComboBox() {

        marksObsList.clear();

        if(subjectComboBox.getValue() != null) {

               populateMarksTable();
        }
    }

    @FXML
    private void handleAddMarksButtonAction() {

        Marks marks = marksListTableView.getSelectionModel().getSelectedItem();

        if (marks != null) {

            //create a modal window
            Stage addMarksModal = new Stage();

            //get the main stage
            Stage parentStage = (Stage) addMarksButton.getScene().getWindow();

            //set the modal window
            FXMLLoader loader = UISetterUtil.setModalWindow("/view/AddMarksModal.fxml"
                    , addMarksModal, parentStage, "Import Marks");

            //get the controller
            AddMarksModalController addMarksModalController = loader.getController();

            addMarksModalController.setMarksDetails(marks);

            addMarksModal.showAndWait();

            if (addMarksModalController.getTableUpdateStatus()) {

                populateMarksTable();
            }
        }
    }

    @FXML
    private void handleImportMarksButtonAction() {

        if (isAllComboBoxesSelected()) {

            //create a modal window
            Stage importMarksCsvModal = new Stage();

            //get the main stage
            Stage parentStage = (Stage) importMarksButton.getScene().getWindow();

            //set the modal window
            FXMLLoader loader = UISetterUtil.setModalWindow("/view/ImportMarksCSVModal.fxml"
                    , importMarksCsvModal, parentStage, "Import Marks");

            //get the controller
            ImportMarksCsvModalController importMarksCsvModalController = loader.getController();
            importMarksCsvModalController.setSubjectDetails(courseId, subjectComboBox.getValue());

            importMarksCsvModal.showAndWait();

            if (importMarksCsvModalController.getTableUpdateStatus()) {

                populateMarksTable();
            }
        }
    }

    private boolean isAllComboBoxesSelected() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (degreeComboBox.getValue() == null) {

            alert.setHeaderText("Please select the degree!");
            alert.show();
            return false;
        }
        if (disciplineComboBox.getValue() == null) {

            alert.setHeaderText("Please select the discipline!");
            alert.show();
            return false;
        }
        if (batchNameComboBox.getValue() == null) {

            alert.setHeaderText("Please select the batch name!");
            alert.show();
            return false;
        }
        if (semesterComboBox.getValue() == null) {

            alert.setHeaderText("Please select the semester!");
            alert.show();
            return false;
        }
        if (subjectComboBox.getValue() == null) {

            alert.setHeaderText("Please select the Subject ID!");
            alert.show();
            return false;
        }
        return true;
    }

    private void initCol() {

        regIdCol.setCellValueFactory(new PropertyValueFactory<>("regId"));
        marksCol.setCellValueFactory(new PropertyValueFactory<>("obtainedMarks"));
    }

    private void populateMarksTable() {

        String additionalQuery = "WHERE v_degree=? AND v_discipline=? AND v_batch_name=?";

        Task<List<Student>> studentDataTask = studentService.getStudentTask(additionalQuery, degreeComboBox.getValue()
                , disciplineComboBox.getValue(), batchNameComboBox.getValue());
        new Thread(studentDataTask).start();

        studentDataTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                List<Student> studentList = studentDataTask.getValue();

                String additionalQuery = "WHERE v_reg_id IN (" + studentList.stream().map(Student::getRegId)
                        .collect(Collectors.joining(",")) + ") AND v_course_id=? AND v_sub_id=?";

                Task<List<Marks>> marksDataTask = marksService.getMarksDataTask(additionalQuery, courseId
                        , subjectComboBox.getValue());
                new Thread(marksDataTask).start();

                marksDataTask.setOnSucceeded(new EventHandler<>() {

                    @Override
                    public void handle(WorkerStateEvent event) {

                        marksObsList = FXCollections.observableArrayList(marksDataTask.getValue());

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
        });

    }
}
