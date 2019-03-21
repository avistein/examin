package service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import database.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import model.Professor;
import model.Subject;
import util.PasswordGenUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Service class to get professor, add professor, edit professor, delete professor, import professors from csv,
 * get total no of departments.
 *
 * @author Sourav Debnath
 */

public class ProfessorService {

    /*--------------------------------Declaration of variables---------------------------------------*/

    private DatabaseHelper databaseHelper;

    /*------------------------------------End of Declaration-----------------------------------------*/

    /**
     * Default public constructor to initialize database helper.
     */
    public ProfessorService() {

        databaseHelper = new DatabaseHelper();
    }

    /**
     * This method is used to get a single professorTask object which is used to get professor details.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_professor.
     * @return A professorTask which can be used to get a list of professor details from the DB in a separate
     * thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<List<Professor>> getProfessorTask(String additionalQuery, final String... params) {

        SubjectService subjectService = new SubjectService();

        final String query = "SELECT v_prof_id, t_professor.v_first_name, v_middle_name, v_last_name, d_dob" +
                ", d_date_of_joining, v_dept_name, v_highest_qualification, int_hod, v_contact_no, v_address" +
                ", v_email_id FROM t_professor natural join t_prof_dept inner join t_user_contact_details on " +
                "t_professor.v_prof_id = t_user_contact_details.v_user_id " + additionalQuery;

        Task<List<Professor>> professorTask = new Task<>() {
            @Override
            protected List<Professor> call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                //each item in the list is a single professor details
                List<Professor> list = new ArrayList<>();

                /*
                v_prof_id is the primary key, total items in the map1 will always be equal to no of
                v_prof_id retrieved
                 */
                for (int i = 0; i < map.get("v_prof_id").size(); i++) {

                    //get the list of subjects for the respective professor
                    List<Subject> listOfSubjects = subjectService.getSubjectDataForServices
                            ("natural join t_prof_sub WHERE v_prof_id=?", map.get("v_prof_id").get(i));

                    Professor professor = new Professor();

                    professor.setProfId(map.get("v_prof_id").get(i));
                    professor.setFirstName(map.get("v_first_name").get(i));

                    //to avoid storing "null"
                    if (!(map.get("v_middle_name").get(i) == null)) {

                        professor.setMiddleName(map.get("v_middle_name").get(i));
                    }

                    //to avoid storing "null"
                    if (!(map.get("v_last_name").get(i) == null)) {

                        professor.setLastName(map.get("v_last_name").get(i));
                    }
                    professor.setDob(map.get("d_dob").get(i));
                    professor.setDoj(map.get("d_date_of_joining").get(i));
                    professor.setEmail(map.get("v_email_id").get(i));
                    professor.setAddress(map.get("v_address").get(i));
                    professor.setContactNo(map.get("v_contact_no").get(i));
                    professor.setHighestQualification(map.get("v_highest_qualification").get(i));
                    professor.setDeptName(map.get("v_dept_name").get(i));
                    professor.setHodStatus(map.get("int_hod").get(i).equals("1") ? "HOD" : "");
                    professor.setSubjects(FXCollections.observableArrayList(listOfSubjects));

                    //a single professor details is added to the list
                    list.add(professor);
                }

