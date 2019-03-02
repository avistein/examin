package service;

import model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

/**
 * Service class to
 *
 * @author Avik Sarkar
 */
public class LoginService {

    private UserService userService;

    public LoginService() {
        userService = new UserService();
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

        final String additionalQuery = "where v_user_id=?";

        int status = 0;
        String retrievedPassword ;

        int gid;

        List<User> users = userService.getLoginDetails(additionalQuery, username);

        if (!users.isEmpty()) {

            retrievedPassword = users.get(0).getPassword();
            gid = Integer.parseInt(users.get(0).getGid());

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

