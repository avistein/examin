package service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Batch;
import model.Student;
import model.Subject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Service class to get student, add student, edit student, delete student, import students from csv,
 * get total no of departments.
 *
 * @author Avik Sarkar
 */
public class StudentService {

    /*--------------------------------Declaration of variables---------------------------------------*/

    private DatabaseHelper databaseHelper;
    private SubjectService subjectService;

    /*------------------------------------End of Declaration-----------------------------------------*/

    /**
     * Default public constructor to initialize database helper.
     */
    public StudentService() {

        databaseHelper = new DatabaseHelper();
        subjectService = new SubjectService();
    }

    /**
     * This method is used to get a single studentTask object which is used to get student details in a separate thread.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_student.
     * @return A studentTask which can be used to get a list of student details from the DB in a separate
     * thread.
     */
    public Task<List<Student>> getStudentTask(String additionalQuery, final String... params) {

        Task<List<Student>> studentTask = new Task<>() {

            @Override
            protected List<Student> call() {

                List<Student> list = getStudentData(additionalQuery, params);

                //a list of student details
                return list;
            }
        };
        return studentTask;
    }

    /**
     * This method is used to get a list of students' data which is used to get student details in the same UI thread.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_student.
     * @return A list of students' data.
     */
    public List<Student> getStudentData(String additionalQuery, final String... params) {

        final String query = "SELECT v_reg_id, v_first_name, v_middle_name, v_last_name,d_dob, v_gender, v_reg_year" +
                ",v_email_id, v_address, v_mother_name, v_guardian_contact_no, v_roll_no, v_contact_no" +
                ", v_father_guardian_name, int_curr_semester, v_batch_name, v_discipline, v_degree, v_dept_name" +
                ", v_profile_picture_location FROM t_student natural join t_student_enrollment_details natural join " +
                "t_course natural join t_batch " + additionalQuery;

        Map<String, List<String>> map = databaseHelper.execQuery(query, params);

        //each item in the list is a single student details
        List<Student> list = new ArrayList<>();

                /*
                v_reg_id is the primary key, total items in the map will always be equal to no of
                v_reg_id retrieved
                 */
        for (int i = 0; i < map.get("v_reg_id").size(); i++) {

            Student student = new Student();

            student.setFirstName(map.get("v_first_name").get(i));

            //to avoid storing "null"
            if (!(map.get("v_middle_name").get(i) == null)) {

                student.setMiddleName(map.get("v_middle_name").get(i));
            }

            //to avoid storing "null"
            if (!(map.get("v_last_name").get(i) == null)) {

                student.setLastName(map.get("v_last_name").get(i));
            }
            student.setDob(map.get("d_dob").get(i));
            student.setGender(map.get("v_gender").get(i));
            student.setRegYear(map.get("v_reg_year").get(i));
            student.setEmail(map.get("v_email_id").get(i));
            student.setAddress(map.get("v_address").get(i));

            //to avoid storing "null"
            if (!(map.get("v_mother_name").get(i) == null)) {

                student.setMotherName(map.get("v_mother_name").get(i));
            }
            student.setGuardianContactNo(map.get("v_guardian_contact_no").get(i));
            student.setRegId(map.get("v_reg_id").get(i));
            student.setRollNo(map.get("v_roll_no").get(i));
            student.setContactNo(map.get("v_contact_no").get(i));
            student.setGuardianName(map.get("v_father_guardian_name").get(i));
            student.setCurrSemester(map.get("int_curr_semester").get(i));
            student.setBatchName(map.get("v_batch_name").get(i));
            student.setDiscipline(map.get("v_discipline").get(i));
            student.setDegree(map.get("v_degree").get(i));
            student.setDeptName(map.get("v_dept_name").get(i));

            //to avoid storing "null" in image location
            if (!(map.get("v_profile_picture_location").get(i) == null)) {

                student.setProfileImagePath(map.get("v_profile_picture_location").get(i));
            }

            //a single student details is added to the list
            list.add(student);
        }
        return list;
    }

