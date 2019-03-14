package controller.login;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import service.FileHandlingService;

import java.io.File;

import static util.ConstantsUtil.LINE_SEPARATOR;

/**
 * Controller class for /login/PropertiesFileNotFound.fxml.
 * This class basically creates a new db.properties file if it doesn't exist in the User's System.
 *
 * @author Avik Sarkar
 */
public class PropertiesFileNotFoundController {

    /*-------------------------------Declaration and initialization of variables--------------------------------*/

    @FXML
    private Button chooseFileButton;

    @FXML
    private TextField databaseUrlTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private Button createButton;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private StackPane statusStackPane;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ImageView statusImageView;

    @FXML
    private Label statusLabel;

    @FXML
    private Label msgLabel;

    private FileHandlingService fileHandlingService;

    private boolean fileLoadingStatus;

    /*-----------------------------------End of declaration and initialization----------------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    private void initialize() {

        fileHandlingService = new FileHandlingService();
        fileLoadingStatus = false;
    }

    /**
     * Callback method to handle the action of Create File button.
     * <p>
     * This method basically enables all the textFields for the User to enter the db.properties details.
     */
    @FXML
    private void handleCreateFileButtonAction() {

        databaseUrlTextField.setDisable(false);
        usernameTextField.setDisable(false);
        passwordPasswordField.setDisable(false);
        createButton.setDisable(false);
    }

    /**
     * Callback method to handle action of Choose File button.
     * <p>
     * Basically this method asks the user to choose a properties file from his/her file system and loads it by copying
     * it to the User's {USER_HOME}/configs folder.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleChooseFileButtonAction() {

        //choose the properties file from the file system
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());

        //only if a file is chosen from FileChooser
        if (file != null) {

            //create a db.properties and copy it to /configs folder
            Task<Boolean> createAndCopyFileTask = fileHandlingService.getCreateAndCopyFileTask
                    (file, "configs", "db.properties");
            new Thread(createAndCopyFileTask).start();

            //display a loading spinner and fade the background
            activateProgressAndStatusIndicator();

            createAndCopyFileTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    progressIndicator.setVisible(false);

                    //get a status indicating success or failure in file creation & copy
                    if (createAndCopyFileTask.getValue()) {

                        statusImageView.setImage(new Image("/png/success.png"));
                        statusLabel.setText("File loaded successfully!");
                        fileLoadingStatus = true;
                    } else {

                        statusImageView.setImage(new Image("/png/critical error.png"));
                        statusLabel.setText("File loading failed!");
                        fileLoadingStatus = false;
                    }
                }
            });
        }
    }

    /**
     * Callback method to handle action of Create Button.
     * <p>
     * Basically this method creates a db.properties file and write content to it.
     */
    @SuppressWarnings("Duplicates")
    @FXML
    private void handleCreateButtonAction() {

        //the content to be written into the db.properties file
        String content = "jdbc.driver=com.mysql.cj.jdbc.Driver" + LINE_SEPARATOR +
                "jdbc.url=jdbc:mysql://" + databaseUrlTextField.getText() + LINE_SEPARATOR +
                "jdbc.username=" + usernameTextField.getText() + LINE_SEPARATOR +
                "jdbc.password=" + passwordPasswordField.getText();

        //convert string to byte array to write into the db.properties file
        byte[] contentToBytes = content.getBytes();

        //create a task to create a db.properties in /configs folder
        Task<Boolean> createAndWriteToFileTask = fileHandlingService.getCreateAndWriteToFileTask(contentToBytes
                , "configs", "db.properties");
        new Thread(createAndWriteToFileTask).start();

        //display loading spinner and fade the background
        activateProgressAndStatusIndicator();

        createAndWriteToFileTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                progressIndicator.setVisible(false);

                //get a boolean value indicating success or failure in db.properties file creation
                if (createAndWriteToFileTask.getValue()) {

                    statusImageView.setImage(new Image("/png/success.png"));
                    statusLabel.setText("File creation successful!");
                    fileLoadingStatus = true;
                } else {

                    statusImageView.setImage(new Image("/png/critical error.png"));
                    statusLabel.setText("File creation failed!");
                    fileLoadingStatus = false;
                }
            }
        });


    }

    /**
     * Method for configuring the fileChooser.
     */
    private void configureFileChooser(FileChooser fileChooser) {

        fileChooser.setTitle("Open Properties File");

        //only .properties files can be chosen
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("properties", "*.properties"));
    }

    /**
     * This method fades the background , displays a loading spinner and shows status.
     */
    private void activateProgressAndStatusIndicator() {

        mainGridPane.setOpacity(0.5);
        statusStackPane.setVisible(true);
        progressIndicator.setVisible(true);
        statusImageView.setVisible(true);
        statusLabel.setVisible(true);
        msgLabel.setVisible(true);
    }

    /**
     * This method is used to send the status of the creation/loading of the db.properties.
     *
     * @return The status denoting the db.properties file created/loaded or not.
     */
    public boolean getFileLoadingStatus() {

        return fileLoadingStatus;
    }

}
