package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class to set the UI of the JavaFX application.
 * <p>
 * Utility classes are final, cannot be instantiated and have static methods.
 *
 * @author Avik Sarkar
 */
public final class UISetterUtil {

    /**
     * Private default constructor so that no other class can create an instance of this class.
     */
    private UISetterUtil() {
    }


    /**
     * This method is to set the stage of the JavaFX application.
     *
     * @param resourcePath The location of the FXML file which is used to populate the scene.
     * @param stage        The stage which will be set.
     * @param title        The title of the stage.
     * @param height       The height of the scene.
     * @param width        The width of the scene.
     * @return A loader which can be used to get the controller of the FXML file.
     */
    public static FXMLLoader setStage(String resourcePath, Stage stage, String title
            , double height, double width) {

        FXMLLoader loader = new FXMLLoader(UISetterUtil.class.getResource(resourcePath));

        try {

            Parent root = loader.load();

            //set title of the stage
            stage.setTitle(title);

            //create a new scene and set the stage with that scene
            stage.setScene(new Scene(root, width, height));

            //the stage cannot be resized
            stage.setResizable(false);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return loader;
    }

    /**
     * This method is to set the modal window of the JavaFX application.
     *
     * @param resourcePath The location of the FXML file which is used to populate the scene of the modal window.
     * @param modal        The modal which which will be set.
     * @param parent       The owner of the modal window.
     * @param title        The title of the modal window.
     * @return A loader which can be used to get the controller of the FXML file.
     */
    public static FXMLLoader setModalWindow(String resourcePath, Stage modal, Stage parent
            , String title) {

        //set title of the modal
        modal.setTitle(title);

        //modal windows cannot be resized
        modal.setResizable(false);

        //set the stage as modal window
        modal.initModality(Modality.WINDOW_MODAL);

        FXMLLoader loader = new FXMLLoader(UISetterUtil.class.getResource(resourcePath));

        try {

            Parent root = loader.load();

            //create a new scene and set the modal window with that scene
            modal.setScene(new Scene(root));

            //initialize the parent as the owner of the modal
            modal.initOwner(parent);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return loader;
    }
}

