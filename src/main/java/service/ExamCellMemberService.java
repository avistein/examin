package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.ExamCellMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class to get exam cell member, add exam cell member, edit exam cell member, delete exam cell member,
 * get total no of exam cell members.
 *
 * @author Avik Sarkar
 */
public class ExamCellMemberService {

    /*--------------------------------Declaration of variables---------------------------------------*/

    private DatabaseHelper databaseHelper;

    /*------------------------------------End of Declaration-----------------------------------------*/

    /**
     * Default public constructor to initialize database helper.
     */
    public ExamCellMemberService() {

        databaseHelper = new DatabaseHelper();
    }

    /**
     * This method is used to get a single examCellMemberTask object which is used to get Exam Cell Members details.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_exam_cell_member.
     * @return A examCellMemberTask which can be used to get a list of Exam Cell Members details from the DB in a
     * separate thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<List<ExamCellMember>> getExamCellMembersTask(String additionalQuery, final String... params) {

        final String query = "SELECT * from t_exam_cell_member " + additionalQuery;

        Task<List<ExamCellMember>> examCellMemberTask = new Task<>() {

            @Override
            protected List<ExamCellMember> call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                //each item in the list is a single  Exam Cell Member details
                List<ExamCellMember> list = new ArrayList<>();

                /*
                v_emp_id is the primary key, total items in the map will always be equal to no of
                v_emp_id retrieved
                 */
                for (int i = 0; i < map.get("v_emp_id").size(); i++) {

                    ExamCellMember examCellMember = new ExamCellMember();

                    examCellMember.setEmpId(map.get("v_emp_id").get(i));
                    examCellMember.setFirstName(map.get("v_first_name").get(i));
                    examCellMember.setMiddleName(map.get("v_middle_name").get(i));
                    examCellMember.setLastName(map.get("v_last_name").get(i));
                    examCellMember.setDob(map.get("d_dob").get(i));
                    examCellMember.setDoj(map.get("d_date_of_joining").get(i));
                    examCellMember.setEmail(map.get("v_email_id").get(i));
                    examCellMember.setAddress(map.get("v_address").get(i));
                    examCellMember.setContactNo(map.get("v_contact_no").get(i));

                    //a single Exam Cell Member details is added to the list
                    list.add(examCellMember);
                }

                //a list of Exam Cell Member details
                return list;
            }
        };
        return examCellMemberTask;
    }

    /**
     * This method is used to get a single examCellMembersCountTask object which is used to get total no of
     * Exam Cell members in the DB.
     *
     * @return A examCellMembersCountTask object which is used to get the total no. of Exam Cell members in the
     * DB in a separate thread.
     */
    public Task<Integer> getExamCellMembersCountTask() {

        final String query = "SELECT v_emp_id FROM t_exam_cell_member";

        Task<Integer> examCellMembersCountTask = new Task<>() {

            @Override
            protected Integer call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query);

                 /*
                v_emp_id is the primary key, so total count will always be equal to the no of v_emp_id
                retrieved
                 */
                return map.get("v_emp_id").size();
            }
        };
        return examCellMembersCountTask;
    }
}
