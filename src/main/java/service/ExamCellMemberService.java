package service;

import database.DatabaseHelper;
import model.ExamCellMember;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExamCellMemberService {

    private DatabaseHelper databaseHelper;

    public ExamCellMemberService(){
        databaseHelper = new DatabaseHelper();
    }

    public List<ExamCellMember> getExamCellMemberData(String additionalQuery, String ...params){

        String query = "SELECT * from t_exam_cell_member " + additionalQuery;
        databaseHelper.openConnection();
        List<ExamCellMember> list = new ArrayList<>();
        Map<String, List<String>> map = databaseHelper.execQuery(query, params);
        for (int i = 0; i < map.get("v_emp_id").size(); i++) {

            String empId= map.get("v_emp_id").get(i);
            String firstName = map.get("v_first_name").get(i);
            String middleName = map.get("v_middle_name").get(i);
            if(middleName == null)
                middleName = "";
            String lastName = map.get("v_last_name").get(i);
            String dob = map.get("d_dob").get(i);
            String doj = map.get("d_date_of_joining").get(i);
            String email = map.get("v_email_id").get(i);
            String address = map.get("v_address").get(i);
            String contactNo = map.get("v_contact_no").get(i);

            list.add(new ExamCellMember(empId, firstName, middleName, lastName
                    , dob, doj, email, address, contactNo));
        }
        databaseHelper.closeConnection();
        return list;
    }
}
