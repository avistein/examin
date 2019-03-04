package command;

import model.User;
import org.mindrot.jbcrypt.BCrypt;

import static util.ConstantsUtil.*;

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


        int gid;

        gid = Integer.parseInt(user.getGid());

        if (BCrypt.checkpw(inputPassword, user.getPassword())) {

            switch (gid) {

                case 1:
                    return ADMIN_GID;

                case 2:
                    return EXAM_CELL_MEMBER_GID;

                case 3:
                    return PROFESSOR_HOD_GID;

                case 4:
                    return PROFESSOR_GID;

            }
        }
        return LOGIN_ERROR;
    }

    /**
     * Resets the password and send it to the user via email and store it into DB
     *
     * @param username Username of the forgotten password
     */
    public void resetPassword(String username) {

    }
}