                //a list of professor details
                return list;
            }
        };
        return professorTask;
    }

    /**
     * This method can be used to get a task to load a bunch of Professors from the CSV file into the memory.
     *
     * @param file The CSV file which contains a bunch of Professor details.
     * @param map  A hashMap with data of Professor bean as keys and the column names of the CSV file as values.
     *             The first line of the CSV contains the column names.
     * @return A task which can be used to retrieve Professors list , which is loaded from the CSV file.
     * @see <a href="http://opencsv.sourceforge.net/apidocs/com/opencsv/bean/HeaderColumnNameMappingStrategy.html">
     * OpenCSV HeaderColumnNameMappingStrategy</a>
     */
    @SuppressWarnings("Duplicates")
    public Task<List<Professor>> getLoadProfessorFromCsvToMemoryTask(final File file, final Map<String, String> map) {

        Task<List<Professor>> loadProfessorFromCsvToMemoryTask = new Task<>() {

            @Override
            protected List<Professor> call() {

                Map<String, String> columnNameMapping = new HashMap<>();

                //each item in the list is a Professor details obtained from the CSV
                List<Professor> listOfProfessorsFromCsv = new ArrayList<>();

                /*
                Whatever be the column names, those will be mapped to the data of the Professor Bean
                 */
                columnNameMapping.put(map.get("profId"), "profId");
                columnNameMapping.put(map.get("firstName"), "firstName");
                columnNameMapping.put(map.get("middleName"), "middleName");
                columnNameMapping.put(map.get("lastName"), "lastName");
                columnNameMapping.put(map.get("dob"), "dob");
                columnNameMapping.put(map.get("contactNo"), "contactNo");
                columnNameMapping.put(map.get("address"), "address");
                columnNameMapping.put(map.get("email"), "email");
                columnNameMapping.put(map.get("doj"), "doj");
                columnNameMapping.put(map.get("highestQualification"), "highestQualification");
                columnNameMapping.put(map.get("hodStatus"), "hodStatus");
                columnNameMapping.put(map.get("deptName"), "deptName");

                /*
                Maps data to objects using the column names in the first row of the CSV file as reference.
                This way the column order does not matter.
                @see for more.
                 */
                HeaderColumnNameTranslateMappingStrategy<Professor> strategy = new
                        HeaderColumnNameTranslateMappingStrategy<>();
                strategy.setType(Professor.class);
                strategy.setColumnMapping(columnNameMapping);

                //open the CSV file for parsing
                try (CSVReader reader = new CSVReader(new FileReader(file))) {

                    //skip the first line of the csv as it contains the column names
                    CsvToBean<Professor> csvToBean = new CsvToBeanBuilder<Professor>(reader)
                            .withMappingStrategy(strategy).withSkipLines(1).build();

                    //parse the csv and store list of Professor objects
                    listOfProfessorsFromCsv = csvToBean.parse();
                } catch (IOException e) {

                    e.printStackTrace();
                }

                //list of Professor objects
                return listOfProfessorsFromCsv;
            }
        };
        return loadProfessorFromCsvToMemoryTask;
    }

    /**
     * This method can be used to get a task to add list of Professors to database.
     *
     * @param list The ArrayList containing the Professors.
     * @return A task which can be used to add list of Professors to database.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getAddProfessorFromMemoryToDataBaseTask(List<Professor> list) {
        Task<Integer> addProfessorFromMemoryToDataBaseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql1 = "INSERT INTO t_login_details (v_user_id, int_gid, v_pass, v_hash_algo) " +
                        "VALUES(?, ?, ?, ?)";

                final String sql2 = "INSERT INTO t_professor (v_prof_id, v_first_name, v_middle_name, v_last_name" +
                        ", d_dob, d_date_of_joining, v_highest_qualification) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                final String sql3 = "INSERT INTO t_user_contact_details (v_user_id, v_first_name, v_contact_no" +
                        ", v_address, v_email_id) VALUES(?, ?, ?, ?, ?)";

                final String sql4 = "INSERT INTO t_prof_dept (v_prof_id" +
                        ", v_dept_name, int_hod) VALUES(?, ?, ?)";

                /*
                Each item in the list is itself a list of strings ;in the inner list each item is the data
                of the Professor bean.
                For example if two Professor objects are professorObj1 and professorObj2 and the structure is as follows :

                professorObj1 = profId : 1, firstName : Dennis, middleName : MacAlistair, lastName : Ritchie...;
                professorObj2 = profId : 2, firstName : Donald, middleName : E, lastName : Knuth...;

                Then, listOfProfessors will be stored as :

                listOfProfessors = {{"1", "Dennis", "MacAlistair", "Ritchie", ...}, {"2", "Donald" , "E", "Knuth", ...}}

                 professorDeptList stores the data in the same way for t_prof_dept table in DB.
                 */
                List<List<String>> professorsLoginDetailsList = new ArrayList<>();
                List<List<String>> listOfProfessors = new ArrayList<>();
                List<List<String>> professorDeptList = new ArrayList<>();
                List<List<String>> profContactDetailsList = new ArrayList<>();

                for (Professor professor : list) {

                    List<String> singleProfessorLoginDetails = new ArrayList<>();
                    List<String> singleProfessorDetails = new ArrayList<>();
                    List<String> singleProfessorDept = new ArrayList<>();
                    List<String> singleProfContactDetails = new ArrayList<>();

                    //single professor login details
                    singleProfessorLoginDetails.add(professor.getProfId());
                    singleProfessorLoginDetails.add(
                            String.valueOf(professor.getHodStatus().equals("HOD") ? PROFESSOR_HOD_GID : PROFESSOR_GID)
                    );
                    singleProfessorLoginDetails.add(PasswordGenUtil.generateNewPassword().get("hashedPassword"));
                    singleProfessorLoginDetails.add("bcrypt");

                    //add details of a particular professor login  details into the list
                    professorsLoginDetailsList.add(singleProfessorLoginDetails);


                    //single professor details
                    singleProfessorDetails.add(professor.getProfId());
                    singleProfessorDetails.add(professor.getFirstName());
                    singleProfessorDetails.add(professor.getMiddleName());
                    singleProfessorDetails.add(professor.getLastName());
                    singleProfessorDetails.add(professor.getDob());
                    singleProfessorDetails.add(professor.getDoj());
                    singleProfessorDetails.add(professor.getHighestQualification());

                    //add details of a particular professor into the list
                    listOfProfessors.add(singleProfessorDetails);


                    //single professor contact details
                    singleProfContactDetails.add(professor.getContactNo());
                    singleProfContactDetails.add(professor.getAddress());
                    singleProfContactDetails.add(professor.getEmail());

                    //add contact details of a particular professor into the list
                    profContactDetailsList.add(singleProfContactDetails);


                    //single professor dept details
                    singleProfessorDept.add(professor.getProfId());
                    singleProfessorDept.add(professor.getDeptName());
                    singleProfessorDept.add(String.valueOf(professor.getHodStatus().equals("HOD") ? 1 : 0));

                    //add profId,deptName,hodStatus of a particular professor into the list
                    professorDeptList.add(singleProfessorDept);
                }

                /*get the no of insertions or error status of the INSERT operation*/
                int tLoginDetailsStatus = databaseHelper.batchInsert(sql1, professorsLoginDetailsList);
                int tProfessorStatus = databaseHelper.batchInsert(sql2, listOfProfessors);
                int tUserContactDetails = databaseHelper.batchInsert(sql3, profContactDetailsList);
                int tProfessorDeptStatus = databaseHelper.batchInsert(sql4, professorDeptList);

                //if any DB error is present
                if (tLoginDetailsStatus == DATABASE_ERROR || tProfessorStatus == DATABASE_ERROR
                        || tProfessorDeptStatus == DATABASE_ERROR || tUserContactDetails == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                }

                //return success ,if all professors are inserted
                else if (tLoginDetailsStatus == SUCCESS && tProfessorStatus == SUCCESS
                        && tProfessorDeptStatus == SUCCESS && tUserContactDetails == SUCCESS) {

                    return SUCCESS;
                }

                //return the no of professor inserted
                else {

                    return DATA_ALREADY_EXIST_ERROR;
                }
            }
        };
        return addProfessorFromMemoryToDataBaseTask;
    }

    /**
     * This method is used to get a task which can be used to add a single professor to the DB.
     *
     * @param professor The professor to be added to the database.
     * @return A task which can be used to add a single professor into the DB by running a separate thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getAddProfessorToDatabaseTask(final Professor professor) {
        Task<Integer> addProfessorToDatabaseTask = new Task<>() {
            @Override
            protected Integer call() {

                String profGid = String.valueOf(professor.getHodStatus().equals("HOD") ? PROFESSOR_HOD_GID : PROFESSOR_GID);
                Map<String, String> genPassMap = PasswordGenUtil.generateNewPassword();

                final String sql1 = "INSERT INTO t_login_details (v_user_id, int_gid, v_pass, v_hash_algo) " +
                        "VALUES(?, ?, ?, ?)";

                final String sql2 = "INSERT INTO t_professor (v_prof_id, v_first_name, v_middle_name" +
                        ", v_last_name, d_dob, d_date_of_joining, v_highest_qualification) " +
                        "values(?, ?, ?, ?, ?, ?, ?)";

                final String sql3 = "INSERT INTO t_user_contact_details (v_user_id, v_first_name, v_contact_no" +
                        ", v_address, v_email_id) values(?, ?, ?, ?, ?)";

                final String sql4 = "INSERT INTO t_prof_dept(v_prof_id" +
                        ", v_dept_name, int_hod) VALUES(?, ?, ?)";

                //get the status of insertion of professor's login details into t_login_details in the DB
                int tLoginDetailsStatus = databaseHelper.updateDelete(sql1, professor.getProfId()
                        , profGid, genPassMap.get("hashedPassword"), "bcrypt");

                //get the status of insertion of professor details into t_professor in the DB
                int tProfessorStatus = databaseHelper.updateDelete(sql2, professor.getProfId()
                        , professor.getFirstName(), professor.getMiddleName(), professor.getLastName()
                        , professor.getDob(), professor.getDoj(), professor.getHighestQualification());

                // get the status of insertion of professor details into t_user_contact_details in the DB
                int tUserContactDetails = databaseHelper.updateDelete(sql3, professor.getProfId()
                        , professor.getFirstName(), professor.getContactNo(), professor.getAddress()
                        , professor.getEmail());

                // get the status of insertion of professor details into t_prof_dept in the DB
                int tProfessorDeptStatus = databaseHelper.updateDelete(sql4, professor.getProfId()
                        , professor.getDeptName(), String.valueOf(professor.getHodStatus().equals("HOD") ? 1 : 0));

                //return the status of insertion of professor details
                if (tLoginDetailsStatus == DATABASE_ERROR || tProfessorStatus == DATABASE_ERROR
                        || tProfessorDeptStatus == DATABASE_ERROR || tUserContactDetails == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tLoginDetailsStatus == SUCCESS && tProfessorStatus == SUCCESS
                        && tProfessorDeptStatus == SUCCESS && tUserContactDetails == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_ALREADY_EXIST_ERROR;
                }
            }
        };
        return addProfessorToDatabaseTask;
    }

    /**
     * This method is used to get a deleteProfessorTask which is used to delete a single professor in the DB.
     *
     * @param professor The professor to be deleted.
     * @return A deleteProfessor Task instance which is used to delete a professor in the DB in a separate thread.
     */

    public Task<Integer> getDeleteProfessorTask(final Professor professor) {

        final String sql = "DELETE FROM t_login_details where v_user_id=?";
//
//        final String sql2 = "DELETE FROM t_professor where v_prof_id=?";
//
//        final String sql3 = "DELETE FROM t_user_contact_details where v_user_id=?";

        Task<Integer> deleteProfessorTask = new Task<>() {
            @Override
            protected Integer call() {
//                boolean t_loginDetailsStatus = databaseHelper.insertUpdateDelete
//                (sql1, student.getRegId());

                /*
                holds the status of deletion of professor in the DB, i.e success or failure
                 */
                int tLoginDetailsStatus = databaseHelper.updateDelete
                        (sql, professor.getProfId());


                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tLoginDetailsStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;

                else if (tLoginDetailsStatus == SUCCESS)
                    return SUCCESS;

                else
                    return DATA_INEXISTENT_ERROR;
            }
        };
        return deleteProfessorTask;
    }

    /**
     * This method is used to get a updateProfessorTask which is used to edit a single professor in the DB.
     *
     * @param professor The professor to be edited.
     * @return A updateProfessorTask instance which is used to edit a single professor in the DB in a separate thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getUpdateProfessorTask(final Professor professor) {

        final String sql1 = "UPDATE t_professor SET v_first_name=?, v_middle_name=?, v_last_name=?, d_dob=?" +
                ", d_date_of_joining=?, v_highest_qualification=? where v_prof_id=?";

        final String sql2 = "UPDATE t_user_contact_details SET v_contact_no=?, v_address=?, v_email_id=? " +
                "where v_user_id=?";

        final String sql3 = "UPDATE t_prof_Dept SET int_hod=? where v_prof_id=?";

        final String sql4 = "UPDATE t_login_details SET int_gid=? where v_user_id=?";

        Task<Integer> updateProfessorTask = new Task<>() {

            @Override
            protected Integer call() {

                //holds the status of updation of professor in the DB, i.e success or failure
                int tProfessorStatus = databaseHelper.updateDelete
                        (sql1, professor.getFirstName(), professor.getMiddleName(), professor.getLastName()
                                , professor.getDob(), professor.getDoj(), professor.getHighestQualification()
                                , professor.getProfId());

                int tUserContactDetailsStatus = databaseHelper.updateDelete
                        (sql2, professor.getContactNo(), professor.getAddress(), professor.getEmail()
                                , professor.getProfId());

                int tProfDeptStatus = databaseHelper.updateDelete
                        (sql3, String.valueOf(professor.getHodStatus().equals("HOD") ? 1 : 0), professor.getProfId());

                int tLoginDetails = databaseHelper.updateDelete
                        (sql4, String.valueOf(professor.getHodStatus().equals("HOD") ? PROFESSOR_HOD_GID : PROFESSOR_GID)
                                , professor.getProfId());

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tProfessorStatus == DATABASE_ERROR || tUserContactDetailsStatus == DATABASE_ERROR
                        || tProfDeptStatus == DATABASE_ERROR || tLoginDetails == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tProfessorStatus == SUCCESS && tUserContactDetailsStatus == SUCCESS
                        && tProfDeptStatus == SUCCESS && tLoginDetails == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return updateProfessorTask;
    }

    /**
     * This method is used to get a single professorsCountTask object which is used to get total no of Professors in
     * the DB.
     *
     * @return A professorsCountTask object which is used to get the total no. of Professors in the DB in a separate
     * thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getProfessorsCountTask() {

        final String query = "SELECT v_prof_id FROM t_professor";

        Task<Integer> professorsCountTask = new Task<>() {

            @Override
            protected Integer call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query);

                /*
                v_prof_id is the primary key, so total count will always be equal to the no of v_prof_id
                retrieved
                 */
                return map.get("v_prof_id").size();
            }
        };
        return professorsCountTask;
    }
}
