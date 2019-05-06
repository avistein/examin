package service;

import javafx.concurrent.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static util.ConstantsUtil.*;

/**
 * Service class to handle File Handling.
 *
 * @author Avik Sarkar
 */
public class FileHandlingService {

    /**
     * This method is used to Create a file and write contents to it.
     *
     * @param content  Content to be written to the file to be created.
     * @param dirName  Directory of the file to be created.
     * @param fileName Name of the file to be created.
     * @return A createAndWriteToFileTask instance which is used to create a file and write to it in a separate thread;
     * createAndWriteToFileTask.call() method returns a status indicating success or failure in file creation.
     */
    @SuppressWarnings("Duplicates")
    public Task<Boolean> getCreateAndWriteToFileTask(byte[] content, String dirName, String fileName) {

        Task<Boolean> createAndWriteToFileTask = new Task<>() {

            @Override
            protected Boolean call() {

                /*
                The file will be created in for example,"C:/Users/Avik Sarkar/examin/configs/db.properties".
                 */
                final String userHome = System.getProperty("user.home");
                final Path dirPath = Paths.get(userHome, ROOT_DIR, dirName);
                final Path filePath = Paths.get(userHome, ROOT_DIR, dirName, fileName);

                try {

                    //create directories recursively
                    Files.createDirectories(dirPath);

                    //create if the file doesn't exist and write to it
                    Files.write(filePath, content);
                } catch (IOException e) {

                    e.printStackTrace();

                    //if any exception occurs during creation of directory or file, failed status is returned
                    return false;
                }

                //file creation successful status is returned
                return true;
            }
        };
        return createAndWriteToFileTask;
    }

    /**
     * This method is used to Create a file and copy contents to it from the source file.
     *
     * @param srcFile  The file to be copied from.
     * @param dirName  Directory of the file to be copied.
     * @param fileName Name of the to be copied into.
     * @return A createAndCopyFileTask instance which is used to Create a file and copy contents to it in a separate
     * thread; createAndCopyFileTask.call() method returns a status indicating success or failure in file creation.
     */
    @SuppressWarnings("Duplicates")
    public Task<Boolean> getCreateAndCopyFileTask(File srcFile, String dirName, String fileName) {

        Task<Boolean> createAndCopyFileTask = new Task<>() {

            @Override
            protected Boolean call() {

                /*
                The file will be created in for example,"C:/Users/Avik Sarkar/examin/configs/db.properties".
                 */
                final String userHome = System.getProperty("user.home");
                final Path destDirPath = Paths.get(userHome, ROOT_DIR, dirName);
                final Path destFilePath = Paths.get(userHome, ROOT_DIR, dirName, fileName);

                try {

                    //create directories recursively
                    Files.createDirectories(destDirPath);

                    //create the file and copy the contents from the srcFile; replace the file it exists
                    Files.copy(srcFile.toPath(), destFilePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {

                    e.printStackTrace();

                    //if any exception occurs during creation of directory or file, failed status is returned
                    return false;
                }

                //file creation and copy successful status is returned
                return true;
            }
        };
        return createAndCopyFileTask;
    }

    /**
     * This method is used to get a task which can be used to create a .properties file and store properties in it.
     *
     * @param fileName The file name of the .properties to be created.
     * @param propMap  HashMap whose Key will be set as propKey and Value as propValue.
     * @return A task which can be used to create a .properties file and store properties in it.
     */
    public Task<Boolean> getCreatePropertiesFile(String fileName, Map<String, String> propMap) {

        //the file is located for example in "C:/Avik Sarkar/examin/configs/db.properties"
        Path filePath = Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, fileName);

        Task<Boolean> createPropertiesFile = new Task<>() {

            @Override
            protected Boolean call() throws Exception {

                try {

                    //if the .properties file exists, then don't create it again
                    if (!filePath.toFile().exists()) {

                        Files.createFile(filePath);
                    }
                    Properties props = new Properties();

                    //create a new output stream to store props in the .properties file
                    FileOutputStream out = new FileOutputStream(String.valueOf(filePath));

                    /*
                    Iterate the HashMap, and for each entry in the HashMap, get the Key and Values and set the
                    properties in the Property instance as propKey and propValue.
                     */
                    for (Map.Entry<String, String> entry : propMap.entrySet()) {

                        props.setProperty(entry.getKey(), entry.getValue());
                    }

                    //store the properties in the .properties file
                    props.store(out, null);

                    //successful creation and storing of properties in the .properties file
                    return true;
                } catch (IOException e) {

                    //print the stack trace if the file can't be found or unable to load and return false
                    e.printStackTrace();
                    return false;
                }

            }
        };
        return createPropertiesFile;
    }

    /**
     * This method is used to get the properties from a .properties file and save them in a HashMap and return it
     * to the method calling it
     *
     * @param fileName   The name of the file from which the properties will be loaded.
     * @param properties The properties Keys for which the values will be fetched from the .properties file.
     * @return A HashMap containing properties keys as Keys and prop values as Values.
     */
    public Map<String, String> loadPropertiesValuesFromPropertiesFile(String fileName, String... properties) {

        //the file is located for example in "C:/Avik Sarkar/examin/configs/db.properties"
        Path filePath = Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, fileName);

        Map<String, String> map = new HashMap<>();

        //get the inputStream from the .properties file
        try (FileInputStream in = new FileInputStream(String.valueOf(filePath))) {

            Properties props = new Properties();

            //load the properties in the Properties instance
            props.load(in);

            //iterate the required property keys
            for (String propKey : properties) {

                //get the value for the corresponding prop key
                String propValue = props.getProperty(propKey);

                //put them into hashMap
                map.put(propKey, propValue);
            }
        } catch (IOException e) {

            //throw an exception & print the stack trace if the file can't be found or unable to load
            e.printStackTrace();
        }
        return map;
    }
}
