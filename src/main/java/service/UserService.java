package service;

import database.DatabaseHelper;
import javafx.concurrent.Task;
import model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Avik Sarkar
 */
public class UserService {

    private DatabaseHelper databaseHelper;

    public UserService(){
        databaseHelper = new DatabaseHelper();
    }

    public Task<List<User>> getUsersTask(String additionalQuery, final String ...params){

        final String query = "SELECT * from t_login_details " + additionalQuery;
        Task<List<User>> userTask = new Task<>() {
            @Override
            protected List<User> call(){
                Map<String, List<String>> map = databaseHelper.execQuery(query, params);
                List<User> list = new ArrayList<>();
                for (int i = 0; i < map.get("v_user_id").size(); i++) {

                    User user = new User();

                    user.setUserId(map.get("v_user_id").get(i));
                    user.setPassword(map.get("v_pass").get(i));
                    user.setHashAlgo(map.get("v_hash_algo").get(i));
                    user.setGid(map.get("v_gid").get(i));

                    list.add(user);
                }
                return list;
            }
        };
        return userTask;
    }
}
