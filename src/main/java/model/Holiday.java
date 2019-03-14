package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Holiday entity.
 * An instance of this class is used to store data of t_holiday_details table.
 *
 * @author Avik Sarkar
 */
public class Holiday {

    /*--------------------------------Initialization of variables----------------------------------*/

    private SimpleStringProperty holidayId;
    private SimpleStringProperty holidayName;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;

    /*------------------------------------End of Initialization-------------------------------------*/

    /**
     * Default public constructor to initialize data.
     */
    public Holiday() {

        this.holidayId = new SimpleStringProperty("");
        this.holidayName = new SimpleStringProperty("");
        this.startDate = new SimpleStringProperty("");
        this.endDate = new SimpleStringProperty("");
    }

    /**
     * Getter method to get the holidayId.
     *
     * @return The holidayId.
     */
    public String getHolidayId() {

        return this.holidayId.get();
    }

    /**
     * Setter method to set holidayId.
     *
     * @param id The holidayId to set.
     */
    public void setHolidayId(String id) {

        this.holidayId.set(id);
    }

    /**
     * Getter method to get the holidayName.
     *
     * @return The holidayName.
     */
    public String getHolidayName() {
        return this.holidayName.get();
    }

    /**
     * Setter method to set holidayName.
     *
     * @param name The holidayName to set.
     */
    public void setHolidayName(String name) {

        this.holidayName.set(name);
    }

    /**
     * Getter method to get the startDate.
     *
     * @return The startDate.
     */
    public String getStartDate() {

        return this.startDate.get();
    }

    /**
     * Setter method to set startDate.
     *
     * @param startDate The startDate to set.
     */
    public void setStartDate(String startDate) {

        this.startDate.set(startDate);
    }

    /**
     * Getter method to get the endDate.
     *
     * @return The endDate.
     */
    public String getEndDate() {

        return this.endDate.get();
    }

    /**
     * Setter method to set endDate.
     *
     * @param endDate The endDate to set.
     */
    public void setEndDate(String endDate) {

        this.endDate.set(endDate);
    }
}
