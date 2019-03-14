package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Course entity.
 * An instance of this class is used to store data of t_exam_cell_member table.
 *
 * @author Avik Sarkar
 */
public class ExamCellMember {

    /*--------------------------------Initialization of variables----------------------------------*/

    private SimpleStringProperty empId;
    private SimpleStringProperty firstName;
    private SimpleStringProperty middleName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty dob;
    private SimpleStringProperty doj;
    private SimpleStringProperty email;
    private SimpleStringProperty address;
    private SimpleStringProperty contactNo;

    /*------------------------------------End of Initialization-------------------------------------*/

    /**
     * Default public constructor to initialize data.
     */
    public ExamCellMember() {

        this.empId = new SimpleStringProperty("");
        this.firstName = new SimpleStringProperty("");
        this.middleName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.dob = new SimpleStringProperty("");
        this.doj = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.address = new SimpleStringProperty("");
        this.contactNo = new SimpleStringProperty("");
    }

    /**
     * Getter method to get the empId.
     *
     * @return The empId.
     */
    public String getEmpId() {

        return empId.get();
    }

    /**
     * Setter method to set empId.
     *
     * @param empId The empId to set.
     */
    public void setEmpId(String empId) {

        this.empId.set(empId);
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

}
