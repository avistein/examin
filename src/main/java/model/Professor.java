package model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serializable;

/**
 * POJO class for Professor entity.
 *
 * @author Sourav Debnath
 */



public class Professor extends Department implements Serializable {

    private SimpleStringProperty firstName;
    private SimpleStringProperty middleName;
    private SimpleStringProperty lastName ;
    private SimpleStringProperty dob;
    private SimpleStringProperty doj;
    private SimpleStringProperty email;
    private SimpleStringProperty address;
    private SimpleStringProperty contactNo;
    private SimpleStringProperty department;
    private SimpleStringProperty highestQualification;
    private SimpleStringProperty profId;
    private SimpleBooleanProperty hodStatus;
    //private SimpleListProperty<String> subjects;


    public Professor(){
        this.firstName = new SimpleStringProperty("");
        this.middleName= new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.dob = new SimpleStringProperty("");
        this.doj = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.contactNo = new SimpleStringProperty("");
        this.department = new SimpleStringProperty("");
        this.highestQualification = new SimpleStringProperty("");
        this.profId = new SimpleStringProperty("");
        this.hodStatus = new SimpleBooleanProperty();
    }


    public String getFirstName() {
        return this.firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getMiddleName() {
        return this.middleName.get();
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public String getLastName() {
        return this.lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getDob() {
        return this.dob.get();
    }

    public void setDob(String dob) {
        this.dob.set(dob);
    }

    public String getDoj() {
        return this.doj.get();
    }

    public void setDoj(String doj) {
        this.doj.set(doj);
    }

    public String getEmail() {
        return this.email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getAddress() {
        return this.address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getContactNo() {
        return contactNo.get();
    }

    public void setContactNo(String contactNo) {
        this.contactNo.set(contactNo);
    }

    public String getDepartment() { return department.get(); }

    public void setDepartment(String deptName) { this.department.set(deptName);}

    public String getHighestQualification() { return highestQualification.get(); }

    public void setHighestQualification(String hq) { this.highestQualification.set(hq); }

    public String getProfId() { return profId.get(); }

    public void setProfId(String profId) { this.profId.set(profId); }

    public Boolean getHodStatus() { return this.hodStatus.get(); }

    public void setHdStatus(Boolean status) { this.hodStatus.set(status); }

}
