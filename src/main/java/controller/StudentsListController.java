package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.StudentEntity;
import model.StudentListModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentsListController{

    private StudentListModel studentListModel;

    private ObservableList<StudentEntity> list = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> degreeComboBox;

    @FXML
    private ComboBox<String> disciplineComboBox;

    @FXML
    private ComboBox<Integer> semesterComboBox;

    @FXML
    private TableView<StudentEntity> studentTable;

    @FXML
    private TableColumn<StudentEntity, String> regIdCol;

    @FXML
    private TableColumn<StudentEntity, String> rollNoCol;

    @FXML
    private TableColumn<StudentEntity, String> nameCol;

    @FXML
    private TableColumn<StudentEntity, String> guardianNameCol;

    @FXML
    private TableColumn<StudentEntity, String> contactNoCol;

    @FXML
    public void initialize() {
        studentListModel = new StudentListModel();
        final String query = "SELECT v_degree from t_course";
        Map<String, List<String>> map = studentListModel.getData(query);
        if(map != null) {
            List<String> items = new ArrayList<>(map.get("v_degree"));
            ObservableList<String> options = FXCollections.observableArrayList(items);
            degreeComboBox.setItems(options);
        }
    }


    @FXML
    private void  handleDegreeComboBox(){
        final String query = "SELECT v_discipline from t_course where v_degree=?";
        Map<String, List<String>> map = studentListModel.getData(query, degreeComboBox.getValue());
        List<String> items = new ArrayList<>(map.get("v_discipline"));
        ObservableList<String> options = FXCollections.observableArrayList(items);
        disciplineComboBox.setItems(options);
    }

    @FXML
    private void handleDisciplineComboBox(){

        final String query = "SELECT v_duration from t_course where v_degree=? and v_discipline=?";
        Map<String, List<String>> map = studentListModel.getData(query, degreeComboBox.getValue(), disciplineComboBox.getValue());
        int totalSemesters = Integer.parseInt(map.get("v_duration").get(0));
        List<Integer> items = new ArrayList<>();
        for(int i = 1; i <= totalSemesters; i++)
            items.add(i);
        ObservableList<Integer> options = FXCollections.observableArrayList(items);
        semesterComboBox.setItems(options);
    }

    @FXML
    private void handleSemesterComboBox(){
        initCol();
        populateTable();
    }

    private void initCol(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        regIdCol.setCellValueFactory(new PropertyValueFactory<>("regId"));
        rollNoCol.setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        guardianNameCol.setCellValueFactory(new PropertyValueFactory<>("guardianName"));
        contactNoCol.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
    }

    private void populateTable(){
        list.clear();
        final String query = "SELECT v_reg_id, v_roll_no, v_first_name, v_middle_name, v_last_name," +
                "v_contact_no, v_father_guardian_name from t_student natural join t_course natural join" +
                " t_batch_details where v_degree=? and v_discipline=? and v_semester=? ";
        Map<String, List<String>> map = studentListModel.getData(query, degreeComboBox.getValue(), disciplineComboBox.getValue(), semesterComboBox.getValue().toString());
        for(int i = 0; i < map.get("v_reg_id").size(); i ++){
            String regId = map.get("v_reg_id").get(i);
            String rollNo = map.get("v_roll_no").get(i);
            String name = map.get("v_first_name").get(i) +" " +map.get("v_middle_name").get(i)+ " "
                    +map.get("v_last_name").get(i);
            String contactNo = map.get("v_contact_no").get(i);
            String guardianName = map.get("v_father_guardian_name").get(i);

            list.add(new StudentEntity(name, regId, rollNo, contactNo, guardianName));
        }
        studentTable.setItems(list);
    }
}
