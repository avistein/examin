package command;

import javafx.concurrent.Task;
import model.*;
import service.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is used to handle the logic needed for Exam Administration.
 * This class contains algorithms to : 1) Create Exam Routine  2) Create Room Allocation 3) Create Invigilation Duties
 * <p>
 * Command classes form the Business Logic layer in our application.
 *
 * @author Avik Sarkar
 */
public class ExamCommand {

    /*--------------------------------------------Declaration of Variables--------------------------------------------*/

    private List<Holiday> listOfHolidays;
    private SubjectService subjectService;
    private CourseService courseService;
    private BatchService batchService;
    private HolidayService holidayService;
    private ExamService examService;
    private ClassRoomService classRoomService;
    private StudentService studentService;
    private ProfessorService professorService;
    private List<Exam> examRoutine;

    /*---------------------------------------End of Declaration of Variables------------------------------------------*/


    /**
     * Default constructor to initialize variables.
     */
    public ExamCommand() {

        subjectService = new SubjectService();
        courseService = new CourseService();
        batchService = new BatchService();
        holidayService = new HolidayService();
        examService = new ExamService();
        classRoomService = new ClassRoomService();
        studentService = new StudentService();
        professorService = new ProfessorService();
        examRoutine = new ArrayList<>();

        //listOfHolidays for creating exam routine
        listOfHolidays = new ArrayList<>();
    }

