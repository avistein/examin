package controller;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.Marks;
import service.MarksService;
import util.ValidatorUtil;

import static util.ConstantsUtil.DATABASE_ERROR;
import static util.ConstantsUtil.SUCCESS;

public class AddMarksModalController {

    @FXML
    private Label regIdLabel;

    @FXML
    private TextField obtainedMarksTextField;

    @FXML
    private CheckBox absentCheckBox;

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

    private Marks marks;

    private MarksService marksService;

    private boolean tableUpdateStatus;

    @FXML
    private void initialize(){

        marksService = new MarksService();
        tableUpdateStatus = false;
    }

    @FXML
    private void handleAbsentCheckBoxAction(){

        obtainedMarksTextField.setText("ABSENT");
        obtainedMarksTextField.setDisable(absentCheckBox.isSelected());
    }

    @FXML
    private void handleSubmitButtonAction(){

        if(validate()){

            mainGridPane.setOpacity(0.5);
            statusStackPane.setVisible(true);
            progressIndicator.setVisible(true);

            marks.setObtainedMarks(obtainedMarksTextField.getText().trim());

            Task<Integer> addMarksToDbTask = marksService.getAddMarksToDataBaseTask(marks);
            new Thread(addMarksToDbTask).start();

            addMarksToDbTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    int status = addMarksToDbTask.getValue();

                    progressIndicator.setVisible(false);
                    statusImageView.setVisible(true);
                    statusLabel.setVisible(true);
                    msgLabel.setVisible(true);
                    msgLabel.setText("Click anywhere to proceed!");

                    if (status == DATABASE_ERROR) {

                        statusImageView.setImage(new javafx.scene.image.Image("/png/critical error.png"));
                        statusLabel.setText("Database Error!");
                    } else if (status == SUCCESS) {

                        statusImageView.setImage(new javafx.scene.image.Image("/png/success.png"));
                        statusLabel.setText("Successfully added / updated marks to students!");
                        tableUpdateStatus = true;
                    }
                     else {

                        statusImageView.setImage(new Image("/png/error.png"));
                        statusLabel.setText("Student giving this exam not found!");
                    }
                }
            });
        }

    }

    @FXML
    private void handleStatusStackPaneOnMouseClicked(){

        mainGridPane.setOpacity(1);
        progressIndicator.setVisible(false);
        statusImageView.setVisible(false);
        statusLabel.setVisible(false);
        statusStackPane.setVisible(false);
    }

    private boolean validate(){

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if(!absentCheckBox.isSelected()) {

            if (obtainedMarksTextField.getText() == null || obtainedMarksTextField.getText().trim().isEmpty()) {

                alert.setHeaderText("Obtained Marks field cannot be empty!");
                alert.show();
                return false;
            }
            if (!ValidatorUtil.validateNumber(obtainedMarksTextField.getText().trim())) {

                alert.setHeaderText("Not a valid obtained marks!");
                alert.show();
                return false;
            }
        }
        return true;
    }

    public void setMarksDetails(Marks marks){

        this.marks = marks;
        regIdLabel.setText(marks.getRegId());
    }

    public boolean getTableUpdateStatus(){

        return this.tableUpdateStatus;
    }
}
