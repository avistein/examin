package database;

import service.FileHandlingService;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Helper class to create,close database connection and
 * implement SELECT,INSERT,UPDATE,DELETE.
 *
 * @author Avik Sarkar
 */
public class DatabaseHelper {

    /*--------------------------------Declaration of variables----------------------------------*/

    private Connection con;

    private String url;
    private String username;
    private String password;

    private FileHandlingService fileHandlingService;
    /*------------------------------------End of Declaration-------------------------------------*/

    /**
     * Public constructor to get the properties from the db.properties file and setup the DB connection.
     */
    public DatabaseHelper() {

        fileHandlingService = new FileHandlingService();

        Map<String, String> propMap = fileHandlingService.loadPropertiesValuesFromPropertiesFile
                ("db.properties", "jdbc.driver", "jdbc.url", "jdbc.username", "jdbc.password");

        //get the JDBC driver
        String driver = propMap.get("jdbc.driver");

        try {

            //load the driver class if the driver exists
            if (driver != null) {

                Class.forName(driver);
            }
        } catch (ClassNotFoundException e) {

            //if the class of the JDBC driver not found, throw an exception and print stack trace
            e.printStackTrace();
        }

        //get the url where the database server is running
        url = propMap.get("jdbc.url");

        //get the username to connect to the database server
        username = propMap.get("jdbc.username");

        //get the password to connect to the database server
        password = propMap.get("jdbc.password");
    }

