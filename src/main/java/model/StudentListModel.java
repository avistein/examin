package model;

import database.DatabaseHelper;

import java.util.List;
import java.util.Map;

public class StudentListModel {

    private DatabaseHelper databaseHelper;

    public StudentListModel(){
        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();
    }

    public Map<String, List<String>> getData(String query, String ...params){

        return databaseHelper.execQuery(query, params);
    }
}
