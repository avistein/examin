package model;

import javafx.beans.property.SimpleStringProperty;

public class ExamCellMember {

    private SimpleStringProperty empId;
    private SimpleStringProperty firstName;
    private SimpleStringProperty middleName;
    private SimpleStringProperty lastName ;
    private SimpleStringProperty dob;
    private SimpleStringProperty doj;
    private SimpleStringProperty email;
    private SimpleStringProperty address;
    private SimpleStringProperty contactNo;

    public ExamCellMember(){
        this.empId = new SimpleStringProperty("");
        this.firstName = new SimpleStringProperty("");
        this.middleName= new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.dob = new SimpleStringProperty("");
        this.doj = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.contactNo = new SimpleStringProperty("");
    }

    public String getEmpId() {
        return empId.get();
    }

    public void setEmpId(String empId) {
        this.empId.set(empId);
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

}
