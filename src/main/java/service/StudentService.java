package service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Batch;
import model.Student;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static util.ConstantsUtil.*;
/**
 * Service class to
 *
 * @author Avik Sarkar
 */
public class StudentService {

    private DatabaseHelper databaseHelper;

    public StudentService() {
        databaseHelper = new DatabaseHelper();
    }


    @SuppressWarnings("Duplicates")
    public Task<List<Student>> getStudentTask(String additionalQuery, final String ...params){

        final String query = "SELECT v_reg_id, v_first_name, v_middle_name, v_last_name,d_dob, v_gender, v_reg_year" +
                ",v_email_id, v_address, v_mother_name, v_guardian_contact_no, v_roll_no, v_contact_no" +
                ", v_father_guardian_name, v_curr_semester, v_batch_name, v_discipline, v_degree, v_dept_name " +
                "FROM t_student natural join t_student_enrollment_details natural join t_course natural join" +
                " t_batch " + additionalQuery;

        Task<List<Student>> studentTask = new Task<>() {
            @Override
            protected List<Student> call(){
                Map<String, List<String>> map = databaseHelper.execQuery(query, params);
                List<Student>list = new ArrayList<>();
                for (int i = 0; i < map.get("v_reg_id").size(); i++) {

                    Student student = new Student();

                    student.setFirstName(map.get("v_first_name").get(i));
                    student.setMiddleName(map.get("v_middle_name").get(i));
                    student.setLastName(map.get("v_last_name").get(i));
                    student.setDob(map.get("d_dob").get(i));
                    student.setGender(map.get("v_gender").get(i));
                    student.setRegYear(map.get("v_reg_year").get(i));
                    student.setEmail(map.get("v_email_id").get(i));
                    student.setAddress(map.get("v_address").get(i));
                    student.setMotherName(map.get("v_mother_name").get(i));
                    student.setGuardianContactNo(map.get("v_guardian_contact_no").get(i));
                    student.setRegId(map.get("v_reg_id").get(i));
                    student.setRollNo(map.get("v_roll_no").get(i));
                    student.setContactNo(map.get("v_contact_no").get(i));
                    student.setGuardianName(map.get("v_father_guardian_name").get(i));
                    student.setCurrSemester(map.get("v_curr_semester").get(i));
                    student.setBatchName(map.get("v_batch_name").get(i));
                    student.setDiscipline(map.get("v_discipline").get(i));
                    student.setDegree(map.get("v_degree").get(i));
                    student.setDeptName(map.get("v_dept_name").get(i));

                    list.add(student);
                }
                return list;
            }
        };
        return studentTask;
    }

