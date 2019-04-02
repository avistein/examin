package controller.adminPanel;

import command.ExamCommand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import model.Exam;
import model.ExamDetails;
import model.InvigilationDuty;
import model.RoomAllocation;
import service.ExamService;
import service.PdfService;
import util.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static util.ConstantsUtil.*;

public class ExamAdministrationController {

    /*--------------------------------Declaration & Initialization of variables---------------------------------------*/

    @FXML
    private TabPane examAdministrationTabPane;

    private ExamService examService;

    /*------------------------------End of Declaration & Initialization of variables----------------------------------*/


    /*-------------------------------Declaration & Initialization of Create Exam tab items----------------------------*/

    @FXML
    private Tab createExamTab;

    @FXML
    private TextField examDetailsIdTextField;

    @FXML
    private ChoiceBox<String> semesterTypeChoiceBox;

    @FXML
    private ChoiceBox<String> examTypeChoiceBox;

    @FXML
    private DatePicker startDateDatePicker;

    @FXML
    private TextField examIntervalTextField;

    @FXML
    private TextField startTimeTextField;

    @FXML
    private TextField endTimeTextField;

    @FXML
    private TextField academicYearTextField;

    @FXML
    private CheckBox examOnSaturdayCheckBox;

    @FXML
    private GridPane createExamTabMainGridPane;

    @FXML
    private StackPane createExamTabStatusStackPane;

    @FXML
    private ProgressIndicator createExamTabProgressIndicator;

    @FXML
    private ImageView createExamTabStatusImageView;

    @FXML
    private Label createExamTabStatusLabel;

    @FXML
    private Label createExamTabMsgLabel;

    /*--------------------------End of Declaration & Initialization of Create Exam tab items--------------------------*/


    /*-------------------------------Declaration & Initialization of Manage Exam tab items----------------------------*/

    @FXML
    private Tab manageExamTab;

    @FXML
    private TableView<ExamDetails> manageExamTableView;

    @FXML
    private TableColumn<ExamDetails, String> examDetailsIdCol;

    @FXML
    private TableColumn<ExamDetails, String> examTypeCol;

    @FXML
    private TableColumn<ExamDetails, String> semesterTypeCol;

    @FXML
    private TableColumn<ExamDetails, String> startDateCol;

    @FXML
    private TableColumn<ExamDetails, String> endDateCol;

    @FXML
    private GridPane manageExamTabMainGridPane;

    @FXML
    private StackPane manageExamTabStatusStackPane;

    @FXML
    private ProgressIndicator manageExamTabProgressIndicator;

    @FXML
    private ImageView manageExamTabStatusImageView;

    @FXML
    private Label manageExamTabStatusLabel;

    @FXML
    private Label manageExamTabMsgLabel;

    private ObservableList<ExamDetails> examDetailsObsList;

    private ExamCommand examCommand;

    private PdfService pdfService;

    /*--------------------------End of Declaration & Initialization of Manage Exam tab items--------------------------*/

    /**
     * This method is used to initialize variables of this Class.
     * This method is called when the FXMLLoader.load() is called.
     * <p>
     * Do not try to get the Scene or Window of any node in this method.
     */
    @FXML
    private void initialize() {

        examService = new ExamService();
        pdfService = new PdfService();
        examCommand = new ExamCommand();

        semesterTypeChoiceBox.setItems(FXCollections.observableArrayList("ODD", "EVEN"));
        examTypeChoiceBox.setItems(FXCollections.observableArrayList("Theory", "Practical"));

        initExamTableViewCols();

        examDetailsObsList = FXCollections.observableArrayList();
        populateExamTable();
    }

    /*------------------------------------------Create Exam Tab Operation---------------------------------------------*/

