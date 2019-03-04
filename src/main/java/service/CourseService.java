package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Course;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class to
 *
 * @author Avik Sarkar
 */
public class CourseService {

    private DatabaseHelper databaseHelper;

    public CourseService(){
        databaseHelper = new DatabaseHelper();
    }

    @SuppressWarnings("Duplicates")
    public Task<List<Course>> getCoursesTask(String additionalQuery, final String ...params){

        final String query = "SELECT * from t_course " + additionalQuery;

        Task<List<Course>> courseTask = new Task<>() {
            @Override
            protected List<Course> call(){
                Map<String, List<String>> map = databaseHelper.execQuery(query, params);
                List<Course> list = new ArrayList<>();
                for(int i = 0; i < map.get("v_course_id").size(); i ++){

                    Course course = new Course();

                    course.setCourseId( map.get("v_course_id").get(i));
                    course.setDiscipline(map.get("v_discipline").get(i));
                    course.setDegree(map.get("v_degree").get(i));
                    course.setDuration(map.get("v_duration").get(i));
                    course.setDeptName( map.get("v_dept_name").get(i));

                    list.add(course);
                }
                return list;
            }
        };
        return courseTask;
    }

}