    /**
     * This method is used to get a task which can be used to create the exam time table.
     *
     * @param examDetails The details of the examination for which the routine/time table will be created.
     * @return The examination routine which is a list of exams .
     */
    public Task<Integer> getCreateExamRoutineTask(ExamDetails examDetails) {

        Task<Integer> createExamRoutineTask = new Task<>() {

            @Override
            protected Integer call() {

                //parse the exam date string as Date
                LocalDate examStartDate = LocalDate.parse(examDetails.getStartDate());

                //get the Holidays List,so that exam routine can be created
                listOfHolidays = holidayService.getHolidaysData();

                //get all courses in the DB
                List<Course> listOfCourses = courseService.getCourseData("");

                //generate valid Exam Dates and store in the list
                List<String> validExamDates = generateExamDates(examStartDate, examDetails.getIsExamOnSaturday(),
                        Integer.parseInt(examDetails.getExamInterval()));

                /*
                Get the subjects of ODD or EVEN semesters.
                 */
                String additionalQuery;
                if (examDetails.getSemesterType().equals("ODD")) {

                    additionalQuery = "where int_semester IN(1, 3, 5, 7, 9) and v_sub_type=?";
                } else {

                    additionalQuery = "where int_semester IN(2, 4, 6, 8, 10) and v_sub_type=?";
                }
                List<Subject> listOfSubjects = subjectService.getSubjectData
                        (additionalQuery, examDetails.getExamType());

                /*
                The data structure is as follows :

                Key : The Course ID.
                Value : List of list of subjects.

                Suppose there are two Course IDs, CSE and ECE, each has 3 semesters each.

                Subjects in CSE 2nd, 4th & 6th semesters are {CS201, PH101}, {CS401, CS402}, {CS601, CS602}
                Subjects in ECE 2nd, 4th & 6th semesters are {ECE201, PH101}, {ECE401, ECE402}, {ECE601, ECE602}

                The data will be stored as follows :
                CSE : {{CS201, PH101}, {CS401, CS402}, {CS601, CS602}}
                ECE : {{ECE201, PH101}, {ECE401, ECE402}, {ECE601, ECE602}}
                 */
                Map<String, List<List<Subject>>> courseSubjectMap = new HashMap<>();

                /*
                Iterate the courses, put the Course ID as keys and initialize an arrayList for each key to store
                the subjects, semester wise
                 */
                for (Course course : listOfCourses) {

                    List<List<Subject>> list = new ArrayList<>();

                    int totalSemesters = Integer.parseInt(course.getDuration());

                    //increment by 2 , coz the Exam will be either of ODD sem or EVEN sem, such as 1,3,5 or 2,4,6 sems
                    for (int i = 1; i <= totalSemesters; i += 2) {

                        list.add(new ArrayList<>());
                    }
                    courseSubjectMap.put(course.getCourseId(), list);
                }

                /*
                Add subjects to the HashMap semester wise
                 */
                for (Subject subject : listOfSubjects) {

                    courseSubjectMap.get(subject.getCourseId()).get((Integer.parseInt(subject.getSemester()) - 1) / 2)
                            .add(subject);
                }

                //for each course and it's subjects
                for (Map.Entry<String, List<List<Subject>>> entry : courseSubjectMap.entrySet()) {

                    //for subjects of each semesters(odd or even)
                    for (List<Subject> subjectList : entry.getValue()) {

                        //routine of each semester in the Course
                        List<Exam> currSemExamRoutine = new ArrayList<>();

                        //list of dates used for creating the current semester routine
                        List<String> listOfDatesUsed = new ArrayList<>();

                        //list of exam whose dates need to be modified in case of any collisions
                        List<Integer> examsToBeModified = new ArrayList<>();

                        int currExamDateIndex = 0;
                        boolean isCurrentExamRoutineNeedsModification = false;

                        //for each subject in the Subject List of the current semester of the Course
                        for (Subject subject : subjectList) {

                            //if the exam for the subject is already created , then get the date of that exam
                            String examDateIfSubjectPresentInRoutine = getExamDateOfSubjectPresentInRoutine
                                    (subject.getSubId());

                            String examDate;

                            //exam is already created for the subject
                            if (examDateIfSubjectPresentInRoutine != null) {

                                examDate = examDateIfSubjectPresentInRoutine;
                                listOfDatesUsed.add(examDateIfSubjectPresentInRoutine);
                                isCurrentExamRoutineNeedsModification = true;
                            }

                            //exam for the subject is not there in the routine
                            else {

                                examDate = validExamDates.get(currExamDateIndex++);
                                examsToBeModified.add(subjectList.indexOf(subject));
                            }

                            //create a new exam and add it to the current semester routine
                            Exam exam = new Exam();

                            exam.setExamDetailId(examDetails.getExamDetailsId());
                            exam.setCourseId(entry.getKey());
                            exam.setSemester(subject.getSemester());
                            exam.setSubId(subject.getSubId());
                            exam.setSubName(subject.getSubName());
                            exam.setExamDate(examDate);

                            currSemExamRoutine.add(exam);
                        }

                        /*
                        The current routine needs to be modified.
                         */
                        if (isCurrentExamRoutineNeedsModification) {

                            for (int examNo : examsToBeModified) {

                                for (String date : validExamDates) {

                                    //take a date excluding the dates already used in the current routine
                                    if (!listOfDatesUsed.contains(date)) {

                                        listOfDatesUsed.add(date);
                                        currSemExamRoutine.get(examNo).setExamDate(date);
                                        break;
                                    }
                                }
                            }
                        }

                        /*
                        After the current semester routine is made, add it to the main routine list and proceed with
                        other semesters routine
                         */
                        examRoutine.addAll(currSemExamRoutine);
                    }
                }

                //add the routine to the database
                int addRoutineToDbStatus = examService.addRoutineToDatabase(examRoutine);

                return addRoutineToDbStatus;
            }
        };
        return createExamRoutineTask;
    }

    /**
     * This method is used to return the date of the examination if examination of the input Subject ID is already
     * created and present in the Examination Routine.
     *
     * @param subId The Sub ID whose presence will be checked in the exam routine.
     * @return The examination date.
     */
    private String getExamDateOfSubjectPresentInRoutine(String subId) {

        for (Exam exam : examRoutine) {

            if (exam.getSubId().equals(subId)) {

                return exam.getExamDate();
            }
        }
        return null;
    }

