package service;

import database.DatabaseHelper;
import model.Course;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseService {

    private DatabaseHelper databaseHelper;

    public CourseService(){
        databaseHelper = new DatabaseHelper();
    }

    public List<Course> getCourseData(String additionalQuery, String... params){
        String query = "SELECT v_course_id, v_discipline,v_degree, v_duration," +
                "v_dept_name from t_course " + additionalQuery;
        databaseHelper.openConnection();
        List<Course> list = new ArrayList<>();
        Map<String, List<String>> map = databaseHelper.execQuery(query, params);
        for(int i = 0; i < map.get("v_course_id").size(); i ++){

            String discipline = map.get("v_discipline").get(i);
            String degree = map.get("v_degree").get(i);
            String courseId = map.get("v_course_id").get(i);
            String duration = map.get("v_duration").get(i);
            String deptName = map.get("v_dept_name").get(i);

            list.add(new Course(discipline, degree, courseId, duration, deptName));
        }
        databaseHelper.closeConnection();
        return list;
    }
}
