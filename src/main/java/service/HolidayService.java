package service;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.Holiday;
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
 *
 * @author Avik Sarkar
 */
public class HolidayService {

    private DatabaseHelper databaseHelper;

    public HolidayService() {

        databaseHelper = new DatabaseHelper();
    }

    public Task<List<Holiday>> getHolidaysTask() {

        final String query = "SELECT int_holiday_id, v_holiday_name, d_start_date, d_end_date " +
                "from t_holiday_details";

        Task<List<Holiday>> holidaysTask = new Task<>() {
            @Override
            protected List<Holiday> call() {
                Map<String, List<String>> map = databaseHelper.execQuery(query);
                List<Holiday> list = new ArrayList<>();
                for (int i = 0; i < map.get("int_holiday_id").size(); i++) {

                    Holiday holiday = new Holiday();
                    holiday.setHolidayId(map.get("int_holiday_id").get(i));
                    holiday.setHolidayName(map.get("v_holiday_name").get(i));
                    holiday.setStartDate(map.get("d_start_date").get(i));
                    holiday.setEndDate(map.get("d_end_date").get(i));

                    list.add(holiday);
                }
                return list;
            }
        };
        return holidaysTask;
    }

    public Task<List<Holiday>> getLoadHolidaysFromCsvToMemoryTask(final File file, final Map<String, String> map) {

        Task<List<Holiday>> loadHolidaysFromCsvToMemoryTask = new Task<>() {
            @Override
            protected List<Holiday> call() throws Exception {

                Map<String, String> columnNameMapping = new HashMap<>();

                List<Holiday> listOfHolidaysFromCsv = new ArrayList<>();

                columnNameMapping.put(map.get("holidayId"), "holidayId");
                columnNameMapping.put(map.get("holidayName"), "holidayName");
                columnNameMapping.put(map.get("startDate"), "startDate");
                columnNameMapping.put(map.get("endDate"), "endDate");

                HeaderColumnNameTranslateMappingStrategy<Holiday> strategy = new
                        HeaderColumnNameTranslateMappingStrategy<>();
                strategy.setType(Holiday.class);
                strategy.setColumnMapping(columnNameMapping);

                try (CSVReader reader = new CSVReader(new FileReader(file))) {

                    CsvToBean<Holiday> csvToBean = new CsvToBeanBuilder<Holiday>(reader)
                            .withMappingStrategy(strategy).withSkipLines(1).build();
                    listOfHolidaysFromCsv = csvToBean.parse();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return listOfHolidaysFromCsv;
            }
        };
        return loadHolidaysFromCsvToMemoryTask;
    }

    public Task<Integer> getAddHolidayFromMemoryToDataBaseTask(List<Holiday> list) {

        Task<Integer> addHolidayFromMemoryToDataBaseTask = new Task<>() {
            @Override
            protected Integer call() {

                final String sql = "INSERT INTO t_holiday_details (int_holiday_id, v_holiday_name" +
                        ", d_start_date, d_end_date) values(?, ?, ?, ?)";

                List<List<String>> listOfHolidays = new ArrayList<>();
                for (Holiday holiday : list) {

                    List<String> singleHoliday = new ArrayList<>();

                    singleHoliday.add(holiday.getHolidayId());
                    singleHoliday.add(holiday.getHolidayName());
                    singleHoliday.add(holiday.getStartDate());
                    singleHoliday.add(holiday.getEndDate());

                    listOfHolidays.add(singleHoliday);

                }

                int tHolidayDetailsStatus = databaseHelper.batchInsert(sql, listOfHolidays);

                if (tHolidayDetailsStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if (tHolidayDetailsStatus == SUCCESS)
                    return SUCCESS;
                else
                    return DATA_ALREADY_EXIST_ERROR;
            }
        };
        return addHolidayFromMemoryToDataBaseTask;
    }


    public Task<Integer> getDeleteHolidayTask(final Holiday holiday){

        final String sql = "DELETE FROM t_holiday_details where int_holiday_id=?";

        Task<Integer> deleteHolidayTask = new Task<>() {
            @Override
            protected Integer call()  {

                int tHolidayDetailsStatus = databaseHelper.updateDelete
                        (sql, holiday.getHolidayId());

                if(tHolidayDetailsStatus == DATABASE_ERROR)
                    return DATABASE_ERROR;
                else if(tHolidayDetailsStatus == SUCCESS)
                    return SUCCESS;
                else if(tHolidayDetailsStatus == DATA_DEPENDENCY_ERROR)
                    return DATA_DEPENDENCY_ERROR;
                else
                    return DATA_INEXISTENT_ERROR;
            }
        };
        return deleteHolidayTask;
    }
}
