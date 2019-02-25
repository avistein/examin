package service;

import database.DatabaseHelper;
import model.Batch;
import model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BatchService {

    private DatabaseHelper databaseHelper;

    public BatchService(){
        databaseHelper = new DatabaseHelper();
    }

    @SuppressWarnings("Duplicates")
    public List<Batch> getBatchData(String additionalQuery, String ...params){
        databaseHelper.openConnection();
        List<Batch> list = new ArrayList<>();
        final String query = "SELECT v_course_id, v_discipline, v_degree" +
                ", v_duration, v_dept_name,v_batch_id, v_batch_name from " +
                "t_batch natural join t_course " + additionalQuery;
        Map<String, List<String>> map = databaseHelper.execQuery(query, params);
        for(int i = 0; i < map.get("v_batch_id").size(); i ++){

            String courseId = map.get("v_course_id").get(i);
            String batchId = map.get("v_batch_id").get(i);
            String batchName = map.get("v_batch_name").get(i);
            String discipline = map.get("v_discipline").get(i);
            String degree = map.get("v_degree").get(i);
            String duration = map.get("v_duration").get(i);
            String deptName = map.get("v_dept_name").get(i);

            list.add(new Batch(batchId, courseId, batchName,
                    discipline, degree, duration, deptName));
        }
        databaseHelper.closeConnection();
        return list;
    }
}
