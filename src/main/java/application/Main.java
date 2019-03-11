package application;

import controller.PropertiesFileNotFoundController;
import javafx.application.Application;
import javafx.stage.Stage;
import util.UISetterUtil;
import static util.ConstantsUtil.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        boolean proceedToMainAppStatus = true;
        if(Files.notExists(Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, "db.properties"))){
            Stage propertiesFileNotFoundModal = new Stage();

            PropertiesFileNotFoundController propertiesFileNotFoundController = UISetterUtil.setModalWindow("/view/PropertiesFileNotFound.fxml"
                    , propertiesFileNotFoundModal, primaryStage, PROJECT_NAME).getController();

            propertiesFileNotFoundModal.showAndWait();

            proceedToMainAppStatus = propertiesFileNotFoundController.getFileLoadingStatus();
        }

        if(proceedToMainAppStatus) {
            UISetterUtil.setStage("/view/Login.fxml", primaryStage
                    , PROJECT_NAME, 400, 400);
            primaryStage.show();
        }
        else
            primaryStage.hide();
    }
}

