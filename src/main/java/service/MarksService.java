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

public class MarksService {

    private DatabaseHelper databaseHelper;

    public MarksService(){

        databaseHelper = new DatabaseHelper();
    }

    public Task<List<Marks>> getMarksDataTask(String additionalQuery, String ...params) {

        Task<List<Marks>> marksDataTask = new Task<>() {

            @Override
            protected List<Marks> call() {

                List<Marks> list = getMarksData(additionalQuery, params);

                //a list of Holiday details
                return list;
            }
        };
        return marksDataTask;
    }

    /**
     * This method is used to get a single holidaysTask object which is used to get holidays details.
     *
     * @return A holidaysTask which can be used to get a list of Holidays details from the DB in a separate
     * thread.
     */
    public List<Marks> getMarksData(String additionalQuery, String ...params) {

        final String query = "SELECT v_reg_id, int_obtained_marks FROM t_student_marks " + additionalQuery;


        Map<String, List<String>> map = databaseHelper.execQuery(query, params);

        //each item in the list is a single Holiday details
        List<Marks> list = new ArrayList<>();

                /*
                int_holiday_id is the primary key, total items in the map will always be equal to no of
                int_holiday_id retrieved
                 */
        for (int i = 0; i < map.get("v_reg_id").size(); i++) {

            Marks marks = new Marks();

            marks.setRegId(map.get("v_reg_id").get(i));
            marks.setObtainedMarks(map.get("int_obtained_marks").get(i));

            //a single holiday details is added to the list
            list.add(marks);
        }

        //a list of Department details
        return list;
    }

    public Task<Integer> getAddMarksToDataBaseTask(Marks marks) {
        Task<Integer> addMarksToDataBaseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "UPDATE t_student_marks SET int_obtained_marks=? WHERE int_exam_id=? AND v_reg_id=?";


                /*get the no of insertions or error status of the INSERT operation*/
                int tStudentMarksStatus = databaseHelper.updateDelete(sql, marks.getObtainedMarks(), marks.getExamId(),
                        marks.getRegId());

                //if any DB error is present
                if (tStudentMarksStatus == DATABASE_ERROR ) {

                    return DATABASE_ERROR;
                }

                //return success ,if all professors are inserted
                else if (tStudentMarksStatus == SUCCESS) {

                    return SUCCESS;
                }

                //return the no of professor inserted
                else {

                    return DATA_ALREADY_EXIST_ERROR;
                }
            }
        };
        return addMarksToDataBaseTask;
    }


    public Task<List<Marks>> getLoadMarksFromCsvToMemoryTask(final File file, final Map<String, String> map) {

        Task<List<Marks>> loadMarksFromCsvToMemoryTask = new Task<>() {

            @Override
            protected List<Marks> call() {

                Map<String, String> columnNameMapping = new HashMap<>();

                //each item in the list is a Professor details obtained from the CSV
                List<Marks> listOfMarksFromCsv = new ArrayList<>();

                /*
                Whatever be the column names, those will be mapped to the data of the Professor Bean
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

                    //parse the csv and store list of Professor objects
                    listOfMarksFromCsv = csvToBean.parse();
                } catch (IOException e) {

                    e.printStackTrace();
                }

                //list of Professor objects
                return listOfMarksFromCsv;
            }
        };
        return loadMarksFromCsvToMemoryTask;
    }

    /**
     * This method can be used to get a task to add list of Professors to database.
     *
     * @param list The ArrayList containing the Professors.
     * @return A task which can be used to add list of Professors to database.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getAddMarksFromMemoryToDataBaseTask(List<Marks> list, String examId) {
        Task<Integer> addMarksFromMemoryToDataBaseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "UPDATE t_student_marks SET int_obtained_marks=? WHERE int_exam_id=? AND v_reg_id=?";

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
                List<List<String>> marksList = new ArrayList<>();

                for (Marks marks : list) {

                    List<String> singleMarksDetails = new ArrayList<>();

                    singleMarksDetails.add(marks.getObtainedMarks());
                    singleMarksDetails.add(examId);
                    singleMarksDetails.add(marks.getRegId());

                    //add profId,deptName,hodStatus of a particular professor into the list
                    marksList.add(singleMarksDetails);
                }

                /*get the no of insertions or error status of the INSERT operation*/
                int tStudentMarksStatus = databaseHelper.batchInsertUpdate(sql, marksList);

                //if any DB error is present
                if (tStudentMarksStatus == DATABASE_ERROR ) {

                    return DATABASE_ERROR;
                }

                //return success ,if all professors are inserted
                else if (tStudentMarksStatus == marksList.size()) {

                    return SUCCESS;
                }

                //return the no of professor inserted
                else {

                    return DATA_ALREADY_EXIST_ERROR;
                }
            }
        };
        return addMarksFromMemoryToDataBaseTask;
    }
}