    /**
     * This method can be used to get a task to load a bunch of Students from the CSV file into the memory.
     *
     * @param file The CSV file which contains a bunch of Student details.
     * @param map  A hashMap with data of Student bean as keys and the column names of the CSV file as values.
     *             The first line of the CSV contains the column names.
     * @return A task which can be used to retrieve Students list , which is loaded from the CSV file.
     * @see <a href="http://opencsv.sourceforge.net/apidocs/com/opencsv/bean/HeaderColumnNameMappingStrategy.html">
     * OpenCSV HeaderColumnNameMappingStrategy</a>
     */
    @SuppressWarnings("Duplicates")
    public Task<List<Student>> getLoadStudentFromCsvToMemoryTask(final File file, final Map<String, String> map) {

        Task<List<Student>> loadStudentFromCsvToMemoryTask = new Task<>() {

            @Override
            protected List<Student> call() {

                Map<String, String> columnNameMapping = new HashMap<>();

                //each item in the list is a Student details obtained from the CSV
                List<Student> listOfStudentsFromCsv = new ArrayList<>();

                /*
                Whatever be the column names, those will be mapped to the data of the Student Bean
                 */
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

                /*
                Maps data to objects using the column names in the first row of the CSV file as reference.
                This way the column order does not matter.
                @see for more.
                 */
                HeaderColumnNameTranslateMappingStrategy<Student> strategy = new
                        HeaderColumnNameTranslateMappingStrategy<>();
                strategy.setType(Student.class);
                strategy.setColumnMapping(columnNameMapping);

                //open the CSV file for parsing
                try (CSVReader reader = new CSVReader(new FileReader(file))) {

                    //skip the first line of the csv as it contains the column names
                    CsvToBean<Student> csvToBean = new CsvToBeanBuilder<Student>(reader)
                            .withMappingStrategy(strategy).withSkipLines(1).build();

                    //parse the csv and store list of Student objects
                    listOfStudentsFromCsv = csvToBean.parse();
                } catch (IOException e) {

                    e.printStackTrace();
                }

                //list of Student objects
                return listOfStudentsFromCsv;
            }
        };
        return loadStudentFromCsvToMemoryTask;
    }

    /**
     * This method can be used to get a task to add list of Students to database.
     *
     * @param list The ArrayList containing the Students.
     * @return A task which can be used to add list of Students to database.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getAddStudentFromMemoryToDataBaseTask(List<Student> list) {
        Task<Integer> addStudentFromMemoryToDataBaseTask = new Task<>() {

            @Override
            protected Integer call() {
                BatchService batchService = new BatchService();

                final String sql1 = "INSERT INTO t_student (v_first_name, v_middle_name, v_last_name, v_reg_id" +
                        ", v_roll_no, d_dob, v_mother_name, v_reg_year, v_contact_no, v_father_guardian_name" +
                        ", v_email_id, v_address, v_guardian_contact_no, v_gender) values(?, ?, ?, ?, ?, ?, ?, ?, ?" +
                        ", ?, ?, ?, ?, ?)";

                final String sql2 = "INSERT INTO t_student_enrollment_details(v_batch_id, v_reg_id, int_curr_semester)" +
                        " VALUES(?, ?, ?)";

                final String sql3 = "INSERT INTO t_student_marks(v_reg_id, v_course_id, v_sub_id, v_obtained_marks) VALUES (?, ?, ?, ?)";

                //get the list of batches
                List<Batch> listOfBatches = batchService.getBatchData("");

                /*
                Each item in the list is itself a list of strings ;in the inner list each item is the data
                of the Student bean.
                For example if two Student objects are studentObj1 and studentObj2 and the structure is as follows :

                studentObj1 = regId : 1, rollNo : 1, firstName : Dennis, middleName : MacAlistair, lastName : Ritchie...;
                studentObj2 = regId : 2, rollNo : 2, firstName : Donald, middleName : E, lastName : Knuth...;

                Then, listOfStudents will be stored as :

                listOfHolidays = {{"1","1", "Dennis", "MacAlistair", "Ritchie", ...}, {"2", "2" , "Donald" , "E",
                 "Knuth", ...}}

                 studentEnrollmentList stores the data in the same way for t_student_enrollment_details table in DB.
                 */
                List<List<String>> listOfStudents = new ArrayList<>();
                List<List<String>> studentEnrollmentList = new ArrayList<>();
                List<List<String>> listOfStudentAssignmentsInSubjects = new ArrayList<>();

