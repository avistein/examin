package command;

import model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Service class to
 *
 * @author Avik Sarkar
 */
public class LoginCommand {


    public LoginCommand() {
    }

//    /**
//     * Authenticates a user to enter the system
//     *
//     * @param username      Username entered to login
//     * @param inputPassword Password entered to login
//     * @return The status of the authentication
//     * @link http://www.mindrot.org/projects/jBCrypt
//     */
    public int authenticateLogin(String inputPassword, User user) {


        int status = 0;

        int gid;

            gid = Integer.parseInt(user.getGid());

            if (BCrypt.checkpw(inputPassword, user.getPassword())) {

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

