package model;

import javafx.beans.property.SimpleStringProperty;

public class StudentEntity {

    private final SimpleStringProperty name;
    private final SimpleStringProperty regId;
    private final SimpleStringProperty rollNo;
    private final SimpleStringProperty contactNo;
    private final SimpleStringProperty guardianName;

    public StudentEntity(String name, String regId, String rollNo, String contactNo, String guardianName ){
        this.name = new SimpleStringProperty(name);
        this.regId = new SimpleStringProperty(regId);
        this.rollNo = new SimpleStringProperty(rollNo);
        this.contactNo = new SimpleStringProperty(contactNo);
        this.guardianName = new SimpleStringProperty(guardianName);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
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
