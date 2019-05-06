package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.ExamCellMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

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

        final String query1 = "SELECT * from t_exam_cell_member " + additionalQuery;
        final String query2 = "SELECT * from t_user_contact_details where v_user_id=?";

        Task<List<ExamCellMember>> examCellMemberTask = new Task<>() {

            @Override
            protected List<ExamCellMember> call() {

                Map<String, List<String>> map1 = databaseHelper.execQuery(query1, params);
                //each item in the list is a single  Exam Cell Member details
                List<ExamCellMember> list = new ArrayList<>();

                /*
                v_emp_id is the primary key, total items in the map will always be equal to no of
                v_emp_id retrieved
                 */
                for (int i = 0; i < map1.get("v_emp_id").size(); i++) {

                    ExamCellMember examCellMember = new ExamCellMember();

                    examCellMember.setEmpId(map1.get("v_emp_id").get(i));
                    examCellMember.setFirstName(map1.get("v_first_name").get(i));

                    //get the exam cell member email,address,contact no from t_user_contact_details
                    Map<String, List<String>> map2 = databaseHelper.execQuery(query2, examCellMember.getEmpId());

                    //to avoid storing "null"
                    if (!(map1.get("v_middle_name").get(i) == null)) {

                        examCellMember.setMiddleName(map1.get("v_middle_name").get(i));
                    }

                    //to avoid storing "null"
                    if (!(map1.get("v_last_name").get(i) == null)) {

                        examCellMember.setLastName(map1.get("v_last_name").get(i));
                    }

                    examCellMember.setDob(map1.get("d_dob").get(i));
                    examCellMember.setDoj(map1.get("d_date_of_joining").get(i));
                    examCellMember.setEmail(map2.get("v_email_id").get(i));
                    examCellMember.setAddress(map2.get("v_address").get(i));
                    examCellMember.setContactNo(map2.get("v_contact_no").get(i));

                    //to avoid storing "null" in image location
                    if (!(map1.get("v_profile_picture_location").get(i) == null)) {

                        examCellMember.setProfileImagePath(map1.get("v_profile_picture_location").get(i));
                    }

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
     * This method is used to get a updateExamCellMemberTask which is used to update a single exam cell member
     * in the DB.
     *
     * @param examCellMember The examCellMember to be updated.
     * @return A updateExamCellMemberTask instance which is used to update a single exam cell member in the DB in
     * a separate thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getUpdateExamCellMemberTask(final ExamCellMember examCellMember) {

        Task<Integer> updateExamCellMember = new Task<>() {

            final String sql1 = "UPDATE t_exam_cell_member SET v_first_name=?, v_middle_name=?, v_last_name=?," +
                    " d_dob=?, d_date_of_joining=?, v_profile_picture_location=? WHERE v_emp_id=?";

            final String sql2 = "UPDATE t_user_contact_details SET v_email_id=?, v_address=?, v_contact_no=?" +
                    "WHERE v_user_id=?";

            @Override
            protected Integer call() {

                //holds the status of updation of student in the DB, i.e success or failure
                int tExamCellMemberUpdateStatus = databaseHelper.updateDelete
                        (sql1, examCellMember.getFirstName(), examCellMember.getMiddleName()
                                , examCellMember.getLastName(), examCellMember.getDob(), examCellMember.getDoj()
                                , examCellMember.getProfileImagePath(), examCellMember.getEmpId());

                int tUserContactDetailsStatus = databaseHelper.updateDelete
                        (sql2, examCellMember.getEmail(), examCellMember.getAddress(), examCellMember.getContactNo()
                                , examCellMember.getEmpId());

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tExamCellMemberUpdateStatus == DATABASE_ERROR || tUserContactDetailsStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tExamCellMemberUpdateStatus == SUCCESS && tUserContactDetailsStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return updateExamCellMember;
    }

    /**
     * This method is used to get a single examCellMembersCountTask object which is used to get total no of
     * Exam Cell members in the DB.
     *
     * @return A examCellMembersCountTask object which is used to get the total no. of Exam Cell members in the
     * DB in a separate thread.
     */
    public Task<Integer> getExamCellMembersCountTask(String additionalQuery, String... params) {

        final String query = "SELECT v_emp_id FROM t_exam_cell_member " + additionalQuery;

        Task<Integer> examCellMembersCountTask = new Task<>() {

            @Override
            protected Integer call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

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