    /**
     * This method creates a Database connection.
     */
    private void openConnection() {

        try {

            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    /**
     * This method closes a Database connection
     */
    private void closeConnection() {
        try {

            con.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    /**
     * Executes a select query using PreparedStatement.
     *
     * @param query  SQL Select query.
     * @param params Varags for PreparedStatement containing
     *               normally WHERE clause parameters.
     * @return A map with each column name as key and column values as ArrayList.
     */
    public Map<String, List<String>> execQuery(String query, String... params) {

        /*
        The PreparedStatement.executeQuery() returns a ResultSet object containing the result of the query.
        The ResultSet object probably have this type of structure for example:

        At Index0 : {"B.Tech", "CSE", "8"} -> row major order
        At Index1 : {"B.Tech", "ECE", "8"} -> row major order
        At Index2 : {"B.Sc.", "CS", "6"} -> row major order

        Returning a ResultSet object can lead to memory leak.To avoid that we are storing ResultSet data to a HashMap
        and then returning that.The structure of the HashMap will be of the form :

        Key : The name of the column in the database.
        Value : A list of strings holding the data of the corresponding column in the database.

        For example, the data of the ResultSet object will be stored as followed in the HashMap :

        "v_degree" : {"B.Tech", "B.Tech", "B.Sc."} -> column major order
        "v_discipline" : {"CSE", "ECE", "CS"} -> column major order
        "v_duration" : {"8", "8", "6"} -> column major order
         */
        Map<String, List<String>> map = null;

        //open a database connection
        openConnection();

        //preparedStatement is used to resist against SQLInjection attack
        try (PreparedStatement stmt = con.prepareStatement(query)) {

            //if any parameters are there set the PreparedStatement instance with them
            if (params.length != 0) {
                for (int i = 0; i < params.length; i++) {

                    stmt.setString(i + 1, params[i]);
                }
            }

            //execute the query and store the result in a ResultSet instance
            ResultSet resultSet = stmt.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            //get the total no of columns in the ResultSet object
            int noOfColumns = resultSetMetaData.getColumnCount();

            //initialize the HashMap with total data = no of columns
            map = new HashMap<>(noOfColumns);

            //for each column initialize an ArrayList
            for (int i = 1; i <= noOfColumns; i++)

                map.put(resultSetMetaData.getColumnName(i), new ArrayList<>());

            //iterate data row wise
            while (resultSet.next()) {

                //for a single row of data in ResultSet, add each value of the row data to the corresponding columns
                for (int i = 1; i <= noOfColumns; i++) {

                    map.get(resultSetMetaData.getColumnName(i)).add(resultSet.getString(i));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        } finally {

            //close the DB connection
            closeConnection();
        }
        return map;
    }


    /**
     * Method to execute a bunch of INSERT commands.
     *
     * @param sql  The sql command to be executed containing INSERT statements.
     * @param list The list containing list of entity attributes to be inserted.
     * @return Status of the INSERT operation or the total no of insertions in the database.
     * @link https://docs.oracle.com/javase/8/docs/api/java/sql/BatchUpdateException.html#getUpdateCounts--
     */
    public int batchInsertUpdateDelete(String sql, List<List<String>> list) {

        //stores the status for each insertion
        List<Integer> statusList = new ArrayList<>();

        //open a database connection
        openConnection();

        //preparedStatement is used to resist against SQLInjection attack
        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            //iterate a single insert data, to set the parameters of it
            for (List<String> row : list) {

                //initialize the parameter index
                int i = 1;

                //for each insert statement , set the parameters
                for (String data : row) {

                    stmt.setString(i++, data);
                }

                //add each insert statement to the batch
                stmt.addBatch();
            }

            //execute the batch and store the status in the array
            int[] status = stmt.executeBatch();

            //store status of each insert statement in the arrayList
            for (int i : status) {

                statusList.add(i);
            }

        }

        //catch exception for example, due to duplicate data insertion in the DB
        catch (BatchUpdateException e) {

            /*
            If the JDBC driver continues to process commands after an error, one of the following for every command in
            the batch :
            1.an update count
            2.Statement.SUCCESS_NO_INFO to indicate that the command executed successfully but the number of rows
              affected is unknown
            3.Statement.EXECUTE_FAILED to indicate that the command failed to execute successfully,this is mostly due
              to duplicate data insertion
              see @link for more
             */
            for (int i : e.getUpdateCounts()) {

                statusList.add(i);
            }
            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            //close the DB connection
            closeConnection();
        }

        //if there is no error in database, then statusList shouldn't be empty
        if (!statusList.isEmpty()) {

            int rowsAffected = 0;

            //count the total no actual insertions in the DB
            for (int i : statusList) {

                //if successfully inserted to the DB, then i = 1, since 1 row affected only for each INSERT statement
                if (i == 1) {

                    rowsAffected++;
                }
            }
            return rowsAffected;
        }
        return DATABASE_ERROR;
    }

    /**
     * Method to execute a single INSERT command.
     *
     * @param sql    The sql command to be executed containing INSERT statements.
     * @param params Varags for PreparedStatement containing normally VALUES parameters.
     * @return Status of the INSERT operation.
     */
    @SuppressWarnings("Duplicates")
    public int insert(String sql, String... params) {

        //no of rows inserted to the DB
        int rowsAffected;

        //default status
        int status = DATABASE_ERROR;

        //open a DB connection
        openConnection();

        //preparedStatement is used to resist against SQLInjection attack
        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            //if any parameters are there set the PreparedStatement instance with them
            if (params.length != 0) {

                for (int i = 0; i < params.length; i++) {

                    stmt.setString(i + 1, params[i]);
                }
            }

            //if successfully execute, the no of rows inserted will be returned
            rowsAffected = stmt.executeUpdate();

            //if any row is inserted , return success
            if (rowsAffected > 0) {

                status = SUCCESS;
            }
        } catch (SQLIntegrityConstraintViolationException e) {

            //if the data to be inserted already exists in the DB , throw an exception and set status to failure
            e.printStackTrace();
            status = DATA_ALREADY_EXIST_ERROR;
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {

            //close the DB connection
            closeConnection();
        }
        return status;
    }


    /**
     * This method is used to perform UPDATE or DELETE operation in the DB.
     *
     * @param sql    The sql command to be executed containing UPDATE or DELETE statements.
     * @param params Varags for PreparedStatement containing parameters , usually used to denote the columns to be
     *               updated or deleted or on which data the operation will be performed.
     * @return Status of the UPDATE or DELETE operation.
     */
    @SuppressWarnings("Duplicates")
    public int updateDelete(String sql, String... params) {

        //no of rows updated or deleted in the DB
        int rowsAffected;

        //default status
        int status = DATABASE_ERROR;

        //open a DB connection
        openConnection();

        //preparedStatement is used to resist against SQLInjection attack
        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            //if any parameters are there set the PreparedStatement instance with them
            if (params.length != 0) {

                for (int i = 0; i < params.length; i++) {

                    stmt.setString(i + 1, params[i]);
                }
            }

            //if successfully executed, the no of rows modified will be returned
            rowsAffected = stmt.executeUpdate();

            /*
            if any row is inserted , return success otherwise the data doesn't exist in the DB
            */
            if (rowsAffected > 0) {

                status = SUCCESS;
            } else {

                status = DATA_INEXISTENT_ERROR;
            }
        } catch (SQLIntegrityConstraintViolationException e) {

            /*
            if any other data in thw DB is dependent on the data to be deleted, throw an exception and
            set status to failure
             */
            e.printStackTrace();
            status = DATA_DEPENDENCY_ERROR;
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {

            //close the DB connection
            closeConnection();
        }
        return status;
    }
}

