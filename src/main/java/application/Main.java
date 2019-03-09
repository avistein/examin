package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.ValidatorUtil;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        primaryStage.setTitle("examin - Examination Management Tool");
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.setResizable(false);
        System.out.println(ValidatorUtil.validateRegYear("2015-2019", "2015"));
        primaryStage.show();
    }
}

