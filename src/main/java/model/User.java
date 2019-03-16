package model;

/**
 * POJO class for User entity.
 * An instance of this class is used to store data of t_login_details table.
 *
 * @author Avik Sarkar
 */
public class User {

    /*--------------------------------Initialization of variables----------------------------------*/

    private String userId;
    private String password;
    private String hashAlgo;
    private String gid;
    private String lastLoginTimeStamp;
    private String name;
    private String email;

    /*------------------------------------End of Initialization-------------------------------------*/

    /**
     * Default public constructor to initialize data.
     */
    public User() {

        this.userId = "";
        this.password = "";
        this.hashAlgo = "";
        this.gid = "";
        this.lastLoginTimeStamp = "";
        this.name = "";
        this.email = "";
    }

    /**
     * Getter method to get the userId.
     *
     * @return The userId.
     */
    public String getUserId() {

        return this.userId;
    }

    /**
     * Setter method to set userId.
     *
     * @param userId The userId to set with.
     */
    public void setUserId(String userId) {

        this.userId = userId;
    }

    /**
     * Getter method to get the password.
     *
     * @return The password.
     */
    public String getPassword() {

        return this.password;
    }

    /**
     * Setter method to set password.
     *
     * @param password The password to set with.
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     * Getter method to get the hashing Algorithm used.
     *
     * @return The hashing Algorithm used.
     */
    public String getHashAlgo() {

        return this.hashAlgo;
    }

    /**
     * Setter method to set the hashing algorithm.
     *
     * @param hashAlgo The hashing algorithm to set with.
     */
    public void setHashAlgo(String hashAlgo) {

        this.hashAlgo = hashAlgo;
    }

    /**
     * Getter method to get the gid.
     *
     * @return The gid.
     */
    public String getGid() {

        return this.gid;
    }

    /**
     * Setter method to set the gid.
     *
     * @param gid The gid to set with.
     */
    public void setGid(String gid) {

        this.gid = gid;
    }

    /**
     * Getter method to get the timestamp of last successful login.
     *
     * @return The gid.
     */
    public String getLastLoginTimeStamp() {

        return this.lastLoginTimeStamp;
    }

    /**
     * Setter method to set the timestamp of last successful login.
     *
     * @param timeStamp The timestamp to set with.
     */
    public void setLastLoginTimeStamp(String timeStamp) {

        this.lastLoginTimeStamp = timeStamp;
    }

    /**
     * Getter method to get the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method to set the name of the user.
     *
     * @param name The name to set with.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method to get the email of the user.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Setter method to set the name of the user.
     *
     * @param email The email to set with.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
