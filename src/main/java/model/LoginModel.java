package model;

import database.DatabaseHelper;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        Connection conn = databaseHelper.openConnection();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            String retrievedPassword = null;

            int gid = 0;

            while (rs.next()) {
                retrievedPassword = rs.getString("v_pass");
                gid = Integer.parseInt(rs.getString("v_gid"));
            }

            if (retrievedPassword != null) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            databaseHelper.closeConnection(conn);
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

