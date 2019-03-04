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

                    String discipline = map.get("v_discipline").get(i);
                    String degree = map.get("v_degree").get(i);
                    String courseId = map.get("v_course_id").get(i);
                    String duration = map.get("v_duration").get(i);
                    String deptName = map.get("v_dept_name").get(i);

                    list.add(new Course(discipline, degree, courseId, duration, deptName));
                }
                return list;
            }
        };
        return courseTask;
    }

}
