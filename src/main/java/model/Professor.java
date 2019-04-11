package model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;

/**
 * POJO class for Professor entity.
 * An instance of this class is used to store data of t_professor, t_prof_dept, t_department table.
 * That means details about a Professor is stored such as his/her department etc.
 *
 * @author Sourav Debnath
 */


public class Professor extends Department implements Serializable {

    /*--------------------------------Initialization of variables----------------------------------*/

    private SimpleStringProperty firstName;
    private SimpleStringProperty middleName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty dob;
    private SimpleStringProperty doj;
    private SimpleStringProperty email;
    private SimpleStringProperty address;
    private SimpleStringProperty contactNo;
    private SimpleStringProperty highestQualification;
    private SimpleStringProperty profId;
    private SimpleStringProperty hodStatus;
    private SimpleStringProperty designation;
    private SimpleListProperty<Subject> subjects;

    /*------------------------------------End of Initialization-------------------------------------*/


    /**
     * Default public constructor to initialize data.
     */
    public Professor() {
        this.firstName = new SimpleStringProperty("");
        this.middleName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.dob = new SimpleStringProperty("");
        this.doj = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.contactNo = new SimpleStringProperty("");
        this.highestQualification = new SimpleStringProperty("");
        this.profId = new SimpleStringProperty("");
        this.hodStatus = new SimpleStringProperty("");
        this.designation = new SimpleStringProperty("");
        this.subjects = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /**
     * Getter method to get the firstName.
     *
     * @return The firstName.
     */
    public String getFirstName() {

        return this.firstName.get();
    }

    /**
     * Setter method to set firstName.
     *
     * @param firstName The firstName to set.
     */
    public void setFirstName(String firstName) {

        this.firstName.set(firstName);
    }

    /**
     * Getter method to get the middleName.
     *
     * @return The middleName.
     */
    public String getMiddleName() {

        return this.middleName.get();
    }

    /**
     * Setter method to set middleName.
     *
     * @param middleName The middleName to set.
     */
    public void setMiddleName(String middleName) {

        this.middleName.set(middleName);
    }

    /**
     * Getter method to get the lastName.
     *
     * @return The lastName.
     */
    public String getLastName() {

        return this.lastName.get();
    }

    /**
     * Setter method to set lastName.
     *
     * @param lastName The lastName to set.
     */
    public void setLastName(String lastName) {

        this.lastName.set(lastName);
    }

    /**
     * Getter method to get the dob.
     *
     * @return The dob.
     */
    public String getDob() {

        return this.dob.get();
    }

    /**
     * Setter method to set dob.
     *
     * @param dob The dob to set.
     */
    public void setDob(String dob) {

        this.dob.set(dob);
    }

    /**
     * Getter method to get the doj.
     *
     * @return The doj.
     */
    public String getDoj() {

        return this.doj.get();
    }

    /**
     * Setter method to set doj.
     *
     * @param doj The doj to set.
     */
    public void setDoj(String doj) {

        this.doj.set(doj);
    }

    /**
     * Getter method to get the email.
     *
     * @return The email.
     */
    public String getEmail() {

        return this.email.get();
    }

    /**
     * Setter method to set email.
     *
     * @param email The email to set.
     */
    public void setEmail(String email) {

        this.email.set(email);
    }

    /**
     * Getter method to get the address.
     *
     * @return The address.
     */
    public String getAddress() {

        return this.address.get();
    }

    /**
     * Setter method to set address.
     *
     * @param address The address to set.
     */
    public void setAddress(String address) {

        this.address.set(address);
    }

    /**
     * Getter method to get the contactNo.
     *
     * @return The contactNo.
     */
    public String getContactNo() {

        return contactNo.get();
    }

    /**
     * Setter method to set contactNo.
     *
     * @param contactNo The contactNo to set.
     */
    public void setContactNo(String contactNo) {

        this.contactNo.set(contactNo);
    }

    /**
     * Getter method to get the highestQualification.
     *
     * @return The highestQualification.
     */
    public String getHighestQualification() {

        return highestQualification.get();
    }

    /**
     * Setter method to set highestQualification.
     *
     * @param hq The highestQualification to set.
     */
    public void setHighestQualification(String hq) {

        this.highestQualification.set(hq);
    }

    /**
     * Getter method to get the profId.
     *
     * @return The profId.
     */
    public String getProfId() {

        return profId.get();
    }

    /**
     * Setter method to set profId.
     *
     * @param profId The profId to set.
     */
    public void setProfId(String profId) {

        this.profId.set(profId);
    }

    /**
     * Getter method to get the hodStatus.
     *
     * @return The hodStatus.
     */
    public String getHodStatus() {

        return this.hodStatus.get();
    }

    /**
     * Setter method to set hodStatus.
     *
     * @param status The hodStatus to set.
     */
    public void setHodStatus(String status) {

        this.hodStatus.set(status);
    }

    public String getDesignation() {
        return designation.get();
    }

    public void setDesignation(String designation) {
        this.designation.set(designation);
    }

    public ObservableList<Subject> getSubjects() {
        return subjects.get();
    }

    public void setSubjects(ObservableList<Subject> subjects) {
        this.subjects.set(subjects);
    }
}
