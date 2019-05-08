package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/**
 * POJO class for Student entity.
 * An instance of this class is used to store data of t_student, t_student_enrollment_details, t_course, t_department
 * table.
 * That means details about a Student is stored such as his/her course, his/her batch , his/her department etc.
 *
 * @author Avik Sarkar
 */
public class Student extends Batch implements Serializable {

    /*--------------------------------Initialization of variables----------------------------------*/

    private SimpleStringProperty firstName;
    private SimpleStringProperty middleName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty dob;
    private SimpleStringProperty gender;
    private SimpleStringProperty regYear;
    private SimpleStringProperty email;
    private SimpleStringProperty address;
    private SimpleStringProperty motherName;
    private SimpleStringProperty guardianContactNo;
    private SimpleStringProperty regId;
    private SimpleStringProperty rollNo;
    private SimpleStringProperty contactNo;
    private SimpleStringProperty guardianName;
    private SimpleStringProperty currSemester;
    private SimpleStringProperty profileImagePath;
    private SimpleBooleanProperty selected;

    /*------------------------------------End of Initialization-------------------------------------*/

    /**
     * Default public constructor to initialize data.
     */
    public Student() {

        this.firstName = new SimpleStringProperty("");
        this.middleName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.dob = new SimpleStringProperty("");
        this.gender = new SimpleStringProperty("");
        this.regYear = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.motherName = new SimpleStringProperty("");
        this.guardianContactNo = new SimpleStringProperty("");
        this.regId = new SimpleStringProperty("");
        this.rollNo = new SimpleStringProperty("");
        this.contactNo = new SimpleStringProperty("");
        this.guardianName = new SimpleStringProperty("");
        this.currSemester = new SimpleStringProperty("");
        this.profileImagePath = new SimpleStringProperty("");
        this.selected = new SimpleBooleanProperty(false);
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
     * Getter method to get the gender.
     *
     * @return The gender.
     */
    public String getGender() {

        return this.gender.get();
    }

    /**
     * Setter method to set gender.
     *
     * @param gender The gender to set.
     */
    public void setGender(String gender) {

        this.gender.set(gender);
    }

    /**
     * Getter method to get the regYear.
     *
     * @return The regYear.
     */
    public String getRegYear() {

        return this.regYear.get();
    }

    /**
     * Setter method to set regYear.
     *
     * @param regYear The regYear to set.
     */
    public void setRegYear(String regYear) {

        this.regYear.set(regYear);
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
     * Getter method to get the motherName.
     *
     * @return The motherName.
     */
    public String getMotherName() {

        return this.motherName.get();
    }

    /**
     * Setter method to set motherName.
     *
     * @param motherName The motherName to set.
     */
    public void setMotherName(String motherName) {

        this.motherName.set(motherName);
    }

    /**
     * Getter method to get the guardianContactNo.
     *
     * @return The guardianContactNo.
     */
    public String getGuardianContactNo() {

        return this.guardianContactNo.get();
    }

    /**
     * Setter method to set guardianContactNo.
     *
     * @param guardianContactNo The guardianContactNo to set.
     */
    public void setGuardianContactNo(String guardianContactNo) {

        this.guardianContactNo.set(guardianContactNo);
    }

    /**
     * Getter method to get the regId.
     *
     * @return The regId.
     */
    public String getRegId() {

        return regId.get();
    }

    /**
     * Setter method to set regId.
     *
     * @param regId The regId to set.
     */
    public void setRegId(String regId) {

        this.regId.set(regId);
    }

    /**
     * Getter method to get the rollNo.
     *
     * @return The rollNo.
     */
    public String getRollNo() {

        return rollNo.get();
    }

    /**
     * Setter method to set rollNo.
     *
     * @param rollNo The rollNo to set.
     */
    public void setRollNo(String rollNo) {

        this.rollNo.set(rollNo);
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
     * Getter method to get the guardianName.
     *
     * @return The guardianName.
     */
    public String getGuardianName() {

        return guardianName.get();
    }

    /**
     * Setter method to set guardianName.
     *
     * @param gName The guardianName to set.
     */
    public void setGuardianName(String gName) {

        guardianName.set(gName);
    }

    /**
     * Getter method to get the currSemester.
     *
     * @return The currSemester.
     */
    public String getCurrSemester() {

        return currSemester.get();
    }

    /**
     * Setter method to set currSemester.
     *
     * @param currSemester The currSemester to set.
     */
    public void setCurrSemester(String currSemester) {

        this.currSemester.set(currSemester);
    }

    public SimpleStringProperty currSemesterProperty() {
        return currSemester;
    }

    /**
     * Getter method to get the file path of the Student's profile image.
     *
     * @return The file path.
     */
    public String getProfileImagePath() {

        return profileImagePath.get();
    }

    /**
     * Setter method to set path of the Student's profile image.
     *
     * @param path The file path to set.
     */
    public void setProfileImagePath(String path) {

        this.profileImagePath.set(path);
    }

    /**
     *
     * @return
     */
    public boolean isSelected() {
        return selected.get();
    }

    /**
     *
     * @return
     */
    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    /**
     *
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
