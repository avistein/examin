package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Department entity.
 *
 * @author Sourav Debnath
 */


public class Department {

    private final SimpleStringProperty deptName;
    private final SimpleStringProperty building;

    public Department(){

        this.deptName = new SimpleStringProperty("");
        this.building= new SimpleStringProperty("");
    }

    public String getDeptName() { return this.deptName.get(); }

    public void setDeptName(String dept) { this.deptName.set(dept); }

    public String getBuilding() { return this.building.get(); }

    public void setBuildinge(String build) { this.building.set(build); }

}
