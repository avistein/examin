package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Classroom entity.
 * An instance of this class is used to store data of t_classroom table.
 *
 * @author Sourav Debnath
 */

public class Classroom {

    /*--------------------------------Initialization of variables----------------------------------*/

    private SimpleStringProperty roomNo;
    private SimpleStringProperty capacity;
    private SimpleStringProperty noOfRows;
    private SimpleStringProperty noOfCols;

    /*------------------------------------End of Initialization-------------------------------------*/


    /**
     * Default public constructor to initialize data.
     */
    public Classroom() {

        this.roomNo = new SimpleStringProperty("");
        this.capacity = new SimpleStringProperty("");
        this.noOfRows = new SimpleStringProperty("");
        this.noOfCols = new SimpleStringProperty("");
    }


    /**
     * Getter method to get the room no.
     *
     * @return The roomNo.
     */
    public String getRoomNo() {

        return this.roomNo.get();
    }

    /**
     * Setter method to set room no.
     *
     * @param room The room no to set.
     */
    public void setRoomNo(String room) {

        this.roomNo.set(room);
    }

    /**
     * Getter method to get the room capacity.
     *
     * @return The capacity.
     */
    public String getCapacity() {

        return this.capacity.get();
    }

    /**
     * Setter method to set room capacity.
     *
     * @param roomCapacity The room capacity to set.
     */
    public void setCapacity(String roomCapacity) {

        this.capacity.set(roomCapacity);
    }

    /**
     * Getter method to get the no of rows.
     *
     * @return The no of rows.
     */
    public String getNoOfRows() {

        return noOfRows.get();
    }

    /**
     * Setter method to set no of rows.
     *
     * @param noOfRows The no of row to set.
     */
    public void setNoOfRows(String noOfRows) {

        this.noOfRows.set(noOfRows);
    }

    /**
     * Getter method to get the no of column.
     *
     * @return The no of columns.
     */
    public String getNoOfCols() {

        return noOfCols.get();
    }

    /**
     * Setter method to set no of column.
     *
     * @param noOfCols The no of column to set.
     */
    public void setNoOfCols(String noOfCols) {

        this.noOfCols.set(noOfCols);
    }
}
