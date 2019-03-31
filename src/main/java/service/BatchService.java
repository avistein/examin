package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class to  get Batch, add Batch, delete Batch , edit Batch, get total no. of Batches .
 *
 * @author Avik Sarkar
 */
public class BatchService {

    /*--------------------------------Declaration of variables----------------------------------*/

    private DatabaseHelper databaseHelper;

    /*----------------------------------End of Declaration--------------------------------------*/

    /**
     * Default public constructor to initialize a databaseHelper instance.
     */
    public BatchService() {
        databaseHelper = new DatabaseHelper();
    }

    /**
     * This method is used to get a single BatchesTask object which is used to get batch details in a separate thread.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement.
     * @return A batchesTask which can be used to get a list of Batch details from the DB in a separate thread.
     */
    public Task<List<Batch>> getBatchesTask(String additionalQuery, final String... params) {

        Task<List<Batch>> batchTask = new Task<>() {
            @Override
            protected List<Batch> call() {

                List<Batch> list = getBatchData(additionalQuery, params);

                //a list of Batch details
                return list;
            }
        };
        return batchTask;
    }

    /**
     * This method is  is used to get Batches data in the same thread it was called.
     *
     * @param additionalQuery Includes WHERE clause or any other specific query details.
     * @param params          Parameters for the PreparedStatement.
     * @return A list of Batch details retrieved from the DB.
     */
    @SuppressWarnings("Duplicates")
    public List<Batch> getBatchData(String additionalQuery, String... params) {

        //each item in the list is a single Batch details
        List<Batch> list = new ArrayList<>();

        final String query = "SELECT v_course_id, v_discipline, v_degree" +
                ", int_duration, v_dept_name,v_batch_id, v_batch_name from " +
                "t_batch natural join t_course " + additionalQuery;

        Map<String, List<String>> map = databaseHelper.execQuery(query, params);

        //v_batch_id is the primary key, total items in the map will always be equal to no of v_batch_id retrieved
        for (int i = 0; i < map.get("v_batch_id").size(); i++) {

            Batch batch = new Batch();

            batch.setCourseId(map.get("v_course_id").get(i));
            batch.setBatchId(map.get("v_batch_id").get(i));
            batch.setBatchName(map.get("v_batch_name").get(i));
            batch.setDiscipline(map.get("v_discipline").get(i));
            batch.setDegree(map.get("v_degree").get(i));
            batch.setDuration(map.get("int_duration").get(i));
            batch.setDeptName(map.get("v_dept_name").get(i));

            //a single batch details is added to the list
            list.add(batch);
        }
        return list;
    }

    /**
     * This method is used to get a single batchesCountTask object which is used to get total no of Batches in the DB.
     *
     * @return A batchesCountTask object which is used to get the total no. of Batches in the DB in a separate thread.
     */
    public Task<Integer> getBatchesCountTask() {


        final String query = "SELECT v_batch_id FROM t_batch";

        Task<Integer> batchesCountTask = new Task<>() {
            @Override
            protected Integer call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query);

                //v_batch_id is the primary key, so total count will always be equal to the no of v_batch_id retrieved
                return map.get("v_batch_id").size();
            }
        };
        return batchesCountTask;
    }
}
