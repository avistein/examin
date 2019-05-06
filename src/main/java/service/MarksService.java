package service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Marks;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Service class to get marks of students, add marks and update marks of the students.
 *
 * @author Avik Sarkar
 */
public class MarksService {

    /*-------------------------------------------Declaration of variables---------------------------------------------*/

    private DatabaseHelper databaseHelper;

    /*-----------------------------------------------End of Declaration-----------------------------------------------*/

    /**
     * Public constructor to initialize variables.
     */
    public MarksService() {

        databaseHelper = new DatabaseHelper();
    }

    /**
     * This method is used to get a task object which can be used to get the marks details in a separate thread.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_student_marks.
     * @return A task object which can be used to get the marks details of the student in a separate thread.
     */
    public Task<List<Marks>> getMarksDataTask(String additionalQuery, String... params) {

        Task<List<Marks>> marksDataTask = new Task<>() {

            @Override
            protected List<Marks> call() {

                List<Marks> list = getMarksData(additionalQuery, params);

                //a list of Marks details
                return list;
            }
        };
        return marksDataTask;
    }

    /**
     * This method is used to get list of marks from the database.
     *
     * @return A list of marks from the database.
     */
    public List<Marks> getMarksData(String additionalQuery, String... params) {

        final String query = "SELECT v_reg_id, v_obtained_marks, v_sub_id, v_course_id, int_semester FROM " +
                "t_student_marks NATURAL JOIN t_subject " + additionalQuery;

        Map<String, List<String>> map = databaseHelper.execQuery(query, params);

        //each item in the list is a single Marks details
        List<Marks> list = new ArrayList<>();

        /*
        v_reg_id is the primary key, total items in the map will always be equal to no of v_reg_id retrieved
        */
        for (int i = 0; i < map.get("v_reg_id").size(); i++) {

            Marks marks = new Marks();

            marks.setRegId(map.get("v_reg_id").get(i));
            marks.setCourseId(map.get("v_course_id").get(i));
            marks.setSubId(map.get("v_sub_id").get(i));
            if (!map.get("v_obtained_marks").get(i).equals("TBC")) {

                marks.setObtainedMarks(map.get("v_obtained_marks").get(i));
            }
            marks.setSemester(map.get("int_semester").get(i));

            //a single marks details is added to the list
            list.add(marks);
        }

        //a list of marks details
        return list;
    }

    /**
     * This method is used to get a task object which can be used to add marks to the DB in a separate thread.
     *
     * @param marks The marks of the student to be added to the database.
     * @return Status of the operation i.e. success / failure.
     */
    public Task<Integer> getAddMarksToDataBaseTask(Marks marks) {
        Task<Integer> addMarksToDataBaseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "UPDATE t_student_marks SET v_obtained_marks=? WHERE v_course_id=? AND v_sub_id=? " +
                        "AND v_reg_id=?";


                /*get the no of insertions or error status of the INSERT operation*/
                int tStudentMarksStatus = databaseHelper.updateDelete(sql, marks.getObtainedMarks(), marks.getCourseId()
                        , marks.getSubId(), marks.getRegId());

                //if any DB error is present
                if (tStudentMarksStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tStudentMarksStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_ALREADY_EXIST_ERROR;
                }
            }
        };
        return addMarksToDataBaseTask;
    }

    /**
     * This method is used to get a task which can be used to load the contents of marks csv to the memory in a separate
     * thread.
     *
     * @param file The csv file to load into the memory.
     * @param map  The
     * @return A task which can be used to get the list of marks from the csv.
     */
    public Task<List<Marks>> getLoadMarksFromCsvToMemoryTask(final File file, final Map<String, String> map) {

        Task<List<Marks>> loadMarksFromCsvToMemoryTask = new Task<>() {

            @Override
            protected List<Marks> call() {

                Map<String, String> columnNameMapping = new HashMap<>();

                //each item in the list is a Marks details obtained from the CSV
                List<Marks> listOfMarksFromCsv = new ArrayList<>();

                /*
                Whatever be the column names, those will be mapped to the data of the Marks Bean
                 */
                columnNameMapping.put(map.get("regId"), "regId");
                columnNameMapping.put(map.get("obtainedMarks"), "obtainedMarks");

                /*
                Maps data to objects using the column names in the first row of the CSV file as reference.
                This way the column order does not matter.
                @see for more.
                 */
                HeaderColumnNameTranslateMappingStrategy<Marks> strategy = new
                        HeaderColumnNameTranslateMappingStrategy<>();
                strategy.setType(Marks.class);
                strategy.setColumnMapping(columnNameMapping);

                //open the CSV file for parsing
                try (CSVReader reader = new CSVReader(new FileReader(file))) {

                    //skip the first line of the csv as it contains the column names
                    CsvToBean<Marks> csvToBean = new CsvToBeanBuilder<Marks>(reader)
                            .withMappingStrategy(strategy).withSkipLines(1).build();

                    //parse the csv and store list of Marks objects
                    listOfMarksFromCsv = csvToBean.parse();
                } catch (IOException e) {

                    e.printStackTrace();
                }

                //list of Marks objects
                return listOfMarksFromCsv;
            }
        };
        return loadMarksFromCsvToMemoryTask;
    }

    /**
     * This method can be used to get a task to add marks from the memory to database.
     *
     * @param list The ArrayList containing the marks from the csv.
     * @return A task which can be used to add list of marks to database.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getAddMarksFromMemoryToDataBaseTask(List<Marks> list, String courseId, String subId) {
        Task<Integer> addMarksFromMemoryToDataBaseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "UPDATE t_student_marks SET v_obtained_marks=? WHERE v_course_id=? AND v_sub_id=? " +
                        "AND v_reg_id=?";

                /*
                Each item in the list is itself a list of strings ;in the inner list each item is the data
                of the Marks bean.
                For example if two Marks objects are marksObj1 and marksObj2 and the structure is as follows :

                marksObj1 = obtainedMarks : 90, courseId : CSE, subId : CS401, regId : 23900115011;
                marksObj2 = obtainedMarks : 60, courseId : CSE, subId : CS201, regId : 23900115011;

                Then, marksList will be stored as :

                marksList = {{"90", "CSE", "CS401", "23900115011"}, {"60", "CSE" , "CS201", "23900115011"}}
                */
                List<List<String>> marksList = new ArrayList<>();

                for (Marks marks : list) {

                    List<String> singleMarksDetails = new ArrayList<>();

                    singleMarksDetails.add(marks.getObtainedMarks());
                    singleMarksDetails.add(courseId);
                    singleMarksDetails.add(subId);
                    singleMarksDetails.add(marks.getRegId());

                    //add obtainedMarks, courseId, subId, regId
                    marksList.add(singleMarksDetails);
                }

                /*get the no of insertions or error status of the INSERT operation*/
                int tStudentMarksStatus = databaseHelper.batchInsertUpdateDelete(sql, marksList);

                //if any DB error is present
                if (tStudentMarksStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                }

                //return success ,if all marks data are inserted
                else if (tStudentMarksStatus == marksList.size()) {

                    return SUCCESS;
                }

                //return the no of marks data inserted
                else {

                    return DATA_ALREADY_EXIST_ERROR;
                }
            }
        };
        return addMarksFromMemoryToDataBaseTask;
    }
}
