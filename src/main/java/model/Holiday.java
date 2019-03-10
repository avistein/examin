package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Holiday entity.
 *
 * @author Avik Sarkar
 */

public class Holiday {

    private SimpleStringProperty holidayId;
    private SimpleStringProperty holidayName;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;

    public Holiday() {

        this.holidayId = new SimpleStringProperty("");
        this.holidayName = new SimpleStringProperty("");
        this.startDate = new SimpleStringProperty("");
        this.endDate = new SimpleStringProperty("");
    }

    public String getHolidayId() {
        return this.holidayId.get();
    }

    public void setHolidayId(String id) {
        this.holidayId.set(id);
    }


    public String getHolidayName() {
        return this.holidayName.get();
    }

    public void setHolidayName(String name) {
        this.holidayName.set(name);
    }


    public String getStartDate() {
        return this.startDate.get();
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getEndDate() {
        return this.endDate.get();
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }
}
