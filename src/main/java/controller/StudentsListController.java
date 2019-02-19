package controller;

import database.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StudentsListController{


    private DatabaseHelper databaseHelper;

    @FXML
    private ComboBox<String> degreeComboBox;

    @FXML
    private ComboBox<String> disciplineComboBox;

    @FXML
    private ComboBox<Integer> semesterComboBox;

    @FXML
    public void initialize() {
        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();
        final String query = "SELECT  v_degree from t_course";
        Map<String, List<String>> map;map = databaseHelper.execQuery(query);
        if(map != null) {
            List<String> items = new ArrayList<>(map.get("v_degree"));
            ObservableList<String> options = FXCollections.observableArrayList(items);
            degreeComboBox.setItems(options);
        }
    }


    @FXML
    private void  handleDegreeComboBox(){
        final String query = "SELECT v_discipline from t_course where v_degree=?";
        Map<String, List<String>> map = databaseHelper.execQuery(query, degreeComboBox.getValue());
        List<String> items = new ArrayList<>(map.get("v_discipline"));
        ObservableList<String> options = FXCollections.observableArrayList(items);
        disciplineComboBox.setItems(options);
    }

    @FXML
    private void handleDisciplineComboBox(){
        final String query = "SELECT v_duration from t_course where v_degree=? and v_discipline=?";
        Map<String, List<String>> map = databaseHelper.execQuery(query, degreeComboBox.getValue(), disciplineComboBox.getValue());
        int totalSemesters = Integer.parseInt(map.get("v_duration").get(0));
        List<Integer> items = new ArrayList<>();
        for(int i = 1; i <= totalSemesters; i++)
            items.add(i);
        ObservableList<Integer> options = FXCollections.observableArrayList(items);
        semesterComboBox.setItems(options);
    }

    @FXML
    private void handleSemesterComboBox(){

    }
}
