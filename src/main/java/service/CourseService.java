package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Service class to get course, add course, edit course, delete course, get total no of courses.
 *
 * @author Avik Sarkar
 */
public class CourseService {

    /*--------------------------------Declaration of variables---------------------------------------*/

    private DatabaseHelper databaseHelper;

    /*------------------------------------End of Declaration-----------------------------------------*/

    /**
     * Default public constructor to initialize database helper.
     */
    public CourseService() {

        databaseHelper = new DatabaseHelper();
    }

    /**
     * This method is used to get a single CoursesTask object which is used to get courses details in a separate thread.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_course.
     * @return A coursesTask which can be used to get a list of Course details from the DB in a separate thread.
     */
    public Task<List<Course>> getCoursesTask(String additionalQuery, final String... params) {

        Task<List<Course>> courseTask = new Task<>() {

            @Override
            protected List<Course> call() {

                List<Course> list = getCourseData(additionalQuery, params);

                //a list of Course details
                return list;
            }
        };
        return courseTask;
    }

    /**
     * This method is used to get Course details in the same thread it is called.
     *
     * @param additionalQuery Includes WHERE clause or any other specific query details.
     * @param params          Parameters for the PreparedStatement.
     * @return A list of Course details retrieved from the DB.
     */
    @SuppressWarnings("Duplicates")
    public List<Course> getCourseData(String additionalQuery, String... params) {

        final String query = "SELECT * from t_course " + additionalQuery;

        Map<String, List<String>> map = databaseHelper.execQuery(query, params);

        //each item in the list is a single Course details
        List<Course> list = new ArrayList<>();

        /*
        v_course_id is the primary key, total items in the map will always be equal to no of
        v_course_id retrieved
        */
        for (int i = 0; i < map.get("v_course_id").size(); i++) {

            Course course = new Course();

            course.setCourseId(map.get("v_course_id").get(i));
            course.setDiscipline(map.get("v_discipline").get(i));
            course.setDegree(map.get("v_degree").get(i));
            course.setDuration(map.get("int_duration").get(i));
            course.setDeptName(map.get("v_dept_name").get(i));

            //a single course details is added to the list
            list.add(course);
        }

        //a list of Course details
        return list;
    }

    /**
     * This method is used to get a addCourseToDatabaseTask which is used to add a single course to the DB.
     *
     * @param course The course to be added to the DB.
     * @return A addCourseToDatabaseTask instance which is used to add a single course to DB in a separate thread.
     */
    public Task<Integer> getAddCourseToDatabaseTask(final Course course) {

        Task<Integer> addCourseToDatabaseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "INSERT INTO t_course(v_dept_name, v_course_id" +
                        ", v_degree, v_discipline, int_duration) VALUES(?, ?, ?, ?, ?)";

                //holds the status of insertion of course to the DB, i.e success or failure
                int tCourseStatus = databaseHelper.insert(sql, course.getDeptName()
                        , course.getCourseId(), course.getDegree(), course.getDiscipline()
                        , course.getDuration());

                /*returns an integer holding the success or failure status*/
                if (tCourseStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tCourseStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_ALREADY_EXIST_ERROR;
                }

            }
        };
        return addCourseToDatabaseTask;
    }

    /**
     * This method is used to get a updateCourseTask which is used to edit a single course in the DB.
     *
     * @param course The course to be edited.
     * @return A updateCourseTask instance which is used to edit a single course in the DB in a separate thread.
     */
    public Task<Integer> getUpdateCourseTask(final Course course) {

        Task<Integer> updateCourseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "UPDATE t_course SET v_degree=?, v_discipline=?, int_duration=? " +
                        "WHERE v_course_id=?";

                //holds the status of updation of course in the DB, i.e success or failure
                int tCourseStatus = databaseHelper.updateDelete(sql, course.getDegree(), course.getDiscipline()
                        , course.getDuration(), course.getCourseId());


                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tCourseStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tCourseStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return updateCourseTask;
    }

    /**
     * This method is used to get a deleteCourseTask which is used to delete a single course in the DB.
     *
     * @param param Course id of the course to be deleted.
     * @return A deleteCourseTask instance which is used to delete a single course in the DB in a separate thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getDeleteCourseTask(final String param) {

        Task<Integer> deleteCourseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "DELETE FROM t_course WHERE v_course_id=?";

                //holds the status of deletion of course in the DB, i.e success or failure
                int tCourseStatus = databaseHelper.updateDelete(sql, param);

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tCourseStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tCourseStatus == SUCCESS) {

                    return SUCCESS;
                } else if (tCourseStatus == DATA_DEPENDENCY_ERROR) {

                    return DATA_DEPENDENCY_ERROR;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return deleteCourseTask;
    }


    /**
     * This method is used to get a single coursesCountTask object which is used to get total no of Courses in the DB.
     *
     * @return A coursesCountTask object which is used to get the total no. of Courses in the DB in a separate thread.
     */
    public Task<Integer> getCoursesCountTask(String additionalQuery, String... params) {

        final String query = "SELECT v_course_id FROM t_course " + additionalQuery;

        Task<Integer> examCoursesCountTask = new Task<>() {

            @Override
            protected Integer call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                /*
                v_course_id is the primary key, so total count will always be equal to the no of v_course_id
                retrieved
                 */
                return map.get("v_course_id").size();
            }
        };
        return examCoursesCountTask;
    }
}
