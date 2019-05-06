package util;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class to get the column names used in the CSV file.
 * <p>
 * Utility classes are final, cannot be instantiated and have static methods.
 *
 * @author Avik Sarkar
 */
public final class CSVUtil {

    /**
     * Private default constructor so that no other class can create an instance of this class.
     */
    private CSVUtil() {
    }

    /**
     * This method is used to extract the column names used in the CSV file and get them in the form of a list.
     *
     * @param file The CSV file from which the column names will be extracted.
     * @return A list containing the column names extracted from the CSV file.
     */
    public static List<String> getColumnNames(File file) {

        List<String> listOfColumns = null;

        try (CSVReader reader = new CSVReader(new FileReader(file))) {

            String[] cols;

            //read the first line of the CSV file only , as it contains the column names
            if ((cols = reader.readNext()) != null)

                //get the array of columns as an ArrayList
                listOfColumns = Arrays.asList(cols);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return listOfColumns;
    }
}
