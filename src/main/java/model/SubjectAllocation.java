package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Subject Allocation entity.
 * An instance of this class is used to store SUBJECT ALLOCATION data for the t_prof_sub table in the database.
 *
 * @author Avik Sarkar
 */
public class SubjectAllocation extends Subject{

    /*-----------------------------------------Declaration of variables-----------------------------------------------*/

    private SimpleStringProperty profId;

    /*----------------------------------------End of Declaration of variables-----------------------------------------*/


    /**
     * Default public constructor to initialize the data.
     */
    public SubjectAllocation(){

        this.profId = new SimpleStringProperty("");
    }

    /**
     * Getter method to get the prof ID who teaches this subject.
     *
     * @return Professor ID of the professor.
     */
    public String getProfId() {
        return profId.get();
    }

    /**
     * Setter method to assign the subject with a Professor ID.
     *
     * @param profId The ID of the professor.
     */
    public void setProfId(String profId) {
        this.profId.set(profId);
    }
}
