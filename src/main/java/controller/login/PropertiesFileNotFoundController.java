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

import static util.ConstantsUtil.LINE_SEPARATOR;
import java.io.File;
import java.nio.file.Files;


public class PropertiesFileNotFoundController {

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

    private File file;

    private FileHandlingService fileHandlingService;

    private boolean fileLoadingStatus;

    @FXML
    private void initialize(){

        fileHandlingService = new FileHandlingService();
        fileLoadingStatus = false;
    }

    @FXML
    private void handleCreateFileButtonAction(){

        databaseUrlTextField.setDisable(false);
        usernameTextField.setDisable(false);
        passwordPasswordField.setDisable(false);
        createButton.setDisable(false);
    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleChooseFileButtonAction(){

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());

        if(file != null) {

            Task<Boolean> createAndCopyFileTask = fileHandlingService.getCreateAndCopyFileTask
                    (file, "configs", "db.properties");
            new Thread(createAndCopyFileTask).start();

            activateProgressAndStatusIndicator();
            createAndCopyFileTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    progressIndicator.setVisible(false);

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

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleCreateButtonAction(){

        String content = "jdbc.driver=com.mysql.cj.jdbc.Driver" + LINE_SEPARATOR +
                "jdbc.url=jdbc:mysql://" + databaseUrlTextField.getText() + LINE_SEPARATOR +
                "jdbc.username=" + usernameTextField.getText() + LINE_SEPARATOR +
                "jdbc.password=" + passwordPasswordField.getText();

        byte[] contentToBytes = content.getBytes();

        Task<Boolean> createAndWriteToFileTask = fileHandlingService.getCreateAndWriteToFileTask(contentToBytes
                , "configs", "db.properties");
        new Thread(createAndWriteToFileTask).start();

        activateProgressAndStatusIndicator();

        createAndWriteToFileTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                progressIndicator.setVisible(false);

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
     * Method for configuring the fileChooser
     */
    private void configureFileChooser(FileChooser fileChooser) {

        fileChooser.setTitle("Open Properties File");
        //only .properties files can be chosen
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("properties", "*.properties"));
    }

    private void activateProgressAndStatusIndicator(){

        mainGridPane.setOpacity(0.5);
        statusStackPane.setVisible(true);
        progressIndicator.setVisible(true);
        statusImageView.setVisible(true);
        statusLabel.setVisible(true);
        msgLabel.setVisible(true);
    }

    public boolean getFileLoadingStatus(){

        return fileLoadingStatus;
    }

}
