//package service;
//
//import com.opencsv.CSVReader;
//import com.opencsv.bean.CsvToBean;
//import com.opencsv.bean.CsvToBeanBuilder;
//import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
//import database.DatabaseHelper;
//import model.Batch;
//import model.Professor;
//import model.Student;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ProfessorService {
//
//    private DatabaseHelper databaseHelper;
//
//    public ProfessorService() {
//        databaseHelper = new DatabaseHelper();
//    }
//
//    @SuppressWarnings("Duplicates")
//    public List<Professor> getProfessorData(String additionalQuery, String ...params) {
//        String query = "SELECT * FROM t_professor natural join t_prof_dept" +
//                " natural join t_department natural join t_course" +
//                " natural join t_subject" + additionalQuery;
//        databaseHelper.openConnection();
//        List<Professor> list = new ArrayList<>();
//        Map<String, List<String>> map = databaseHelper.execQuery(query, params);
//        for (int i = 0; i < map.get("v_prof_id").size(); i++) {
//
//            String profFirstName = map.get("v_first_name").get(i);
//            String profMiddleName = map.get("v_middle_name").get(i);
//            String profLastName = map.get("v_last_name").get(i);
//            String profDob = map.get("d_dob").get(i);
//            String profDoj = map.get("d_date_of_joining").get(i);
//            //String gender = map.get("v_gender").get(i);
//            //String regYear = map.get("v_reg_year").get(i);
//            String profEmail = map.get("v_email_id").get(i);
//            String profAddress = map.get("v_address").get(i);
//            //String motherName = map.get("v_mother_name").get(i);
//            //String guardianContactNo = map.get("v_guardian_contact_no").get(i);
//            //String regId = map.get("v_reg_id").get(i);
//            //String rollNo = map.get("v_roll_no").get(i);
//            String profContactNo = map.get("v_contact_no").get(i);
//            //String guardianName = map.get("v_father_guardian_name").get(i);
//            //String batchId = map.get("v_batch_id").get(i);
//            //String courseId = map.get("v_course_id").get(i);
//            //String currSemester = map.get("v_curr_semester").get(i);
//            //String batchName = map.get("v_batch_name").get(i);
//            //String discipline = map.get("v_discipline").get(i);
//            //String degree = map.get("v_degree").get(i);
//            //String duration = map.get("v_duration").get(i);
//            String deptName = map.get("v_dept_name").get(i);
//            String highestQualification = map.get("v_highest_qualification").get(i);
//            String profId = map.get("v_prof_id").get(i);
//
//            list.add(new Professor(profFirstName, profMiddleName, profLastName, profDob, profDoj,
//                     profEmail, profAddress, profContactNo, deptName, highestQualification, profId));
//        }
//        databaseHelper.closeConnection();
//        return list;
//    }
//
//
//    public boolean loadToDataBase(File file, Map<String, String> map) {
//
//        //DepartmentService departmentService = new DepartmentService();
//
//        Map<String, String> columnNameMapping = new HashMap<>();
//        List<Professor> list = new ArrayList<>();
//        //System.out.println(map.get("firstName"));
//        columnNameMapping.put(map.get("firstName"), "firstName");
//        columnNameMapping.put(map.get("middleName"), "middleName");
//        columnNameMapping.put(map.get("lastName"), "lastName");
//        columnNameMapping.put(map.get("deptName"), "deptName");
//        columnNameMapping.put(map.get("dob"), "dob");
//        columnNameMapping.put(map.get("profId"), "profId");
//        columnNameMapping.put(map.get("contactNo"), "contactNo");
//        //columnNameMapping.put(map.get("subjects"), "subjects");   //UNDONE
//        columnNameMapping.put(map.get("email"), "email");
//        columnNameMapping.put(map.get("highestQualification"), "highestQualification");
//        columnNameMapping.put(map.get("doj"), "doj");
//        columnNameMapping.put(map.get("address"), "address");
//
//
//
//
//       /**
//        columnNameMapping.put(map.get("guardianContactNo"), "guardianContactNo");
//
//        columnNameMapping.put(map.get("guardianName"), "guardianName");
//        columnNameMapping.put(map.get("currSemester"), "currSemester");
//        columnNameMapping.put(map.get("discipline"), "discipline");
//        columnNameMapping.put(map.get("degree"), "degree");
//        //columnNameMapping.put(map.get("deptName"), "deptName");
//        columnNameMapping.put(map.get("batchName"), "batchName");
//        **/
//
//
//
//
//        HeaderColumnNameTranslateMappingStrategy<Student> strategy = new
//                HeaderColumnNameTranslateMappingStrategy<>();
//        strategy.setType(Professor.class);
//        strategy.setColumnMapping(columnNameMapping);
//
//        try (CSVReader reader = new CSVReader(new FileReader(file))) {
//
//            CsvToBean<Professor> csvToBean = new CsvToBeanBuilder<Professor>(reader)
//                    .withMappingStrategy(strategy).withSkipLines(1).build();
//            list = csvToBean.parse();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        databaseHelper.openConnection();
//        final String sql1 = "INSERT INTO t_student (v_first_name, v_middle_name" +
//                ", v_last_name, v_reg_id, v_roll_no, d_dob, v_mother_name, v_reg_year" +
//                ", v_contact_no, v_father_guardian_name, v_email_id, v_address" +
//                ", v_guardian_contact_no, v_gender) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
//                ", ?, ?, ?, ?)";
//
//        final String sql2 = "INSERT INTO t_student_enrollment_details(v_batch_id" +
//                ", v_reg_id, v_curr_semester) VALUES(?, ?, ?)";
//        List<Batch> listOfBatches = batchService.getBatchData("");
//
//        List<List<String>> listOfStudents = new ArrayList<>();
//        List<List<String>> studentEnrollmentList = new ArrayList<>();
//
//        for(Student student : list){
//            List<String> singleStudentDetails = new ArrayList<>();
//            List<String> singleStudentEnrollment = new ArrayList<>();
//
//            for(Batch batch : listOfBatches){
//
//                if(batch.getBatchName().equals(student.getBatchName())
//                        && batch.getDegree().equals(student.getDegree())
//                        && batch.getDiscipline().equals(student.getDiscipline())) {
//
//                    singleStudentEnrollment.add(batch.getBatchId());
//                    singleStudentEnrollment.add(student.getRegId());
//                    singleStudentEnrollment.add(student.getCurrSemester());
//
//                    studentEnrollmentList.add(singleStudentEnrollment);
//
//                }
//            }
//
//            singleStudentDetails.add(student.getFirstName());
//            singleStudentDetails.add(student.getMiddleName());
//            singleStudentDetails.add(student.getLastName());
//            singleStudentDetails.add(student.getRegId());
//            singleStudentDetails.add(student.getRollNo());
//            singleStudentDetails.add(student.getDob());
//            singleStudentDetails.add(student.getMotherName());
//            singleStudentDetails.add(student.getRegYear());
//            singleStudentDetails.add(student.getContactNo());
//            singleStudentDetails.add(student.getGuardianName());
//            singleStudentDetails.add(student.getEmail());
//            singleStudentDetails.add(student.getAddress());
//            singleStudentDetails.add(student.getGuardianContactNo());
//            singleStudentDetails.add(student.getGender());
//            listOfStudents.add(singleStudentDetails);
//        }
//
//        databaseHelper.openConnection();
//        boolean t_studentStatus = databaseHelper.batchInsert(sql1, listOfStudents);
//        boolean t_student_enrollmentStatus = databaseHelper.batchInsert(sql2, studentEnrollmentList);
//        databaseHelper.closeConnection();
//        return t_studentStatus & t_student_enrollmentStatus;
//
//    }
//
//}
