package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Service class to perform exam administration and management.
 */
public class ExamService {

    /*---------------------------------------------Declaration of variables-------------------------------------------*/

    private DatabaseHelper databaseHelper;

    /*-------------------------------------------End of declaration of variables--------------------------------------*/


    /**
     *
     */
    public ExamService() {

        databaseHelper = new DatabaseHelper();
    }

    /**
     *
     * @param additionalQuery
     * @param params
     * @return
     */
    public Task<List<ExamDetails>> getExamDetailsTask(String additionalQuery, String... params) {

        String query = "SELECT * FROM t_exam_details " + additionalQuery;

        Task<List<ExamDetails>> examDetailsTask = new Task<>() {

            @Override
            protected List<ExamDetails> call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                List<ExamDetails> list = new ArrayList<>();

                for (int i = 0; i < map.get("v_exam_details_id").size(); i++) {

                    ExamDetails examDetails = new ExamDetails();

                    examDetails.setExamDetailsId(map.get("v_exam_details_id").get(i));
                    examDetails.setExamType(map.get("v_exam_type").get(i));
                    examDetails.setSemesterType(map.get("v_semester_type").get(i));
                    examDetails.setStartDate(map.get("d_start_date").get(i));
                    examDetails.setEndDate(map.get("d_end_date").get(i) != null ? map.get("d_end_date").get(i) : "");
                    examDetails.setAcademicYear(map.get("v_academic_year").get(i));
                    examDetails.setStartTime(map.get("t_start_time").get(i));
                    examDetails.setEndTime(map.get("t_end_time").get(i));
                    examDetails.setIsExamOnSaturday(map.get("int_is_exam_on_saturday").get(i).equals("1"));

                    list.add(examDetails);
                }
                return list;
            }
        };
        return examDetailsTask;
    }

    /**
     *
     * @param examDetails
     * @return
     */
    public Task<Integer> getAddExamDetailsTask(ExamDetails examDetails) {

        String sql = "INSERT INTO t_exam_details(v_exam_details_id, v_exam_type, v_semester_type, d_start_date, " +
                "t_start_time, t_end_time, v_academic_year, int_is_exam_on_saturday)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Task<Integer> addExamDetailsTask = new Task<>() {

            @Override
            protected Integer call(){

                int tExamDetailsStatus = databaseHelper.insert(sql,
                        examDetails.getExamDetailsId(),
                        examDetails.getExamType(),
                        examDetails.getSemesterType(),
                        examDetails.getStartDate(),
                        examDetails.getStartTime(),
                        examDetails.getEndTime(),
                        examDetails.getAcademicYear(),
                        examDetails.getIsExamOnSaturday() ? "1" : "0");

                if (tExamDetailsStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tExamDetailsStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_ALREADY_EXIST_ERROR;
                }
            }
        };
        return addExamDetailsTask;
    }

    /**
     *
     * @param examDetails
     * @return
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getDeleteExamDetailsTask(ExamDetails examDetails) {

        String sql = "DELETE FROM t_exam_details WHERE v_exam_details_id=?";

        Task<Integer> deleteExamDetailsTask = new Task<>() {

            @Override
            protected Integer call(){

                int tExamDetailsStatus = databaseHelper.updateDelete(sql, examDetails.getExamDetailsId());

                if (tExamDetailsStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tExamDetailsStatus == SUCCESS) {

                    return SUCCESS;
                } else if (tExamDetailsStatus == DATA_DEPENDENCY_ERROR) {

                    return DATA_DEPENDENCY_ERROR;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return deleteExamDetailsTask;
    }

    /**
     *
     * @param examRoutine
     * @return
     */
    @SuppressWarnings("Duplicates")
    public int addRoutineToDatabase(List<Exam> examRoutine) {

        final String sql = "INSERT INTO t_exam_time_table (v_exam_details_id, v_course_id, v_sub_id, d_exam_date)" +
        " VALUES(?, ?, ?, ?)";


        List<List<String>> routine = new ArrayList<>();

        System.out.println(examRoutine.size());
        //for each exam in the exam routine ,form the data in the List<List<String>> structure
        for (Exam exam : examRoutine) {

            List<String> singleExam = new ArrayList<>();

            singleExam.add(exam.getExamDetailId());
            singleExam.add(exam.getCourseId());
            singleExam.add(exam.getSubId());
            singleExam.add(exam.getExamDate());

            //add details of a particular exam into the list
            routine.add(singleExam);
        }

        /*get the no of insertions or error status of the INSERT operation*/
        int tExamTimeTableStatus = databaseHelper.batchInsertUpdateDelete(sql, routine);

        if (tExamTimeTableStatus == DATABASE_ERROR) {

            return DATABASE_ERROR;
        } else if (tExamTimeTableStatus == routine.size()) {

            return SUCCESS;
        } else {

            return tExamTimeTableStatus;
        }
    }

    /**
     *
     * @param additionalQuery
     * @param params
     * @return
     */
    public Task<List<Exam>> getExamRoutineTask(String additionalQuery, String... params) {

        Task<List<Exam>> examRoutineTask = new Task<>() {

            @Override
            protected List<Exam> call() {

                List<Exam> list = getExamRoutine(additionalQuery, params);

                return list;
            }
        };

        return examRoutineTask;
    }

    /**
     *
     * @param additionalQuery
     * @param params
     * @return
     */
    public List<Exam> getExamRoutine(String additionalQuery, String... params) {

        final String query = "SELECT int_exam_id, v_exam_details_id, v_course_id, v_sub_id, v_sub_name, int_semester" +
                ", d_exam_date FROM t_exam_time_table natural join t_subject " + additionalQuery;

        Map<String, List<String>> map = databaseHelper.execQuery(query, params);

        List<Exam> list = new ArrayList<>();

        for (int i = 0; i < map.get("int_exam_id").size(); i++) {

            Exam exam = new Exam();

            exam.setExamDetailId(map.get("v_exam_details_id").get(i));
            exam.setExamId(map.get("int_exam_id").get(i));
            exam.setCourseId(map.get("v_course_id").get(i));
            exam.setSemester(map.get("int_semester").get(i));
            exam.setSubId(map.get("v_sub_id").get(i));
            exam.setSubName(map.get("v_sub_name").get(i));
            exam.setExamDate(map.get("d_exam_date").get(i));

            list.add(exam);
        }
        return list;
    }


    public Task<Integer> getDeleteExamRoutineTask(final ExamDetails examDetails) {

        final String sql = "DELETE FROM t_exam_time_table where v_exam_details_id=?";

        Task<Integer> deleteExamRoutineTask = new Task<>() {

            @Override
            protected Integer call() {

                /*
                holds the status of deletion of student in the DB, i.e success or failure
                 */
                int tExamTimeTableStatus = databaseHelper.updateDelete(sql, examDetails.getExamDetailsId());

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tExamTimeTableStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tExamTimeTableStatus == SUCCESS) {

                    return SUCCESS;
                } else if (tExamTimeTableStatus == DATA_DEPENDENCY_ERROR) {

                    return DATA_DEPENDENCY_ERROR;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return deleteExamRoutineTask;
    }

    /**
     *
     * @param roomAllocationList
     * @return
     */
    @SuppressWarnings("Duplicates")
    public int addRoomAllocationToDatabase(List<RoomAllocation> roomAllocationList) {

        String sql = "INSERT INTO t_room_allocation(int_ralloc_id, v_exam_details_id, int_room_no, v_reg_id)"
                + " VALUES(?, ?, ?, ?)";

        List<List<String>> list = new ArrayList<>();

        //for each RoomAllocation ,form the data in the List<List<String>> structure
        for (RoomAllocation roomAllocation : roomAllocationList) {

            for (Map.Entry<Integer, Integer> entry : roomAllocation.getRoomAllocationMap().entrySet()) {

                List<String> singleRoomAllocation = new ArrayList<>();
                Student student = roomAllocation.getStudentList().get(entry.getValue());

                singleRoomAllocation.add(String.valueOf(entry.getKey()));
                singleRoomAllocation.add(roomAllocation.getExamDetailsId());
                singleRoomAllocation.add(roomAllocation.getRoomNo());
                singleRoomAllocation.add(student.getRegId());

                //add details of a particular RoomAllocation into the list
                list.add(singleRoomAllocation);
            }
        }

        /*get the no of insertions or error status of the INSERT operation*/
        int tRoomAllocationStatus = databaseHelper.batchInsertUpdateDelete(sql, list);

        if (tRoomAllocationStatus == DATABASE_ERROR) {

            return DATABASE_ERROR;
        } else if (tRoomAllocationStatus == list.size()) {

            return SUCCESS;
        } else {

            return tRoomAllocationStatus;
        }
    }

    public Task<Integer> getDeleteRoomAllocationTask(final ExamDetails examDetails) {

        final String sql = "DELETE FROM t_room_allocation where v_exam_details_id=?";

        Task<Integer> deleteRoomAllocationTask = new Task<>() {

            @Override
            protected Integer call() {

                /*
                holds the status of deletion of student in the DB, i.e success or failure
                 */
                int tRoomAllocationStatus = databaseHelper.updateDelete(sql, examDetails.getExamDetailsId());

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tRoomAllocationStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tRoomAllocationStatus == SUCCESS) {

                    return SUCCESS;
                } else if (tRoomAllocationStatus == DATA_DEPENDENCY_ERROR) {

                    return DATA_DEPENDENCY_ERROR;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return deleteRoomAllocationTask;
    }

    /**
     *
     * @param additionalQuery
     * @param params
     * @return
     */
    public Task<List<RoomAllocation>> getRoomAllocationTask(String additionalQuery, String ...params) {

        Task<List<RoomAllocation>> roomAllocationTask = new Task<>() {
            @Override
            protected List<RoomAllocation> call(){

                List<RoomAllocation> list = getRoomAllocation(additionalQuery, params);

                return list;
            }
        };
        return roomAllocationTask;
    }

    /**
     *
     * @param additionalQuery
     * @param params
     * @return
     */
    public List<RoomAllocation> getRoomAllocation(String additionalQuery, String ...params) {

        ClassRoomService classRoomService = new ClassRoomService();
        List<Classroom> classroomList = classRoomService.getClassroomsData("ORDER BY int_room_no");
        List<RoomAllocation> list = new ArrayList<>();

        for(Classroom classroom : classroomList){

            final String query = "SELECT int_ralloc_id ,v_exam_details_id, int_room_no, v_reg_id " +
                    "FROM t_room_allocation natural join t_student_enrollment_details WHERE " +
                    "int_room_no=" + classroom.getRoomNo() + " and v_exam_details_id=? " + additionalQuery;

            Map<String, List<String>> map = databaseHelper.execQuery(query, params);

            if(!map.get("int_ralloc_id").isEmpty()){

                RoomAllocation roomAllocation = new RoomAllocation();

                roomAllocation.setExamDetailsId(params[0]);
                roomAllocation.setRoomNo(classroom.getRoomNo());
                roomAllocation.setNoOfRows(classroom.getNoOfRows());
                roomAllocation.setNoOfCols(classroom.getNoOfCols());

                list.add(roomAllocation);

                for (int i = 0; i < map.get("int_ralloc_id").size(); i++) {

                    Student student = new Student();

                    student.setRegId(map.get("v_reg_id").get(i));

                    roomAllocation.getRoomAllocationMap().put(Integer.valueOf(map.get("int_ralloc_id").get(i)), i);
                    roomAllocation.getStudentList().add(student);
                }
            }
        }
        return list;
    }


    /**
     *
     * @param additionalQuery
     * @param params
     * @return
     */
    public List<ExamsOnRoom> getExamsOnRoomData(String additionalQuery, String ...params){

        List<ExamsOnRoom> listOfExamsGoingOnInRoom = new ArrayList<>();

        String query = "SELECT int_ralloc_id ,int_room_no, v_reg_id, v_sub_id, v_exam_details_id from " +
                "t_room_allocation natural join t_student_marks " + additionalQuery + " ORDER BY int_room_no";

        System.out.println(query);
        Map<String, List<String>> map = databaseHelper.execQuery(query, params);

        String prevRoomNo = "";
        List<String> subIdsUsed = new ArrayList<>();

        ExamsOnRoom examsOnRoom = new ExamsOnRoom();

        for (int i = 0; i < map.get("int_ralloc_id").size(); i++) {

            if (!prevRoomNo.equals(map.get("int_room_no").get(i))) {

                examsOnRoom = new ExamsOnRoom();
                subIdsUsed.clear();
                examsOnRoom.setExamDetailsId(map.get("v_exam_details_id").get(i));
                examsOnRoom.setRoomNo(map.get("int_room_no").get(i));
                listOfExamsGoingOnInRoom.add(examsOnRoom);
                prevRoomNo = examsOnRoom.getRoomNo();
            }
            if(!subIdsUsed.contains(map.get("v_sub_id").get(i))){

                Exam exam = new Exam();
                exam.setSubId(map.get("v_sub_id").get(i));

                examsOnRoom.getExamsList().add(exam);

                subIdsUsed.add(map.get("v_sub_id").get(i));
            }

            Student student = new Student();
            student.setRegId(map.get("v_reg_id").get(i));
            examsOnRoom.getStudentList().add(student);
        }
        return listOfExamsGoingOnInRoom;
    }

    @SuppressWarnings("Duplicates")
    public int addInvigilationDutyToDatabase(List<InvigilationDuty> invigilationDutyList) {

        final String sql = "INSERT INTO t_invigilation_duty (int_room_no, d_exam_date, v_prof_id, v_exam_details_id)" +
                " VALUES(?, ?, ?, ?)";


        List<List<String>> list = new ArrayList<>();

        //for each exam in the exam routine ,form the data in the List<List<String>> structure
        for (InvigilationDuty invigilationDuty : invigilationDutyList) {

            List<String> singleInvigilationDuty = new ArrayList<>();

            singleInvigilationDuty.add(invigilationDuty.getRoomNo());
            singleInvigilationDuty.add(invigilationDuty.getExamDate());
            singleInvigilationDuty.add(invigilationDuty.getProfId());
            singleInvigilationDuty.add(invigilationDuty.getExamDetailId());

            //add details of a particular exam into the list
            list.add(singleInvigilationDuty);
        }

        /*get the no of insertions or error status of the INSERT operation*/
        int tInvigilationDutyStatus = databaseHelper.batchInsertUpdateDelete(sql, list);

        if (tInvigilationDutyStatus == DATABASE_ERROR) {

            return DATABASE_ERROR;
        } else if (tInvigilationDutyStatus == list.size()) {

            return SUCCESS;
        } else {

            return tInvigilationDutyStatus;
        }
    }


    public Task<Integer> getDeleteInvigilationDutyTask(final ExamDetails examDetails) {

        final String sql = "DELETE FROM t_invigilation_duty where v_exam_details_id=?";

        Task<Integer> deleteInvigilationDutyTask = new Task<>() {

            @Override
            protected Integer call() {

                /*
                holds the status of deletion of student in the DB, i.e success or failure
                 */
                int tInvigilationDutyStatus = databaseHelper.updateDelete(sql, examDetails.getExamDetailsId());

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tInvigilationDutyStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tInvigilationDutyStatus == SUCCESS) {

                    return SUCCESS;
                } else if (tInvigilationDutyStatus == DATA_DEPENDENCY_ERROR) {

                    return DATA_DEPENDENCY_ERROR;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return deleteInvigilationDutyTask;
    }

    public Task<List<InvigilationDuty>> getInvigilationDutyDataTask(String examDetailsId){

        Task<List<InvigilationDuty>> invigilationDutyDataTask = new Task<List<InvigilationDuty>>() {

            @Override
            protected List<InvigilationDuty> call(){

                return getInvigilationDutyData(examDetailsId);
            }
        };
        return invigilationDutyDataTask;
    }

    public List<InvigilationDuty> getInvigilationDutyData(String examDetailsId){

        String query = "SELECT int_room_no, d_exam_date, v_prof_id FROM t_invigilation_duty WHERE v_exam_details_id=? " +
                "ORDER BY d_exam_date";

        Map<String, List<String>> map = databaseHelper.execQuery(query, examDetailsId);

        List<InvigilationDuty> list = new ArrayList<>();

        for (int i = 0; i < map.get("v_prof_id").size(); i++) {

            InvigilationDuty invigilationDuty = new InvigilationDuty();

            invigilationDuty.setProfId(map.get("v_prof_id").get(i));
            invigilationDuty.setExamDate(map.get("d_exam_date").get(i));
            invigilationDuty.setRoomNo(map.get("int_room_no").get(i));

            list.add(invigilationDuty);
        }
        return list;
    }

    public Task<Integer> getUpdateInvigilationDutyTask(String oldInvigilatorId, InvigilationDuty invigilationDuty) {

        final String sql = "UPDATE t_invigilation_duty SET v_prof_id=? where d_exam_date=? AND v_prof_id=?";

        Task<Integer> updateInvigilationDutyTask = new Task<>() {

            @Override
            protected Integer call() {

                //holds the status of updation of student in the DB, i.e success or failure
                int tInvigilationDutyStatus = databaseHelper.updateDelete
                        (sql, invigilationDuty.getProfId(), invigilationDuty.getExamDate(), oldInvigilatorId);

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tInvigilationDutyStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tInvigilationDutyStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return updateInvigilationDutyTask;
    }
}
