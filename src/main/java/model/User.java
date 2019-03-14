package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for User entity.
 * An instance of this class is used to store data of t_login_details table.
 *
 * @author Avik Sarkar
 */
public class User {

    /*--------------------------------Initialization of variables----------------------------------*/

    private SimpleStringProperty userId;
    private SimpleStringProperty password;
    private SimpleStringProperty hashAlgo;
    private SimpleStringProperty gid;

    /*------------------------------------End of Initialization-------------------------------------*/

    /**
     * Default public constructor to initialize data.
     */
    public User() {

        this.userId = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
        this.hashAlgo = new SimpleStringProperty("");
        this.gid = new SimpleStringProperty("");
    }

    /**
     * Getter method to get the userId.
     *
     * @return The userId.
     */
    public String getUserId() {

        return this.userId.get();
    }

    /**
     * Setter method to set userId.
     *
     * @param userId The userId to set.
     */
    public void setUserId(String userId) {

        this.userId.set(userId);
    }

    /**
     * Getter method to get the password.
     *
     * @return The password.
     */
    public String getPassword() {

        return this.password.get();
    }

    /**
     * Setter method to set password.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {

        this.password.set(password);
    }

    /**
     * Getter method to get the hashing Algorithm used.
     *
     * @return The hashing Algorithm used.
     */
    public String getHashAlgo() {

        return this.hashAlgo.get();
    }

    /**
     * Setter method to set the hashing algorithm.
     *
     * @param hashAlgo The hashing algorithm to set.
     */
    public void setHashAlgo(String hashAlgo) {

        this.hashAlgo.set(hashAlgo);
    }

    /**
     * Getter method to get the gid.
     *
     * @return The gid.
     */
    public String getGid() {

        return this.gid.get();
    }

    /**
     * Setter method to set the gid.
     *
     * @param gid The gid to set.
     */
    public void setGid(String gid) {

        this.gid.set(gid);
    }
}
