package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static util.ConstantsUtil.*;

/**
 * Service class to get user, update user pass and get user contact details, and set login timestamp of the user.
 *
 * @author Avik Sarkar
 */
public class UserService {

    /*--------------------------------Declaration of variables---------------------------------------*/

    private DatabaseHelper databaseHelper;

    /*------------------------------------End of Declaration-----------------------------------------*/

    /**
     * Default public constructor to initialize database helper.
     */
    public UserService() {

        databaseHelper = new DatabaseHelper();
    }

    /**
     * This method is used to get a single userTask object which is used to get users login details.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_login_Details.
     * @return A userLoginDetailsTask which can be used to get a list of User login details from the DB in a
     * separate thread.
     */
    public Task<List<User>> getUsersLoginDetailsTask(String additionalQuery, final String... params) {

        final String query = "SELECT * from t_login_details " + additionalQuery;

        Task<List<User>> userLoginDetailsTask = new Task<>() {

            @Override
            protected List<User> call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                //each item in the list is a single User login details
                List<User> list = new ArrayList<>();

                /*
                v_user_id is the primary key, total items in the map will always be equal to no of
                v_user_id retrieved
                 */
                for (int i = 0; i < map.get("v_user_id").size(); i++) {

                    User user = new User();

                    user.setUserId(map.get("v_user_id").get(i));
                    user.setPassword(map.get("v_pass").get(i));
                    user.setHashAlgo(map.get("v_hash_algo").get(i));
                    user.setGid(map.get("int_gid").get(i));
                    user.setLastLoginTimeStamp(map.get("ts_login_timestamp").get(i));

                    //a single user login details is added to the list
                    list.add(user);
                }

                //a list of user login details
                return list;
            }
        };
        return userLoginDetailsTask;
    }

    /**
     * This method is used to get a updateUserTask which is used to update password single User in the DB.
     *
     * @param user The User password to be updated.
     * @return A updateUserPassTask instance which is used to update password of  a single User in the DB in a
     * separate thread.
     */
    public Task<Integer> getUpdateUserPassTask(final User user) {

        final String sql = "UPDATE t_login_details SET v_pass=? where v_user_id=?";

        Task<Integer> updateUserPassTask = new Task<>() {

            @Override
            protected Integer call() {

                //holds the status of updation of user password in the DB, i.e success or failure
                int tLoginDetailsStatus = databaseHelper.updateDelete
                        (sql, user.getPassword(), user.getUserId());

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tLoginDetailsStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tLoginDetailsStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return updateUserPassTask;
    }

    /**
     * This method is used to get a task which can be used to get the First name and email of the User.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement.
     * @return A task which can be used to get list of User contact details like Name and Email ID.
     */
    public Task<List<User>> getUserContactTask(String additionalQuery, final String... params) {

        final String query = "SELECT v_user_id, v_first_name, v_email_id from t_user_contact_details "
                + additionalQuery;

        Task<List<User>> userTask = new Task<>() {

            @Override
            protected List<User> call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                //each item in the list is a single User Contact details
                List<User> list = new ArrayList<>();

                /*
                v_user_id is the primary key, total items in the map will always be equal to no of
                v_user_id retrieved
                 */
                for (int i = 0; i < map.get("v_user_id").size(); i++) {

                    User user = new User();

                    user.setUserId(map.get("v_user_id").get(i));
                    user.setName(map.get("v_first_name").get(i));
                    user.setEmail(map.get("v_email_id").get(i));

                    //a single user contact details is added to the list
                    list.add(user);
                }

                //a list of user contact details
                return list;
            }
        };
        return userTask;
    }

    /**
     * This method is used to get a task which can be used to update the timestamp of the Login of the user.
     *
     * @param user The user object whose Login timestamp will be updated.
     * @return A task which can be used to update the timestamp of the Login of the user in a separate thread.
     */
    public Task<Integer> getUpdateUserLastLoginTimeStampTask(final User user) {

        final String sql = "UPDATE t_login_details SET ts_login_timestamp=? where v_user_id=?";

        Task<Integer> updateUserLastLoginTimeStampTask = new Task<>() {

            @Override
            protected Integer call() {

                //get the current time of the system
                Timestamp timestamp = new Timestamp((new Date()).getTime());

                //holds the status of updation of user login timestamp in the DB, i.e success or failure
                int tLoginDetailsStatus = databaseHelper.updateDelete
                        (sql, String.valueOf(timestamp), user.getUserId());

                /*returns an integer holding the different status i.e success, failure etc.*/
                if (tLoginDetailsStatus == DATABASE_ERROR) {

                    return DATABASE_ERROR;
                } else if (tLoginDetailsStatus == SUCCESS) {

                    return SUCCESS;
                } else {

                    return DATA_INEXISTENT_ERROR;
                }
            }
        };
        return updateUserLastLoginTimeStampTask;
    }
}
