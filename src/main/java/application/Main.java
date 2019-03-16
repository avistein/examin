package application;

import controller.login.PropertiesFileNotFoundController;
import javafx.application.Application;
import javafx.stage.Stage;
import util.UISetterUtil;

import java.nio.file.Paths;

import static util.ConstantsUtil.*;

/**
 * Main class. This is where it all starts!
 *
 * @author Avik Sarkar
 */
public class Main extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    public void start(Stage primaryStage) {

        //default status
        boolean proceedToMainAppStatus = true;

        /*
        Check if db.properties exists in the location in USER's system.If it doesn't exist ask for the file first time
        while starting the application or ask the user to create the file using UI.if it exists , don't show the user
        the UI to create or load the file and directly load the db.properties from then location.
         */
        if (!Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, "db.properties").toFile().exists()) {

            //create the modal window
            Stage propertiesFileNotFoundModal = new Stage();

            //set the modal and get the controller
            PropertiesFileNotFoundController propertiesFileNotFoundController = UISetterUtil.setModalWindow
                    ("/view/login/PropertiesFileNotFound.fxml", propertiesFileNotFoundModal, primaryStage
                            , PROJECT_NAME).getController();

            /*
            showAndWait() ensures that the data FileLoadingStatus is fetched after the modal window is closed.
            This method blocks the UI thread here.
             */
            propertiesFileNotFoundModal.showAndWait();

            //get the status whether the db.properties is loaded and actually exists in the location
            proceedToMainAppStatus = propertiesFileNotFoundController.getFileLoadingStatus();
        }

        /*
        If the db.properties file exists,display the Login screen,otherwise close the primaryStage which will in
        turn close the whole application.
         */
        if (proceedToMainAppStatus) {

            UISetterUtil.setStage("/view/login/Login.fxml", primaryStage
                    , PROJECT_NAME, 350, 300);
            primaryStage.show();
        } else {

            primaryStage.hide();
        }
    }
}

