package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class to
 *
 * @author Avik Sarkar
 */
public class SubjectService {

    private DatabaseHelper databaseHelper;

    public SubjectService(){
        databaseHelper = new DatabaseHelper();
    }


    @SuppressWarnings("Duplicates")
    public Task<List<Subject>> getSubjectsTask(String additionalQuery, final String ...params){

        final String query = "SELECT v_course_id, v_sub_id, v_sub_name" +
                ", v_credit, v_semester, int_opt_status, v_full_marks" +
                ", v_sub_type from t_subject" + additionalQuery;

        Task<List<Subject>> subjectTask = new Task<>() {
            @Override
            protected List<Subject> call(){
                Map<String, List<String>> map = databaseHelper.execQuery(query, params);
                List<Subject> list = new ArrayList<>();
                for(int i = 0; i < map.get("v_sub_id").size(); i ++){

                    Subject subject = new Subject();
                    subject.setCourseId(map.get("v_course_id").get(i));
                    subject.setSubId(map.get("v_sub_id").get(i));
                    subject.setSubName(map.get("v_sub_name").get(i));
                    subject.setCredit(map.get("v_credit").get(i));
                    subject.setSemester(map.get("v_semester").get(i));
                    subject.setOptStatus(Integer.parseInt(map.get("int_opt_status").get(i)));
                    subject.setFullMarks( map.get("v_full_marks").get(i));
                    subject.setSubType(map.get("v_sub_type").get(i));


                    list.add(subject);
                }
                return list;
            }
        };
        return subjectTask;
    }

    @SuppressWarnings("Duplicates")
    List<Subject> getSubjectDataForServices(String additionalQuery, String ...params){

        List<Subject> list = new ArrayList<>();
        final String query = "SELECT v_course_id, v_sub_id, v_sub_name" +
                ", v_credit, v_semester, int_opt_status, v_full_marks" +
                ", v_sub_type from t_subject" + additionalQuery;
        Map<String, List<String>> map = databaseHelper.execQuery(query, params);
        for(int i = 0; i < map.get("v_sub_id").size(); i ++){

            Subject subject = new Subject();
            subject.setCourseId( map.get("v_course_id").get(i));
            subject.setSubId(map.get("v_sub_id").get(i));
            subject.setSubName(map.get("v_sub_name").get(i));
            subject.setCredit(map.get("v_credit").get(i));
            subject.setSemester(map.get("v_semester").get(i));
            subject.setOptStatus(Integer.parseInt(map.get("int_opt_status").get(i)));
            subject.setFullMarks( map.get("v_full_marks").get(i));
            subject.setSubType( map.get("v_sub_type").get(i));

            list.add(subject);
        }
        return list;
    }

}
