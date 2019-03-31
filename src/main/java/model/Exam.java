package model;

import javafx.beans.property.SimpleStringProperty;

public class Exam extends Subject {

    private SimpleStringProperty examDetailId;
    private SimpleStringProperty examId;
    private SimpleStringProperty examDate;
    private SimpleStringProperty startTime;
    private  SimpleStringProperty endTime;

    public Exam() {

        this.examDetailId = new SimpleStringProperty("");
        this.examId = new SimpleStringProperty("");
        this.examDate = new SimpleStringProperty("");
        this.startTime = new SimpleStringProperty("");
        this.endTime = new SimpleStringProperty("");
    }

    public String getExamDetailId() {
        return examDetailId.get();
    }

    public void setExamDetailId(String examDetailId) {
        this.examDetailId.set(examDetailId);
    }

    public String getExamId() {
        return examId.get();
    }


    public void setExamId(String examId) {
        this.examId.set(examId);
    }

    public String getExamDate() {
        return examDate.get();
    }

    public void setExamDate(String examDate) {
        this.examDate.set(examDate);
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
}
