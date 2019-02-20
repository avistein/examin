package model;

import database.DatabaseHelper;

import java.util.List;
import java.util.Map;

public class AdminModel {

    private DatabaseHelper databaseHelper;

    public AdminModel(){
        databaseHelper = new DatabaseHelper();
        databaseHelper.openConnection();
    }

    public Map<String, List<String>> getData(String query, String ...params){

        return databaseHelper.execQuery(query, params);
    }
}
