package database;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DatabaseHelper {

    private Connection con;
    private String url;
    private String username;
    private String password;

    public DatabaseHelper(){
        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream("src/main/configs/db.properties");
            props.load(in);
            in.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        String driver = props.getProperty("jdbc.driver");
        try{
            if (driver != null) {
                Class.forName(driver);
            }
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        url = props.getProperty("jdbc.url");
        username = props.getProperty("jdbc.username");
        password = props.getProperty("jdbc.password");
    }

    /**
     * Opens a Database connection
     */
    public void openConnection(){

        try {
            con = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Closes a Database connection
     */
    public void closeConnection(){
        try {
            con.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Executes a select query using PreparedStatement
     * @param query SQL Select query
     * @param params Columns to retrieve
     * @return A map with each column name as key and column values as ArrayList
     */
    public Map<String, List<String>> execQuery(String query, String ...params){

        Map<String, List<String>> map = null;

        try(PreparedStatement stmt = con.prepareStatement(query)) {

            if(params.length != 0){
                for(int i = 0; i < params.length; i++)
                    stmt.setString(i+1 , params[i]);
            }

            ResultSet resultSet = stmt.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int noOfcolumns = resultSetMetaData.getColumnCount();
            map = new HashMap<>(noOfcolumns);
            for(int i = 1; i <= noOfcolumns; i++)
                map.put(resultSetMetaData.getColumnName(i), new ArrayList<>());

            while(resultSet.next()){
                for(int i = 1; i <= noOfcolumns; i++){
                    //if(resultSet.getString(i) != null)
                        map.get(resultSetMetaData.getColumnName(i)).add(resultSet.getString(i));
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return map;
    }

    public void load(File file, String tableName){
        openConnection();
       final String query = "LOAD DATA INFILE '"+file.getName()+"' INTO TABLE "
               + tableName + " FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 LINES ";
       try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.execute();
       }
       catch (SQLException e){
           e.printStackTrace();
       }
        closeConnection();
    }
}