                //for each student ,form the data in the List<List<String>> structure
                for (Student student : list) {

                    List<String> singleStudentDetails = new ArrayList<>();
                    List<String> singleStudentEnrollment = new ArrayList<>();

                    List<Subject> subjectList = subjectService.getSubjectData("WHERE v_degree=? " +
                            "AND v_discipline=?", student.getDegree(), student.getDiscipline());
                    /*
                    iterate each batch to find out the batchId corresponding to the particular Course and semester, the
                    student is undertaking.
                     */
                    for (Batch batch : listOfBatches) {

                        if (batch.getBatchName().equals(student.getBatchName())
                                && batch.getDegree().equals(student.getDegree())
                                && batch.getDiscipline().equals(student.getDiscipline())) {

                            singleStudentEnrollment.add(batch.getBatchId());
                            singleStudentEnrollment.add(student.getRegId());
                            singleStudentEnrollment.add(student.getCurrSemester());

                            //add batchId,regId,semester of a particular student into the list
                            studentEnrollmentList.add(singleStudentEnrollment);
                        }
                    }

                    for (Subject subject : subjectList) {

                        List<String> singleStudentAssignmentInSubjects = new ArrayList<>();

                        singleStudentAssignmentInSubjects.add(student.getRegId());
                        singleStudentAssignmentInSubjects.add(subject.getCourseId());
                        singleStudentAssignmentInSubjects.add(subject.getSubId());

                        if (subject.getSemester().equals(student.getCurrSemester())) {

                            singleStudentAssignmentInSubjects.add("TBC");
                        } else {

                            singleStudentAssignmentInSubjects.add("40");
                        }

                        listOfStudentAssignmentsInSubjects.add(singleStudentAssignmentInSubjects);
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

                    //add details of a particular student into the list
                    listOfStudents.add(singleStudentDetails);
                }

                /*get the no of insertions or error status of the INSERT operation*/
                int tStudentStatus = databaseHelper.batchInsertUpdateDelete(sql1, listOfStudents);
                int tStudentEnrollmentStatus = databaseHelper.batchInsertUpdateDelete(sql2, studentEnrollmentList);
                int tStudentMarksStatus = databaseHelper.batchInsertUpdateDelete(sql3, listOfStudentAssignmentsInSubjects);

                //if any DB error is present
                if (tStudentStatus == DATABASE_ERROR || tStudentEnrollmentStatus == DATABASE_ERROR
                        || tStudentMarksStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                }

                //return success ,if all students are inserted
                else if (tStudentStatus == listOfStudents.size()
                        && tStudentEnrollmentStatus == studentEnrollmentList.size()
                        && tStudentMarksStatus == listOfStudentAssignmentsInSubjects.size()) {

                    return SUCCESS;
                }

                //return the no of student inserted
                else {

                    return tStudentStatus;
                }
            }
        };
        return addStudentFromMemoryToDataBaseTask;
    }

    /**
     * This method is used to get a task which can be used to add a single student to the DB.
     *
     * @param student The student to be added to the database.
     * @return A task which can be used to add a single student into the DB by running a separate thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getAddStudentToDatabaseTask(final Student student) {

        Task<Integer> addStudentToDatabaseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql1 = "INSERT INTO t_student (v_first_name, v_middle_name, v_last_name, v_reg_id" +
                        ", v_roll_no, d_dob, v_mother_name, v_reg_year, v_contact_no, v_father_guardian_name" +
                        ", v_email_id, v_address, v_guardian_contact_no, v_gender, v_profile_picture_location) " +
                        "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                final String sql2 = "INSERT INTO t_student_enrollment_details(v_batch_id" +
                        ", v_reg_id, int_curr_semester) VALUES(?, ?, ?)";

                final String sql3 = "INSERT INTO t_student_marks(v_reg_id, v_course_id, v_sub_id, v_obtained_marks) VALUES (?, ?, ?, ?)";

                List<Subject> subjectList = subjectService.getSubjectData("WHERE v_degree=? " +
                        "AND v_discipline=?", student.getDegree(), student.getDiscipline());

                List<List<String>> listOfStudentAssignmentsInSubjects = new ArrayList<>();

                for (Subject subject : subjectList) {

                    List<String> singleStudentAssignmentInSubjects = new ArrayList<>();

                    singleStudentAssignmentInSubjects.add(student.getRegId());
                    singleStudentAssignmentInSubjects.add(subject.getCourseId());
                    singleStudentAssignmentInSubjects.add(subject.getSubId());
                    singleStudentAssignmentInSubjects.add("TBC");

                    listOfStudentAssignmentsInSubjects.add(singleStudentAssignmentInSubjects);
                }


                //get the status of insertion of student details into t_student in the DB
                int tStudentStatus = databaseHelper.insert(sql1, student.getFirstName(), student.getMiddleName()
                        , student.getLastName(), student.getRegId(), student.getRollNo(), student.getDob()
                        , student.getMotherName(), student.getRegYear(), student.getContactNo()
                        , student.getGuardianName(), student.getEmail(), student.getAddress()
                        , student.getGuardianContactNo(), student.getGender(), student.getProfileImagePath());

                // get the status of insertion of student details into t_student_enrollment_details in the DB
                int tStudentEnrollmentStatus = databaseHelper.insert(sql2, student.getBatchId()
                        , student.getRegId(), student.getCurrSemester());

                int tStudentMarksStatus = databaseHelper.batchInsertUpdateDelete(sql3, listOfStudentAssignmentsInSubjects);

                //return the status of insertion of student details
                if (tStudentStatus == DATABASE_ERROR || tStudentEnrollmentStatus == DATABASE_ERROR
                        || tStudentMarksStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tStudentStatus == SUCCESS && tStudentEnrollmentStatus == SUCCESS
                        && tStudentMarksStatus == listOfStudentAssignmentsInSubjects.size()) {

                    return SUCCESS;
                } else {

                    return DATA_ALREADY_EXIST_ERROR;
                }

            }
        };
        return addStudentToDatabaseTask;
    }

    /**
     * This method is used to delete students from the database in a separate thread.
     *
     * @param studentList The students to be deleted.
     * @return A task object which can be used to delete students from the database in a separate thread.
     */
    public Task<Integer> getDeleteStudentsTask(final List<Student> studentList) {

        final String sql = "DELETE FROM t_student where v_reg_id=?";

        Task<Integer> deleteStudentsTask = new Task<>() {

            @Override
            protected Integer call() {

                List<List<String>> listOfStudents = new ArrayList<>();

                for (Student student : studentList) {

                    List<String> singleStudent = new ArrayList<>();

                    singleStudent.add(student.getRegId());

                    listOfStudents.add(singleStudent);
                }
                /*
                holds the status of deletion of student in the DB, i.e success or failure
                 */
                int tStudentStatus = databaseHelper.batchInsertUpdateDelete(sql, listOfStudents);

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tStudentStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tStudentStatus == listOfStudents.size()) {

                    return SUCCESS;
                } else {

                    return tStudentStatus;
                }
            }
        };
        return deleteStudentsTask;
    }

    /**
     * This method is used to get a updateStudentTask which is used to edit a single student in the DB.
     *
     * @param student The student to be edited.
     * @return A updateStudentTask instance which is used to edit a single student in the DB in a separate thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getUpdateStudentTask(final Student student) {

        final String sql = "UPDATE t_student SET v_first_name=?, v_middle_name=?, v_last_name=?, d_dob=?" +
                ", v_mother_name=?, v_reg_year=?, v_contact_no=?, v_father_guardian_name=?, v_email_id=?, v_address=?" +
                ", v_guardian_contact_no=?, v_gender=?, v_profile_picture_location=? where v_reg_id=?";

        Task<Integer> updateStudentTask = new Task<>() {

            @Override
            protected Integer call() {

                //holds the status of updation of student in the DB, i.e success or failure
                int tStudentStatus = databaseHelper.updateDelete
                        (sql, student.getFirstName(), student.getMiddleName(), student.getLastName(), student.getDob()
                                , student.getMotherName(), student.getRegYear(), student.getContactNo()
                                , student.getGuardianName(), student.getEmail(), student.getAddress()
                                , student.getGuardianContactNo(), student.getGender(), student.getProfileImagePath()
                                , student.getRegId());

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tStudentStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tStudentStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return updateStudentTask;
    }

    /**
     * This method is used to promote students to next higher semester, basically it updates the int_senester in
     * t_student_enrollment.
     *
     * @param studentList The students whose semesters have to be updates i.e. promoted.
     * @return Status of the operation i.e. success or failure.
     */
    public int promoteStudentToNextSemester(final List<Student> studentList) {

        final String sql = "UPDATE t_student_enrollment_details SET int_curr_semester=? where v_reg_id=?";

        List<List<String>> list = new ArrayList<>();

        for (Student student : studentList) {

            List<String> singleStudent = new ArrayList<>();

            singleStudent.add(student.getCurrSemester());
            singleStudent.add(student.getRegId());

            list.add(singleStudent);
        }

        //holds the status of updation of student in the DB, i.e success or failure
        int tStudentEnrollmentStatus = databaseHelper.batchInsertUpdateDelete(sql, list);

        /*returns an integer holding the different status i.e success, failure etc.*/
        return tStudentEnrollmentStatus;
    }

    /**
     * This method is used to delete a list of students from the t_student_enrollment and place them in the
     * t_degree_completed.
     *
     * @param studentList The student list to work with.
     * @return The status  of the operation i.e. success / failure.
     */
    public int awardDegreeCompletion(final List<Student> studentList) {

        final String sql1 = "DELETE FROM t_student_enrollment_details where v_reg_id=?";
        final String sql2 = "INSERT INTO t_degree_completed_student(v_reg_id, v_batch_id) VALUES(?, ?)";

        List<List<String>> list1 = new ArrayList<>();
        List<List<String>> list2 = new ArrayList<>();

        for (Student student : studentList) {

            List<String> singleStudentInStudentEnrollment = new ArrayList<>();
            List<String> singleStudentInDegreeCompleted = new ArrayList<>();

            String batchId = new BatchService().getBatchData("WHERE v_degree=? AND v_discipline=? " +
                    "AND v_batch_name=?", student.getDegree(), student.getDiscipline(), student.getBatchName())
                    .get(0).getBatchId();

            singleStudentInStudentEnrollment.add(student.getRegId());

            singleStudentInDegreeCompleted.add(student.getRegId());
            singleStudentInDegreeCompleted.add(batchId);

            list1.add(singleStudentInStudentEnrollment);
            list2.add(singleStudentInDegreeCompleted);
        }

        //holds the status of deletion of student from the t_student_enrollment in the DB, i.e success or failure
        int tStudentEnrollmentDetailsStatus = databaseHelper.batchInsertUpdateDelete(sql1, list1);

        int tDegreeCompletedStudentStatus = databaseHelper.batchInsertUpdateDelete(sql2, list2);

        /*returns an integer holding the different status i.e success, failure etc.*/
        if (tDegreeCompletedStudentStatus == DATABASE_ERROR || tStudentEnrollmentDetailsStatus == DATABASE_ERROR) {

            return DATABASE_ERROR;
        } else {

            return tDegreeCompletedStudentStatus;
        }
    }

    /**
     * This method is used to get a single studentsCountTask object which is used to get total no of Students in the DB.
     *
     * @return A studentsCountTask object which is used to get the total no. of Students in the DB in a separate thread.
     */
    public Task<Integer> getStudentsCountTask(String additionalQuery, String... params) {

        final String query = "SELECT v_reg_id FROM t_student " + additionalQuery;

        Task<Integer> studentsCountTask = new Task<>() {

            @Override
            protected Integer call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                /*
                v_reg_id is the primary key, so total count will always be equal to the no of v_reg_id
                retrieved
                 */
                return map.get("v_reg_id").size();
            }
        };
        return studentsCountTask;
    }

}

