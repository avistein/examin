package controller.adminPanel;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.InvigilationDuty;
import model.Professor;
import service.ExamService;
import service.ProfessorService;

import java.util.ArrayList;
import java.util.List;

import static util.ConstantsUtil.DATABASE_ERROR;
import static util.ConstantsUtil.SUCCESS;

public class EditInvigilationDutyModalController {

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

    @FXML
    private Label examDetailsIdLabel;

    @FXML
    private DatePicker examDatePicker;

    @FXML
    private ComboBox<String> roomNoComboBox;

    @FXML
    private ComboBox<String> invigilatorComboBox;

    @FXML
    private ComboBox<String> professorComboBox;

    private ExamService examService;

    private ProfessorService professorService;

    private String examDetailsId;

    private boolean editStatus;

    @FXML
    private void initialize(){

        editStatus = false;
        examService = new ExamService();
        professorService = new ProfessorService();
    }

    @FXML
    private void handleExamDatePickerAction(){

        List<String> invigilators = new ArrayList<>();

        Task<List<InvigilationDuty>> invigilationDutyDataTask = examService.getInvigilationDutyDataTask(examDetailsId);
        new Thread(invigilationDutyDataTask).start();

        invigilationDutyDataTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                List<InvigilationDuty> invigilationDutyList = invigilationDutyDataTask.getValue();

                for(InvigilationDuty invigilationDuty : invigilationDutyList){


                    if(invigilationDuty.getExamDate().equals(examDatePicker.getValue().toString())){

                        invigilators.add(invigilationDuty.getProfId());
                    }
                }
                invigilatorComboBox.setItems(FXCollections.observableArrayList(invigilators));
            }
        });


        Task<List<Professor>> professorDataTask = professorService.getProfessorTask("");
        new Thread(professorDataTask).start();

        professorDataTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {

                List<Professor> professorList = professorDataTask.getValue();
                List<String> items = new ArrayList<>();
                for(Professor professor : professorList){

                    if(!invigilators.contains(professor.getProfId())){

                        items.add(professor.getProfId());
                    }
                }
                professorComboBox.setItems(FXCollections.observableArrayList(items));
            }
        });
    }

    @FXML
    private void handleSubmitButtonAction(){

        if(validate()){

            InvigilationDuty invigilationDuty = new InvigilationDuty();

            invigilationDuty.setProfId(professorComboBox.getValue());
            invigilationDuty.setExamDate(String.valueOf(examDatePicker.getValue()));

            mainGridPane.setOpacity(0.2);
            statusStackPane.setVisible(true);
            progressIndicator.setVisible(true);

            Task<Integer> updateInvigilationDutyTask = examService.getUpdateInvigilationDutyTask
                    (invigilatorComboBox.getValue(), invigilationDuty);
            new Thread(updateInvigilationDutyTask).start();

            updateInvigilationDutyTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    int status = updateInvigilationDutyTask.getValue();

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
                        statusLabel.setText("Successfully updated invigilation duty!");
                        editStatus = true;
                    }
                    else {

                        statusImageView.setImage(new Image("/png/error.png"));
                        statusLabel.setText("Invigilation Duty associated with the given Exam ID and date not found!");
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


    void setExamDetailsId(String examDetailsId){

        this.examDetailsId = examDetailsId;
        examDetailsIdLabel.setText(examDetailsId);
    }

    boolean getEditStatus(){

        return editStatus;
    }

    private boolean validate(){

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if(examDatePicker.getValue() == null){

            alert.setHeaderText("Please choose a date!");
            alert.show();
            return false;
        }
        if(invigilatorComboBox.getValue() == null){

            alert.setHeaderText("Please choose a invigilator to replace!");
            alert.show();
            return false;
        }
        if(professorComboBox.getValue() == null){

            alert.setHeaderText("Please choose a professor to replace with!");
            alert.show();
            return false;
        }
        return true;
    }
}
