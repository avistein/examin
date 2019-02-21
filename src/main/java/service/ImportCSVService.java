package service;

import database.DatabaseHelper;

import java.io.File;

public class ImportCSVService {

    private DatabaseHelper databaseHelper;

    public ImportCSVService(){
            databaseHelper = new DatabaseHelper();
    }

    public void loadCSV(File file, String tableName){
        databaseHelper.load(file, tableName);
    }
}
