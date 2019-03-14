package service;

import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static util.ConstantsUtil.ROOT_DIR;

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
}
