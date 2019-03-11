package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Avik Sarkar
 */
public final class UISetterUtil {


    public static FXMLLoader setStage(String resourcePath, Stage stage, String title
            , double height, double width) {

        FXMLLoader loader = new FXMLLoader(UISetterUtil.class.getResource(resourcePath));

        try {
            Parent root = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }

    public static FXMLLoader setModalWindow(String resourcePath, Stage modal, Stage parent
            , String title) {

        modal.setTitle(title);
        modal.initModality(Modality.WINDOW_MODAL);
        FXMLLoader loader = new FXMLLoader(UISetterUtil.class.getResource(resourcePath));
        try {
            Parent root = loader.load();
            modal.setScene(new Scene(root));
            modal.initOwner(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loader;
    }
}

