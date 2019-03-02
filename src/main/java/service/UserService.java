package service;

import database.DatabaseHelper;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserService {

    private DatabaseHelper databaseHelper;

    public UserService(){
        databaseHelper = new DatabaseHelper();
    }
    public List<User> getLoginDetails(String additionalQuery, String ...params){

        String query = "SELECT * from t_login_details " + additionalQuery;
        databaseHelper.openConnection();
        List<User> list = new ArrayList<>();
        Map<String, List<String>> map = databaseHelper.execQuery(query, params);
        for (int i = 0; i < map.get("v_user_id").size(); i++) {
            String userId = map.get("v_user_id").get(i);
            String password = map.get("v_pass").get(i);
            String hashAlgo = map.get("v_hash_algo").get(i);
            String gid = map.get("v_gid").get(i);
            list.add(new User(userId, password, hashAlgo, gid));
        }
        databaseHelper.closeConnection();
        return list;
    }
}
