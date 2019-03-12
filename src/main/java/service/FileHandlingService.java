package service;

import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static util.ConstantsUtil.ROOT_DIR;

public class FileHandlingService {

    @SuppressWarnings("Duplicates")
    public Task<Boolean> getCreateAndWriteToFileTask(byte[] content, String dirName, String fileName) {

        Task<Boolean> createAndWriteToFileTask = new Task<>() {
            @Override
            protected Boolean call() {

                final String userHome = System.getProperty("user.home");
                final Path dirPath = Paths.get(userHome, ROOT_DIR, dirName);
                final Path filePath = Paths.get(userHome, ROOT_DIR, dirName, fileName);

                try {
                    Files.createDirectories(dirPath);
                    Files.write(filePath, content);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        };

        return createAndWriteToFileTask;
    }

    @SuppressWarnings("Duplicates")
    public Task<Boolean> getCreateAndCopyFileTask(File srcFile, String dirName, String fileName) {

        Task<Boolean> createAndCopyFileTask = new Task<>() {
            @Override
            protected Boolean call() {

                final String userHome = System.getProperty("user.home");

                final Path destDirPath = Paths.get(userHome, ROOT_DIR, dirName);
                final Path destFilePath = Paths.get(userHome, ROOT_DIR, dirName, fileName);

                try {
                    Files.createDirectories(destDirPath);
                    Files.copy(srcFile.toPath(), destFilePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        };

        return createAndCopyFileTask;
    }
}
