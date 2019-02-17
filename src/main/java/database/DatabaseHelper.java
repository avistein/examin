package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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

    public Connection openConnection(){

        try {
            con = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return con;
    }

    public void closeConnection(Connection connection){
        try {
            connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}

