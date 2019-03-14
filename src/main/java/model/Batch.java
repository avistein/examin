package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Batch entity.
 * An instance of this class is used to store data of t_batch & t_course table.
 *
 * @author Avik Sarkar
 */
public class Batch extends Course {

    /*--------------------------------Initialization of variables----------------------------------*/

    private SimpleStringProperty batchId;
    private SimpleStringProperty batchName;

    /*------------------------------------End of Initialization------------------------------------*/

    /**
     * Default public constructor to initialize data.
     */
    public Batch() {

        this.batchId = new SimpleStringProperty("");
        this.batchName = new SimpleStringProperty("");
    }

    /**
     * Getter method to get the Batch ID.
     *
     * @return The batch ID.
     */
    public String getBatchId() {

        return this.batchId.get();
    }

    /**
     * Setter method to set batchId.
     *
     * @param batchId The discipline to set.
     */
    public void setBatchId(String batchId) {

        this.batchId.set(batchId);
    }

    /**
     * Getter method to get the Batch Name.
     *
     * @return The batch name.
     */
    public String getBatchName() {

        return this.batchName.get();
    }

    /**
     * Setter method to set batchName.
     *
     * @param batchName The batchName to set.
     */
    public void setBatchName(String batchName) {

        this.batchName.set(batchName);
    }

}
