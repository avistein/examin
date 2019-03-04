package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.security.PublicKey;

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
    private SimpleBooleanProperty optStatus;


    public Subject() {

        this.subId = new SimpleStringProperty("");
        this.subName= new SimpleStringProperty("");
        this.credit = new SimpleStringProperty("");
        this.semester = new SimpleStringProperty("");
        this.subType = new SimpleStringProperty("");
        this.optStatus = new SimpleBooleanProperty();

    }


    public String getSubjectId() { return this.subId.get(); }

    public void setSubjectId(String subId) { this.subId.set(subId) ;}

    public String getSubjectName() { return this.subName.get(); }

    public void setSubjectName(String subName) { this.subName.set(subName) ;}

    public String getCredit() { return this.credit.get(); }

    public void setCredit(String credit) { this.credit.set(credit) ;}

    public String getSemester() { return this.semester.get(); }

    public void setSemester(String sem) { this.semester.set(sem) ;}

    public String getSubjectType() { return this.subType.get(); }

    public void setSubjectType(String subType) { this.subType.set(subType) ;}

    public Boolean getOptionalStatus() { return this.optStatus.get(); }

    public void setptionalStatus(Boolean optStatus) { this.optStatus.set(optStatus) ;}

}
