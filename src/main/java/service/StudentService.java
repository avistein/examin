package service;

import database.DatabaseHelper;
import model.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentService {

    private DatabaseHelper databaseHelper;

    public StudentService(){
        databaseHelper = new DatabaseHelper();
    }

    public List<Student>  getStudentData(String additionalQuery,String ...params){
        String query = "SELECT v_first_name, v_middle_name, v_last_name, v_reg_id, " +
                "v_roll_no ,d_dob, v_mother_name, v_reg_year,v_contact_no," +
                "v_guardian_contact_no,v_email_id, v_address, v_father_guardian_name," +
                " v_gender FROM t_student " + additionalQuery;
        databaseHelper.openConnection();
        List<Student> list = new ArrayList<>();
        Map<String, List<String>> map = databaseHelper.execQuery(query, params);
        for(int i = 0; i < map.get("v_reg_id").size(); i ++){

            String firstName = map.get("v_first_name").get(i);
            String middleName = map.get("v_middle_name").get(i);
            String lastName = map.get("v_last_name").get(i);
            String dob =map.get("d_dob").get(i);
            String gender =map.get("v_gender").get(i);
            String regYear =map.get("v_reg_year").get(i);
            String email =map.get("v_email_id").get(i);
            String address = map.get("v_address").get(i);
            String motherName =map.get("v_mother_name").get(i);
            String guardianContactNo =map.get("v_guardian_contact_no").get(i);
            String regId = map.get("v_reg_id").get(i);
            String rollNo = map.get("v_roll_no").get(i);
            String contactNo = map.get("v_contact_no").get(i);
            String guardianName = map.get("v_father_guardian_name").get(i);
            list.add(new Student(firstName, middleName, lastName, dob, gender,
                    regYear, email, address, motherName, guardianContactNo, regId,
                    rollNo, contactNo, guardianName));
        }
        databaseHelper.closeConnection();
        return list;
    }

}
