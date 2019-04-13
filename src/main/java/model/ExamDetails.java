package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExamDetails{

    private SimpleStringProperty examDetailsId;
    private SimpleStringProperty examType;
    private SimpleStringProperty semesterType;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;
    private SimpleStringProperty startTime;
    private SimpleStringProperty endTime;
    private SimpleBooleanProperty isExamOnSaturday;
    private SimpleStringProperty academicYear;

    public ExamDetails() {

        this.examDetailsId = new SimpleStringProperty("");
        this.examType = new SimpleStringProperty("");
        this.semesterType = new SimpleStringProperty("");
        this.startDate = new SimpleStringProperty("");
        this.endDate = new SimpleStringProperty("");
        this.startTime = new SimpleStringProperty("");
        this.endTime = new SimpleStringProperty("");
        this.isExamOnSaturday = new SimpleBooleanProperty(false);
        this.academicYear = new SimpleStringProperty("");
    }

    public String getExamDetailsId() {
        return examDetailsId.get();
    }

    public void setExamDetailsId(String examDetailsId) {
        this.examDetailsId.set(examDetailsId);
    }

    public String getSemesterType() {
        return semesterType.get();
    }

    public void setSemesterType(String semesterType) {
        this.semesterType.set(semesterType);
    }

    public String getExamType() {
        return examType.get();
    }

    public void setExamType(String examType) {
        this.examType.set(examType);
    }

    public String getStartDate() {
        return startDate.get();
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getEndDate() {
        return endDate.get();
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public String getStartTime() {
        return startTime.get();
    }


    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    public String getEndTime() {
        return endTime.get();
    }


    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    public boolean getIsExamOnSaturday() {
        return isExamOnSaturday.get();
    }


    public void setIsExamOnSaturday(boolean isExamOnSaturday) {
        this.isExamOnSaturday.set(isExamOnSaturday);
    }

    public String getAcademicYear() {
        return academicYear.get();
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear.set(academicYear);
    }
}