    public Task<Integer> getAddStudentFromMemoryToDataBaseTask(List<Student> list){
        Task<Integer> addStudentFromMemoryToDataBaseTask = new Task<>() {
            @Override
            protected Integer call(){

                BatchService batchService = new BatchService();

                final String sql1 = "INSERT INTO t_student (v_first_name, v_middle_name" +
                        ", v_last_name, v_reg_id, v_roll_no, d_dob, v_mother_name, v_reg_year" +
                        ", v_contact_no, v_father_guardian_name, v_email_id, v_address" +
                        ", v_guardian_contact_no, v_gender) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                        ", ?, ?, ?, ?)";

                final String sql2 = "INSERT INTO t_student_enrollment_details(v_batch_id" +
                        ", v_reg_id, v_curr_semester) VALUES(?, ?, ?)";

                List<Batch> listOfBatches = batchService.getBatchDataForServices("");

                List<List<String>> listOfStudents = new ArrayList<>();
                List<List<String>> studentEnrollmentList = new ArrayList<>();

                for(Student student : list){
                    List<String> singleStudentDetails = new ArrayList<>();
                    List<String> singleStudentEnrollment = new ArrayList<>();

                    for(Batch batch : listOfBatches){

                        if(batch.getBatchName().equals(student.getBatchName())
                                && batch.getDegree().equals(student.getDegree())
                                && batch.getDiscipline().equals(student.getDiscipline())) {

                            singleStudentEnrollment.add(batch.getBatchId());
                            singleStudentEnrollment.add(student.getRegId());
                            singleStudentEnrollment.add(student.getCurrSemester());

                            studentEnrollmentList.add(singleStudentEnrollment);

                        }
                    }

                    singleStudentDetails.add(student.getFirstName());
                    singleStudentDetails.add(student.getMiddleName());
                    singleStudentDetails.add(student.getLastName());
                    singleStudentDetails.add(student.getRegId());
                    singleStudentDetails.add(student.getRollNo());
                    singleStudentDetails.add(student.getDob());
                    singleStudentDetails.add(student.getMotherName());
                    singleStudentDetails.add(student.getRegYear());
                    singleStudentDetails.add(student.getContactNo());
                    singleStudentDetails.add(student.getGuardianName());
                    singleStudentDetails.add(student.getEmail());
                    singleStudentDetails.add(student.getAddress());
                    singleStudentDetails.add(student.getGuardianContactNo());
                    singleStudentDetails.add(student.getGender());
                    listOfStudents.add(singleStudentDetails);
                }
                int tStudentStatus = databaseHelper.batchInsert(sql1, listOfStudents);
                int tStudentEnrollmentStatus = databaseHelper.batchInsert(sql2, studentEnrollmentList);

                if(tStudentStatus == DATABASE_ERROR ||
                        tStudentEnrollmentStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tStudentStatus == SUCCESS &&
                        tStudentEnrollmentStatus == SUCCESS)
                    return SUCCESS;
                else
                    return DATA_ALREADY_EXIST_ERROR;
            }
        };
        return addStudentFromMemoryToDataBaseTask;
    }


    public Task<List<Student>>  getLoadStudentFromCsvToMemoryTask(final File file, final Map<String, String> map) {

        Task<List<Student>> loadStudentFromCsvToMemoryTask = new Task<>() {
            @Override
            protected List<Student> call() throws Exception {

                Map<String, String> columnNameMapping = new HashMap<>();

                List<Student> listOfStudentsFromCsv = new ArrayList<>();

                columnNameMapping.put(map.get("firstName"), "firstName");
                columnNameMapping.put(map.get("middleName"), "middleName");
                columnNameMapping.put(map.get("lastName"), "lastName");
                columnNameMapping.put(map.get("regId"), "regId");
                columnNameMapping.put(map.get("rollNo"), "rollNo");
                columnNameMapping.put(map.get("dob"), "dob");
                columnNameMapping.put(map.get("gender"), "gender");
                columnNameMapping.put(map.get("regYear"), "regYear");
                columnNameMapping.put(map.get("email"), "email");
                columnNameMapping.put(map.get("address"), "address");
                columnNameMapping.put(map.get("motherName"), "motherName");
                columnNameMapping.put(map.get("guardianContactNo"), "guardianContactNo");
                columnNameMapping.put(map.get("contactNo"), "contactNo");
                columnNameMapping.put(map.get("guardianName"), "guardianName");
                columnNameMapping.put(map.get("currSemester"), "currSemester");
                columnNameMapping.put(map.get("discipline"), "discipline");
                columnNameMapping.put(map.get("degree"), "degree");
                columnNameMapping.put(map.get("batchName"), "batchName");

                HeaderColumnNameTranslateMappingStrategy<Student> strategy = new
                        HeaderColumnNameTranslateMappingStrategy<>();
                strategy.setType(Student.class);
                strategy.setColumnMapping(columnNameMapping);

                try (CSVReader reader = new CSVReader(new FileReader(file))) {

                    CsvToBean<Student> csvToBean = new CsvToBeanBuilder<Student>(reader)
                            .withMappingStrategy(strategy).withSkipLines(1).build();
                    listOfStudentsFromCsv = csvToBean.parse();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return listOfStudentsFromCsv;
            }
        };
        return loadStudentFromCsvToMemoryTask;
    }


    public Task<Integer>  getAddStudentToDatabaseTask(final Student student){
        Task<Integer> addStudentToDatabaseTask = new Task<>() {
            @Override
            protected Integer call() {

                final String sql1 = "INSERT INTO t_student (v_first_name, v_middle_name" +
                        ", v_last_name, v_reg_id, v_roll_no, d_dob, v_mother_name, v_reg_year" +
                        ", v_contact_no, v_father_guardian_name, v_email_id, v_address" +
                        ", v_guardian_contact_no, v_gender) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                        ", ?, ?, ?, ?)";

                final String sql2 = "INSERT INTO t_student_enrollment_details(v_batch_id" +
                        ", v_reg_id, v_curr_semester) VALUES(?, ?, ?)";

                int tStudentStatus = databaseHelper.insert(sql1, student.getFirstName(), student.getMiddleName()
                        , student.getLastName(), student.getRegId(), student.getRollNo()
                        , student.getDob(), student.getMotherName(), student.getRegYear()
                        , student.getContactNo(), student.getGuardianName(), student.getEmail()
                        , student.getAddress(), student.getGuardianContactNo(), student.getGender());

                int tStudentEnrollmentStatus = databaseHelper.insert(sql2, student.getBatchId()
                        , student.getRegId(), student.getCurrSemester());

                if(tStudentStatus == DATABASE_ERROR ||
                        tStudentEnrollmentStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tStudentStatus == SUCCESS &&
                        tStudentEnrollmentStatus== SUCCESS)
                    return SUCCESS;
                else
                    return DATA_ALREADY_EXIST_ERROR;

            }
        };
        return addStudentToDatabaseTask;
    }

    public Task<Integer> getDeleteStudentTask(final Student student){

        //final String sql1 = "DELETE FROM t_login_details where v_user_id=?";
        final String sql2 = "DELETE FROM t_student_enrollment_details where v_reg_id=?";
        final String sql3 = "DELETE FROM t_student where v_reg_id=?";
        Task<Integer> deleteStudentTask = new Task<>() {
            @Override
            protected Integer call()  {
//                boolean t_loginDetailsStatus = databaseHelper.insert
//                (sql1, student.getRegId());
                int tStudentEnrollmentStatus = databaseHelper.updateDelete
                        (sql2, student.getRegId());
                int tStudentStatus = databaseHelper.updateDelete
                        (sql3, student.getRegId());

                if(tStudentStatus == DATABASE_ERROR ||
                        tStudentEnrollmentStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tStudentStatus == SUCCESS &&
                        tStudentEnrollmentStatus== SUCCESS)
                    return SUCCESS;
                else if(tStudentStatus == DATA_DEPENDENCY_ERROR ||
                        tStudentEnrollmentStatus == DATA_DEPENDENCY_ERROR)
                    return DATA_DEPENDENCY_ERROR;
                else
                    return DATA_INEXISTENT_ERROR;
            }
        };
        return deleteStudentTask;
    }

    public Task<Integer> getUpdateStudentTask(final Student student){

        final String sql1 = "UPDATE t_student SET v_first_name=?, v_middle_name=?, v_last_name=?, d_dob=?, v_mother_name=?" +
                ", v_reg_year=?, v_contact_no=?, v_father_guardian_name=?, v_email_id=?, v_address=?, v_guardian_contact_no=?" +
                ", v_gender=? where v_reg_id=?";

        final String sql2 = "UPDATE t_student_enrollment_details SET v_batch_id=?, v_curr_semester=? where v_reg_id=?";
        Task<Integer> updateStudentTask = new Task<>() {
            @Override
            protected Integer call()  {

                int tStudentStatus = databaseHelper.updateDelete
                        (sql1, student.getFirstName(), student.getMiddleName(),student.getLastName(), student.getDob()
                                , student.getMotherName(), student.getRegYear(), student.getContactNo(), student.getGuardianName()
                                , student.getEmail(), student.getAddress(), student.getGuardianContactNo(), student.getGender()
                                , student.getRegId());

                int tStudentEnrollmentStatus = databaseHelper.updateDelete(sql2, student.getBatchId(), student.getCurrSemester()
                        , student.getRegId());

                if(tStudentStatus == DATABASE_ERROR ||
                        tStudentEnrollmentStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tStudentStatus == SUCCESS &&
                        tStudentEnrollmentStatus== SUCCESS)
                    return SUCCESS;
                else
                    return DATA_INEXISTENT_ERROR;
            }
        };
        return updateStudentTask;
    }

    public Task<Integer> getStudentsCountTask(){

        final String query = "SELECT v_reg_id FROM t_student";

        Task<Integer> studentsCountTask = new Task<>() {
            @Override
            protected Integer call(){

                Map<String, List<String>> map = databaseHelper.execQuery(query);
                return map.get("v_reg_id").size();
            }
        };
        return studentsCountTask;
    }

}

