package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Department entity.
 *
 * @author Sourav Debnath
 */


public class Department {

    private final SimpleStringProperty deptName;
    private final SimpleStringProperty buildingName;

    public Department(){

        this.deptName = new SimpleStringProperty("");
        this.buildingName= new SimpleStringProperty("");
    }

    public String getDeptName() {
        return this.deptName.get();
    }

    public void setDeptName(String deptName) {
        this.deptName.set(deptName);
    }

    public String getBuildingName() {
        return this.buildingName.get();
    }

    public void setBuildingName(String buildingName) {
        this.buildingName.set(buildingName);
    }

}
