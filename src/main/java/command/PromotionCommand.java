package command;

import javafx.concurrent.Task;
import model.Course;
import model.Marks;
import model.Student;
import service.CourseService;
import service.MarksService;
import service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static util.ConstantsUtil.*;

public class PromotionCommand {

    private CourseService courseService;
    private StudentService studentService;
    private MarksService marksService;

    public PromotionCommand() {

        courseService = new CourseService();
        studentService = new StudentService();
        marksService = new MarksService();
    }

    public Task<Integer> getPromoteStudentTask(List<Student> studentList) {

        Task<Integer> promoteStudentTask = new Task<>() {
            @Override
            protected Integer call() {

                List<Student> studentsToBePromoted = new ArrayList<>();
                List<Student> studentsToBeAwardedDegree = new ArrayList<>();
                for (Student student : studentList) {

                    boolean awardDegreeCompletion = true;
                    List<Course> course = courseService.getCourseData("WHERE v_degree=? AND " +
                            "v_discipline=?", student.getDegree(), student.getDiscipline());

                    String lastSemester = course.get(0).getDuration();

                    if (lastSemester.equals(student.getCurrSemester())) {

                        List<Marks> marksList = marksService.getMarksData("WHERE v_reg_id=?"
                                , student.getRegId());

                        for (Marks marks : marksList) {

                            if (marks.getObtainedMarks().isEmpty() || marks.getObtainedMarks().equals("ABSENT") || Integer.parseInt(marks.getObtainedMarks()) < 40) {

                                awardDegreeCompletion = false;
                                break;
                            }
                        }
                        if(awardDegreeCompletion){

                            studentsToBeAwardedDegree.add(student);
                        }
                    } else {

                        String newSemester = String.valueOf(Integer.parseInt(student.getCurrSemester()) + 1);

                        student.setCurrSemester(newSemester);

                        studentsToBePromoted.add(student);
                    }
                }
                int studentsPromotedStatus = 0;
                int studentsDegreeCompletionStatus = 0;

                if(!studentsToBePromoted.isEmpty()) {

                    studentsPromotedStatus = studentService.promoteStudentToNextSemester(studentsToBePromoted);
                }
                if(!studentsToBeAwardedDegree.isEmpty()) {


                    studentsDegreeCompletionStatus = studentService.awardDegreeCompletion(studentsToBeAwardedDegree);
                }
                if(studentsPromotedStatus == DATABASE_ERROR || studentsDegreeCompletionStatus == DATABASE_ERROR){

                    return DATABASE_ERROR;
                }

                else if(studentsPromotedStatus == studentsToBePromoted.size()
                        && studentsDegreeCompletionStatus == studentsToBeAwardedDegree.size()
                        && studentsToBePromoted.size() + studentsToBeAwardedDegree.size() == studentList.size()){

                    return SUCCESS;
                }
                else if(studentsPromotedStatus == studentsToBePromoted.size()
                        && studentsDegreeCompletionStatus == studentsToBeAwardedDegree.size()
                        && studentsToBePromoted.size() + studentsToBeAwardedDegree.size() != studentList.size()){

                    return PROMOTION_ERROR;
                }
                else{

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return promoteStudentTask;
    }
}
