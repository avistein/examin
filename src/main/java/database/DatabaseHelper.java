package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import static util.ConstantsUtil.*;
/**
 * Helper class to create,close database connection and
 * implement SELECT,INSERT,UPDATE,DELETE.
 *
 * @author Avik Sarkar
 */
public class DatabaseHelper {

    private Connection con;
    private String url;
    private String username;
    private String password;

    /**
     * Public constructor to get the db.properties file and setup the DB connection
     */
    public DatabaseHelper(){
        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream("src/main/resources/configs/db.properties");
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
     * This method creates a Database connection
     */
    private void openConnection(){

        try {
            con = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * This method closes a Database connection
     */
    private void closeConnection(){
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
     * @param params Varags for PreparedStatement containing
     *               normally WHERE clause parameters
     * @return A map with each column name as key and column values as ArrayList
     */
    public Map<String, List<String>> execQuery(String query, String ...params){

        Map<String, List<String>> map = null;

        openConnection();

        try(PreparedStatement stmt = con.prepareStatement(query)) {

            if(params.length != 0){
                for(int i = 0; i < params.length; i++)
                    stmt.setString(i+1 , params[i]);
            }

            ResultSet resultSet = stmt.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int noOfColumns = resultSetMetaData.getColumnCount();
            map = new HashMap<>(noOfColumns);
            for(int i = 1; i <= noOfColumns; i++)
                map.put(resultSetMetaData.getColumnName(i), new ArrayList<>());

            while(resultSet.next()){
                for(int i = 1; i <= noOfColumns; i++){
                    //if(resultSet.getString(i) != null)
                    map.get(resultSetMetaData.getColumnName(i)).add(resultSet.getString(i));
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return map;
    }


    /**
     * Method to execute a bunch of INSERT commands.
     * @param sql The sql command to be executed containing INSERT statements.
     * @param list The list containing list of entity attributes to be inserted.
     * @return Status of the operation.
     */
    public int batchInsert(String sql, List<List<String>> list){

        int[] status = new int[list.size()];
        openConnection();
        try(PreparedStatement stmt = con.prepareStatement(sql)){

            for(List<String> row : list){
                int i = 1;
                for(String data : row) {
                    stmt.setString(i++, data);
                }
                stmt.addBatch();
            }
            status = stmt.executeBatch();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        finally {
            closeConnection();
        }
        if(list.size() == status.length) {
            int rowsAffected = 0;
            for (int i : status) {
                if(i == 1)
                    rowsAffected++;
            }
            if(rowsAffected == list.size())
                return SUCCESS;
            else
                return DATA_ALREADY_EXIST_ERROR;
        }
        return DATABASE_ERROR;
    }

    /**
     * Method to execute a single INSERT command.
     * @param sql The sql command to be executed containing INSERT statements.
     * @param params Varags for PreparedStatement containing
     *      *        normally VALUES parameters.
     * @return Status of the operation.
     */
    @SuppressWarnings("Duplicates")
    public int insert(String sql, String ...params){

        int rowsAffected = 0;
        int status = DATABASE_ERROR;
        openConnection();
        try(PreparedStatement stmt = con.prepareStatement(sql)){
            if(params.length != 0){
                for(int i = 0; i < params.length; i++)
                    stmt.setString(i+1 , params[i]);
            }
            rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0)
                status = SUCCESS;
        }
        catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            status = DATA_ALREADY_EXIST_ERROR;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return status;
    }


    @SuppressWarnings("Duplicates")
    public int updateDelete(String sql, String ...params){

        int rowsAffected = 0;
        int status = DATABASE_ERROR;
        openConnection();
        try(PreparedStatement stmt = con.prepareStatement(sql)){
            if(params.length != 0){
                for(int i = 0; i < params.length; i++)
                    stmt.setString(i+1 , params[i]);
            }
            rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0)
                status = SUCCESS;
            else
                status = DATA_INEXISTENT_ERROR;
        }
        catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            status = DATA_DEPENDENCY_ERROR;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
        return status;
    }

}

