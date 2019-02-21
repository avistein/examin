package model;

import javafx.beans.property.SimpleStringProperty;

public class Student {

    private final SimpleStringProperty firstName;
    private final SimpleStringProperty middleName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty dob;
    private final SimpleStringProperty gender;
    private final SimpleStringProperty regYear;
    private final SimpleStringProperty email;
    private final SimpleStringProperty address;
    private final SimpleStringProperty motherName;
    private final SimpleStringProperty guardianContactNo;
    private final SimpleStringProperty regId;
    private final SimpleStringProperty rollNo;
    private final SimpleStringProperty contactNo;
    private final SimpleStringProperty guardianName;

    public Student(String fName, String mName, String lName,String dob, String gender,
                   String regYear, String email, String address,  String motherName,
                   String guardianContactNo, String regId, String rollNo,
                   String contactNo, String guardianName ){
        this.firstName = new SimpleStringProperty(fName);
        this.middleName= new SimpleStringProperty(mName);
        this.lastName = new SimpleStringProperty(lName);
        this.dob = new SimpleStringProperty(dob);
        this.gender = new SimpleStringProperty(gender);
        this.regYear = new SimpleStringProperty(regYear);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
        this.motherName = new SimpleStringProperty(motherName);
        this.guardianContactNo = new SimpleStringProperty(guardianContactNo);
        this.regId = new SimpleStringProperty(regId);
        this.rollNo = new SimpleStringProperty(rollNo);
        this.contactNo = new SimpleStringProperty(contactNo);
        this.guardianName = new SimpleStringProperty(guardianName);
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

    public String getGender() {
        return this.gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getRegYear() {
        return this.regYear.get();
    }

    public void setRegYear(String regYear) {
        this.regYear.set(regYear);
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

    public String getMotherName() {
        return this.motherName.get();
    }

    public void setMotherName(String motherName) {
        this.motherName.set(motherName);
    }

    public String getGuardianContactNo() {
        return this.guardianContactNo.get();
    }

    public void setGuardianContactNo(String guardianContactNo) {
        this.guardianContactNo.set(guardianContactNo);
    }

    public String getRegId() {
        return regId.get();
    }

    public void setRegId(String regId) {
        this.regId.set(regId);
    }

    public String getRollNo() {
        return rollNo.get();
    }

    public void setRollNo(String rollNo) {
        this.rollNo.set(rollNo);
    }

    public String getContactNo() {
        return contactNo.get();
    }

    public void setContactNo(String contactNo) {
        this.contactNo.set(contactNo);
    }

    public String getGuardianName() {
        return guardianName.get();
    }

    public void setGuardianName(String gName) {
        guardianName.set(gName);
    }



}