    /**
     * Generates a list containing 30 valid examination dates according to the criteria mentioned by the user and the
     * Holidays list.
     *
     * @param currExamDate     Initially it is the start date of the examination, and will be updated to get the next
     *                         exam dates
     * @param isExamOnSaturday Boolean value to indicate if any exam can happen on Saturday.
     * @param examInterval     The no of days gap between two exams.
     * @return A list of valid exam dates.
     */
    private List<String> generateExamDates(LocalDate currExamDate, boolean isExamOnSaturday, int examInterval) {

        List<String> validExamDates = new ArrayList<>();

        //only 30 valid exam dates are generated
        for (int i = 1; i <= 30; i++) {

            currExamDate = getNextExamDate(currExamDate, isExamOnSaturday);
            validExamDates.add(currExamDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            currExamDate = currExamDate.plusDays(examInterval);
        }
        return validExamDates;
    }

    /**
     * This method is used to generate the next valid exam date.
     *
     * @param currExamDate       The current exam date which will be used to generate the next valid exam date.
     * @param keepExamOnSaturday Boolean value to indicate if any exam can happen on Saturday.
     * @return A localDate object containing the next valid exam date.
     */
    @SuppressWarnings("Duplicates")
    private LocalDate getNextExamDate(LocalDate currExamDate, boolean keepExamOnSaturday) {

        //get the day of the current exam date
        String dayOfTheCurrExamDate = currExamDate.getDayOfWeek().name();

        //if no holiday list is provided then check for sundays and saturdays only
        if (listOfHolidays.isEmpty()) {

            if (dayOfTheCurrExamDate.equals("SUNDAY")) {

                currExamDate = currExamDate.plusDays(1);
            }

            if (!keepExamOnSaturday && dayOfTheCurrExamDate.equals("SATURDAY")) {

                currExamDate = currExamDate.plusDays(2);
            }
        }

        //check the entire holiday list to prevent any exam date to fall on a Holiday
        for (Holiday holiday : listOfHolidays) {

            dayOfTheCurrExamDate = currExamDate.getDayOfWeek().name();

            if (dayOfTheCurrExamDate.equals("SUNDAY")) {

                currExamDate = currExamDate.plusDays(1);
            }

            if (!keepExamOnSaturday && dayOfTheCurrExamDate.equals("SATURDAY")) {

                currExamDate = currExamDate.plusDays(2);
            }

            LocalDate holidayStartDate = LocalDate.parse(holiday.getStartDate());
            LocalDate holidayEndDate = LocalDate.parse(holiday.getEndDate());
            Stream<LocalDate> interval = holidayStartDate.datesUntil(holidayEndDate.plusDays(1));

            LocalDate date = currExamDate;

            //check if the currently taken exam date fall between any holiday
            boolean isCurrExamDateAHoliday = interval.anyMatch(new Predicate<>() {

                @Override
                public boolean test(LocalDate localDate) {

                    return localDate.isEqual(date);
                }
            });

            //if this falls between a holiday , then get the next successive date
            if (isCurrExamDateAHoliday) {

                currExamDate = holidayEndDate.plusDays(1);
            }
        }
        return currExamDate;
    }

    /**
     *
     * @return
     */
    public Task<Integer> getAssignStudentsToExamTask(){

        Task<Integer> assignStudentsToExamTask = new Task<>() {
            @Override
            protected Integer call(){

                List<Exam> examRoutine = examService.getExamRoutine("");
                List<Marks> studentAssignmentInExamsList = new ArrayList<>();
                for (Exam exam : examRoutine) {

                    List<Student> studentList = studentService.getStudentData("WHERE " +
                            "v_course_id=? AND int_curr_semester=?", exam.getCourseId(), exam.getSemester());

                    for (Student student : studentList) {

                        Marks studentAssignmentInExams = new Marks();

                        studentAssignmentInExams.setRegId(student.getRegId());
                        studentAssignmentInExams.setExamId(exam.getExamId());

                        studentAssignmentInExamsList.add(studentAssignmentInExams);
                    }
                }
                int status = examService.assignExamsToStudents(studentAssignmentInExamsList);
                return status;
            }
        };
        return assignStudentsToExamTask;
    }

    /*--------------------------------------------------Room Allocation-----------------------------------------------*/

    /**
     * This method is used to get a task which can be used to create room allocation for the students.
     *
     * @param examDetails The details of the examination for which the room allocation will be created.
     * @return A list of RoomAllocation objects which contains information about the room allocation of a single
     * student.
     */
    public Task<Integer> getCreateRoomAllocationTask(ExamDetails examDetails) {

        Task<Integer> createRoomAllocationTask = new Task<>() {

            @Override
            protected Integer call() {

                //get the details about the classrooms
                List<Classroom> classroomList = classRoomService.getClassroomsData("");

                //create a list of RoomAllocation
                List<RoomAllocation> roomAllocationList = new ArrayList<>();

                //for each classroom, create a new roomAllocation object and initialize it
                for (Classroom classroom : classroomList) {

                    RoomAllocation roomAllocation = new RoomAllocation();

                    roomAllocation.setRoomNo(classroom.getRoomNo());
                    roomAllocation.setCapacity(classroom.getCapacity());
                    roomAllocation.setExamDetailsId(examDetails.getExamDetailsId());

                    roomAllocationList.add(roomAllocation);
                }

                //get the list of batches available in the db
                List<Batch> batchesList = batchService.getBatchData("");

                for (Batch batch : batchesList) {

                    String additionalQuery = "where v_batch_id=? " +
                            "ORDER BY v_reg_id";

                    //get the list of students for each batch
                    List<Student> studentListOfTheBatch = studentService.getStudentData
                            (additionalQuery, batch.getBatchId());

                    int currStudentIndex = 0;

                    for (RoomAllocation roomAllocation : roomAllocationList) {

                        List<String> allocIdList = roomAllocation.getRoomAllocationIdlist();
                        int currAllocId = 0;
                        while(allocIdList.contains(String.valueOf(currAllocId))){

                            currAllocId++;
                        }
                        int roomCapacity = Integer.parseInt(roomAllocation.getCapacity());

                        /*
                        For each room , ensure that the total students in the room is less than or equal to the capacity
                        of the room and put students of a batch in the seats alternatively.
                         */
                        while(roomAllocation.getStudentList().size() < roomCapacity
                                && currStudentIndex < studentListOfTheBatch.size()
                                && currAllocId < roomCapacity) {

                            allocIdList.add(String.valueOf(currAllocId));
                            currAllocId += 2;
                            Student student = studentListOfTheBatch.get(currStudentIndex++);

                            roomAllocation.getStudentList().add(student);
                        }

                        //all students of a batch have been allocated a seat, so now take a new batch
                        if (currStudentIndex >= studentListOfTheBatch.size()) {

                            break;
                        }
                    }
                }

                //save the room allocations details to the database
                int addRoomAllocationToDbStatus = examService.addRoomAllocationToDatabase(roomAllocationList);

                return addRoomAllocationToDbStatus;
            }
        };
        return createRoomAllocationTask;
    }

    /*----------------------------------------------End of Room Allocation--------------------------------------------*/


    /*------------------------------------------------Invigilation Duty-----------------------------------------------*/

    /**
     * This method is used to get a task which can be used to create invigilation duties for an examination.
     *
     * @return A task which can be used to create invigilation duties for the examination.
     */
    public Task<Integer> getCreateInvigilationDutyTask(ExamDetails examDetails) {

        Task<Integer> createInvigilationDutyTask = new Task<>() {

            @Override
            protected Integer call() {

                List<Professor> professorList = professorService.getProfessorData("");
                List<Exam> examRoutine = examService.getExamRoutine("WHERE v_exam_details_id=?"
                        , examDetails.getExamDetailsId());

                //true value in this array denotes a professor has been assigned an invigilation duty in the cycle
                boolean[] invigilatorsAssignment = new boolean[professorList.size()];

                List<InvigilationDuty> invigilationDutyList = new ArrayList<>();

                /*
                The data structure is as follows :
                Key : Exam date
                Value : Exams on that date
                 */
                Map<String, List<Exam>> examMap = new HashMap<>();

                for (Exam exam : examRoutine) {

                    examMap.putIfAbsent(exam.getExamDate(), new ArrayList<>());
                    examMap.get(exam.getExamDate()).add(exam);
                }

                /*
                This HashMap is used to count the no of duties assigned to each professor.
                The data structure is as follows :
                Key : Prof ID of the invigilator
                Value : Total no of duties assigned
                 */
                Map<String, Integer> invigilatorsDutyCountMap = new HashMap<>();

                for (Professor professor : professorList) {

                    invigilatorsDutyCountMap.put(professor.getProfId(), 0);
                }

                System.out.println("-----------------------------------------------------------------");
                System.out.println("\t\t\t\t\t\t\tExams");
                System.out.println("-----------------------------------------------------------------");
                System.out.println();

                for (Map.Entry<String, List<Exam>> entry : examMap.entrySet()) {

                    /*
                    true value in this array denotes a professor has been assigned an invigilation duty in the
                    current date
                     */
                    boolean[] invigilatorAssignedInTheCurrentDate = new boolean[professorList.size()];

                    String additionalQuery = "WHERE int_exam_id IN (" + entry.getValue().stream().map(Exam::getExamId)
                            .collect(Collectors.joining(",")) + ")";

                    //get the list of exams happening on the particular date
                    List<ExamsOnRoom> examsOnRoomList = examService.getExamsOnRoomData(additionalQuery);

                    System.out.println("-----------------------------------------------------------------");
                    System.out.println("Date : " + entry.getKey());
                    System.out.println("-----------------------------------------------------------------");
                    System.out.println();

                    for (ExamsOnRoom examsOnRoom : examsOnRoomList) {

                        System.out.println("Room No : " + examsOnRoom.getRoomNo());
                        System.out.println("Total Students : " + examsOnRoom.getStudentList().size());

                        for (Exam exam : examsOnRoom.getExamsList()) {

                            System.out.println("Exam : " + exam.getSubId());
                        }

                        System.out.println();
                        System.out.println();

                        //get total no of students giving exams in a room
                        int totalStudents = examsOnRoom.getStudentList().size();

                        //get the total of invigilator required for a room
                        int requiredInvigilators = getNoOfRequiredInvigilators(totalStudents);

                        //get the required invigilators and put them in the list
                        for (int i = 1; i <= requiredInvigilators; i++) {

                            InvigilationDuty invigilationDuty = new InvigilationDuty();

                            invigilationDuty.setProfId(getValidInvigilator(professorList, examsOnRoom
                                    , invigilatorsAssignment, invigilatorAssignedInTheCurrentDate));
                            invigilationDuty.setRoomNo(examsOnRoom.getRoomNo());
                            invigilationDuty.setExamDetailId(examsOnRoom.getExamDetailsId());
                            invigilationDuty.setExamDate(entry.getKey());

                            invigilationDutyList.add(invigilationDuty);
                        }

                    }
                }
                System.out.println();
                System.out.println("-----------------------------------------------------------------");
                System.out.println("\t\t\t\t\t\tInvigilation Duty");
                System.out.println("-----------------------------------------------------------------");
                System.out.format("%10s%10s%10s", "Exam Date", "Room No.", "Prof. ID");
                System.out.println();

                for (InvigilationDuty invigilationDuty : invigilationDutyList) {

                    System.out.format("%10s%10s%10s", invigilationDuty.getExamDate(), invigilationDuty.getRoomNo(),
                            invigilationDuty.getProfId());
                    System.out.println();
                    int count = invigilatorsDutyCountMap.get(invigilationDuty.getProfId()) + 1;
                    invigilatorsDutyCountMap.put(invigilationDuty.getProfId(), count);
                }

                System.out.println();
                System.out.println();
                System.out.println("-----------------------------------------------------------------");
                System.out.println("\t\t\tInvigilation Duty Count of each Invigilators");
                System.out.println("-----------------------------------------------------------------");

                for (Map.Entry<String, Integer> entry : invigilatorsDutyCountMap.entrySet()) {

                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }

                int addInvigilationDutyToDbStatus = examService.addInvigilationDutyToDatabase(invigilationDutyList);

                return addInvigilationDutyToDbStatus;
            }
        };
        return createInvigilationDutyTask;
    }

    /**
     * This method returns the total number of invigilators required.
     *
     * @param totalStudents The total no of students giving the exams for which invigilators are required.
     * @return The total number of Invigilators required.
     */
    private int getNoOfRequiredInvigilators(int totalStudents) {

        //the ratio is 10:1 i.e. for 10 students giving the exam, 1 invigilator is required
        return (int) Math.ceil(totalStudents / 10.0);
    }

    /**
     * This method returns the profId of a valid Invigilator.
     * A valid invigilator has to meet two criteria :
     * <p>
     * 1. He cannot invigilate for his own subjects he teach.
     * 2. Unless all invigilators have been assigned atleast 1 invigilation duty in the cycle he cannot be assigned
     * 2 invigilation duties in the same cycle.
     *
     * @param professorList        The list of professors from which a valid invigilator will be chosen.
     * @param examsOnRoom          This objects tells what exam is going on in the particular room.
     * @param invigilatorsAssigned Boolean array that stores whether an invigilator has been assigned duty or not.
     * @return A valid prof ID of an Invigilator.
     */
    private String getValidInvigilator(List<Professor> professorList, ExamsOnRoom examsOnRoom
            , boolean[] invigilatorsAssigned, boolean[] invigilatorsAssignedInTheDate) {

        String profId = null;

        //store the indices of the invigilators here
        List<Integer> invigilatorIndexList = new ArrayList<>();

        for (int i = 0; i < professorList.size(); i++) {

            invigilatorIndexList.add(i);
        }

        //randomly shuffle the invigilators indices
        Collections.shuffle(invigilatorIndexList);

        for (int i : invigilatorIndexList) {

            //get the professor of that index
            Professor professor = professorList.get(i);

            //check whether invigilation duty can be assigned to the professor
            if (!doInvigilatorTeachesTheSubjectOnExam(professor.getSubjects(), examsOnRoom.getExamsList())
                    && !invigilatorsAssigned[i] && !invigilatorsAssignedInTheDate[i]) {

                profId = professor.getProfId();

                //mark the professor as 'Assigned' for invigilation duty in the cycle
                invigilatorsAssigned[i] = true;

                //mark the professor as 'Assigned' for invigilation duty in a day
                invigilatorsAssignedInTheDate[i] = true;

                //if all invigilators have been assigned in the current cycle, reset the boolean array
                resetInvigilatorsAssigmentIfAllInvigilatorsAreAssigned(invigilatorsAssigned);

                break;
            }
        }

        /*
        If no invigilator is found , that means all the invigilators the current cycle is used and the invigilators who
        are left teaches the subject for which examination is taking place, so for that assign a random professor from
        another cycle who has no other duty on that particular day
         */
        if (profId == null) {

            Collections.shuffle(invigilatorIndexList);

            for (int i : invigilatorIndexList) {

                //get the professor of that index
                Professor professor = professorList.get(i);

                /*
                Check whether invigilation duty can be assigned to the professor in the same day and also the professor
                doesn't teach the subjects for which exams in going on
                 */

                if (!doInvigilatorTeachesTheSubjectOnExam(professor.getSubjects(), examsOnRoom.getExamsList())
                        && !invigilatorsAssignedInTheDate[i]) {

                    profId = professor.getProfId();

                    //mark the professor as 'Assigned' for invigilation duty on the day
                    invigilatorsAssignedInTheDate[i] = true;

                    break;
                }
            }
        }
        return profId;
    }

    /**
     * This method is used to check whether the Invigilator chosen, teaches any subject for which the exam is
     * going on.
     *
     * @param subjectList The list of subjects taught by the Invigilator.
     * @param examList    The list of exams which is taking place.
     * @return The result of the checking, if true that means the invigilator teaches any one or more subjects for
     * which exams are taking place.
     */
    private boolean doInvigilatorTeachesTheSubjectOnExam(List<Subject> subjectList, List<Exam> examList) {

        for (Subject subject : subjectList) {

            for (Exam exam : examList) {

                if (subject.getSubId().equals(exam.getSubId())) {

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method is used to reset the boolean array of invigilatorUsed if all values of the array is 1.
     * This means all the Invigilators has been assigned a single invigilator duty in the current cycle and now reset
     * the array so that more duties can be assigned to the invigilators in the next cycles.
     *
     * @param invigilatorsAssigned The boolean array.
     */
    private void resetInvigilatorsAssigmentIfAllInvigilatorsAreAssigned(boolean[] invigilatorsAssigned) {

        for (boolean i : invigilatorsAssigned) {

            if (!i) {

                return;
            }
        }
        Arrays.fill(invigilatorsAssigned, false);
    }

    /*------------------------------------------------Invigilation Duty-----------------------------------------------*/
}
