package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.ExamCellMember;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Avik Sarkar
 */
public class ExamCellMemberService {

    private DatabaseHelper databaseHelper;

    public ExamCellMemberService(){
        databaseHelper = new DatabaseHelper();
    }

    @SuppressWarnings("Duplicates")
    public Task<List<ExamCellMember>> getExamCellMembersTask(String additionalQuery, final String ...params){

        final String query = "SELECT * from t_exam_cell_member " + additionalQuery;

        Task<List<ExamCellMember>> exaCellMemberTask = new Task<>() {
            @Override
            protected List<ExamCellMember> call(){
                Map<String, List<String>> map = databaseHelper.execQuery(query, params);
                List<ExamCellMember> list = new ArrayList<>();
                for (int i = 0; i < map.get("v_emp_id").size(); i++) {

                    ExamCellMember examCellMember = new ExamCellMember();

                    examCellMember.setEmpId(map.get("v_emp_id").get(i));
                    examCellMember.setFirstName( map.get("v_first_name").get(i));
                    examCellMember.setMiddleName(map.get("v_middle_name").get(i));
                    examCellMember.setLastName(map.get("v_last_name").get(i));
                    examCellMember.setDob(map.get("d_dob").get(i));
                    examCellMember.setDoj(map.get("d_date_of_joining").get(i));
                    examCellMember.setEmail(map.get("v_email_id").get(i));
                    examCellMember.setAddress(map.get("v_address").get(i));
                    examCellMember.setContactNo(map.get("v_contact_no").get(i));

                    list.add(examCellMember);
                }
                return list;
            }
        };
        return exaCellMemberTask;
    }

}
