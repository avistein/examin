package service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Holiday;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Service class to get Holidays, delete holiday, import Holidays from csv.
 *
 * @author Avik Sarkar
 */
public class HolidayService {

    /*--------------------------------Declaration of variables---------------------------------------*/

    private DatabaseHelper databaseHelper;

    /*------------------------------------End of Declaration-----------------------------------------*/

    /**
     * Default public constructor to initialize database helper.
     */
    public HolidayService() {

        databaseHelper = new DatabaseHelper();
    }

    /**
     * This method is used to get a single holidaysTask object which is used to get holidays details.
     *
     * @return A holidaysTask which can be used to get a list of Holidays details from the DB in a separate
     * thread.
     */
    public Task<List<Holiday>> getHolidaysTask() {

        final String query = "SELECT int_holiday_id, v_holiday_name, d_start_date, d_end_date " +
                "from t_holiday_details";

        Task<List<Holiday>> holidaysTask = new Task<>() {

            @Override
            protected List<Holiday> call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query);

                //each item in the list is a single Holiday details
                List<Holiday> list = new ArrayList<>();

                /*
                int_holiday_id is the primary key, total items in the map will always be equal to no of
                int_holiday_id retrieved
                 */
                for (int i = 0; i < map.get("int_holiday_id").size(); i++) {

                    Holiday holiday = new Holiday();

                    holiday.setHolidayId(map.get("int_holiday_id").get(i));
                    holiday.setHolidayName(map.get("v_holiday_name").get(i));
                    holiday.setStartDate(map.get("d_start_date").get(i));
                    holiday.setEndDate(map.get("d_end_date").get(i));

                    //a single holiday details is added to the list
                    list.add(holiday);
                }

                //a list of Department details
                return list;
            }
        };
        return holidaysTask;
    }

    /**
     * This method can be used to get a task to load a bunch of Holidays from the CSV file into the memory.
     *
     * @param file The CSV file which contains a bunch of Holiday details.
     * @param map  A hashMap with data of Holiday bean as keys and the column names of the CSV file as values.
     *             The first line of the CSV contains the column names.
     * @return A task which can be used to retrieve holidays list , which is loaded from the CSV file.
     * @link http://opencsv.sourceforge.net/apidocs/com/opencsv/bean/HeaderColumnNameMappingStrategy.html
     */
    @SuppressWarnings("Duplicates")
    public Task<List<Holiday>> getLoadHolidaysFromCsvToMemoryTask(final File file, final Map<String, String> map) {

        Task<List<Holiday>> loadHolidaysFromCsvToMemoryTask = new Task<>() {

            @Override
            protected List<Holiday> call() {

                Map<String, String> columnNameMapping = new HashMap<>();

                //each item in the list is a holiday details obtained from the CSV
                List<Holiday> listOfHolidaysFromCsv = new ArrayList<>();

                /*
                Whatever be the column names, those will be mapped to the data of the Holiday Bean
                 */
                columnNameMapping.put(map.get("holidayId"), "holidayId");
                columnNameMapping.put(map.get("holidayName"), "holidayName");
                columnNameMapping.put(map.get("startDate"), "startDate");
                columnNameMapping.put(map.get("endDate"), "endDate");

                /*
                Maps data to objects using the column names in the first row of the CSV file as reference.
                This way the column order does not matter.
                see @link for more.
                 */
                HeaderColumnNameTranslateMappingStrategy<Holiday> strategy = new
                        HeaderColumnNameTranslateMappingStrategy<>();
                strategy.setType(Holiday.class);
                strategy.setColumnMapping(columnNameMapping);

                //open the CSV file for parsing
                try (CSVReader reader = new CSVReader(new FileReader(file))) {

                    //skip the first line of the csv as it contains the column names
                    CsvToBean<Holiday> csvToBean = new CsvToBeanBuilder<Holiday>(reader)
                            .withMappingStrategy(strategy).withSkipLines(1).build();

                    //parse the csv and store list of Holiday objects
                    listOfHolidaysFromCsv = csvToBean.parse();
                } catch (IOException e) {

                    e.printStackTrace();
                }

                //list of holiday objects
                return listOfHolidaysFromCsv;
            }
        };
        return loadHolidaysFromCsvToMemoryTask;
    }

    /**
     * This method can be used to get a task to add list of Holidays to database.
     *
     * @param list The ArrayList containing the holidays.
     * @return A task which can be used to add list of Holidays to database.
     */
    public Task<Integer> getAddHolidayFromMemoryToDataBaseTask(List<Holiday> list) {

        Task<Integer> addHolidayFromMemoryToDataBaseTask = new Task<>() {

            @Override
            protected Integer call() {

                final String sql = "INSERT INTO t_holiday_details (int_holiday_id, v_holiday_name" +
                        ", d_start_date, d_end_date) values(?, ?, ?, ?)";

                /*
                Each item in the list is itself a list of strings ;in the inner list each item is the data
                of the Holiday bean.
                For example if two holiday objects are holidayObj1 and holidayObj2 and the structure is as follows :

                holidayObj1 = holidayId : 1, holidayName : Kali Puja, startDate : 2019-10-27, endDate : 2019-10-27;
                holidayObj2 = holidayId : 2, holidayName : Laxmi Puja, startDate : 2019-10-13, endDate : 2019-10-13;

                Then, listOfHolidays will be stored as :

                listOfHolidays = {{"1","Kali Puja", "2019-10-27", "2019-10-27"}, {"2", "Laxmi Puja" , "2019-10-13" ,
                 "2019-10-13"}}
                 */
                List<List<String>> listOfHolidays = new ArrayList<>();


                for (Holiday holiday : list) {

                    List<String> singleHoliday = new ArrayList<>();

                    singleHoliday.add(holiday.getHolidayId());
                    singleHoliday.add(holiday.getHolidayName());
                    singleHoliday.add(holiday.getStartDate());
                    singleHoliday.add(holiday.getEndDate());

                    //a single Holiday details is added to the list
                    listOfHolidays.add(singleHoliday);
                }

                //status which denotes successful or failed insertion of data
                int tHolidayDetailsStatus = databaseHelper.batchInsert(sql, listOfHolidays);

                /*
                Returns a status which indicates success or failure in data insertion to the DB.
                 */
                if (tHolidayDetailsStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tHolidayDetailsStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_ALREADY_EXIST_ERROR;
                }
            }
        };
        return addHolidayFromMemoryToDataBaseTask;
    }

    /**
     * This method is used to get a deleteDepartmentTask which is used to delete a single department in the DB.
     *
     * @param holiday The Holiday item to be deleted.
     * @return A deleteHolidayTask instance which is used to delete a single holiday in the DB in a separate
     * thread.
     */
    @SuppressWarnings("Duplicates")
    public Task<Integer> getDeleteHolidayTask(final Holiday holiday) {

        final String sql = "DELETE FROM t_holiday_details where int_holiday_id=?";

        Task<Integer> deleteHolidayTask = new Task<>() {

            @Override
            protected Integer call() {

                //holds the status of deletion of Holiday in the DB, i.e success or failure
                int tHolidayDetailsStatus = databaseHelper.updateDelete
                        (sql, holiday.getHolidayId());

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tHolidayDetailsStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tHolidayDetailsStatus == SUCCESS) {

                    return SUCCESS;
                } else if (tHolidayDetailsStatus == DATA_DEPENDENCY_ERROR) {

                    return DATA_DEPENDENCY_ERROR;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return deleteHolidayTask;
    }
}
