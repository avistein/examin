package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Classroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Service class to get classroom, add classroom, edit classroom, delete classroom, get total no of classroom.
 *
 * @author Sourav Debnath
 */

public class ClassRoomService {

    /*--------------------------------Declaration of variables---------------------------------------*/

    private DatabaseHelper databaseHelper;

    /*------------------------------------End of Declaration-----------------------------------------*/

    /**
     * Default public constructor to initialize database helper.
     */
    public ClassRoomService() {

        databaseHelper = new DatabaseHelper();
    }

    /**
     * This method is used to get a single ClassroomTask object which is used to get classroom details in a separate
     * thread.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_classroom.
     * @return A classroomTask which can be used to get a list of Classroom details from the DB in a separate thread.
     */
    public Task<List<Classroom>> getClassroomTask(String additionalQuery, final String... params) {

        Task<List<Classroom>> classroomTask = new Task<>() {

            @Override
            protected List<Classroom> call() {

                List<Classroom> list = getClassroomsData(additionalQuery, params);

                //a list of Classroom details
                return list;
            }
        };
        return classroomTask;
    }

    /**
     * This method is used to get a single ClassroomTask object which is used to get classroom details in the same
     * thread it was called.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_classroom.
     * @return A classroomTask which can be used to get a list of Classroom details from the DB in a separate thread.
     */
    public List<Classroom> getClassroomsData(String additionalQuery, final String... params) {

        final String query = "SELECT * from t_classroom " + additionalQuery;


        Map<String, List<String>> map = databaseHelper.execQuery(query, params);

        //each item in the list is a single Classroom details
        List<Classroom> list = new ArrayList<>();

                /*
                v_course_id and v_room_no is composite primary key, total items in the map will always be equal to no of
                v_room_no retrieved
                 */
        for (int i = 0; i < map.get("int_room_no").size(); i++) {

            Classroom classroom = new Classroom();

            classroom.setRoomNo(map.get("int_room_no").get(i));
            classroom.setCapacity(map.get("int_capacity").get(i));
            classroom.setNoOfCols(map.get("int_cols").get(i));
            classroom.setNoOfRows(map.get("int_rows").get(i));
            //a single classroom details is added to the list
            list.add(classroom);
        }

        //a list of Classroom details
        return list;
    }

    /**
     * This method is used to get a addClassroomToDatabaseTask which is used to add a single classroom to the DB.
     *
     * @param classroom The classroom to be added to the DB.
     * @return A addClassroomToDatabaseTask instance which is used to add a single classroom to DB in a separate thread.
     */
    public Task<Integer> getAddClassroomToDatabaseTask(final Classroom classroom) {

        Task<Integer> addClassroomToDatabaseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "INSERT INTO t_classroom(int_room_no, int_capacity" +
                        ", int_rows, int_cols) VALUES(?, ?, ?, ?)";

                //holds the status of insertion of classroom to the DB, i.e success or failure
                int tClassroomStatus = databaseHelper.insert(sql, classroom.getRoomNo(), classroom.getCapacity()
                        , classroom.getNoOfRows(), classroom.getNoOfCols());

                /*returns an integer holding the success or failure status*/
                if (tClassroomStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tClassroomStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_ALREADY_EXIST_ERROR;
                }

            }
        };
        return addClassroomToDatabaseTask;
    }

    /**
     * This method is used to get a updateClassroomTask which is used to edit a single classroom in the DB.
     *
     * @param classroom The classroom to be edited.
     * @return A updateClassroomTask instance which is used to edit a single classroom in the DB in a separate thread.
     */
    public Task<Integer> getUpdateClassroomTask(final Classroom classroom) {

        Task<Integer> updateClassroomTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "UPDATE t_classroom SET int_capacity=?, int_rows=?, int_cols=? WHERE int_room_no=?";

                //holds the status of updation of classroom in the DB, i.e success or failure
                int tClassroomStatus = databaseHelper.updateDelete(sql, classroom.getCapacity(), classroom.getNoOfRows()
                        , classroom.getNoOfCols(), classroom.getRoomNo());


                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tClassroomStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tClassroomStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return updateClassroomTask;
    }

    /**
     * This method is used to get a deleteClassroomTask which is used to delete a single classroom in the DB.
     *
     * @param param Classroom of the classroom to be deleted.
     * @return A deleteClassroomTask instance which is used to delete a single classroom in the DB in a separate thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getDeleteClassroomTask(final String param) {

        Task<Integer> deleteClassroomTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "DELETE FROM t_classroom WHERE int_room_no=?";

                //holds the status of deletion of classroom in the DB, i.e success or failure
                int tClassroomStatus = databaseHelper.updateDelete(sql, param);

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tClassroomStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tClassroomStatus == SUCCESS) {

                    return SUCCESS;
                } else if (tClassroomStatus == DATA_DEPENDENCY_ERROR) {

                    return DATA_DEPENDENCY_ERROR;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return deleteClassroomTask;
    }


    /**
     * This method is used to get a single classroomCountTask object which is used to get total no of Classroom in the DB.
     *
     * @return A classroomCountTask object which is used to get the total no. of Classroom in the DB in a separate thread.
     */
    public Task<Integer> getClassroomCountTask() {

        final String query = "SELECT int_room_no FROM t_classroom";

        Task<Integer> classroomCountTask = new Task<>() {

            @Override
            protected Integer call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query);

                /*
                int_room_no is the primary key, so total count will always be equal to the no of v_room_no
                retrieved
                 */
                return map.get("int_room_no").size();
            }
        };
        return classroomCountTask;
    }
}
