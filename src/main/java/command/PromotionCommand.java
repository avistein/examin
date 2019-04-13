package command;

import javafx.concurrent.Task;
import model.Course;
import model.Marks;
import model.Student;
import service.CourseService;
import service.MarksService;
import service.StudentService;
import static util.ConstantsUtil.PROMOTION_ERROR;
import java.util.List;

public class PromotionCommand {

    private CourseService courseService;
    private StudentService studentService;
    private MarksService marksService;

    public PromotionCommand() {

        courseService = new CourseService();
        studentService = new StudentService();
        marksService = new MarksService();
    }

    public Task<Integer> getPromoteStudentTask(Student student) {

        Task<Integer> promoteStudentTask = new Task<>() {
            @Override
            protected Integer call() {
                List<Course> course = courseService.getCourseData("WHERE v_degree=? AND " +
                        "v_discipline=?", student.getDegree(), student.getDiscipline());

                String lastSemester = course.get(0).getDuration();

                if (lastSemester.equals(student.getCurrSemester())) {

                    List<Marks> marksList = marksService.getMarksData("WHERE v_reg_id=?"
                            , student.getRegId());

                    for(Marks marks : marksList){

                        if(marks.getObtainedMarks().isEmpty() || Integer.parseInt(marks.getObtainedMarks()) < 40){

                            return PROMOTION_ERROR;
                        }
                    }
                    return studentService.awardDegreeCompletion(student);

                } else {

                    String newSemester = String.valueOf(Integer.parseInt(student.getCurrSemester()) + 1);

                    student.setCurrSemester(newSemester);

                    return studentService.promoteStudentToNextSemester(student);
                }
            }
        };
        return promoteStudentTask;
    }
}
