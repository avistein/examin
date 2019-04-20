package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import controller.professorPanel.ProfessorPanelController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import model.Holiday;
import model.InvigilationDuty;
import model.Professor;
import model.User;
import service.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

import static util.ConstantsUtil.*;

/**
 * Controller for ProfessorAndHodDashboard.fxml
 *
 * @author Sourav Debnath
 */
public class ProfessorAndHodDashboardController {

    /*-----------------------------------End of declaration and initialization----------------------------------*/

    @FXML
    private Label deptNameLabel;

    @FXML
    private Label totalStudentsLabel;

    @FXML
    private Label totalProfessorsLabel;

    @FXML
    private Label totalBatchesLabel;

    @FXML
    private Label totalCoursesLabel;

    @FXML
    private Label totalSubjectsLabel;

    @FXML
    private TableView<Holiday> holidaysTableView;

    @FXML
    private TableColumn<Holiday, String> holidayIdCol;

    @FXML
    private TableColumn<Holiday, String> holidayNameCol;

    @FXML
    private TableColumn<Holiday, String> startDateCol;

    @FXML
    private TableColumn<Holiday, String> endDateCol;

    @FXML
    private TableView<InvigilationDuty> invigilationTableView;

    @FXML
    private TableColumn<InvigilationDuty, String> invigilationDateCol;

    @FXML
    private TableColumn<InvigilationDuty, String> invigilationRoomNoCol;

    private HolidayService holidayService;

    private StudentService studentService;

    private BatchService batchService;

    private CourseService courseService;

    private SubjectService subjectService;

    private ProfessorService professorService;

    private ExamService examService;

    private ObservableList<Holiday> holidayObsList;

    private Professor professor;

    private String additionalQuery;


    /*-----------------------------------End of declaration and initialization----------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    public void initController(Professor professor) {

        this.professor = professor;

        holidayService = new HolidayService();
        studentService = new StudentService();
        subjectService = new SubjectService();
        batchService = new BatchService();
        courseService = new CourseService();
        professorService = new ProfessorService();
        examService = new ExamService();

        //for the tableView
        holidayObsList = FXCollections.observableArrayList();

        /*
        Initialize columns and populate HolidayList tableView.
         */
        initHolidayCols();
        populateHolidayTable();

        /*
        Initialize columns and populate Invigilation tableView
         */
        initInvigilationCols();
        populateInvigilationTable();

        /*
        Sets total count of item in the respective labels.
         */
        updateTotalBatchesCount();
        updateTotalCoursesCount();
        updateTotalStudentsCount();
        updateTotalProfessorsCount();
        updateTotalSubjectsCount();

        deptNameLabel.setText(professor.getDeptName());
    }

    /**
     * This method is used to count the total no. of Students in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalStudentsCount() {

        additionalQuery = "natural join t_student_enrollment_details natural join t_batch natural join t_course " +
                "where v_dept_name=?";

        Task<Integer> studentsCountTask = studentService.getStudentsCountTask(additionalQuery, professor.getDeptName());
        new Thread(studentsCountTask).start();

        studentsCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalStudents = studentsCountTask.getValue();
                totalStudentsLabel.setText(Integer.toString(totalStudents));
            }
        });
    }

    /**
     * This method is used to count the total no. of Batches in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalBatchesCount() {

        additionalQuery = "natural join t_course where v_dept_name=?";

        Task<Integer> batchesCountTask = batchService.getBatchesCountTask(additionalQuery, professor.getDeptName());
        new Thread(batchesCountTask).start();

        batchesCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalBatches = batchesCountTask.getValue();
                totalBatchesLabel.setText(Integer.toString(totalBatches));
            }
        });
    }

    /**
     * This method is used to count the total no. of Subjects in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalSubjectsCount() {

        additionalQuery = " natural join t_course where v_dept_name=?";

        Task<Integer> subjectsCountTask = subjectService.getSubjectsCountTask(additionalQuery, professor.getDeptName());
        new Thread(subjectsCountTask).start();

        subjectsCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalSubjects = subjectsCountTask.getValue();
                totalSubjectsLabel.setText(Integer.toString(totalSubjects));
            }
        });
    }

    /**
     * This method is used to count the total no. of Courses in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalCoursesCount() {

        additionalQuery = " where v_dept_name=?";

        Task<Integer> coursesCountTask = courseService.getCoursesCountTask(additionalQuery, professor.getDeptName());
        new Thread(coursesCountTask).start();

        coursesCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalCourses = coursesCountTask.getValue();
                totalCoursesLabel.setText(Integer.toString(totalCourses));
            }
        });
    }

    /**
     * This method is used to count the total no. of Courses in the DB by getting the appropriate task and update
     * the corresponding label in the UI.
     */
    @SuppressWarnings("Duplicates")
    private void updateTotalProfessorsCount() {

        additionalQuery = " natural join t_prof_dept where v_dept_name=?";

        Task<Integer> profsCountTask = professorService.getProfessorsCountTask(additionalQuery, professor.getDeptName());
        new Thread(profsCountTask).start();

        profsCountTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                int totalProfs = profsCountTask.getValue();
                totalProfessorsLabel.setText(Integer.toString(totalProfs));
            }
        });
    }

    /*-------------------------------------Holidays TableView operation---------------------------------------*/

    /**
     * Initializes the columns in the holiday table.
     */
    private void initHolidayCols() {

        holidayIdCol.setCellValueFactory(new PropertyValueFactory<>("holidayId"));
        holidayNameCol.setCellValueFactory(new PropertyValueFactory<>("holidayName"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }

    /**
     * Populates and updates the holiday table.
     */
    @SuppressWarnings("Duplicates")
    private void populateHolidayTable() {

        //get the task to get list of holidays from the DB
        Task<List<Holiday>> holidaysTask = holidayService.getHolidaysTask();
        new Thread(holidaysTask).start();

        holidaysTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                holidayObsList.setAll(holidaysTask.getValue());
                holidaysTableView.setItems(holidayObsList);
            }
        });
    }

    /*-------------------------------------End of Holidays TableView operation--------------------------------*/


    /*-----------------------------------Invigilation TableView operation------------------------------------*/

    /**
     * Initializes the columns in the invigilation table.
     */
    private void initInvigilationCols() {

        invigilationDateCol.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        invigilationRoomNoCol.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
    }

    /**
     * Populates and updates the invigilation table.
     */
    @SuppressWarnings("Duplicates")
    private void populateInvigilationTable() {

        LocalDate currDate = LocalDate.now();
        String additionalQuery = "WHERE v_prof_id=? AND d_exam_date >= ?";

        Task<List<InvigilationDuty>> invigilationDutyTask = examService.getInvigilationDutyDataTask(additionalQuery
                , professor.getProfId(), currDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        new Thread(invigilationDutyTask).start();

        invigilationDutyTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                invigilationTableView.setItems(FXCollections.observableArrayList(invigilationDutyTask.getValue()));
            }
        });
    }

    /*---------------------------------End of Invigilation TableView operation-------------------------------*/
}

