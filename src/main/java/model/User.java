package model;

import javafx.beans.property.SimpleStringProperty;

public class User {

    private SimpleStringProperty userId;
    private SimpleStringProperty password;
    private SimpleStringProperty hashAlgo;
    private SimpleStringProperty gid;

    public User(String userId, String password, String hashAlgo, String gid){

        this.userId = new SimpleStringProperty(userId);
        this.password = new SimpleStringProperty(password);
        this.hashAlgo = new SimpleStringProperty(hashAlgo);
        this.gid = new SimpleStringProperty(gid);
    }

    public String getUserId(){
        return this.userId.get();
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public String getPassword(){
        return this.password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getHashAlgo(){
        return this.hashAlgo.get();
    }

    public void setHashAlgo(String hashAlgo) {
        this.hashAlgo.set(hashAlgo);
    }

    public String getGid(){
        return this.gid.get();
    }

    public void setGid(String gid) {
        this.gid.set(gid);
    }
}
