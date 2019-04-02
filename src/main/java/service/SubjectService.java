package service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Course;
import model.Subject;
import model.SubjectAllocation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Service class to get all subjects from the Db, add subjects, delete subjects , assign subject to professor, delete
 * assignment of subject.
 *
 * @author Avik Sarkar
 */
public class SubjectService {

    private DatabaseHelper databaseHelper;

    public SubjectService() {
        databaseHelper = new DatabaseHelper();
    }

    /**
     * This method is used to get a single subjectTask object which is used to get subject .
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_subject.
     * @return A subjectTask which can be used to get a list of subjects from the DB in a separate thread.
     */
    public Task<List<Subject>> getSubjectsTask(String additionalQuery, final String... params) {

        Task<List<Subject>> subjectTask = new Task<>() {
            @Override
            protected List<Subject> call() {

                List<Subject> list = getSubjectData(additionalQuery, params);

                return list;
            }
        };
        return subjectTask;
    }

    /**
     * Similar to {@link #getSubjectsTask(String, String...)} except that it is used by Service classes.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_subject.
     * @return List of subjects in the DB.
     */
    public List<Subject> getSubjectData(String additionalQuery, String... params) {

        //list to store the subjects
        List<Subject> list = new ArrayList<>();

        final String query = "SELECT v_course_id, v_degree, v_discipline, v_sub_id, v_sub_name, v_credit, int_semester" +
                ", v_full_marks, v_sub_type from t_subject natural join t_course" + additionalQuery;

        Map<String, List<String>> map = databaseHelper.execQuery(query, params);

        for (int i = 0; i < map.get("v_sub_id").size(); i++) {

            Subject subject = new Subject();
            subject.setCourseId(map.get("v_course_id").get(i));
            subject.setSubId(map.get("v_sub_id").get(i));
            subject.setSubName(map.get("v_sub_name").get(i));
            subject.setCredit(map.get("v_credit").get(i));
            subject.setSemester(map.get("int_semester").get(i));
            subject.setFullMarks(map.get("v_full_marks").get(i));
            subject.setSubType(map.get("v_sub_type").get(i));
            subject.setDegree(map.get("v_degree").get(i));
            subject.setDiscipline(map.get("v_discipline").get(i));

            //add a single subject to the list
            list.add(subject);
        }
        return list;
    }

    /**
     * This method is used to get a task which can be used to add a single subject to the DB.
     *
     * @param subject The subject to be added to the database.
     * @return A task which can be used to add a single subject into the DB by running a separate thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getAddSubjectToDatabaseTask(final Subject subject) {

        Task<Integer> addSubjectToDatabaseTask = new Task<>() {

            @Override
            protected Integer call() {

                CourseService courseService = new CourseService();

                final String additionalQuery = "WHERE v_degree=? AND v_discipline=?";

                String courseId = courseService.getCourseData(additionalQuery, subject.getDegree()
                        , subject.getDiscipline()).get(0).getCourseId();

                final String sql = "INSERT INTO t_subject(v_course_id, v_sub_id, v_sub_name, v_credit" +
                        ", int_semester, v_full_marks, v_sub_type) VALUES(?, ?, ?, ?, ?, ?, ?)";

                //get the status of insertion of subject details into t_subject in the DB
                int tSubjectStatus = databaseHelper.insert(sql, courseId, subject.getSubId()
                        , subject.getSubName(), subject.getCredit(), subject.getSemester()
                        , subject.getFullMarks(), subject.getSubType());

                //return the status of insertion of student details
                if (tSubjectStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tSubjectStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_ALREADY_EXIST_ERROR;
                }

            }
        };
        return addSubjectToDatabaseTask;
    }

    /**
     * This method is used to get a updateSubjectTask which is used to edit a single subject in the DB.
     *
     * @param subject The course to be edited.
     * @return A updateSubjectTask instance which is used to edit a single subject in the DB in a separate thread.
     */
    public Task<Integer> getUpdateSubjectTask(final Subject subject) {

        Task<Integer> updateSubjectTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "UPDATE t_subject SET v_sub_name=?, v_credit=?" +
                        ", int_semester=?, v_full_marks=?, v_sub_type=? WHERE v_sub_id=?";

                //holds the status of updation of subject in the DB, i.e success or failure
                int tSubjectStatus = databaseHelper.updateDelete(sql, subject.getSubName(), subject.getCredit()
                        , subject.getSemester(), subject.getFullMarks(), subject.getSubType(), subject.getSubId());


                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tSubjectStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tSubjectStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return updateSubjectTask;
    }

