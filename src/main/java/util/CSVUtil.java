package util;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CSVUtil {

    public static List<String> getColumnNames(File file) {
        List<String> listOfColumns = null;
        try(CSVReader reader = new CSVReader(new FileReader(file))) {

            String[] cols ;

            if ((cols = reader.readNext()) != null)
                listOfColumns = Arrays.asList(cols);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return listOfColumns;

    }
}
