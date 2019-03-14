package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Department entity.
 * An instance of this class is used to store data of t_department table.
 *
 * @author Sourav Debnath
 */
public class Department {

    /*--------------------------------Initialization of variables----------------------------------*/

    private final SimpleStringProperty deptName;
    private final SimpleStringProperty buildingName;

    /*------------------------------------End of Initialization-------------------------------------*/

    /**
     * Default public constructor to initialize data.
     */
    public Department() {

        this.deptName = new SimpleStringProperty("");
        this.buildingName = new SimpleStringProperty("");
    }

    /**
     * Getter method to get the deptName.
     *
     * @return The deptName.
     */
    public String getDeptName() {

        return this.deptName.get();
    }

    /**
     * Setter method to set deptName.
     *
     * @param deptName The deptName to set.
     */
    public void setDeptName(String deptName) {

        this.deptName.set(deptName);
    }

    /**
     * Getter method to get the buildingName.
     *
     * @return The buildingName.
     */
    public String getBuildingName() {
        return this.buildingName.get();
    }

    /**
     * Setter method to set buildingName.
     *
     * @param buildingName The buildingName to set.
     */
    public void setBuildingName(String buildingName) {

        this.buildingName.set(buildingName);
    }

}
