package model;

import database.DatabaseHelper;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;
import java.util.Map;

public class LoginModel {

    private DatabaseHelper databaseHelper;

    public LoginModel() {
        databaseHelper = new DatabaseHelper();
    }

    /**
     * Authenticates a user to enter the system
     *
     * @param username      Username entered to login
     * @param inputPassword Password entered to login
     * @return The status of the authentication
     * @link http://www.mindrot.org/projects/jBCrypt
     */
    public int authenticateLogin(String username, String inputPassword) {

        final String query = "select v_pass,v_gid from t_login_details " +
                "where v_user_id=?";

        int status = 0;
        String retrievedPassword ;

        databaseHelper.openConnection();

        int gid;

        Map<String , List<String>> map = databaseHelper.execQuery(query, username);
//        for(Map.Entry<String, List<String>> entry : map.entrySet()){
//            for(String val : entry.getValue()){
//                if(entry.getKey().equals("v_gid"))
//                    gid = Integer.parseInt(val);
//                if(entry.getKey().equals("v_pass"))
//                    retrievedPassword = val;
//            }
//        }
        if (map != null) {

            retrievedPassword = map.get("v_pass").get(0);
            gid = Integer.parseInt(map.get("v_gid").get(0));

            if (BCrypt.checkpw(inputPassword, retrievedPassword)) {

                switch (gid) {

                    case 1:
                        status = 1;
                        break;

                    case 2:
                        status = 2;
                        break;

                    case 3:
                        status = 3;
                        break;

                    case 4:
                        status = 4;
                        break;

                    default:
                        status = 0;
                }
            }
        }
        return status;
    }

    /**
     * Resets the password and send it to the user via email and store it into DB
     *
     * @param username Username of the forgotten password
     */
    public void resetPassword(String username) {

    }
}

