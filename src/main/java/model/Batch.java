package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Batch entity.
 *
 * @author Avik Sarkar
 */
public class Batch extends Course {

    private SimpleStringProperty batchId;
    private SimpleStringProperty batchName;

    public Batch(){
        this.batchId = new SimpleStringProperty("");
        this.batchName = new SimpleStringProperty("");
    }

    public Batch(String batchId, String courseId, String batchName,
                 String discipline, String degree, String duration, String deptName){

        super(discipline, degree, courseId, duration, deptName);
        this.batchId = new SimpleStringProperty(batchId);
        this.batchName = new SimpleStringProperty(batchName);
    }



    public String getBatchId(){
        return this.batchId.get();
    }

    public void setBatchId(String batchId) {
        this.batchId.set(batchId);
    }

    public String getBatchName(){
        return this.batchName.get();
    }

    public void setBatchName(String batchName) {
        this.batchName.set(batchName);
    }

}