    /**
     * This method is used to get a deleteSubjectTask which is used to delete a single subject in the DB.
     *
     * @param param Subject Id of the subject to be deleted.
     * @return A deleteSubjectTask instance which is used to delete a single subject in the DB in a separate thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getDeleteSubjectTask(final String param) {

        Task<Integer> deleteSubjectTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "DELETE FROM t_subject WHERE v_sub_id=?";

                //holds the status of deletion of subject in the DB, i.e success or failure
                int tSubjectStatus = databaseHelper.updateDelete(sql, param);

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tSubjectStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tSubjectStatus == SUCCESS) {

                    return SUCCESS;
                } else if (tSubjectStatus == DATA_DEPENDENCY_ERROR) {

                    return DATA_DEPENDENCY_ERROR;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return deleteSubjectTask;
    }

    /**
     * This method is used to get a single subjectsCountTask object which is used to get total no of Subjects in
     * the DB.
     *
     * @return A subjectsCountTask object which is used to get the total no. of Subjects in the DB in a separate
     * thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getSubjectsCountTask() {

        final String query = "SELECT DISTINCT v_sub_id FROM t_subject";

        Task<Integer> subjectsCountTask = new Task<>() {

            @Override
            protected Integer call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query);

                /*
                total count will always be equal to the no of unique v_sub_id retrieved
                 */
                return map.get("v_sub_id").size();
            }
        };
        return subjectsCountTask;
    }

    /**
     * This method can be used to get a task to load a bunch of Subject Allocation from the CSV file into the memory.
     *
     * @param file The CSV file which contains a bunch of Subject Allocation details.
     * @param map  A hashMap with data of SubjectAllocation bean as keys and the column names of the CSV file as values.
     *             The first line of the CSV contains the column names.
     * @return A task which can be used to retrieve Subject Allocations list , which is loaded from the CSV file.
     * @see <a href="http://opencsv.sourceforge.net/apidocs/com/opencsv/bean/HeaderColumnNameMappingStrategy.html">
     * OpenCSV HeaderColumnNameMappingStrategy</a>
     */
    @SuppressWarnings("Duplicates")
    public Task<List<SubjectAllocation>> getLoadSubjectAllocationFromCsvToMemoryTask(final File file
            , final Map<String, String> map) {

        Task<List<SubjectAllocation>> loadSubjectAllocationFromCsvToMemoryTask = new Task<>() {

            @Override
            protected List<SubjectAllocation> call() {

                Map<String, String> columnNameMapping = new HashMap<>();

                //each item in the list is a Subject Allocation details obtained from the CSV
                List<SubjectAllocation> listOfSubjectAlloationsFromCsv = new ArrayList<>();

                /*
                Whatever be the column names, those will be mapped to the data of the SubjectAllocation Bean
                 */
                columnNameMapping.put(map.get("profId"), "profId");
                columnNameMapping.put(map.get("degree"), "degree");
                columnNameMapping.put(map.get("discipline"), "discipline");
                columnNameMapping.put(map.get("subId"), "subId");
                columnNameMapping.put(map.get("subName"), "subName");

                /*
                Maps data to objects using the column names in the first row of the CSV file as reference.
                This way the column order does not matter.
                @see for more.
                 */
                HeaderColumnNameTranslateMappingStrategy<SubjectAllocation> strategy = new
                        HeaderColumnNameTranslateMappingStrategy<>();
                strategy.setType(SubjectAllocation.class);
                strategy.setColumnMapping(columnNameMapping);

                //open the CSV file for parsing
                try (CSVReader reader = new CSVReader(new FileReader(file))) {

                    //skip the first line of the csv as it contains the column names
                    CsvToBean<SubjectAllocation> csvToBean = new CsvToBeanBuilder<SubjectAllocation>(reader)
                            .withMappingStrategy(strategy).withSkipLines(1).build();

                    //parse the csv and store list of SubjectAllocation objects
                    listOfSubjectAlloationsFromCsv = csvToBean.parse();
                } catch (IOException e) {

                    e.printStackTrace();
                }

                //list of SubjectAllocation objects
                return listOfSubjectAlloationsFromCsv;
            }
        };
        return loadSubjectAllocationFromCsvToMemoryTask;
    }

    /**
     * This method can be used to get a task to add list of SubjectAllocations to database.
     *
     * @param list The ArrayList containing the SubjectAllocations.
     * @return A task which can be used to add list of SubjectAllocations to database.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getAddSubAllocFromMemoryToDbTask(List<SubjectAllocation> list) {

        Task<Integer> addSubAllocFromMemoryToDbTask = new Task<>() {

            @Override
            protected Integer call() {

                CourseService courseService = new CourseService();

                final String sql1 = "INSERT INTO t_prof_sub (v_prof_id, v_course_id, v_sub_id) values(?, ?, ?)";

                /*
                Each item in the list is itself a list of strings ;in the inner list each item is the data
                of the SubjectAllocation bean.
                For example if two SubjectAllocation objects are subAllocObj1 and subAllocObj2 and the structure is
                as follows :

                subAllocObj1 = subId : 1, subName : "C", degree : B.Tech, discipline : CSE, profId : 1;
                subAllocObj2 = subId : 2, subName : "Java", degree : B.Tech, discipline : CSE, profId : 2;

                Then, listOfSubjectAllocations will be stored as :

                listOfSubjectAllocations = {{"1","C", "B.Tech", "CSE", "1"}, {"2", "Java" , "B.Tech" , "CSE", "2"}};
                 */
                List<List<String>> listOfSubjectAllocations = new ArrayList<>();

                //for each student ,form the data in the List<List<SubjectAllocation>> structure
                for (SubjectAllocation subjectAllocation : list) {

                    List<String> singleSubjectAllocation = new ArrayList<>();

                    List<Course> course = courseService.getCourseData
                            ("where v_degree=? and v_discipline=?"
                                    , subjectAllocation.getDegree(), subjectAllocation.getDiscipline());

                    singleSubjectAllocation.add(subjectAllocation.getProfId());
                    singleSubjectAllocation.add(course.get(0).getCourseId());
                    singleSubjectAllocation.add(subjectAllocation.getSubId());

                    //add details of a particular Subject Allocation into the list
                    listOfSubjectAllocations.add(singleSubjectAllocation);
                }

                /*get the no of insertions or error status of the INSERT operation in the t_prof_sub table in the DB*/
                int tProfSubStatus = databaseHelper.batchInsert(sql1, listOfSubjectAllocations);

                //if any DB error is present
                if (tProfSubStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                }

                //return success ,if all Subject Allocations are inserted
                else if (tProfSubStatus == listOfSubjectAllocations.size()) {

                    return SUCCESS;
                }

                //return the no of Subject Allocations inserted
                else {

                    return tProfSubStatus;
                }
            }
        };
        return addSubAllocFromMemoryToDbTask;
    }

    /**
     * This method is used to get a single subjectAllocTask object which is used to get subject allocation.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_prof_sub.
     * @return A subjectAllocTask which can be used to get a list of subject allocations from the DB in a separate
     * thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<List<SubjectAllocation>> getSubAllocTask(String additionalQuery, final String... params) {

        final String query = "SELECT v_course_id, v_sub_id, v_prof_id from t_prof_sub " + additionalQuery;

        Task<List<SubjectAllocation>> subjectAllocTask = new Task<>() {
            @Override
            protected List<SubjectAllocation> call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                List<SubjectAllocation> list = new ArrayList<>();

                for (int i = 0; i < map.get("v_sub_id").size(); i++) {

                    SubjectAllocation subjectAllocation = new SubjectAllocation();

                    subjectAllocation.setCourseId(map.get("v_course_id").get(i));
                    subjectAllocation.setSubId(map.get("v_sub_id").get(i));
                    subjectAllocation.setProfId(map.get("v_prof_id").get(i));

                    list.add(subjectAllocation);
                }
                return list;
            }
        };
        return subjectAllocTask;
    }

    /**
     * This method can be used to get a task to add a single SubjectAllocation to database.
     *
     * @param subjectAllocation The SubjectAllocation entry to add to the DB..
     * @return A task which can be used to add a SubjectAllocation to database.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getAddSubAllocTask(SubjectAllocation subjectAllocation) {

        Task<Integer> addSubAllocTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "INSERT INTO t_prof_sub (v_prof_id, v_course_id, v_sub_id) values(?, ?, ?)";

                /*get the successful or error status of the INSERT operation in the t_prof_sub table in the DB*/
                int tProfSubStatus = databaseHelper.insert(sql, subjectAllocation.getProfId()
                        , subjectAllocation.getCourseId(), subjectAllocation.getSubId());

                //if any DB error is present
                if (tProfSubStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                }

                //return success if successfully inserted to the db
                else if (tProfSubStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_ALREADY_EXIST_ERROR;
                }
            }
        };
        return addSubAllocTask;
    }

    /**
     * This method can be used to get a task to delete a single SubjectAllocation to database.
     *
     * @param subjectAllocation The SubjectAllocation entry to delete from the DB.
     * @return A task which can be used to delete a SubjectAllocation from database.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getDeleteSubAllocTask(SubjectAllocation subjectAllocation) {

        Task<Integer> deleteSubAllocTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "DELETE FROM t_prof_sub WHERE v_prof_id=? AND v_course_id=? AND v_sub_id=?";

                /*get the successful or error status of the DELETE operation in the t_prof_sub table in the DB*/
                int tProfSubStatus = databaseHelper.updateDelete(sql, subjectAllocation.getProfId()
                        , subjectAllocation.getCourseId(), subjectAllocation.getSubId());

                //if any DB error is present
                if (tProfSubStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                }

                //return success if successfully deleted from the db
                else if (tProfSubStatus == SUCCESS) {

                    return SUCCESS;
                } else if (tProfSubStatus == DATA_DEPENDENCY_ERROR) {

                    return DATA_DEPENDENCY_ERROR;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return deleteSubAllocTask;
    }
}
