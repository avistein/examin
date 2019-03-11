package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Course;
import model.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

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

    public Task<Integer>  getAddCourseToDatabaseTask(final Course course){
        Task<Integer> addCourseToDatabaseTask = new Task<>() {
            @Override
            protected Integer call() {

                final String sql = "INSERT INTO t_course(v_dept_name, v_course_id" +
                        ", v_degree, v_discipline, v_duration) VALUES(?, ?, ?, ?, ?)";

                int tCourseStatus = databaseHelper.insert(sql, course.getDeptName()
                        , course.getCourseId(), course.getDegree(), course.getDiscipline()
                        , course.getDuration());

                if(tCourseStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tCourseStatus == SUCCESS)
                    return SUCCESS;
                else
                    return DATA_ALREADY_EXIST_ERROR;

            }
        };
        return addCourseToDatabaseTask;
    }


    public Task<Integer>  getUpdateCourseTask(final Course course){
        Task<Integer> updateCourseTask = new Task<>() {
            @Override
            protected Integer call() {

                final String sql = "UPDATE t_course SET v_degree=?, v_discipline=?, v_duration=? " +
                        "WHERE v_course_id=?";

                int tCourseStatus = databaseHelper.updateDelete(sql, course.getDegree(), course.getDiscipline()
                        , course.getDuration(), course.getCourseId());

                if(tCourseStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tCourseStatus == SUCCESS)
                    return SUCCESS;
                else if(tCourseStatus == DATA_DEPENDENCY_ERROR)
                    return DATA_DEPENDENCY_ERROR;
                else
                    return DATA_INEXISTENT_ERROR;
            }
        };
        return updateCourseTask;
    }

    @SuppressWarnings("Duplicates")
    public Task<Integer>  getDeleteCourseTask(final String param){
        Task<Integer> deleteCourseTask = new Task<>() {
            @Override
            protected Integer call() {

                final String sql = "DELETE FROM t_course WHERE v_course_id=?";

                int tCourseStatus = databaseHelper.updateDelete(sql, param);

                if(tCourseStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tCourseStatus == SUCCESS)
                    return SUCCESS;
                else if(tCourseStatus == DATA_DEPENDENCY_ERROR)
                    return DATA_DEPENDENCY_ERROR;
                else
                    return DATA_INEXISTENT_ERROR;
            }
        };
        return deleteCourseTask;
    }

    public Task<Integer> getCoursesCountTask(){

        final String query = "SELECT v_course_id FROM t_course";

        Task<Integer> examCoursesCountTask = new Task<>() {
            @Override
            protected Integer call(){

                Map<String, List<String>> map = databaseHelper.execQuery(query);
                return map.get("v_course_id").size();
            }
        };
        return examCoursesCountTask;
    }









}
