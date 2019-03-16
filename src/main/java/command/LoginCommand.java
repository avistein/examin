package command;

import model.User;
import org.mindrot.jbcrypt.BCrypt;

import static util.ConstantsUtil.*;

/**
 * Command class to handle Login logic.
 * <p>
 * Command classes form the Business Logic layer in our application.
 *
 * @author Avik Sarkar
 */
public class LoginCommand {

    /**
     * Authenticates a user to enter the system.
     *
     * @param user          User to be authenticated.
     * @param inputPassword Password entered to login.
     * @return The status of the authentication.
     * @see <a href="http://www.mindrot.org/projects/jBCrypt">jBCrypt</a>
     */
    public int authenticateLogin(String inputPassword, User user) {

        //get the gid of the user
        int gid = Integer.parseInt(user.getGid());

        //if password matches ,return status corresponding to the gid
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

        //error if password doesn't match
        return LOGIN_ERROR;
    }
}

