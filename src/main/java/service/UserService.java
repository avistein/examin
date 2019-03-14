package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class to get user.
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
     * This method is used to get a single userTask object which is used to get users details.
     *
     * @param additionalQuery Includes WHERE clause or any other extra specific query details.
     * @param params          Parameters for the PreparedStatement i.e. basically column names of t_login_Details.
     * @return A userTask which can be used to get a list of User details from the DB in a separate thread.
     */
    public Task<List<User>> getUsersTask(String additionalQuery, final String... params) {

        final String query = "SELECT * from t_login_details " + additionalQuery;

        Task<List<User>> userTask = new Task<>() {

            @Override
            protected List<User> call() {

                Map<String, List<String>> map = databaseHelper.execQuery(query, params);

                //each item in the list is a single User details
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
                    user.setGid(map.get("v_gid").get(i));

                    //a single user details is added to the list
                    list.add(user);
                }

                //a list of user details
                return list;
            }
        };
        return userTask;
    }
}
