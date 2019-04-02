package model;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/**
 * POJO class for Subject entity.
 *
 * @author Sourav Debnath
 */
public class Subject extends Course implements Serializable {

    /*--------------------------------Initialization of variables----------------------------------*/

    private SimpleStringProperty subId;
    private SimpleStringProperty subName;
    private SimpleStringProperty credit;
    private SimpleStringProperty semester;
    private SimpleStringProperty subType;
    private SimpleStringProperty fullMarks;

    /*------------------------------------End of Initialization------------------------------------*/

    /**
     * Default public constructor to initialize data.
     */
    public Subject() {

        this.subId = new SimpleStringProperty("");
        this.subName = new SimpleStringProperty("");
        this.credit = new SimpleStringProperty("");
        this.semester = new SimpleStringProperty("");
        this.subType = new SimpleStringProperty("");
        this.fullMarks = new SimpleStringProperty("");
    }

    /**
     * Getter method to get the Subject ID.
     *
     * @return The subject ID.
     */
    public String getSubId() {

        return this.subId.get();
    }

    /**
     * Setter method to set subjectId.
     *
     * @param subId The discipline to set.
     */
    public void setSubId(String subId) {

        this.subId.set(subId);
    }

    /**
     * Getter method to get the Subject Name.
     *
     * @return The subject Name.
     */
    public String getSubName() {

        return this.subName.get();
    }

    /**
     * Setter method to set subName.
     *
     * @param subName The discipline to set.
     */
    public void setSubName(String subName) {

        this.subName.set(subName);
    }

    /**
     * Getter method to get the Subject Credit.
     *
     * @return The Subject Credit.
     */
    public String getCredit() {

        return this.credit.get();
    }

    /**
     * Setter method to set credit.
     *
     * @param credit The discipline to set.
     */
    public void setCredit(String credit) {

        this.credit.set(credit);
    }

    /**
     * Getter method to get the Semester.
     *
     * @return The semester.
     */
    public String getSemester() {

        return this.semester.get();
    }

    /**
     * Setter method to set semester.
     *
     * @param sem The discipline to set.
     */
    public void setSemester(String sem) {

        this.semester.set(sem);
    }

    /**
     * Getter method to get the Subject Type.
     *
     * @return The Subject Type.
     */
    public String getSubType() {

        return this.subType.get();
    }

    /**
     * Setter method to set subjectType.
     *
     * @param subType The discipline to set.
     */
    public void setSubType(String subType) {

        this.subType.set(subType);
    }

    /**
     * Getter method to get the Full Marks.
     *
     * @return The Full Marks.
     */
    public String getFullMarks() {

        return this.fullMarks.get();
    }

    /**
     * Setter method to set fullMarks.
     *
     * @param fullMarks The discipline to set.
     */
    public void setFullMarks(String fullMarks) {

        this.fullMarks.set(fullMarks);
    }
}
