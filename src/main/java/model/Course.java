package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * POJO class for Course entity.
 * An instance of this class is used to store data of t_course and t_department table.
 *
 * @author Avik Sarkar
 */
public class Course extends Department {

    /*--------------------------------Initialization of variables----------------------------------*/

    private SimpleStringProperty discipline;
    private SimpleStringProperty degree;
    private SimpleStringProperty courseId;
    private SimpleStringProperty duration;

    /*------------------------------------End of Initialization-------------------------------------*/

    /**
     * Default public constructor to initialize data.
     */
    public Course() {

        this.discipline = new SimpleStringProperty("");
        this.degree = new SimpleStringProperty("");
        this.courseId = new SimpleStringProperty("");
        this.duration = new SimpleStringProperty("");
    }

    /**
     * Getter method to get the discipline.
     *
     * @return The discipline.
     */
    public String getDiscipline() {

        return this.discipline.get();
    }

    /**
     * Setter method to set discipline.
     *
     * @param discipline The discipline to set.
     */
    public void setDiscipline(String discipline) {

        this.discipline.set(discipline);
    }

    /**
     * Getter method to get the degree.
     *
     * @return The degree.
     */
    public String getDegree() {

        return this.degree.get();
    }

    /**
     * Setter method to set degree.
     *
     * @param degree The degree to set.
     */
    public void setDegree(String degree) {

        this.degree.set(degree);
    }

    /**
     * Getter method to get the courseId.
     *
     * @return The courseId.
     */
    public String getCourseId() {

        return this.courseId.get();
    }

    /**
     * Setter method to set courseId.
     *
     * @param courseId The courseId to set.
     */
    public void setCourseId(String courseId) {

        this.courseId.set(courseId);
    }

    /**
     * Getter method to get the duration.
     *
     * @return The duration.
     */
    public String getDuration() {

        return this.duration.get();
    }

    /**
     * Setter method to set duration.
     *
     * @param duration The courseId to set.
     */
    public void setDuration(String duration) {

        this.duration.set(duration);
    }

}