    @FXML
    private void handleResetButtonAction() {

        resetItems();
    }

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleSubmitButtonAction() {

        if (validateCreateExamTabItems()) {

            ExamDetails examDetails = new ExamDetails();

            examDetails.setExamDetailsId(examDetailsIdTextField.getText().trim());
            examDetails.setExamType(examTypeChoiceBox.getValue());
            examDetails.setSemesterType(semesterTypeChoiceBox.getValue());
            examDetails.setStartDate(String.valueOf(startDateDatePicker.getValue()));
            examDetails.setStartTime(startTimeTextField.getText().trim());
            examDetails.setEndTime(endTimeTextField.getText().trim());
            examDetails.setAcademicYear(academicYearTextField.getText().trim());
            examDetails.setExamInterval(examIntervalTextField.getText().trim());
            examDetails.setIsExamOnSaturday(examOnSaturdayCheckBox.isSelected());

            createExamTabMainGridPane.setOpacity(0.2);
            createExamTabStatusStackPane.setVisible(true);
            createExamTabProgressIndicator.setVisible(true);

            Task<Integer> addExamDetailsTask = examService.getAddExamDetailsTask(examDetails);
            new Thread(addExamDetailsTask).start();

            addExamDetailsTask.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {

                    int status = addExamDetailsTask.getValue();

                    createExamTabProgressIndicator.setVisible(false);
                    createExamTabStatusImageView.setVisible(true);
                    createExamTabStatusLabel.setVisible(true);
                    createExamTabMsgLabel.setVisible(true);

                    if (status == DATABASE_ERROR) {

                        createExamTabStatusImageView.setImage(new Image("/png/critical error.png"));
                        createExamTabStatusLabel.setText("Error!");
                    } else if (status == SUCCESS) {

                        createExamTabStatusImageView.setImage(new Image("/png/success.png"));
                        createExamTabStatusLabel.setText("Success added exam!");
                        examDetailsObsList.add(examDetails);
                    } else {

                        createExamTabStatusImageView.setImage(new Image("/png/error.png"));
                        createExamTabStatusLabel.setText("Exam with same Exam ID already exists!");
                    }
                }
            });
        }
    }

    @FXML
    private void handleCreateExamStatusStackPaneOnMouseClicked() {

        deactivateCreateExamStatusStackPane();
        resetItems();
    }

    @SuppressWarnings("Duplicates")
    private void deactivateCreateExamStatusStackPane() {

        createExamTabMainGridPane.setOpacity(1);
        createExamTabStatusStackPane.setVisible(false);
        createExamTabProgressIndicator.setVisible(false);
        createExamTabStatusImageView.setVisible(false);
        createExamTabStatusLabel.setVisible(false);
        createExamTabMsgLabel.setVisible(false);
    }

    private void resetItems() {

        examDetailsIdTextField.clear();
        examTypeChoiceBox.setValue(null);
        semesterTypeChoiceBox.setValue(null);
        startDateDatePicker.setValue(null);
        startTimeTextField.clear();
        endTimeTextField.clear();
        academicYearTextField.clear();
        examOnSaturdayCheckBox.setSelected(false);
        examIntervalTextField.clear();
    }

    private boolean validateCreateExamTabItems() {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (examDetailsIdTextField.getText() == null || examDetailsIdTextField.getText().trim().isEmpty()) {

            alert.setHeaderText("Exam ID cannot be empty!");
            alert.show();
            return false;
        }

        if (!ValidatorUtil.validateId(examDetailsIdTextField.getText().trim())) {

            alert.setHeaderText("Invalid Exam ID!");
            alert.show();
            return false;
        }
        if (examTypeChoiceBox.getValue() == null) {

            alert.setHeaderText("Please choose exam type!");
            alert.show();
            return false;
        }
        if (semesterTypeChoiceBox.getValue() == null) {

            alert.setHeaderText("Please choose semester type!");
            alert.show();
            return false;
        }
        if (startDateDatePicker.getValue() == null) {

            alert.setHeaderText("Please pick exam start date!");
            alert.show();
            return false;
        }
        if (startTimeTextField.getText() == null || startTimeTextField.getText().trim().isEmpty()) {

            alert.setHeaderText("Exam Start Time cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateTimeFormat(startTimeTextField.getText().trim())) {

            alert.setHeaderText("Invalid Exam Start Time format!");
            alert.show();
            return false;
        }
        if (endTimeTextField.getText() == null || endTimeTextField.getText().trim().isEmpty()) {

            alert.setHeaderText("Exam End Time cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateTimeFormat(endTimeTextField.getText().trim())) {

            alert.setHeaderText("Invalid Exam End Time format!");
            alert.show();
            return false;
        }
        if (academicYearTextField.getText() == null || academicYearTextField.getText().trim().isEmpty()) {

            alert.setHeaderText("Academic Year cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateBatchName(academicYearTextField.getText().trim())) {

            alert.setHeaderText("Invalid Academic Year!");
            alert.show();
            return false;
        }

        if (examIntervalTextField.getText() == null || examIntervalTextField.getText().trim().isEmpty()) {

            alert.setHeaderText("Exam Interval cannot be empty!");
            alert.show();
            return false;
        }
        if (!ValidatorUtil.validateNumber(examIntervalTextField.getText().trim())) {

            alert.setHeaderText("Invalid Exam Interval format!");
            alert.show();
            return false;
        }
        return true;
    }

    /*-------------------------------------End of Create Exam Tab operation-------------------------------------------*/


    /*-----------------------------------------Manage Exam Tab operation----------------------------------------------*/

    @SuppressWarnings("Duplicates")
    @FXML
    private void handleDeleteExamButtonAction() {

        ExamDetails examDetails = manageExamTableView.getSelectionModel().getSelectedItem();

        if (examDetails != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Do you really want to delete?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                manageExamTabMainGridPane.setOpacity(0.2);
                manageExamTabStatusStackPane.setVisible(true);
                manageExamTabProgressIndicator.setVisible(true);

                Task<Integer> deleteExamDetailsTask = examService.getDeleteExamDetailsTask(examDetails);
                new Thread(deleteExamDetailsTask).start();

                deleteExamDetailsTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        int status = deleteExamDetailsTask.getValue();

                        manageExamTabProgressIndicator.setVisible(false);
                        manageExamTabStatusImageView.setVisible(true);
                        manageExamTabStatusLabel.setVisible(true);
                        manageExamTabMsgLabel.setVisible(true);

                        if (status == DATABASE_ERROR) {

                            manageExamTabStatusImageView.setImage(new Image("/png/critical error.png"));
                            manageExamTabStatusLabel.setText("Error!");
                        } else if (status == SUCCESS) {

                            manageExamTabStatusImageView.setImage(new Image("/png/success.png"));
                            manageExamTabStatusLabel.setText("Successfully deleted the exam!");
                            examDetailsObsList.remove(examDetails);
                        } else if (status == DATA_DEPENDENCY_ERROR) {

                            manageExamTabStatusImageView.setImage(new Image("/png/error.png"));
                            manageExamTabStatusLabel.setText("Cannot delete exam!");
                        } else {

                            manageExamTabStatusImageView.setImage(new Image("/png/error.png"));
                            manageExamTabStatusLabel.setText("Exam doesn't exist!");
                        }
                    }
                });
            }
        }
    }

    @FXML
    private void handleTimeTableCreateButtonAction() {

        ExamDetails examDetails = manageExamTableView.getSelectionModel().getSelectedItem();

        if (examDetails != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Do you really want to create routine for Exam ID " + examDetails.getExamDetailsId());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                manageExamTabMainGridPane.setOpacity(0.2);
                manageExamTabStatusStackPane.setVisible(true);
                manageExamTabProgressIndicator.setVisible(true);
                manageExamTabMsgLabel.setVisible(true);
                manageExamTabMsgLabel.setText("Exam Routine is being generated.Please do not close this window.");

                Task<Integer> createExamRoutineTask = examCommand.getCreateExamRoutineTask(examDetails);
                new Thread(createExamRoutineTask).start();

                createExamRoutineTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        manageExamTabStatusLabel.setVisible(true);

                        int status = createExamRoutineTask.getValue();

                        if (status == DATABASE_ERROR) {

                            manageExamTabProgressIndicator.setVisible(false);
                            manageExamTabStatusImageView.setVisible(true);
                            manageExamTabStatusImageView.setImage(new Image("/png/critical error.png"));
                            manageExamTabStatusLabel.setText("Error!");
                        } else if (status == SUCCESS) {

                            manageExamTabStatusLabel.setText("Successfully created Exam Routine!");
                            manageExamTabMsgLabel.setText("Assigning students to exams now. Please do not close this.");


                            Task<Integer> assignStudentsToExamTask = examCommand.getAssignStudentsToExamTask();
                            new Thread(assignStudentsToExamTask).start();

                            assignStudentsToExamTask.setOnSucceeded(new EventHandler<>() {
                                @Override
                                public void handle(WorkerStateEvent event) {

                                    manageExamTabProgressIndicator.setVisible(false);
                                    manageExamTabStatusImageView.setVisible(true);
                                    manageExamTabStatusLabel.setVisible(true);
                                    manageExamTabMsgLabel.setText("Click anywhere to proceed! ");

                                    int status = assignStudentsToExamTask.getValue();

                                    if (status == DATABASE_ERROR) {

                                        manageExamTabStatusImageView.setImage(new Image("/png/critical error.png"));
                                        manageExamTabStatusLabel.setText("Error!");
                                    } else if (status == SUCCESS) {

                                        manageExamTabStatusImageView.setImage(new Image("/png/success.png"));
                                        manageExamTabStatusLabel.setText("Successfully assigned all students to exams!");
                                    } else {

                                        manageExamTabStatusImageView.setImage(new Image("/png/error.png"));
                                        manageExamTabStatusLabel.setText("Student assignment already exists!");
                                    }
                                }
                            });
                        } else {

                            manageExamTabProgressIndicator.setVisible(false);
                            manageExamTabStatusImageView.setVisible(true);
                            manageExamTabStatusImageView.setImage(new Image("/png/error.png"));
                            manageExamTabStatusLabel.setText("Routine for this exam already exists!");
                        }
                    }
                });
            }
        }
    }

    @FXML
    private void handleTimeTableGeneratePdfButtonAction() {

        ExamDetails examDetails = manageExamTableView.getSelectionModel().getSelectedItem();

        if (examDetails != null) {

            manageExamTabMainGridPane.setOpacity(0.2);
            manageExamTabStatusStackPane.setVisible(true);
            manageExamTabProgressIndicator.setVisible(true);
            manageExamTabMsgLabel.setVisible(true);
            manageExamTabMsgLabel.setText("Gathering data to create pdf.Please do not close this window.");

            String additionalQuery = "WHERE v_exam_details_id=? ORDER BY d_exam_date";

            Task<List<Exam>> examRoutineTask = examService.getExamRoutineTask(additionalQuery
                    , examDetails.getExamDetailsId());
            new Thread(examRoutineTask).start();

            examRoutineTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    List<Exam> routine = examRoutineTask.getValue();
                    List<String> examDates = new ArrayList<>();
                    List<String> semesters = new ArrayList<>();

                    for (Exam exam : routine) {

                        if (!examDates.contains(exam.getExamDate())) {

                            examDates.add(exam.getExamDate());
                        }

                        if (!semesters.contains(exam.getSemester())) {

                            semesters.add(exam.getSemester());
                        }
                    }

                    manageExamTabMsgLabel.setText("Creating PDFs now.Please do not close this window.");

                    Task<Boolean> createRoutinePdfTask = pdfService.getCreateRoutinePdfTask(routine,
                            examDates, semesters, examDetails);
                    new Thread(createRoutinePdfTask).start();

                    createRoutinePdfTask.setOnSucceeded(new EventHandler<>() {

                        @Override
                        public void handle(WorkerStateEvent event) {

                            boolean status = createRoutinePdfTask.getValue();

                            manageExamTabProgressIndicator.setVisible(false);
                            manageExamTabStatusImageView.setVisible(true);
                            manageExamTabStatusLabel.setVisible(true);
                            manageExamTabMsgLabel.setText("Click anywhere to proceed!");

                            if (status) {

                                manageExamTabStatusImageView.setImage(new Image("/png/success.png"));
                                manageExamTabStatusLabel.setText("Successfully created Exam Routine PDF!");
                            } else {

                                manageExamTabStatusImageView.setImage(new Image("/png/critical error.png"));
                                manageExamTabStatusLabel.setText("Unable to create exam routine pdf!");
                            }
                        }
                    });
                }
            });
        }
    }

    @FXML
    private void handleTimeTableViewButtonAction() {

    }

    @FXML
    private void handleTimeTableDeleteButtonAction() {

    }

    @FXML
    private void handleRoomAllocationCreateButtonAction() {

        ExamDetails examDetails = manageExamTableView.getSelectionModel().getSelectedItem();

        if (examDetails != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Do you really want to create Room Allocation for Exam ID "
                    + examDetails.getExamDetailsId());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                manageExamTabMainGridPane.setOpacity(0.2);
                manageExamTabStatusStackPane.setVisible(true);
                manageExamTabProgressIndicator.setVisible(true);
                manageExamTabMsgLabel.setVisible(true);
                manageExamTabMsgLabel.setText("Room Allocation is being generated.Please do not close this window. ");

                Task<Integer> createRoomAllocationTask = examCommand.getCreateRoomAllocationTask(examDetails);
                new Thread(createRoomAllocationTask).start();

                createRoomAllocationTask.setOnSucceeded(new EventHandler<>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        manageExamTabStatusLabel.setVisible(true);
                        manageExamTabProgressIndicator.setVisible(false);
                        manageExamTabStatusImageView.setVisible(true);
                        manageExamTabMsgLabel.setText("Click anywhere to proceed! ");

                        int status = createRoomAllocationTask.getValue();

                        if (status == DATABASE_ERROR) {

                            manageExamTabStatusImageView.setImage(new Image("/png/critical error.png"));
                            manageExamTabStatusLabel.setText("Error!");
                        } else if (status == SUCCESS) {

                            manageExamTabStatusImageView.setImage(new Image("/png/success.png"));
                            manageExamTabStatusLabel.setText("Successfully created Room Allocation!");
                        } else {

                            manageExamTabStatusImageView.setImage(new Image("/png/error.png"));
                            manageExamTabStatusLabel.setText("Room Allocation for this exam already exists!");
                        }
                    }
                });
            }
        }
    }

    @FXML
    private void handleRoomAllocationGeneratePdfButtonAction() {

        ExamDetails examDetails = manageExamTableView.getSelectionModel().getSelectedItem();

        if (examDetails != null) {

            manageExamTabMainGridPane.setOpacity(0.2);
            manageExamTabStatusStackPane.setVisible(true);
            manageExamTabProgressIndicator.setVisible(true);
            manageExamTabMsgLabel.setVisible(true);

            manageExamTabMsgLabel.setText("Creating PDFs now.Please do not close this window.");

            Task<Boolean> createRoomAllocationPdfTask = pdfService.getCreateRoomAllocationPdfTask(examDetails);
            new Thread(createRoomAllocationPdfTask).start();

            createRoomAllocationPdfTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    boolean status = createRoomAllocationPdfTask.getValue();

                    manageExamTabProgressIndicator.setVisible(false);
                    manageExamTabStatusImageView.setVisible(true);
                    manageExamTabStatusLabel.setVisible(true);
                    manageExamTabMsgLabel.setText("Click anywhere to proceed !");

                    if (status) {

                        manageExamTabStatusImageView.setImage(new Image("/png/success.png"));
                        manageExamTabStatusLabel.setText("Successfully created Room Allocation PDF!");
                    } else {

                        manageExamTabStatusImageView.setImage(new Image("/png/critical error.png"));
                        manageExamTabStatusLabel.setText("Unable to create Room Allocation pdf!");
                    }
                }
            });
        }
    }

    @FXML
    private void handleRoomAllocationViewButtonAction() {

    }

    @FXML
    private void handleRoomAllocationDeleteButtonAction() {

    }

    @FXML
    private void handleSeatArrangementGeneratePdfButtonAction() {

        ExamDetails examDetails = manageExamTableView.getSelectionModel().getSelectedItem();

        if (examDetails != null) {

            manageExamTabMainGridPane.setOpacity(0.2);
            manageExamTabStatusStackPane.setVisible(true);
            manageExamTabProgressIndicator.setVisible(true);
            manageExamTabMsgLabel.setVisible(true);
            manageExamTabMsgLabel.setText("Gathering data now to create pdf. Please do not close this window.");

            String additionalQuery = "ORDER BY int_ralloc_id";
            Task<List<RoomAllocation>> roomAllocationTask = examService.getRoomAllocationTask(additionalQuery,
                    examDetails.getExamDetailsId());
            new Thread(roomAllocationTask).start();

            roomAllocationTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    manageExamTabMsgLabel.setText("Creating PDFs now.Please do not close this window.");

                    Task<Boolean> generateSeatArrangementPdfTask = pdfService.getCreateSeatArrangementPdfTask
                            (roomAllocationTask.getValue(), examDetails);
                    new Thread(generateSeatArrangementPdfTask).start();

                    generateSeatArrangementPdfTask.setOnSucceeded(new EventHandler<>() {

                        @Override
                        public void handle(WorkerStateEvent event) {

                            boolean status = generateSeatArrangementPdfTask.getValue();

                            manageExamTabProgressIndicator.setVisible(false);
                            manageExamTabStatusImageView.setVisible(true);
                            manageExamTabStatusLabel.setVisible(true);
                            manageExamTabMsgLabel.setText("Click anywhere to proceed! ");

                            if (status) {

                                manageExamTabStatusImageView.setImage(new Image("/png/success.png"));
                                manageExamTabStatusLabel.setText("Successfully created Seating Arrangement PDF!");
                            } else {

                                manageExamTabStatusImageView.setImage(new Image("/png/critical error.png"));
                                manageExamTabStatusLabel.setText("Unable to create Seating Arrangement pdf!");
                            }
                        }
                    });
                }
            });

        }
    }

    @FXML
    private void handleSeatArrangementViewButtonAction() {

    }

    @FXML
    private void handleInvigilationDutyCreateButtonAction() {

        ExamDetails examDetails = manageExamTableView.getSelectionModel().getSelectedItem();

        if (examDetails != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Do you really want to create Invigilation Duty for Exam ID "
                    + examDetails.getExamDetailsId());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                manageExamTabMainGridPane.setOpacity(0.2);
                manageExamTabStatusStackPane.setVisible(true);
                manageExamTabProgressIndicator.setVisible(true);
                manageExamTabMsgLabel.setVisible(true);
                manageExamTabMsgLabel.setText("Invigilation Duty is being generated.Please do not close this window.");

                Task<Integer> createInvigilationDutyTask = examCommand.getCreateInvigilationDutyTask
                        (examDetails);
                new Thread(createInvigilationDutyTask).start();

                createInvigilationDutyTask.setOnSucceeded(new EventHandler<>() {

                    @Override
                    public void handle(WorkerStateEvent event) {

                        manageExamTabProgressIndicator.setVisible(false);
                        manageExamTabStatusImageView.setVisible(true);
                        manageExamTabStatusLabel.setVisible(true);
                        manageExamTabMsgLabel.setText("Click anywhere to proceed!  ");

                        int status = createInvigilationDutyTask.getValue();

                        if (status == DATABASE_ERROR) {

                            manageExamTabStatusImageView.setImage(new Image("/png/critical error.png"));
                            manageExamTabStatusLabel.setText("Error!");
                        } else if (status == SUCCESS) {

                            manageExamTabStatusImageView.setImage(new Image("/png/success.png"));
                            manageExamTabStatusLabel.setText("Successfully created Invigilation Duty!");
                        } else {

                            manageExamTabStatusImageView.setImage(new Image("/png/error.png"));
                            manageExamTabStatusLabel.setText("Invigilation Duty for this exam already exists!");
                        }
                    }
                });
            }
        }
    }

    @FXML
    private void handleInvigilationDutyGeneratePdfButtonAction() {

        ExamDetails examDetails = manageExamTableView.getSelectionModel().getSelectedItem();

        if (examDetails != null) {

            manageExamTabMainGridPane.setOpacity(0.2);
            manageExamTabStatusStackPane.setVisible(true);
            manageExamTabProgressIndicator.setVisible(true);
            manageExamTabMsgLabel.setVisible(true);
            manageExamTabMsgLabel.setText("Gathering data now to create pdf. Please do not close this window. ");

            Task<List<InvigilationDuty>> invigilationDutyDataTask = examService.getInvigilationDutyDataTask(
                    examDetails.getExamDetailsId());
            new Thread(invigilationDutyDataTask).start();

            invigilationDutyDataTask.setOnSucceeded(new EventHandler<>() {

                @Override
                public void handle(WorkerStateEvent event) {

                    manageExamTabMsgLabel.setText("Creating PDFs now.Please do not close this window. ");


                    Task<Boolean> createInvigilationDutyPdfTask = pdfService.getCreateInvigilationDutyPdfTask
                            (invigilationDutyDataTask.getValue(), examDetails);
                    new Thread(createInvigilationDutyPdfTask).start();

                    createInvigilationDutyPdfTask.setOnSucceeded(new EventHandler<>() {

                        @Override
                        public void handle(WorkerStateEvent event) {

                            boolean status = createInvigilationDutyPdfTask.getValue();

                            manageExamTabProgressIndicator.setVisible(false);
                            manageExamTabStatusImageView.setVisible(true);
                            manageExamTabStatusLabel.setVisible(true);
                            manageExamTabMsgLabel.setText("Click anywhere to proceed ! ");

                            if (status) {

                                manageExamTabStatusImageView.setImage(new Image("/png/success.png"));
                                manageExamTabStatusLabel.setText("Successfully created Invigilation Duty Chart PDF!");
                            } else {

                                manageExamTabStatusImageView.setImage(new Image("/png/critical error.png"));
                                manageExamTabStatusLabel.setText("Unable to create Invigilation Duty Chart PDF!");
                            }
                        }
                    });
                }
            });
        }
    }

    @FXML
    private void handleInvigilationDutyViewButtonAction() {

    }

    @FXML
    private void handleInvigilationDutyDeleteButtonAction() {

    }

    @FXML
    private void handleManageExamStatusStackPaneOnMouseClicked() {

        if (!manageExamTabProgressIndicator.isVisible()) {

            deactivateManageExamStatusStackPane();
        }
    }

    private void initExamTableViewCols() {

        examDetailsIdCol.setCellValueFactory(new PropertyValueFactory<>("examDetailsId"));
        examTypeCol.setCellValueFactory(new PropertyValueFactory<>("examType"));
        semesterTypeCol.setCellValueFactory(new PropertyValueFactory<>("semesterType"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }

    private void populateExamTable() {

        Task<List<ExamDetails>> examDetailsTask = examService.getExamDetailsTask("");
        new Thread(examDetailsTask).start();

        examDetailsTask.setOnSucceeded(new EventHandler<>() {

            @Override
            public void handle(WorkerStateEvent event) {

                List<ExamDetails> examDetailsList = examDetailsTask.getValue();

                examDetailsObsList.setAll(examDetailsList);

                manageExamTableView.setItems(examDetailsObsList);
            }
        });
    }

    @SuppressWarnings("Duplicates")
    private void deactivateManageExamStatusStackPane() {

        manageExamTabMainGridPane.setOpacity(1);
        manageExamTabStatusStackPane.setVisible(false);
        manageExamTabProgressIndicator.setVisible(false);
        manageExamTabStatusImageView.setVisible(false);
        manageExamTabStatusLabel.setVisible(false);
        manageExamTabMsgLabel.setVisible(false);
    }
}