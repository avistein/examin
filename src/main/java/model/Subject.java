package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/**
 * POJO class for Subject entity.
 *
 * @author Sourav Debnath
 */
public class Subject extends Course implements Serializable {

    private SimpleStringProperty subId;
    private SimpleStringProperty subName;
    private SimpleStringProperty credit;
    private SimpleStringProperty semester;
    private SimpleStringProperty subType;
    private SimpleIntegerProperty optStatus;
    private SimpleStringProperty fullMarks;

    public Subject() {

        this.subId = new SimpleStringProperty("");
        this.subName= new SimpleStringProperty("");
        this.credit = new SimpleStringProperty("");
        this.semester = new SimpleStringProperty("");
        this.subType = new SimpleStringProperty("");
        this.optStatus = new SimpleIntegerProperty(0);
        this.fullMarks = new SimpleStringProperty("");
    }


    public String getSubId() {
        return this.subId.get();
    }

    public void setSubId(String subId) {
        this.subId.set(subId) ;
    }

    public String getSubName() {
        return this.subName.get();
    }

    public void setSubName(String subName) {
        this.subName.set(subName) ;
    }

    public String getCredit() {
        return this.credit.get();
    }

    public void setCredit(String credit) {
        this.credit.set(credit) ;
    }

    public String getSemester() {
        return this.semester.get();
    }

    public void setSemester(String sem) {
        this.semester.set(sem) ;
    }

    public String getSubType() {
        return this.subType.get();
    }

    public void setSubType(String subType) {
        this.subType.set(subType) ;
    }

    public int getOptStatus() {
        return this.optStatus.get();
    }

    public void setOptStatus(int optStatus) {
        this.optStatus.set(optStatus) ;
    }

    public String  getFullMarks() {
        return this.fullMarks.get();
    }

    public void setFullMarks(String fullMarks) {
        this.fullMarks.set(fullMarks) ;
    }
}
